package com.leemba.monitor.json;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.leemba.monitor.server.annotations.PublicGroup;

/**
 * Some classes have a seperate public representation controlled by annotations.
 *
 * @author mrjohnson
 */
public class PublicSerializer implements ExclusionStrategy {

    @Override
    public boolean shouldSkipField(FieldAttributes f) {
        return f.getAnnotation(PublicGroup.class) == null;
    }

    @Override
    public boolean shouldSkipClass(Class<?> clazz) {
        return false;
    }
}
