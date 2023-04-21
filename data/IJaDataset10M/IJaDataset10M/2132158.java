package com.director.core.json.impl.gson;

import com.director.core.DirectException;
import com.director.core.DirectTransactionData;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * Author: Simone Ricciardi
 * Date: 1-giu-2010
 * Time: 11.24.03
 */
public class GsonTransactionData implements DirectTransactionData {

    private JsonElement json;

    private JsonDeserializationContext context;

    public GsonTransactionData(JsonElement json, JsonDeserializationContext context) {
        this.json = json;
        this.context = context;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T parseValue(int order, Class<T> type) {
        if (json.isJsonArray()) {
            JsonArray jsonArray = json.getAsJsonArray();
            if (jsonArray.size() <= order) {
                throw new DirectException("Transaction data list of values cannot doesn't contain parameter with order index: " + order + " cause is too short: " + jsonArray.size());
            }
            return (T) this.context.deserialize(jsonArray.get(order), type);
        }
        throw new DirectException("Transaction data aren't a list of values, cannot access through parameter order index: " + order);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T parseValue(String name, Class<T> type) {
        JsonElement jsonData = json;
        if (json.isJsonArray()) {
            jsonData = ((JsonArray) json).get(0);
        }
        if (jsonData.isJsonObject()) {
            JsonObject jsonObject = jsonData.getAsJsonObject();
            return (T) this.context.deserialize(jsonObject.get(name), type);
        }
        throw new DirectException("Transaction data aren't a list of names/values pairs, cannot access through parameter name: " + name);
    }
}
