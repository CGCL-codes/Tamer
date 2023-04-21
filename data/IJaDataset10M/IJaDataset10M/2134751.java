package de.muntjak.tinylookandfeel.util;

/**
 * A (mutable) boolean wrapper.
 * @author Hans Bickel
 *
 */
public class BooleanReference {

    private boolean value;

    public BooleanReference(boolean value) {
        this.value = value;
    }

    public boolean getValue() {
        return value;
    }

    public void setValue(boolean value) {
        this.value = value;
    }
}
