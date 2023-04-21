package org.nexopenframework.deployment.enhacer.addition;

/**
 * 
 * <p>Nextret Service Architecture</p>
 * 
 * <p>Base Interface for enhacement phase which allows 
 *    adding extra interceptors to a given service</p>
 *
 * @author <a href="mailto:fme@nextret.net">Francesc Xavier Magdaleno</a>
 * @version 1.0
 * @since 1.0
 */
public interface InterceptorAddition {

    /**
	 * <p>Extra interceptors to be added to a service</p>
	 * @return
	 */
    Class[] getInterceptors();
}
