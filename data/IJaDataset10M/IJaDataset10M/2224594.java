package de.dhke.projects.cutil.collections.aspect;

import de.dhke.projects.cutil.IDecorator;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 *
 * @param <E> The element type
 * @param <L> The actual list type.
 * @author Peter Wullinger <java@dhke.de>
 */
public class AspectList<E, L extends List<E>> extends AspectCollectionNotifier<E, List<E>> implements List<E>, IDecorator<L> {

    private enum IterationDirection {

        FORWARD, NONE, BACKWARD
    }

    ;

    public class Itr implements Iterator<E>, ListIterator<E> {

        private final ListIterator<E> _baseIterator;

        private IterationDirection _lastDirection = IterationDirection.NONE;

        public Itr() {
            _baseIterator = getDecoratee().listIterator();
        }

        public Itr(final int index) {
            _baseIterator = getDecoratee().listIterator(index);
        }

        public boolean hasNext() {
            return _baseIterator.hasNext();
        }

        public E next() {
            E item = _baseIterator.next();
            _lastDirection = IterationDirection.FORWARD;
            return item;
        }

        public void remove() {
            E item = get(getCurrentIndex());
            ListItemEvent<E, List<E>> ev = new ListItemEvent<E, List<E>>(AspectList.this, item, getCurrentIndex());
            notifyBeforeElementRemoved(ev);
            _baseIterator.remove();
            _lastDirection = IterationDirection.NONE;
            notifyAfterElementRemoved(ev);
        }

        public boolean hasPrevious() {
            return _baseIterator.hasPrevious();
        }

        public E previous() {
            E item = _baseIterator.previous();
            _lastDirection = IterationDirection.FORWARD;
            return item;
        }

        public int nextIndex() {
            return _baseIterator.nextIndex();
        }

        public int previousIndex() {
            return _baseIterator.previousIndex();
        }

        public void set(final E e) {
            E oldItem = get(getCurrentIndex());
            ListItemReplacedEvent<E, List<E>> ev = new ListItemReplacedEvent<E, List<E>>(AspectList.this, getCurrentIndex(), oldItem, e);
            notifyBeforeElementReplaced(ev);
            _baseIterator.set(e);
            notifyAfterElementReplaced(ev);
        }

        public void add(final E e) {
            CollectionItemEvent<E, List<E>> ev = new CollectionItemEvent<E, List<E>>(AspectList.this, e);
            notifyBeforeElementAdded(ev);
            _baseIterator.add(e);
            ev = new ListItemEvent<E, List<E>>(AspectList.this, e, previousIndex());
            notifyAfterElementAdded(ev);
        }

        /**
		 * @return The position of the "current" item with regard
		 * to the last all to {@link #next()} or {@link #previous() }.
		 */
        private int getCurrentIndex() {
            switch(_lastDirection) {
                case BACKWARD:
                    return nextIndex();
                case FORWARD:
                    return previousIndex();
                default:
                    throw new NoSuchElementException("Iterator position unknown");
            }
        }
    }

    private final L _baseList;

    public AspectList(final L baseList) {
        super();
        _baseList = baseList;
    }

    public static <E, L extends List<E>> AspectList<E, L> decorate(final L list) {
        return new AspectList<E, L>(list);
    }

    public boolean addAll(int index, final Collection<? extends E> c) {
        for (E item : c) {
            add(index, item);
            ++index;
        }
        return true;
    }

    public E get(final int index) {
        return getDecoratee().get(index);
    }

    public E set(final int index, final E element) {
        E oldItem = get(index);
        ListItemReplacedEvent<E, List<E>> ev = new ListItemReplacedEvent<E, List<E>>(this, this, index, oldItem, element);
        notifyBeforeElementReplaced(ev);
        oldItem = getDecoratee().set(index, element);
        notifyAfterElementReplaced(ev);
        return oldItem;
    }

    public void add(final int index, final E element) {
        ListItemEvent<E, List<E>> ev = new ListItemEvent<E, List<E>>(this, null, element, index);
        notifyBeforeElementAdded(ev);
        getDecoratee().add(index, element);
        notifyAfterElementAdded(ev);
    }

