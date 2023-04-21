package org.objectstyle.cayenne.remote.hessian;

import java.util.HashMap;
import com.caucho.hessian.io.AbstractSerializerFactory;
import com.caucho.hessian.io.Deserializer;
import com.caucho.hessian.io.HessianProtocolException;
import com.caucho.hessian.io.Serializer;

/**
 * A Hessian SerializerFactory extension that supports serializing Enums.
 * <p>
 * <i>Requires Java 1.5 or newer</i>
 * </p>
 * 
 * @since 1.2
 * @author Andrus Adamchik
 */
class EnumSerializerFactory extends AbstractSerializerFactory {

    private final EnumSerializer enumSerializer = new EnumSerializer();

    private HashMap<Class, Deserializer> cachedDeserializerMap;

    public Serializer getSerializer(Class cl) throws HessianProtocolException {
        return (cl.isEnum()) ? enumSerializer : null;
    }

    public Deserializer getDeserializer(Class cl) throws HessianProtocolException {
        if (cl.isEnum()) {
            Deserializer deserializer = null;
            synchronized (this) {
                if (cachedDeserializerMap != null) {
                    deserializer = cachedDeserializerMap.get(cl);
                }
                if (deserializer == null) {
                    deserializer = new EnumDeserializer(cl);
                    if (cachedDeserializerMap == null) {
                        cachedDeserializerMap = new HashMap<Class, Deserializer>();
                    }
                    cachedDeserializerMap.put(cl, deserializer);
                }
            }
            return deserializer;
        }
        return null;
    }
}
