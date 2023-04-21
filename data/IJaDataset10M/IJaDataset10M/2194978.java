package net.sf.doolin.util;

import com.google.common.base.Function;

/**
 * Converts an object to another.
 * 
 * @author Damien Coraboeuf
 * @param <S>
 *            Source type
 * @param <T>
 *            Target type
 * @deprecated Use {@link Function} instead.
 */
@Deprecated
public interface ItemConverter<S, T> {

    /**
	 * Convert a source to a target
	 * 
	 * @param source
	 *            Source object
	 * @return Target object
	 */
    T convert(S source);
}
