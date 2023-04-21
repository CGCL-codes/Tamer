package net.sf.sasl.aop.common.grammar.placeholder.resolver;

import java.lang.reflect.Method;

/**
 * Interfaces defines methods to retrieve (currently only) the parameter names
 * of a method.
 * 
 * @author Philipp Förmer
 * @since 0.0.1
 */
public interface IParameterNameLoader {

    /**
	 * Tries to load the parameter names of the method. Returns a string array
	 * of the parameter names on success. If the parameter names could not get
	 * loaded than null is returned.
	 * 
	 * @param method
	 *            non null
	 * @return null or non null array
	 * 
	 * @throws if method is null.
	 * @since 0.0.1
	 */
    public String[] loadParameterTable(Method method) throws IllegalArgumentException;
}
