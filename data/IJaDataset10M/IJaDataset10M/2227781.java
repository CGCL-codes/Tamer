package playground.mrieser.core.mobsim.utils;

import org.junit.Assert;
import org.junit.Test;
import playground.mrieser.core.mobsim.utils.ClassBasedMap;

/**
 * @author mrieser
 */
public class ClassBasedMapTest {

    @Test
    public void testGet_Simple() {
        ClassBasedMap<Object, String> map = new ClassBasedMap<Object, String>();
        map.put(String.class, "String");
        map.put(Integer.class, "Integer");
        map.put(Float.class, "Float");
        Assert.assertEquals("String", map.get(String.class));
        Assert.assertEquals("Integer", map.get(Integer.class));
        Assert.assertEquals("Float", map.get(Float.class));
        Assert.assertNull(map.get(Double.class));
    }

    @Test
    public void testGet_withInheritance() {
        ClassBasedMap<Object, String> map = new ClassBasedMap<Object, String>();
        map.put(A.class, "A");
        map.put(B.class, "B");
        Assert.assertEquals("A", map.get(A.class));
        Assert.assertEquals("B", map.get(B.class));
        Assert.assertEquals("A", map.get(Aimpl.class));
        Assert.assertEquals("B", map.get(Bimpl.class));
        Assert.assertEquals("B", map.get(Cimpl.class));
    }

    /**
	 * Tests an internal implementation detail: if the cache works correctly
	 */
    @Test
    public void testGet_withInheritance_Cached() {
        ClassBasedMap<Object, String> map = new ClassBasedMap<Object, String>();
        map.put(A.class, "A");
        map.put(B.class, "B");
        Assert.assertEquals("A", map.get(A.class));
        Assert.assertEquals("B", map.get(B.class));
        Assert.assertEquals("A", map.get(Aimpl.class));
        Assert.assertEquals("B", map.get(Bimpl.class));
        Assert.assertEquals("B", map.get(Cimpl.class));
        Assert.assertEquals("A", map.get(A.class));
        Assert.assertEquals("B", map.get(B.class));
        Assert.assertEquals("A", map.get(Aimpl.class));
        Assert.assertEquals("B", map.get(Bimpl.class));
        Assert.assertEquals("B", map.get(Cimpl.class));
    }

    @Test
    public void testGet_withMultipleInheritance() {
        ClassBasedMap<Object, String> map = new ClassBasedMap<Object, String>();
        map.put(A.class, "A");
        map.put(B.class, "B");
        map.put(ABimpl.class, "AB");
        Assert.assertEquals("AB", map.get(ABimpl.class));
    }

    @Test
    public void testRemove() {
        ClassBasedMap<Object, String> map = new ClassBasedMap<Object, String>();
        map.put(A.class, "A");
        map.put(B.class, "B");
        Assert.assertEquals("A", map.get(A.class));
        Assert.assertEquals("A", map.get(Aimpl.class));
        Assert.assertEquals("B", map.get(B.class));
        Assert.assertEquals("A", map.remove(A.class));
        Assert.assertNull(map.get(A.class));
        Assert.assertNull(map.get(Aimpl.class));
        Assert.assertEquals("B", map.get(B.class));
    }

    @Test
    public void testPut_Replace() {
        ClassBasedMap<Object, String> map = new ClassBasedMap<Object, String>();
        map.put(A.class, "A");
        map.put(B.class, "B");
        Assert.assertEquals("A", map.get(A.class));
        Assert.assertEquals("A", map.get(Aimpl.class));
        Assert.assertEquals("B", map.get(B.class));
        Assert.assertEquals("A", map.put(A.class, "aa"));
        Assert.assertEquals("aa", map.get(A.class));
        Assert.assertEquals("aa", map.get(Aimpl.class));
        Assert.assertEquals("B", map.get(B.class));
    }

    @Test
    public void testGet_Superclass() {
        ClassBasedMap<Object, String> map = new ClassBasedMap<Object, String>();
        map.put(Aimpl.class, "A");
        Assert.assertEquals("A", map.get(Aimpl.class));
        Assert.assertNull(map.get(A.class));
    }

    static interface A {
    }

    static interface B {
    }

    static class Aimpl implements A {
    }

    static class Bimpl implements B {
    }

    static class Cimpl extends Bimpl {
    }

    static class ABimpl implements A, B {
    }
}
