package anyware.common.io;

import java.util.Iterator;

/**
 * TODO: Doc me
 * 
 * @author keke <keke@codehaus.org>
 * @version
 * @revision $Revision: 137 $
 */
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

    public void clear() {
        if (initialCapacity != -1) {
            buf = new UnboundedFifoByteBuffer(initialCapacity);
        } else {
            buf = new UnboundedFifoByteBuffer();
        }
    }

    public int count() {
        return buf.size();
    }

    public byte dequeue() {
        return buf.remove();
    }

    public void enqueue(byte b) {
        buf.add(b);
    }

    public Iterator iterator() {
        return buf.iterator();
    }
}
