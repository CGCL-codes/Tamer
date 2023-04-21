package edu.oswego.cs.dl.util.concurrent;

/**
 * A class useful for offloading synch for byte instance variables.
 *
 * <p>[<a href="http://gee.cs.oswego.edu/dl/classes/EDU/oswego/cs/dl/util/concurrent/intro.html"> Introduction to this package. </a>]
 **/
public class SynchronizedByte extends SynchronizedVariable implements Comparable, Cloneable {

    protected byte value_;

    /** 
   * Make a new SynchronizedByte with the given initial value,
   * and using its own internal lock.
   **/
    public SynchronizedByte(byte initialValue) {
        super();
        value_ = initialValue;
    }

    /** 
   * Make a new SynchronizedByte with the given initial value,
   * and using the supplied lock.
   **/
    public SynchronizedByte(byte initialValue, Object lock) {
        super(lock);
        value_ = initialValue;
    }

    /** 
   * Add amount to value (i.e., set value += amount)
   * @return the new value 
   **/
    public byte add(byte amount) {
        synchronized (lock_) {
            return value_ += amount;
        }
    }

    /** 
   * Set value to value &amp; b.
   * @return the new value 
   **/
    public byte and(byte b) {
        synchronized (lock_) {
            value_ = (byte) (value_ & b);
            return value_;
        }
    }

    /**
   * Set value to newValue only if it is currently assumedValue.
   * @return true if successful
   **/
    public boolean commit(byte assumedValue, byte newValue) {
        synchronized (lock_) {
            boolean success = (assumedValue == value_);
            if (success) value_ = newValue;
            return success;
        }
    }

    public int compareTo(byte other) {
        byte val = get();
        return (val < other) ? -1 : (val == other) ? 0 : 1;
    }

    public int compareTo(SynchronizedByte other) {
        return compareTo(other.get());
    }

    public int compareTo(Object other) {
        return compareTo((SynchronizedByte) other);
    }

    /** 
   * Set the value to its complement
   * @return the new value 
   **/
    public byte complement() {
        synchronized (lock_) {
            value_ = (byte) ~value_;
            return value_;
        }
    }

    /** 
   * Decrement the value.
   * @return the new value 
   **/
    public byte decrement() {
        synchronized (lock_) {
            return --value_;
        }
    }

    /** 
   * Divide value by factor (i.e., set value /= factor)
   * @return the new value 
   **/
    public byte divide(byte factor) {
        synchronized (lock_) {
            return value_ /= factor;
        }
    }

    public boolean equals(Object other) {
        if (other != null && other instanceof SynchronizedByte) return get() == ((SynchronizedByte) other).get(); else return false;
    }

    /** 
   * Return the current value 
   **/
    public final byte get() {
        synchronized (lock_) {
            return value_;
        }
    }

    public int hashCode() {
        return (int) (get());
    }

    /** 
   * Increment the value.
   * @return the new value 
   **/
    public byte increment() {
        synchronized (lock_) {
            return ++value_;
        }
    }

    /** 
   * Multiply value by factor (i.e., set value *= factor)
   * @return the new value 
   **/
    public synchronized byte multiply(byte factor) {
        synchronized (lock_) {
            return value_ *= factor;
        }
    }

    /** 
   * Set the value to the negative of its old value
   * @return the new value 
   **/
    public byte negate() {
        synchronized (lock_) {
            value_ = (byte) (-value_);
            return value_;
        }
    }

    /** 
   * Set value to value | b.
   * @return the new value 
   **/
    public byte or(byte b) {
        synchronized (lock_) {
            value_ = (byte) (value_ | b);
            return value_;
        }
    }

    /** 
   * Set to newValue.
   * @return the old value 
   **/
    public byte set(byte newValue) {
        synchronized (lock_) {
            byte old = value_;
            value_ = newValue;
            return old;
        }
    }

    /** 
   * Subtract amount from value (i.e., set value -= amount)
   * @return the new value 
   **/
    public byte subtract(byte amount) {
        synchronized (lock_) {
            return value_ -= amount;
        }
    }

    /** 
   * Atomically swap values with another SynchronizedByte.
   * Uses identityHashCode to avoid deadlock when
   * two SynchronizedBytes attempt to simultaneously swap with each other.
   * (Note: Ordering via identyHashCode is not strictly guaranteed
   * by the language specification to return unique, orderable
   * values, but in practice JVMs rely on them being unique.)
   * @return the new value 
   **/
    public byte swap(SynchronizedByte other) {
        if (other == this) return get();
        SynchronizedByte fst = this;
        SynchronizedByte snd = other;
        if (System.identityHashCode(fst) > System.identityHashCode(snd)) {
            fst = other;
            snd = this;
        }
        synchronized (fst.lock_) {
            synchronized (snd.lock_) {
                fst.set(snd.set(fst.get()));
                return get();
            }
        }
    }

    public String toString() {
        return Byte.toString(get());
    }

    /** 
   * Set value to value ^ b.
   * @return the new value 
   **/
    public byte xor(byte b) {
        synchronized (lock_) {
            value_ = (byte) (value_ ^ b);
            return value_;
        }
    }
}
