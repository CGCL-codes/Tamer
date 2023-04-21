package com.microfly.event;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.EventListener;

/**
 *  �¼�������
 *  COPY FROM GNU java.awt.EventListenerList
 *  a new publishing system
 *  Copyright (c) 2007
 *
 * @author jialin
 * @version 1.0
 */
public class EventListenerList implements Serializable {

    /**
     * An empty array that is shared by all instances of this class that
     * have no listeners.
     */
    private static final Object[] NO_LISTENERS = new Object[0];

    /**
     * An array with all currently registered listeners.  The array has
     * twice as many elements as there are listeners.  For an even
     * integer <code>i</code>, <code>listenerList[i]</code> indicates
     * the registered class, and <code>listenerList[i + 1]</code> is the
     * listener.
     */
    protected transient Object[] listenerList = NO_LISTENERS;

    /**
     * EventListenerList constructor
     */
    public EventListenerList() {
    }

    /**
     * Registers a listener of a specific type.
     *
     */
    public <T extends EventListener> void add(Class<T> t, T listener) {
        int oldLength;
        Object[] newList;
        if (listener == null) return;
        if (!t.isInstance(listener)) throw new IllegalArgumentException();
        oldLength = listenerList.length;
        newList = new Object[oldLength + 2];
        if (oldLength > 0) System.arraycopy(listenerList, 0, newList, 0, oldLength);
        newList[oldLength] = t;
        newList[oldLength + 1] = listener;
        listenerList = newList;
    }

    /**
     * Determines the number of listeners.
     */
    public int getListenerCount() {
        return listenerList.length / 2;
    }

    /**
     * Determines the number of listeners of a particular class.
     *
     */
    public int getListenerCount(Class<?> t) {
        int result = 0;
        for (int i = 0; i < listenerList.length; i += 2) if (t == listenerList[i]) ++result;
        return result;
    }

    /**
     * Returns an array containing a sequence of listenerType/listener pairs, one
     * for each listener.
     */
    public Object[] getListenerList() {
        return listenerList;
    }

    /**
     * Retrieves the currently subscribed listeners of a particular
     * type.  For a listener to be returned, it must have been
     * registered with exactly the type <code>c</code>; subclasses are
     * not considered equal.
     */
    public <T extends EventListener> T[] getListeners(Class<T> c) {
        int count, f;
        EventListener[] result;
        count = getListenerCount(c);
        result = (EventListener[]) Array.newInstance(c, count);
        f = 0;
        for (int i = listenerList.length - 2; i >= 0; i -= 2) if (listenerList[i] == c) result[f++] = (EventListener) listenerList[i + 1];
        return (T[]) result;
    }

    /**
     * Removes a listener of a specific type.
     */
    public <T extends EventListener> void remove(Class<T> t, T listener) {
        Object[] oldList, newList;
        int oldLength;
        if (listener == null) return;
        if (!t.isInstance(listener)) throw new IllegalArgumentException();
        oldList = listenerList;
        oldLength = oldList.length;
        for (int i = 0; i < oldLength; i += 2) if (oldList[i] == t && oldList[i + 1] == listener) {
            if (oldLength == 2) newList = NO_LISTENERS; else {
                newList = new Object[oldLength - 2];
                if (i > 0) System.arraycopy(oldList, 0, newList, 0, i);
                if (i < oldLength - 2) System.arraycopy(oldList, i + 2, newList, i, oldLength - 2 - i);
            }
            listenerList = newList;
            return;
        }
    }

    /**
     * Returns a string representation of this object that may be useful
     * for debugging purposes.
     */
    public String toString() {
        StringBuffer buf = new StringBuffer("EventListenerList: ");
        buf.append(listenerList.length / 2);
        buf.append(" listeners: ");
        for (int i = 0; i < listenerList.length; i += 2) {
            buf.append(" type ");
            buf.append(((Class) listenerList[i]).getName());
            buf.append(" listener ");
            buf.append(listenerList[i + 1]);
        }
        return buf.toString();
    }

    /**
     * Serializes an instance to an ObjectOutputStream.
     */
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        for (int i = 0; i < listenerList.length; i += 2) {
            Class cl = (Class) listenerList[i];
            EventListener l = (EventListener) listenerList[i + 1];
            if (l != null && l instanceof Serializable) {
                out.writeObject(cl.getName());
                out.writeObject(l);
            }
        }
        out.writeObject(null);
    }

    /**
     * Deserializes an instance from an ObjectInputStream.
     */
    private <T extends EventListener> void readObject(ObjectInputStream in) throws ClassNotFoundException, IOException {
        listenerList = NO_LISTENERS;
        in.defaultReadObject();
        Object type;
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        while ((type = in.readObject()) != null) {
            EventListener l = (EventListener) in.readObject();
            add(((Class<T>) Class.forName((String) type, true, cl)), (T) l);
        }
    }
}
