package org.nexopenframework.engine.support;

import java.lang.reflect.Method;

/**
 * <p>NexOpen Framework</p>
 * 
 * <p>Contract for dealing with cases that it hass been aborted a caugth exception and a default value
 * is would like to be returned</p>
 * 
 * @author Francesc Xavier Magdaleno
 * @version 1.0
 * @since 1.0
 */
public interface ReturnValueLocator {

    Object locateReturnValue(final Method m);
}
