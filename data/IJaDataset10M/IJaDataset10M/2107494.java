package jpcsp.HLE.modules280;

import jpcsp.HLE.HLEFunction;
import jpcsp.hardware.Model;

public class SysMemForKernel extends jpcsp.HLE.modules150.SysMemForKernel {

    @Override
    public String getName() {
        return "SysMemForKernel";
    }

    @Override
    public void start() {
    }

    @Override
    public void stop() {
    }

    @HLEFunction(nid = 0x6373995D, version = 280)
    public int sceKernelGetModel() {
        int result = Model.getModel();
        if (log.isDebugEnabled()) {
            log.debug(String.format("sceKernelGetModel ret: %d", result));
        }
        return result;
    }
}
