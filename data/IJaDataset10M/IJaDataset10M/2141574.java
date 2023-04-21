package org.redpin.android.json;

import java.lang.reflect.Type;
import org.redpin.android.core.Location;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/**
 * adapter for specific org.redpin.base.core.* type (it is needed to get always
 * a org.repin.server.standalone.core.* instance after deserialization
 * 
 * @see JsonSerializer
 * @see JsonDeserializer
 * @author Pascal Brogle (broglep@student.ethz.ch)
 * 
 */
public class BaseLocationTypeAdapter implements JsonSerializer<org.redpin.base.core.Location>, JsonDeserializer<org.redpin.base.core.Location> {

    /**
	 * @see JsonSerializer#serialize(Object, Type, JsonSerializationContext)
	 */
    public JsonElement serialize(org.redpin.base.core.Location src, Type typeOfSrc, JsonSerializationContext context) {
        return context.serialize(src, Location.class);
    }

    /**
	 * @see JsonDeserializer#deserialize(JsonElement, Type,
	 *      JsonDeserializationContext)
	 */
    public Location deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return context.deserialize(json, Location.class);
    }
}
