package org.gamegineer.common.persistence.serializable;

import net.jcip.annotations.Immutable;

/**
 * A fake non-serializable class used for testing the object serialization
 * streams.
 * 
 * <p>
 * This class is non-serializable because a) it does not implement {@code
 * Serializable}, b) it is immutable, and c) it does not define a default
 * constructor.
 * </p>
 */
@Immutable
public final class FakeNonSerializableClass {

    /** The integer field. */
    private final int intField_;

    /** The string field. */
    private final String stringField_;

    /**
     * Initializes a new instance of the {@code FakeNonSerializableClass} class.
     * 
     * @param intField
     *        The integer field.
     * @param stringField
     *        The string field; may be {@code null}.
     */
    public FakeNonSerializableClass(final int intField, final String stringField) {
        intField_ = intField;
        stringField_ = stringField;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof FakeNonSerializableClass)) {
            return false;
        }
        final FakeNonSerializableClass other = (FakeNonSerializableClass) obj;
        return (intField_ == other.intField_) && stringField_.equals(other.stringField_);
    }

    /**
     * Gets the integer field.
     * 
     * @return The integer field.
     */
    public int getIntField() {
        return intField_;
    }

    public String getStringField() {
        return stringField_;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = result * 31 + intField_;
        result = result * 31 + stringField_.hashCode();
        return result;
    }
}
