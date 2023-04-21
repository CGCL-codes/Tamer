package com.phloc.commons.vminit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.phloc.commons.annotations.IsSPIImplementation;

@IsSPIImplementation
public final class MockVirtualMachineSPI implements IVirtualMachineEventSPI {

    private static final Logger s_aLogger = LoggerFactory.getLogger(MockVirtualMachineSPI.class);

    private static int s_nInstanceCount = 0;

    public MockVirtualMachineSPI() {
        s_nInstanceCount++;
    }

    public void onVirtualMachineStart() {
        s_aLogger.info("onVirtualMachineStart");
    }

    public void onVirtualMachineStop() {
        s_aLogger.info("onVirtualMachineStop");
    }

    public static int getInstanceCount() {
        return s_nInstanceCount;
    }
}
