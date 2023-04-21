package com.tencent.tendon.convert.json.listeners;

import com.tencent.tendon.convert.json.*;

/**
 *
 * @author nbzhang
 */
public final class JsonCharListener extends JsonListener<Character> {

    public static final JsonCharListener instance = new JsonCharListener();

    private static final JsonXListener listener = JsonXListener.getInstance();

    private JsonCharListener() {
    }

    @Override
    public void convertTo(final JsonWriter out, final Character value) {
        listener.convertCharTo(out, value);
    }

    @Override
    public final Character convertFrom(final JsonReader in) {
        return listener.convertCharFrom(in);
    }

    @Override
    public Character convertFrom(char[] text, int start, int len) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Class<Character> getType() {
        return Character.class;
    }
}
