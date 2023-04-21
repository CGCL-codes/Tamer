package org.jpropeller.collection.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.Callable;
import org.jpropeller.collection.ListChange;
import org.jpropeller.collection.ObservableList;
import org.jpropeller.map.ExtendedPropMap;
import org.jpropeller.map.PropMap;
import org.jpropeller.properties.UneditableProp;
import org.jpropeller.properties.event.ListPropEvent;
import org.jpropeller.properties.event.PropEvent;
import org.jpropeller.properties.event.PropInternalListener;
import org.jpropeller.properties.event.impl.ListPropEventDefault;
import org.jpropeller.system.Props;

/**
 * A ListBeanShell wraps an underlying List implementation, delegating actual storage
 * of elements to this core list, and adding tracking of the elements so that events
 * are generated as required for ListBean compliance.
 * 
 * @author shingoki
 *
 * @param <E>
 * 		The type of element in the list
 */
public class ObservableListDefault<E> implements ObservableList<E>, PropInternalListener {

    ContentsTracking<E> tracking;

    ExtendedPropMap propMap = Props.getPropSystem().createExtendedPropMap(this);

    @Override
    public PropMap props() {
        return propMap;
    }

    private boolean extraChecks = true;

    private ModificationsProp modificationsProp = new ModificationsProp(this);

    private List<E> core;

    public UneditableProp<Long> modifications() {
        return modificationsProp;
    }

    ;

    /**
	 * Create a new {@link ObservableListDefault} based on a new {@link ArrayList}
	 */
    public ObservableListDefault() {
        this(new ArrayList<E>());
    }

    /**
	 * Create a new ListBeanShell based on a new ArrayList
	 * @param capacity
	 * 		The initial capacity of the ArrayList
	 */
    public ObservableListDefault(int capacity) {
        this(new ArrayList<E>(capacity));
    }

    /**
	 * Create a new ListBeanShell based on a given core
	 * list. The core List provides the actual storage and
	 * implementation of List methods - this shell just
	 * intercepts method calls to keep track of the List
	 * contents to implement ListBean, for example 
	 * by firing the proper events on element changes, etc.
	 * 
	 * NOTE: you must NOT modify the core list after using it
	 * to create a ListBeanShell, otherwise you will stop the
	 * ListBeanShell functioning as a compliant ListBean. It is
	 * safest not to retain a reference to the core list at all,
	 * e.g.
	 * <code>ListBean<String> listBean = new ListBeanShell(new LinkedList<String>());</code>
	 * 
	 * @param core
	 * 		The list implementation this ListBean will delegate to.
	 * 		If this is a null, an empty {@link ArrayList} will be
	 * used instead	
	 */
    public ObservableListDefault(List<E> core) {
        super();
        if (core == null) core = new ArrayList<E>();
        propMap.add(modificationsProp);
        this.core = core;
        tracking = new ContentsTracking<E>(this);
        retrackAll();
    }

    @Override
    public <T> void propInternalChanged(PropEvent<T> event) {
        if (extraChecks) {
            if (!tracking.getReferenceCounts().keySet().contains(event.getProp().getBean())) {
                throw new ObservableCollectionRuntimeException("Received a PropEvent from a bean not in the reference set. Prop: '" + event.getProp() + "'");
            }
        }
        ListChange listChange = ListChangeDefault.newEntireListAlteration(this);
        ListPropEvent<Long> listEvent = new ListPropEventDefault<Long>(modificationsProp, event, listChange);
        modificationsProp.increment(listEvent);
    }

    /**
	 * Start tracking all elements in the list again. 
	 * clearAllTracking() is called, then all elements
	 * are passed to startTrackingElement(e);
	 */
    public void retrackAll() {
        tracking.clearAllTracking();
        for (E e : core) {
            tracking.startTrackingElement(e);
        }
    }

