package org.coos.messaging.serializer;

import org.coos.util.serialize.AFClassLoader;
import org.coos.util.serialize.ObjectHelper;
import java.io.DataInputStream;
import java.io.ByteArrayInputStream;
import org.coos.messaging.Serializer;

/**
 * @author Knut Eilif Husa, Tellu AS Serialization by java object serialization
 */
public class ObjectSerializer implements Serializer {

    public byte[] serialize(Object object) throws Exception {
        return ObjectHelper.persist(object);
    }

    public Object deserialize(byte[] bytes) throws Exception {
        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(bytes));
        return ObjectHelper.resurrect(dis, null);
    }

    public Object deserialize(byte[] bytes, AFClassLoader cl) throws Exception {
        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(bytes));
        return ObjectHelper.resurrect(dis, cl);
    }
}
