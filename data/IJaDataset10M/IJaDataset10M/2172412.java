package hu.jmemoryeditorw.jna;

import com.sun.jna.Structure;

/**
 * A RECT structure.
 * @author karnok, 2011.03.06.
 */
public class Rect extends Structure {

    /** The left coordinate. */
    public int left;

    /** The top coordinate. */
    public int top;

    /** The right coordinate. */
    public int right;

    /** The bottom coordinate. */
    public int bottom;
}
