package org.jpc.storage;

import java.io.IOException;
import java.net.URI;

public class Raw implements SeekableDataIO {

    private DataIO dio;

    private long address = 0;

    public Raw(URI uri) throws NullPointerException, IOException {
        this(IOFactory.open(uri));
    }

    public Raw(DataIO dio) throws IOException {
        this.dio = dio;
    }

    public static Raw create(URI uri, long length) throws IOException {
        Raw raw = new Raw(IOFactory.create(uri));
        raw.setLength(length);
        return raw;
    }

    public long getPosition() throws IOException {
        return address;
    }

    public void seek(long address) throws IOException {
        if (address < 0) {
            throw new IOException("The value of the position cannot be negative.");
        }
        this.address = address;
    }

    public int read(byte[] data, int offset, int length) throws IOException {
        return dio.read(address, data, offset, length);
    }

    public int readFully(byte[] data, int offset, int length) throws IOException {
        return dio.readFully(address, data, offset, length);
    }

    public int write(byte[] data, int offset, int length) throws IOException {
        return dio.write(address, data, offset, length);
    }

    public void writeFully(byte[] data, int offset, int length) throws IOException {
        dio.writeFully(address, data, offset, length);
    }

    public void commit() throws IOException {
        close();
    }

    public void close() throws IOException {
        dio.close();
        address = 0;
    }

    public long getLength() throws IOException {
        return dio.getLength();
    }

    public void setLength(long length) throws IOException {
        dio.setLength(length);
    }
}
