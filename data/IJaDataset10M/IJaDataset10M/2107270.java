package org.enerj.apache.commons.collections.map;

import java.util.HashMap;
import java.util.Map;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.enerj.apache.commons.collections.Factory;
import org.enerj.apache.commons.collections.FactoryUtils;
import org.enerj.apache.commons.collections.Transformer;
import org.enerj.apache.commons.collections.functors.ConstantFactory;

/**
 * Extension of {@link TestMap} for exercising the 
 * {@link DefaultedMap} implementation.
 *
 * @since Commons Collections 3.2
 * @version $Revision: 155406 $ $Date: 2005-03-22 22:53:50 +0000 (Tue, 22 Mar 2005) $
 * 
 * @author Stephen Colebourne
 */
public class TestDefaultedMap extends AbstractTestMap {

    protected static final Factory nullFactory = FactoryUtils.nullFactory();

    public TestDefaultedMap(String testName) {
        super(testName);
    }

    public static Test suite() {
        return new TestSuite(TestDefaultedMap.class);
    }

    public static void main(String args[]) {
        String[] testCaseName = { TestDefaultedMap.class.getName() };
        junit.textui.TestRunner.main(testCaseName);
    }

    public Map makeEmptyMap() {
        return DefaultedMap.decorate(new HashMap(), nullFactory);
    }

    public void testMapGet() {
        Map map = new DefaultedMap("NULL");
        assertEquals(0, map.size());
        assertEquals(false, map.containsKey("NotInMap"));
        assertEquals("NULL", map.get("NotInMap"));
        map.put("Key", "Value");
        assertEquals(1, map.size());
        assertEquals(true, map.containsKey("Key"));
        assertEquals("Value", map.get("Key"));
        assertEquals(false, map.containsKey("NotInMap"));
        assertEquals("NULL", map.get("NotInMap"));
    }

    public void testMapGet2() {
        HashMap base = new HashMap();
        Map map = DefaultedMap.decorate(base, "NULL");
        assertEquals(0, map.size());
        assertEquals(0, base.size());
        assertEquals(false, map.containsKey("NotInMap"));
        assertEquals("NULL", map.get("NotInMap"));
        map.put("Key", "Value");
        assertEquals(1, map.size());
        assertEquals(1, base.size());
        assertEquals(true, map.containsKey("Key"));
        assertEquals("Value", map.get("Key"));
        assertEquals(false, map.containsKey("NotInMap"));
        assertEquals("NULL", map.get("NotInMap"));
    }

    public void testMapGet3() {
        HashMap base = new HashMap();
        Map map = DefaultedMap.decorate(base, ConstantFactory.getInstance("NULL"));
        assertEquals(0, map.size());
        assertEquals(0, base.size());
        assertEquals(false, map.containsKey("NotInMap"));
        assertEquals("NULL", map.get("NotInMap"));
        map.put("Key", "Value");
        assertEquals(1, map.size());
        assertEquals(1, base.size());
        assertEquals(true, map.containsKey("Key"));
        assertEquals("Value", map.get("Key"));
        assertEquals(false, map.containsKey("NotInMap"));
        assertEquals("NULL", map.get("NotInMap"));
    }

    public void testMapGet4() {
        HashMap base = new HashMap();
        Map map = DefaultedMap.decorate(base, new Transformer() {

            public Object transform(Object input) {
                if (input instanceof String) {
                    return "NULL";
                }
                return "NULL_OBJECT";
            }
        });
        assertEquals(0, map.size());
        assertEquals(0, base.size());
        assertEquals(false, map.containsKey("NotInMap"));
        assertEquals("NULL", map.get("NotInMap"));
        assertEquals("NULL_OBJECT", map.get(new Integer(0)));
        map.put("Key", "Value");
        assertEquals(1, map.size());
        assertEquals(1, base.size());
        assertEquals(true, map.containsKey("Key"));
        assertEquals("Value", map.get("Key"));
        assertEquals(false, map.containsKey("NotInMap"));
        assertEquals("NULL", map.get("NotInMap"));
        assertEquals("NULL_OBJECT", map.get(new Integer(0)));
    }

    public String getCompatibilityVersion() {
        return "3.2";
    }
}
