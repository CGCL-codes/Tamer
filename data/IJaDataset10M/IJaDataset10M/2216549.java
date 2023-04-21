package GSim;

/**
 * Sorted buffer implemented as a linked list
 * 
 * @author Fredrik Wahlberg
 * @version $Rev$
 * 
 */
public class BufferSorted extends Buffer {

    private Object lockOnLast;

    public BufferSorted() {
        lockOnLast = new Object();
    }

    /**
	 * Add a ComparableData object to the buffer
	 * 
	 * @param ComparableData
	 *            Any ComparableData
	 */
    public void push(ComparableData value) {
        synchronized (lockOnLast) {
            list.insertSorted(value);
        }
    }

    /**
	 * Pop a ComparableData object from the buffer
	 * 
	 * @return ComparableData oldest ComparableData or null
	 */
    public ComparableData pop() {
        synchronized (lockOnLast) {
            ComparableData ret = list.pop();
            return ret;
        }
    }

    /**
	 * Get the oldest BufferData object from the buffer without removing it
	 * 
	 * @return BufferData oldest BufferData or null
	 */
    public ComparableData top() {
        synchronized (lockOnLast) {
            ComparableData ret = list.top();
            return ret;
        }
    }

    public String toString() {
        return list.toString();
    }
}
