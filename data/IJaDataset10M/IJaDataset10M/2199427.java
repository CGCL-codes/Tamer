package com.aurorasoftworks.signal.runtime.core.context.proxy;

/**
 * A proxy object for a bean registered in a context.
 * The notion of a reference is needed to support lazy
 * initialization.
 * 
 * @author Marek
 */
public interface IBeanDelegate {

    /**
	 * Returns the wrapped bean.
	 * 
	 * @return wrapped bean
	 * @throws ContextException if an error occurs while
	 *         retrieving the bean
	 */
    Object getBean() throws Exception;

    /**
	 * Returns the class of the wrapped bean.
	 * This method is useful for determining a class
	 * of a bean that may not actually be created until
	 * {@link #getBean()} is invoked.
	 *
	 * @return class of the wrapped bean
	 */
    Class getBeanClass();
}
