package org.ccnx.ccn.profiles.versioning;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.TreeSet;

/**
 * Add some missing features from the JDK 5 TreeSet.  If
 * running on JDK 6, use them as they will be way more efficient.
 * This class is to provide JDK compatibility for the versioning
 * package and only implements the needed functionality.
 * 
 * The user should call the *Compatible methods (floorCompatible, etc.)
 * instead of the Java 6 methods (e.g. floor).
 * 
 * When used in a JDK 5 environment, the implementations of the
 * provided algorithms is going to be O(N), not O(log N).  There is no
 * fast-fail detection for concurrent modifications.  The descendingIterator
 * works fine for iteration and calling remove(), but if you mix in
 * other calls, like to add() while iterating, you will not see those
 * values.
 */
public class TreeSet6<E> extends TreeSet<E> {

    public TreeSet6() {
        initialize();
    }

    public TreeSet6(Comparator<? super E> c) {
        super(c);
        initialize();
    }

    /**
	 * Returns the greatest element in this set less than or equal to the 
	 * given element, or null if there is no such element.
	 * 
	 * Use this method, not floor().
	 */
    @SuppressWarnings("unchecked")
    public E floorCompatible(E key) {
        if (null != floor) try {
            return (E) floor.invoke(this, (Object) key);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            throw e;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            throw new IllegalArgumentException(e.getMessage());
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            throw new IllegalArgumentException(e.getMessage());
        }
        return internalFloor(key);
    }

    /**
	 * Returns the least element in this set greater than or equal to the 
	 * given element, or null if there is no such element.
	 * 
	 * Use this method, not ceiling().
	 */
    @SuppressWarnings("unchecked")
    public E ceilingCompatible(E key) {
        if (null != ceiling) try {
            return (E) ceiling.invoke(this, (Object) key);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            throw e;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            throw new IllegalArgumentException(e.getMessage());
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            throw new IllegalArgumentException(e.getMessage());
        }
        return internalCeiling(key);
    }

    /**
	 * Returns the greatest element in this set strictly less than the 
	 * given element, or null if there is no such element.
	 * 
	 * Use this method, not lower().
	 */
    @SuppressWarnings("unchecked")
    public E lowerCompatible(E key) {
        if (null != lower) try {
            return (E) lower.invoke(this, (Object) key);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            throw e;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            throw new IllegalArgumentException(e.getMessage());
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            throw new IllegalArgumentException(e.getMessage());
        }
        return internalLower(key);
    }

    /**
	 * Returns the least element in this set strictly greater than the 
	 * given element, or null if there is no such element.
	 * 
	 * Use this method, not higher().
	 */
    @SuppressWarnings("unchecked")
    public E higherCompatible(E key) {
        if (null != higher) try {
            return (E) higher.invoke(this, (Object) key);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            throw e;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            throw new IllegalArgumentException(e.getMessage());
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            throw new IllegalArgumentException(e.getMessage());
        }
        return internalHigher(key);
    }

    /**
	 * Returns an iterator over the elements in this set in descending order.
	 * @return
	 */
    @SuppressWarnings("unchecked")
    public Iterator<E> descendingIteratorCompatible() {
        if (null != higher) try {
            return (Iterator<E>) descendingIterator.invoke(this);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            throw e;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            throw new IllegalArgumentException(e.getMessage());
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            throw new IllegalArgumentException(e.getMessage());
        }
        return internalDescendingIterator();
    }

    private static final long serialVersionUID = 7840825335033077895L;

    private transient Method floor = null;

    private transient Method ceiling = null;

    private transient Method lower = null;

    private transient Method higher = null;

    private transient Method descendingIterator = null;

    /**
	 * Our own wrapper for getMethod that returns null if the method is not found.
	 * @param c class to search for method
	 * @param name method name
	 * @param parameterTypes
	 * @return the method or null if not found
	 */
    protected Method getMethod(Class<?> c, String name, Class<?>... parameterTypes) {
        Method m = null;
        try {
            m = c.getMethod(name, parameterTypes);
        } catch (NoSuchMethodException nsme) {
        }
        return m;
    }

    private Iterator<E> internalDescendingIterator() {
        return new DescendingIterator<E>();
    }

