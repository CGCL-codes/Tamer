package org.objectstyle.cayenne.util;

import java.util.HashMap;
import java.util.Map;
import junit.framework.TestCase;

/**
 * @author Andrei Adamchik
 */
public class CayenneMapTst extends TestCase {

    protected CayenneMapEntry makeEntry() {
        return new CayenneMapEntry() {

            protected Object parent;

            public String getName() {
                return "abc";
            }

            public Object getParent() {
                return parent;
            }

            public void setParent(Object parent) {
                this.parent = parent;
            }
        };
    }

    public void testConstructor1() throws Exception {
        Object o1 = new Object();
        String k1 = "123";
        Map map = new HashMap();
        map.put(k1, o1);
        CayenneMap cm = new CayenneMap(null, map);
        assertSame(o1, cm.get(k1));
    }

    public void testConstructor2() throws Exception {
        Object parent = new Object();
        CayenneMapEntry o1 = makeEntry();
        String k1 = "123";
        Map map = new HashMap();
        map.put(k1, o1);
        CayenneMap cm = new CayenneMap(parent, map);
        assertSame(o1, cm.get(k1));
        assertSame(parent, o1.getParent());
    }

    public void testPut() throws Exception {
        Object parent = new Object();
        CayenneMapEntry o1 = makeEntry();
        String k1 = "123";
        CayenneMap cm = new CayenneMap(parent);
        cm.put(k1, o1);
        assertSame(o1, cm.get(k1));
        assertSame(parent, o1.getParent());
    }

    public void testParent() throws Exception {
        Object parent = new Object();
        CayenneMap cm = new CayenneMap(null);
        assertNull(cm.getParent());
        cm.setParent(parent);
        assertSame(parent, cm.getParent());
    }

    public void testSerializability() throws Exception {
        String parent = "abcde";
        CayenneMap cm = new CayenneMap(parent);
        CayenneMap d1 = (CayenneMap) Util.cloneViaSerialization(cm);
        assertEquals(cm, d1);
        assertEquals(parent, d1.getParent());
        cm.put("a", "b");
        cm.values();
        CayenneMap d2 = (CayenneMap) Util.cloneViaSerialization(cm);
        assertEquals(cm, d2);
        assertEquals(parent, d2.getParent());
    }
}
