package com.bingzer.bison;

import com.bingzer.bison.JsonValue;
import com.bingzer.bison.JsonType;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Ricky Tobing
 */
public class Test_PrimitiveTypesTest {

    Json json;

    CharSequence jsonText;

    /**
     * 
     */
    public Test_PrimitiveTypesTest() {
        System.out.println(">>Running: Test -- Primitive types");
    }

    /**
     * 
     */
    @Test
    public void testByte() {
        json = Bison.jsonify((byte) 1);
        assertTrue(json.as(JsonNumber.class).byteValue() == 1);
    }

    /**
     * 
     */
    @Test
    public void testShort() {
        json = Bison.jsonify((short) 10);
        assertTrue(json.as(JsonNumber.class).shortValue() == 10);
    }

    /**
     * 
     */
    @Test
    public void testInteger() {
        json = Bison.jsonify((int) 10);
        assertTrue(json.as(JsonNumber.class).intValue() == 10);
    }

    /**
     * 
     */
    @Test
    public void testLong() {
        json = Bison.jsonify((long) 10);
        assertTrue(json.as(JsonNumber.class).longValue() == 10);
    }

    /**
     * 
     */
    @Test
    public void testFloat() {
        json = Bison.jsonify((float) 10.5f);
        assertTrue((float) json.as(JsonNumber.class).floatValue() == 10.5f);
    }

    /**
     * 
     */
    @Test
    public void testDouble() {
        json = Bison.jsonify((double) 10.5f);
        assertTrue((double) json.as(JsonNumber.class).doubleValue() == (double) 10.5);
    }

    /**
     * 
     */
    @Test
    public void testBoolean() {
        json = Bison.jsonify(true);
        assertTrue(json.as(JsonBoolean.class).booleanValue());
    }

    /**
     * 
     */
    @Test
    public void testChar() {
        json = Bison.jsonify('c');
        assertTrue(json.as(JsonString.class).stringValue().equals("c"));
    }
}
