package synthdrivers.YamahaSY77;

import core.IPatchDriver;
import core.Patch;
import core.IPatch;
import core.BankDriver;
import javax.sound.midi.SysexMessage;

public class YamahaSY77VoiceBankDriver extends BankDriver implements IPatchDriver {

    private static final int numPatches = 16;

    private YamahaSY77VoiceDriver mVoiceD;

    public YamahaSY77VoiceBankDriver(YamahaSY77VoiceDriver iVoiceD) {
        super("Voice bank", YamahaSY77Device.author, numPatches, 1);
        mVoiceD = iVoiceD;
        singleSysexID = YamahaSY77VoiceDriver.SYSEXID;
        sysexID = "SY77 Voice Bank";
        numSysexMsgs = 16;
        patchSize = 0;
        deviceIDoffset = YamahaSY77VoiceDriver.DEV_ID_OFS;
        bankNumbers = YamahaSY77VoiceDriver.SINGLE_VOICE_BANK_NUMBERS;
        patchNumbers = YamahaSY77VoiceDriver.SINGLE_VOICE_PATCH_NUMBERS;
    }

    public Patch createNewPatch() {
        BankPatch bank = new BankPatch(this, sysexID);
        int i;
        for (i = 0; i < getNumPatches(); i++) bank.putSingle(mVoiceD.createNewPatch(), i);
        return bank;
    }

    protected void putPatch(Patch bank, Patch patch, int patchNum) {
        ((BankPatch) bank).putSingle(patch, patchNum);
    }

    protected void deletePatch(Patch single, int patchNum) {
        setPatchName(single, patchNum, "          ");
    }

    protected Patch getPatch(Patch bank, int patchNum) {
        return ((BankPatch) bank).getSingle(patchNum);
    }

    protected String getPatchName(Patch bank, int patchNum) {
        return ((BankPatch) bank).getSingle(patchNum).getName();
    }

    protected void setPatchName(Patch bank, int patchNum, String name) {
        ((BankPatch) bank).getSingle(patchNum).setName(name);
    }

    protected void storePatch(Patch bank, int bankNum, int patchNum) {
        int i;
        for (i = 0; i < getNumPatches(); i++) mVoiceD.storePatch(((BankPatch) bank).getSingle(i), bankNum, i);
    }

    public void requestPatchDump(int bankNum, int patchNum) {
        int i;
        for (i = 0; i < getNumPatches(); i++) {
            mVoiceD.requestPatchDump(bankNum, i);
            try {
                Thread.sleep(400);
            } catch (Exception e) {
            }
        }
    }

    public IPatch[] createPatches(SysexMessage[] msgs) {
        BankPatch bank = new BankPatch(this, sysexID);
        int i, j;
        int n = msgs.length;
        if (n > getNumPatches()) n = getNumPatches();
        for (i = 0; i < n; i++) {
            bank.putSingle((Patch) mVoiceD.createPatch(msgs[i].getMessage()), i);
        }
        IPatch[] res = new IPatch[1];
        res[0] = bank;
        return res;
    }

    public boolean supportsPatch(String patchString, byte[] sysex) {
        try {
            String id = new String(sysex, "ISO-8859-1");
            return id.equals(sysexID);
        } catch (Exception e) {
            return false;
        }
    }

    public java.lang.String getPatchName(Patch bank) {
        return ((BankPatch) bank).getBankName();
    }

    public void setPatchName(Patch bank, String name) {
        ((BankPatch) bank).setBankName(name);
    }
}
