package org.cubictest.ui.i18n;

import org.cubictest.model.i18n.AllLanguages;

public class I18nMessage {

    private AllLanguages languages;

    private String key;

    public I18nMessage(AllLanguages languages, String key) {
        this.languages = languages;
        this.key = key;
    }
}
