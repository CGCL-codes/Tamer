package com.ibm.tuningfork.infra.util;

import com.ibm.tuningfork.infra.Logging;

/**
 * A logically infinite indexable queue of non-null objects. The backing store is finite (but lazily created) so dropped
 * elements are reported as null.
 * 
 */
public class CircularBuffer {

    public static final int DEFAULT_MAXSIZE = 1000;

    private final int maxSize;

    private ChunkedObjectArray data;

    int start, end;

    long lost;

    public CircularBuffer() {
        this(DEFAULT_MAXSIZE);
    }

    public CircularBuffer(int maxSize) {
        data = new ChunkedObjectArray(maxSize);
        this.maxSize = maxSize;
        clear();
    }

    public void clear() {
        start = end = 0;
        lost = 0;
    }

    private boolean full() {
        long afterEnd = end + 1;
        if (afterEnd == maxSize) {
            afterEnd = 0;
        }
        return afterEnd == start;
    }

    public long getOldest() {
        return lost;
    }

    public long getYoungest() {
        return logicalLength() - 1;
    }

    public int physicalLength() {
        int len = end - start;
        if (len < 0) {
            len += maxSize;
        }
        return len;
    }

    public long logicalLength() {
        return lost + physicalLength();
    }

    public synchronized Object enqueue(Object val) {
        if (val == null) {
            Logging.msgln("Cannot add a null to a CircularBuffer");
            throw new NullPointerException();
        }
        Object ejected = null;
        if (full()) {
            if (full()) {
                ejected = data.get(start);
                if (++start == maxSize) {
                    start = 0;
                }
                lost++;
            }
        }
        data.set(end, val);
        if (++end == maxSize) {
            end = 0;
        }
        return ejected;
    }

    public synchronized Object dequeue() {
        if (start == end) {
            return null;
        }
        lost++;
        Object result = data.get(start);
        if (++start == maxSize) {
            start = 0;
        }
        return result;
    }

    private int toPhysical(long index) {
        if (index < lost) {
            return -1;
        }
        if (index >= logicalLength()) {
            return -1;
        }
        index -= lost;
        index += start;
        if (index >= maxSize) {
            index -= maxSize;
        }
        return (int) index;
    }

    public synchronized Object peek(long index) {
        int pindex = toPhysical(index);
        if (pindex < 0) {
            return null;
        }
        return data.get(pindex);
    }

    public synchronized void replace(long index, Object obj) {
        int pindex = toPhysical(index);
        if (pindex < 0) {
            return;
        }
        data.set(pindex, obj);
    }

    public boolean isAvailable(long index) {
        return toPhysical(index) >= 0;
    }

    /**
     * for sharing -- Ungar 9/07
     */
    public int getMaxSize() {
        return maxSize;
    }
}
