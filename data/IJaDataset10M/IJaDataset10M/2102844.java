package com.google.gdata.data.sidewiki;

import com.google.gdata.data.AbstractExtension;
import com.google.gdata.data.AttributeGenerator;
import com.google.gdata.data.AttributeHelper;
import com.google.gdata.data.ExtensionDescription;
import com.google.gdata.util.ParseException;

/**
 * Number of Sidewiki entries written by this user.
 *
 * 
 */
@ExtensionDescription.Default(nsAlias = SidewikiNamespace.SIDEWIKI_ALIAS, nsUri = SidewikiNamespace.SIDEWIKI, localName = EntriesNumber.XML_NAME)
public class EntriesNumber extends AbstractExtension {

    /** XML element name */
    static final String XML_NAME = "numEntries";

    /** Number of written Sidewiki entries */
    private Integer value = null;

    /**
   * Default mutable constructor.
   */
    public EntriesNumber() {
        super();
    }

    /**
   * Immutable constructor.
   *
   * @param value number of written Sidewiki entries.
   */
    public EntriesNumber(Integer value) {
        super();
        setValue(value);
        setImmutable(true);
    }

    /**
   * Returns the number of written Sidewiki entries.
   *
   * @return number of written Sidewiki entries
   */
    public Integer getValue() {
        return value;
    }

    /**
   * Sets the number of written Sidewiki entries.
   *
   * @param value number of written Sidewiki entries or <code>null</code> to
   *     reset
   */
    public void setValue(Integer value) {
        throwExceptionIfImmutable();
        this.value = value;
    }

    /**
   * Returns whether it has the number of written Sidewiki entries.
   *
   * @return whether it has the number of written Sidewiki entries
   */
    public boolean hasValue() {
        return getValue() != null;
    }

    @Override
    protected void validate() {
        if (value != null && value < 0) {
            throw new IllegalStateException("Text content must be non-negative: " + value);
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
        ExtensionDescription desc = ExtensionDescription.getDefaultDescription(EntriesNumber.class);
        desc.setRequired(required);
        desc.setRepeatable(repeatable);
        return desc;
    }

    @Override
    protected void putAttributes(AttributeGenerator generator) {
        generator.setContent(value.toString());
    }

    @Override
    protected void consumeAttributes(AttributeHelper helper) throws ParseException {
        value = helper.consumeInteger(null, false);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!sameClassAs(obj)) {
            return false;
        }
        EntriesNumber other = (EntriesNumber) obj;
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
        return "{EntriesNumber value=" + value + "}";
    }
}
