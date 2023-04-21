package org.tanso.fountain.util;

import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

/**
 * Storage class for init-params of a component specified in the node agent's
 * config file
 * 
 * @author Haiping Huang
 */
public class Parameters {

    private Map<String, String> params = new Hashtable<String, String>(3);

    /**
	 * Add a new parameter
	 * 
	 * @param paramName
	 *            The parameter's name
	 * @param paramStr
	 *            The parameter's value in string
	 * @return True: if the param doesn't exist False: if the param has already
	 *         existed. NOTE: the old param's (with the same param name) value
	 *         will be overwritten
	 */
    public boolean add(String paramName, String paramStr) {
        if (null == paramStr) {
            return false;
        }
        return (null == params.put(paramName, paramStr)) ? true : false;
    }

    /**
	 * Get the number of params
	 * 
	 * @return The number of the params
	 */
    public int getSize() {
        return params.size();
    }

    /**
	 * Get the param value with a specified param name
	 * 
	 * @param paramName
	 *            The param name
	 * @return The param value in string. null if param doesn't exist
	 */
    public String getValue(String paramName) {
        return getValue(paramName, null);
    }

    /**
	 * Get the param value with a specified param name. If the param doesn't
	 * exist, default value will be returned.
	 * 
	 * @param paramName
	 *            The param name
	 * @param defaultValue
	 *            The default value
	 * @return The param value in string. Default value if param doesn't exist
	 */
    public String getValue(String paramName, String defaultValue) {
        String value = params.get(paramName);
        return (null == value) ? defaultValue : value;
    }

    /**
	 * Get the set of the params' names
	 * 
	 * @return The set of params' names
	 */
    public Set<String> getNameSet() {
        return params.keySet();
    }

    @Override
    public String toString() {
        return params.toString();
    }
}
