package se.uu.it.cats.brick.filter;

/**
 * FIFO buffer implemented as a linked list
 * 
 * @author Fredrik Wahlberg
 * @version $Rev$
 * 
 */
public class BufferFIFO extends Buffer {

    private Object lockOnLast;

    private int nonodes = 0;

    public BufferFIFO() {
        super();
        lockOnLast = new Object();
    }

    /**
	 * Push a ComparableData object to the buffer.
	 * 
	 * @param ComparableData
	 *            Any ComparableData
	 */
    public void push(ComparableData value) {
        synchronized (lockOnLast) {
            if (value != null) {
                list.insertLast(value);
                nonodes++;
            }
        }
    }

    /**
	 * Pop a ComparableData object from the buffer
	 * 
	 * @return ComparableData oldest ComparableData or null
	 */
    public synchronized ComparableData pop() {
        ComparableData ret = list.pop();
        if (ret != null) {
            nonodes--;
        }
        return ret;
    }

    /**
	 * Get the oldest BufferData object from the buffer without removing it
	 * 
	 * @return BufferData oldest BufferData or null
	 */
    public synchronized ComparableData top() {
        ComparableData ret = list.top();
        return ret;
    }

    public String toString() {
        return list.toString();
    }
}
