package net.sourceforge.ondex.core.util;

import it.unimi.dsi.fastutil.ints.IntIterator;
import java.util.BitSet;

/**
 * @author hindlem
 */
public class DefaultBitSet extends BitSet implements ONDEXBitSet, Cloneable {

    private static final long serialVersionUID = 1L;

    /**
     * Creates a bit set whose initial size is large enough to explicitly
     * represent bits with indices in the range <code>0</code> through
     * <code>nbits-1</code>. All bits are initially <code>false</code>.
     *
     * @param i the initial size of the bit set.
     * @throws NegativeArraySizeException if the specified initial size
     *                                    is negative.
     */
    public DefaultBitSet(int i) {
        super(i);
    }

    /**
     * Creates a new bit set. All bits are initially <code>false</code>.
     */
    public DefaultBitSet() {
        super();
    }

    /**
     * creates a new bit set with identical bits set to the given bitset.
     */
    public DefaultBitSet(ONDEXBitSet bs) {
        this.or(bs);
    }

    @Override
    public IntIterator iterator() {
        return new IntIterator() {

            private int currentIndex = -1;

            private int nextIndex = nextSetBit(0);

            @Override
            public boolean hasNext() {
                return nextIndex > -1;
            }

            @Override
            public Integer next() {
                return nextInt();
            }

            @Override
            public void remove() {
                if (currentIndex > -1) clear(currentIndex); else throw new IllegalStateException();
            }

            @Override
            public int nextInt() {
                currentIndex = nextIndex;
                nextIndex = nextSetBit(currentIndex + 1);
                return currentIndex;
            }

            @Override
            public int skip(int arg0) {
                int i = 0;
                while (hasNext()) {
                    nextInt();
                    i++;
                    if (i > arg0) break;
                }
                return i;
            }
        };
    }

    @Override
    public ONDEXBitSet and(ONDEXBitSet set) {
        if (set instanceof DefaultBitSet) {
            and((BitSet) set);
        } else {
            BitSet bs = new BitSet(set.length());
            IntIterator ints = set.iterator();
            while (ints.hasNext()) {
                int i = ints.nextInt();
                bs.set(i);
            }
            this.and(bs);
        }
        return this;
    }

    @Override
    public ONDEXBitSet andNot(ONDEXBitSet set) {
        if (set instanceof DefaultBitSet) {
            andNot((BitSet) set);
        } else {
            IntIterator ints = set.iterator();
            while (ints.hasNext()) {
                clear(ints.nextInt());
            }
        }
        return this;
    }

    @Override
    public ONDEXBitSet or(ONDEXBitSet set) {
        if (set instanceof DefaultBitSet) {
            or((BitSet) set);
        } else {
            IntIterator ints = set.iterator();
            while (ints.hasNext()) {
                set(ints.nextInt());
            }
        }
        return this;
    }

    @Override
    public ONDEXBitSet xor(ONDEXBitSet set) {
        if (set instanceof DefaultBitSet) {
            xor((BitSet) set);
        } else {
            BitSet exclusiveSet = new BitSet(size() / 2);
            IntIterator ints = set.iterator();
            while (ints.hasNext()) {
                int i = ints.nextInt();
                if (!get(i)) {
                    exclusiveSet.set(i);
                } else {
                    clear(i);
                }
            }
            or(exclusiveSet);
        }
        return this;
    }

    @Override
    public ONDEXBitSet clone() {
        return (ONDEXBitSet) super.clone();
    }

    @Override
    public int countSetBits() {
        int size = 0;
        for (int i = nextSetBit(0); i >= 0; i = nextSetBit(i + 1)) {
            size++;
        }
        return size;
    }
}
