package com.aaspring.translation;

import java.util.Locale;
import java.util.Map;
import com.aaspring.translation.model.LanguageFile;

/**
 * @author Balazs
 * 
 */
class LocaleItem {

    private final String key;

    private final LanguageFile languageFile;

    private final Map<Locale, String> values;

    /**
	 * @param key
	 * @param values
	 * @param languageFile
	 */
    public LocaleItem(final String key, final Map<Locale, String> values, final LanguageFile languageFile) {
        super();
        this.key = key;
        this.values = values;
        this.languageFile = languageFile;
    }

    public String getKey() {
        return this.key;
    }

    public LanguageFile getLanguageFile() {
        return this.languageFile;
    }

    public Map<Locale, String> getValues() {
        return this.values;
    }
}
