package openrpg2.common.dice;

/**
 * A value representing a string
 * @author markt
 */
public class StrValue implements DiceValue {

    private String value;

    /**
     * Creates a new instance of StrValue
     * @param s The string to represent
     */
    public StrValue(String s) {
        value = s;
    }

    /**
     * Determines whether or not this expression is an integer.
     * @return false, always
     */
    public boolean isInt() {
        return false;
    }

    /**
     * Gets the number of elements in this value
     * @return 0, always
     */
    public int getSize() {
        return 0;
    }

    /**
     * Gets the string representation for this value
     * @return the string this value was set to
     */
    public String toString() {
        return value;
    }

    /**
     * Gets the base value for this expression
     * @return this
     */
    public DiceValue getBaseValue() {
        return this;
    }
}
