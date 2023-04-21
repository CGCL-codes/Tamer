package com.jtri.base;

import java.util.Map;

/**
 * @author atorres
 *
 * IMapped classes implements a method that returns a converter map that
 * explains how to convert each input field to bussiness objects properties.
 */
public interface IMapped {

    /**
	 * Return a map of property->FieldMapping. Each fieldMapping will 
	 * explain how to load and (if it's the case) how to save back de object
	 * @return
	 */
    public Map getConverter();

    /**
	 * convert a property to the the business object object
	 * @param prop
	 * @return
	 * @throws Exception
	 */
    public Object convertProperty(String prop) throws Exception;
}
