package org.variance.version.diff;

public interface ThreeWayDifference<T> extends TwoWayDifference<T> {

    /**
	 * Get the common ancestor between the source and the current.
	 */
    T getCommonAncestor();
}
