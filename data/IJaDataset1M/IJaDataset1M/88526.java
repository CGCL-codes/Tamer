package rcm.util;

import java.util.Enumeration;

public class History {

    public static rcm.util.Debug debug = rcm.util.Debug.QUIET;

    protected Object[] history;

    protected int start;

    protected int end;

    protected int curr;

    /**
     * Make a History.
     * @param   max     Maximum length of history
     */
    public History(int max) {
        history = new Object[max + 1];
        start = end = curr = 0;
    }

    /**
     * Make a duplicate of another History.
     * @param   h History to copy
     */
    public History(History h) {
        this.history = new Object[h.history.length];
        System.arraycopy(h.history, 0, this.history, 0, h.history.length);
        this.start = h.start;
        this.end = h.end;
        this.curr = h.curr;
    }

    /**
     * Clear the history.
     */
    public void clear() {
        for (int i = 0; i < history.length; ++i) history[i] = null;
        int s = start;
        int e = end;
        start = end = curr = 0;
        if (s != e) fireRemoved(s, (e > 0) ? e - 1 : history.length - 1);
    }

    /**
     * Double the capacity of the history.
     */
    public void expand() {
        Object[] newHistory = new Object[(history.length * 2) - 1];
        int i = 0;
        int newCurr = 0;
        for (int j = start; j != end; j = (j + 1) % history.length, ++i) {
            newHistory[i] = history[j];
            if (j == curr) newCurr = i;
        }
        history = newHistory;
        start = 0;
        end = i;
        curr = newCurr;
    }

    /**
     * Add an object to the history after the current point (deleting all
     * objects forward of this point).  If history overflows, the oldest
     * object is thrown away.
     * @param obj   Object to add to history
     */
    public void put(Object obj) {
        if (!isEmpty()) {
            int newEnd = (curr + 1) % history.length;
            if (newEnd != end) {
                int e = end;
                end = newEnd;
                fireRemoved(newEnd, (e > 0) ? e - 1 : history.length - 1);
            }
        }
        add(obj);
    }

    /**
     * Add an object to the end of the history, moving the current point to it.
     * If history overflows, the oldest object is thrown away.
     * @param obj   Object to add to history
     */
    public void add(Object obj) {
        if (isFull()) {
            start = (start + 1) % history.length;
            fireRemoved(start, start);
        }
        history[end] = obj;
        curr = end;
        end = (end + 1) % history.length;
        fireAdded(curr, curr);
        debug.println("after put: start=" + start + ", end=" + end + ", curr=" + curr);
    }

    /**
     * Get the current object of the history.
     * @return current object of history, or null if history is empty.
     */
    public Object get() {
        return !isEmpty() ? history[curr] : null;
    }

    /**
     * Get the object that would be returned by back(), without actually
     * changing the current object.
     * @return object before current object, or null if at beginning of 
     * history or history is empty.
     */
    public Object peekBack() {
        if (curr == start) return null; else {
            int bk = (curr > 0) ? curr - 1 : history.length - 1;
            return history[bk];
        }
    }

    /**
     * Get the object that would be returned by forward(), without actually
     * changing the current object.
     * @return object after current object, or null if at end of 
     * history or history is empty.
     */
    public Object peekForward() {
        if (start == end) return null;
        int fw = (curr + 1) % history.length;
        if (fw == end) return null; else return history[fw];
    }

    /**
     * Replace the current object of the history. The rest of the history
     * is unaffected, and the current pointer stays where it is.
     * <P> If the history is empty,
     * then this call is equivalent to put(obj).
     * @param obj object to substitute
     */
    public void replace(Object obj) {
        if (isEmpty()) put(obj); else {
            history[curr] = obj;
            fireChanged(curr, curr);
        }
    }

    /**
     * Move back one object in the history, if possible.
     * @return previous object in the history, or null if at start.
     */
    public Object back() {
        if (curr == start) return null; else {
            curr = (curr > 0) ? curr - 1 : history.length - 1;
            fireChanged(curr, curr);
            return history[curr];
        }
    }

    /**
     * Move forward one object in the history, if possible.
     * @return next object in the history, or null if at end of history.
     */
    public Object forward() {
        if (start == end) return null;
        int newCurr = (curr + 1) % history.length;
        if (newCurr == end) return null; else {
            curr = newCurr;
            fireChanged(curr, curr);
            return history[curr];
        }
    }

    /**
     * Move to first (oldest) object in the history, if possible.
     * @return first object in the history, or null if history empty.
     */
    public Object toStart() {
        if (curr != start) {
            curr = start;
            fireChanged(curr, curr);
        }
        return history[curr];
    }

    /**
     * Move to last (newest) object in the history, if possible.
     * @return last object in the history, or null if history empty.
     */
    public Object toEnd() {
        if (start == end) return null;
        int newCurr = (end > 0) ? end - 1 : history.length - 1;
        if (curr != newCurr) {
            curr = newCurr;
            fireChanged(curr, curr);
        }
        return history[curr];
    }

    /**
     * Test whether back() will succeed.
     * @return true if and only if there are objects before the current object
     */
    public boolean canBack() {
        return (curr != start);
    }

    /**
     * Test whether forward() will succeed.
     * @return true if and only if there are objects after the current object
     */
    public boolean canForward() {
        return ((curr + 1) % history.length != end);
    }

    /**
     * Test whether history is empty.
     * @return true if and only if history contains no objects
     */
    public boolean isEmpty() {
        return start == end;
    }

    /**
     * Test whether history is full.
     * @return true if and only if history contains max objects
     */
    public boolean isFull() {
        return start == (end + 1) % history.length;
    }

    /**
     * Test whether history already contains an object.
     * @param obj   Object to search for
     * @return true if and only if history contains an object that equals() obj
     */
    public boolean contains(Object obj) {
        for (int i = start; i != end; i = (i + 1) % history.length) if (history[i].equals(obj)) return true;
        return false;
    }

    /**
     * Get the objects in the history.
     * @returns enumeration yielding the history objects in order from
     * oldest to newest.
     */
    public Enumeration elements() {
        return new HistoryEnumeration(start, end);
    }

    /**
     * Get the objects AFTER the current object.
     * @returns enumeration yielding the history objects after current,
     * in order from oldest to newest.
     */
    public Enumeration forwardElements() {
        return (curr == end) ? new HistoryEnumeration(curr, end) : new HistoryEnumeration((curr + 1) % history.length, end);
    }

    /**
     * Get the objects BEFORE the current object.
     * @returns enumeration yielding the history objects before current,
     * in order from oldest to newest.
     */
    public Enumeration backElements() {
        return new HistoryEnumeration(start, curr);
    }

    class HistoryEnumeration implements Enumeration {

        int p;

        int e;

        public HistoryEnumeration(int start, int end) {
            p = start;
            e = end;
        }

        public boolean hasMoreElements() {
            return p != e;
        }

        public Object nextElement() {
            Object obj = history[p];
            p = (p + 1) % history.length;
            return obj;
        }
    }

    protected void fireRemoved(int i, int j) {
    }

    protected void fireAdded(int i, int j) {
    }

    protected void fireChanged(int i, int j) {
    }
}
