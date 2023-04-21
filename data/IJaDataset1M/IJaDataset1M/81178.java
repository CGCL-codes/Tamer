package gwt.mosaic.client.util.observablecollections;

import gwt.mosaic.client.events.ElementUpdateEvent;
import gwt.mosaic.client.events.ElementUpdateHandler;
import gwt.mosaic.client.events.ElementsInsertedEvent;
import gwt.mosaic.client.events.ElementsInsertedHandler;
import gwt.mosaic.client.events.ElementsRemovedEvent;
import gwt.mosaic.client.events.ElementsRemovedHandler;
import gwt.mosaic.client.events.KeyAddedEvent;
import gwt.mosaic.client.events.KeyAddedHandler;
import gwt.mosaic.client.events.KeyRemovedEvent;
import gwt.mosaic.client.events.KeyRemovedHandler;
import gwt.mosaic.client.events.KeyValueUpdateEvent;
import gwt.mosaic.client.events.KeyValueUpdateHandler;
import java.util.AbstractList;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;

public final class ObservableCollections {

    public static <E> ObservableList<E> observableList(List<E> list) {
        if (list == null) {
            throw new IllegalArgumentException("list is null.");
        }
        if (list instanceof ObservableList) {
            return (ObservableList<E>) list;
        } else {
            return new ObservableListImpl<E>(list);
        }
    }

    private static final class ObservableListImpl<E> extends AbstractList<E> implements ObservableList<E> {

        private List<E> list;

        private transient HandlerManager handlerManager;

        ObservableListImpl(List<E> list) {
            this.list = list;
        }

        @Override
        public HandlerRegistration addElemntInsertedAhndler(ElementsInsertedHandler handler) {
            return ensureHandlers().addHandler(ElementsInsertedEvent.getType(), handler);
        }

        @Override
        public HandlerRegistration addElemntRemovedAhndler(ElementsRemovedHandler<E> handler) {
            return ensureHandlers().addHandler(ElementsRemovedEvent.getType(), handler);
        }

        @Override
        public HandlerRegistration addElementUpdateHandler(ElementUpdateHandler<E> handler) {
            return ensureHandlers().addHandler(ElementUpdateEvent.getType(), handler);
        }

        private HandlerManager ensureHandlers() {
            return handlerManager == null ? handlerManager = new HandlerManager(this) : handlerManager;
        }

        @Override
        public void fireEvent(GwtEvent<?> event) {
            if (handlerManager != null) {
                handlerManager.fireEvent(event);
            }
        }

        private void fireElementsInsertedEvent(int index, int length) {
            ElementsInsertedEvent.fire(this, index, length);
        }

        private void fireElementsRemovedEvent(int index, List<E> oldElements) {
            ElementsRemovedEvent.fire(this, index, oldElements);
        }

        @SuppressWarnings("unused")
        private void fireElementUpdatedEvent(int index, E previousValue) {
            ElementUpdateEvent.fire(this, index, previousValue);
        }

        private void fireElementUpdatedEvent(int index, E oldValue, E newValue) {
            ElementUpdateEvent.fire(this, index, oldValue, newValue);
        }

        public E get(int index) {
            return list.get(index);
        }

        public int size() {
            return list.size();
        }

        public E set(int index, E element) {
            E oldValue = list.set(index, element);
            fireElementUpdatedEvent(index, oldValue, element);
            return oldValue;
        }

        public void add(int index, E element) {
            list.add(index, element);
            fireElementsInsertedEvent(index, 1);
        }

        public E remove(int index) {
            E oldValue = list.remove(index);
            fireElementsRemovedEvent(index, java.util.Collections.singletonList(oldValue));
            return oldValue;
        }

        public boolean addAll(Collection<? extends E> c) {
            return addAll(size(), c);
        }

        public boolean addAll(int index, Collection<? extends E> c) {
            if (list.addAll(index, c)) {
                fireElementsInsertedEvent(index, c.size());
                return true;
            }
            return false;
        }

        public void clear() {
            List<E> dup = new ArrayList<E>(list);
            list.clear();
            if (dup.size() != 0) {
                fireElementsRemovedEvent(0, dup);
            }
        }

        public boolean containsAll(Collection<?> c) {
            return list.containsAll(c);
        }

        public <T> T[] toArray(T[] a) {
            return list.toArray(a);
        }

        public Object[] toArray() {
            return list.toArray();
        }
    }

    public static <K, V> ObservableMap<K, V> observable(Map<K, V> map) {
        if (map == null) {
            throw new IllegalArgumentException("map is null.");
        }
        if (map instanceof ObservableMap) {
            return (ObservableMap<K, V>) map;
        } else {
            return new ObservableMapImpl<K, V>(map);
        }
    }

