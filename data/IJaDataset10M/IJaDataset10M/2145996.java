package org.jgentleframework.core.factory.support;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * The Interface CachedConstructor.
 * 
 * @author Quoc Chung - mailto: <a
 *         href="mailto:skydunkpro@yahoo.com">skydunkpro@yahoo.com</a>
 * @date Dec 17, 2008
 */
public interface CachedConstructor {

    /**
	 * Constructs an instance for the given arguments.
	 * 
	 * @param arguments
	 *            the arguments
	 * @throws InvocationTargetException
	 *             the invocation target exception
	 */
    Object newInstance(Object... arguments) throws InvocationTargetException;

    /**
	 * Gets the meta def object.
	 */
    MetaDefObject getMetaDefObject();

    /**
	 * Sets the meta def object.
	 * 
	 * @param mdo
	 */
    void setMetaDefObject(MetaDefObject mdo);

    /**
	 * Gets the java constructor.
	 * 
	 * @return the java constructor
	 */
    Constructor<?> getJavaConstructor();

    /**
	 * Returns the hashcodeID of this {@link CachedConstructor}
	 * 
	 * @return int
	 */
    int hashcodeID();
}
