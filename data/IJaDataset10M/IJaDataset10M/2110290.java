package org.xmlbinder;

import static junit.framework.Assert.assertEquals;
import org.junit.Test;
import java.io.StringReader;
import java.util.Properties;
import java.util.Map;
import junit.framework.Assert;

/**
 * @author Noble Paul (noble.paul@gmail.com)
 *         Date: Aug 31, 2007
 *         Time: 2:20:36 PM
 */
public class TestMap {

    @Test
    public void testStarAttr() {
        String xml = "<root a=\"Attribute A\" b = \"Attribute B\"/>";
        RootAttrProp r = new XmlBinder().bind(new StringReader(xml), RootAttrProp.class);
        assertEquals(r.ab.size(), 2);
        assertEquals(r.ab.getProperty("a"), "Attribute A");
        assertEquals(r.ab.getProperty("b"), "Attribute B");
    }

    public static class RootAttrProp {

        @Attribute("*")
        Properties ab;
    }

    @Test
    public void testStarAttrMap() {
        String xml = "<root a=\"Attribute A\" b = \"Attribute B\"/>";
        RootAttrProp r = new XmlBinder().bind(new StringReader(xml), RootAttrProp.class);
        assertEquals(r.ab.size(), 2);
        assertEquals(r.ab.getProperty("a"), "Attribute A");
        assertEquals(r.ab.getProperty("b"), "Attribute B");
    }

    public static class RootAttrMap {

        @Attribute("*")
        Map<String, String> ab;
    }
}
