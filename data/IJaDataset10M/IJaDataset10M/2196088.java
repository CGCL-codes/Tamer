package com.phloc.commons.system;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * Test class for class {@link EJVMVendor}.
 * 
 * @author philip
 */
public final class EJVMVendorTest {

    @Test
    public void testSun() {
        for (final EJVMVendor e : EJVMVendor.values()) assertSame(e, EJVMVendor.valueOf(e.name()));
        final EJVMVendor eVendor = EJVMVendor.getCurrentVendor();
        assertTrue(eVendor.isSun());
        assertTrue(EJVMVendor.SUN_CLIENT.isSun());
        assertTrue(EJVMVendor.SUN_SERVER.isSun());
        assertFalse(EJVMVendor.UNKNOWN.isSun());
    }
}
