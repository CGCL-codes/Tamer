package Specification.Galaxy;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.TreeMap;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.reflect.TypeToken;
import FileOps.GSON.GSONHandler;
import FileOps.GSON.GSONWrapper;
import FileOps.XStream.AdvancedXStreamHandler;
import Galaxy.Tree.Workflow.ToolState;
import Galaxy.Tree.Workflow.ToolState.Primitive;
import Galaxy.Tree.Workflow.ToolState.PrimitiveOrMap;

/**
 * This handler correctly handles the tool_state string inside
 * the step dictionary in a galaxy workflow. Recall the tool_state string
 * is a series of nested, escaped dictionaries. This handler parses it into
 * a complex object, so fields in the tool_state string can be easily accessed
 * for conversion.
 * @author viper
 *
 */
public class StateHandler extends GSONHandler {

    GSONWrapper gsonMap;

    GSONWrapper gsonStringMap;

    public static class PrimitiveHandler extends GSONHandler {

        @Override
        public JsonElement serialize(Object arg0, Type arg1, JsonSerializationContext arg2) {
            return arg2.serialize(arg0);
        }

        @Override
        public Object deserialize(JsonElement arg0, Type arg1, JsonDeserializationContext arg2) throws JsonParseException {
            if (arg0.isJsonArray()) {
                JsonArray ar = arg0.getAsJsonArray();
                Queue<Primitive> primitives = new LinkedList<Primitive>();
                for (JsonElement e : ar) {
                    Primitive p = (Primitive) deserialize(e, arg1, arg2);
                    primitives.add(p);
                }
                return new Primitive(primitives);
            } else if (arg0.isJsonPrimitive()) {
                JsonPrimitive jp = arg0.getAsJsonPrimitive();
                if (jp.isBoolean()) return new Primitive(arg0.getAsBoolean());
                if (jp.isNumber()) {
                    Number jn = jp.getAsNumber();
                    return new Primitive(jn.intValue());
                }
                if (jp.isString()) return new Primitive(jp.getAsString());
            }
            return new Primitive();
        }
    }

    public StateHandler() {
        Type t = new TypeToken<HashMap<String, Primitive>>() {
        }.getType();
        gsonMap = new GSONWrapper<Map<String, Primitive>>(t);
        gsonMap.bindHandler(Primitive.class, new PrimitiveHandler());
        gsonStringMap = new GSONWrapper<PrimitiveOrMap>(PrimitiveOrMap.class);
    }

    @Override
    public JsonElement serialize(Object arg0, Type arg1, JsonSerializationContext arg2) {
        return arg2.serialize(arg0.toString());
    }

    public PrimitiveOrMap process(Primitive s) {
        if (s == null) return null;
        if (s.isString() == false) return new PrimitiveOrMap(s);
        try {
            Map<String, Primitive> prelimMap;
            prelimMap = (Map<String, Primitive>) gsonMap.parse(s.getString());
            PrimitiveOrMap sm = new PrimitiveOrMap(new HashMap<String, PrimitiveOrMap>());
            if (prelimMap == null) throw new JsonParseException("Null prelimMap");
            for (String key : prelimMap.keySet()) {
                Primitive val = prelimMap.get(key);
                PrimitiveOrMap valSM;
                valSM = process(val);
                sm.getSecond().put(key, valSM);
            }
            return sm;
        } catch (JsonParseException e) {
            return new PrimitiveOrMap(s);
        }
    }

    @Override
    public Object deserialize(JsonElement arg0, Type arg1, JsonDeserializationContext arg2) throws JsonParseException {
        String parseString = arg0.getAsString();
        HashMap<String, String> map;
        ToolState myToolState = new ToolState();
        PrimitiveOrMap sm = process(new Primitive(parseString));
        if (sm.isFirst()) myToolState.getToolState().setFirst(sm.getFirst()); else myToolState.getToolState().setSecond(sm.getSecond());
        return myToolState;
    }
}
