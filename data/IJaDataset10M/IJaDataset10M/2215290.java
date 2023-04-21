package mcaplantlr.runtime;

import java.util.List;

/**A stripped-down version of org.antlr.misc.BitSet that is just
 * good enough to handle runtime requirements such as FOLLOW sets
 * for automatic error recovery.
 */
public class BitSet implements Cloneable {

    protected static final int BITS = 64;

    protected static final int LOG_BITS = 6;

    protected static final int MOD_MASK = BITS - 1;

    /** The actual data bits */
    protected long bits[];

    /** Construct a bitset of size one word (64 bits) */
    public BitSet() {
        this(BITS);
    }

    /** Construction from a static array of longs */
    public BitSet(long[] bits_) {
        bits = bits_;
    }

    /** Construction from a list of integers */
    public BitSet(List items) {
        this();
        for (int i = 0; i < items.size(); i++) {
            Integer v = (Integer) items.get(i);
            add(v.intValue());
        }
    }

    /** Construct a bitset given the size
     * @param nbits The size of the bitset in bits
     */
    public BitSet(int nbits) {
        bits = new long[((nbits - 1) >> LOG_BITS) + 1];
    }

    public static BitSet of(int el) {
        BitSet s = new BitSet(el + 1);
        s.add(el);
        return s;
    }

    public static BitSet of(int a, int b) {
        BitSet s = new BitSet(Math.max(a, b) + 1);
        s.add(a);
        s.add(b);
        return s;
    }

    public static BitSet of(int a, int b, int c) {
        BitSet s = new BitSet();
        s.add(a);
        s.add(b);
        s.add(c);
        return s;
    }

    public static BitSet of(int a, int b, int c, int d) {
        BitSet s = new BitSet();
        s.add(a);
        s.add(b);
        s.add(c);
        s.add(d);
        return s;
    }

    /** return this | a in a new set */
    public BitSet or(BitSet a) {
        if (a == null) {
            return this;
        }
        BitSet s = (BitSet) this.clone();
        s.orInPlace(a);
        return s;
    }

    /** or this element into this set (grow as necessary to accommodate) */
    public void add(int el) {
        int n = wordNumber(el);
        if (n >= bits.length) {
            growToInclude(el);
        }
        bits[n] |= bitMask(el);
    }

    /**
         * Grows the set to a larger number of bits.
         * @param bit element that must fit in set
         */
    public void growToInclude(int bit) {
        int newSize = Math.max(bits.length << 1, numWordsToHold(bit));
        long newbits[] = new long[newSize];
        System.arraycopy(bits, 0, newbits, 0, bits.length);
        bits = newbits;
    }

    public void orInPlace(BitSet a) {
        if (a == null) {
            return;
        }
        if (a.bits.length > bits.length) {
            setSize(a.bits.length);
        }
        int min = Math.min(bits.length, a.bits.length);
        for (int i = min - 1; i >= 0; i--) {
            bits[i] |= a.bits[i];
        }
    }

    /**
         * Sets the size of a set.
         * @param nwords how many words the new set should be
         */
    private void setSize(int nwords) {
        long newbits[] = new long[nwords];
        int n = Math.min(nwords, bits.length);
        System.arraycopy(bits, 0, newbits, 0, n);
        bits = newbits;
    }

    private static final long bitMask(int bitNumber) {
        int bitPosition = bitNumber & MOD_MASK;
        return 1L << bitPosition;
    }

    public Object clone() {
        BitSet s;
        try {
            s = (BitSet) super.clone();
            s.bits = new long[bits.length];
            System.arraycopy(bits, 0, s.bits, 0, bits.length);
        } catch (CloneNotSupportedException e) {
            throw new InternalError();
        }
        return s;
    }

    public int size() {
        int deg = 0;
        for (int i = bits.length - 1; i >= 0; i--) {
            long word = bits[i];
            if (word != 0L) {
                for (int bit = BITS - 1; bit >= 0; bit--) {
                    if ((word & (1L << bit)) != 0) {
                        deg++;
                    }
                }
            }
        }
        return deg;
    }

    public boolean equals(Object other) {
        if (other == null || !(other instanceof BitSet)) {
            return false;
        }
        BitSet otherSet = (BitSet) other;
        int n = Math.min(this.bits.length, otherSet.bits.length);
        for (int i = 0; i < n; i++) {
            if (this.bits[i] != otherSet.bits[i]) {
                return false;
            }
        }
        if (this.bits.length > n) {
            for (int i = n + 1; i < this.bits.length; i++) {
                if (this.bits[i] != 0) {
                    return false;
                }
            }
        } else if (otherSet.bits.length > n) {
            for (int i = n + 1; i < otherSet.bits.length; i++) {
                if (otherSet.bits[i] != 0) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean member(int el) {
        if (el < 0) {
            return false;
        }
        int n = wordNumber(el);
        if (n >= bits.length) return false;
        return (bits[n] & bitMask(el)) != 0;
    }

    public void remove(int el) {
        int n = wordNumber(el);
        if (n < bits.length) {
            bits[n] &= ~bitMask(el);
        }
    }

    public boolean isNil() {
        for (int i = bits.length - 1; i >= 0; i--) {
            if (bits[i] != 0) return false;
        }
        return true;
    }

    private final int numWordsToHold(int el) {
        return (el >> LOG_BITS) + 1;
    }

    public int numBits() {
        return bits.length << LOG_BITS;
    }

    /** return how much space is being used by the bits array not
     *  how many actually have member bits on.
     */
    public int lengthInLongWords() {
        return bits.length;
    }

    public int[] toArray() {
        int[] elems = new int[size()];
        int en = 0;
        for (int i = 0; i < (bits.length << LOG_BITS); i++) {
            if (member(i)) {
                elems[en++] = i;
            }
        }
        return elems;
    }

    public long[] toPackedArray() {
        return bits;
    }

    private static final int wordNumber(int bit) {
        return bit >> LOG_BITS;
    }

    public String toString() {
        return toString(null);
    }

    public String toString(String[] tokenNames) {
        StringBuffer buf = new StringBuffer();
        String separator = ",";
        boolean havePrintedAnElement = false;
        buf.append('{');
        for (int i = 0; i < (bits.length << LOG_BITS); i++) {
            if (member(i)) {
                if (i > 0 && havePrintedAnElement) {
                    buf.append(separator);
                }
                if (tokenNames != null) {
                    buf.append(tokenNames[i]);
                } else {
                    buf.append(i);
                }
                havePrintedAnElement = true;
            }
        }
        buf.append('}');
        return buf.toString();
    }
}
