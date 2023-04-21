package org.jazzteam.edu.lang.objectCollections;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class HashMapEx1 {

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        Random rand = new Random(47);
        Map<Integer, Integer> mapInt = new HashMap<Integer, Integer>();
        for (int i = 0; i < 1000; i++) {
            int r = rand.nextInt(20);
            Integer freq = mapInt.get(r);
            mapInt.put(r, freq == null ? i : freq + 1);
        }
        System.out.println(mapInt);
    }
}
