package org.enerj.jga.fn.comparison;

import org.enerj.jga.fn.BinaryPredicate;

/**
 * Marker interface for those predicates that provide some sort of a test for
 * equality.
 *
 * @author <a href="mailto:davidahall@users.sf.net">David A. Hall</a>
 */
public abstract class Equality<T> extends BinaryPredicate<T, T> {
}
