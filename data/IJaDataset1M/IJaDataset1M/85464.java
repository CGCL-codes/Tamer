package com.volantis.mcs.xdime.xhtml2;

import java.util.HashMap;

/**
 *  Validator for object parameters.
 */
final class ObjectParameter {

    /**
     * All valid parameter names.
     */
    public static final String MCS_ASPECT_RATIO_WIDTH = "mcs-aspect-ratio-width";

    public static final String MCS_ASPECT_RATIO_HEIGHT = "mcs-aspect-ratio-height";

    public static final String MCS_TRANSCODE = "mcs-transcode";

    public static final String MCS_EXTERNAL_LABEL = "mcs-external-label";

    public static final String[] ALL_VALID_NAMES = new String[] { MCS_ASPECT_RATIO_WIDTH, MCS_ASPECT_RATIO_HEIGHT, MCS_TRANSCODE, MCS_EXTERNAL_LABEL };

    /**
     * Single instance of all type parsers
     */
    private static Parser NUMBER = new NumberParser();

    private static Parser BOOLEAN = new BooleanParser();

    private static Parser STRING = new StringParser();

    /**
     * HashMap of valid parameter names mapped to value parsers.
     */
    private static final HashMap parameterTypes = new HashMap();

    /**
     * HashMap of default values for parameters.
     */
    private static final HashMap parameterDefaults = new HashMap();

    static {
        parameterTypes.put(MCS_ASPECT_RATIO_WIDTH, NUMBER);
        parameterTypes.put(MCS_ASPECT_RATIO_HEIGHT, NUMBER);
        parameterTypes.put(MCS_TRANSCODE, BOOLEAN);
        parameterTypes.put(MCS_EXTERNAL_LABEL, STRING);
        parameterDefaults.put(MCS_ASPECT_RATIO_WIDTH, null);
        parameterDefaults.put(MCS_ASPECT_RATIO_HEIGHT, null);
        parameterDefaults.put(MCS_TRANSCODE, Boolean.TRUE);
        parameterDefaults.put(MCS_EXTERNAL_LABEL, null);
    }

    /**
     * Given a name value pair, get the paser appropriate for the name
     * and use it to parse the value.
     *
     * @param name valid parameter name
     * @param value valid paramerter value
     * @return parameter object or null if either the name or value is invalid
     */
    public static Object parseValue(String name, String value) {
        Object result = null;
        Parser parser = (Parser) parameterTypes.get(name);
        if (parser != null) {
            result = parser.parse(value);
        }
        return result;
    }

    /**
     * Is the given name a valid parameter name.
     *
     * @param name parameter name, case sensitive
     * @return
     */
    public static boolean isNameValid(String name) {
        return parameterTypes.containsKey(name);
    }

    /**
     * Given a map containing a number of parameter, add in any default values.
     *
     * @param parameters map of defined parameters, may be updated by this
     *                   method
     */
    public static void addDefaultValues(HashMap parameters) {
        for (int i = 0; i < ALL_VALID_NAMES.length; i++) {
            String name = ALL_VALID_NAMES[i];
            Object currentValue = parameters.get(name);
            Object defaultValue = parameterDefaults.get(name);
            if (currentValue == null && defaultValue != null) {
                parameters.put(name, defaultValue);
            }
        }
    }

    /**
     * Interface defining a single parse method implemented by all type
     * parsers.
     */
    private static interface Parser {

        /**
         * Given a string representation of the parameter parse it and
         * return the processed attribute as an object.
         *
         * @param value string to parse
         * @return Parsed object or null if value is invalid
         */
        Object parse(String value);
    }

    /**
     * Parser used to parse numeric parmater values.
     */
    private static class NumberParser implements Parser {

        /**
         * Parse the value string as a positive integer.
         *
         * @param value string to parse
         * @return Integer object or null if not valid
         */
        public Object parse(String value) {
            Object valueObject = null;
            try {
                Integer intValue = new Integer(value);
                if (intValue.intValue() >= 0) {
                    valueObject = intValue;
                }
            } catch (NumberFormatException e) {
            }
            return valueObject;
        }
    }

    /**
     * Parser used to parse boolean paramter values.
     */
    private static class BooleanParser implements Parser {

        /**
         * Parse the string value as a boolean, only "true" and "false"
         * are allowed.
         *
         * @param value string to parse
         * @return Boolean object or null if not allowed
         */
        public Object parse(String value) {
            Object valueObject = null;
            if (value.equalsIgnoreCase("true")) {
                valueObject = Boolean.TRUE;
            } else if (value.equalsIgnoreCase("false")) {
                valueObject = Boolean.FALSE;
            } else {
            }
            return valueObject;
        }
    }

    /**
     * Parser used to parse string paramter values.
     * This parser do nothing, only return passed value
     */
    private static class StringParser implements Parser {

        /**
         * Parse the string value.
         *
         * @param value string to parse
         * @return string
         */
        public Object parse(String value) {
            return value;
        }
    }
}
