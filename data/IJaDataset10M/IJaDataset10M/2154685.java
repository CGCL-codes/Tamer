package org.nexopenframework.eh.attributes;

import java.lang.reflect.Method;

/**
 * <p>NexOpen Framework</p>
 *
 * <p>TODO Document me</p>
 *
 *@author Francesc Xavier Magdaleno
 * @since 2.0.0.GA
 * @version $Revision $,$Date: 2009-07-01 20:48:44 +0100 $
 */
public interface ExceptionAttributeSource {

    /**
	 * <p></p>
	 * 
	 * @param m
	 * @param targetClass
	 * @param clazz
	 * @return
	 */
    ExceptionAttribute getExceptionAttribute(final Method m, final Class<?> targetClass, final Class<? extends Throwable> clazz);
}