    private void initialize() {
        try {
            Class<?>[] parameterTypes = new Class[] { Object.class };
            Class<?> c = this.getClass();
            Class<?> cc = c.getSuperclass();
            try {
                floor = cc.getMethod("floor", parameterTypes);
            } catch (NoSuchMethodException nsme) {
            }
            try {
                ceiling = cc.getMethod("ceiling", parameterTypes);
            } catch (NoSuchMethodException nsme) {
            }
            try {
                lower = cc.getMethod("lower", parameterTypes);
            } catch (NoSuchMethodException nsme) {
            }
            try {
                higher = cc.getMethod("higher", parameterTypes);
            } catch (NoSuchMethodException nsme) {
            }
            try {
                descendingIterator = cc.getMethod("descendingIterator");
            } catch (NoSuchMethodException nsme) {
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
	 * Returns the greatest element in this set less than or equal to the 
	 * given element, or null if there is no such element.
	 */
    @SuppressWarnings("unchecked")
    protected E internalFloor(E key) {
        Comparator<? super E> comparator = this.comparator();
        Comparable<? super E> comparable = null;
        if (key instanceof Comparable<?>) {
            comparable = (Comparable<? super E>) key;
        }
        E rtn = null;
        Iterator<E> iter = iterator();
        while (iter.hasNext()) {
            E test = iter.next();
            if (mycompare(comparator, comparable, key, test) == 0) return test;
            if (mycompare(comparator, comparable, key, test) < 0) return rtn;
            rtn = test;
        }
        return rtn;
    }

    /**
	 * Returns the least element in this set greater than or equal to the 
	 * given element, or null if there is no such element.
	 */
    @SuppressWarnings("unchecked")
    protected E internalCeiling(E key) {
        Comparator<? super E> comparator = this.comparator();
        Comparable<? super E> comparable = null;
        if (key instanceof Comparable<?>) {
            comparable = (Comparable<? super E>) key;
        }
        E rtn = null;
        Iterator<E> iter = iterator();
        while (iter.hasNext()) {
            E test = iter.next();
            if (mycompare(comparator, comparable, key, test) == 0) return test;
            if (mycompare(comparator, comparable, key, test) < 0) return test;
            rtn = test;
        }
        return rtn;
    }

    /**
	 * Returns the least element in this set strictly greater than the 
	 * given element, or null if there is no such element.
	 */
    @SuppressWarnings("unchecked")
    protected E internalHigher(E key) {
        Comparator<? super E> comparator = this.comparator();
        Comparable<? super E> comparable = null;
        if (key instanceof Comparable<?>) {
            comparable = (Comparable<? super E>) key;
        }
        E rtn = null;
        Iterator<E> iter = iterator();
        while (iter.hasNext()) {
            E test = iter.next();
            if (mycompare(comparator, comparable, key, test) < 0) return test;
            rtn = test;
        }
        return rtn;
    }

    /**
	 * Returns the greatest element in this set strictly less than the 
	 * given element, or null if there is no such element.
	 */
    @SuppressWarnings("unchecked")
    protected E internalLower(E key) {
        Comparator<? super E> comparator = this.comparator();
        Comparable<? super E> comparable = null;
        if (key instanceof Comparable<?>) {
            comparable = (Comparable<? super E>) key;
        }
        E rtn = null;
        Iterator<E> iter = iterator();
        while (iter.hasNext()) {
            E test = iter.next();
            if (mycompare(comparator, comparable, key, test) == 0) return rtn;
            if (mycompare(comparator, comparable, key, test) < 0) return rtn;
            rtn = test;
        }
        return rtn;
    }

    /**
	 * if comp not null, use comp, else use comparable if not null, else
	 * throw a ClassCast exception
	 * @param comparator the Comparator to use
	 * @param comparable the casting to (Comparable) from a
	 * @param a
	 * @param b
	 * @return -1 if a<b, 0 a==0, +1 a>b
	 */
    protected int mycompare(Comparator<? super E> comparator, Comparable<? super E> comparable, E a, E b) throws ClassCastException {
        if (null != comparator) return (comparator.compare(a, b));
        if (null != comparable) return (comparable.compareTo(b));
        throw new ClassCastException("not comparable");
    }

    protected class DescendingIterator<T> implements Iterator<T> {

        private final LinkedList<T> _list;

        private ListIterator<T> _listIterator;

        private T _lastReturnedValue = null;

        @SuppressWarnings("unchecked")
        public DescendingIterator() {
            _list = new LinkedList<T>((Collection<? extends T>) TreeSet6.this);
            _listIterator = _list.listIterator();
            while (_listIterator.hasNext()) _listIterator.next();
        }

        public boolean hasNext() {
            return _listIterator.hasPrevious();
        }

        public T next() {
            _lastReturnedValue = _listIterator.previous();
            return _lastReturnedValue;
        }

        public void remove() {
            if (null == _lastReturnedValue) throw new IllegalStateException("Remove has already been called or no value has been returned");
            _listIterator.remove();
            TreeSet6.this.remove(_lastReturnedValue);
            _lastReturnedValue = null;
        }
    }
}
