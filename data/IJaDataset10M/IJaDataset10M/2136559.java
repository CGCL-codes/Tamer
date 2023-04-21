package autogroup;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by IntelliJ IDEA.
 * User: adenysenko
 * Date: 24 лист 2008
 * Time: 12:57:49
 */
public class Grper {

    String prefix = "";

    TreeMap<String, Grper> groups = new TreeMap<String, Grper>();

    TreeMap<String, AtomicLong> intree = new TreeMap<String, AtomicLong>();

    void next(final String s) {
        NavigableMap<String, Grper> m = groups.headMap(s, true);
        if (!m.isEmpty()) {
            String k = m.lastEntry().getKey();
            if (s.startsWith(k)) {
                groups.get(k).next(s);
                return;
            }
        }
        AtomicLong l = intree.get(s);
        if (l == null) {
            intree.put(s, new AtomicLong(1));
        } else {
            l.incrementAndGet();
        }
        long ccPrev = 0;
        for (int i = s.length(); i >= prefix.length() + 1; i--) {
            String pref = s.substring(0, i);
            long cc = countWithPrefix(pref);
            if (ccPrev != 0 && cc >= ccPrev * 2 && cc > 2) {
                Grper g = new Grper();
                g.prefix = pref;
                Map<String, AtomicLong> sub = intree.subMap(pref, true, pref + "￿", true);
                g.intree.putAll(sub);
                sub.clear();
                groups.put(pref, g);
                break;
            }
            ccPrev = cc;
        }
    }

    long countWithPrefix(String p) {
        long c = 0;
        for (String k : intree.subMap(p, true, p + "￿", true).keySet()) {
            c += intree.get(k).longValue();
        }
        return c;
    }
}
