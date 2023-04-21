package utils.curves;

import utils.linear.Linear;

/**
 * Defines a curve of an arbitrary type over a time period ranging from 0 to 1.
 * @author Calvin Ashmore
 */
public interface Curve<T extends Linear<T>> {

    T getValue(double t);
}
