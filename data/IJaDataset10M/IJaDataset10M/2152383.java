package com.jaxws.json.serializer;

import java.io.OutputStream;

/**
 * @author Sundaramurthi Saminathan
 * @version 2.0
 * 
 * User may customize the serialization and deserialization of JSON process by implementing  JSONObjectCustomizer inteface.
 */
public interface JSONObjectCustomizer {

    /**
	 * Codec handle class type
	 */
    public Class<? extends Object> getAcceptClass();

    /**
	 * To Json
	 */
    public void encode(OutputStream output, Object object);

    /**
	 * To Json
	 */
    public Object decode(Object value);

    /**
	 * Content type json model
	 */
    public void metaData(StringBuilder buf);
}
