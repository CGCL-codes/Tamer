package com.entelience.soap;

import org.apache.axis.encoding.ser.SimpleDeserializer;
import javax.xml.namespace.QName;
import org.apache.log4j.Logger;
import com.entelience.util.Logs;

/**
 * Deserializer for a jdk5 enum.
 *
 */
public class EnumSimpleDeserializer extends SimpleDeserializer {

    private static final long serialVersionUID = 1L;

    public EnumSimpleDeserializer(Class javaType, QName xmlType) {
        super(javaType, xmlType);
    }

    @SuppressWarnings("unchecked")
    public Object makeValue(String source) throws Exception {
        Logger _logger = Logs.getLogger();
        if (isNil) return null;
        if (source == null || source.length() == 0) source = "UNDEFINED";
        return Enum.valueOf(javaType, source);
    }
}
