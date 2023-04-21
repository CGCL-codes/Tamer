package org.apache.axis.encoding.ser;

import javax.xml.namespace.QName;

/**
 * A MapDeserializer Factory
 *
 *  @author Rich Scheuerle (scheu@us.ibm.com)
 */
public class MapDeserializerFactory extends BaseDeserializerFactory {

    public MapDeserializerFactory(Class javaType, QName xmlType) {
        super(MapDeserializer.class, xmlType, javaType);
    }
}
