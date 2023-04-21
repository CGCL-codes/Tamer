package com.yahoo.jute;

import java.io.DataInput;
import java.io.IOException;
import java.io.DataInputStream;
import java.io.InputStream;

/**
 *
 * @author Milind Bhandarkar
 */
public class BinaryInputArchive implements InputArchive {

    private DataInput in;

    public static BinaryInputArchive getArchive(InputStream strm) {
        return new BinaryInputArchive(new DataInputStream(strm));
    }

    private static class BinaryIndex implements Index {

        private int nelems;

        BinaryIndex(int nelems) {
            this.nelems = nelems;
        }

        public boolean done() {
            return (nelems <= 0);
        }

        public void incr() {
            nelems--;
        }
    }

    /** Creates a new instance of BinaryInputArchive */
    public BinaryInputArchive(DataInput in) {
        this.in = in;
    }

    public byte readByte(String tag) throws IOException {
        return in.readByte();
    }

    public boolean readBool(String tag) throws IOException {
        return in.readBoolean();
    }

    public int readInt(String tag) throws IOException {
        return in.readInt();
    }

    public long readLong(String tag) throws IOException {
        return in.readLong();
    }

    public float readFloat(String tag) throws IOException {
        return in.readFloat();
    }

    public double readDouble(String tag) throws IOException {
        return in.readDouble();
    }

    public String readString(String tag) throws IOException {
        int len = in.readInt();
        if (len == -1) return null;
        byte b[] = new byte[len];
        in.readFully(b);
        return new String(b, "UTF8");
    }

    public static final int maxBuffer = determineMaxBuffer();

    private static int determineMaxBuffer() {
        String maxBufferString = System.getProperty("jute.maxbuffer");
        try {
            return Integer.parseInt(maxBufferString);
        } catch (Exception e) {
            return 0xfffff;
        }
    }

    public byte[] readBuffer(String tag) throws IOException {
        int len = readInt(tag);
        if (len == -1) return null;
        if (len < 0 || len > maxBuffer) {
            throw new RuntimeException("Unreasonable length = " + len);
        }
        byte[] arr = new byte[len];
        in.readFully(arr);
        return arr;
    }

    public void readRecord(Record r, String tag) throws IOException {
        r.deserialize(this, tag);
    }

    public void startRecord(String tag) throws IOException {
    }

    public void endRecord(String tag) throws IOException {
    }

    public Index startVector(String tag) throws IOException {
        int len = readInt(tag);
        if (len == -1) {
            return null;
        }
        return new BinaryIndex(len);
    }

    public void endVector(String tag) throws IOException {
    }

    public Index startMap(String tag) throws IOException {
        return new BinaryIndex(readInt(tag));
    }

    public void endMap(String tag) throws IOException {
    }
}