    public E remove(final int index) {
        E item = get(index);
        ListItemEvent<E, List<E>> ev = new ListItemEvent<E, List<E>>(this, item, index);
        notifyBeforeElementRemoved(ev);
        item = getDecoratee().remove(index);
        notifyAfterElementRemoved(ev);
        return item;
    }

    public int indexOf(final Object o) {
        return getDecoratee().indexOf(o);
    }

    public int lastIndexOf(final Object o) {
        return getDecoratee().indexOf(o);
    }

    public ListIterator<E> listIterator() {
        return new Itr();
    }

    public ListIterator<E> listIterator(final int index) {
        return new Itr(index);
    }

    public List<E> subList(final int fromIndex, final int toIndex) {
        return new AspectList<E, List<E>>(getDecoratee().subList(fromIndex, toIndex));
    }

    public int size() {
        return _baseList.size();
    }

    public boolean isEmpty() {
        return _baseList.isEmpty();
    }

    public boolean contains(final Object o) {
        return _baseList.contains(o);
    }

    public Iterator<E> iterator() {
        return new Itr();
    }

    public Object[] toArray() {
        return _baseList.toArray();
    }

    public <T> T[] toArray(final T[] a) {
        return _baseList.toArray(a);
    }

    public boolean add(final E e) {
        CollectionItemEvent<E, List<E>> ev = new CollectionItemEvent<E, List<E>>(this, e);
        notifyBeforeElementAdded(ev);
        boolean wasAdded = _baseList.add(e);
        if (wasAdded) notifyAfterElementAdded(ev);
        return wasAdded;
    }

    @SuppressWarnings("unchecked")
    public boolean remove(final Object o) {
        boolean wasRemoved = false;
        int index = indexOf(o);
        if (index != -1) {
            ListItemEvent<E, List<E>> ev = new ListItemEvent<E, List<E>>(this, (E) o, index);
            notifyBeforeElementRemoved(ev);
            try {
                E item = _baseList.remove(index);
                ev = new ListItemEvent<E, List<E>>(this, item, index);
                notifyAfterElementRemoved(ev);
                wasRemoved = true;
            } catch (IndexOutOfBoundsException ex) {
                wasRemoved = false;
            }
        }
        return wasRemoved;
    }

    public boolean containsAll(final Collection<?> c) {
        return _baseList.containsAll(c);
    }

    public boolean addAll(final Collection<? extends E> c) {
        for (E item : c) {
            CollectionItemEvent<E, List<E>> ev = new CollectionItemEvent<E, List<E>>(this, this, item);
            notifyBeforeElementAdded(ev);
        }
        boolean wasAdded = false;
        for (E item : c) {
            if (_baseList.add(item)) {
                CollectionItemEvent<E, List<E>> ev = new CollectionItemEvent<E, List<E>>(this, this, item);
                notifyAfterElementAdded(this, this, item);
                wasAdded = true;
            }
        }
        return wasAdded;
    }

    private boolean batchRemove(final Collection<?> c, final boolean retain) {
        if (retain) {
            for (E item : _baseList) if (c.contains(item)) notifyBeforeElementRemoved(this, this, item);
        } else {
            for (Object item : c) notifyBeforeElementRemoved(this, this, (E) item);
        }
        boolean wasRemoved = false;
        Iterator<E> iter = _baseList.iterator();
        while (iter.hasNext()) {
            E item = iter.next();
            if (c.contains(item) != retain) {
                wasRemoved = true;
                iter.remove();
                notifyAfterElementRemoved(this, this, item);
            }
        }
        return wasRemoved;
    }

    public boolean removeAll(final Collection<?> c) {
        return batchRemove(c, false);
    }

    public boolean retainAll(final Collection<?> c) {
        return batchRemove(c, true);
    }

    public void clear() {
        if (!isEmpty()) {
            CollectionEvent<E, List<E>> ev = new CollectionEvent<E, List<E>>(this, this);
            notifyBeforeCollectionCleared(ev);
            _baseList.clear();
            notifyAfterCollectionCleared(ev);
        }
    }

    public L getDecoratee() {
        return _baseList;
    }

    @Override
    public String toString() {
        return _baseList.toString();
    }
}
