package ircam.jmax.utils;

/**
 * The interface of objects that can be inserted in a TDTree.
 * These objects have two comparison functions, used
 * at alternatives levels of the TDT.
 */
public interface TwoOrderObject {

    public abstract boolean firstLessOrEqual(TwoOrderObject obj);

    public abstract boolean secondLessOrEqual(TwoOrderObject obj);

    public abstract int getFirst();

    public abstract int getSecond();

    public final int FIRST = 0;

    public final int SECOND = 1;
}
