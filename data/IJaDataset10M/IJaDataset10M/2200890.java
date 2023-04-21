package net.sourceforge.javabits.beans;

/**
 * @author Jochen Kuhnle
 */
public class AmbiguosPropertyException extends RuntimeException {

    private final BeanDescriptor<?> beanDescriptor;

    private final String name;

    public AmbiguosPropertyException(final BeanDescriptor<?> beanDescriptor, final String name) {
        this.beanDescriptor = beanDescriptor;
        this.name = name;
    }

    public BeanDescriptor<?> getBeanDescriptor() {
        return this.beanDescriptor;
    }

    public String getName() {
        return this.name;
    }
}
