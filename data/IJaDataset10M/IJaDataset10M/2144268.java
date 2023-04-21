package uncertain.testcase.composite;

import java.util.Iterator;
import junit.framework.TestCase;
import uncertain.composite.CompositeLoader;
import uncertain.composite.CompositeMap;
import uncertain.composite.CompositeUtil;

public class CompositeUtilTest extends TestCase {

    CompositeLoader loader = CompositeLoader.createInstanceWithExt("xml");

    static final String PKG_NAME = CompositeUtilTest.class.getPackage().getName();

    static String[] keys = { "unit_id", "name" };

    public CompositeUtilTest(String arg0) {
        super(arg0);
    }

    protected void setUp() throws Exception {
        super.setUp();
    }

    public void testJoin() throws Exception {
        CompositeMap root = loader.loadFromClassPath(PKG_NAME + ".join_test");
        CompositeMap m1 = root.getChild("list1");
        CompositeMap m2 = root.getChild("list2");
        assertNotNull(m1);
        assertNotNull(m2);
        int size = m1.size();
        CompositeUtil.join(m1.getChilds(), m2.getChilds(), keys);
        assertEquals(m1.size(), size);
        Iterator it = m1.getChildIterator();
        while (it.hasNext()) {
            CompositeMap item = (CompositeMap) it.next();
            assertTrue(item.containsKey("propB"));
            String a = item.getString("propA");
            String b = item.getString("propB");
            assertEquals(a.substring(1), b.substring(1));
        }
    }

    public void testGetParent() {
        CompositeMap root = new CompositeMap("root");
        CompositeMap child1 = root.createChild("child1");
        CompositeMap child2 = child1.createChild("child2");
        assertNull(CompositeUtil.findParentWithName(root, "root"));
        assertNull(CompositeUtil.findParentWithName(child1, "child1"));
        assertTrue(CompositeUtil.findParentWithName(child1, "root") == root);
        assertTrue(CompositeUtil.findParentWithName(child2, "child1") == child1);
        assertTrue(CompositeUtil.findParentWithName(child2, "root") == root);
    }

    public void testGetChild() {
        CompositeMap root = new CompositeMap("root");
        CompositeMap child1 = root.createChild("child");
        CompositeMap child2 = root.createChild("child");
        CompositeMap child3 = root.createChild("not_child");
        CompositeMap child4 = child3.createChild("child");
        CompositeMap child5 = child4.createChild("child5");
        child1.put("name", "child1");
        child2.put("name", "child2");
        child3.put("name", "right");
        child4.put("name", "right");
        assertTrue(CompositeUtil.findChild(root, "child", "name", "right") == child4);
        assertTrue(CompositeUtil.findChild(root, "child5") == child5);
    }

    public void testConnectAttribute() {
        CompositeMap root = new CompositeMap("root");
        root.createChild("r").put("name", "a");
        root.createChild("r").put("name", "b");
        root.createChild("r").put("name", "c");
        assertEquals(CompositeUtil.connectAttribute(root, "name"), "a,b,c");
    }

    private static CompositeMap findField(CompositeMap root, String field) {
        return CompositeUtil.findChild(root, "field", "name", field);
    }

    public void testMergeChildsByOverride() throws Exception {
        CompositeMap parent = loader.loadFromClassPath(PKG_NAME + ".parent_map");
        CompositeMap child = loader.loadFromClassPath(PKG_NAME + ".child_map");
        CompositeUtil.mergeChildsByOverride(parent.getChild("fields"), child.getChild("fields"), "name");
        CompositeMap childs = child.getChild("fields");
        CompositeMap id = findField(childs, "id");
        assertNotNull(id);
        assertEquals("true", id.getString("required"));
        CompositeMap name = findField(childs, "name");
        assertNotNull(name);
        assertEquals("string", name.getString("datatype"));
        CompositeMap deptno = findField(childs, "deptno");
        assertNotNull(deptno);
        CompositeMap borndate = findField(childs, "borndate");
        assertNotNull(borndate);
        assertEquals("false", borndate.getString("required"));
        assertEquals("false", borndate.getString("forInsert"));
        CompositeMap address = findField(childs, "address");
        assertNotNull(address);
    }

    public void testMergeChildsByReference() throws Exception {
        CompositeMap parent = loader.loadFromClassPath(PKG_NAME + ".parent_map");
        CompositeMap child = loader.loadFromClassPath(PKG_NAME + ".child_map");
        CompositeUtil.mergeChildsByReference(parent.getChild("fields"), child.getChild("fields"), "name");
        CompositeMap childs = child.getChild("fields");
        assertEquals(childs.getChilds().size(), 4);
        CompositeMap id = findField(childs, "id");
        assertNotNull(id);
        assertEquals("true", id.getString("required"));
        CompositeMap name = findField(childs, "name");
        assertNotNull(name);
        assertEquals("string", name.getString("datatype"));
        CompositeMap deptno = findField(childs, "deptno");
        assertNotNull(deptno);
        CompositeMap borndate = findField(childs, "borndate");
        assertNotNull(borndate);
        assertEquals("false", borndate.getString("required"));
        assertEquals("false", borndate.getString("forInsert"));
    }
}
