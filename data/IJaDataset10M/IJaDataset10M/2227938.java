package org.josef.test.ejb;

import static org.josef.annotations.Status.Stage.PRODUCTION;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.josef.annotations.Review;
import org.josef.annotations.Reviews;
import org.josef.annotations.Status;
import org.josef.ejb.ValueObject;
import org.josef.ejb.ValueObjectInterface;
import org.josef.util.InvalidArgumentException;
import org.josef.util.InvalidStateException;
import org.junit.Test;

/**
 * JUnit test class for class {@link ValueObject}.
 * @author Kees Schotanus
 * @version 1.0 $Revision: 2840 $
 */
@Status(stage = PRODUCTION)
@Reviews({ @Review(by = "Kees Schotanus", at = "2010-05-10", reason = "Initial review") })
public final class ValueObjectTest {

    /**
     * Tests {@link ValueObject#ValueObject(Long, Integer)}.
     */
    @Test
    public void testValueObject() {
        final DemoValueObject valueObject = new DemoValueObject(1L, 1);
        assertEquals("Object ID must have been set", Long.valueOf(1L), valueObject.getObjectId());
        assertEquals("Version must have been set", Integer.valueOf(1), valueObject.getVersion());
    }

    /**
     * Tests {@link ValueObject#ValueObject(ValueObject)}.
     * @throws InvalidArgumentException Never!
     * @throws InvalidStateException Never!
     */
    @Test
    public void testCopyConstructor() throws InvalidArgumentException, InvalidStateException {
        final DemoValueObject source = new DemoValueObject(1L, 1);
        source.setValue(2L);
        final DemoValueObject copy = new DemoValueObject(source);
        assertNull("Object ID must not be copied", copy.getObjectId());
        assertNull("Version must not be copied", copy.getVersion());
        assertEquals("Value must be equal", source.getValue(), copy.getValue());
    }

    /**
     * Tests {@link ValueObject#validate()}.
     * @throws InvalidArgumentException Never.
     * @throws InvalidStateException  Always.
     */
    @Test(expected = InvalidStateException.class)
    public void testValidateWithIncorrectObject() throws InvalidArgumentException, InvalidStateException {
        final DemoValueObject valueObject = new DemoValueObject(1L, 1);
        valueObject.validate();
    }

    /**
     * Tests {@link ValueObject#equals(Object)}.
     */
    @Test
    public void testEquals() {
        final DemoValueObject one = new DemoValueObject(1L, 1);
        final DemoValueObject alsoOne = new DemoValueObject(1L, 2);
        final DemoValueObject two = new DemoValueObject(2L, 1);
        assertEquals("Value objects with equal objectId are equal", one, one);
        assertEquals("Value objects with equal objectId are equal", one, alsoOne);
        assertFalse("Different object ID's", one.equals(two));
        assertFalse("Different object ID's", two.equals(one));
    }

    /**
     * Tests {@link ValueObject#hashCode()}.
     */
    @Test
    public void testHashCode() {
        final DemoValueObject one = new DemoValueObject(1L, 2);
        final DemoValueObject first = new DemoValueObject(1L, 1);
        final DemoValueObject nullId = new DemoValueObject(null, 2);
        assertEquals("Equal ID's results in the same hash codes", one.hashCode(), first.hashCode());
        assertEquals("Null ID gives 0 hash code", 0, nullId.hashCode());
    }

    /**
     * Tests {@link ValueObject#toString()}.
     */
    @Test
    public void testToString() {
        final DemoValueObject one = new DemoValueObject(1L, 2);
        assertTrue("Should start with class name:", one.toString().startsWith(DemoValueObject.class.getSimpleName() + ":"));
    }
}

/**
 * Concrete implementation of a ValueObject to test the abstract ValueObject.
 * @author Kees Schotanus
 * @version 1.0 $Revision: 2840 $
 */
class DemoValueObject extends ValueObject {

    /**
     * Universal version identifier for this serializable class.
     */
    private static final long serialVersionUID = 5963316832628363664L;

    /**
     * Value.
     */
    private Long value;

    /**
     * Copy constructor.
     * @param source The source.
     * @throws InvalidStateException When the superclass constructor throws this
     *  exception.
     */
    public DemoValueObject(final ValueObject source) throws InvalidStateException {
        super(source);
        copyFrom(source);
    }

    /**
     * Creates this value object using the supplied objectId and version.
     * @param objectId The object Id for the DemoValueObject.
     * @param version The version for the DemoValueObject.
     */
    public DemoValueObject(final Long objectId, final Integer version) {
        super(objectId, version);
    }

    /**
     * Gets the value.
     * @return The value.
     */
    public final Long getValue() {
        return value;
    }

    /**
     * Sets the value.
     * @param value The value.
     * @throws InvalidArgumentException When the supplied value is null.
     */
    public final void setValue(final Long value) throws InvalidArgumentException {
        if (value == null) {
            throw new InvalidArgumentException("Value can't be null!");
        }
        this.value = value;
    }

    /**
     * {@inheritDoc}.
     */
    @Override
    public final DemoValueObject copyFrom(final ValueObjectInterface source) throws InvalidStateException {
        try {
            setValue(((DemoValueObject) source).value);
        } catch (final InvalidArgumentException exception) {
            throw new InvalidStateException(exception, exception.getMessage());
        }
        return this;
    }
}
