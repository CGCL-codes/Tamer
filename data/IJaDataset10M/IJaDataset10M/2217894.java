package org.middleheaven.core.wiring.mock;

public class HashDictionaryService implements DictionaryService {

    private String lang;

    public HashDictionaryService(String lang) {
        super();
        this.lang = lang;
    }

    @Override
    public String getLang() {
        return lang;
    }
}
