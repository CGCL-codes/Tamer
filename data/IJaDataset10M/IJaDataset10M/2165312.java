package org.zeroexchange.model.dictionary;

import javax.persistence.Entity;

/**
 * The string dictionary item.
 * 
 * @author black
 */
@Entity
public class StringDictionaryItem extends DictionaryItem {

    /** The dictionary value. */
    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return value;
    }
}
