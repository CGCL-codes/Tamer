package com.cidero.util;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 *  Wrapper class for the standard RandomAccessFile to allow it to be
 *  used within the custom RandomAccessFile implementation framework.
 *  (Need an object that implements the AbstractRandomAccessFile
 *  interface)
 */
public class StdRandomAccessFileWrapper implements AbstractRandomAccessFile {

    java.io.RandomAccessFile raf;

    /**
   *  Constructor. Invoke the constructor of the wrapped class, trap
   *  the SMB-specific exceptions and throw the 'standard' RandomAccessFile
   *  equivalents (as close as we can)
   */
    public StdRandomAccessFileWrapper(com.cidero.util.File file, String mode) throws FileNotFoundException {
        System.out.println("Using wrapper for RandomAccessFile");
        raf = new java.io.RandomAccessFile((java.io.File) file.getUnderlyingFileObj(), mode);
    }

    public StdRandomAccessFileWrapper(String filename, String mode) throws FileNotFoundException {
        System.out.println("Using wrapper for RandomAccessFile");
        raf = new java.io.RandomAccessFile(filename, mode);
    }

    public int read() throws IOException {
        return raf.read();
    }

    public int read(byte b[]) throws IOException {
        return raf.read(b);
    }

    public int read(byte b[], int off, int len) throws IOException {
        return raf.read(b, off, len);
    }

    public long getFilePointer() throws IOException {
        return raf.getFilePointer();
    }

    public void seek(long pos) throws IOException {
        raf.seek(pos);
    }

    public long length() throws IOException {
        return raf.length();
    }

    public void setLength(long newLength) throws IOException {
        raf.setLength(newLength);
    }

    public void close() throws IOException {
        raf.close();
    }

    public boolean readBoolean() throws IOException {
        return raf.readBoolean();
    }

    public byte readByte() throws IOException {
        return raf.readByte();
    }

    public int readUnsignedByte() throws IOException {
        return raf.readUnsignedByte();
    }

    public short readShort() throws IOException {
        return raf.readShort();
    }

    public int readUnsignedShort() throws IOException {
        return raf.readUnsignedShort();
    }

    public char readChar() throws IOException {
        return raf.readChar();
    }

    public int readInt() throws IOException {
        return raf.readInt();
    }

    public long readLong() throws IOException {
        return raf.readLong();
    }

    public float readFloat() throws IOException {
        return raf.readFloat();
    }

    public double readDouble() throws IOException {
        return raf.readDouble();
    }

    public void readFully(byte b[]) throws IOException {
        raf.readFully(b);
    }

    public void readFully(byte b[], int off, int len) throws IOException {
        raf.readFully(b, off, len);
    }

    public String readLine() throws IOException {
        return raf.readLine();
    }

    public String readUTF() throws IOException {
        return raf.readUTF();
    }

    public int skipBytes(int n) throws IOException {
        return raf.skipBytes(n);
    }

    public void write(int b) throws IOException {
        raf.write(b);
    }

    public void write(byte b[]) throws IOException {
        raf.write(b);
    }

    public void write(byte b[], int off, int len) throws IOException {
        write(b, off, len);
    }

    public void writeBoolean(boolean v) throws IOException {
        raf.writeBoolean(v);
    }

    public void writeByte(int v) throws IOException {
        raf.writeByte(v);
    }

    public void writeShort(int v) throws IOException {
        raf.writeShort(v);
    }

    public void writeChar(int v) throws IOException {
        raf.writeChar(v);
    }

    public void writeInt(int v) throws IOException {
        raf.writeInt(v);
    }

    public void writeLong(long v) throws IOException {
        raf.writeLong(v);
    }

    public void writeFloat(float v) throws IOException {
        raf.writeFloat(v);
    }

    public void writeDouble(double v) throws IOException {
        raf.writeDouble(v);
    }

    public void writeBytes(String s) throws IOException {
        raf.writeBytes(s);
    }

    public void writeChars(String s) throws IOException {
        raf.writeChars(s);
    }

    public void writeUTF(String str) throws IOException {
        raf.writeUTF(str);
    }
}
