package net.innig.util;

import java.util.*;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class GraphTypeTest extends TestCase {

    public GraphTypeTest(String name) {
        super(name);
    }

    public static Test suite() {
        return new TestSuite(GraphTypeTest.class);
    }

    protected void setUp() {
    }

    protected void tearDown() {
    }

    public void test_equality() {
        assertEquals(JavaItem.MEMBER, JavaItem.MEMBER);
        assertTrue(JavaItem.TYPE.equals(JavaItem.TYPE));
        assertTrue(!JavaItem.MEMBER.equals(JavaItem.TYPE));
    }

    public void test_parents() {
        assertEquals(Collections.EMPTY_SET, JavaItem.ANY.getParents());
        assertEquals(Collections.singleton(JavaItem.ANY), JavaItem.MEMBER.getParents());
        assertEquals(makeSet(new JavaItem[] { JavaItem.TYPE, JavaItem.MEMBER }), JavaItem.CLASS.getParents());
    }

    public void test_ancestors() {
        assertEquals(Collections.singleton(JavaItem.ANY), JavaItem.ANY.getAncestors());
        assertEquals(makeSet(new JavaItem[] { JavaItem.ANY, JavaItem.MEMBER }), JavaItem.MEMBER.getAncestors());
        assertEquals(makeSet(new JavaItem[] { JavaItem.ANY, JavaItem.TYPE, JavaItem.MEMBER, JavaItem.CLASS }), JavaItem.CLASS.getAncestors());
    }

    public void test_children() {
        assertEquals(Collections.EMPTY_SET, JavaItem.FIELD.getChildren());
        assertEquals(makeSet(new JavaItem[] { JavaItem.PACKAGE, JavaItem.MEMBER, JavaItem.TYPE }), JavaItem.ANY.getChildren());
        assertEquals(makeSet(new JavaItem[] { JavaItem.CLASS, JavaItem.INTERFACE, JavaItem.PRIMITIVE }), JavaItem.TYPE.getChildren());
        assertEquals(makeSet(new JavaItem[] { JavaItem.CLASS, JavaItem.INTERFACE, JavaItem.FIELD, JavaItem.METHOD }), JavaItem.MEMBER.getChildren());
    }

    public void test_descendents() {
        assertEquals(Collections.singleton(JavaItem.FIELD), JavaItem.FIELD.getDescendents());
        assertEquals(EnumeratedType.allTypes(JavaItem.class), JavaItem.ANY.getDescendents());
        assertEquals(makeSet(new JavaItem[] { JavaItem.TYPE, JavaItem.CLASS, JavaItem.INTERFACE, JavaItem.PRIMITIVE }), JavaItem.TYPE.getDescendents());
    }

    public void test_is() {
        assertTrue(JavaItem.TYPE.is(JavaItem.ANY));
        assertTrue(JavaItem.CLASS.is(JavaItem.TYPE));
        assertTrue(JavaItem.CLASS.is(JavaItem.MEMBER));
        assertTrue(!JavaItem.MEMBER.is(JavaItem.CLASS));
        assertTrue(!JavaItem.MEMBER.is(JavaItem.TYPE));
    }

    private Set makeSet(JavaItem[] items) {
        return new HashSet(Arrays.asList(items));
    }
}
