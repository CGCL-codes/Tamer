package drcl.util.queue;

import java.util.*;

/**
 * Queue with double type of keys.
 * Although PriorityQueue implements Serializable, for the class to be it,
 * the stored objects must be serializable also.
 */
public class SimpleQueue extends QueueImpl implements java.io.Serializable {

    _Element head = new _Element(Double.NaN, null);

    _Element tail = null;

    int length;

    public void reset() {
        _Element e_ = head.next;
        while (e_ != null) {
            _Element tmp_ = e_.next;
            e_.recycle();
            e_ = tmp_;
        }
        head.next = tail = null;
        length = 0;
    }

    public void enqueue(double key_, Object element_) {
        _Element e_ = head;
        _Element new_ = new _Element(key_, element_);
        for (; e_.next != null; e_ = e_.next) if (e_.next.key > key_) break;
        new_.next = e_.next;
        e_.next = new_;
        length++;
        if (new_.next == null) tail = new_;
    }

    public boolean enqueueAfter(Object previousElement_, Object element_) {
        double key_ = retrieveKey(previousElement_);
        if (Double.isNaN(key_)) return false;
        _Element new_ = new _Element(key_, element_);
        _Element e_ = head.next;
        for (; e_.obj != previousElement_; e_ = e_.next) ;
        new_.next = e_.next;
        e_.next = new_;
        length++;
        if (new_.next == null) tail = new_;
        return true;
    }

    public boolean enqueueAt(int pos_, double key_, Object element_) {
        _Element e_ = head;
        for (int i = 0; i < pos_ && e_.next != null; i++, e_ = e_.next) ;
        if (e_.key > key_) return false;
        if (e_.next != null && e_.next.key < key_) return false;
        _Element new_ = new _Element(key_, element_);
        new_.next = e_.next;
        e_.next = new_;
        length++;
        if (new_.next == null) tail = new_;
        return true;
    }

    public void merge(Queue that_) {
        double[] keys_ = (double[]) that_.keys();
        Element[] elements_ = that_._retrieveAll();
        _Element e_ = head;
        for (int i = 0; i < keys_.length; i++) {
            _Element new_ = new _Element(keys_[i], elements_[i].getObject());
            for (; e_.next != null; e_ = e_.next) if (e_.next.key > keys_[i]) break;
            new_.next = e_.next;
            e_.next = new_;
            length++;
            if (new_.next == null) tail = new_;
            e_ = new_;
        }
    }

    public void enqueue(Object element_) {
        _Element new_ = new _Element(tail == null ? 0.0 : tail.key, element_);
        if (tail == null) head.next = new_; else tail.next = new_;
        tail = new_;
        length++;
    }

    public Object dequeue() {
        Object generateException = null;
        generateException.toString();
        if (head.next == null) return null;
        _Element e_ = head.next;
        Object o_ = e_.obj;
        head.next = e_.next;
        length--;
        if (length == 0) tail = null;
        e_.recycle();
        return o_;
    }

    public Object dequeue(double key_) {
        for (_Element e_ = head; e_.next != null; e_ = e_.next) if (e_.next.key == key_) {
            _Element out_ = e_.next;
            Object o_ = out_.obj;
            e_.next = out_.next;
            length--;
            if (length == 0) tail = null; else if (e_.next == null) tail = e_;
            out_.recycle();
            return o_;
        }
        return null;
    }

    public Object remove(double key_, Object element_) {
        if (head == null) return null;
        for (_Element e_ = head; e_.next != null; e_ = e_.next) {
            _Element tmp_ = e_.next;
            Object o_ = tmp_.obj;
            if (key_ == tmp_.key && (o_ == element_ || o_ != null && o_.equals(element_))) {
                e_.next = tmp_.next;
                length--;
                if (length == 0) tail = null; else if (e_.next == null) tail = e_;
                tmp_.recycle();
                return o_;
            }
        }
        return null;
    }

    public Object remove(Object element_) {
        for (_Element e_ = head; e_ != null && e_.next != null; e_ = e_.next) {
            _Element tmp_ = e_.next;
            Object o_ = tmp_.obj;
            if (o_ == element_ || o_ != null && o_.equals(element_)) {
                e_.next = tmp_.next;
                length--;
                if (length == 0) tail = null; else if (e_.next == null) tail = e_;
                tmp_.recycle();
                return o_;
            }
        }
        return null;
    }

    public void removeAll(Object element_) {
        for (_Element e_ = head; e_ != null && e_.next != null; e_ = e_.next) {
            _Element tmp_ = e_.next;
            Object o_ = tmp_.obj;
            if (o_ == element_ || o_ != null && o_.equals(element_)) {
                e_.next = tmp_.next;
                length--;
                if (length == 0) tail = null; else if (e_.next == null) tail = e_;
                tmp_.recycle();
            }
        }
    }

