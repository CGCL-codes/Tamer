package com.google.gwt.dev.json;

import java.io.IOException;
import java.io.Writer;

/**
 * JSON String.
 */
public class JsonString implements JsonValue {

    public static JsonString create(String value) {
        return new JsonString(value);
    }

    static void write(String data, Writer writer) throws IOException {
        if (data == null) {
            writer.append("null");
            return;
        }
        writer.append('"');
        for (int i = 0, n = data.length(); i < n; ++i) {
            final char c = data.charAt(i);
            switch(c) {
                case '\\':
                case '"':
                    writer.append('\\').append(c);
                    break;
                case '\b':
                    writer.append("\\b");
                    break;
                case '\t':
                    writer.append("\\t");
                    break;
                case '\n':
                    writer.append("\\n");
                    break;
                case '\f':
                    writer.append("\\f");
                    break;
                case '\r':
                    writer.append("\\r");
                    break;
                default:
                    writer.append(c);
            }
        }
        writer.append('"');
    }

    private final String value;

    private JsonString(String value) {
        this.value = value;
    }

    public JsonArray asArray() {
        return null;
    }

    public JsonBoolean asBoolean() {
        return null;
    }

    public JsonNumber asNumber() {
        return null;
    }

    public JsonObject asObject() {
        return null;
    }

    public JsonString asString() {
        return this;
    }

    public JsonString copyDeeply() {
        return new JsonString(value);
    }

    public String getString() {
        return value;
    }

    public boolean isArray() {
        return false;
    }

    public boolean isBoolean() {
        return false;
    }

    public boolean isNumber() {
        return false;
    }

    public boolean isObject() {
        return false;
    }

    public boolean isString() {
        return true;
    }

    public void write(Writer writer) throws IOException {
        write(value, writer);
    }
}
