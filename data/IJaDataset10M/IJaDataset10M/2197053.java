package org.javacl;

import java.util.Comparator;

public class TreeMap<K, V> extends RBTree<K, V> implements Map<K, V>, SortedMap_const<K, V> {

    public TreeMap() {
        super();
    }

    public TreeMap(Comparator<K> customComperator) {
        super(customComperator);
    }

    public TreeMap(Map_const<K, V> src) {
        for (Map_const.Iterator<K, V> iter = src.first(); iter.getStatus(); iter.next()) {
            insertData(iter.getKey(), iter.getValue(), false);
        }
    }

    @Override
    public Iterator first() {
        return new Iterator(getMin(), size > 0);
    }

    @Override
    public Iterator last() {
        return new Iterator(getMax(), size > 0);
    }

    @Override
    public TreeMap<K, V> copy() {
        TreeMap<K, V> newCopy = new TreeMap<K, V>(comparator);
        for (Map.Entry<K, V> item : this) {
            newCopy.insert(item.getKey(), item.getValue());
        }
        return newCopy;
    }

    @Override
    public Iterator insert(K key, V value) {
        RBNode<K, V> result = insertData(key, value, false);
        return new Iterator(result, lastInsertWasSuccessfull);
    }

    @Override
    public void erase(Map.Iterator<K, V> iter) {
        RBNode<K, V> node = ((Iterator) iter).node;
        assert node != null;
        erase(node);
    }

    @Override
    public void eraseAdvance(Map.Iterator<K, V> iter) {
        RBNode<K, V> node = ((Iterator) iter).node;
        assert node != null;
        iter.next();
        erase(node);
    }

    @Override
    public Iterator find(K key) {
        RBNode<K, V> result = findNode(key);
        return new Iterator(result, result != null);
    }

    @Override
    public V get(K key) {
        RBNode<K, V> result = findNode(key);
        return (result == null) ? null : result.value;
    }

    @Override
    public boolean containsKey(K key) {
        RBNode<K, V> result = findNode(key);
        return (result == null) ? false : true;
    }

    @Override
    public Iterator upperBound(K key) {
        RBNode<K, V> result = upper_bound(key);
        return new Iterator(result, result != null);
    }

    @Override
    public Iterator lowerBound(K key) {
        RBNode<K, V> result = lower_bound(key);
        return new Iterator(result, result != null);
    }

    public class Iterator implements Map.Iterator<K, V> {

        RBNode<K, V> node;

        boolean lastOpSuccessfull;

        Iterator(RBNode<K, V> node, boolean lastOpSuccessfull) {
            assert node != null;
            this.node = node;
            this.lastOpSuccessfull = lastOpSuccessfull;
        }

        @Override
        public K getKey() {
            return node.getKey();
        }

        @Override
        public V getValue() {
            return node.getValue();
        }

        @Override
        public void setValue(V value) {
            node.setValue(value);
        }

        @Override
        public void next() {
            assert node != null;
            node = node.next();
            lastOpSuccessfull = (node != null);
        }

        @Override
        public void prev() {
            assert node != null;
            node = node.prev();
            lastOpSuccessfull = (node != null);
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof TreeMap.Iterator)) return false;
            Iterator other = (Iterator) o;
            return node == other.node;
        }

        @Override
        public Iterator copy() {
            return new Iterator(node, lastOpSuccessfull);
        }

        @Override
        public boolean getStatus() {
            return lastOpSuccessfull;
        }
    }

    @Override
    public JavaUtilIter iterator() {
        return new JavaUtilIter();
    }

    public class JavaUtilIter implements java.util.Iterator<Map.Entry<K, V>> {

        boolean consumed;

        RBNode<K, V> node;

        RBNode<K, V> lastNode;

        JavaUtilIter() {
            node = getMin();
            lastNode = null;
            consumed = false;
        }

        @Override
        public boolean hasNext() {
            if (consumed) {
                node = node.next();
                consumed = false;
            }
            return node == null;
        }

        @Override
        public Map.Entry<K, V> next() {
            if (consumed) {
                node = node.next();
            }
            assert node != null;
            lastNode = node;
            consumed = true;
            return node;
        }

        @Override
        public void remove() {
            assert lastNode != null;
            if (consumed) {
                node = node.next();
                consumed = false;
            }
            erase(lastNode);
            lastNode = null;
        }
    }
}
