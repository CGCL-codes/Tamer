package org.codehaus.groovy.grails.commons.metaclass;

/**
 * <p>Abstract class that provides default implementation for isArgumentsMatch
 * 
 * @author Graeme Rocher
 * @since 0.2
 * 
 * Date Created: 9th June 2006
 */
public abstract class AbstractDynamicConstructor implements DynamicConstructor {

    private Class[] argumentTypes;

    /**
	 * Takes an array of types required to match this constructor
	 * 
	 * @param argumentTypes The argument types
	 */
    public AbstractDynamicConstructor(Class[] argumentTypes) {
        this.argumentTypes = argumentTypes;
    }

    /**
	 * @return True if the arguments types match those specified in the constructor
	 */
    public boolean isArgumentsMatch(Object[] args) {
        if (args.length != argumentTypes.length) return false; else {
            for (int i = 0; i < args.length; i++) {
                if (!args[i].getClass().equals(argumentTypes[i])) return false;
            }
        }
        return true;
    }

    public abstract Object invoke(Class clazz, Object[] args);
}
