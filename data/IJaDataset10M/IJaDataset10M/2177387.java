package jwebtk.validator;

import org.apache.commons.validator.GenericValidator;
import org.apache.commons.validator.ISBNValidator;
import java.util.Locale;

/**
 * Provides a number of software available validation methods for common 
 * validation tasks.
 */
public class Validator {

    /**
     * Validates an International Standard Book Number.
     *
     * @param value to validate
     * @return true if the value passes validation, false otherwise.
     */
    public static boolean isIsbn(String value) {
        if (value == null || value.trim().length() == 0) return false;
        return new ISBNValidator().isValid(value);
    }

    /**
     * Validates a value to make sure it is not empty or null.
     *
     * @param value to validate
     * @return true if the value passes validation, false otherwise.
     */
    public static boolean isEmptyOrNull(String value) {
        if (value == null || value.trim().length() == 0) return true;
        return false;
    }

    /**
     * Validates a value to make sure it is not empty or null.
     *
     * @param value to validate
     * @return true if the value passes validation, false otherwise.
     */
    public static boolean isNotEmptyOrNull(String value) {
        return !GenericValidator.isBlankOrNull(value);
    }

    /**
     * Validates a byte value
     *
     * @param value to validate
     * @return true if the value passes validation, false otherwise.
     */
    public static boolean isByte(String value) {
        if (value == null || value.trim().length() == 0) return false;
        return GenericValidator.isByte(value);
    }

    /**
     * Validates a credit card number.  Simply checks if it is properly formatted. Does not contact any other party for card validation.
     *
     * @param value to validate
     * @return true if the value passes validation, false otherwise.
     */
    public static boolean isCreditCard(String value) {
        if (value == null || value.trim().length() == 0) return false;
        return GenericValidator.isCreditCard(value);
    }

    /**
     * Validates a date value to make sure it has a valid format.
     *
     * @param value to validate
     * @return true if the value passes validation, false otherwise.
     */
    public static boolean isDate(String value, Locale locale) {
        if (value == null || value.trim().length() == 0) return false;
        return GenericValidator.isDate(value, locale);
    }

    /**
     * Validates a date value to make sure it has a valid format matching the given date pattern.
     * Date pattern is defined in java.text.SimpleDateFormat.
     *
     * @param value to validate
     * @return true if the value passes validation, false otherwise.
     */
    public static boolean isDate(String value, String datePattern, boolean strict) {
        if (value == null || value.trim().length() == 0) return false;
        return GenericValidator.isDate(value, datePattern, strict);
    }

    /**
     * Validates a double value.
     * @param value to validate
     * @return true if the value passes validation, false otherwise.
     */
    public static boolean isDouble(String value) {
        if (value == null || value.trim().length() == 0) return false;
        return GenericValidator.isDouble(value);
    }

    /**
     * Validates as an email value.
     * @param value to validate
     * @return true if the value passes validation, false otherwise.
     */
    public static boolean isEmail(String value) {
        if (value == null || value.trim().length() == 0) return false;
        return GenericValidator.isEmail(value);
    }

    /**
     * Validate that value compares equal to str.
     * @param value to validate
     * @param value2 string to compare
     * @param ignore character case
     * @return true if the value passes validation, false otherwise.
     */
    public static boolean isEqualTo(String value, String value2, boolean ignore) {
        if (value == null) if (value2 == null) return true; else return false;
        if (ignore) return value.equalsIgnoreCase(value2);
        return value.equals(value2);
    }

    /**
     * Validates as a float value.
     * @param value to validate
     * @return true if the value passes validation, false otherwise.
     */
    public static boolean isFloat(String value) {
        if (value == null || value.trim().length() == 0) return false;
        return GenericValidator.isFloat(value);
    }

    /**
     * Validates that a value is in a given range, inclusive.
     * @param value to validate
     * @param min value is equal or greater
     * @param max value is equal or less
     * @return true if the value passes validation, false otherwise.
     */
    public static boolean isInRange(byte value, byte min, byte max) {
        return GenericValidator.isInRange(value, min, max);
    }

    /**
     * Validates that a value is in a given range, inclusive.
     * @param value to validate
     * @param min value is equal or greater
     * @param max value is equal or less
     * @return true if the value passes validation, false otherwise.
     */
    public static boolean isInRange(double value, double min, double max) {
        return GenericValidator.isInRange(value, min, max);
    }

    /**
     * Validates that a value is in a given range, inclusive.
     * @param value to validate
     * @param min value is equal or greater
     * @param max value is equal or less
     * @return true if the value passes validation, false otherwise.
     */
    public static boolean isInRange(float value, float min, float max) {
        return GenericValidator.isInRange(value, min, max);
    }

    /**
     * Validates that a value is in a given range, inclusive.
     * @param value to validate
     * @param min value is equal or greater
     * @param max value is equal or less
     * @return true if the value passes validation, false otherwise.
     */
    public static boolean isInRange(int value, int min, int max) {
        return GenericValidator.isInRange(value, min, max);
    }

    /**
     * Validates that a value is in a given range, inclusive.
     * @param value to validate
     * @param min value is equal or greater
     * @param max value is equal or less
     * @return true if the value passes validation, false otherwise.
     */
    public static boolean isInRange(long value, long min, long max) {
        return GenericValidator.isInRange(value, min, max);
    }

    /**
     * Validates that a value is in a given range, inclusive.
     * @param value to validate
     * @param min value is equal or greater
     * @param max value is equal or less
     * @return true if the value passes validation, false otherwise.
     */
    public static boolean isInRange(short value, short min, short max) {
        return GenericValidator.isInRange(value, min, max);
    }

