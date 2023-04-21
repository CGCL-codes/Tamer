package synthdrivers.YamahaDX7.common;

import core.Driver;
import core.JSLFrame;
import core.Patch;
import core.SysexHandler;

public class DX7FamilyFractionalScalingSingleDriver extends Driver {

    byte[] initSysex;

    String[] dxPatchNumbers;

    String[] dxBankNumbers;

    public DX7FamilyFractionalScalingSingleDriver(byte[] initSysex, String[] dxPatchNumbers, String[] dxBankNumbers) {
        super("Single Fractional Scaling", "Torsten Tittmann");
        this.initSysex = initSysex;
        this.dxPatchNumbers = dxPatchNumbers;
        this.dxBankNumbers = dxBankNumbers;
        sysexID = "f0430*7e03764c4d2020464b53594520";
        sysexRequestDump = new SysexHandler("f0 43 @@ 7e 4c 4d 20 20  46 4b 53 59 45 20 f7");
        patchNameStart = 0;
        patchNameSize = 0;
        deviceIDoffset = 2;
        checksumOffset = 508;
        checksumStart = 6;
        checksumEnd = 507;
        bankNumbers = dxBankNumbers;
        patchNumbers = dxPatchNumbers;
        patchSize = 510;
        trimSize = 510;
        numSysexMsgs = 1;
    }

    public Patch createNewPatch() {
        return new Patch(initSysex, this);
    }

    public JSLFrame editPatch(Patch p) {
        return new DX7FamilyFractionalScalingEditor(getManufacturerName() + " " + getModelName() + " \"" + getPatchType() + "\" Editor", (Patch) p);
    }
}
