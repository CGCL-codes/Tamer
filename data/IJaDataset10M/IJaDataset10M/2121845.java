package com.lepidllama.packageeditor.fileio;

import java.io.ByteArrayInputStream;
import java.util.Arrays;
import com.lepidllama.packageeditor.utility.StringUtils;

public class ByteArrayDataWriter implements DataWriter {

    byte[] content;

    int pos = 0;

    public ByteArrayDataWriter(int size) {
        content = new byte[size];
    }

    public void writeDwordInt(int data) {
        this.content[pos] = (byte) (data & 0xFF);
        this.content[pos + 1] = (byte) ((data >> 8) & 0xFF);
        this.content[pos + 2] = (byte) ((data >> 16) & 0xFF);
        this.content[pos + 3] = (byte) ((data >> 24) & 0xFF);
        pos += 4;
    }

    public void writeDwordLong(long data) {
        this.content[pos + 0] = (byte) (data & 0xFF);
        this.content[pos + 1] = (byte) ((data >> 8) & 0xFF);
        this.content[pos + 2] = (byte) ((data >> 16) & 0xFF);
        this.content[pos + 3] = (byte) ((data >> 24) & 0xFF);
        pos += 4;
    }

    public void writeWordInt(int data) {
        this.content[pos + 0] = (byte) (data & 0xFF);
        this.content[pos + 1] = (byte) ((data >> 8) & 0xFF);
        pos += 2;
    }

    public void writeDwordString(String data) {
        writeFixedString(data, 4);
    }

    public void writeFixedString(String data, int length) {
        byte[] b = Arrays.copyOf(data.getBytes(), length);
        System.arraycopy(b, 0, this.content, pos, b.length);
        pos += b.length;
    }

    public void writeDwordBytes(byte[] data) {
        System.arraycopy(data, 0, this.content, pos, data.length);
        pos += data.length;
    }

    public void writeChunk(byte[] data) {
        System.arraycopy(data, 0, this.content, pos, data.length);
        pos += data.length;
    }

    public void writeLengthString(String data) {
        writeDwordInt(data.length());
        byte[] b = data.getBytes();
        System.arraycopy(b, 0, this.content, pos, b.length);
        pos += b.length;
    }

    public void write7BitString(String data) {
        byte[] b = data.getBytes();
        StringUtils.write7BitStringLength(this, b.length);
        System.arraycopy(b, 0, this.content, pos, b.length);
        pos += b.length;
    }

    public void writeByte(byte data) {
        this.content[pos] = data;
        pos++;
    }

    public void writeUnsignedByte(int data) {
        this.content[pos] = (byte) (data & 0xFF);
        pos++;
    }

    public void writeNullTermString(String data) {
        byte[] b = data.getBytes();
        System.arraycopy(b, 0, this.content, pos, b.length);
        pos += b.length;
        this.content[pos] = 0;
        pos++;
    }

    public long getFilePointer() {
        return pos;
    }

    public void seek(long pos) {
        this.pos = (int) pos;
    }

    public byte[] getContent() {
        return content;
    }

    public ByteArrayInputStream getBais() {
        return new ByteArrayInputStream(getContent());
    }

    public void writeDwordFloat(float data) {
        writeDwordInt(Float.floatToRawIntBits(data));
    }
}