    /**
	 * Perform a change to the core list in a way that preserves proper tracking,
	 * and then fires an event for a complete change to the list
	 * @param action
	 * 		The action to perform to the list - may manipulate the core list in
	 * any way, but should not do anything else.
	 * 		The action must NOT throw a non-runtime exception - if it does, a 
	 * ListBeanRunTimeException will be thrown with the exception as its cause.
	 * List methods do not throw non-runtime exceptions, so the action should not
	 * either 
	 * 		Runtime exceptions from the action will just be thrown from this method,
	 * but only after tracking has been set up, and a prop event fired for a 
	 * complete list change
	 * @param oldSize
	 * 		The size of the list before the change (that is, at the time that 
	 * this method is called)
	 * @return
	 * 		The return value of the action
	 * @throws ObservableCollectionRuntimeException
	 * 		If the action throws a non-runtime exception
	 */
    private boolean trackAroundListChange(Callable<Boolean> action) {
        int oldSize = size();
        tracking.clearAllTracking();
        try {
            try {
                return action.call();
            } catch (Exception e) {
                throw new ObservableCollectionRuntimeException("Unexpected exception from a list action", e);
            }
        } finally {
            retrackAll();
            ListChange listChange = ListChangeDefault.newCompleteChange(this, oldSize);
            modificationsProp.incrementFromListChange(listChange);
        }
    }

    public boolean add(E e) {
        boolean success = core.add(e);
        if (!success) return false;
        tracking.startTrackingElement(e);
        ListChange listChange = ListChangeDefault.newAddChange(this);
        modificationsProp.incrementFromListChange(listChange);
        return true;
    }

    public void add(int index, E e) {
        core.add(index, e);
        tracking.startTrackingElement(e);
        ListChange listChange = ListChangeDefault.newAddChange(this, index);
        modificationsProp.incrementFromListChange(listChange);
    }

    public boolean addAll(final Collection<? extends E> c) {
        Callable<Boolean> action = new Callable<Boolean>() {

            @Override
            public Boolean call() throws Exception {
                return core.addAll(c);
            }
        };
        return trackAroundListChange(action);
    }

    public boolean addAll(final int index, final Collection<? extends E> c) {
        Callable<Boolean> action = new Callable<Boolean>() {

            @Override
            public Boolean call() throws Exception {
                return core.addAll(index, c);
            }
        };
        return trackAroundListChange(action);
    }

    public void clear() {
        Callable<Boolean> action = new Callable<Boolean>() {

            @Override
            public Boolean call() throws Exception {
                core.clear();
                return true;
            }
        };
        trackAroundListChange(action);
    }

    @Override
    public void replace(final Iterable<E> newContents) {
        Callable<Boolean> action = new Callable<Boolean>() {

            @Override
            public Boolean call() throws Exception {
                core.clear();
                for (E e : newContents) {
                    core.add(e);
                }
                return true;
            }
        };
        trackAroundListChange(action);
    }

    public E remove(int index) {
        E removed = core.remove(index);
        tracking.stopTrackingElement(removed);
        ListChange listChange = ListChangeDefault.newRemoveChange(this, index);
        modificationsProp.incrementFromListChange(listChange);
        return removed;
    }

    public boolean remove(Object o) {
        int oldSize = core.size();
        boolean success = core.remove(o);
        if (!success) return false;
        tracking.stopTrackingElement(o);
        ListChange listChange = ListChangeDefault.newCompleteChange(this, oldSize);
        modificationsProp.incrementFromListChange(listChange);
        return true;
    }

    public boolean removeAll(final Collection<?> c) {
        Callable<Boolean> action = new Callable<Boolean>() {

            @Override
            public Boolean call() throws Exception {
                return core.removeAll(c);
            }
        };
        return trackAroundListChange(action);
    }

    public boolean retainAll(final Collection<?> c) {
        Callable<Boolean> action = new Callable<Boolean>() {

            @Override
            public Boolean call() throws Exception {
                return core.retainAll(c);
            }
        };
        return trackAroundListChange(action);
    }

