package com.xavax.jsf.json.test;

import java.util.List;
import java.util.Map;
import com.xavax.jsf.json.JSONArray;
import com.xavax.jsf.json.JSONParser;
import com.xavax.jsf.test.TestCase;

public class TestJSONArray extends TestCase {

    private JSONParser parser;

    private final String inputATL = "{name: 'Atlanta', x: 3.0000, y: 4.0000, pop: 4500000}";

    private final String inputBHM = "{name: 'Birmingham', x: 1.0000, y: 2.0000, pop: 1250000}";

    private final String inputCLT = "{name: 'Charlotte', x: 5.0000, y: 6.0000, pop: 2000000}";

    private final String cities = "[" + inputATL + "," + inputBHM + "," + inputCLT + "]";

    public void setUp() {
        parser = new JSONParser();
    }

    public void testFlatten() {
        JSONArray ja = parser.parseArray(cities);
        assertTrue(parser.isValid());
        List<String> l = ja.flatten();
        assertEquals(3, l.size());
        String s = l.get(0);
        assertEquals(40, s.length());
    }

    public void testAsMap() {
        JSONArray ja = parser.parseArray(cities);
        assertTrue(parser.isValid());
        Map<String, String> map = ja.asMap();
        assertEquals(3, map.size());
        String s = map.get("0");
        assertEquals(40, s.length());
    }
}