    private static final class ObservableMapImpl<K, V> extends AbstractMap<K, V> implements ObservableMap<K, V> {

        private final Map<K, V> map;

        private Set<Map.Entry<K, V>> entrySet;

        private transient HandlerManager handlerManager;

        ObservableMapImpl(Map<K, V> map) {
            this.map = map;
        }

        @Override
        public HandlerRegistration addKeyAddedHandler(KeyAddedHandler<K> handler) {
            return ensureHandlers().addHandler(KeyAddedEvent.getType(), handler);
        }

        @Override
        public HandlerRegistration addKeyRemovedHandler(KeyRemovedHandler<K> handler) {
            return ensureHandlers().addHandler(KeyRemovedEvent.getType(), handler);
        }

        @Override
        public HandlerRegistration addKeyValueUpdateHandler(KeyValueUpdateHandler<K, V> handler) {
            return ensureHandlers().addHandler(KeyValueUpdateEvent.getType(), handler);
        }

        private HandlerManager ensureHandlers() {
            return handlerManager == null ? handlerManager = new HandlerManager(this) : handlerManager;
        }

        @Override
        public void fireEvent(GwtEvent<?> event) {
            if (handlerManager != null) {
                handlerManager.fireEvent(event);
            }
        }

        private void fireKeyAddedEvent(K key) {
            KeyAddedEvent.fire(this, key);
        }

        private void fireKeyRemovedEvent(K key) {
            KeyRemovedEvent.fire(this, key);
        }

        @SuppressWarnings("unused")
        private void fireKeyValueUpdatedEvent(K key, V previousValue) {
            KeyValueUpdateEvent.fire(this, key, previousValue);
        }

        private void fireKeyValueUpdatedEvent(K key, V oldValue, V newValue) {
            KeyValueUpdateEvent.fire(this, key, oldValue, newValue);
        }

        @Override
        public void clear() {
            Iterator<K> iterator = keySet().iterator();
            while (iterator.hasNext()) {
                iterator.next();
                iterator.remove();
            }
        }

        @Override
        public boolean containsKey(Object key) {
            return map.containsKey(key);
        }

        @Override
        public boolean containsValue(Object value) {
            return map.containsValue(value);
        }

        @Override
        public Set<Map.Entry<K, V>> entrySet() {
            Set<Map.Entry<K, V>> es = entrySet;
            return es != null ? es : (entrySet = new EntrySet());
        }

        public V get(Object key) {
            return map.get(key);
        }

        public boolean isEmpty() {
            return map.isEmpty();
        }

        public V put(K key, V value) {
            V lastValue = map.put(key, value);
            if (containsKey(key)) {
                fireKeyValueUpdatedEvent(key, lastValue, value);
            } else {
                fireKeyAddedEvent(key);
            }
            return lastValue;
        }

        public void putAll(Map<? extends K, ? extends V> m) {
            for (K key : m.keySet()) {
                put(key, m.get(key));
            }
        }

        @SuppressWarnings("unchecked")
        public V remove(Object key) {
            if (containsKey(key)) {
                V value = map.remove(key);
                fireKeyRemovedEvent((K) key);
                return value;
            }
            return null;
        }

        public int size() {
            return map.size();
        }

        private class EntryIterator implements Iterator<Map.Entry<K, V>> {

            private Iterator<Map.Entry<K, V>> realIterator;

            private Map.Entry<K, V> last;

            EntryIterator() {
                realIterator = map.entrySet().iterator();
            }

            public boolean hasNext() {
                return realIterator.hasNext();
            }

            public Map.Entry<K, V> next() {
                last = realIterator.next();
                return last;
            }

            public void remove() {
                if (last == null) {
                    throw new IllegalStateException();
                }
                Object toRemove = last.getKey();
                last = null;
                ObservableMapImpl.this.remove(toRemove);
            }
        }

        private class EntrySet extends AbstractSet<Map.Entry<K, V>> {

            public Iterator<Map.Entry<K, V>> iterator() {
                return new EntryIterator();
            }

            @SuppressWarnings("unchecked")
            public boolean contains(Object o) {
                if (!(o instanceof Map.Entry)) {
                    return false;
                }
                Map.Entry<K, V> e = (Map.Entry<K, V>) o;
                return containsKey(e.getKey());
            }

            @SuppressWarnings("unchecked")
            public boolean remove(Object o) {
                if (o instanceof Map.Entry) {
                    K key = ((Map.Entry<K, V>) o).getKey();
                    if (containsKey(key)) {
                        remove(key);
                        return true;
                    }
                }
                return false;
            }

            public int size() {
                return ObservableMapImpl.this.size();
            }

            public void clear() {
                ObservableMapImpl.this.clear();
            }
        }
    }
}
