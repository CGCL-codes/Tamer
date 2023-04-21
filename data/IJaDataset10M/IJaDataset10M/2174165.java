package org.jfree.xml.attributehandlers;

/**
 * A class that handles the conversion of {@link Character} attributes to and from an appropriate
 * {@link String} representation.
 */
public class CharacterAttributeHandler implements AttributeHandler {

    /**
     * Creates a new attribute handler.
     */
    public CharacterAttributeHandler() {
        super();
    }

    /**
     * Converts the attribute to a string.
     * 
     * @param o  the attribute ({@link Character} expected).
     * 
     * @return A string representing the {@link Character} value.
     */
    public String toAttributeValue(final Object o) {
        final Character in = (Character) o;
        return in.toString();
    }

    /**
     * Converts a string to a {@link Character}.
     * 
     * @param s  the string.
     * 
     * @return a {@link Character}.
     */
    public Object toPropertyValue(final String s) {
        if (s.length() == 0) {
            throw new RuntimeException("Ugly, no char set!");
        }
        return new Character(s.charAt(0));
    }
}
