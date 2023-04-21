package org.abreslav.java2ecore.transformation.utils;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class SetFactory {

    public static <T> Set<T> createSet(T... objects) {
        return createSet(null, objects);
    }

    public static <T> Set<T> createSet(Collection<T> parent, T... objects) {
        if (parent == null) {
            parent = Collections.<T>emptySet();
        }
        Set<T> result = new HashSet<T>(parent);
        for (T t : objects) {
            result.add(t);
        }
        return result;
    }
}
