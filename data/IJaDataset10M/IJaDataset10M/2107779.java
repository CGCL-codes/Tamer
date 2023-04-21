package de.kugihan.dictionaryformids.general;

public class DictionaryException extends Exception {

    public DictionaryException(Throwable t) {
        super(t.toString());
    }

    public DictionaryException(String message) {
        super(message);
    }
}
