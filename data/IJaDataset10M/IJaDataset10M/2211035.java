package com.frameworkset.util;

/**
 * <p>Title: HtmlUtils.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2010-9-28
 * @author biaoping.yin
 * @version 1.0
 */
public abstract class HtmlUtils {

    /**
	 * Shared instance of pre-parsed HTML character entity references.
	 */
    private static final HtmlCharacterEntityReferences characterEntityReferences = new HtmlCharacterEntityReferences();

    /**
	 * Turn special characters into HTML character references.
	 * Handles complete character set defined in HTML 4.01 recommendation.
	 * <p>Escapes all special characters to their corresponding
	 * entity reference (e.g. <code>&lt;</code>).
	 * <p>Reference:
	 * <a href="http://www.w3.org/TR/html4/sgml/entities.html">
	 * http://www.w3.org/TR/html4/sgml/entities.html
	 * </a>
	 * @param input the (unescaped) input string
	 * @return the escaped string
	 */
    public static String htmlEscape(String input) {
        if (input == null) {
            return null;
        }
        StringBuffer escaped = new StringBuffer(input.length() * 2);
        for (int i = 0; i < input.length(); i++) {
            char character = input.charAt(i);
            String reference = characterEntityReferences.convertToReference(character);
            if (reference != null) {
                escaped.append(reference);
            } else {
                escaped.append(character);
            }
        }
        return escaped.toString();
    }

    /**
	 * Turn special characters into HTML character references.
	 * Handles complete character set defined in HTML 4.01 recommendation.
	 * <p>Escapes all special characters to their corresponding numeric
	 * reference in decimal format (&#<i>Decimal</i>;).
	 * <p>Reference:
	 * <a href="http://www.w3.org/TR/html4/sgml/entities.html">
	 * http://www.w3.org/TR/html4/sgml/entities.html
	 * </a>
	 * @param input the (unescaped) input string
	 * @return the escaped string
	 */
    public static String htmlEscapeDecimal(String input) {
        if (input == null) {
            return null;
        }
        StringBuffer escaped = new StringBuffer(input.length() * 2);
        for (int i = 0; i < input.length(); i++) {
            char character = input.charAt(i);
            if (characterEntityReferences.isMappedToReference(character)) {
                escaped.append(HtmlCharacterEntityReferences.DECIMAL_REFERENCE_START);
                escaped.append((int) character);
                escaped.append(HtmlCharacterEntityReferences.REFERENCE_END);
            } else {
                escaped.append(character);
            }
        }
        return escaped.toString();
    }

    /**
	 * Turn special characters into HTML character references.
	 * Handles complete character set defined in HTML 4.01 recommendation.
	 * <p>Escapes all special characters to their corresponding numeric
	 * reference in hex format (&#x<i>Hex</i>;).
	 * <p>Reference:
	 * <a href="http://www.w3.org/TR/html4/sgml/entities.html">
	 * http://www.w3.org/TR/html4/sgml/entities.html
	 * </a>
	 * @param input the (unescaped) input string
	 * @return the escaped string
	 */
    public static String htmlEscapeHex(String input) {
        if (input == null) {
            return null;
        }
        StringBuffer escaped = new StringBuffer(input.length() * 2);
        for (int i = 0; i < input.length(); i++) {
            char character = input.charAt(i);
            if (characterEntityReferences.isMappedToReference(character)) {
                escaped.append(HtmlCharacterEntityReferences.HEX_REFERENCE_START);
                escaped.append(Integer.toString((int) character, 16));
                escaped.append(HtmlCharacterEntityReferences.REFERENCE_END);
            } else {
                escaped.append(character);
            }
        }
        return escaped.toString();
    }

    /**
	 * Turn HTML character references into their plain text UNICODE equivalent.
	 * <p>Handles complete character set defined in HTML 4.01 recommendation
	 * and all reference types (decimal, hex, and entity).
	 * <p>Correctly converts the following formats:
	 * <blockquote>
	 * &amp;#<i>Entity</i>; - <i>(Example: &amp;amp;) case sensitive</i>
	 * &amp;#<i>Decimal</i>; - <i>(Example: &amp;#68;)</i><br>
	 * &amp;#x<i>Hex</i>; - <i>(Example: &amp;#xE5;) case insensitive</i><br>
	 * </blockquote>
	 * Gracefully handles malformed character references by copying original
	 * characters as is when encountered.<p>
	 * <p>Reference:
	 * <a href="http://www.w3.org/TR/html4/sgml/entities.html">
	 * http://www.w3.org/TR/html4/sgml/entities.html
	 * </a>
	 * @param input the (escaped) input string
	 * @return the unescaped string
	 */
    public static String htmlUnescape(String input) {
        if (input == null) {
            return null;
        }
        return new HtmlCharacterEntityDecoder(characterEntityReferences, input).decode();
    }
}
