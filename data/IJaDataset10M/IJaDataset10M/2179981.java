package com.googlecode.jue.compression.lzw;

import java.io.*;

/**
 * 
 * 
 * @author Moshe Fresko
 * @course Algorithmic Programming 1
 * @exercise 3
 */
public class LZW implements Compression {

    boolean stopped = false;

    Dict dict;

    int numOfBits;

    final ByteArray emptyBA = new ByteArray();

    ByteArray w = emptyBA;

    public LZW() {
        numOfBits = 12;
        dict = new LimitedDict(1 << numOfBits);
        for (int i = 0; i < 256; ++i) dict.add(new ByteArray((byte) i));
    }

    int encodeOneChar(int n) {
        byte c = (byte) n;
        ByteArray nw = w.conc(c);
        int code = dict.numFromStr(nw);
        if (code != -1) {
            w = nw;
            return -1;
        } else {
            dict.add(nw);
            nw = w;
            w = new ByteArray(c);
            return dict.numFromStr(nw);
        }
    }

    int encodeLast() {
        ByteArray nw = w;
        w = emptyBA;
        return dict.numFromStr(nw);
    }

    void writeCode(OutputStream os, int code) throws IOException {
        for (int i = 0; i < numOfBits; ++i) {
            os.write(code & 1);
            code /= 2;
        }
    }

    int readCode(InputStream is) throws IOException {
        int num = 0;
        for (int i = 0; i < numOfBits; ++i) {
            int next = is.read();
            if (next < 0) return -1;
            num += next << i;
        }
        return num;
    }

    private class UnClosedOutputStream extends FilterOutputStream {

        public UnClosedOutputStream(OutputStream os) {
            super(os);
        }

        public void write(byte b[], int off, int len) throws IOException {
            out.write(b, off, len);
        }

        public void close() throws IOException {
        }
    }

    public void compress(InputStream is, OutputStream os) throws IOException {
        os = new BitOutputStream(new UnClosedOutputStream(os));
        int next;
        int code;
        while ((next = is.read()) >= 0) {
            if (stopped) break;
            code = encodeOneChar(next);
            if (code >= 0) writeCode(os, code);
        }
        code = encodeLast();
        if (code >= 0) writeCode(os, code);
        os.close();
    }

    ByteArray decodeOne(int code) {
        ByteArray str = dict.strFromNum(code);
        if (str == null) {
            str = w.conc(w.getAt(0));
            dict.add(str);
        } else if (!w.isEmpty()) dict.add(w.conc(str.getAt(0)));
        w = str;
        return w;
    }

    public void decompress(InputStream is, OutputStream os) throws IOException {
        is = new BitInputStream(is);
        ByteArray str;
        int code;
        while ((code = readCode(is)) >= 0) {
            if (stopped) break;
            str = decodeOne(code);
            os.write(str.getBytes());
        }
    }

    public void stop() {
        stopped = true;
    }

    public static void main(String args[]) {
        LZW lzw = new LZW();
        try {
            lzw.compress(new FileInputStream("D:/des_test/src/LZW/TEST.PNG"), new FileOutputStream("D:/des_test/src/LZW/test.lzw"));
        } catch (Exception e) {
        }
    }
}
