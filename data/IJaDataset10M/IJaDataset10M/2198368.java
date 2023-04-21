package tests.com.scholardesk.collections;

import java.util.Map;
import junit.framework.TestCase;
import com.scholardesk.collections.PreservingHashMap;
import com.scholardesk.logging.Log4JLogger;
import org.apache.log4j.Logger;

public class PreservingHashMapTest extends TestCase {

    private static final Logger m_logger = Log4JLogger.getLogger(PreservingHashMapTest.class);

    public PreservingHashMap m_map = null;

    public PreservingHashMapTest(String name) {
        super(name);
    }

    protected void setUp() {
        m_map = new PreservingHashMap();
    }

    protected void tearDown() {
    }

    public void testAdd() {
        m_map.put("dogfries", new Integer(1));
        m_map.put("dogfries", new Integer(7));
        m_map.put("dogfries", new Integer(11));
        m_map.put("dogfries", new Integer(4));
        m_map.put("dogfries", new Integer(2));
        m_map.put("catfries", new Integer(193));
        m_map.put("catfries", new Integer(128));
        m_map.put("piefries", new Integer(200));
        Map map = m_map.getMap();
        assertNotNull(map);
    }

    public void testAssert() {
        assertTrue(true);
    }

    public static void main(String args[]) {
        junit.textui.TestRunner.run(PreservingHashMapTest.class);
    }
}
