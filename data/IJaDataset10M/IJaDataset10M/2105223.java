package net.sf.dozer.util.mapping.annotation.test.field.isAccessible;

import net.sf.dozer.util.mapping.annotation.util.Mapper;
import junit.framework.TestCase;

public class Test extends TestCase {

    public void testAToB() {
        A a = new A();
        B b = Mapper.map(a, B.class);
        assertEquals(b.field, "fieldA");
    }

    public void testBToA() {
        B b = new B();
        A a = Mapper.map(b, A.class);
        assertEquals(a.field, "fieldB");
    }
}
