package net.sf.sail.common.persistance.memory;

import java.util.Date;
import java.util.Iterator;
import java.util.Stack;
import net.sf.sail.core.entity.ISock;
import net.sf.sail.core.entity.ISockEntry;
import net.sf.sail.core.entity.Rim;

public class MemorySock<T> extends Stack<T> implements ISock<T> {

    /**
	 * @author turadg
	 * 
	 */
    public final class MemorySockEntry implements ISockEntry<T> {

        public Date getDate() {
            throw new UnsupportedOperationException("date not yet implemented on MemorySockEntry");
        }

        public T getValue() {
            return peek();
        }
    }

    /**
	 * 
	 */
    private static final long serialVersionUID = -752831757729119985L;

    static transient int sockCount = 0;

    transient int sockSerial = ++sockCount;

    private Rim<T> rim;

    /**
	 * @param rim
	 */
    public MemorySock(Rim<T> rim) {
        this.rim = rim;
    }

    public synchronized boolean add(T o, boolean replace) {
        return add(o);
    }

    @Override
    public synchronized boolean add(T o) {
        final Class<?> clazz = o.getClass();
        final Class<T> shape = rim.getShape();
        if (!shape.isAssignableFrom(clazz)) throw new ClassCastException("sock " + this + " is shaped " + shape);
        return super.add(o);
    }

    @Override
    public synchronized String toString() {
        return "ISock@" + sockSerial + super.toString();
    }

    /**
	 * FIXME this isn't really implemented
	 */
    public Iterator<ISockEntry<T>> entryIterator() {
        return new Iterator<ISockEntry<T>>() {

            public boolean hasNext() {
                return false;
            }

            public ISockEntry<T> next() {
                return null;
            }

            public void remove() {
                throw new UnsupportedOperationException("no remove on this iterator");
            }
        };
    }

    public ISockEntry<T> entryPeek() {
        return new MemorySockEntry();
    }
}
