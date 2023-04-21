package titancommon.bluetooth;

/**
 * Implements the java.util.Queue as it is not available here.
 * 
 * @author Clemens Lombriser <lombriser@ife.ee.ethz.ch>
 */
public class PacketQueue {

    /**
    * Used to generate a linked list of elements
    */
    private class QueueElement {

        public Object element;

        public QueueElement next;

        public QueueElement(Object o) {
            element = o;
            next = null;
        }
    }

    private QueueElement m_firstElement;

    private QueueElement m_lastElement;

    /**
    *  Retrieves, but does not remove, the head of this queue.
    * @return the head of this queue, null if empty 
    */
    public Object element() {
        return peek();
    }

    /**
    * Inserts the specified element into this queue
    * @param o the element to insert. 
    * @return true if it was possible to add the element to this queue, else false
    */
    public boolean offer(Object o) {
        if (m_lastElement == null) {
            m_firstElement = m_lastElement = new QueueElement(o);
        } else {
            m_lastElement.next = new QueueElement(o);
            m_lastElement = m_lastElement.next;
        }
        return true;
    }

    /**
    * Retrieves, but does not remove, the head of this queue, returning null if this queue is empty. 
    * @return the head of this queue, or null if this queue is empty.
    */
    public Object peek() {
        return (m_firstElement == null) ? null : m_firstElement.element;
    }

    /**
    * Retrieves, but does not remove, n items from the head of the queue. 
    * Always returns an array with n elements, but sets them null if the queue 
    * is too short.
    * @param n
    * @return
    */
    public Object[] peekN(int n) {
        Object[] obs = new Object[n];
        QueueElement curEl = m_firstElement;
        for (int i = 0; i < n; i++) {
            if (curEl != null) {
                obs[i] = curEl.element;
                curEl = curEl.next;
            } else break;
        }
        return obs;
    }

    /**
    * Retrieves and removes the head of this queue, or null if this queue is empty. 
    * @return the head of this queue, or null if this queue is empty.
    */
    public Object poll() {
        if (m_firstElement == null) return null;
        Object o = m_firstElement.element;
        m_firstElement = m_firstElement.next;
        if (m_firstElement == null) m_lastElement = null;
        return o;
    }

    /**
    * Retrieves and removes the head of this queue. This method differs from the poll method in that it throws an exception if this queue is empty. 
    * @return the head of this queue. 
    */
    public Object remove() {
        return poll();
    }

    public BTDataPacket[] getAllPackets() {
        int count = 0;
        QueueElement curElement = m_firstElement;
        while (curElement != null) {
            count++;
            curElement = curElement.next;
        }
        BTDataPacket[] packets = new BTDataPacket[count];
        count = 0;
        curElement = m_firstElement;
        while (curElement != null) {
            packets[count++] = (BTDataPacket) curElement.element;
            curElement = curElement.next;
        }
        return packets;
    }
}
