package org.raken.messaging.config;

import java.lang.reflect.Type;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

public class ServiceEntryDeserializer implements JsonDeserializer<Class<?>> {

    public Class<?> deserialize(JsonElement arg0, Type arg1, JsonDeserializationContext arg2) throws JsonParseException {
        try {
            StringBuffer sb = new StringBuffer(arg0.toString());
            sb.deleteCharAt(0);
            sb.deleteCharAt(sb.length() - 1);
            return Class.forName(sb.toString());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
