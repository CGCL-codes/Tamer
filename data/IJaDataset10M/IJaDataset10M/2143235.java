package net.sf.doolin.util;

import com.google.common.base.Function;

/**
 * This class performs a conversion by extracting a property from the source?
 * 
 * @author Damien Coraboeuf
 * 
 * @param <S>
 *            Type for the source
 * @param <T>
 *            Expected type for the target
 * @see Utils#getProperty(Object, String)
 */
public class PropertyFunction<S, T> implements Function<S, T> {

    private final String propertyName;

    /**
	 * Constructor.
	 * 
	 * @param propertyName
	 *            Name of the property to take from the source.
	 */
    public PropertyFunction(String propertyName) {
        this.propertyName = propertyName;
    }

    @Override
    public T apply(S source) {
        @SuppressWarnings("unchecked") T k = (T) Utils.getProperty(source, this.propertyName);
        return k;
    }
}
