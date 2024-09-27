package org.translation;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;

/**
 * An implementation of the Translator interface which reads in the translation
 * data from a JSON file. The data is read in once each time an instance of this class is constructed.
 */
public class JSONTranslator implements Translator {

    // TODO Task: pick appropriate instance variables for this class
    private final HashMap<String, HashMap<String, String>> countryLanguageMap = new HashMap<>();
    private final ArrayList<String> countryCodes = new ArrayList<>();
    /**
     * Constructs a JSONTranslator using data from the sample.json resources file.
     */

    public JSONTranslator() {
        this("sample.json");
    }

    /**
     * Constructs a JSONTranslator populated using data from the specified resources file.
     * @param filename the name of the file in resources to load the data from
     * @throws RuntimeException if the resource file can't be loaded properly
     */
    public JSONTranslator(String filename) {
        // read the file to get the data to populate things...
        try {

            String jsonString = Files.readString(Paths.get(getClass()
                    .getClassLoader().getResource(filename).toURI()));

            JSONArray jsonArray = new JSONArray(jsonString);

            // TODO Task: use the data in the jsonArray to populate your instance variables
            //            Note: this will likely be one of the most substantial pieces of code you write in this lab.
            for (int i = 0; i < jsonArray.length(); i++) {
                var countryData = jsonArray.getJSONObject(i);

                String countryCode = countryData.getString("alpha3").toLowerCase();
                countryCodes.add(countryCode);

                HashMap<String, String> languageMap = new HashMap<>();
                for (String key : countryData.keySet()) {
                    if (!"alpha3".equals(key) && !"id".equals(key) && !"alpha2".equals(key)) {
                        languageMap.put(key.toLowerCase(), countryData.getString(key));
                    }
                }
                countryLanguageMap.put(countryCode, languageMap);
            }
        }
        catch (IOException | URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public List<String> getCountryLanguages(String country) {
        // TODO Task: return an appropriate list of language codes,
        //            but make sure there is no aliasing to a mutable object
        return new ArrayList<>(countryLanguageMap.keySet());
    }

    @Override
    public List<String> getCountries() {
        // TODO Task: return an appropriate list of country codes,
        //            but make sure there is no aliasing to a mutable object
        return new ArrayList<>(countryCodes);
    }

    @Override
    public String translate(String country, String language) {
        // TODO Task: complete this method using your instance variables as needed
        var languageMap = countryLanguageMap.get(country.toLowerCase());
        if (languageMap != null) {
            return languageMap.getOrDefault(language.toLowerCase(), "Translation not found");
        }
        return "Country not found";
    }
}
