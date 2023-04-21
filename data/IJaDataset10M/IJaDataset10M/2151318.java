package jpcsp.HLE.kernel.types;

import jpcsp.HLE.Modules;
import jpcsp.HLE.kernel.managers.SceUidManager;
import jpcsp.HLE.kernel.types.interrupts.VTimerInterruptHandler;
import jpcsp.HLE.modules.SysMemUserForUser;
import jpcsp.HLE.modules150.SysMemUserForUser.SysMemInfo;
import jpcsp.scheduler.VTimerInterruptAction;
import jpcsp.scheduler.VTimerInterruptResultAction;

public class SceKernelVTimerInfo extends pspAbstractMemoryMappedStructureVariableLength {

    public String name;

    public int active;

    public long base;

    public long current;

    public long schedule;

    public int handlerAddress;

    public int handlerArgument;

    public final int uid;

    public VTimerInterruptHandler vtimerInterruptHandler;

    public final VTimerInterruptAction vtimerInterruptAction;

    public final VTimerInterruptResultAction vtimerInterruptResultAction;

    private int internalMemory;

    private SysMemInfo sysMemInfo;

    public static final int ACTIVE_RUNNING = 1;

    public static final int ACTIVE_STOPPED = 0;

    public SceKernelVTimerInfo(String name) {
        this.name = name;
        active = ACTIVE_STOPPED;
        uid = SceUidManager.getNewUid("ThreadMan-VTimer");
        vtimerInterruptHandler = new VTimerInterruptHandler(this);
        vtimerInterruptAction = new VTimerInterruptAction(this);
        vtimerInterruptResultAction = new VTimerInterruptResultAction(this);
        internalMemory = 0;
    }

    public int getInternalMemory() {
        if (internalMemory == 0) {
            sysMemInfo = Modules.SysMemUserForUserModule.malloc(SysMemUserForUser.USER_PARTITION_ID, "SceKernelVTimerInfo", jpcsp.HLE.modules150.SysMemUserForUser.PSP_SMEM_Low, 16, 0);
            if (sysMemInfo != null) {
                internalMemory = sysMemInfo.addr;
            }
        }
        return internalMemory;
    }

    public void delete() {
        if (internalMemory != 0) {
            Modules.SysMemUserForUserModule.free(sysMemInfo);
            internalMemory = 0;
        }
    }

    @Override
    protected void write() {
        super.write();
        writeStringNZ(32, name);
        write32(active);
        write64(base);
        write64(current);
        write64(schedule);
        write32(handlerAddress);
        write32(handlerArgument);
    }
}
