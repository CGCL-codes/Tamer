package com.bingzer.bison;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Ricky Tobing
 */
public class JsonObjectTest {

    Json json = null;

    CharSequence jsonText = null;

    /**
     * 
     */
    public JsonObjectTest() {
        System.out.println(">>Running: JsonObject Test");
    }

    /**
     * 
     */
    @Before
    public void setUp() {
        json = Bison.jsonify(JsonValue.OBJECT);
        assertTrue(json instanceof JsonObject);
        json = Bison.jsonify("{}");
        assertTrue(json instanceof JsonObject);
        json = Bison.jsonify(JsonValue.OBJECT);
        assertTrue(json.append("name:true").find("name") instanceof JsonPair);
    }

    /**
     * 
     */
    @Test
    public void testFind() {
        json.append("name:true");
        assertTrue(json.find("name") instanceof JsonPair);
    }

    /**
     * 
     */
    @Test
    public void testAppend() {
        json.append("name:true");
        assertTrue(json.find("name") instanceof JsonPair);
        assertTrue(json.find("name").value() instanceof com.bingzer.bison.JsonBoolean);
    }

    /**
     * 
     */
    @Test
    public void testAppendMultiple() {
        json.append("name1:true", "name2:2", "name3:'string'");
        assertTrue(json.find("name1") instanceof JsonPair);
        assertTrue(json.find("name1").toString().equals("\"name1\":true"));
        assertTrue(json.find("name2") instanceof JsonPair);
        assertTrue(json.find("name2").toString().equals("\"name2\":2"));
        assertTrue(json.find("name3") instanceof JsonPair);
        assertTrue(json.find("name3").toString().equals("\"name3\":\"string\""));
    }

    /**
     * 
     */
    @Test
    public void testPair() {
        json.pair("namePair1", false);
        assertTrue(!json.find("namePair1").valueAs(JsonBoolean.class).value());
    }

    /**
     * 
     */
    @Test
    public void testParse() {
        json = Bison.jsonify(JsonValue.OBJECT);
        json.append("namePair1:true");
        json.append("namePair2:false");
        assertTrue(json.as(JsonObject.class).length() == 2);
        json.as(JsonValue.class).parse("{}");
        assertTrue(json.as(JsonObject.class).length() == 0);
        json.as(JsonValue.class).parse("{name:true,name2:null}");
        assertTrue(json.as(JsonObject.class).length() == 2);
        assertTrue(json.find("name").valueAs(JsonBoolean.class).value());
    }

    /**
     * 
     */
    @Test
    public void testLength() {
        json = Bison.jsonify("{name0:'string'}");
        json.append("name1:true");
        json.append("name2:false");
        assertTrue(json.as(JsonObject.class).length() == 3);
    }

    /**
     * 
     */
    @Test
    public void testValue() {
        json = Bison.jsonify("{name:true,x:'y'}");
        assertTrue(json.as(JsonObject.class).value().size() == 2);
    }

    /**
     * 
     */
    @Test
    public void testToString() {
        json = Bison.jsonify("{name:true,x:'y'}");
        assertTrue(json.toString().equals("{\"name\":true,\"x\":\"y\"}"));
    }
}
