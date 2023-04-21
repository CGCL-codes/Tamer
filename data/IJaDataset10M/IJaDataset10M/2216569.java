package org.riverock.portlet.test;

import java.util.Hashtable;
import java.util.Enumeration;

public class TestHashtable {

    public static void main(String args[]) throws Exception {
        Hashtable hash = new Hashtable(20);
        hash.put("1", "Object 1");
        hash.put("2", "Object 2");
        hash.put("3", "Object 3");
        hash.put("4", "Object 4");
        hash.put("2", "Object 222");
        for (Enumeration e = hash.elements(); e.hasMoreElements(); ) {
            System.out.println(e.nextElement());
        }
        System.out.println("count element - " + hash.size());
        hash.put("5", "Object 5");
        System.out.println("count element - " + hash.size());
        String key1 = new String("1");
        String key2 = new String("1");
        System.out.println("key 1 - " + key1.getClass());
        System.out.println("key 2 - " + key2.getClass());
        System.out.println("key1==key2  - " + (key1 == key2));
        System.out.println("object with key 1 - " + hash.get(key1));
        System.out.println("object with key 2 - " + hash.get(key2));
    }
}
