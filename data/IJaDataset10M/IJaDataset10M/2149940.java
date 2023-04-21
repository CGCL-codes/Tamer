package de.rentoudu.chat.server.guice;

import java.util.Date;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.Provider;
import com.googlecode.objectify.Key;
import de.rentoudu.chat.server.serializer.DateSerializer;
import de.rentoudu.chat.server.serializer.KeyStringSerializer;

/**
 * Simple Provider for {@link Gson} instance.
 * 
 * @author Florian Sauter
 */
public class GsonProvider implements Provider<Gson> {

    @Override
    public Gson get() {
        GsonBuilder gson = new GsonBuilder();
        gson.registerTypeAdapter(Date.class, DateSerializer.create());
        gson.registerTypeAdapter(Key.class, KeyStringSerializer.create());
        return gson.create();
    }
}
