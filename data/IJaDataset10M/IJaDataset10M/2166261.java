package com.blackberry.facebook;

import java.util.Enumeration;
import java.util.Hashtable;

public class Parameters {

    /**
	 * Stores parameters.
	 */
    private Hashtable parameters = new Hashtable();

    /**
	 * Default constructor.
	 * 
	 */
    public Parameters() {
    }

    /**
	 * Add a parameter to the collection.
	 * 
	 * @param name
	 *            the parameter's name.
	 * @param value
	 *            the parameter's value.
	 * @return this collection instance.
	 */
    public Parameters add(String name, String value) {
        parameters.put(name, new Parameter(name, value));
        return this;
    }

    /**
	 * Add a parameter instance to the collection.
	 * 
	 * @param parameter
	 *            the parameter's instance.
	 * @return this collection instance.
	 */
    public Parameters add(Parameter parameter) {
        parameters.put(parameter.getName(), parameter);
        return this;
    }

    /**
	 * Obtain a parameter from the collection.
	 * 
	 * @param name
	 *            the parameter's name.
	 * @return an instance of parameter.
	 */
    public Parameter get(String name) {
        try {
            return (Parameter) parameters.get(name);
        } catch (Exception e) {
        }
        return null;
    }

    /**
	 * Obtain the number of parameters in the collection.
	 * 
	 * @return the count.
	 */
    public int getCount() {
        return parameters.size();
    }

    /**
	 * Obtain the enumeration of parameter names.
	 * 
	 * @return the enumeration.
	 */
    public Enumeration getParameterNames() {
        return parameters.keys();
    }

    /**
	 * Clear the collection.
	 * 
	 */
    public void clear() {
        parameters.clear();
    }
}
