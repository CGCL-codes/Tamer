package org.xaware.server.engine.enums;

import java.util.HashMap;
import java.util.Map;
import org.xaware.shared.util.XAwareInvalidEnumerationValueException;

/**
 * This defines a type-safe enumeration of the valid values for attributes with "yes" or "no" values.
 * 
 * @author Tim Uttormark
 */
public class YesNoValue {

    private static final Map<String, YesNoValue> attributeValueTranslationMap = new HashMap<String, YesNoValue>();

    public static final YesNoValue YES = new YesNoValue("yes");

    public static final YesNoValue NO = new YesNoValue("no");

    /** The value of the attribute */
    private final String value;

    /**
     * Constructor made private to enforce type-safe enumeration pattern.
     * 
     * @param attrValue
     *            the value of the attribute
     */
    private YesNoValue(final String attrValue) {
        this.value = attrValue;
        attributeValueTranslationMap.put(attrValue, this);
    }

    /**
     * Translates an attribute value String into the corresponding YesNoAttributeValue object
     * 
     * @param attrValue
     *            the value of the attribute
     * @return the YesNoAttributeValue object which corresponds to the attribute value provided.
     * @throws XAwareInvalidEnumerationValueException
     *             if the value provided is not a valid enumeration value.
     */
    public static YesNoValue getYesNoAttributeValue(final String attrValue) throws XAwareInvalidEnumerationValueException {
        final YesNoValue retVal = attributeValueTranslationMap.get(attrValue);
        if (retVal != null) {
            return retVal;
        }
        throw new XAwareInvalidEnumerationValueException(attrValue + " is not a valid value, \"yes\" or \"no\" is expected.");
    }

    /**
     * Returns the value of the attribute
     * 
     * @return the value of the attribute
     */
    public String getValue() {
        return this.value;
    }

    /**
     * if the value equals "yes" return a true else return a false.
     * @return
     */
    public boolean getBooleanValue() {
        return YES.getValue().equals(this.value);
    }

    /**
     * Returns a String representation of the object.
     * 
     * @return a String representation of the object.
     */
    @Override
    public String toString() {
        return "YesNoAttributeValue:" + value;
    }
}