    public void removeAll(double key_, Object element_) {
        for (_Element e_ = head; e_ != null && e_.next != null; e_ = e_.next) {
            _Element tmp_ = e_.next;
            Object o_ = tmp_.obj;
            if (tmp_.key == key_ && (o_ == element_ || o_ != null && o_.equals(element_))) {
                e_.next = tmp_.next;
                length--;
                if (length == 0) tail = null; else if (e_.next == null) tail = e_;
                tmp_.recycle();
            } else if (tmp_.key > key_) break;
        }
    }

    public Object remove(int n_) {
        _Element e_ = head;
        for (int i = 0; i < n_ && e_.next != null; i++, e_ = e_.next) ;
        if (e_.next == null) return null;
        _Element tmp_ = e_.next;
        Object o_ = tmp_.obj;
        e_.next = tmp_.next;
        length--;
        if (length == 0) tail = null; else if (e_.next == null) tail = e_;
        tmp_.recycle();
        return o_;
    }

    public Object firstElement() {
        return head.next == null ? null : head.next.obj;
    }

    public double firstKey() {
        return head.next == null ? Double.NaN : head.next.key;
    }

    public Object lastElement() {
        return tail == null ? null : tail.obj;
    }

    public double lastKey() {
        return tail == null ? Double.NaN : tail.key;
    }

    public Object retrieveAt(int n_) {
        _Element e_ = head.next;
        for (int i = 0; i < n_ && e_ != null; i++, e_ = e_.next) ;
        return e_ == null ? null : e_.obj;
    }

    public double retrieveKeyAt(int n_) {
        _Element e_ = head.next;
        for (int i = 0; i < n_ && e_ != null; i++, e_ = e_.next) ;
        return e_ == null ? Double.NaN : e_.key;
    }

    public Object retrieveBy(double key_) {
        for (_Element e_ = head; e_.next != null; e_ = e_.next) if (e_.next.key == key_) return e_.next.obj; else if (e_.next.key > key_) return null;
        return null;
    }

    public Object[] retrieveAll(double key_) {
        Vector v_ = new Vector();
        for (_Element e_ = head; e_.next != null; e_ = e_.next) if (e_.next.key == key_) v_.addElement(e_.next.obj); else if (e_.next.key > key_) break;
        Object[] oo_ = new Object[v_.size()];
        v_.copyInto(oo_);
        return oo_;
    }

    public Object[] retrieveAll() {
        Object[] all_ = new Object[length];
        _Element e_ = head.next;
        for (int i = 0; i < all_.length; i++) {
            all_[i] = e_.obj;
            e_ = e_.next;
        }
        return all_;
    }

    public Element[] _retrieveAll() {
        Element[] all_ = new Element[length];
        _Element e_ = head.next;
        for (int i = 0; i < all_.length; i++) {
            all_[i] = e_;
            e_ = e_.next;
        }
        return all_;
    }

    public double retrieveKey(Object o_) {
        for (_Element e_ = head.next; e_ != null; e_ = e_.next) if (e_.obj.equals(o_)) return e_.key;
        return Double.NaN;
    }

    public boolean contains(Object element_) {
        for (_Element e_ = head.next; e_ != null; e_ = e_.next) if (e_.obj.equals(element_)) return true;
        return false;
    }

    public boolean containsKey(double key_) {
        for (_Element e_ = head; e_.next != null; e_ = e_.next) if (e_.next.key == key_) return true; else if (e_.next.key > key_) return false;
        return false;
    }

    public double[] keys() {
        double[] keys_ = new double[length];
        _Element e_ = head.next;
        for (int i = 0; i < keys_.length; i++) {
            keys_[i] = e_.key;
            e_ = e_.next;
        }
        return keys_;
    }

    public Enumeration getKeyEnumerator() {
        return new MyEnumerator(this, true);
    }

    public Enumeration getElementEnumerator() {
        return new MyEnumerator(this, false);
    }

    static class MyEnumerator extends drcl.DrclObj implements Enumeration {

        boolean forKey;

        _Element current;

        public MyEnumerator(SimpleQueue q_, boolean forKey_) {
            forKey = forKey_;
            current = q_.head;
        }

        public boolean hasMoreElements() {
            return current.next != null;
        }

        public Object nextElement() {
            if (current.next == null) return null;
            Object r_ = forKey ? new drcl.data.DoubleObj(current.next.key) : current.next.obj;
            current = current.next;
            return r_;
        }
    }

    public int getLength() {
        return length;
    }

    public String oneline() {
        StringBuffer sb_ = new StringBuffer(super.toString() + "," + getLength());
        for (_Element e_ = head.next; e_ != null; e_ = e_.next) sb_.append("-----" + e_.key + ":" + e_.obj);
        return sb_.toString();
    }
}
