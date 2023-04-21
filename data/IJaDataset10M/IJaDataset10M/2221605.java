package org.jsynthlib.synthdrivers.RolandMKS50;

import org.jsynthlib.core.BankDriver;
import org.jsynthlib.core.Device;
import org.jsynthlib.core.Logger;
import org.jsynthlib.core.Patch;

/**
 * @author Kenneth L. Martinez
 */
public class MKS50PatchBankDriver extends BankDriver {

    public MKS50PatchBankDriver(final Device device) {
        super(device, "Patch Bank", "Kenneth L. Martinez", 64, 4);
        sysexID = "F041370*23300100";
        patchSize = 4256;
        patchNameStart = 5;
        patchNameSize = 8;
        deviceIDoffset = 3;
        bankNumbers = new String[] { "Patch Bank" };
        patchNumbers = new String[] { "11-", "12-", "13-", "14-", "15-", "16-", "17-", "18-", "21-", "22-", "23-", "24-", "25-", "26-", "27-", "28-", "31-", "32-", "33-", "34-", "35-", "36-", "37-", "38-", "41-", "42-", "43-", "44-", "45-", "46-", "47-", "48-", "51-", "52-", "53-", "54-", "55-", "56-", "57-", "58-", "61-", "62-", "63-", "64-", "65-", "66-", "67-", "68-", "71-", "72-", "73-", "74-", "75-", "76-", "77-", "78-", "81-", "82-", "83-", "84-", "85-", "86-", "87-", "88-" };
        singleSize = 31;
        singleSysexID = "F041350*233001";
    }

    public void calculateChecksum(Patch p) {
    }

    public void storePatch(Patch p, int bankNum, int patchNum) {
        sendPatchWorker(p);
    }

    public void putPatch(Patch bank, Patch p, int patchNum) {
        if (!canHoldPatch(p)) {
            Logger.reportError("Error", "This type of patch does not fit in to this type of bank.");
            return;
        }
        byte bankSysex[] = new byte[32];
        bankSysex[0] |= p.sysex[7];
        bankSysex[1] |= (byte) (p.sysex[8] + 4);
        bankSysex[2] |= (byte) (p.sysex[9] + 4);
        bankSysex[3] |= p.sysex[10];
        bankSysex[10] |= (byte) (p.sysex[11] << 4);
        bankSysex[4] |= p.sysex[12];
        bankSysex[5] |= p.sysex[13];
        bankSysex[6] |= p.sysex[14];
        bankSysex[7] |= p.sysex[15];
        bankSysex[9] |= p.sysex[16];
        bankSysex[8] |= (byte) (p.sysex[17] << 4);
        bankSysex[8] |= p.sysex[18];
        bankSysex[10] |= (byte) (p.sysex[19] & 0x60);
        for (int i = 0; i < 10; i++) {
            bankSysex[i + 11] = p.sysex[20 + i];
        }
        byte bankSysexNibbles[] = new byte[64];
        for (int i = 0; i < 32; i++) {
            bankSysexNibbles[i * 2] = (byte) (bankSysex[i] & 0x0F);
            bankSysexNibbles[i * 2 + 1] = (byte) ((bankSysex[i] & 0xF0) >> 4);
        }
        int patchOffset = getPatchStart(patchNum);
        System.arraycopy(bankSysexNibbles, 0, bank.sysex, patchOffset, 64);
    }

    public Patch getPatch(Patch bank, int patchNum) {
        byte bankSysexNibbles[] = new byte[64];
        byte bankSysex[] = new byte[32];
        byte sysex[] = new byte[31];
        sysex[0] = (byte) 0xF0;
        sysex[1] = (byte) 0x41;
        sysex[2] = (byte) 0x35;
        sysex[3] = (byte) 0x00;
        sysex[4] = (byte) 0x23;
        sysex[5] = (byte) 0x30;
        sysex[6] = (byte) 0x01;
        sysex[30] = (byte) 0xF7;
        int patchOffset = getPatchStart(patchNum);
        System.arraycopy(bank.sysex, patchOffset, bankSysexNibbles, 0, 64);
        for (int i = 0; i < 32; i++) {
            bankSysex[i] = (byte) (bankSysexNibbles[i * 2] | bankSysexNibbles[i * 2 + 1] << 4);
        }
        sysex[7] = bankSysex[0];
        sysex[8] = (byte) (bankSysex[1] - 4);
        sysex[9] = (byte) (bankSysex[2] - 4);
        sysex[10] = bankSysex[3];
        sysex[11] = (byte) ((bankSysex[10] & 0x10) >> 4);
        sysex[12] = bankSysex[4];
        sysex[13] = bankSysex[5];
        sysex[14] = bankSysex[6];
        sysex[15] = bankSysex[7];
        sysex[16] = bankSysex[9];
        sysex[17] = (byte) ((bankSysex[8] & 0xF0) >> 4);
        sysex[18] = (byte) (bankSysex[8] & 0x0F);
        sysex[19] = (byte) (bankSysex[10] & 0x60);
        for (int i = 0; i < 10; i++) {
            sysex[20 + i] = (byte) (bankSysex[i + 11] & 0x3F);
        }
        return new Patch(sysex);
    }

    public String getPatchName(Patch p, int patchNum) {
        byte bankSysexNibbles[] = new byte[64];
        byte bankSysex[] = new byte[32];
        char patchName[] = new char[10];
        int patchOffset = getPatchStart(patchNum);
        System.arraycopy(p.sysex, patchOffset, bankSysexNibbles, 0, 64);
        for (int i = 0; i < 32; i++) {
            bankSysex[i] = (byte) (bankSysexNibbles[i * 2] | bankSysexNibbles[i * 2 + 1] << 4);
        }
        for (int i = 0; i < 10; i++) {
            patchName[i] = MKS50ToneSingleDriver.nameChars[(byte) (bankSysex[i + 11] & 0x3F)];
        }
        return new String(patchName);
    }

    protected void setPatchName(Patch bank, int patchNum, String name) {
    }

    public int getPatchStart(int PatchNum) {
        return PatchNum / 4 * 266 + PatchNum % 4 * 64 + 9;
    }
}
