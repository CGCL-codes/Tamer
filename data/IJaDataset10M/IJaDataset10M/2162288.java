package org.apache.harmony.jndi.tests.javax.naming;

import javax.naming.NameClassPair;
import junit.framework.TestCase;

public class NameClassPairTest extends TestCase {

    /**
	 * Constructor for TestNameClassPair.
	 * 
	 * @param arg0
	 */
    public NameClassPairTest(String arg0) {
        super(arg0);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testConstructor_Simple() {
        NameClassPair p;
        p = new NameClassPair("name1", "class1");
        assertEquals("name1", p.getName());
        assertEquals("class1", p.getClassName());
        p = new NameClassPair("name2", "class2", false);
        assertEquals("name2", p.getName());
        assertEquals("class2", p.getClassName());
        assertFalse(p.isRelative());
    }

    public void testConstructor_NullValue() {
        NameClassPair p;
        p = new NameClassPair("name1", null);
        assertEquals("name1", p.getName());
        assertNull(p.getClassName());
    }

    public void testConstructor_DefaultRelativeValue() {
        NameClassPair p;
        p = new NameClassPair("name1", null);
        assertTrue(p.isRelative());
    }

    public void testToString() {
        NameClassPair p;
        p = new NameClassPair("name1", null, false);
        assertTrue(p.toString().startsWith("(not relative"));
    }

    public void testGetSetName() {
        NameClassPair p;
        p = new NameClassPair("name1", null, true);
        p.setName("aname");
        assertEquals("aname", p.getName());
    }

    public void testGetSetClassName() {
        NameClassPair p;
        p = new NameClassPair("name1", null, true);
        p.setClassName("aclassname");
        assertEquals("aclassname", p.getClassName());
    }

    public void testGetSetRelative() {
        NameClassPair p;
        p = new NameClassPair("name1", null, true);
        p.setRelative(false);
        assertFalse(p.isRelative());
    }
}
