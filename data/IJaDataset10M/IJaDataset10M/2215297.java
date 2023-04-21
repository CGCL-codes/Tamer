package prajna.data;

import java.io.Serializable;

/**
 * This is a convenience class that represents a string that has an associated
 * integer value.
 * 
 * @author <a href="http://www.ganae.com/edswing">Edward Swing</a>
 */
public class ValueString implements Serializable {

    private static final long serialVersionUID = 6068770595941310587L;

    private String str;

    private int val;

    /**
     * Create a new ValueString with the given text and a value of zero.
     * 
     * @param text the text for this ValueString
     */
    public ValueString(String text) {
        this(text, 0);
    }

    /**
     * Create a new ValueString with the given text and value
     * 
     * @param text the text for this ValueString
     * @param value the value for this ValueString
     */
    public ValueString(String text, int value) {
        str = new String(text);
        val = value;
    }

    /**
     * Return whether the object equals this ValueString object
     * 
     * @param obj the object to check for equality
     * @return true if the other object is a ValueString, and has the same text
     *         and value. Otherwise, it returns false.
     */
    @Override
    public boolean equals(Object obj) {
        return (obj != null && obj instanceof ValueString && str.equals(((ValueString) obj).getText()) && val == ((ValueString) obj).getValue());
    }

    /**
     * Get the text for this ValueString
     * 
     * @return the text
     */
    public String getText() {
        return str;
    }

    /**
     * Get the value for this ValueString
     * 
     * @return the value
     */
    public int getValue() {
        return val;
    }

    /**
     * Return the hash code for this ValueString
     * 
     * @return the hash code
     */
    @Override
    public int hashCode() {
        return str.hashCode() * 31 + val;
    }

    /**
     * Return a string representation of this ValueString
     * 
     * @return a string representation of this ValueString
     */
    @Override
    public String toString() {
        return new String(str + ": " + val);
    }
}
