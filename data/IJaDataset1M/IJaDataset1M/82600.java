package com.nhncorp.cubridqa.test.console.util;

import java.util.Map;
import junit.framework.TestCase;
import com.nhncorp.cubridqa.utils.EnvGetter;

public class EnvGetterTest extends TestCase {

    public void testSet() {
        Map<String, String> map = EnvGetter.getenv();
        String findkey = null;
        for (String key : map.keySet()) {
            if (key.startsWith("hello")) {
                findkey = key;
                System.out.println("(key=" + key + "|value=" + map.get(key) + ")");
            }
        }
        System.out.println("xxxx==" + System.getenv("xxxx"));
        System.out.println("xxxxx==" + System.getenv("xxxxx"));
        this.assertNotNull(findkey);
    }
}
