package com.dukesoftware.utils.data;

import java.util.HashMap;
import com.dukesoftware.utils.data.CachedIntegerMap;
import junit.framework.TestCase;

public class CachedIntegerMapTest extends TestCase {

    public void testname() throws Exception {
        CachedIntegerMap<String> map = new CachedIntegerMap<String>(-100, 100);
        HashMap<Integer, String> map2 = new HashMap<Integer, String>();
        for (int i = -100; i < 101; i++) {
            map.put(i, "A:" + i);
            map2.put(i, "A:" + i);
        }
        for (int j = -100; j < 101; j++) {
            assertEquals(map2.get(j), map.get(j));
        }
    }
}
