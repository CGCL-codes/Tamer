package foo;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;

public class Hashing {

    public static void main(final String... args) {
        populate(new Hashtable());
        populate(new Hashtable(47), " (47)");
        populate(new Hashtable(95), " (95)");
        populate(new HashMap());
        populate(new HashMap(32), " (32)");
        populate(new HashMap(64), " (64)");
        populate(new LinkedHashMap());
        populate(new IdentityHashMap());
        populate(new WeakHashMap());
        populate(new ConcurrentHashMap());
        populate(new TreeMap());
        populate(new ConcurrentSkipListMap());
    }

    private static void populate(final Map map) {
        populate(map, "");
    }

    private static void populate(final Map map, final String suffix) {
        for (final String s : "one,two,three,four,five,six,seven,eight,nine,ten".split(",")) map.put(s, s);
        System.out.println(map.getClass().getSimpleName() + suffix + " " + map.keySet());
    }
}
