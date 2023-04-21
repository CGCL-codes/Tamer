package com.friendfeed.api.internal.gson;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import java.util.Date;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

class CustomDateTypeAdapter implements JsonDeserializer<java.util.Date> {

    private final DateFormat format;

    public CustomDateTypeAdapter(String datePattern) {
        this.format = new SimpleDateFormat(datePattern);
    }

    public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (!(json instanceof JsonPrimitive)) {
            throw new JsonParseException("The date should be a string value");
        }
        String dateString = json.getAsString();
        if (!dateString.endsWith("UTC")) {
            dateString += " UTC";
        }
        try {
            return format.parse(dateString);
        } catch (ParseException e) {
            throw new JsonParseException(e);
        }
    }
}
