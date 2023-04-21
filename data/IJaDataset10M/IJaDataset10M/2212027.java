package net.sf.openforge.util.cli;

import java.io.PrintStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * 
 * @author Andreas Kollegger
 */
public class GnuOptionDictionary {

    /** A map of short keys to GnuOptionDefinitions. */
    Map shortMap;

    /** A map of long keys to GnuOptionDefinitions. */
    Map longMap;

    /**
     * Contructs an empty dictionary. Use @link #add to populate the
     * dictionary with definitions.
     */
    public GnuOptionDictionary() {
        shortMap = new HashMap();
        longMap = new HashMap();
    }

    /**
     * Contructs a new parser for a set of GnuOptionDefinitions.
     *  
     * @param definitions
     */
    public GnuOptionDictionary(Set definitions) {
        this();
        for (Iterator it = definitions.iterator(); it.hasNext(); ) {
            GnuOptionDefinition definition = (GnuOptionDefinition) it.next();
            add(definition);
        }
    }

    /**
     * Adds a definition to the dictionary.
     * 
     * @param definition the definition to add
     */
    public void add(GnuOptionDefinition definition) {
        Character shortKey = new Character(definition.getShortKey());
        shortMap.put(shortKey, definition);
        String longKey = definition.getLongKey();
        longMap.put(longKey, definition);
    }

    /**
     * Retrieves the option definition related to a long key.
     * 
     * @param longKey the long key
     * @return the related definition, or null if there is no matching definition
     */
    public GnuOptionDefinition getDefinition(String longKey) {
        return (GnuOptionDefinition) longMap.get(longKey);
    }

    /**
     * Retrieves the option definition related to a short key.
     * 
     * @param shortKey the short key
     * @return the related definition, or null if there is no matching definition
     */
    public GnuOptionDefinition getDefinition(char shortKey) {
        return (GnuOptionDefinition) shortMap.get(new Character(shortKey));
    }

    /**
     * Retrieves all the definitions.
     * 
     * @return the set of GnuOptionDefinitions.
     */
    public Collection getDefinitions() {
        return shortMap.values();
    }

    /**
     * Gets the number of entries.
     */
    public int size() {
        return shortMap.size();
    }

    /**
     * Checks whether there are no entries.
     */
    public boolean isEmpty() {
        return shortMap.isEmpty();
    }

    /**
     * Checks whether the dictionary contains a definition for the given key.
     * 
     * @param key either the short key (as a Character) or the long key (as a String)
     */
    public boolean containsKey(Object key) {
        return (shortMap.containsKey(key) || longMap.containsKey(key));
    }

    public Object remove(Object arg0) {
        return null;
    }

    public void putAll(Map arg0) {
    }

    public void clear() {
        shortMap.clear();
        longMap.clear();
    }

    public Set keySet() {
        return null;
    }

    public Collection values() {
        return shortMap.values();
    }

    public Set entrySet() {
        Set entries = new HashSet();
        entries.addAll(shortMap.entrySet());
        return entries;
    }

    /**
     * @param err
     */
    public void printUsage(PrintStream printer) {
        for (Iterator it = values().iterator(); it.hasNext(); ) {
            printer.println("  " + it.next());
        }
    }
}
