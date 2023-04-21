package synthdrivers.NordLead;

import core.*;
import javax.swing.*;

public class NLDrumBankDriver extends BankDriver {

    static final int BANK_NUM_OFFSET = 4;

    static final int PATCH_NUM_OFFSET = 5;

    static final int NUM_IN_BANK = 10;

    public NLDrumBankDriver() {
        super("Drum Bank", "Kenneth L. Martinez", NLDrumSingleDriver.PATCH_LIST.length, 2);
        sysexID = "F033**04**";
        sysexRequestDump = new SysexHandler("F0 33 @@ 04 *bankNum* *patchNum* F7");
        singleSysexID = "F033**04**";
        singleSize = 1063;
        patchSize = singleSize * NUM_IN_BANK;
        patchNameStart = -1;
        patchNameSize = 0;
        deviceIDoffset = 2;
        bankNumbers = NLDrumSingleDriver.BANK_LIST;
        patchNumbers = NLDrumSingleDriver.PATCH_LIST;
    }

    public void calculateChecksum(Patch p) {
    }

    public void storePatch(Patch p, int bankNum, int patchNum) {
        if (bankNum == 0) {
            JOptionPane.showMessageDialog(PatchEdit.getInstance(), "Cannot send to ROM bank", "Store Patch", JOptionPane.WARNING_MESSAGE);
        } else {
            setBankNum(bankNum);
            setPatchNum(patchNum);
            sendPatchWorker((Patch) p, bankNum);
        }
    }

    public void putPatch(Patch bank, Patch p, int patchNum) {
        if (!canHoldPatch(p)) {
            ErrorMsg.reportError("Error", "This type of patch does not fit in to this type of bank.");
            return;
        }
        System.arraycopy(((Patch) p).sysex, 0, ((Patch) bank).sysex, patchNum * singleSize, singleSize);
        ((Patch) bank).sysex[patchNum * singleSize + PATCH_NUM_OFFSET] = (byte) (patchNum + 99);
    }

    public Patch getPatch(Patch bank, int patchNum) {
        byte sysex[] = new byte[singleSize];
        System.arraycopy(((Patch) bank).sysex, patchNum * singleSize, sysex, 0, singleSize);
        return new Patch(sysex);
    }

    public String getPatchName(Patch p, int patchNum) {
        return "-";
    }

    public void setPatchName(Patch p, int patchNum, String name) {
    }

    protected void sendPatchWorker(Patch p, int bankNum) {
        byte tmp[] = new byte[singleSize];
        try {
            PatchEdit.showWaitDialog();
            for (int i = 0; i < NUM_IN_BANK; i++) {
                System.arraycopy(p.sysex, i * singleSize, tmp, 0, singleSize);
                tmp[deviceIDoffset] = (byte) (((NordLeadDevice) getDevice()).getGlobalChannel() - 1);
                tmp[BANK_NUM_OFFSET] = (byte) (bankNum + 1);
                tmp[PATCH_NUM_OFFSET] = (byte) (i + 99);
                send(tmp);
                Thread.sleep(50);
            }
            PatchEdit.hideWaitDialog();
        } catch (Exception e) {
            ErrorMsg.reportStatus(e);
            ErrorMsg.reportError("Error", "Unable to send Patch");
        }
    }

    public Patch createNewPatch() {
        byte tmp[] = new byte[singleSize];
        byte sysex[] = new byte[patchSize];
        System.arraycopy(NLDrumSingleDriver.NEW_PATCH, 0, tmp, 0, singleSize);
        for (int i = 0; i < NUM_IN_BANK; i++) {
            tmp[PATCH_NUM_OFFSET] = (byte) i;
            System.arraycopy(tmp, 0, sysex, i * singleSize, singleSize);
        }
        return new Patch(sysex, this);
    }

    public void requestPatchDump(int bankNum, int patchNum) {
        int devID = ((NordLeadDevice) getDevice()).getGlobalChannel();
        for (int i = 0; i < NUM_IN_BANK; i++) {
            setBankNum(bankNum);
            setPatchNum(i + 99);
            send(sysexRequestDump.toSysexMessage(devID, new SysexHandler.NameValue("bankNum", 10), new SysexHandler.NameValue("patchNum", 0)));
            try {
                Thread.sleep(400);
            } catch (Exception e) {
                ErrorMsg.reportStatus(e);
                ErrorMsg.reportError("Error", "Unable to request Patch " + i);
            }
        }
    }
}
