package com.curl.orb.type;

import com.curl.io.serialize.SerializeException;

/**
 * Proxy class of java.math.BigDecimal to serializer and deserialize.
 * 
 * @author Ucai Zhang
 * @since 0.6
 */
public class BigDecimal extends AbstractSerializableProxyType {

    private static final long serialVersionUID = 1L;

    /**
	 * A string to contain the the BigDecimal's value
	 */
    private String strValue = "";

    @Override
    public Object extractProperObject() throws SerializeException {
        return new java.math.BigDecimal(strValue);
    }

    @Override
    public void injectProperObject(Object obj) throws SerializeException {
        java.math.BigDecimal bigDecimal = (java.math.BigDecimal) obj;
        strValue = bigDecimal.toPlainString();
    }
}
