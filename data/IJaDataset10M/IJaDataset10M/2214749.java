package org.jugile.daims;

import java.util.HashMap;
import java.util.Map;
import org.jugile.util.Buffer;

public class Stats {

    private Class classes[];

    public Class[] classes() {
        return classes;
    }

    public Stats(Class classes[]) {
        this.classes = classes;
    }

    private int count = 0;

    private int total = 0;

    public static class CollStats {

        public int count = 0;

        public int total = 0;

        public int countEmpty = 0;

        public int count1 = 0;

        public String toString() {
            return "" + count + ", " + total + ", " + countEmpty + ", " + count1;
        }
    }

    public static class BoStats {

        public int count = 0;

        public int total = 0;

        public String name = "";

        public BoStats(String name) {
            this.name = name;
        }

        public Map<String, CollStats> map = new HashMap<String, CollStats>();

        public CollStats cstats(String name) {
            CollStats ct = map.get(name);
            if (ct == null) {
                ct = new CollStats();
                map.put(name, ct);
            }
            return ct;
        }
    }

    private Map<Class<Bo>, BoStats> map = new HashMap<Class<Bo>, BoStats>();

    private BoStats bostats(Class<Bo> cl) {
        BoStats bs = map.get(cl);
        if (bs == null) {
            bs = new BoStats(cl.getName());
            map.put(cl, bs);
        }
        return bs;
    }

    protected void setSize(Class<Bo> cl, int size) {
        bostats(cl).count = size;
        count += size;
    }

    protected void addCollStats(Class<Bo> cl, String name, int size) {
        total += size;
        BoStats bs = bostats(cl);
        bs.total += size;
        CollStats cs = bs.cstats(name);
        cs.count++;
        cs.total = cs.total + size;
        if (size == 0) cs.countEmpty++;
        if (size == 1 && size > 0) cs.count1++;
    }

    public String toString() {
        Buffer buf = new Buffer();
        buf.ln();
        buf.ln("================");
        buf.ln("Domain Stats:");
        buf.ln("================");
        buf.ln("name, count = collections or objects, item count = items in collections, empty collections, collections size 1");
        for (Class cl : classes) {
            BoStats bs = map.get(cl);
            buf.ln(bs.name + ", " + bs.count);
            buf.incrIndent();
            for (String name : bs.map.keySet()) {
                CollStats cs = bs.map.get(name);
                buf.ln(name + ", " + cs.toString());
            }
            buf.decrIndent();
        }
        buf.ln("================");
        buf.ln(" objects: 	   " + count);
        buf.ln(" associations: " + total);
        return buf.toString();
    }
}
