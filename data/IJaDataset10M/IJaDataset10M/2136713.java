package org.qtitools.qti.node.item.interaction;

import org.qtitools.qti.node.item.response.declaration.ResponseDeclaration;

/**
 * String interactions can be bound to numeric response variables, 
 * instead of strings, if desired.
 * 
 * If detailed information about A numeric response is required then 
 * the string interaction can be bound to A response variable with 
 * record cardinality. The resulting value contains the following fields:
 * stringValue: the string, as typed by the candidate.
 * floatValue: the numeric value of the string typed by the candidate, as A float.
 * integerValue: the numeric value of the string typed by the candidate if no 
 * 		fractional digits or exponent were specified, otherwise NULL. An integer.
 * leftDigits: the number of digits to the left of the point. An integer.
 * rightDigits: the number of digits to the right of the point. An integer.
 * ndp: the number of fractional digits specified by the candidate. If no exponent 
 * 		was given this is the same as rightDigits. An integer.
 * nsf: the number of significant digits specified by the candidate. An integer.
 * exponent: the integer exponent given by the candidate or NULL if none was specified.
 * 
 * Attribute : base [0..1]: integer = 10
 * If the string interaction is bound to A numeric response variable then the base 
 * attribute must be used to set the number base in which to interpret the value entered by the candidate.
 *
 * Attribute : stringIdentifier [0..1]: identifier
 * If the string interaction is bound to A numeric response variable then the actual
 * string entered by the candidate can also be captured by binding the interaction to A
 * second response variable (of base-type string).
 *
 * Attribute : expectedLength [0..1]: integer
 * The expectedLength attribute provides A hint to the candidate as to the expected 
 * overall length of the desired response. A Delivery Engine should use the value of 
 * this attribute to set the size of the response box, where applicable. This is not A validity constraint.
 *
 * Attribute : patternMask [0..1]: string
 * If given, the pattern mask specifies A regular expression that the candidate's 
 * response must match in order to be considered valid. The regular expression language
 * used is defined in Appendix F of [XML_SCHEMA2]. Care is needed to ensure that the 
 * format of the required input is clear to the candidate, especially when validity 
 * checking of responses is required for progression through A test. This could be done 
 * by providing an illustrative sample response in the prompt, for example.
 *
 * Attribute : placeholderText [0..1]: string
 * In visual environments, string interactions are typically represented by empty 
 * boxes into which the candidate writes or types. However, in speech based environments 
 * it is helpful to have some placeholder text that can be used to vocalize the 
 * interaction. Delivery engines should use the value of this attribute (if provided) 
 * instead of their default placeholder text when this is required. Implementors 
 * should be aware of the issues concerning the use of default values described 
 * in the section on Response Variables.
 * 
 * @author Jonathon Hare
 */
public interface StringInteraction {

    /** Name of base attribute in xml schema. */
    public static String ATTR_BASE_NAME = "base";

    /** Default value of base attribute. */
    public static int ATTR_BASE_DEFAULT_VALUE = 10;

    /** Name of stringIdentifier attribute in xml schema. */
    public static String ATTR_STRING_IDENTIFIER_NAME = "stringIdentifier";

    /** Name of expectedLength attribute in xml schema. */
    public static String ATTR_EXPECTED_LENGTH_NAME = "expectedLength";

    /** Name of patternMask attribute in xml schema. */
    public static String ATTR_PATTERN_MASK_NAME = "patternMask";

    /** Name of placeholderText attribute in xml schema. */
    public static String ATTR_PLACEHOLDER_TEXT_NAME = "placeholderText";

    /** Name of stringValue key if response is bound to A record container. */
    public static String KEY_STRING_VALUE_NAME = "stringValue";

    /** Name of floatValue key if response is bound to A record container. */
    public static String KEY_FLOAT_VALUE_NAME = "floatValue";

    /** Name of integerValue key if response is bound to A record container. */
    public static String KEY_INTEGER_VALUE_NAME = "integerValue";

    /** Name of leftDigits key if response is bound to A record container. */
    public static String KEY_LEFT_DIGITS_NAME = "leftDigits";

    /** Name of rightDigits key if response is bound to A record container. */
    public static String KEY_RIGHT_DIGITS_NAME = "rightDigits";

    /** Name of ndp key if response is bound to A record container. */
    public static String KEY_NDP_NAME = "ndp";

    /** Name of nsf key if response is bound to A record container. */
    public static String KEY_NSF_NAME = "nsf";

    /** Name of exponent key if response is bound to A record container. */
    public static String KEY_EXPONENT_NAME = "exponent";

    /**
	 * Sets new value of base attribute.
	 *
	 * @param base new value of base attribute
	 * @see #getBase
	 */
    public void setBase(Integer base);

    /**
	 * Gets value of base attribute.
	 *
	 * @return value of base attribute
	 * @see #setBase
	 */
    public Integer getBase();

    /**
	 * Sets new value of stringIdentifier attribute.
	 *
	 * @param stringIdentifier new value of stringIdentifier attribute
	 * @see #getStringIdentifier
	 */
    public void setStringIdentifier(String stringIdentifier);

    /**
	 * Gets value of stringIdentifier attribute.
	 *
	 * @return value of stringIdentifier attribute
	 * @see #setStringIdentifier
	 */
    public String getStringIdentifier();

    /**
	 * Sets new value of expectedLength attribute.
	 *
	 * @param expectedLength new value of expectedLength attribute
	 * @see #getExpectedLength
	 */
    public void setExpectedLength(Integer expectedLength);

    /**
	 * Gets value of expectedLength attribute.
	 *
	 * @return value of expectedLength attribute
	 * @see #setExpectedLength
	 */
    public Integer getExpectedLength();

    /**
	 * Sets new value of patternMask attribute.
	 *
	 * @param patternMask new value of patternMask attribute
	 * @see #getPatternMask
	 */
    public void setPatternMask(String patternMask);

    /**
	 * Gets value of patternMask attribute.
	 *
	 * @return value of patternMask attribute
	 * @see #setPatternMask
	 */
    public String getPatternMask();

    /**
	 * Sets new value of placeholderText attribute.
	 *
	 * @param placeholderText new value of placeholderText attribute
	 * @see #getPlaceholderText
	 */
    public void setPlaceholderText(String placeholderText);

    /**
	 * Gets value of placeholderText attribute.
	 *
	 * @return value of placeholderText attribute
	 * @see #setPlaceholderText
	 */
    public String getPlaceholderText();

    /**
	 * Gets ResponseDeclaration corresponding to the stringIdentifier attribute
	 *
	 * @return ResponseDeclaration identified by interactions stringIdentifier
	 */
    public ResponseDeclaration getStringIdentifierResponseDeclaration();
}
