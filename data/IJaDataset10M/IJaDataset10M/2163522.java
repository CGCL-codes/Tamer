package com.myJava.file;

public interface OutputStreamListener {

    public void bytesWritten(byte[] data, int offset, int length);

    public void byteWritten(int data);

    public void closed();
}
