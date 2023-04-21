package org.zkoss.util;

import junit.framework.*;
import java.util.*;
import java.io.*;
import org.zkoss.util.logging.Log;

public class MapsTest extends TestCase {

    public static final Log log = Log.lookup(MapsTest.class);

    protected static final String FILE = "/metainfo/org/zkoss/util/t1.properties";

    public MapsTest(String name) {
        super(name);
        log.setLevel(Log.ALL);
    }

    public static Test suite() {
        return new TestSuite(MapsTest.class);
    }

    protected void setUp() {
    }

    protected void tearDown() {
    }

    public void compare(Map exp, Map map) throws Exception {
        assertEquals(new Integer(exp.size()), new Integer(map.size()));
        final Iterator itExp = exp.entrySet().iterator();
        final Iterator it = map.entrySet().iterator();
        while (it.hasNext()) {
            final Map.Entry meExp = (Map.Entry) itExp.next();
            final Map.Entry me = (Map.Entry) it.next();
            assertEquals(meExp.getKey(), me.getKey());
            assertEquals(meExp.getValue(), me.getValue());
        }
    }

    public void testMaps() throws Exception {
        String[] keys = { "a", "b", "c d", "d", "e", "f", "g h", "x.y.z", "x.y.m", "x.ijk" };
        String[] vals = { "1", "2 C", "3 4", "4", "5 ", "6{", "\t\t8", "xyz", "xym", "i { j" };
        Map exp = new LinkedHashMap();
        for (int j = 0; j < keys.length; ++j) exp.put(keys[j], vals[j]);
        Map map = new LinkedHashMap();
        Maps.load(map, MapsTest.class.getResourceAsStream(FILE), "UTF-16");
        compare(exp, map);
        map.clear();
        Maps.load(map, MapsTest.class.getResourceAsStream(FILE));
        compare(exp, map);
    }

    public void testParseMap() throws Exception {
        final Map exp = new LinkedHashMap();
        exp.put("ab", "cd");
        exp.put("a12", "1 2");
        exp.put("123", null);
        exp.put("xy", "");
        exp.put("cdf", "xyz  'wcm");
        exp.put("ad", "");
        String s1 = "ab=cd a12='1 2'  123 xy  =''cdf='xyz  \\'wcm' ad=";
        compare(exp, Maps.parse(new LinkedHashMap(), s1, ' ', '\''));
        String s2 = "ab=cd  ,  a12=1 2,123 , xy = ,cdf=xyz  'wcm , ad=    ";
        compare(exp, Maps.parse(new LinkedHashMap(), s2, ',', (char) 0));
        String s3 = "ab=cd;a12=1 2;123;xy=;cdf=\"xyz  'wcm\"; ad=";
        compare(exp, Maps.parse(new LinkedHashMap(), s3, ';', '"'));
        assertEquals("ab='cd' a12='1\\ 2' 123 xy='' cdf='xyz\\ \\ \\'wcm' ad=''", Maps.toString(exp, '\'', ' '));
    }
}
