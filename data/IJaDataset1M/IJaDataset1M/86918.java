package br.gov.framework.demoiselle.util.layer.integration;

import br.gov.framework.demoiselle.core.layer.integration.InjectionContext;

/**
 * Superclass intended to be extended by factories in general.
 * 
 * @author CETEC/CTJEE
 */
public class GenericFactory {

    protected GenericFactory() {
    }

    /**
	 * Instanciates an object for the specified class using its default constructor method.
	 * 
	 * @param <T>	the resulting object
	 * @param clazz	the class to create
	 * @return	the created object
	 */
    @SuppressWarnings("unchecked")
    protected <T> T createWithLazyCreateProxy(Class<T> clazz) {
        return (T) LazyCreateProxy.newInstance(clazz);
    }

    /**
	 * Instanciates an object for the specified class using an alternative constructor method using the given parameter types and values.
	 * 
	 * @param <T>	the resulting object
	 * @param clazz	the class to create
	 * @param parameterTypes	the types on the constructor
	 * @param initargs			the argument values for the constructor
	 * @return	the created object
	 */
    @SuppressWarnings("unchecked")
    protected <T> T createWithLazyCreateProxy(Class<T> clazz, Class<?>[] parameterTypes, Object[] initargs) {
        return (T) LazyCreateProxy.newInstance(clazz, parameterTypes, initargs);
    }

    /**
	 * Retrieves for the given injection context the replacement rules by convention.
	 * 
	 * @param ctx		the injection context
	 * @param find		the strings to search for
	 * @param replace	the strings to replace to
	 * @return	a String
	 */
    protected String conventionForClassName(InjectionContext ctx, String[] find, String[] replace) {
        String className = null;
        if (ctx.getInjection() != null && ctx.getInjection().name() != null && !ctx.getInjection().name().trim().equals("")) {
            className = ctx.getInjection().name();
        } else {
            if ((find.length != replace.length) || (find.length == 0)) {
                throw new LayerIntegrationException("Error: 'find' has " + find.length + " and 'replace' has " + replace.length + ", which are not compatible.");
            } else {
                className = ctx.getFieldType().getName();
                for (int i = 0; i < find.length; i++) {
                    className = className.replaceFirst(find[i], replace[i]);
                }
            }
        }
        return className;
    }
}
