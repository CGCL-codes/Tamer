package org.quickfix;

import java.util.Collection;
import java.util.Calendar;
import java.util.Date;
import java.io.IOException;

public class MySQLStore implements MessageStore {

    private int cppPointer;

    public MySQLStore() {
        create();
    }

    private MySQLStore(int cppPointer) {
        this.cppPointer = cppPointer;
    }

    protected void finalize() {
        destroy();
    }

    public boolean set(int sequence, String message) throws IOException {
        return set0(sequence, message);
    }

    public boolean get(int sequence, String message) throws IOException {
        return get0(sequence, message);
    }

    public void get(int startSequence, int endSequence, Collection messages) throws IOException {
        get0(startSequence, endSequence, messages);
    }

    public int getNextSenderMsgSeqNum() throws IOException {
        return getNextSenderMsgSeqNum0();
    }

    public int getNextTargetMsgSeqNum() throws IOException {
        return getNextTargetMsgSeqNum0();
    }

    public void setNextSenderMsgSeqNum(int next) throws IOException {
        setNextSenderMsgSeqNum0(next);
    }

    public void setNextTargetMsgSeqNum(int next) throws IOException {
        setNextTargetMsgSeqNum0(next);
    }

    public void incrNextSenderMsgSeqNum() throws IOException {
        incrNextSenderMsgSeqNum0();
    }

    public void incrNextTargetMsgSeqNum() throws IOException {
        incrNextTargetMsgSeqNum0();
    }

    public Date getCreationTime() throws IOException {
        return getCreationTime0();
    }

    public void reset() throws IOException {
        reset0();
    }

    private native void create();

    private native void destroy();

    private native boolean set0(int sequence, String message) throws IOException;

    private native boolean get0(int sequence, String message) throws IOException;

    private native void get0(int startSequence, int endSequence, Collection messages) throws IOException;

    private native int getNextSenderMsgSeqNum0() throws IOException;

    public native int getNextTargetMsgSeqNum0() throws IOException;

    private native void setNextSenderMsgSeqNum0(int next) throws IOException;

    private native void setNextTargetMsgSeqNum0(int next) throws IOException;

    private native void incrNextSenderMsgSeqNum0() throws IOException;

    private native void incrNextTargetMsgSeqNum0() throws IOException;

    private native Date getCreationTime0() throws IOException;

    private native void reset0() throws IOException;
}
