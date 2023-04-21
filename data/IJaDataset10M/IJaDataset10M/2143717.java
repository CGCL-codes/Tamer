package com.twolattes.json.inheritance2;

import static com.twolattes.json.Json.number;
import static com.twolattes.json.Json.string;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import com.twolattes.json.EntityMarshaller;
import com.twolattes.json.Json;
import com.twolattes.json.TwoLattes;

/**
 * Tests marshalling and unmarshalling for the inheritance of entities defined
 * in this package.
 * @author Pascal
 */
public class Inheritance2Test {

    private EntityMarshaller<A> marshaller;

    @Before
    public void start() {
        marshaller = TwoLattes.createEntityMarshaller(A.class);
    }

    @Test
    public void testMarshallA() throws Exception {
        A a = new A();
        a.age = 17;
        Json.Object o = marshaller.marshall(a);
        assertEquals(2, o.size());
        assertEquals(string("a"), o.get(string("type")));
        assertEquals(number(17), o.get(string("age")));
    }

    @Test
    public void testMarshallB() throws Exception {
        B b = new B();
        b.name = "John";
        b.age = 83;
        Json.Object o = marshaller.marshall(b);
        assertEquals(3, o.size());
        assertEquals(string("b"), o.get(string("type")));
        assertEquals(string("John"), o.get(string("name")));
        assertEquals(number(83), o.get(string("age")));
    }

    @Test
    public void testUnmarshallA() throws Exception {
        A a = marshaller.unmarshall(Json.fromString("{\"type\":\"a\", \"age\":5}"));
        assertEquals(5, a.age);
    }

    @Test
    public void testUnmarshallB() throws Exception {
        A a = marshaller.unmarshall(Json.fromString("{\"type\":\"b\", \"age\": 24, \"name\":\"John\"}"));
        assertTrue(a instanceof B);
        B b = (B) a;
        assertEquals("John", b.name);
        assertEquals(24, b.age);
    }
}
