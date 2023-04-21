package org.jsynthlib.synthdrivers.RolandMT32;

import java.io.UnsupportedEncodingException;
import javax.swing.JOptionPane;
import org.jsynthlib.core.BankDriver;
import org.jsynthlib.core.Device;
import org.jsynthlib.core.DriverUtil;
import org.jsynthlib.core.Logger;
import org.jsynthlib.core.Patch;
import org.jsynthlib.core.SysexHandler;

public class RolandMT32PatchMemoryBankDriver extends BankDriver {

    /** Header Size */
    private static final int HSIZE = 8;

    /** Single Patch size */
    private static final int SSIZE = 16;

    /** the number of single patches in a bank patch. */
    private static final int NS = 128;

    private static final SysexHandler SYS_REQ = new SysexHandler("F0 41 10 16 11 03 *bankNum* *patchNum* 00 00 0F *checkSum* F7");

    public RolandMT32PatchMemoryBankDriver(final Device device) {
        super(device, "Patch Memory Bank", "Fred Jan Kraan", NS, 4);
        sysexID = "F041**16";
        deviceIDoffset = 0;
        bankNumbers = new String[] { "" };
        patchNumbers = new String[32 * 4];
        System.arraycopy(DriverUtil.generateNumbers(1, 128, "##"), 0, patchNumbers, 0, 128);
        singleSysexID = "F041**16";
        singleSize = HSIZE + SSIZE + 1;
        patchSize = HSIZE + SSIZE * NS + 1;
    }

    public int getPatchStart(int patchNum) {
        return HSIZE + (SSIZE * patchNum);
    }

    public String getPatchName(Patch p, int patchNum) {
        int nameStart = getPatchStart(patchNum);
        nameStart += 0;
        try {
            return new String(p.sysex, nameStart, 10, "US-ASCII");
        } catch (UnsupportedEncodingException ex) {
            return "-";
        }
    }

    public void setPatchName(Patch p, int patchNum, String name) {
        patchNameSize = 10;
        patchNameStart = getPatchStart(patchNum);
        if (name.length() < patchNameSize) {
            name += "            ";
        }
        try {
            byte[] namebytes = name.getBytes("US-ASCII");
            System.arraycopy(namebytes, 0, p.sysex, patchNameStart, patchNameSize);
        } catch (UnsupportedEncodingException ex) {
        }
    }

    protected void calculateChecksum(Patch p, int start, int end, int ofs) {
        int i;
        int sum = 0;
        for (i = start; i <= end; i++) {
            sum += p.sysex[i];
        }
        sum += 0xA5;
        p.sysex[ofs] = (byte) (sum % 128);
    }

    public void calculateChecksum(Patch p) {
        for (int i = 0; i < NS; i++) {
            calculateChecksum(p, HSIZE + (i * SSIZE), HSIZE + (i * SSIZE) + SSIZE - 2, HSIZE + (i * SSIZE) + SSIZE - 1);
        }
    }

    public void putPatch(Patch bank, Patch p, int patchNum) {
        if (!canHoldPatch(p)) {
            JOptionPane.showMessageDialog(null, "This type of patch does not fit in to this type of bank.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        System.arraycopy(p.sysex, HSIZE, bank.sysex, getPatchStart(patchNum), SSIZE);
        calculateChecksum(bank);
    }

    public Patch getPatch(Patch bank, int patchNum) {
        int addressISB = 0x00;
        int addressLSB = 0x00;
        byte[] sysex = new byte[HSIZE + SSIZE + 1];
        sysex[0] = (byte) 0xF0;
        sysex[1] = (byte) 0x41;
        sysex[2] = (byte) 0x10;
        sysex[3] = (byte) 0x16;
        sysex[4] = (byte) 0x11;
        sysex[5] = (byte) 0x04;
        sysex[6] = (byte) addressISB;
        sysex[7] = (byte) addressLSB;
        sysex[8] = (byte) patchNum;
        sysex[9] = (byte) 0x00;
        sysex[10] = (byte) 0x01;
        sysex[11] = (byte) 0x76;
        sysex[12] = (byte) 0x04;
        sysex[HSIZE + SSIZE] = (byte) 0xF7;
        System.arraycopy(bank.sysex, getPatchStart(patchNum), sysex, HSIZE, SSIZE);
        try {
            Patch p = new Patch(sysex, getDevice());
            p.calculateChecksum();
            return p;
        } catch (Exception e) {
            Logger.reportError("Error", "Error in MT32 Bank Driver", e);
            return null;
        }
    }

    public Patch createNewPatch() {
        byte[] sysex = new byte[HSIZE + SSIZE * NS + 1];
        sysex[0] = (byte) 0xF0;
        sysex[1] = (byte) 0x41;
        sysex[2] = (byte) 0x10;
        sysex[3] = (byte) 0x16;
        sysex[4] = (byte) 0x11;
        sysex[5] = (byte) 0x04;
        sysex[6] = (byte) 0x00;
        sysex[7] = (byte) 0x00;
        sysex[8] = (byte) 0x00;
        sysex[9] = (byte) 0x01;
        sysex[10] = (byte) 0x76;
        sysex[11] = (byte) 0x00;
        sysex[HSIZE + SSIZE * NS] = (byte) 0xF7;
        Patch p = new Patch(sysex, this);
        for (int i = 0; i < NS; i++) {
            setPatchName(p, i, "New PM Patch");
        }
        calculateChecksum(p);
        return p;
    }

    public void requestPatchDump(int bankNum, int patchNum) {
        send(SYS_REQ.toSysexMessage(getChannel(), new SysexHandler.NameValue("bankNum", bankNum << 1)));
    }

    public void storePatch(Patch p, int bankNum, int patchNum) {
        try {
            Thread.sleep(100);
        } catch (Exception e) {
        }
        p.sysex[3] = (byte) 0x16;
        p.sysex[6] = (byte) bankNum;
        p.sysex[7] = (byte) patchNum;
        sendPatchWorker(p);
        try {
            Thread.sleep(100);
        } catch (Exception e) {
        }
    }
}
