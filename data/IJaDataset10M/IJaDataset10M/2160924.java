package org.gzigzag.util;

import java.util.*;
import org.gzigzag.benchmark.*;

public class BenchMaps {

    public static final String rcsid = "$Id: BenchMaps.java,v 1.3 2001/10/19 19:33:42 bfallenstein Exp $";

    private static void pa(String s) {
        System.out.println(s);
    }

    static final int NOBJS = 1000;

    static class FieldId {

        FieldId(int hash) {
            this.hash = hash;
        }

        int hash;

        public int hashCode() {
            return hash;
        }
    }

    FieldId[] different = new FieldId[NOBJS];

    FieldId[] same = new FieldId[NOBJS];

    {
        Random r = new Random();
        for (int i = 0; i < NOBJS; i++) {
            different[i] = new FieldId(r.nextInt());
            same[i] = new FieldId(42);
        }
    }

    void addBenches(String name, final Class type, List into) throws Exception {
        into.add("MAP_ADD_DIFF_" + NOBJS + "_" + name);
        into.add("Add different-hashing objects to a " + name);
        into.add(new Runnable() {

            public void run() {
                try {
                    final Map map = (Map) type.newInstance();
                    for (int i = 0; i < NOBJS; i++) map.put(different[i], same[i]);
                } catch (InstantiationException e) {
                    throw new Error(" " + e);
                } catch (IllegalAccessException e) {
                    throw new Error(" " + e);
                }
            }
        });
        final Map oneMap = (Map) type.newInstance();
        into.add("MAP_SAME_ADD_DIFF_" + NOBJS + "_" + name);
        into.add("Add different-hashing objects to a " + name);
        into.add(new Runnable() {

            public void run() {
                for (int i = 0; i < NOBJS; i++) oneMap.put(different[i], same[i]);
            }
        });
        into.add("MAP_GET_DIFF_" + NOBJS + "_" + name);
        into.add("Get different-hashing objects from a " + name);
        into.add(new Runnable() {

            public void run() {
                for (int i = 0; i < NOBJS; i++) oneMap.get(different[i]);
            }
        });
    }

    public void bench(String[] argv) throws Exception {
        Bench b = new Bench(argv);
        List l = new ArrayList();
        addBenches("hashmap", HashMap.class, l);
        addBenches("secondaryflathash", SecondaryHashMap.class, l);
        Object[] o = l.toArray();
        b.run(o);
    }

    public static void main(String[] argv) throws Exception {
        try {
            new BenchMaps().bench(argv);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}
