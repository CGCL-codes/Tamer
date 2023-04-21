package com.memoire.fu;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.NoSuchElementException;

/**
 * A fast vector not synchronized and using == as test.
 */
public final class FuVectorFast implements Serializable {

    Object data_[];

    int count_;

    private int increment_;

    public FuVectorFast(int _capacity, int _increment) {
        super();
        data_ = new Object[_capacity];
        increment_ = _increment;
    }

    public FuVectorFast(int _capacity) {
        this(_capacity, 0);
    }

    public FuVectorFast() {
        this(10);
    }

    public final void copyInto(Object[] _array) {
        for (int i = 0; i < count_; i++) _array[i] = data_[i];
    }

    public final void trimToSize() {
        int old_capacity = data_.length;
        if (count_ < old_capacity) {
            Object[] old_data = data_;
            data_ = new Object[count_];
            System.arraycopy(old_data, 0, data_, 0, count_);
        }
    }

    public final void ensureCapacity(int _min) {
        if (_min > data_.length) resize0(_min);
    }

    private void resize0(int _min) {
        int old_capacity = data_.length;
        Object[] old_data = data_;
        int new_capacity = ((increment_ > 0) ? (old_capacity + increment_) : (old_capacity * 2));
        if (new_capacity < _min) new_capacity = _min;
        data_ = new Object[new_capacity];
        System.arraycopy(old_data, 0, data_, 0, count_);
    }

    public final void setSize(int _new) {
        if ((_new > count_) && (_new > data_.length)) resize0(_new); else for (int i = _new; i < count_; i++) data_[i] = null;
        count_ = _new;
    }

    public final int capacity() {
        return data_.length;
    }

    public final int size() {
        return count_;
    }

    public final boolean isEmpty() {
        return count_ == 0;
    }

    public final Enumeration elements() {
        return new Enumerator(this);
    }

    public final boolean contains(Object _o) {
        return (indexOf(_o, 0) >= 0);
    }

    public final int indexOf(Object _o) {
        return indexOf(_o, 0);
    }

    public final int indexOf(Object _o, int _index) {
        for (int i = _index; i < count_; i++) if (_o == data_[i]) return i;
        return -1;
    }

    public final int lastIndexOf(Object _o) {
        return lastIndexOf(_o, count_ - 1);
    }

    public final int lastIndexOf(Object _o, int _index) {
        for (int i = _index; i >= 0; i--) if (_o == data_[i]) return i;
        return -1;
    }

    public final Object elementAt(int _index) {
        return data_[_index];
    }

    public final Object firstElement() {
        if (count_ == 0) throw new NoSuchElementException();
        return data_[0];
    }

    public final Object lastElement() {
        if (count_ == 0) throw new NoSuchElementException();
        return data_[count_ - 1];
    }

    public final void setElementAt(Object _o, int _index) {
        if (_index >= count_) throw new ArrayIndexOutOfBoundsException();
        data_[_index] = _o;
    }

    public final void removeElementAt(int _index) {
        int j = count_ - _index - 1;
        if (j > 0) System.arraycopy(data_, _index + 1, data_, _index, j);
        count_--;
        data_[count_] = null;
    }

    public final void insertElementAt(Object _o, int _index) {
        if (_index == count_) addElement(_o); else {
            if (count_ >= data_.length) resize0(count_ + 1);
            count_++;
            System.arraycopy(data_, _index, data_, _index + 1, count_ - 1 - _index);
            data_[_index] = _o;
        }
    }

    public final void addElement(Object _o) {
        if (count_ >= data_.length) resize0(count_ + 1);
        data_[count_] = _o;
        count_++;
    }

    public final boolean removeElement(Object _o) {
        int i = indexOf(_o);
        if (i >= 0) {
            removeElementAt(i);
            return true;
        }
        return false;
    }

    public final void removeAllElements() {
        for (int i = 0; i < count_; i++) data_[i] = null;
        count_ = 0;
    }

    public final String toString() {
        return "FuVectorFast(" + count_ + ")";
    }

    private static final class Enumerator implements Enumeration {

        private FuVectorFast vector;

        private int count;

        Enumerator(FuVectorFast _v) {
            vector = _v;
            count = 0;
        }

        public final boolean hasMoreElements() {
            return count < vector.count_;
        }

        public final Object nextElement() {
            if (count < vector.count_) return vector.data_[count++];
            throw new NoSuchElementException("FuVectorFast.Enumerator");
        }
    }
}
