package org.callbackparams.combine.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

/**
 * @author Henrik Kaipe
 */
public class TestValuesClassFactory extends junit.framework.TestCase {

    private ArrayList values(final Object[] originalValues, final Class c) {
        try {
            final Object[] values = (Object[]) c.getMethod("values", (Class[]) null).invoke(null, (Object[]) null);
            assertNotSame(ValuesClassFactory.class + " should return a brand new array ", originalValues, values);
            return new ArrayList(Arrays.asList(values));
        } catch (Exception ex) {
            throw new Error(ex);
        }
    }

    private void assertValues(final Object... expectedValues) {
        final ArrayList backupValues = new ArrayList(Arrays.asList(expectedValues));
        final Class valuesClass = ValuesClassFactory.newValuesClass(expectedValues);
        assertEquals("The name of the template class is to be preserved", ValuesClassImpl.class.getName(), valuesClass.getName());
        assertNotSame("Template class and actual class must differ", ValuesClassImpl.class, valuesClass);
        assertFalse("It must not be possible to cast class", ValuesClassImpl.class.isAssignableFrom(valuesClass));
        assertEquals("First check of values", backupValues, values(expectedValues, valuesClass));
        assertEquals("Second check on values", backupValues, values(expectedValues, valuesClass));
        assertEquals("Original expected values must be unchanged", backupValues, new ArrayList(Arrays.asList(expectedValues)));
    }

    public void testIt() {
        assertValues();
        assertValues(new Object());
        assertValues("hosdfef", new Date());
        assertValues(this, "test-value", null, new Object());
    }
}
