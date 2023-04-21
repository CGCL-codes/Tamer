package maze.commons.generic.impl;

import maze.commons.generic.Pair;

/**
 * @author Normunds Mazurs (MAZE)
 * 
 */
public class PairImpl<F, S> implements Pair<F, S> {

    protected final F first;

    protected final S second;

    private final int buildedHashCode;

    protected int buildHashCode(final Object obj, final int hashCode) {
        return 31 * hashCode + (obj == null ? 0 : obj.hashCode());
    }

    protected int buildHashCode(final Object obj) {
        return buildHashCode(obj, 1);
    }

    public PairImpl(final F first, final S second) {
        this.first = first;
        this.second = second;
        buildedHashCode = buildHashCode(first, buildHashCode(second));
    }

    @Override
    public F getFirst() {
        return first;
    }

    @Override
    public S getSecond() {
        return second;
    }

    @Override
    public int hashCode() {
        return buildedHashCode;
    }

    protected boolean checkEquality(Object object1, Object object2) {
        if (object1 == object2) {
            return true;
        }
        if ((object1 == null) || (object2 == null)) {
            return false;
        }
        return object1.equals(object2);
    }

    @Override
    public boolean equals(final Object another) {
        if (another instanceof Pair) {
            final Pair<?, ?> anotherPair = (Pair<?, ?>) another;
            return checkEquality(anotherPair.getFirst(), getFirst()) && checkEquality(anotherPair.getSecond(), getSecond());
        }
        return false;
    }
}
