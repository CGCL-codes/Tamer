package com.google.gwt.user.client.rpc.core.java.lang;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;

/**
 * Custom field serializer for {@link java.lang.String}.
 */
public class String_CustomFieldSerializer {

    public static void deserialize(SerializationStreamReader streamReader, String instance) {
    }

    public static String instantiate(SerializationStreamReader streamReader) throws SerializationException {
        return streamReader.readString();
    }

    public static void serialize(SerializationStreamWriter streamWriter, String instance) throws SerializationException {
        streamWriter.writeString(instance);
    }
}