    public E set(int index, E element) {
        E oldValue = core.set(index, element);
        tracking.stopTrackingElement(oldValue);
        tracking.startTrackingElement(element);
        ListChange listChange = ListChangeDefault.newSingleElementAlteration(this, index);
        modificationsProp.incrementFromListChange(listChange);
        return oldValue;
    }

    public List<E> subList(int fromIndex, int toIndex) {
        return Collections.unmodifiableList(core.subList(fromIndex, toIndex));
    }

    public Iterator<E> iterator() {
        return new IteratorShell<E>(core.iterator());
    }

    public ListIterator<E> listIterator() {
        return new ListIteratorShell<E>(core.listIterator());
    }

    public ListIterator<E> listIterator(int index) {
        return new ListIteratorShell<E>(core.listIterator(index));
    }

    /**
	 *	Wraps an iterator, and makes main class fire property change (complete list
	 *	change) when remove is used.
	 */
    private class IteratorShell<T> implements Iterator<T> {

        Iterator<T> it;

        /**
		 * Make a wrapper 
		 * @param it The iterator to wrap
		 */
        public IteratorShell(Iterator<T> it) {
            this.it = it;
        }

        public void remove() {
            Callable<Boolean> action = new Callable<Boolean>() {

                @Override
                public Boolean call() throws Exception {
                    it.remove();
                    return true;
                }
            };
            trackAroundListChange(action);
        }

        public boolean equals(Object obj) {
            return it.equals(obj);
        }

        public int hashCode() {
            return it.hashCode();
        }

        public boolean hasNext() {
            return it.hasNext();
        }

        public T next() {
            return it.next();
        }

        public String toString() {
            return it.toString();
        }
    }

    /**
	 *	Wraps an iterator, and makes main class fire property change (complete list
	 *	change) when remove is used.
	 */
    private class ListIteratorShell<T> implements ListIterator<T> {

        ListIterator<T> it;

        /**
		 * Make a wrapper 
		 * @param it The iterator to wrap
		 */
        public ListIteratorShell(ListIterator<T> it) {
            super();
            this.it = it;
        }

        public void add(final T e) {
            Callable<Boolean> action = new Callable<Boolean>() {

                @Override
                public Boolean call() throws Exception {
                    it.add(e);
                    return true;
                }
            };
            trackAroundListChange(action);
        }

        public void remove() {
            Callable<Boolean> action = new Callable<Boolean>() {

                @Override
                public Boolean call() throws Exception {
                    it.remove();
                    return true;
                }
            };
            trackAroundListChange(action);
        }

        public void set(final T e) {
            Callable<Boolean> action = new Callable<Boolean>() {

                @Override
                public Boolean call() throws Exception {
                    it.set(e);
                    return true;
                }
            };
            trackAroundListChange(action);
        }

        public boolean hasNext() {
            return it.hasNext();
        }

        public boolean hasPrevious() {
            return it.hasPrevious();
        }

        public T next() {
            return it.next();
        }

        public int nextIndex() {
            return it.nextIndex();
        }

        public T previous() {
            return it.previous();
        }

        public int previousIndex() {
            return it.previousIndex();
        }
    }

    public boolean contains(Object o) {
        return core.contains(o);
    }

    public boolean containsAll(Collection<?> c) {
        return core.containsAll(c);
    }

    public boolean equals(Object o) {
        return core.equals(o);
    }

    public E get(int index) {
        return core.get(index);
    }

    public int hashCode() {
        return core.hashCode();
    }

    public int indexOf(Object o) {
        return core.indexOf(o);
    }

    public boolean isEmpty() {
        return core.isEmpty();
    }

    public int lastIndexOf(Object o) {
        return core.lastIndexOf(o);
    }

    public int size() {
        return core.size();
    }

    public Object[] toArray() {
        return core.toArray();
    }

    public <T> T[] toArray(T[] a) {
        return core.toArray(a);
    }

    @Override
    public String toString() {
        return "Observable List of " + size() + " items";
    }
}
