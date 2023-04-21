package com.tightvnc.vncviewer;

public class MemInStream extends InStream {

    public MemInStream(byte[] data, int offset, int len) {
        b = data;
        ptr = offset;
        end = offset + len;
    }

    public int pos() {
        return ptr;
    }

    protected int overrun(int itemSize, int nItems) throws Exception {
        throw new Exception("MemInStream overrun: end of stream");
    }
}
