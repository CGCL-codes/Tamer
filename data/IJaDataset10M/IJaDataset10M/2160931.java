package com.memoire.fu;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.NoSuchElementException;

/**
 * A simple vector with public fields.
 * Needed for Yapod serialization.
 */
public class FuVectorPublic implements Cloneable, Serializable {

    public Object data_[];

    public int count_;

    public int increment_;

    public FuVectorPublic(int _capacity, int _increment) {
        super();
        data_ = new Object[_capacity];
        increment_ = _increment;
    }

    public FuVectorPublic(int _capacity) {
        this(_capacity, 0);
    }

    public FuVectorPublic() {
        this(11, 0);
    }

    public synchronized void copyInto(Object[] _array) {
        for (int i = 0; i < count_; i++) _array[i] = data_[i];
    }

    public synchronized void trimToSize() {
        int old_capacity = data_.length;
        if (count_ < old_capacity) {
            Object[] old_data = data_;
            data_ = new Object[count_];
            System.arraycopy(old_data, 0, data_, 0, count_);
        }
    }

    public synchronized void ensureCapacity(int _min) {
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

    public synchronized void setSize(int _new) {
        if ((_new > count_) && (_new > data_.length)) resize0(_new); else for (int i = _new; i < count_; i++) data_[i] = null;
        count_ = _new;
    }

    public int capacity() {
        return data_.length;
    }

    public int size() {
        return count_;
    }

    public boolean isEmpty() {
        return count_ == 0;
    }

    public synchronized Enumeration elements() {
        return new Enumerator(this);
    }

    public synchronized Enumeration reverseElements() {
        return new ReverseEnumerator(this);
    }

    public boolean contains(Object _o) {
        return (indexOf(_o, 0) >= 0);
    }

    public int indexOf(Object _o) {
        return indexOf(_o, 0);
    }

    public synchronized int indexOf(Object _o, int _index) {
        for (int i = _index; i < count_; i++) if (_o.equals(data_[i])) return i;
        return -1;
    }

    public int lastIndexOf(Object _o) {
        return lastIndexOf(_o, count_ - 1);
    }

    public synchronized int lastIndexOf(Object _o, int _index) {
        for (int i = _index; i >= 0; i--) if (_o.equals(data_[i])) return i;
        return -1;
    }

    public synchronized Object elementAt(int _index) {
        return data_[_index];
    }

    public synchronized Object firstElement() {
        if (count_ == 0) throw new NoSuchElementException();
        return data_[0];
    }

    public synchronized Object lastElement() {
        if (count_ == 0) throw new NoSuchElementException();
        return data_[count_ - 1];
    }

    public synchronized void setElementAt(Object _o, int _index) {
        if (_index >= count_) throw new ArrayIndexOutOfBoundsException();
        data_[_index] = _o;
    }

    public synchronized void removeElementAt(int _index) {
        int j = count_ - 1 - _index;
        if (j > 0) System.arraycopy(data_, _index + 1, data_, _index, j);
        count_--;
        data_[count_] = null;
    }

    public synchronized void insertElementAt(Object _o, int _index) {
        if (_index == count_) addElement(_o); else {
            if (count_ >= data_.length) resize0(count_ + 1);
            count_++;
            System.arraycopy(data_, _index, data_, _index + 1, count_ - 1 - _index);
            data_[_index] = _o;
        }
    }

    public synchronized void addElement(Object _o) {
        if (count_ >= data_.length) resize0(count_ + 1);
        data_[count_] = _o;
        count_++;
    }

    public synchronized boolean removeElement(Object _o) {
        int i = indexOf(_o);
        if (i >= 0) {
            removeElementAt(i);
            return true;
        }
        return false;
    }

    public synchronized void removeAllElements() {
        for (int i = 0; i < count_; i++) data_[i] = null;
        count_ = 0;
    }

    public synchronized Object clone() {
        FuVectorPublic r = null;
        try {
            r = (FuVectorPublic) super.clone();
            r.data_ = new Object[count_];
            System.arraycopy(data_, 0, r.data_, 0, count_);
        } catch (CloneNotSupportedException ex) {
            throw new InternalError();
        }
        return r;
    }

    public String toString() {
        return "FuVectorPublic(" + count_ + ")";
    }

    private static class Enumerator implements Enumeration {

        private FuVectorPublic vector;

        private int count;

        Enumerator(FuVectorPublic _v) {
            vector = _v;
            count = 0;
        }

        public final boolean hasMoreElements() {
            return count < vector.count_;
        }

        public final Object nextElement() {
            if (count < vector.count_) return vector.data_[count++];
            throw new NoSuchElementException("FuVectorPublic.Enumerator");
        }
    }

    private static class ReverseEnumerator implements Enumeration {

        private FuVectorPublic vector;

        private int count;

        ReverseEnumerator(FuVectorPublic _v) {
            vector = _v;
            count = vector.size() - 1;
        }

        public final boolean hasMoreElements() {
            return count >= 0;
        }

        public final Object nextElement() {
            if ((count >= 0) && (count < vector.size())) return vector.elementAt(count--);
            throw new NoSuchElementException("FuVectorPublic.ReverseEnumerator");
        }
    }
}
