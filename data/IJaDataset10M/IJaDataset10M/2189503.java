package org.tritonus.lowlevel.alsa;

import org.tritonus.share.TDebug;

public class AlsaSeqPortSubscribe {

    static {
        Alsa.loadNativeLibrary();
        if (TDebug.TraceAlsaSeqNative) {
            setTrace(true);
        }
    }

    long m_lNativeHandle;

    public AlsaSeqPortSubscribe() {
        if (TDebug.TraceAlsaSeqNative) {
            TDebug.out("AlsaSeq.PortSubscribe.<init>(): begin");
        }
        int nReturn = malloc();
        if (nReturn < 0) {
            throw new RuntimeException("malloc of port_info failed");
        }
        if (TDebug.TraceAlsaSeqNative) {
            TDebug.out("AlsaSeq.PortSubscribe.<init>(): end");
        }
    }

    public void finalize() {
    }

    private native int malloc();

    public native void free();

    public native int getSenderClient();

    public native int getSenderPort();

    public native int getDestClient();

    public native int getDestPort();

    public native int getQueue();

    public native boolean getExclusive();

    public native boolean getTimeUpdate();

    public native boolean getTimeReal();

    public native void setSender(int nClient, int nPort);

    public native void setDest(int nClient, int nPort);

    public native void setQueue(int nQueue);

    public native void setExclusive(boolean bExclusive);

    public native void setTimeUpdate(boolean bUpdate);

    public native void setTimeReal(boolean bReal);

    private static native void setTrace(boolean bTrace);
}
