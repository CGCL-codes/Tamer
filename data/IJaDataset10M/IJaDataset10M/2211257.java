package com.leemba.monitor.json;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.leemba.monitor.server.objects.state.rules.Action;
import java.lang.reflect.Type;

/**
 *
 * @author mrjohnson
 */
public class ActionDeserializer implements JsonDeserializer<Action> {

    @Override
    public Action deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
        if (!json.isJsonObject()) return null;
        JsonElement jname = json.getAsJsonObject().get("className");
        if (jname == null) return null;
        String name = jname.getAsString();
        Class<? extends Action> clazz = Action.getAction(name);
        if (clazz == null) return null;
        return context.deserialize(json, clazz);
    }
}
