package org.furthurnet.furi;

public class CQueueElement implements CQueueEntry {

    private Object mObj = null;

    public CQueueElement() {
    }

    public CQueueElement(Object Obj) {
        setObj(Obj);
    }

    public void setObj(Object Obj) {
        mObj = Obj;
    }

    public Object getObj() {
        return mObj;
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
