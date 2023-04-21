package com.hp.hpl.mars.portal.util;

import static org.junit.Assert.assertEquals;
import java.util.Map;
import org.junit.Test;

public class StringHelperTest {

    @Test
    public void tokenize() {
        Map<String, String> tokens = StringHelper.tokenizeAction("offset=10;limit=20");
        assertEquals(2, tokens.size());
        assertEquals("10", tokens.get("offset"));
        assertEquals("20", tokens.get("limit"));
    }

    @Test
    public void tokenizeWrong() {
        Map<String, String> tokens = StringHelper.tokenizeAction("offset=10;limit");
        assertEquals(2, tokens.size());
        assertEquals("10", tokens.get("offset"));
        assertEquals(null, tokens.get("limit"));
    }

    @Test
    public void toInteger() {
        assertEquals(null, StringHelper.stringToInteger(null));
        assertEquals(null, StringHelper.stringToInteger(""));
        assertEquals(null, StringHelper.stringToInteger("aa"));
        assertEquals(10, StringHelper.stringToInteger("10"));
    }

    @Test
    public void splitLabelPath() {
        String arg = "/elt1/elt2/elt3";
        String[] result = StringHelper.splitLabelPath(arg);
        assertEquals(3, result.length);
        assertEquals("elt1", result[0]);
        assertEquals("elt3", result[2]);
    }
}
