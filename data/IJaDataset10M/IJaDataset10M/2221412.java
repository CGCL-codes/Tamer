package jpcsp.HLE.kernel.types;

import jpcsp.HLE.kernel.managers.SceUidManager;

public class SceKernelEventFlagInfo extends pspAbstractMemoryMappedStructureVariableLength {

    public final String name;

    public final int attr;

    public final int initPattern;

    public int currentPattern;

    public int numWaitThreads;

    public final int uid;

    public SceKernelEventFlagInfo(String name, int attr, int initPattern, int currentPattern) {
        this.name = name;
        this.attr = attr;
        this.initPattern = initPattern;
        this.currentPattern = currentPattern;
        numWaitThreads = 0;
        uid = SceUidManager.getNewUid("ThreadMan-eventflag");
    }

    @Override
    protected void write() {
        super.write();
        writeStringNZ(32, name);
        write32(attr);
        write32(initPattern);
        write32(currentPattern);
        write32(numWaitThreads);
    }
}
