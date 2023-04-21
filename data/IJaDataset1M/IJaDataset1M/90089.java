package de.javatt.data.scenario;

import junit.framework.TestCase;

public class TestTestResultValueImpl extends TestCase {

    public void testTestResultValueImpl() {
        TestResultValue value = new TestResultValueImpl(TestResultValue.FATAL);
        assertTrue(value.getValue() == TestResultValue.FATAL.getValue());
        assertEquals(value.getValueString(), TestResultValue.FATAL.getValueString());
    }

    public void testTestResultValueImplNullValue() {
        NullPointerException nullPointer = null;
        TestResultValue value = null;
        try {
            value = new TestResultValueImpl(null);
        } catch (NullPointerException nullP) {
            nullPointer = nullP;
        }
        assertNotNull(nullPointer);
        assertNull(value);
    }
}
