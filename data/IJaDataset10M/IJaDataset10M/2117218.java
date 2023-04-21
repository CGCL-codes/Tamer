package com.itstherules.io;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import com.itstherules.metadata.AMFObject;
import com.itstherules.metadata.AMFTime;

public class BufferHelper {

    public BufferHelper() {
        mbb = null;
        pos = 0;
        debug = false;
        str = null;
        buf = null;
    }

    public void reset() {
        mbb = null;
        pos = 0;
        str = null;
        buf = null;
    }

    public ByteBuffer byte2buffer(byte bytes[]) {
        ByteBuffer bbuf = ByteBuffer.allocate(bytes.length);
        bbuf.put(bytes);
        bbuf.rewind();
        return bbuf;
    }

    public int bit2uint(char bits[]) {
        int uint = 0;
        for (int i = 0; i < bits.length; i++) if (bits[i] == '1') uint = (int) ((double) uint + Math.pow(2D, bits.length - i - 1));
        return uint;
    }

    public int readUint(byte mpb[], int start, int len) {
        int uint = 0;
        for (int i = 0; i < len; i++) uint += (mpb[i + start] & 255) << (len - i - 1) * 8;
        return uint;
    }

    public int readInt(byte mpb[], int start, int len) {
        int uint = 0;
        for (int i = 0; i < len; i++) uint += mpb[i + start];
        return uint;
    }

    public String readBinaryString(byte mpb[], int start, int len) {
        buf = new byte[len];
        System.arraycopy(mpb, start, buf, 0, len);
        return (new BigInteger(buf)).toString(2);
    }

    public String readString(byte mpb[], int start, int len) {
        buf = new byte[len];
        str = null;
        try {
            System.arraycopy(mpb, start, buf, 0, len);
            str = new String(buf);
            buf = null;
        } catch (Exception e) {
            System.out.println("Error - could not read string from given bytes");
            if (debug) e.printStackTrace();
            str = "";
        }
        return str;
    }

    public double readDouble(byte mpb[], int start, int len) {
        ByteBuffer bbuf = ByteBuffer.allocate(len);
        buf = new byte[len];
        System.arraycopy(mpb, start, buf, 0, len);
        bbuf.put(buf);
        bbuf.rewind();
        buf = null;
        return bbuf.getDouble();
    }

    public void reverseByteArray(byte b[]) {
        int left = 0;
        for (int right = b.length - 1; left < right; right--) {
            byte temp = b[left];
            b[left] = b[right];
            b[right] = temp;
            left++;
        }
    }

    public Object getAMFData() {
        int amfSwtch = readUint(mbb, pos, 1);
        pos++;
        return getAMFData(amfSwtch);
    }

    public Object getAMFData(int amfSwtch) {
        Object amfData = null;
        switch(amfSwtch) {
            case 0:
                amfData = getAMFDouble();
                break;
            case 1:
                amfData = getAMFBoolean();
                break;
            case 2:
                amfData = getAMFString();
                break;
            case 3:
                amfData = getAMFObject();
                break;
            case 8:
                amfData = getAMFMixedArray();
                break;
            case 10:
                amfData = getAMFArray();
                break;
            case 11:
                amfData = getAMFTime();
                break;
        }
        return amfData;
    }

    public Double getAMFDouble() {
        double dbl = readDouble(mbb, pos, 8);
        pos += 8;
        return new Double(dbl);
    }

    public Boolean getAMFBoolean() {
        int val = readUint(mbb, pos, 1);
        pos++;
        return new Boolean(val == 1);
    }

    public String getAMFString() {
        int bytes2read = readUint(mbb, pos, 2);
        pos += 2;
        String str = readString(mbb, pos, bytes2read);
        pos += bytes2read;
        return str;
    }

    public AMFObject getAMFObject() {
        AMFObject amfObj = new AMFObject();
        String key = "";
        int type = 0;
        do {
            if (pos >= mbb.length) break;
            key = getAMFString();
            type = readUint(mbb, pos, 1);
            pos++;
            amfObj.put(key, getAMFData(type));
        } while (key.length() >= 1 || type != 9);
        return amfObj;
    }

    public HashMap<String, Object> getAMFMixedArray() {
        pos += 4;
        HashMap<String, Object> amfMap = new HashMap<String, Object>();
        String key = "";
        int type = 0;
        do {
            if (pos >= mbb.length) break;
            key = getAMFString();
            type = readUint(mbb, pos, 1);
            pos++;
            amfMap.put(key, getAMFData(type));
        } while (key.length() >= 1 || type != 9);
        return amfMap;
    }

    public ArrayList<Object> getAMFArray() {
        int size = readUint(mbb, pos, 4);
        pos += 4;
        ArrayList<Object> afmArray = new ArrayList<Object>();
        for (int i = 0; i < size; i++) afmArray.add(getAMFData());
        return afmArray;
    }

    public AMFTime getAMFTime() {
        long time = (long) getAMFDouble().doubleValue();
        byte buf[] = new byte[2];
        System.arraycopy(mbb, pos, buf, 0, 2);
        pos += 2;
        reverseByteArray(buf);
        int gmtOff = 0;
        for (int i = 0; i < 2; i++) gmtOff += (buf[i] & 255) << (1 - i) * 8;
        buf = null;
        int gmt = gmtOff * 60 * 1000;
        return new AMFTime(time, gmt);
    }

    public void clearData() {
        mbb = null;
    }

    public byte[] getBuffer() {
        return mbb;
    }

    public void setBuffer(byte mbb[]) {
        this.mbb = mbb;
    }

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    private byte mbb[];

    private int pos;

    private boolean debug;

    private String str;

    private byte buf[];
}
