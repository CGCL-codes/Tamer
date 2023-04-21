package org.t2framework.commons.transaction.util;

import java.util.Iterator;
import java.util.ServiceLoader;

public class ServiceLoaderUtil {

    public static <S> S get(Class<S> clazz) {
        ServiceLoader<S> loader = ServiceLoader.load(clazz);
        if (loader != null) {
            Iterator<S> iterator = loader.iterator();
            if (iterator.hasNext()) {
                return iterator.next();
            }
        }
        return null;
    }
}
