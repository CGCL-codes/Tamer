package net.sf.locale4j.storage;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;

/**
 * In memory storage of locale data using HashTables.
 */
public class HashTableStore implements LocaleStore {

    /**
     * List of Hash Tables to store parsed values for each locale.
     */
    private Hashtable<String, Hashtable<String, String>> table;

    /**
     * @return the table
     */
    public final Hashtable<String, Hashtable<String, String>> getTable() {
        return table;
    }

    /**
     * @param newTable
     *        the table to set
     */
    public final void setTable(Hashtable<String, Hashtable<String, String>> newTable) {
        this.table = newTable;
    }

    /**
     * Default Constructor.
     */
    public HashTableStore() {
        table = new Hashtable<String, Hashtable<String, String>>();
    }

    /**
     * Fetches a string in the language specified by the locale. If the string
     * does not exist, "???" is returned.
     * 
     * @param locale
     *        the locale for which the String is localized.
     * @param key
     *        the key used to reference the stored String.
     * @return the localized string.
     */
    public String get(Locale locale, String key) {
        if (locale == null || key == null || key.length() == 0) {
            return "???";
        }
        String lang = locale.getLanguage();
        if (table.containsKey(lang) && table.get(lang).containsKey(key)) {
            return table.get(lang).get(key);
        } else {
            return "???";
        }
    }

    /**
     * Stores a string in the language specified by the locale.
     * 
     * @param locale
     *        the locale for which the String is localized.
     * @param key
     *        the key used to reference the stored String.
     * @param text
     *        the String to store.
     */
    public void put(Locale locale, String key, String text) {
        if (locale == null || key == null || key.length() == 0 || text == null || text.length() == 0) {
            return;
        }
        String lang = locale.getLanguage();
        if (!table.containsKey(lang)) {
            Hashtable<String, String> newLocaleTable = new Hashtable<String, String>();
            newLocaleTable.put(key, text);
            table.put(lang, newLocaleTable);
        } else if (table.get(lang).containsKey(key)) {
            table.get(lang).remove(key);
            table.get(lang).put(key, text);
        } else {
            table.get(lang).put(key, text);
        }
    }

    /**
     * Adds the specified locale to the store without any key/value pairs.
     * @param locale
     */
    public void addLocale(Locale locale) {
        if (locale == null) {
            return;
        }
        String lang = locale.getLanguage();
        if (!table.containsKey(lang)) {
            Hashtable<String, String> newLocaleTable = new Hashtable<String, String>();
            table.put(lang, newLocaleTable);
        }
    }

    /**
     * Fetch a list of locales for which translations exist.
     * 
     * @return a list of locales.
     */
    public List<Locale> getLocales() {
        ArrayList<Locale> locales = new ArrayList<Locale>();
        Enumeration<String> keys = table.keys();
        while (keys.hasMoreElements()) {
            locales.add(new Locale(keys.nextElement()));
        }
        return locales;
    }

    /**
     * Fetch a list of keys for the given locale.
     * 
     * @param locale
     *        the locale of the Strings the keys point to.
     * @return a list of keys
     */
    public List<String> getKeys(Locale locale) {
        if (locale == null) {
            return new ArrayList<String>();
        }
        String lang = locale.getLanguage();
        if (table.get(lang) == null) {
            return new ArrayList<String>();
        }
        Enumeration<String> keys = table.get(lang).keys();
        ArrayList<String> rset = new ArrayList<String>();
        while (keys.hasMoreElements()) {
            rset.add(keys.nextElement());
        }
        return rset;
    }

    /**
     * Removes a string from the language specified by the locale.
     * 
     * @param locale
     *        the locale for which the String is localized.
     * @param key
     *        the key used to reference the stored String.
     * @return the values associated with the removed key, or null if the
     *         key/table doesn't exist or is set to null.
     */
    public Object removeKey(Locale locale, String key) throws Exception {
        if (locale == null || key == null || key.length() == 0) {
            throw new NullPointerException();
        }
        String lang = locale.getLanguage();
        if (table.containsKey(lang)) {
            return table.get(lang).remove(key);
        } else {
            throw new Exception("Specified language locale table does not exist!");
        }
    }

    /**
     * Removes an entire locale.
     * 
     * @param locale
     *        the locale to remove.
     * @return the values associated with the removed key, or null if the
     *         key/table doesn't exist or is set to null.
     */
    public Object removeLocale(Locale locale) throws Exception {
        if (locale == null) {
            throw new NullPointerException();
        }
        String lang = locale.getLanguage();
        return table.remove(lang);
    }
}
