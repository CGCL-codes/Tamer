package org.tinymarbles.model;

import org.tinymarbles.model.PAttribute;
import org.tinymarbles.model.PObject;
import org.tinymarbles.model.PType;
import org.tinymarbles.model.PValue;
import junit.framework.TestCase;

/**
 * @author duke
 * @param <T> Type of the PValue's value property.
 * 
 */
public abstract class PValueAbstractTest<T> extends TestCase implements PValueTestImplementor<T> {

    public static final String TEST_ATT_NAME = "TestAttribute";

    protected final PType testType = new PType("TestType");

    public abstract PValue<T> create(PAttribute att, PObject owner);

    public abstract T createDefaultContent();

    public abstract T[] createComparableContent();

    public abstract Class<? extends PValue<T>> getValueClass();

    public PValueAbstractTest() {
        super();
    }

    protected PAttribute createAttribute() {
        return new PAttribute(TEST_ATT_NAME, this.testType, this.getValueClass());
    }

    protected PObject createOwner() {
        return new PObject(this.testType, null);
    }

    protected PValue<T> createDefault() {
        return create(createAttribute(), createOwner());
    }

    public void testConstructorOk() {
        PAttribute att = createAttribute();
        PObject owner = createOwner();
        assertTrue(owner.getValues().isEmpty());
        PValue<T> tested = this.create(att, owner);
        assertFalse(owner.getValues().isEmpty());
        assertEquals(1, owner.getValues().size());
        assertTrue(this.getValueClass().isInstance(tested));
        assertEquals(owner.getValues().get(tested.getName()), tested);
        assertSame(owner, tested.getOwner());
        assertSame(att, tested.getAttribute());
        assertEquals(TEST_ATT_NAME, tested.getName());
    }

    public void testSetAttribute() {
        PAttribute att = createAttribute();
        PObject owner = createOwner();
        PValue<T> tested = this.create(att, owner);
        assertSame(att, tested.getAttribute());
        assertEquals(TEST_ATT_NAME, tested.getName());
        final String OTHER_NAME = "other";
        PAttribute other = new PAttribute(OTHER_NAME, this.testType, this.getValueClass());
        tested.setAttribute(other);
        assertSame(other, tested.getAttribute());
        assertEquals(OTHER_NAME, tested.getName());
    }

    public void testGetAndSet() {
        PValue<T> tested = this.createDefault();
        T content = this.createDefaultContent();
        assertNull("null before set", tested.getValue());
        tested.setValue(content);
        assertEquals("content set", content, tested.getValue());
        tested.setValue(null);
        assertNull("null after clear", tested.getValue());
    }

    public void testHashCode() {
        PValue<T> one = this.createDefault();
        PValue<T> two = this.createDefault();
        assertEquals("hash code", one.hashCode(), two.hashCode());
    }

    public void testEquals() {
        PValue<T> one = this.createDefault();
        PValue<T> two = this.createDefault();
        assertFalse("not equal to null", one.equals(null));
        assertTrue("equality", one.equals(two));
        T content = this.createDefaultContent();
        one.setValue(content);
        assertFalse("different contents", one.equals(two));
        two.setValue(content);
        assertTrue("same content", one.equals(two));
        two.setValue(this.createDefaultContent());
        assertTrue("equal content", one.equals(two));
    }
}
