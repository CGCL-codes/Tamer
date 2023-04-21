package org.apache.axis2.databinding.types;

import org.apache.axis2.util.XMLChar;

/**
 * Custom class for supporting XSD data type Name Name represents XML Names. The value space of Name
 * is the set of all strings which match the Name production of [XML 1.0 (Second Edition)]. The base
 * type of Name is token.
 *
 * @see <a href="http://www.w3.org/TR/xmlschema-2/#Name">XML Schema 3.3.6</a>
 */
public class Name extends Token {

    private static final long serialVersionUID = -8354594301737358441L;

    public Name() {
        super();
    }

    /**
     * ctor for Name
     *
     * @throws IllegalArgumentException will be thrown if validation fails
     */
    public Name(String stValue) throws IllegalArgumentException {
        try {
            setValue(stValue);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(" invalid value for name " + "data=[" + stValue + "]");
        }
    }

    /**
     * validates the data and sets the value for the object.
     *
     * @param stValue String value
     * @throws IllegalArgumentException if invalid format
     */
    public void setValue(String stValue) throws IllegalArgumentException {
        if (!Name.isValid(stValue)) throw new IllegalArgumentException(" invalid value for name " + " data=[" + stValue + "]");
        m_value = stValue;
    }

    public static boolean isValid(String stValue) {
        int scan;
        boolean bValid = true;
        for (scan = 0; scan < stValue.length(); scan++) {
            if (scan == 0) {
                bValid = XMLChar.isNameStart(stValue.charAt(scan));
            } else {
                bValid = XMLChar.isName(stValue.charAt(scan));
            }
            if (!bValid) break;
        }
        return bValid;
    }
}
