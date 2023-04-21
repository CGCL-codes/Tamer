package org.zeroexchange.i18n;

import java.io.Serializable;

/**
 * The container holds localized values.
 *  
 * @author black
 */
public class LocalizedValue implements Serializable {

    /** The value itself. */
    private String value;

    /** The value's language. */
    private String language;

    /**
     * Constructor.
     */
    public LocalizedValue(String value, String language) {
        this.value = value;
        this.language = language;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * @return the language
     */
    public String getLanguage() {
        return language;
    }
}
