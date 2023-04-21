package org.fb4j.client.parser.json;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.fb4j.client.parser.ParserException;
import org.fb4j.model.Element;
import com.google.gson.FieldNamingStrategy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

@SuppressWarnings("unchecked")
public class JsonParser<T> {

    private Type returnType;

    private GsonBuilder gsonBuilder;

    public JsonParser(Type returnType) {
        this.returnType = returnType;
        gsonBuilder = new GsonBuilder();
        gsonBuilder.setFieldNamingStrategy(new CustomAnnotationFieldNamingStrategy());
        registerTypeAdapter(Long.class, new LongDeserializer());
        registerTypeAdapter(Date.class, new DateDeserializer());
        registerTypeAdapter(Locale.class, new LocaleDeserializer());
        registerTypeAdapter(List.class, new ListDeserializer());
        registerTypeAdapter(Map.class, new MapDeserializer());
    }

    protected GsonBuilder registerTypeAdapter(Type type, Object adapter) {
        return gsonBuilder.registerTypeAdapter(type, adapter);
    }

    public T parse(String data) throws ParserException {
        try {
            Gson gson = gsonBuilder.create();
            return (T) gson.fromJson(data, returnType);
        } catch (JsonParseException e) {
            throw new ParserException(e);
        }
    }

    private static class DateDeserializer implements JsonDeserializer<Date> {

        public Date deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
            long phpDate = jsonElement.getAsLong();
            return phpDate == 0 ? null : new Date((long) phpDate * 1000);
        }
    }

    private static class LocaleDeserializer implements JsonDeserializer<Locale> {

        public Locale deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
            return new Locale(jsonElement.getAsString());
        }
    }

    private static class LongDeserializer implements JsonDeserializer<Long> {

        public Long deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return json.getAsString().length() == 0 ? null : json.getAsLong();
        }
    }

    private static class ListDeserializer implements JsonDeserializer<List> {

        public List deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            if (json.isJsonNull()) {
                return null;
            } else if (json.isJsonObject() && json.getAsJsonObject().entrySet().isEmpty()) {
                return new ArrayList();
            }
            JsonArray jsonArray = json.getAsJsonArray();
            List list = new ArrayList(jsonArray.size());
            Type childType = ((ParameterizedType) typeOfT).getActualTypeArguments()[0];
            for (JsonElement childElement : jsonArray) {
                if (childElement == null || childElement.isJsonNull()) {
                    list.add(null);
                } else {
                    Object value = context.deserialize(childElement, childType);
                    list.add(value);
                }
            }
            return list;
        }
    }

    private static class CustomAnnotationFieldNamingStrategy implements FieldNamingStrategy {

        public String translateName(Field field) {
            Element ann = field.getAnnotation(Element.class);
            return ann == null ? null : ann.value();
        }
    }

    private static class MapDeserializer implements JsonDeserializer<Map<String, String>> {

        public Map<String, String> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonArray jsonArray = json.getAsJsonArray();
            Map<String, String> map = new HashMap<String, String>(jsonArray.size());
            for (JsonElement element : jsonArray) {
                JsonObject pair = element.getAsJsonObject();
                String key = pair.get("key").getAsString();
                String value = pair.get("value").getAsString();
                map.put(key, value);
            }
            return map;
        }
    }
}
