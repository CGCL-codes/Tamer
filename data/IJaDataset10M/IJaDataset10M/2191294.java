package com.phloc.commons.vminit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Test;

/**
 * Test class for class {@link VirtualMachineInitializer}.
 * 
 * @author philip
 */
public final class VirtualMachineInitializerTest {

    static {
        VirtualMachineInitializer.runInitialization();
    }

    @Test
    public void testAll() {
        assertEquals(1, MockVirtualMachineSPI.getInstanceCount());
        try {
            VirtualMachineInitializer.runInitialization();
            fail();
        } catch (final IllegalStateException ex) {
        }
        assertEquals(1, MockVirtualMachineSPI.getInstanceCount());
    }
}
