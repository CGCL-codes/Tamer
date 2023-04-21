package phex.host;

import phex.*;
import phex.cqueue.*;
import phex.interfaces.*;

public class HostMsg implements CQueueEntry {

    private Host mHost = null;

    private IMsg mMsg = null;

    private boolean mUrgent = false;

    public HostMsg() {
    }

    public void setHost(Host host) {
        mHost = host;
    }

    public Host getHost() {
        return mHost;
    }

    public void setMsg(IMsg msg) {
        mMsg = msg;
    }

    public IMsg getMsg() {
        return mMsg;
    }

    public void setUrgent(boolean urgent) {
        mUrgent = urgent;
    }

    public boolean getUrgent() {
        return mUrgent;
    }

    public String toString() {
        return mHost + " " + mMsg;
    }

    private CQueueEntry mNext = null;

    private CQueueEntry mPrev = null;

    private CQueue mQueue = null;

    public CQueueEntry getPrev() {
        return mPrev;
    }

    public void setPrev(CQueueEntry prev) {
        mPrev = prev;
    }

    public CQueueEntry getNext() {
        return mNext;
    }

    public void setNext(CQueueEntry next) {
        mNext = next;
    }

    public void setCQueue(CQueue queue) {
        mQueue = queue;
    }

    public CQueue getCQueue() {
        return mQueue;
    }
}
