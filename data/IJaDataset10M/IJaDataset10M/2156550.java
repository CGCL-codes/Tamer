package org.apache.axis.encoding.ser.xbeans;

import org.apache.axis.encoding.ser.BaseSerializerFactory;
import javax.xml.namespace.QName;

/**
 * Class XmlBeanSerializerFactory
 * @author Jonathan Colwell
 */
public class XmlBeanSerializerFactory extends BaseSerializerFactory {

    public XmlBeanSerializerFactory(Class javaType, QName xmlType) {
        super(XmlBeanSerializer.class, xmlType, javaType, javaType);
    }
}
