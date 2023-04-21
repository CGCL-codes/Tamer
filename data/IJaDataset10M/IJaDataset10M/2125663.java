package org.jpc.storage;

import java.net.URI;
import java.io.IOException;
import java.io.RandomAccessFile;

public class FileIO implements DataIO {

    private RandomAccessFile raf;

    public FileIO(URI uri) throws IOException {
        raf = new RandomAccessFile(uri.getPath(), "rw");
    }

    public int read(long address, byte[] content, int offset, int length) throws IOException {
        raf.seek(address);
        return raf.read(content, offset, length);
    }

    public int readFully(long address, byte[] content, int offset, int length) throws IOException {
        raf.seek(address);
        int rd = 0;
        while (rd < length) {
            int tmpRd = raf.read(content, (int) offset + rd, (int) length - rd);
            if (tmpRd < 0) {
                throw new IOException("Unexpected EOF of input data");
            }
            rd += tmpRd;
        }
        return rd;
    }

    public int write(long address, byte[] content, int offset, int length) throws IOException {
        raf.seek(address);
        long wtBak = raf.getFilePointer();
        raf.write(content, offset, length);
        return (int) (raf.getFilePointer() - wtBak);
    }

    public void writeFully(long address, byte[] content, int offset, int length) throws IOException {
        raf.seek(address);
        write(address, content, offset, length);
    }

    public void close() throws IOException {
        raf.close();
    }

    public void setLength(long length) throws IOException {
        raf.setLength(length);
    }

    public long getLength() throws IOException {
        return raf.length();
    }
}
