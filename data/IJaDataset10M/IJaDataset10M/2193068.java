package org.apache.axis.encoding.ser;

import javax.xml.namespace.QName;

/**
 * DeserializerFactory for Enumeration.
 *
 * @author Rich Scheuerle <scheu@us.ibm.com>
 */
public class EnumDeserializerFactory extends BaseDeserializerFactory {

    public EnumDeserializerFactory(Class javaType, QName xmlType) {
        super(EnumDeserializer.class, xmlType, javaType);
    }
}
