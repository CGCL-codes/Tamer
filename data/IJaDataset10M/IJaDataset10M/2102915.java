package com.google.gdata.data.sidewiki;

import com.google.gdata.data.AbstractExtension;
import com.google.gdata.data.AttributeGenerator;
import com.google.gdata.data.AttributeHelper;
import com.google.gdata.data.ExtensionDescription;
import com.google.gdata.util.ParseException;

/**
 * Usefulness of entry.
 *
 * 
 */
@ExtensionDescription.Default(nsAlias = SidewikiNamespace.SIDEWIKI_ALIAS, nsUri = SidewikiNamespace.SIDEWIKI, localName = Usefulness.XML_NAME)
public class Usefulness extends AbstractExtension {

    /** XML element name */
    static final String XML_NAME = "usefulness";

    private static final AttributeHelper.EnumToAttributeValue<Value> VALUE_ENUM_TO_ATTRIBUTE_VALUE = new AttributeHelper.EnumToAttributeValue<Value>() {

        public String getAttributeValue(Value enumValue) {
            return enumValue.toValue();
        }
    };

    /** Value */
    private Value value = null;

    /** Value. */
    public enum Value {

        /** LessUseful usefulness. */
        LESSUSEFUL("lessUseful"), /** Useful usefulness. */
        USEFUL("useful");

        private final String value;

        private Value(String value) {
            this.value = value;
        }

        /**
     * Returns the value used in the XML.
     *
     * @return value used in the XML
     */
        public String toValue() {
            return value;
        }
    }

    /**
   * Default mutable constructor.
   */
    public Usefulness() {
        super();
    }

    /**
   * Immutable constructor.
   *
   * @param value value.
   */
    public Usefulness(Value value) {
        super();
        setValue(value);
        setImmutable(true);
    }

    /**
   * Returns the value.
   *
   * @return value
   */
    public Value getValue() {
        return value;
    }

    /**
   * Sets the value.
   *
   * @param value value or <code>null</code> to reset
   */
    public void setValue(Value value) {
        throwExceptionIfImmutable();
        this.value = value;
    }

    /**
   * Returns whether it has the value.
   *
   * @return whether it has the value
   */
    public boolean hasValue() {
        return getValue() != null;
    }

    @Override
    protected void validate() {
        if (value == null) {
            throw new IllegalStateException("Missing text content");
        }
    }

    /**
   * Returns the extension description, specifying whether it is required, and
   * whether it is repeatable.
   *
   * @param required   whether it is required
   * @param repeatable whether it is repeatable
   * @return extension description
   */
    public static ExtensionDescription getDefaultDescription(boolean required, boolean repeatable) {
        ExtensionDescription desc = ExtensionDescription.getDefaultDescription(Usefulness.class);
        desc.setRequired(required);
        desc.setRepeatable(repeatable);
        return desc;
    }

    @Override
    protected void putAttributes(AttributeGenerator generator) {
        generator.setContent(VALUE_ENUM_TO_ATTRIBUTE_VALUE.getAttributeValue(value));
    }

    @Override
    protected void consumeAttributes(AttributeHelper helper) throws ParseException {
        value = helper.consumeEnum(null, true, Value.class, null, VALUE_ENUM_TO_ATTRIBUTE_VALUE);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!sameClassAs(obj)) {
            return false;
        }
        Usefulness other = (Usefulness) obj;
        return eq(value, other.value);
    }

    @Override
    public int hashCode() {
        int result = getClass().hashCode();
        if (value != null) {
            result = 37 * result + value.hashCode();
        }
        return result;
    }

    @Override
    public String toString() {
        return "{Usefulness value=" + value + "}";
    }
}
