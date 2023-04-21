package org.mime4j;

import java.util.Iterator;

public class ByteQueue {

    private UnboundedFifoByteBuffer buf;

    private int initialCapacity = -1;

    public ByteQueue() {
        buf = new UnboundedFifoByteBuffer();
    }

    public ByteQueue(int initialCapacity) {
        buf = new UnboundedFifoByteBuffer(initialCapacity);
        this.initialCapacity = initialCapacity;
    }

    public void enqueue(byte b) {
        buf.add(b);
    }

    public byte dequeue() {
        return buf.remove();
    }

    public int count() {
        return buf.size();
    }

    public void clear() {
        if (initialCapacity != -1) buf = new UnboundedFifoByteBuffer(initialCapacity); else buf = new UnboundedFifoByteBuffer();
    }

    public Iterator iterator() {
        return buf.iterator();
    }
}
