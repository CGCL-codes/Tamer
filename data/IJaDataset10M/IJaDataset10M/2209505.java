package org.springframework.beans.factory;

/**
 * Exception thrown when a bean instance has been requested for a bean
 * which has been defined as abstract
 * @author Juergen Hoeller
 * @since 1.1
 * @see org.springframework.beans.factory.support.AbstractBeanDefinition#setAbstract
 */
public class BeanIsAbstractException extends BeanCreationException {

    /**
	 * Create a new BeanIsAbstractException.
	 * @param beanName the name of the bean requested
	 */
    public BeanIsAbstractException(String beanName) {
        super(beanName, "Bean definition is abstract");
    }
}
