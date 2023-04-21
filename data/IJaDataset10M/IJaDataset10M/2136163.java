package org.jsynthlib.synthdrivers.MIDIboxSID;

import java.io.UnsupportedEncodingException;
import javax.swing.JOptionPane;
import org.jsynthlib.core.BankDriver;
import org.jsynthlib.core.Device;
import org.jsynthlib.core.Logger;
import org.jsynthlib.core.Patch;
import org.jsynthlib.core.SysexHandler;

public class MIDIboxSIDBankDriver extends BankDriver {

    public MIDIboxSIDBankDriver(final Device device) {
        super(device, "Bank", "Thorsten Klose", 128, 4);
        sysexID = "F000007E46**04";
        sysexRequestDump = new SysexHandler("F0 00 00 7E 46 @@ 03 F7");
        bankNumbers = new String[] { "BankStick" };
        patchNumbers = new String[] { "INT", "002", "003", "004", "005", "006", "007", "008", "009", "010", "011", "012", "013", "014", "015", "016", "017", "018", "019", "020", "021", "022", "023", "024", "025", "026", "027", "028", "029", "030", "031", "032", "033", "034", "035", "036", "037", "038", "039", "040", "041", "042", "043", "044", "045", "046", "047", "048", "049", "050", "051", "052", "053", "054", "055", "056", "057", "058", "059", "060", "061", "062", "063", "064", "065", "066", "067", "068", "069", "070", "071", "072", "073", "074", "075", "076", "077", "078", "079", "080", "081", "082", "083", "084", "085", "086", "087", "088", "089", "090", "091", "092", "093", "094", "095", "096", "097", "098", "099", "020", "101", "102", "103", "104", "105", "106", "107", "108", "109", "110", "111", "112", "113", "114", "115", "116", "117", "118", "119", "120", "121", "122", "123", "124", "125", "126", "127", "128" };
        patchSize = 128 * 256 + 9;
        deviceIDoffset = 5;
        singleSysexID = "F000007E46**02";
        singleSize = 266;
        checksumStart = 7;
        checksumEnd = 128 * 256 + 7;
        checksumOffset = 128 * 256 + 8;
    }

    public void storePatch(Patch p, int bankNum, int patchNum) {
        MIDIboxSIDSlowSender SlowSender = new MIDIboxSIDSlowSender();
        for (int i = 0; i < 128; ++i) {
            Patch ps = getPatch(p, i);
            send(ps.sysex);
            try {
                Thread.sleep(500);
            } catch (Exception e) {
            }
        }
    }

    public int getPatchStart(int patchNum) {
        int start = (256 * patchNum) + 7;
        return start;
    }

    public String getPatchName(Patch p, int patchNum) {
        int nameStart = getPatchStart(patchNum);
        try {
            return new String(p.sysex, nameStart, 16, "US-ASCII");
        } catch (UnsupportedEncodingException ex) {
            return "-";
        }
    }

    public void setPatchName(Patch p, int patchNum, String name) {
        patchNameSize = 16;
        patchNameStart = getPatchStart(patchNum);
        if (name.length() < patchNameSize) {
            name += "                ";
        }
        try {
            byte[] namebytes = name.getBytes("US-ASCII");
            System.arraycopy(namebytes, 0, p.sysex, patchNameStart, patchNameSize);
        } catch (UnsupportedEncodingException ex) {
            return;
        }
    }

    public void putPatch(Patch bank, Patch p, int patchNum) {
        if (!canHoldPatch(p)) {
            JOptionPane.showMessageDialog(null, "This type of patch does not fit in to this type of bank.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        System.arraycopy(p.sysex, 8, bank.sysex, getPatchStart(patchNum), 256);
        calculateChecksum(bank);
    }

    public Patch getPatch(Patch bank, int patchNum) {
        try {
            byte[] sysex = new byte[266];
            sysex[0] = (byte) 0xF0;
            sysex[1] = (byte) 0x00;
            sysex[2] = (byte) 0x00;
            sysex[3] = (byte) 0x7e;
            sysex[4] = (byte) 0x46;
            sysex[5] = (byte) ((getDeviceID() - 1) & 0x7f);
            sysex[6] = (byte) 0x02;
            sysex[7] = (byte) (patchNum);
            sysex[265] = (byte) 0xF7;
            System.arraycopy(bank.sysex, getPatchStart(patchNum), sysex, 8, 256);
            Patch p = new Patch(sysex, getDevice());
            p.calculateChecksum();
            return p;
        } catch (Exception e) {
            Logger.reportError("Error", "Error in MIDIboxSID Bank Driver", e);
            return null;
        }
    }

    public Patch createNewPatch() {
        byte[] sysex = new byte[128 * 256 + 9];
        sysex[0] = (byte) 0xF0;
        sysex[1] = (byte) 0x00;
        sysex[2] = (byte) 0x00;
        sysex[3] = (byte) 0x7e;
        sysex[4] = (byte) 0x46;
        sysex[5] = (byte) ((getDeviceID() - 1) & 0x7f);
        sysex[6] = (byte) 0x04;
        sysex[128 * 256 + 8] = (byte) 0xF7;
        Patch p = new Patch(sysex, this);
        MIDIboxSIDSingleDriver SingleDriver = new MIDIboxSIDSingleDriver(getDevice());
        Patch ps = SingleDriver.createNewPatch();
        for (int i = 0; i < 128; i++) {
            putPatch(p, ps, i);
        }
        calculateChecksum(p);
        return p;
    }
}
