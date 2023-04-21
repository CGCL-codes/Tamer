package org.gvsig.i18n.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * @author cesar
 *
 */
public class TranslationDatabase {

    public TranslationDatabase(ConfigOptions config) {
        this.config = config;
    }

    private ConfigOptions config;

    private HashMap dictionaries;

    public void load() {
        dictionaries = new HashMap();
        String lang;
        DoubleProperties dictionary;
        FileInputStream stream = null;
        for (int currentLang = 0; currentLang < config.languages.length; currentLang++) {
            lang = config.languages[currentLang];
            dictionary = new DoubleProperties();
            try {
                stream = new FileInputStream(config.databaseDir + File.separator + config.defaultBaseName + "_" + lang + ".properties");
                try {
                    dictionary.load(stream);
                } catch (IOException e) {
                    System.err.println("Error cargando la base de datos para el idioma: [" + lang + "]. " + e.getLocalizedMessage());
                }
            } catch (FileNotFoundException e) {
                System.err.println("Error cargando la base de datos para el idioma: [" + lang + "]. " + e.getLocalizedMessage());
            }
            dictionaries.put(lang, dictionary);
        }
    }

    public void save() {
        String lang;
        DoubleProperties dictionary;
        FileOutputStream stream = null;
        for (int currentLang = 0; currentLang < config.languages.length; currentLang++) {
            lang = config.languages[currentLang];
            dictionary = ((DoubleProperties) dictionaries.get(lang));
            try {
                stream = new FileOutputStream(config.databaseDir + File.separator + config.defaultBaseName + "_" + lang + ".properties");
                try {
                    dictionary.store(stream, "Translations for language: " + lang);
                } catch (IOException e) {
                    System.err.println("Error guardando la base de datos para el idioma: [" + lang + "]. " + e.getLocalizedMessage());
                }
            } catch (FileNotFoundException e) {
                System.err.println("Error guardando la base de datos para el idioma: [" + lang + "]. " + e.getLocalizedMessage());
            }
        }
    }

    public String getTranslation(String lang, String key) {
        if (lang == null || key == null) return null;
        DoubleProperties dictionary = (DoubleProperties) dictionaries.get(lang);
        if (dictionary == null) return null;
        return (String) dictionary.get(key);
    }

    public String setTranslation(String lang, String key, String translation) {
        if (lang == null || key == null) return null;
        DoubleProperties dictionary = (DoubleProperties) dictionaries.get(lang);
        if (dictionary == null) return null;
        String oldvalue = (String) dictionary.get(key);
        dictionary.put(key, translation);
        return oldvalue;
    }

    /**
	 * Removes the key from the specified dictionary, and its associated translation.
	 * It has no effect if the key was not present in the dictionary.
	 * 
	 * @param lang The language from which the key should be removed.
	 * @param key  The key to be removed.
	 * @return The translation associated with the key, or null if the
	 * key was not present in the dictionary. It also returns null if any of the parameters is
	 * null, or if there was no dictionary for the specified language.
	 */
    public String removeTranslation(String lang, String key) {
        if (lang == null || key == null) return null;
        DoubleProperties dictionary = (DoubleProperties) dictionaries.get(lang);
        if (dictionary == null) return null;
        String oldvalue = (String) dictionary.get(key);
        dictionary.remove(key);
        return oldvalue;
    }

    /**
	 * Removes the key and its associated translation from all the dictionaries.
	 * The key will be deleted from the dictionaries in which it is present (if any).
	 * 
	 * @param key  The key to be removed.
	 * @return True if the key was removed from any dictionary, or false if the key
	 * was not present in any dictionary.
	 * @throws NullPointerException if the key is null.
	 */
    public boolean removeTranslation(String key) throws NullPointerException {
        DoubleProperties dictionary;
        String lang;
        boolean present = false;
        Set keys = dictionaries.keySet();
        Iterator langIterator = keys.iterator();
        while (langIterator.hasNext()) {
            lang = (String) langIterator.next();
            dictionary = (DoubleProperties) dictionaries.get(lang);
            if (dictionary.containsKey(key)) {
                present = true;
                dictionary.remove(key);
            }
        }
        return present;
    }

    public boolean containsLanguage(String lang) {
        return dictionaries.containsKey(lang);
    }

    public boolean containsKey(String lang, String key) {
        if (lang == null || key == null) return false;
        DoubleProperties dictionary = (DoubleProperties) dictionaries.get(lang);
        return dictionary.containsKey(key);
    }

    public String getAssociatedKey(String lang, String value) {
        if (lang == null || value == null) return null;
        DoubleProperties dictionary = (DoubleProperties) dictionaries.get(lang);
        return dictionary.getAssociatedKey(value);
    }

    public ArrayList getAssociatedKeys(String lang, String value) {
        if (lang == null || value == null) return null;
        DoubleProperties dictionary = (DoubleProperties) dictionaries.get(lang);
        return dictionary.getAssociatedKeys(value);
    }

    public boolean containsTranslation(String lang, String translation) {
        if (lang == null || translation == null) return false;
        DoubleProperties dictionary = (DoubleProperties) dictionaries.get(lang);
        return dictionary.containsValue(translation);
    }
}
