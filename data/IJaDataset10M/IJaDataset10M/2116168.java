package org.apache.harmony.jndi.tests.javax.naming.ldap;

import javax.naming.ldap.BasicControl;
import junit.framework.TestCase;

public class BasicControlTest extends TestCase {

    /**
     * <p>
     * Test method for 'javax.naming.ldap.BasicControl.BasicControl'
     * </p>
     */
    public void testBasicControl() {
        new BasicControl(null);
        new BasicControl("");
        new BasicControl("1.2.3.333");
        new BasicControl("", true, null);
        new BasicControl("", false, new byte[0]);
        new BasicControl(null, false, null);
    }

    /**
     * Test method for {@link javax.naming.ldap.BasicControl#isCritical()}.
     */
    public void testIsCritical() {
        BasicControl bc = new BasicControl("fixture");
        assertFalse(bc.isCritical());
        bc = new BasicControl(null, false, null);
        assertFalse(bc.isCritical());
        bc = new BasicControl(null, true, null);
        assertTrue(bc.isCritical());
    }

    /**
     * @tests javax.naming.ldap.BasicControl#getID()
     */
    public void testGetID() {
        String ID = "somestring";
        assertSame(ID, new BasicControl(ID).getID());
        assertNull(new BasicControl(null).getID());
        assertNull(new BasicControl(null, false, new byte[1]).getID());
    }

    /**
     * <p>
     * Test method for 'javax.naming.ldap.BasicControl.getEncodedValue()'
     * </p>
     * <p>
     * Here we are testing the return method of the encoded value of
     * BasicControl. In this case we send an encoded value null.
     * </p>
     * <p>
     * The expected result is a null encoded value.
     * </p>
     */
    public void testGetEncodedValue() {
        assertNull(new BasicControl("control", true, null).getEncodedValue());
        byte[] test = new byte[15];
        BasicControl bc = new BasicControl("control", true, test);
        assertSame(test, bc.getEncodedValue());
    }
}
