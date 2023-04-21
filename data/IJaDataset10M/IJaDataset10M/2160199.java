package org.eichar.client;

import com.google.gwt.core.client.GWT;

/**
 * Basically a parameter object but can look up the internationalization classes
 * if required
 * 
 * @author Jesse
 */
public class I18N {

    private final LanguageMessages messages;

    private final LanguageConstants constants;

    /**
     * Creates messages and constants. Takes some time so try to minimize this
     */
    public I18N() {
        constants = (LanguageConstants) GWT.create(LanguageConstants.class);
        messages = (LanguageMessages) GWT.create(LanguageMessages.class);
    }

    public I18N(final LanguageMessages messages, final LanguageConstants constants) {
        this.messages = messages;
        this.constants = constants;
    }

    public final LanguageConstants getConstants() {
        return constants;
    }

    public final LanguageMessages getMessages() {
        return messages;
    }
}
