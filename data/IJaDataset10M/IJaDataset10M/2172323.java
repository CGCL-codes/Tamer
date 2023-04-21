package com.volantis.shared.metadata.impl.value;

import com.volantis.shared.inhibitor.ImmutableInhibitor;
import com.volantis.shared.inhibitor.MutableInhibitor;
import com.volantis.shared.metadata.impl.MetaDataHelper;
import com.volantis.shared.metadata.value.StringValue;

/**
 * Implementation of {@link StringValue}.
 */
abstract class StringValueImpl extends SimpleValueImpl implements StringValue {

    /**
     * The String value;
     */
    private String value;

    /**
     * Copy constructor.
     *
     * @param value The object to copy.
     */
    protected StringValueImpl(StringValue value) {
        this.value = value.getValueAsString();
    }

    /**
     * Protected constructor for sub classes.
     */
    protected StringValueImpl() {
    }

    public ImmutableInhibitor createImmutable() {
        return new ImmutableStringValueImpl(this);
    }

    public MutableInhibitor createMutable() {
        return new MutableStringValueImpl(this);
    }

    public String getAsString() {
        return value;
    }

    public void setFromString(String value) {
        this.value = value;
    }

    public String getValueAsString() {
        return value;
    }

    /**
     * Set the underlying String value.
     *
     * <p>This is implemented here as a convenience to simplify the
     * implementation by not requiring derived classes from duplicating this
     * code.</p>
     *
     * <p><strong>Note</strong>: This must only be invoked through the
     * relevant mutator interface; it must never be called directly on this
     * object.</p>
     *
     * @param string The String value.
     */
    public void setValue(String string) {
        value = string;
    }

    public int hashCode() {
        return MetaDataHelper.hashCode(value);
    }

    public boolean equals(Object other) {
        return (other instanceof StringValue) ? equalsStringValue((StringValue) other) : false;
    }

    /**
     * Helper method for {@link #equals} which compares two objects of this type for
     * equality.
     * @param other The other <code>StringValue</code> to compare this one to.
     * @return true if the all externally visible fields of the other
     *         <code>StringValue</code> are equal to this one.
     */
    protected boolean equalsStringValue(StringValue other) {
        return MetaDataHelper.equals(value, other.getValueAsString());
    }
}
