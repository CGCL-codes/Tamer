package org.apache.axis.encoding.ser;

import org.apache.axis.utils.JavaUtils;
import javax.xml.namespace.QName;
import javax.xml.rpc.JAXRPCException;

public class SimpleSerializerFactory extends BaseSerializerFactory {

    private boolean isBasicType = false;

    /**
     * Note that the factory is constructed with the QName and xmlType.  This is important
     * to allow distinction between primitive values and java.lang wrappers.
     **/
    public SimpleSerializerFactory(Class javaType, QName xmlType) {
        super(SimpleSerializer.class, xmlType, javaType, javaType);
        this.isBasicType = JavaUtils.isBasic(javaType);
    }

    public javax.xml.rpc.encoding.Serializer getSerializerAs(String mechanismType) throws JAXRPCException {
        if (this.isBasicType) {
            return new SimpleSerializer(javaClazz, xmlType);
        } else {
            return super.getSerializerAs(mechanismType);
        }
    }
}
