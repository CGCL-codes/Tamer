package com.pureperfect.purview;

import java.lang.reflect.Method;

/**
 * Interface for filtering methods for inclusion/exclusion.
 * 
 * @author J. Chris Folsom
 * @version 1.0
 * @since 1.0
 */
public interface MethodFilter {

    /**
	 * Whether or not the method should be included.
	 * 
	 * @param method
	 *            the method to test.
	 * @return true if the method should be included false otherwise.
	 */
    public boolean include(Method method);
}