    /**
     * Validates an integer value.
     * @param value to validate
     * @return true if the value passes validation, false otherwise.
     */
    public static boolean isInt(String value) {
        if (value == null || value.trim().length() == 0) return false;
        return GenericValidator.isInt(value);
    }

    /**
     * Validates a long value.
     * @param value to validate
     * @return true if the value passes validation, false otherwise.
     */
    public static boolean isLong(String value) {
        if (value == null || value.trim().length() == 0) return false;
        return GenericValidator.isLong(value);
    }

    /**
     * Validates a short value.
     * @param value to validate
     * @return true if the value passes validation, false otherwise.
     */
    public static boolean isShort(String value) {
        if (value == null || value.trim().length() == 0) return false;
        return GenericValidator.isShort(value);
    }

    /**
     * Validates that the given value is a properly formatted URL.
     * @param value to validate
     * @return true if the value passes validation, false otherwise.
     */
    public static boolean isUrl(String value) {
        if (value == null || value.trim().length() == 0) return false;
        return GenericValidator.isUrl(value);
    }

    /**
     * Validates that the given value is a substring of cmp_string.
     * @param value to validate
     * @param compareString the string to check
     * @return true if the value passes validation, false otherwise.
     */
    public static boolean isSubstring(String value, String compareString, boolean ignoreCase) {
        if (value == null || value.trim().length() == 0) return false;
        if (compareString == null || compareString.trim().length() == 0) return false;
        if (ignoreCase) return value.toLowerCase().indexOf(compareString.toLowerCase()) != -1;
        return value.indexOf(compareString) != -1;
    }

    /**
     * Validates that the given value starts with the match string.
     * @param value to validate
     * @param matchString the string to check
     * @return true if the value passes validation, false otherwise.
     */
    public static boolean startsWith(String value, String matchString, boolean ignoreCase) {
        if (value == null || value.trim().length() == 0) return false;
        if (matchString == null || matchString.trim().length() == 0) return false;
        if (ignoreCase) return value.toLowerCase().startsWith(matchString.toLowerCase());
        return value.startsWith(matchString);
    }

    /**
     * Validates that the given value ends with the match string.
     * @param value to validate
     * @param matchString the string to check
     * @return true if the value passes validation, false otherwise.
     */
    public static boolean endsWith(String value, String matchString, boolean ignoreCase) {
        if (value == null || value.trim().length() == 0) return false;
        if (matchString == null || matchString.trim().length() == 0) return false;
        if (ignoreCase) return value.toLowerCase().endsWith(matchString.toLowerCase());
        return value.endsWith(matchString);
    }

    /**
     * Validates that the given value matches the provided regular expression.  
     * this is based on Java regular expression as found in String.matchRegexp().
     * @param value to validate
     * @param regexp the regular expression string to check
     * @return true if the value passes validation, false otherwise.
     */
    public static boolean matchRegexp(String value, String regexp) {
        if (value == null || value.trim().length() == 0) return false;
        return GenericValidator.matchRegexp(value, regexp);
    }

    /**
     * Validates that value is less than or equal to the maximum length.
     * @param value to validate
     * @param max the maximum length
     * @return true if the value passes validation, false otherwise.
     */
    public static boolean maxLength(String value, int max) {
        if (value == null || value.trim().length() == 0) return false;
        return GenericValidator.maxLength(value, max);
    }

    /**
     * Validates that value is greater than or equal to the minimum length.
     * @param value to validate
     * @param min the minimum length
     * @return true if the value passes validation, false otherwise.
     */
    public static boolean minLength(String value, int min) {
        if (value == null || value.trim().length() == 0) return false;
        return GenericValidator.minLength(value, min);
    }

    /**
     * Validates that value is less than or equal to the maximum.
     * @param value to validate
     * @param max the maximum length
     * @return true if the value passes validation, false otherwise.
     */
    public static boolean maxValue(double value, double max) {
        return GenericValidator.maxValue(value, max);
    }

    /**
     * Validates that value is less than or equal to the maximum.
     * @param value to validate
     * @param max the maximum length
     * @return true if the value passes validation, false otherwise.
     */
    public static boolean maxValue(float value, float max) {
        return GenericValidator.maxValue(value, max);
    }

    /**
     * Validates that value is less than or equal to the maximum.
     * @param value to validate
     * @param max the maximum length
     * @return true if the value passes validation, false otherwise.
     */
    public static boolean maxValue(int value, int max) {
        return GenericValidator.maxValue(value, max);
    }

    /**
     * Validates that value is less than or equal to the maximum.
     * @param value to validate
     * @param max the maximum length
     * @return true if the value passes validation, false otherwise.
     */
    public static boolean maxValue(long value, long max) {
        return GenericValidator.maxValue(value, max);
    }

    /**
     * Validates that value is less than or equal to the maximum.
     * @param value to validate
     * @param min the minimum length
     * @return true if the value passes validation, false otherwise.
     */
    public static boolean minValue(double value, double min) {
        return GenericValidator.minValue(value, min);
    }

    /**
     * Validates that value is greater than or equal to the minimum.
     * @param value to validate
     * @param min the minimum length
     * @return true if the value passes validation, false otherwise.
     */
    public static boolean minValue(float value, float min) {
        return GenericValidator.minValue(value, min);
    }

    /**
     * Validates that value is greater than or equal to the minimum.
     * @param value to validate
     * @param min the minimum length
     * @return true if the value passes validation, false otherwise.
     */
    public static boolean minValue(int value, int min) {
        return GenericValidator.minValue(value, min);
    }

    /**
     * Validates that value is greater than or equal to the minimum.
     * @param value to validate
     * @param min the minimum length
     * @return true if the value passes validation, false otherwise.
     */
    public static boolean minValue(long value, long min) {
        return GenericValidator.minValue(value, min);
    }
}
