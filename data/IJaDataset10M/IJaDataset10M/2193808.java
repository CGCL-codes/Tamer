package org.retro.gis;

import java.util.Vector;

/**
 * Similar to Queue, modified for Bot message processing.
 * 
 * @see org.retro.gis.Queue
 *
 */
public class NewQueue {

    private Vector _queue = new Vector();

    public NewQueue() {
    }

    public void add(Object o) {
        synchronized (_queue) {
            _queue.addElement(o);
            _queue.notify();
        }
    }

    public void addFront(Object o) {
        synchronized (_queue) {
            _queue.insertElementAt(o, 0);
            _queue.notify();
        }
    }

    /**
     * Remove the next object from the queue, (very important), this method
     * performs a 'wait' till another message comes in, in the case of the BotProcessThread,
     * no processing will continue until that messages arrives.
     *
     * @see Queue
     */
    public Object next() {
        Object o = null;
        synchronized (_queue) {
            if (_queue.size() == 0) {
                try {
                    _queue.wait();
                } catch (InterruptedException e) {
                    return null;
                }
            }
            try {
                o = _queue.firstElement();
                _queue.removeElementAt(0);
            } catch (ArrayIndexOutOfBoundsException e) {
                throw new InternalError("Race hazard in Queue object.");
            }
        }
        return o;
    }

    public boolean hasNext() {
        return (this.size() != 0);
    }

    public void clear() {
        synchronized (_queue) {
            _queue.removeAllElements();
        }
    }

    public int size() {
        return _queue.size();
    }
}
