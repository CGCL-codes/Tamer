package synthdrivers.EmuProteusMPS;

import core.*;
import java.io.*;
import javax.swing.*;

public class EmuProteusMPSBankDriver extends BankDriver {

    public EmuProteusMPSBankDriver() {
        super("Bank", "Brian Klock", 100, 5);
        sysexID = "F01808**01";
        singleSysexID = "F01808**01";
        singleSize = 319;
        patchSize = 31900;
        bankNumbers = new String[] { "0-ROM Bank", "1-RAM Bank", "2-Card Bank", "3-ROM Bank", "4-ROM Bank" };
        patchNumbers = new String[] { "00-", "01-", "02-", "03-", "04-", "05-", "06-", "07-", "08-", "09-", "10-", "11-", "12-", "13-", "14-", "15-", "16-", "17-", "18-", "19-", "20-", "21-", "22-", "23-", "24-", "25-", "26-", "27-", "28-", "29-", "30-", "31-", "32-", "33-", "34-", "35-", "36-", "37-", "38-", "39-", "40-", "41-", "42-", "43-", "44-", "45-", "46-", "47-", "48-", "49-", "50-", "51-", "52-", "53-", "54-", "55-", "56-", "57-", "58-", "59-", "60-", "61-", "62-", "63-", "64-", "65-", "66-", "67-", "68-", "69-", "70-", "71-", "72-", "73-", "74-", "75-", "76-", "77-", "78-", "79-", "80-", "81-", "82-", "83-", "84-", "85-", "86-", "87-", "88-", "89-", "90-", "91-", "92-", "93-", "94-", "95-", "96-", "97-", "98-", "99-" };
        deviceIDoffset = 3;
    }

    public int getPatchStart(int patchNum) {
        int start = (319 * patchNum);
        return start;
    }

    public String getPatchName(Patch p, int patchNum) {
        int nameStart = getPatchStart(patchNum);
        nameStart += 7;
        try {
            StringBuffer s = new StringBuffer(new String(((Patch) p).sysex, nameStart, 24, "US-ASCII"));
            for (int i = 1; i < s.length(); i++) s.deleteCharAt(i);
            return s.toString();
        } catch (UnsupportedEncodingException ex) {
            return "-";
        }
    }

    public void setPatchName(Patch p, int patchNum, String name) {
        patchNameSize = 12;
        patchNameStart = getPatchStart(patchNum) + 7;
        if (name.length() < patchNameSize) name = name + "            ";
        byte[] namebytes = new byte[64];
        try {
            namebytes = name.getBytes("US-ASCII");
            for (int i = 0; i < patchNameSize; i++) ((Patch) p).sysex[patchNameStart + (i * 2)] = namebytes[i];
        } catch (UnsupportedEncodingException ex) {
            return;
        }
    }

    protected void calculateChecksum(Patch p, int start, int end, int ofs) {
        int i;
        int sum = 0;
        for (i = start; i <= end; i++) sum += p.sysex[i];
        p.sysex[ofs] = (byte) (sum % 128);
    }

    public void calculateChecksum(Patch p) {
        for (int i = 0; i < 100; i++) calculateChecksum(p, 7 + (i * 319), 7 + (i * 319) + 309, 7 + (i * 319) + 310);
    }

    public void putPatch(Patch bank, Patch p, int patchNum) {
        if (!canHoldPatch(p)) {
            JOptionPane.showMessageDialog(null, "This type of patch does not fit in to this type of bank.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        System.arraycopy(((Patch) p).sysex, 0, ((Patch) bank).sysex, getPatchStart(patchNum), 319);
        calculateChecksum(bank);
    }

    public Patch getPatch(Patch bank, int patchNum) {
        try {
            byte[] sysex = new byte[319];
            System.arraycopy(((Patch) bank).sysex, getPatchStart(patchNum), sysex, 0, 319);
            Patch p = new Patch(sysex, getDevice());
            p.calculateChecksum();
            return p;
        } catch (Exception e) {
            ErrorMsg.reportError("Error", "Error in Proteus MPS Bank Driver", e);
            return null;
        }
    }

    public void storePatch(Patch p, int bankNum, int patchNum) {
        setBankNum(bankNum);
        for (int i = 0; i < 100; i++) {
            ((Patch) p).sysex[getPatchStart(i) + 5] = (byte) ((bankNum * 100 + i) % 128);
            ((Patch) p).sysex[getPatchStart(i) + 6] = (byte) ((bankNum * 100 + i) / 128);
        }
        sendPatchWorker(p);
    }

    public Patch createNewPatch() {
        byte[] sysex = new byte[31900];
        for (int i = 0; i < 100; i++) {
            sysex[i * 319 + 0] = (byte) 0xF0;
            sysex[i * 319 + 1] = (byte) 0x18;
            sysex[i * 319 + 2] = (byte) 0x08;
            sysex[i * 319 + 3] = (byte) 0x00;
            sysex[i * 319 + 4] = (byte) 0x01;
            sysex[i * 319 + 318] = (byte) 0xF7;
        }
        Patch p = new Patch(sysex, this);
        for (int i = 0; i < 100; i++) setPatchName(p, i, "New Patch");
        calculateChecksum(p);
        return p;
    }
}
