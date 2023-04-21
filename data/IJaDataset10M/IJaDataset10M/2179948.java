package org.jsynthlib.synthdrivers.RolandSPD11;

import org.jsynthlib.core.Device;
import org.jsynthlib.core.Driver;
import org.jsynthlib.core.IPatch;
import org.jsynthlib.core.Patch;
import org.jsynthlib.core.SysexHandler;
import org.jsynthlib.core.JSLFrame;

/**
 *
 * @author peter
 */
public class SPD11SettingsDriver extends Driver {

    static final SysexHandler SYS_REQ = new SysexHandler("F0 41 @@ 60 11 00 *bankNum* 00 00 00 00 00 08 *checkSum* F7");

    /**
     *
     */
    public SPD11SettingsDriver(final Device device) {
        super(device, "Settings", "Peter Geirnaert");
        sysexID = "F041**601200**00";
        deviceIDoffset = 2;
        bankNumbers = Constants.SPD11_PATCHES;
        patchNumbers = new String[] { "FX & Pedal Settings" };
        patchSize = 19;
        patchNameSize = 0;
    }

    /**
     * SPD11PatchDriver CheckSum calculation replaces the offset byte<br>
     * with the result of the formula 128 -(sum % 128)
     * @param p the patch we work with
     * @param start is where the values start adding to the sum
     * @param end  is where the values stops adding to the sum
     * @param offset is where the checksum value is replaced
     */
    protected void calculateChecksum(Patch p, int start, int end, int offset) {
        int sum = 0x00;
        for (int i = start; i < offset; i++) {
            sum += p.sysex[i];
        }
        int chkSm = 0x80 - (sum % 0x80);
        p.sysex[offset] = (byte) chkSm;
    }

    /**
     * @see core.Driver#requestPatchDump(int, int)
     * @param patchNum is always 0 for settings so ignored
     * @param bankNum is the SPD-11 patch we request settings from
     */
    public void requestPatchDump(int bankNum, int patchNum) {
        int checkSum = 0x80 - bankNum - 0x08;
        final SysexHandler.NameValue[] nameValues = { new SysexHandler.NameValue("bankNum", bankNum), new SysexHandler.NameValue("checkSum", checkSum) };
        send(SYS_REQ.toSysexMessage(getDeviceID(), nameValues));
    }

    /**
     * TODO:replace this by an external standard "Settings"
     * @return a new settings patch
     */
    public Patch createNewPatch() {
        byte sysex[] = { (byte) 0xF0, (byte) 0x41, (byte) 0x09, (byte) 0x60, (byte) 0x12, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0x06, (byte) 0x05, (byte) 0x00, (byte) 0x07, (byte) 0x6D, (byte) 0xF7 };
        Patch p = new Patch(sysex, this);
        return p;
    }

    public Patch createNewPatch(byte[] sysex) {
        Patch p = new Patch(sysex, this);
        return p;
    }

    public IPatch createPatch(byte[] sysex) {
        Patch p = new Patch(sysex, this);
        return p;
    }

    public void sendPatch(Patch p) {
        sendPatchWorker(p);
    }

    /**
     * Sends a settings to a set patch<p>
     * @see Patch#send(int, int)
     * @param p
     * @param bankNum is the SPD-11 patch
     * @param patchNum is ignored because it's always 0 for settings
     */
    protected void storePatch(Patch p, int bankNum, int patchNum) {
        p.sysex[6] = (byte) bankNum;
        calculateChecksum(p, 5, 16, 17);
    }

    protected String getPatchName(Patch p) {
        return "FX&PedalSettings _ P" + Constants.SPD11_PATCHES1[p.sysex[6]];
    }

    public JSLFrame editPatch(Patch p) {
        return new SPD11SettingsEditor(p);
    }
}
