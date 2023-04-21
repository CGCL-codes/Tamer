package org.powermock.reflect.context;

import java.util.HashMap;
import java.util.Map;

/**
 * The purpose of this context is that it should define fields not available in
 * the target object to where the state is supposed to be copied.
 */
public class ClassFieldsNotInTargetContext {

    private static long something = 42L;

    private static Map<?, ?> map = new HashMap<Object, Object>();

    public static long getLong() {
        return something;
    }

    public static Map<?, ?> getMap() {
        return map;
    }
}
