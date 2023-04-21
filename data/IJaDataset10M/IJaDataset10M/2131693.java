package verinec.netsim.util.tables;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.NoSuchElementException;
import verinec.netsim.NetSimException;

/** A sliding window buffer
 * @author Dominik Jungo
 * @version $Revision: 47 $
 */
public class SlidingWindowBuffer extends Hashtable {

    private int capacity;

    private int lastAcked;

    /** Creates a new sliding window buffer.
     * @param initialCapacity the window size (capacity of the buffer)
     */
    public SlidingWindowBuffer(int initialCapacity) {
        super(initialCapacity);
        capacity = initialCapacity;
        lastAcked = 0;
    }

    /** writes a byte of data
     * @param key a key
     * @param data the data
     * @return address of the last part of data in a contiguous part 
     * @throws NetSimException if there if no more spave left in buffer or the address is out of window range
     */
    public int put(int key, byte data) throws NetSimException {
        if (windowsize() <= 0) {
            throw new NetSimException("no more space left in buffer");
        }
        int smallestKey;
        try {
            smallestKey = getSmallestKey().intValue();
        } catch (NoSuchElementException e) {
            smallestKey = -1;
        }
        if (Math.abs((key - smallestKey)) > capacity && smallestKey != -1) {
            throw new NetSimException("can't write element in buffer. out of window range.");
        }
        put(new Integer(key), new Byte(data));
        updateLastAcked();
        return lastAcked;
    }

    /** gets the address of the last part of the buffer that can be acknowledged
     * @return the address of the last part of the buffer that can be acknowledged
     */
    public int getAck() {
        return lastAcked;
    }

    private void updateLastAcked() {
        Enumeration e = keys();
        while (e.hasMoreElements()) {
            int i = ((Integer) e.nextElement()).intValue();
            if (lastAcked + 1 == i) {
                lastAcked++;
            } else {
                break;
            }
        }
    }

    /** gets data (a byte) from the contiguous part of the window and removes it from the buffer
     * @param key the address of the byte
     * @return the requested byte
     * @throws NetSimException if requested element not in buffer or separated by missing part
     */
    public byte get(int key) throws NetSimException {
        if (key > lastAcked) {
            throw new NetSimException("requested element not in buffer or separated by missing part");
        }
        Object o = get(new Integer(key));
        remove(new Integer(key));
        return ((Byte) o).byteValue();
    }

    private Integer getSmallestKey() {
        Enumeration e = keys();
        Integer returnvalue = (Integer) e.nextElement();
        while (e.hasMoreElements()) {
            Integer i = (Integer) e.nextElement();
            if (i.compareTo(returnvalue) < 0) {
                returnvalue = i;
            }
        }
        return returnvalue;
    }

    /** gets the first part of data (a byte) from the contiguous part of the window and removes it from the buffer
     * @return the first byte from the contiguous part of the window
     * @throws NetSimException if requested element not in buffer or separated by missing part
     */
    public byte get() throws NetSimException {
        try {
            Object key = getSmallestKey();
            return get(((Integer) key).intValue());
        } catch (NoSuchElementException e) {
            throw new NetSimException("requested element not in buffer or separated by missing part", e);
        }
    }

    /** gets the remaining window size
     * @return the window size
     */
    public int windowsize() {
        return capacity - size();
    }
}
