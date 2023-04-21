package com.trollworks.ttk.collections;

/** A utility for consistent extraction of an {@link Enum} value from a text buffer. */
public class EnumExtractor {

    private static final String EMPTY = "";

    private static final String COMMA = ",";

    private static final String UNDERSCORE = "_";

    private static final String SPACE = " ";

    @SuppressWarnings("unchecked")
    public static final Enum extract(String buffer, Enum<?>[] values, Enum<?> defaultValue) {
        Enum<?> value = extract(buffer, values);
        return value != null ? value : defaultValue;
    }

    @SuppressWarnings("unchecked")
    public static final Enum extract(String buffer, Enum<?>[] values) {
        for (Enum<?> type : values) {
            if (type.name().equalsIgnoreCase(buffer)) {
                return type;
            }
        }
        for (Enum<?> type : values) {
            if (type.name().replaceAll(UNDERSCORE, SPACE).equalsIgnoreCase(buffer)) {
                return type;
            }
        }
        for (Enum<?> type : values) {
            if (type.name().replaceAll(UNDERSCORE, COMMA).equalsIgnoreCase(buffer)) {
                return type;
            }
        }
        for (Enum<?> type : values) {
            if (type.name().replaceAll(UNDERSCORE, EMPTY).equalsIgnoreCase(buffer)) {
                return type;
            }
        }
        return null;
    }
}
