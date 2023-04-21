package net.sf.crispy.impl.jaxrpc.serializer;

import javax.xml.namespace.QName;
import org.apache.axis.encoding.ser.SimpleDeserializer;

public class CharacterDeserializer extends SimpleDeserializer {

    private static final long serialVersionUID = -6356586901498317988L;

    public CharacterDeserializer(Class pvClass, QName pvQName) {
        super(pvClass, pvQName);
    }

    public Object makeValue(String pvSource) throws Exception {
        return (pvSource == null ? null : new Character(pvSource.charAt(0)));
    }
}
