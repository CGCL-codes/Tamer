package se.kth.cid.util;

import java.util.*;

/** Implements a SizedEnumeration on a vector.
 *
 *  @author Mikael Nilsson
 *  @version $Revision: 155 $
 */
public class SizedVectorEnumeration implements SizedEnumeration {

    /** The vector to use.
   */
    Vector v;

    /** The current index.
   */
    int index;

    /** The last element retrieved.
   */
    Object lastEl;

    /** Constructs an enumeration from the given vector.
   *
   *  @param nv the Vector to use.
   */
    public SizedVectorEnumeration(Vector nv) {
        v = nv;
        index = -1;
    }

    public int getSize() {
        return v.size();
    }

    public Object nextElement() {
        try {
            lastEl = v.elementAt(index + 1);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new NoSuchElementException();
        }
        index++;
        return lastEl;
    }

    public Object lastElement() {
        if (index == -1) throw new NoSuchElementException();
        return lastEl;
    }

    public boolean hasMoreElements() {
        return v.size() > 0 && index + 1 < v.size();
    }
}
