package com.coldcore.coloradoftp.filter.impl;

import com.coldcore.coloradoftp.filter.DataFilter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

public class GenericDataFilter implements DataFilter {

    protected WritableByteChannel wbc;

    protected ReadableByteChannel rbc;

    protected String name;

    protected boolean upload;

    public void setChannel(WritableByteChannel wbc) {
        this.wbc = wbc;
        upload = true;
    }

    public void setChannel(ReadableByteChannel rbc) {
        this.rbc = rbc;
        upload = false;
    }

    public boolean mayModifyDataLength() {
        return false;
    }

    public boolean isOpen() {
        return upload ? wbc.isOpen() : rbc.isOpen();
    }

    public void close() throws IOException {
        if (upload) wbc.close(); else rbc.close();
    }

    public int write(ByteBuffer src) throws IOException {
        return wbc.write(src);
    }

    public int read(ByteBuffer dst) throws IOException {
        return rbc.read(dst);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
