package org.jsynthlib.synthdrivers.YamahaTX7;

import org.jsynthlib.core.Device;
import org.jsynthlib.core.Patch;
import org.jsynthlib.synthdrivers.YamahaDX7.common.DX7FamilyDevice;
import org.jsynthlib.synthdrivers.YamahaDX7.common.DX7FamilyPerformanceSingleDriver;

public class YamahaTX7PerformanceSingleDriver extends DX7FamilyPerformanceSingleDriver {

    public YamahaTX7PerformanceSingleDriver(final Device device) {
        super(device, YamahaTX7PerformanceConstants.INIT_PERFORMANCE, YamahaTX7PerformanceConstants.SINGLE_PERFORMANCE_PATCH_NUMBERS, YamahaTX7PerformanceConstants.SINGLE_PERFORMANCE_BANK_NUMBERS);
    }

    public void storePatch(Patch p, int bankNum, int patchNum) {
        if ((((DX7FamilyDevice) (getDevice())).getTipsMsgFlag() & 0x01) == 1) {
            YamahaTX7Strings.dxShowInformation(toString(), YamahaTX7Strings.STORE_SINGLE_PERFORMANCE_STRING);
        }
        sendPatchWorker(p);
    }

    public void requestPatchDump(int bankNum, int patchNum) {
        setPatchNum(patchNum);
        send(sysexRequestDump.toSysexMessage(getChannel() + 0x20));
    }
}
