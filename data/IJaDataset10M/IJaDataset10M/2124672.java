package org.dmd.util.parsing;

import java.util.*;

/**
 * The Dictionary class is used to maintain a set of Tokens that are used
 * by an Classifier during token classification.
 * @see org.dmd.util.parsing.Token
 * @see org.dmd.util.parsing.Classifier
 */
public class Dictionary {

    /**
     * This map contains all the tokens keyed on their respective string values.
     * Key: String
     * Value: Token
     */
    @SuppressWarnings("unchecked")
    private TreeMap byString;

    /**
     * This map contains all the tokens keyed on their respective type values.
     * Key: Integer
     * Value: Token
     */
    @SuppressWarnings("unchecked")
    private TreeMap byType;

    /**
     * The largest id so far.
     */
    int maxId;

    /**
      * Default constructor.
      */
    @SuppressWarnings("unchecked")
    public Dictionary() {
        byString = new TreeMap();
        byType = new TreeMap();
    }

    /**
     * Returns the largest ID.
     */
    public int getMaxID() {
        return (maxId);
    }

    /**
     * Sets the largest ID. Use with caution!
     */
    public void setMaxID(int i) {
        if (i > maxId) maxId = i;
    }

    /**
      * Adds the specified token to the dictionary.
      * @param t The token to add.
      * @return true if the token was successful added or false if the string or
      * type was already in the dictionary.
      */
    public boolean add(Token t) {
        boolean rc = true;
        rc = checkAndAdd(t.getValue(), t, byString);
        if (rc == true) {
            if (t.getType() > maxId) maxId = t.getType();
            rc = checkAndAdd(new Integer(t.getType()), t, byType);
        }
        return (rc);
    }

    /**
      * Adds the specified string/type to the dictionary.
      * @param v The string value.
      * @param t The type.
      * @return true if the token was successful added or false if the string or
      * type was already in the dictionary.
      */
    public boolean add(String v, int t) {
        Token newt = new Token(v, t);
        return (this.add(newt));
    }

    /**
      * Attempts to find the specified string in the dictionary.
      * @param v The string value.
      * @return The token associated with the string or null if the string
      * wasn't found.
      */
    public Token find(String v) {
        return ((Token) byString.get(v));
    }

    /**
      * Formats the dictionary as a String.
      */
    @SuppressWarnings("unchecked")
    public String toString() {
        StringBuffer sb = new StringBuffer();
        Iterator i = byString.keySet().iterator();
        Token t = null;
        while (i.hasNext()) {
            t = (Token) byString.get(i.next());
            sb.append(t.toString());
        }
        return (new String(sb));
    }

    /**
     * This function checks to see whether the specified key exists in the hashmap. If not,
     * the key and the obj are added.
     * @param key the key of the object being checked for
     * @param obj the object that may be adde to the map
     * @param map the map that may be updated
     * @returns false if the key already exists and true otherwise
     */
    @SuppressWarnings("unchecked")
    private boolean checkAndAdd(Object key, Object obj, TreeMap map) {
        if (map.containsKey(key)) return (false); else map.put(key, obj);
        return (true);
    }
}
