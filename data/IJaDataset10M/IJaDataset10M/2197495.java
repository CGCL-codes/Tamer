package com.atolsystems.atolutilities;

import java.io.*;
import java.nio.channels.FileChannel;

public class RandomAccessFileOutputStream extends OutputStream {

    protected RandomAccessFile randomFile;

    public RandomAccessFileOutputStream(String fileName) throws IOException {
        this(new File(fileName));
    }

    public RandomAccessFileOutputStream(File file) throws IOException {
        super();
        file = file.getAbsoluteFile();
        randomFile = new RandomAccessFile(file, "rw");
    }

    public RandomAccessFileOutputStream(RandomAccessFile file) throws IOException {
        super();
        randomFile = file;
    }

    @Override
    public void flush() throws IOException {
        randomFile.getFD().sync();
    }

    @Override
    public void close() throws IOException {
        randomFile.close();
    }

    public final void writeUTF(String str) throws IOException {
        randomFile.writeUTF(str);
    }

    public final void writeShort(int v) throws IOException {
        randomFile.writeShort(v);
    }

    public final void writeLong(long v) throws IOException {
        randomFile.writeLong(v);
    }

    public final void writeInt(int v) throws IOException {
        randomFile.writeInt(v);
    }

    public final void writeFloat(float v) throws IOException {
        randomFile.writeFloat(v);
    }

    public final void writeDouble(double v) throws IOException {
        randomFile.writeDouble(v);
    }

    public final void writeChars(String s) throws IOException {
        randomFile.writeChars(s);
    }

    public final void writeChar(int v) throws IOException {
        randomFile.writeChar(v);
    }

    public final void writeBytes(String s) throws IOException {
        randomFile.writeBytes(s);
    }

    public final void writeByte(int v) throws IOException {
        randomFile.writeByte(v);
    }

    public final void writeBoolean(boolean v) throws IOException {
        randomFile.writeBoolean(v);
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        randomFile.write(b, off, len);
    }

    @Override
    public void write(byte[] b) throws IOException {
        randomFile.write(b);
    }

    public void write(int b) throws IOException {
        randomFile.write(b);
    }

    public int skipBytes(int n) throws IOException {
        return randomFile.skipBytes(n);
    }

    public void setLength(long newLength) throws IOException {
        randomFile.setLength(newLength);
    }

    public void seek(long pos) throws IOException {
        randomFile.seek(pos);
    }

    public final int readUnsignedShort() throws IOException {
        return randomFile.readUnsignedShort();
    }

    public final int readUnsignedByte() throws IOException {
        return randomFile.readUnsignedByte();
    }

    public final String readUTF() throws IOException {
        return randomFile.readUTF();
    }

    public final short readShort() throws IOException {
        return randomFile.readShort();
    }

    public final long readLong() throws IOException {
        return randomFile.readLong();
    }

    public final String readLine() throws IOException {
        return randomFile.readLine();
    }

    public final int readInt() throws IOException {
        return randomFile.readInt();
    }

    public final void readFully(byte[] b, int off, int len) throws IOException {
        randomFile.readFully(b, off, len);
    }

    public final void readFully(byte[] b) throws IOException {
        randomFile.readFully(b);
    }

    public final float readFloat() throws IOException {
        return randomFile.readFloat();
    }

    public final double readDouble() throws IOException {
        return randomFile.readDouble();
    }

    public final char readChar() throws IOException {
        return randomFile.readChar();
    }

    public final byte readByte() throws IOException {
        return randomFile.readByte();
    }

    public final boolean readBoolean() throws IOException {
        return randomFile.readBoolean();
    }

    public int read(byte[] b) throws IOException {
        return randomFile.read(b);
    }

    public int read(byte[] b, int off, int len) throws IOException {
        return randomFile.read(b, off, len);
    }

    public int read() throws IOException {
        return randomFile.read();
    }

    public long length() throws IOException {
        return randomFile.length();
    }

    public long getFilePointer() throws IOException {
        return randomFile.getFilePointer();
    }

    public final FileDescriptor getFD() throws IOException {
        return randomFile.getFD();
    }

    public final FileChannel getChannel() {
        return randomFile.getChannel();
    }
}
