package uchicago.src.sim.util;

/**
 * Mutable wrapper for an int primitive. Used by
 * ProbeableNumber as the probed object when probing
 * spaces that contain primitives.
 *
 * @author Nick Collier
 * @version $Revision: 1.3 $ $Date: 2004/11/03 19:51:06 $
 */
public class IntWrapper extends DoubleWrapper {

    /**
   * Creates an IntWrapper that wraps the specified int.
   *
   * @param val the int to wrap
   */
    public IntWrapper(int val) {
        super((double) val);
    }

    /**
   * Gets the wrapped value as a Number.
   */
    public Number getWrappedNumber() {
        return new Integer((int) val);
    }
}
