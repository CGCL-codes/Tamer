package tefkat.model.internal;

import java.util.Arrays;
import java.util.Collection;

/**
 * @author lawley
 *
 */
public class IntMap {

    private static final double LOAD_FACTOR = 1.5;

    private int size;

    private Entry[] contents;

    public IntMap() {
        this(5);
    }

    public IntMap(int initialCapacity) {
        int idx = Arrays.binarySearch(primes, initialCapacity);
        if (idx >= 0) {
            initialCapacity = primes[idx];
        } else {
            initialCapacity = primes[-idx - 1];
        }
        contents = new Entry[initialCapacity];
        size = 0;
    }

    public Collection entries() {
        return Arrays.asList(contents);
    }

    public int size() {
        return size;
    }

    public void put(Object key, int val) {
        if (contents.length < size * LOAD_FACTOR) {
            resize();
        }
        int h = hash(key);
        int i;
        for (i = 0; null != contents[h] && !contents[h].getKey().equals(key) && i < contents.length; i++) {
            h = hash2(h);
        }
        if (null == contents[h]) {
            contents[h] = new Entry(key, val);
            size++;
        } else if (i >= contents.length) {
            throw new IllegalStateException("Map is full - cannot add new entry: " + key + " -> " + val);
        } else {
            contents[h].setValue(val);
        }
    }

    /**
     * 
     * @param key
     * 
     * @return  mapped int or zero if not found
     */
    public int get(Object key) {
        int h = hash(key);
        int i;
        for (i = 0; null != contents[h] && !contents[h].getKey().equals(key) && i < contents.length; i++) {
            h = hash2(h);
        }
        if (null == contents[h] || i >= contents.length) {
            return 0;
        }
        return contents[h].getValue();
    }

    private int hash(Object key) {
        return key.hashCode() % contents.length;
    }

    private int hash2(int hash1) {
        return 1 + (hash1 % (contents.length - 1));
    }

    /**
     * Increase the size by a factor of approximately 2
     *
     */
    private void resize() {
        int newSize;
        int idx = Arrays.binarySearch(primes, contents.length * 2);
        if (idx >= 0) {
            newSize = primes[idx];
        } else {
            newSize = primes[-idx - 1];
        }
        Entry[] oldContents = contents;
        contents = new Entry[newSize];
        size = 0;
        for (int i = 0; i < oldContents.length; i++) {
            Entry entry = oldContents[i];
            if (null != entry) {
                put(entry.getKey(), entry.getValue());
            }
        }
    }

    private static int[] primes = { 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97, 101, 103, 107, 109, 113, 127, 131, 137, 139, 149, 151, 157, 163, 167, 173, 179, 181, 191, 193, 197, 199, 211, 223, 227, 229, 233, 239, 241, 251, 257, 263, 269, 271, 277, 281, 283, 293, 307, 311, 313, 317, 331, 337, 347, 349, 353, 359, 367, 373, 379, 383, 389, 397, 401, 409, 419, 421, 431, 433, 439, 443, 449, 457, 461, 463, 467, 479, 487, 491, 499, 503, 509, 521, 523, 541, 547, 557, 563, 569, 571, 577, 587, 593, 599, 601, 607, 613, 617, 619, 631, 641, 643, 647, 653, 659, 661, 673, 677, 683, 691, 701, 709, 719, 727, 733, 739, 743, 751, 757, 761, 769, 773, 787, 797, 809, 811, 821, 823, 827, 829, 839, 853, 857, 859, 863, 877, 881, 883, 887, 907, 911, 919, 929, 937, 941, 947, 953, 967, 971, 977, 983, 991, 997, 1009 };

    public static class Entry {

        public Object key;

        public int value;

        Entry(Object key, int value) {
            this.key = key;
            this.value = value;
        }

        public Object getKey() {
            return key;
        }

        public int getValue() {
            return value;
        }

        public int setValue(int value) {
            int old = this.value;
            this.value = value;
            return old;
        }
    }
}
