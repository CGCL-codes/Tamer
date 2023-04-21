package net.mkp.libmp4;

import java.io.IOException;
import java.io.RandomAccessFile;
import android.util.Base64;

public class StsdBox {

    private RandomAccessFile fis;

    private byte[] buffer = new byte[4];

    private long pos = 0;

    private byte[] pps = new byte[5];

    private byte[] sps = new byte[11];

    public StsdBox(RandomAccessFile fis, long pos) {
        this.fis = fis;
        this.pos = pos;
        findPPS();
        findSPS();
    }

    public String getProfileLevel() {
        return toHexString(sps, 1, 3);
    }

    public String getB64PPS() {
        return Base64.encodeToString(pps, 0, 5, Base64.NO_WRAP);
    }

    public String getB64SPS() {
        return Base64.encodeToString(sps, 0, 9, Base64.NO_WRAP);
    }

    private byte[] findPPS() {
        if (!findBoxAvcc()) return null;
        try {
            fis.skipBytes(20);
            fis.read(pps, 0, 5);
        } catch (IOException e) {
            return null;
        }
        return pps;
    }

    private byte[] findSPS() {
        if (!findBoxAvcc()) return null;
        try {
            fis.skipBytes(8);
            fis.read(sps, 0, 11);
        } catch (IOException e) {
            return null;
        }
        return sps;
    }

    private boolean findBoxAvcc() {
        try {
            fis.seek(pos + 8);
            while (true) {
                while (fis.read() != 'a') ;
                fis.read(buffer, 0, 3);
                if (buffer[0] == 'v' && buffer[1] == 'c' && buffer[2] == 'C') break;
            }
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    private static String toHexString(byte[] buffer, int start, int len) {
        String c;
        StringBuilder s = new StringBuilder();
        for (int i = start; i < start + len; i++) {
            c = Integer.toHexString(buffer[i] & 0xFF);
            s.append(c.length() < 2 ? "0" + c : c);
        }
        return s.toString();
    }
}
