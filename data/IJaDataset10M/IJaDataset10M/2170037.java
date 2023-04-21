package com.siberhus.commons.util.thai;

import java.util.regex.Pattern;
import org.apache.commons.lang.ArrayUtils;

public class ThaiWordUtil {

    public static final int FIRST_ENTITY_CODE = 3585;

    public static final int LAST_ENTITY_CODE = 3675;

    public static final int FIRST_CONSONANT_CODE = 3585;

    public static final int LAST_CONSONANT_CODE = 3630;

    public static final int FIRST_VOWEL_AND_SIGN_CODE = 3652;

    public static final int LAST_VOWEL_AND_SIGN_CODE = 3662;

    public static final int FIRST_NUMBER_CODE = 3664;

    public static final int LAST_NUMBER_CODE = 3673;

    public static final String PATTERN_THAI_CHARS_STR = "[" + (char) FIRST_ENTITY_CODE + "-" + (char) LAST_ENTITY_CODE + "]";

    public static final String PATTERN_NON_THAI_CHARS_STR = "[^" + (char) FIRST_ENTITY_CODE + "-" + (char) LAST_ENTITY_CODE + "]";

    public static final String PATTERN_THAI_CHARS_AND_SPACE_STR = "[" + (char) FIRST_ENTITY_CODE + "-" + (char) LAST_ENTITY_CODE + "\\s]";

    public static final String PATTERN_NON_THAI_CHARS_AND_SPACE_STR = "[^" + (char) FIRST_ENTITY_CODE + "-" + (char) LAST_ENTITY_CODE + "\\s]";

    public static final Pattern PATTERN_THAI_CHARS = Pattern.compile(PATTERN_THAI_CHARS_STR);

    public static final Pattern PATTERN_NON_THAI_CHARS = Pattern.compile(PATTERN_NON_THAI_CHARS_STR);

    public static final Pattern PATTERN_THAI_AND_SPACE_CHARS = Pattern.compile(PATTERN_THAI_CHARS_AND_SPACE_STR);

    public static final Pattern PATTERN_NON_THAI_AND_SPACE_CHARS = Pattern.compile(PATTERN_NON_THAI_CHARS_AND_SPACE_STR);

    public static boolean isThaiEntityChar(char v) {
        if (v >= FIRST_ENTITY_CODE && v <= LAST_ENTITY_CODE) {
            return true;
        }
        return false;
    }

    public static boolean isThaiConsonantChar(char v) {
        if (v >= FIRST_CONSONANT_CODE && v <= LAST_CONSONANT_CODE) {
            return true;
        }
        return false;
    }

    public static boolean isAllThaiAlphabet(String value) {
        String tmp = filterOutNonThaiChars(value);
        if (value.length() == tmp.length()) {
            return true;
        }
        return false;
    }

    public static boolean maybeValidThaiName(String value) {
        value = value.replaceAll("[\\s\\.]", "");
        return maybeValidThaiWord(value);
    }

    /**
	 * Simple and stupid Thai name check method
	 * 
	 * @param value
	 * @return
	 */
    public static boolean maybeValidThaiWord(String value) {
        char v = value.charAt(0);
        int validVowels[] = new int[] { 3648, 3649, 3650, 3651, 3652 };
        if (!isThaiConsonantChar(v)) {
            if (!ArrayUtils.contains(validVowels, v)) {
                return false;
            }
        }
        String tmp = filterOutNonThaiChars(value);
        if (value.length() == tmp.length()) {
            return true;
        }
        return false;
    }

    public static String filterOutNonThaiChars(String value) {
        return filterOutNonThaiChars(value, true);
    }

    public static String filterOutNonThaiChars(String value, boolean preserveSpace) {
        if (value == null) return null;
        if (preserveSpace) {
            return PATTERN_NON_THAI_AND_SPACE_CHARS.matcher(value).replaceAll("");
        } else {
            return PATTERN_NON_THAI_CHARS.matcher(value).replaceAll("");
        }
    }

    public static String filterOutThaiChars(String value) {
        return filterOutThaiChars(value, true);
    }

    public static String filterOutThaiChars(String value, boolean preserveSpace) {
        if (value == null) return null;
        if (preserveSpace) {
            return PATTERN_THAI_CHARS.matcher(value).replaceAll("");
        } else {
            return PATTERN_THAI_AND_SPACE_CHARS.matcher(value).replaceAll("");
        }
    }
}
