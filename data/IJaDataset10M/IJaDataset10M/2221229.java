package com.tridion.extensions.dynamicdelivery.foundation.serializers;

import java.io.Serializable;

/**
 * Interface for serializer which is used to deserialize the xml source from
 * tridion into the cwa content model.
 * 
 * @author bjornl
 * 
 */
public interface Serializer {

    /**
	 * Deserialize the input into the specified class
	 * 
	 * @param s
	 *            the object to deserialize
	 * @param c
	 *            the class to deserialize into
	 * @return
	 * @throws Exception
	 */
    public Object deserialize(Object s, Class c) throws Exception;

    /**
	 * Serialize an object 
	 * 
	 * @param o
	 * @return
	 * @throws Exception
	 */
    public Serializable serialize(Object o) throws Exception;
}
