package com.maiereni.common.util;

import com.maiereni.common.util.StreamUtils;
import com.maiereni.common.util.StringTemplateHolder;
import junit.framework.TestCase;

public class StringTemplateHolderTest extends TestCase {

    private static final String TEMPLATE1 = "classpath:/com/maiereni/web/filter/InitializationTemplate.txt";

    private static final String TEMPLATE2 = "classpath:/com/maiereni/web/filter/RedirectionTemplate.txt";

    public void testGenerate1() throws Exception {
        java.util.Map<String, Object> values = new java.util.Hashtable<String, Object>();
        StringTemplateHolder templateHolder = StreamUtils.getTemplateForResource(StringTemplateHolderTest.TEMPLATE1);
        values.put("ids", "<option>simple</option>");
        values.put("key", "Ajax2Key");
        String result = templateHolder.makeString(values);
        assertNotNull(result);
        System.out.println(result);
        assertTrue(result.indexOf(StringTemplateHolder.UNDEFINED) == -1);
    }

    public void testGenerate2() throws Exception {
        java.util.Map<String, Object> values = new java.util.Hashtable<String, Object>();
        StringTemplateHolder templateHolder = StreamUtils.getTemplateForResource(StringTemplateHolderTest.TEMPLATE2);
        values.put("content", "A simple content");
        values.put("key", "Ajax2Key");
        String result = templateHolder.makeString(values);
        assertNotNull(result);
        assertTrue(result.indexOf("A simple content") == 0);
        assertTrue(result.indexOf(StringTemplateHolder.UNDEFINED) == -1);
    }
}
