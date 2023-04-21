package com.healthmarketscience.common.util;

import junit.framework.TestCase;

/**
 * @author James Ahlborn
 */
public class AppendeeObjectTest extends TestCase {

    public AppendeeObjectTest(String name) {
        super(name);
    }

    public void test() throws Exception {
        AppendeeObject obj = new AppendeeObject();
        StringAppendableExt app = new StringAppendableExt();
        obj.appendTo(app);
        assertEquals(obj.toString(), app.toString());
        assertEquals(obj.toString(), obj.toString(5));
    }
}
