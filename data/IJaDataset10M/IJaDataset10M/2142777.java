package net.sourceforge.signal.runtime.core.context;

/**
 * A proxy object for a bean registered in a context.
 * The notion of a reference is needed to support lazy
 * initialization.
 * 
 * @author Marek
 */
public interface IBeanReference {

    /**
	 * Returns the name of the wrapped bean.
	 * 
	 * @return name of the wrapped bean
	 */
    String getName();

    /**
	 * Returns the wrapped bean.
	 * 
	 * @return wrapped bean
	 * @throws ContextException if an error occurs while
	 *         retrieving the bean
	 */
    Object getBean() throws ContextException;

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
