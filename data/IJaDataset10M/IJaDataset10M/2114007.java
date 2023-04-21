package synthdrivers.YamahaDX7.common;

import core.Driver;
import core.JSLFrame;
import core.Patch;
import core.SysexHandler;

public class DX7FamilySystemSetupIIDriver extends Driver {

    byte[] initSysex;

    String[] dxPatchNumbers;

    String[] dxBankNumbers;

    public DX7FamilySystemSetupIIDriver(byte[] initSysex, String[] dxPatchNumbers, String[] dxBankNumbers) {
        super("System Setup", "Torsten Tittmann");
        this.initSysex = initSysex;
        this.dxPatchNumbers = dxPatchNumbers;
        this.dxBankNumbers = dxBankNumbers;
        sysexID = "F0430*7E005f4c4d2020383937335320";
        patchNameStart = 0;
        patchNameSize = 0;
        deviceIDoffset = 2;
        checksumOffset = 101;
        checksumStart = 6;
        checksumEnd = 100;
        patchNumbers = dxPatchNumbers;
        bankNumbers = dxBankNumbers;
        patchSize = 103;
        trimSize = 103;
        numSysexMsgs = 1;
        sysexRequestDump = new SysexHandler("f0 43 @@ 7e 4c 4d 20 20 38 39 37 33 53 20 f7");
    }

    public Patch createNewPatch() {
        return new Patch(initSysex, this);
    }

    public JSLFrame editPatch(Patch p) {
        return new DX7FamilySystemSetupIIEditor(getManufacturerName() + " " + getModelName() + " \"" + getPatchType() + "\" Editor", (Patch) p);
    }
}
