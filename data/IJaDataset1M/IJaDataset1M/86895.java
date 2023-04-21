package de.berlin.fu.inf.gameai.utils;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Random;

public final class RandomIterator implements Iterable<Integer>, Serializable {

    private static final long serialVersionUID = 97456017321634168L;

    private static final Random DEFAULT_RND = CommonObjectFactory.createRandom();

    private final int size;

    private final int index[];

    private int offset;

    private final int valid[];

    private int nu;

    public RandomIterator(final int size) {
        this.size = size;
        this.index = new int[size];
        this.valid = new int[size];
        this.nu = 1;
        this.offset = size;
    }

    public void reset() {
        offset = size;
        nu++;
    }

    public boolean hasNext() {
        return offset > 0;
    }

    public int nextInt(final Random rnd) {
        final int n = rnd.nextInt(offset);
        int r;
        if (valid[n] == nu) {
            r = index[n];
        } else {
            r = n;
        }
        offset -= 1;
        valid[n] = nu;
        if (valid[offset] == nu) {
            index[n] = index[offset];
        } else {
            index[n] = offset;
        }
        return r;
    }

    public Iterator<Integer> iterator() {
        return new Iterator<Integer>() {

            public boolean hasNext() {
                return RandomIterator.this.hasNext();
            }

            public Integer next() {
                return nextInt(DEFAULT_RND);
            }

            public void remove() {
                next();
            }
        };
    }
}
