package jopt.csp.util;

/**
 * Base class for integer sets
 */
public abstract class IntSet implements NumSet {

    /**
     *  Creates a new set
     */
    public IntSet() {
    }

    /**
     * Removes all values from the set
     */
    public abstract void clear();

    /**
     * Returns minimum value for set
     */
    public abstract int getMin();

    /**
     * Returns maximum value for set
     */
    public abstract int getMax();

    /**
     * Returns true if value is contained in set
     */
    public abstract boolean contains(int val);

    /**
     * Adds a value to set
     */
    public abstract void add(int val);

    /**
     * Adds a range of values to set
     */
    public abstract void add(int start, int end);

    /**
     * Adds all values to set
     */
    public final void addAll(NumSet set) {
        if (set instanceof IntervalSet) {
            IntervalSet iset = (IntervalSet) set;
            IntervalIterator intervalIter = iset.intervals();
            while (intervalIter.hasNext()) {
                add(intervalIter.nextInt(), intervalIter.endInt());
            }
        } else {
            NumberIterator valIter = set.values();
            while (valIter.hasNext()) {
                valIter.next();
                add(valIter.intValue());
            }
        }
    }

    /**
     * Removes a value from the set
     */
    public abstract void remove(int val);

    /**
     * Removes a range of values from the set
     */
    public abstract void remove(int start, int end);

    /**
     * Removes all values above given value
     */
    public final void removeStartingAfter(int val) {
        if (val == Integer.MAX_VALUE) return;
        removeStartingFrom(val + 1);
    }

    /**
     * Removes all values above and including given value
     */
    public abstract void removeStartingFrom(int val);

    /**
     * Removes all values below given value
     */
    public final void removeEndingBefore(int val) {
        if (val == Integer.MIN_VALUE) return;
        removeEndingAt(val - 1);
    }

    /**
     * Removes all values below and including given value
     */
    public abstract void removeEndingAt(int val);

    /**
     * Removes all values in a given set
     */
    public final void removeAll(NumSet set) {
        if (size() == 0) return;
        if (set instanceof IntervalSet) {
            IntervalSet iset = (IntervalSet) set;
            IntervalIterator intervalIter = iset.intervals();
            while (intervalIter.hasNext()) {
                remove(intervalIter.nextInt(), intervalIter.endInt());
            }
        } else {
            NumberIterator valIter = set.values();
            while (valIter.hasNext()) {
                valIter.next();
                remove(valIter.intValue());
            }
        }
    }

    /**
     * Retains only given values in a given set
     */
    public final void retainAll(NumSet set) {
        if (size() == 0) return;
        NumSet working = (NumSet) clone();
        working.removeAll(set);
        removeAll(working);
        releaseObject(working);
    }

    /**
     * Returns the next higher value in the domain or current value if none
     * exists
     */
    public abstract int getNextHigher(int val);

    /**
     * Returns the next lower value in the domain or current value if none
     * exists
     */
    public abstract int getNextLower(int val);

    /**
     * Creates a duplicate of this set
     */
    public abstract Object clone();

    /**
     * Releases an object that is not needed for pooling purposes
     */
    protected void releaseObject(Object o) {
    }
}
