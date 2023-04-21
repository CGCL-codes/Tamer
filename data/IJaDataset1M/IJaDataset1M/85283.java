package java.awt.datatransfer;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * An object that encapsualtes the parameter list of a MimeType
 * as defined in RFC 2045 and 2046.
 *
 * @version 1.9, 02/02/00
 * @author Jeff Dunn
 */
class MimeTypeParameterList implements Cloneable {

    /**
     * Default constructor.
     */
    public MimeTypeParameterList() {
        parameters = new Hashtable();
    }

    public MimeTypeParameterList(String rawdata) throws MimeTypeParseException {
        parameters = new Hashtable();
        parse(rawdata);
    }

    private MimeTypeParameterList(Hashtable ht) throws CloneNotSupportedException {
        parameters = (Hashtable) ht.clone();
    }

    public int hashCode() {
        int code = Integer.MAX_VALUE / 45;
        String paramName = null;
        for (Enumeration enum_ = this.getNames(); (enum_.hasMoreElements()); paramName = (String) enum_.nextElement()) {
            code += paramName.hashCode();
            code += this.get(paramName).hashCode();
        }
        return code;
    }

    /**
     * Two parameter lists are considered equal if they have exactly
     * the same set of parameter names and associated values. The
     * order of the parameters is not considered.
     */
    public boolean equals(Object thatObject) {
        if (!(thatObject instanceof MimeTypeParameterList)) {
            return false;
        }
        MimeTypeParameterList that = (MimeTypeParameterList) thatObject;
        if (this.size() != that.size()) {
            return false;
        }
        String name = null;
        String thisValue = null;
        String thatValue = null;
        Set entries = parameters.entrySet();
        Iterator iterator = entries.iterator();
        Map.Entry entry = null;
        while (iterator.hasNext()) {
            entry = (Map.Entry) iterator.next();
            name = (String) entry.getKey();
            thisValue = (String) entry.getValue();
            thatValue = (String) that.parameters.get(name);
            if ((thisValue == null) || (thatValue == null)) {
                if (thisValue != thatValue) {
                    return false;
                }
            } else if (!thisValue.equals(thatValue)) {
                return false;
            }
        }
        return true;
    }

    /**
     * A routine for parsing the parameter list out of a String.
     */
    protected void parse(String rawdata) throws MimeTypeParseException {
        int length = rawdata.length();
        if (length > 0) {
            int currentIndex = skipWhiteSpace(rawdata, 0);
            int lastIndex = 0;
            if (currentIndex < length) {
                char currentChar = rawdata.charAt(currentIndex);
                while ((currentIndex < length) && (currentChar == ';')) {
                    String name;
                    String value;
                    boolean foundit;
                    ++currentIndex;
                    currentIndex = skipWhiteSpace(rawdata, currentIndex);
                    if (currentIndex < length) {
                        lastIndex = currentIndex;
                        currentChar = rawdata.charAt(currentIndex);
                        while ((currentIndex < length) && isTokenChar(currentChar)) {
                            ++currentIndex;
                            currentChar = rawdata.charAt(currentIndex);
                        }
                        name = rawdata.substring(lastIndex, currentIndex).toLowerCase();
                        currentIndex = skipWhiteSpace(rawdata, currentIndex);
                        if ((currentIndex < length) && (rawdata.charAt(currentIndex) == '=')) {
                            ++currentIndex;
                            currentIndex = skipWhiteSpace(rawdata, currentIndex);
                            if (currentIndex < length) {
                                currentChar = rawdata.charAt(currentIndex);
                                if (currentChar == '"') {
                                    ++currentIndex;
                                    lastIndex = currentIndex;
                                    if (currentIndex < length) {
                                        foundit = false;
                                        while ((currentIndex < length) && !foundit) {
                                            currentChar = rawdata.charAt(currentIndex);
                                            if (currentChar == '\\') {
                                                currentIndex += 2;
                                            } else if (currentChar == '"') {
                                                foundit = true;
                                            } else {
                                                ++currentIndex;
                                            }
                                        }
                                        if (currentChar == '"') {
                                            value = unquote(rawdata.substring(lastIndex, currentIndex));
                                            ++currentIndex;
                                        } else {
                                            throw new MimeTypeParseException("Encountered unterminated quoted parameter value.");
                                        }
                                    } else {
                                        throw new MimeTypeParseException("Encountered unterminated quoted parameter value.");
                                    }
                                } else if (isTokenChar(currentChar)) {
                                    lastIndex = currentIndex;
                                    foundit = false;
                                    while ((currentIndex < length) && !foundit) {
                                        currentChar = rawdata.charAt(currentIndex);
                                        if (isTokenChar(currentChar)) {
                                            ++currentIndex;
                                        } else {
                                            foundit = true;
                                        }
                                    }
                                    value = rawdata.substring(lastIndex, currentIndex);
                                } else {
                                    throw new MimeTypeParseException("Unexpected character encountered at index " + currentIndex);
                                }
                                parameters.put(name, value);
                            } else {
                                throw new MimeTypeParseException("Couldn't find a value for parameter named " + name);
                            }
                        } else {
                            throw new MimeTypeParseException("Couldn't find the '=' that separates a parameter name from its value.");
                        }
                    } else {
                        throw new MimeTypeParseException("Couldn't find parameter name");
                    }
                    currentIndex = skipWhiteSpace(rawdata, currentIndex);
                    if (currentIndex < length) {
                        currentChar = rawdata.charAt(currentIndex);
                    }
                }
                if (currentIndex < length) {
                    throw new MimeTypeParseException("More characters encountered in input than expected.");
                }
            }
        }
    }

