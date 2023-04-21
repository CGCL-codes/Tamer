package jp.ne.nifty.iga.midori.shell.io;

import java.io.*;

final class MdShellBase64Util {

    private byte[] base64charValueList = null;

    public MdShellBase64Util() {
        try {
            base64charValueList = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=".getBytes("ISO8859_1");
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        }
    }

    public final int decode(int iRead) {
        if (iRead == 0x2b) return 62; else if (iRead == 0x2f) return 63; else if (iRead >= 0x30 && iRead <= 0x39) return iRead - 0x30 + 52; else if (iRead == 0x3d) return 64; else if (iRead >= 0x41 && iRead <= 0x5a) return iRead - 0x41; else if (iRead >= 0x61 && iRead <= 0x7a) return iRead - 0x61 + 26; else return -1;
    }

    public final byte[] encodeBuf(int[] bufInput, int iLength) {
        if (iLength == 1) {
            bufInput[1] = 0x00;
            bufInput[2] = 0x00;
        } else if (iLength == 2) {
            bufInput[2] = 0x00;
        }
        int[] iByteWork = new int[4];
        iByteWork[0] = (bufInput[0] & 0xfc) >> 2;
        iByteWork[1] = ((bufInput[0] & 0x03) << 4) | ((bufInput[1] & 0xf0) >> 4);
        iByteWork[2] = ((bufInput[1] & 0x0f) << 2) | ((bufInput[2] & 0xc0) >> 6);
        iByteWork[3] = (bufInput[2] & 0x3f);
        byte[] iByteReturn = new byte[4];
        for (int count = 0; count < 4; count++) iByteReturn[count] = base64charValueList[iByteWork[count]];
        if (iLength == 1) {
            iByteReturn[2] = (byte) '=';
            iByteReturn[3] = (byte) '=';
        } else if (iLength == 2) {
            iByteReturn[3] = (byte) '=';
        }
        return iByteReturn;
    }

    public final int decodeBuf(int[] iByteRead, int[] bufOutput) {
        int[] iByteWork = new int[4];
        int iBufferLength = 0;
        if (iByteRead[3] == '=') {
            if (iByteRead[2] == '=') iBufferLength = 1; else iBufferLength = 2;
        } else iBufferLength = 3;
        for (int index = 0; index < 4; index++) {
            if (iByteRead[index] < 0) iByteWork[index] = (-1); else iByteWork[index] = decode(iByteRead[index]);
        }
        if (iByteWork[0] >= 0) {
            bufOutput[0] = iByteWork[0] << 2;
            if (iByteWork[1] >= 0) {
                bufOutput[0] |= (iByteWork[1] & 0x30) >> 4;
                bufOutput[1] = (iByteWork[1] & 0x0f) << 4;
                if (iByteWork[2] >= 0) {
                    bufOutput[1] |= (iByteWork[2] & 0x3c) >> 2;
                    bufOutput[2] = (iByteWork[2] & 0x03) << 6;
                    if (iByteWork[3] >= 0) bufOutput[2] |= (iByteWork[3]);
                }
            }
        }
        if (iBufferLength == 1) {
            bufOutput[1] = -1;
            bufOutput[2] = -1;
        } else if (iBufferLength == 2) bufOutput[2] = -1;
        return iBufferLength;
    }
}
