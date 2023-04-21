package com.inet.jortho;

import java.util.Locale;

/**
 * This Event is used by <code>LanguageChangeListener</code>.
 * @see LanguageChangeListener
 * @author Volker Berlin
 */
public class LanguageChangeEvent {

    private final Locale currentLocale;

    private final Locale oldLocale;

    /**
     * Creates a new LanguageChangeEvent
     * @param currentLocale the new Locale
     * @param oldLocale the old Locale
     */
    public LanguageChangeEvent(Locale currentLocale, Locale oldLocale) {
        this.currentLocale = currentLocale;
        this.oldLocale = oldLocale;
    }

    /**
     * Gets the value of the old Locale before the firing this Event.
     * @return the old Locale
     * @see SpellChecker#getCurrentLocale()
     */
    public Locale getOldLocale() {
        return oldLocale;
    }

    /**
     * Get the value of the current Locale after firing this Event.
     * It general it should be equal to {@link SpellChecker#getCurrentLocale()}.
     * @return the current Locale
     * @see SpellChecker#getCurrentLocale()
     */
    public Locale getCurrentLocale() {
        return currentLocale;
    }
}
