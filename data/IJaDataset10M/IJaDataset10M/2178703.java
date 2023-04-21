package net.sf.irunninglog.businessobject;

import junit.framework.TestCase;

public class TestBusinessObjectFactory extends TestCase {

    private String[] validValues;

    private String[] invalidValues;

    public TestBusinessObjectFactory(String name) {
        super(name);
    }

    public void setUp() {
        validValues = new String[] { "Runner", "RunType", "Route", "Shoe" };
        invalidValues = new String[] { null, "", "foo" };
    }

    public void testValidObjectCreation() {
        for (int i = 0; i < validValues.length; i++) {
            try {
                assertNotNull(BusinessObjectFactory.newInstance(validValues[i]));
            } catch (Throwable ex) {
                fail("Should not be here");
            }
        }
    }

    public void testInvalidObjectCreation() {
        for (int i = 0; i < invalidValues.length; i++) {
            try {
                BusinessObjectFactory.newInstance(invalidValues[i]);
                fail("Should not be here");
            } catch (Throwable ex) {
                assertTrue(true);
            }
        }
    }
}
