package org.powermock.tests.utils;

import java.lang.reflect.AnnotatedElement;

public interface IgnorePackagesExtractor {

    /**
	 * @return Returns a string-array of all package names if annotation was
	 *         found.
	 */
    String[] getPackagesToIgnore(AnnotatedElement element);
}
