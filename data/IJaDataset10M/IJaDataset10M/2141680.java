package org.apache.axis2.jaxws.message;

import java.util.Locale;

/** Agnostic representation of a Fault Reason/faultstring. See @XMLFault */
public class XMLFaultReason {

    String text;

    String lang;

    /**
     * A Fault Reason has the reason text and language
     *
     * @param text
     * @param lang
     */
    public XMLFaultReason(String text, String lang) {
        this.text = text;
        this.lang = lang;
    }

    /**
     * A Fault Reason with the default language
     *
     * @param text
     */
    public XMLFaultReason(String text) {
        this(text, getDefaultLang());
    }

    /** @return Returns the lang. */
    public String getLang() {
        return lang;
    }

    /** @return Returns the text. */
    public String getText() {
        return text;
    }

    /** @return the IS0 639 language identifier for the default locale */
    public static String getDefaultLang() {
        Locale locale = Locale.getDefault();
        return locale.getLanguage();
    }
}
