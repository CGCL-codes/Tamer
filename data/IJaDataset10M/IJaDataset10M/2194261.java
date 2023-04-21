package synthdrivers.YamahaDX7.common;

import core.BankDriver;
import core.ErrorMsg;
import core.Patch;
import core.SysexHandler;

public class DX7FamilyFractionalScalingBankDriver extends BankDriver {

    byte[] initSysex;

    String[] dxPatchNumbers;

    String[] dxBankNumbers;

    private static final int dxSinglePackedSize = 505;

    private static final int dxSysexHeaderSize = 4;

    public DX7FamilyFractionalScalingBankDriver(byte[] initSysex, String[] dxPatchNumbers, String[] dxBankNumbers) {
        super("Fractional Scaling Bank", "Torsten Tittmann", dxPatchNumbers.length, 4);
        this.initSysex = initSysex;
        this.dxPatchNumbers = dxPatchNumbers;
        this.dxBankNumbers = dxBankNumbers;
        sysexID = "f0430*7e03764c4d2020464b53594320";
        sysexRequestDump = new SysexHandler("f0 43 @@ 7e 4c 4d 20 20 46 4b 53 59 43 20 f7 ");
        deviceIDoffset = 2;
        patchNameStart = 0;
        patchNameSize = 0;
        bankNumbers = dxBankNumbers;
        patchNumbers = dxPatchNumbers;
        singleSysexID = "f0430*7e03764c4d2020464b53594520";
        singleSize = 510;
        numSysexMsgs = 1;
        patchSize = 16165;
        trimSize = patchSize;
    }

    public void calculateChecksum(Patch p) {
    }

    public int getPatchStart(int patchNum) {
        return (dxSinglePackedSize * patchNum) + dxSysexHeaderSize;
    }

    public void putPatch(Patch bank, Patch p, int patchNum) {
        if (!canHoldPatch(p)) {
            DX7FamilyStrings.dxShowError(toString(), "This type of patch does not fit in to this type of bank.");
            return;
        }
        ((Patch) bank).sysex[getPatchStart(patchNum) + 0] = (byte) (0x03);
        ((Patch) bank).sysex[getPatchStart(patchNum) + 1] = (byte) (0x76);
        ((Patch) bank).sysex[getPatchStart(patchNum) + 2] = (byte) (0x4c);
        ((Patch) bank).sysex[getPatchStart(patchNum) + 3] = (byte) (0x4d);
        ((Patch) bank).sysex[getPatchStart(patchNum) + 4] = (byte) (0x20);
        ((Patch) bank).sysex[getPatchStart(patchNum) + 5] = (byte) (0x20);
        ((Patch) bank).sysex[getPatchStart(patchNum) + 6] = (byte) (0x46);
        ((Patch) bank).sysex[getPatchStart(patchNum) + 7] = (byte) (0x4b);
        ((Patch) bank).sysex[getPatchStart(patchNum) + 8] = (byte) (0x53);
        ((Patch) bank).sysex[getPatchStart(patchNum) + 9] = (byte) (0x59);
        ((Patch) bank).sysex[getPatchStart(patchNum) + 10] = (byte) (0x43);
        ((Patch) bank).sysex[getPatchStart(patchNum) + 11] = (byte) (0x20);
        for (int i = 0; i < 492; i++) {
            ((Patch) bank).sysex[getPatchStart(patchNum) + 12 + i] = (byte) ((Patch) p).sysex[16 + i];
        }
        calculateChecksum(bank, getPatchStart(patchNum) + 2, getPatchStart(patchNum) + 12 + 492 - 1, getPatchStart(patchNum) + 12 + 492);
    }

    public Patch getPatch(Patch bank, int patchNum) {
        try {
            byte[] sysex = new byte[singleSize];
            sysex[0] = (byte) 0xf0;
            sysex[1] = (byte) 0x43;
            sysex[2] = (byte) 0x00;
            sysex[3] = (byte) 0x7e;
            sysex[4] = (byte) 0x03;
            sysex[5] = (byte) 0x76;
            sysex[6] = (byte) 0x4c;
            sysex[7] = (byte) 0x4d;
            sysex[8] = (byte) 0x20;
            sysex[9] = (byte) 0x20;
            sysex[10] = (byte) 0x46;
            sysex[11] = (byte) 0x4b;
            sysex[12] = (byte) 0x53;
            sysex[13] = (byte) 0x59;
            sysex[14] = (byte) 0x45;
            sysex[15] = (byte) 0x20;
            sysex[singleSize - 1] = (byte) 0xf7;
            for (int i = 0; i < 492; i++) {
                sysex[16 + i] = (byte) (((Patch) bank).sysex[getPatchStart(patchNum) + 12 + i]);
            }
            Patch p = new Patch(sysex, getDevice());
            p.calculateChecksum();
            return p;
        } catch (Exception e) {
            ErrorMsg.reportError(getManufacturerName() + " " + getModelName(), "Error in " + toString(), e);
            return null;
        }
    }

    public Patch createNewPatch() {
        byte[] sysex = new byte[trimSize];
        sysex[0] = (byte) 0xF0;
        sysex[1] = (byte) 0x43;
        sysex[2] = (byte) 0x00;
        sysex[3] = (byte) 0x7e;
        sysex[trimSize - 1] = (byte) 0xF7;
        Patch v = new Patch(initSysex, getDevice());
        Patch p = new Patch(sysex, this);
        for (int i = 0; i < getNumPatches(); i++) putPatch(p, v, i);
        return p;
    }

    protected String getPatchName(Patch bank, int patchNum) {
        return "-";
    }

    protected void setPatchName(Patch bank, int patchNum, String name) {
    }
}
