package org.fcrepo.server.utilities;

/**
 *
 * @author Edwin Shin
 * @since 3.0.1
 * @version $Id: DCField.java 8493 2010-01-20 02:13:21Z birkland $
 */
public class DCField {

    private final String value;

    private final String lang;

    public DCField(String value) {
        this(value, null);
    }

    public DCField(String value, String lang) {
        this.value = value;
        this.lang = lang;
    }

    public String getValue() {
        return value;
    }

    public String getLang() {
        return lang;
    }
}
