package jreceiver.util;

import java.util.EventListener;

/**
* used to notify listeners to registered events
 *
 * @author Reed Esau
 * @version $Revision: 1.2 $ $Date: 2002/12/29 00:44:07 $
*/
public class EventListenerList {

    private static final Object[] NULL_ARRAY = new Object[0];

    protected transient Object[] listenerList = NULL_ARRAY;

    public Object[] getListenerList() {
        return listenerList;
    }

    public int getListenerCount() {
        return listenerList.length / 2;
    }

    public int getListenerCount(Class t) {
        int count = 0;
        Object[] lList = listenerList;
        for (int i = 0; i < lList.length; i += 2) {
            if (t == (Class) lList[i]) count++;
        }
        return count;
    }

    public void add(Class t, EventListener l) {
        if (l == null) {
            return;
        }
        if (!t.isInstance(l)) {
            throw new IllegalArgumentException("Listener " + l + " is not of type " + t);
        }
        if (listenerList == NULL_ARRAY) {
            listenerList = new Object[] { t, l };
        } else {
            int i = listenerList.length;
            Object[] tmp = new Object[i + 2];
            System.arraycopy(listenerList, 0, tmp, 0, i);
            tmp[i] = t;
            tmp[i + 1] = l;
            listenerList = tmp;
        }
    }

    public void remove(Class t, EventListener l) {
        if (l == null) {
            return;
        }
        if (!t.isInstance(l)) {
            throw new IllegalArgumentException("Listener " + l + " is not of type " + t);
        }
        int index = -1;
        for (int i = listenerList.length - 2; i >= 0; i -= 2) {
            if ((listenerList[i] == t) && (listenerList[i + 1].equals(l) == true)) {
                index = i;
                break;
            }
        }
        if (index != -1) {
            Object[] tmp = new Object[listenerList.length - 2];
            System.arraycopy(listenerList, 0, tmp, 0, index);
            if (index < tmp.length) System.arraycopy(listenerList, index + 2, tmp, index, tmp.length - index);
            listenerList = (tmp.length == 0) ? NULL_ARRAY : tmp;
        }
    }

    public void clear() {
        listenerList = NULL_ARRAY;
    }

    /**
     * Return a string representation of the EventListenerList.
     */
    public String toString() {
        StringBuffer buf = new StringBuffer();
        Object[] lList = listenerList;
        buf.append("EventListenerList: ").append(lList.length / 2).append(" listeners: ");
        for (int i = 0; i <= lList.length - 2; i += 2) {
            buf.append(" type ").append(((Class) lList[i]).getName()).append(" listener ").append(lList[i + 1]);
        }
        return buf.toString();
    }
}
