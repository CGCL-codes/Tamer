package net.walkingtools.international;

import java.util.*;

/**
 * The Translation Interface specifies methods describing the name
 * of the language being translated to, its code, and the translate(String) method that
 * translates a string from the HiperGps Gui into any implemented language.
 * Objects of the Translation type are held by the Languages class in this package.
 * @author Brett Stalbaum
 * @version 0.1.1
 * @since 0.0.4
 */
public abstract class Translation implements Comparable<Translation> {

    /**
     * Hashtable containing the translated strings (to be instantiated in sublclasses
     */
    protected Hashtable<String, String> translation = null;

    /**
     * The ISO 639-1 language code for the language
     * (http://en.wikipedia.org/wiki/List_of_ISO_639-1_codes)
     * @return the ISO 639-1 code for the language
     */
    public abstract String getCode();

    /**
     * The language name
     * @return the language name
     */
    public abstract String getName();

    /**
     * The labels for the Column
     * @return the Column Names String
     */
    public abstract String[] getColumnNames();

    /**
     * Translates a UI string into the string of any implemented languge
     * @param key the string to translate
     * @return the translated string
     */
    public String translate(String key) {
        String trans = translation.get(key);
        if (trans != null) {
            return trans;
        } else {
            System.err.println("warn: translation for \"" + key + "\" not implemented in " + getName());
            return key;
        }
    }

    /**
     * Gets the translation's locale
     * @return the locale for this language
     */
    public abstract Locale getLocale();

    public int compareTo(Translation o) {
        return getName().compareTo(o.getName());
    }
}
