package com.lepidllama.packageeditor.fileio;

/**
 * The DataWriter Interface mirrors the DataReader interface in reverse, so that
 * code for parsing a file or resource can be easily translated to code which 
 * writes that resource out. Like the DataReader, this class allows equivalent
 * access to both files and byte arrays for writing.
 * 
 * @see ByteArrayDataWriter FileDataWriter CountDataWriter
 * @author Erica Smith
 *
 */
public interface DataWriter {

    public void seek(long pos);

    public void writeByte(byte data);

    public void writeDwordInt(int data);

    public void writeDwordLong(long data);

    public void writeWordInt(int data);

    public void writeDwordFloat(float data);

    public void writeDwordString(String data);

    public void writeFixedString(String data, int length);

    public void write7BitString(String data);

    public void writeNullTermString(String data);

    public void writeDwordBytes(byte[] data);

    public void writeChunk(byte[] data);

    public void writeLengthString(String data);

    public void writeUnsignedByte(int data);

    public long getFilePointer();
}
