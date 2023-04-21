package org.illico.common.lang;

import java.util.Collection;

public final class CollectionUtils {

    private CollectionUtils() {
    }

    public static <T> void assign(Collection<T> dest, Collection<T> src) {
        dest.clear();
        dest.addAll(src);
    }
}
