package net.asfun.jangod.util;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

public class ObjectIterator {

    @SuppressWarnings("unchecked")
    public static ForLoop getLoop(Object obj) {
        if (obj == null) {
            return new ForLoop(new ArrayList<Object>().iterator(), 0);
        }
        if (obj instanceof Collection) {
            Collection<?> clt = (Collection<?>) obj;
            return new ForLoop(clt.iterator(), clt.size());
        }
        if (obj.getClass().isArray()) {
            return new ForLoop(new ArrayIterator(obj), Array.getLength(obj));
        }
        if (obj instanceof Map) {
            Collection<?> clt = ((Map<?, ?>) obj).values();
            return new ForLoop(clt.iterator(), clt.size());
        }
        if (obj instanceof Iterable) {
            return new ForLoop(((Iterable<?>) obj).iterator());
        }
        if (obj instanceof Iterator) {
            return new ForLoop((Iterator<?>) obj);
        }
        ArrayList<Object> res = new ArrayList<Object>();
        res.add(obj);
        return new ForLoop(res.iterator(), 1);
    }
}
