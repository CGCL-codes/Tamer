package synthdrivers.YamahaDX7;

import synthdrivers.YamahaDX7.common.DX7FamilyDevice;
import synthdrivers.YamahaDX7.common.DX7FamilyPerformanceBankDriver;
import core.JSLFrame;
import core.Patch;

public class YamahaDX7PerformanceBankDriver extends DX7FamilyPerformanceBankDriver {

    public YamahaDX7PerformanceBankDriver() {
        super(YamahaDX7PerformanceConstants.INIT_PERFORMANCE, YamahaDX7PerformanceConstants.BANK_PERFORMANCE_PATCH_NUMBERS, YamahaDX7PerformanceConstants.BANK_PERFORMANCE_BANK_NUMBERS);
    }

    public Patch createNewPatch() {
        return super.createNewPatch();
    }

    public void storePatch(Patch p, int bankNum, int patchNum) {
        if ((((DX7FamilyDevice) (getDevice())).getTipsMsgFlag() & 0x01) == 1) YamahaDX7Strings.dxShowInformation(toString(), YamahaDX7Strings.PERFORMANCE_STRING);
    }

    public void requestPatchDump(int bankNum, int patchNum) {
        if ((((DX7FamilyDevice) (getDevice())).getTipsMsgFlag() & 0x01) == 1) YamahaDX7Strings.dxShowInformation(toString(), YamahaDX7Strings.PERFORMANCE_STRING);
    }

    public JSLFrame editPatch(Patch p) {
        if ((((DX7FamilyDevice) (getDevice())).getTipsMsgFlag() & 0x01) == 1) YamahaDX7Strings.dxShowInformation(toString(), YamahaDX7Strings.PERFORMANCE_STRING);
        return super.editPatch(p);
    }
}