    /**
     * return the number of name-value pairs in this list.
     */
    public int size() {
        return parameters.size();
    }

    /**
     * Determine whether or not this list is empty.
     */
    public boolean isEmpty() {
        return parameters.isEmpty();
    }

    /**
     * Retrieve the value associated with the given name, or null if there
     * is no current association.
     */
    public String get(String name) {
        return (String) parameters.get(name.trim().toLowerCase());
    }

    /**
     * Set the value to be associated with the given name, replacing
     * any previous association.
     */
    public void set(String name, String value) {
        parameters.put(name.trim().toLowerCase(), value);
    }

    /**
     * Remove any value associated with the given name.
     */
    public void remove(String name) {
        parameters.remove(name.trim().toLowerCase());
    }

    /**
     * Retrieve an enumeration of all the names in this list.
     */
    public Enumeration getNames() {
        return parameters.keys();
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.ensureCapacity(parameters.size() * 16);
        Enumeration keys = parameters.keys();
        while (keys.hasMoreElements()) {
            buffer.append("; ");
            String key = (String) keys.nextElement();
            buffer.append(key);
            buffer.append('=');
            buffer.append(quote((String) parameters.get(key)));
        }
        return buffer.toString();
    }

    /**
     * @return a clone of this object
     */
    public Object clone() throws CloneNotSupportedException {
        return new MimeTypeParameterList(parameters);
    }

    private Hashtable parameters;

    /**
     * Determine whether or not a given character belongs to a legal token.
     */
    private static boolean isTokenChar(char c) {
        return ((c > 040) && (c < 0177)) && (TSPECIALS.indexOf(c) < 0);
    }

    /**
     * return the index of the first non white space character in
     * rawdata at or after index i.
     */
    private static int skipWhiteSpace(String rawdata, int i) {
        int length = rawdata.length();
        if (i < length) {
            char c = rawdata.charAt(i);
            while ((i < length) && Character.isWhitespace(c)) {
                ++i;
                c = rawdata.charAt(i);
            }
        }
        return i;
    }

    /**
     * A routine that knows how and when to quote and escape the given value.
     */
    private static String quote(String value) {
        boolean needsQuotes = false;
        int length = value.length();
        for (int i = 0; (i < length) && !needsQuotes; ++i) {
            needsQuotes = !isTokenChar(value.charAt(i));
        }
        if (needsQuotes) {
            StringBuffer buffer = new StringBuffer();
            buffer.ensureCapacity((int) (length * 1.5));
            buffer.append('"');
            for (int i = 0; i < length; ++i) {
                char c = value.charAt(i);
                if ((c == '\\') || (c == '"')) {
                    buffer.append('\\');
                }
                buffer.append(c);
            }
            buffer.append('"');
            return buffer.toString();
        } else {
            return value;
        }
    }

    /**
     * A routine that knows how to strip the quotes and escape sequences from the given value.
     */
    private static String unquote(String value) {
        int valueLength = value.length();
        StringBuffer buffer = new StringBuffer();
        buffer.ensureCapacity(valueLength);
        boolean escaped = false;
        for (int i = 0; i < valueLength; ++i) {
            char currentChar = value.charAt(i);
            if (!escaped && (currentChar != '\\')) {
                buffer.append(currentChar);
            } else if (escaped) {
                buffer.append(currentChar);
                escaped = false;
            } else {
                escaped = true;
            }
        }
        return buffer.toString();
    }

    /**
     * A string that holds all the special chars.
     */
    private static final String TSPECIALS = "()<>@,;:\\\"/[]?=";
}
