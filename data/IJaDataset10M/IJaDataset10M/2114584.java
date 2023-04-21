package org.stjs.server.json.gson;

import org.stjs.javascript.Array;
import org.stjs.javascript.Date;
import org.stjs.javascript.Map;
import org.stjs.server.ArrayImpl;
import org.stjs.server.MapImpl;
import com.google.gson.GsonBuilder;

public class GsonAdapters {

    public static void addAll(GsonBuilder builder) {
        builder.registerTypeAdapter(MapImpl.class, new JSMapAdapter());
        builder.registerTypeAdapter(Map.class, new JSMapAdapter());
        builder.registerTypeAdapter(ArrayImpl.class, new JSArrayAdapter());
        builder.registerTypeAdapter(Array.class, new JSArrayAdapter());
        builder.registerTypeAdapter(Date.class, new JSDateAdapter());
    }
}
