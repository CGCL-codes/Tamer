package net.sf.javagimmicks.collections;

import java.util.ArrayList;
import java.util.NoSuchElementException;

public class ArrayRing<E> extends AbstractRing<E> {

    private final ArrayList<E> _backingList;

    public ArrayRing() {
        _backingList = new ArrayList<E>();
    }

    public ArrayRing(int initialCapacity) {
        _backingList = new ArrayList<E>(initialCapacity);
    }

    public int size() {
        return _backingList.size();
    }

    public Traverser<E> traverser() {
        return new ArrayTraverser<E>(this, 0);
    }

    public void ensureCapacity(int minCapacity) {
        _backingList.ensureCapacity(minCapacity);
    }

    private static class ArrayTraverser<E> extends BasicTraverser<E, ArrayRing<E>> {

        private int _position;

        private ArrayTraverser(ArrayRing<E> ring, int position) {
            super(ring);
            _position = position;
        }

        public E get() {
            checkForModification();
            if (_ring._backingList.isEmpty()) {
                throw new NoSuchElementException();
            }
            return _ring._backingList.get(_position);
        }

        public void insertAfter(E value) {
            checkForModification();
            if (_ring.isEmpty()) {
                _ring._backingList.add(value);
            } else {
                _ring._backingList.add(_position + 1, value);
            }
            ++_ring._modCount;
            ++_expectedModCount;
        }

        public void insertBefore(E value) {
            checkForModification();
            if (_position == 0) {
                _ring._backingList.add(value);
            } else {
                _ring._backingList.add(_position, value);
                ++_position;
            }
            ++_ring._modCount;
            ++_expectedModCount;
        }

        public E next() {
            checkForModification();
            if (_ring.isEmpty()) {
                throw new NoSuchElementException("Ring is empty");
            }
            ++_position;
            if (_position == _ring.size()) {
                _position = 0;
            }
            return get();
        }

        public E previous() {
            checkForModification();
            if (_ring.isEmpty()) {
                throw new NoSuchElementException("Ring is empty");
            }
            --_position;
            if (_position == -1) {
                _position += _ring.size();
            }
            return get();
        }

        public E remove() {
            checkForModification();
            if (_ring.isEmpty()) {
                throw new NoSuchElementException("Ring is empty");
            }
            E result = _ring._backingList.remove(_position);
            ++_ring._modCount;
            ++_expectedModCount;
            return result;
        }

        public Traverser<E> traverser() {
            return new ArrayTraverser<E>(_ring, _position);
        }
    }
}
