package org.javacl;

public class Vector<T> implements Container<T> {

    int size;

    int capacity;

    T elements[];

    public Vector() {
        this(10);
    }

    public Vector(int initialCapacity) {
        size = 0;
        capacity = initialCapacity;
        @SuppressWarnings("unchecked") T newData[] = (T[]) new Object[capacity];
        elements = newData;
    }

    @Override
    public Vector<T> copy() {
        Vector<T> result = new Vector<T>(capacity);
        System.arraycopy(elements, 0, result.elements, 0, size);
        return result;
    }

    public void addAll(Container_const<T> src) {
        int newSize = size + src.size();
        if (newSize > capacity) {
            int newCapacity = capacity;
            do {
                newCapacity = newCapacity * 2;
            } while (newSize > newCapacity);
            setCapacity(newCapacity);
        }
        if (src instanceof Vector) {
            Vector<T> srcVector = (Vector<T>) src;
            System.arraycopy(srcVector.elements, 0, elements, size, src.size());
        } else {
            int location = size;
            for (Container_const.Iterator<T> iter = src.first(); iter.getStatus(); iter.next(), location++) {
                elements[location] = iter.get();
            }
        }
        size = newSize;
    }

    public void add(T data) {
        if (size == capacity) setCapacity(capacity * 2);
        elements[size] = data;
        size++;
    }

    public void removeLast() {
        assert (size != 0);
        elements[size - 1] = null;
        size--;
    }

    public T get(int index) {
        assert index < size && index >= 0;
        return elements[index];
    }

    @Override
    public T firstEntry() {
        assert size > 0;
        return elements[0];
    }

    @Override
    public T lastEntry() {
        assert size > 0;
        return elements[size - 1];
    }

    public void setCapacity(int newCapacity) {
        assert newCapacity >= size;
        @SuppressWarnings("unchecked") T newData[] = (T[]) new Object[newCapacity];
        System.arraycopy(elements, 0, newData, 0, size);
        capacity = newCapacity;
        elements = newData;
    }

    @Override
    public void erase(Container.Iterator<T> iter) {
        Iterator myIter = (Iterator) iter;
        assert myIter.index >= 0 && myIter.index < size;
        erase(myIter.index);
    }

    @Override
    public void eraseAdvance(org.javacl.Container.Iterator<T> iter) {
        Iterator myIter = (Iterator) iter;
        assert myIter.index >= 0 && myIter.index < size;
        erase(myIter.index);
        myIter.lastOpSuccessfull = myIter.index < size;
    }

    public void erase(int index) {
        System.arraycopy(elements, index, elements, index + 1, size - index - 1);
        size--;
        elements[size - 1] = null;
    }

    @Override
    public boolean empty() {
        return size == 0;
    }

    @Override
    public Iterator first() {
        return new Iterator(0, size > 0);
    }

    @Override
    public Iterator last() {
        return new Iterator(size - 1, size > 0);
    }

    @Override
    public int size() {
        return size;
    }

    class Iterator implements Container.Iterator<T> {

        int index;

        boolean lastOpSuccessfull = true;

        Iterator(int index) {
            this.index = index;
        }

        Iterator(int index, boolean lastOpSuccessfull) {
            this.index = index;
            this.lastOpSuccessfull = lastOpSuccessfull;
        }

        @Override
        public Iterator copy() {
            return new Iterator(index, lastOpSuccessfull);
        }

        @Override
        public T get() {
            assert index < size && lastOpSuccessfull;
            return elements[index];
        }

        @Override
        public boolean getStatus() {
            return lastOpSuccessfull;
        }

        @Override
        public void next() {
            index++;
            lastOpSuccessfull = index < size;
        }

        @Override
        public void prev() {
            index--;
            lastOpSuccessfull = index > 0;
        }
    }

    @Override
    public JavaUtilIter iterator() {
        return new JavaUtilIter();
    }

    class JavaUtilIter implements java.util.Iterator<T> {

        int index = 0;

        @Override
        public boolean hasNext() {
            return index < size;
        }

        @Override
        public T next() {
            T result = elements[index];
            index++;
            return result;
        }

        @Override
        public void remove() {
            index--;
        }
    }
}
