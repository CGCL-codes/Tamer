package com.sts.webmeet.content.client.audio;

import java.io.IOException;

public class Win32Mic implements Microphone {

    static {
        System.loadLibrary("Win32Mic");
    }

    public synchronized boolean open(int iSampleRate, int iChannels, int iBitsPerSample, int iBufferSizeInBytes, int iBufferCount) {
        iHandle = nopen(iSampleRate, iChannels, iBitsPerSample, iBufferSizeInBytes, iBufferCount);
        if (0 == iHandle) {
            System.out.println("unable to open microphone");
        }
        return (0 != iHandle);
    }

    public synchronized void close() {
        System.out.println("in win32mic.close");
        if (0 != iHandle) {
            System.out.println("\tcalling native close from " + Thread.currentThread() + "...");
            nclose(iHandle);
            System.out.println("\t...returned from native close.");
            iHandle = 0;
        }
    }

    public void getBuffer(byte[] baFrame, int iOffset, int iLen) throws IOException {
        if (0 != iHandle) {
            int iResult = ngetBuffer(iHandle, baFrame, iOffset, iLen);
            if (iResult != 0) {
                throw new IOException(getClass().getName() + ".getBuffer: error reading from mic");
            }
        } else {
            throw new IOException(getClass().getName() + ".getBuffer: no mic handle: closing?");
        }
    }

    private native int nopen(int iSampleRate, int iChannels, int iBitsPerSample, int iBufferSizeInBytes, int iBufferCount);

    private native void nclose(int iHandle);

    private native int ngetBuffer(int iHandle, byte[] baFrame, int iOffset, int iLen);

    private int iHandle = 0;
}
