package test.net.sf.japaki.beans;

import java.util.ArrayList;
import java.util.List;
import net.sf.japaki.beans.GenericBean;
import net.sf.japaki.beans.Property;
import org.junit.Assert;
import org.junit.Test;
import test.net.sf.japaki.basic.ObjectTest;

/**
 * Test methods to check generic beans.
 * @version 1.0
 * @author Ralph Wagner
 */
public class GenericBeanTest<B extends GenericBean<B>> extends ObjectTest<B> {

    final ArrayList<PropertyTest<Property<B, Object>, B, Object>> propertyTests;

    /**
     * Creates a new test with the specified candidate.
     * @param testObject the candidate of the test
     * @throws NullPointerException if the specified test objects is
     * <code>null</code>.
     */
    @SuppressWarnings("unchecked")
    protected GenericBeanTest(B testObject) {
        super(testObject);
        propertyTests = new ArrayList<PropertyTest<Property<B, Object>, B, Object>>();
        for (Property property : getTestObject().getProperties()) {
            propertyTests.add(PropertyTest.getInstance(property));
        }
    }

    /**
     * Creates a new instance of this class with the specified testObject
     * as test object.
     * @param testObject the candidate of the test
     * @return an instance of this class with the specified testObject
     * as test object.
     * @throws NullPointerException if the specified test objects is
     * <code>null</code>.
     */
    public static final <B extends GenericBean<B>> GenericBeanTest<B> getInstance(B testObject) {
        return new GenericBeanTest<B>(testObject);
    }

    /**
     * Defines the value to be used for testing.
     * @param testValue new test value
     */
    public void setTestValues(List<Object> testValues) {
        for (int i = 0; i < Math.min(propertyTests.size(), testValues.size()); i++) {
            propertyTests.get(i).setTestValue(testValues.get(i));
        }
    }

    public void assertConsistency() {
        super.assertConsistency();
        for (PropertyTest test : propertyTests) {
            test.assertConsistency();
        }
    }

    /**
     * Performs a test cycle that is expected to leave the test object in an
     * equal state as at the start - if everything works fine.
     * @throws AssertionFailedError if the test object does not pass the test.
     */
    public void performCycle() {
        assertConsistency();
        for (PropertyTest<Property<B, Object>, B, Object> test : propertyTests) {
            test.performCycle(getTestObject());
        }
    }
}
