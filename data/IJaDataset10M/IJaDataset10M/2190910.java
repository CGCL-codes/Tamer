package de.dgrid.bisgrid.services.proxy.redirect.util;

/**
 * A pair consisting of a name and a value.
 * Both parts are strings.
 *
 */
public class StringPair {

    private String key;

    private String value;

    public StringPair(String key, String value) {
        super();
        this.key = key;
        this.value = value;
    }

    public StringPair(String Key) {
        super();
        this.key = Key;
        this.value = null;
    }

    public StringPair(StringPair Src) {
        super();
        this.key = Src.key;
        this.value = Src.value;
    }

    /**
     * Read the key.
     * @return Key part of the pair
     */
    public String getKey() {
        return this.key;
    }

    /**
     * Read the value of the pair.
     * @return The value part of the pair
     */
    public String getValue() {
        return this.value;
    }

    /**
     * Write the value of the pair.
     */
    public void setValue(String NewValue) {
        this.value = NewValue;
    }
}
