package com.dilanperera.rapidws.core;

/**
 * Represents text content.
 */
public final class TextContent {

    /** Represents an empty string. */
    public static final String EMPTY = "";

    /** Represents the index of a string search, when the search target is not found. */
    public static final int TARGET_NOT_FOUND_INDEX = -1;

    /** Represents the newline text system property name. */
    private static final String NEWLINE_SYSTEM_PROPERTY_NAME = "line.separator";

    /** Represents the newline text. */
    public static final String NEWLINE = System.getProperty(TextContent.NEWLINE_SYSTEM_PROPERTY_NAME);

    /**
	 * Gets the Pascal-cased equivalent of the given text.
	 * @param text the text to be converted to its Pascal-cased equivalent.
	 * @return the converted text.
	 */
    public static String getPascalCased(String text) {
        return text.substring(0, 1).toUpperCase() + text.substring(1);
    }

    /**
	 * Gets the camel-cased equivalent of the given text.
	 * @param text the text to be converted to its camel-cased equivalent.
	 * @return the converted text.
	 */
    public static String getCamelCased(String text) {
        return text.substring(0, 1).toLowerCase() + text.substring(1);
    }
}
