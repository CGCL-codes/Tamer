package nl.hoepsch.json.object;

import static org.testng.Assert.*;
import nl.hoepsch.json.JSONArray;
import nl.hoepsch.json.JSONException;
import nl.hoepsch.json.JSONObject;
import org.testng.annotations.Test;

public class JSONObjectArrayMemberTest {

    @Test
    public void testMembers() throws JSONException {
        String source = "{\"foo\": [] }";
        JSONObject jsonObject = new JSONObject(source);
        assertEquals(jsonObject.size(), 1);
        assertTrue(jsonObject.contains("foo"));
        JSONArray embedded = jsonObject.getJSONArray("foo");
        assertNotNull(embedded);
        assertEquals(embedded.size(), 0);
        source = "{\"foo\": [true, false, null, \"\"] }";
        jsonObject = new JSONObject(source);
        assertEquals(jsonObject.size(), 1);
        assertTrue(jsonObject.contains("foo"));
        embedded = jsonObject.getJSONArray("foo");
        assertNotNull(embedded);
        assertEquals(embedded.size(), 4);
        assertTrue(embedded.getBoolean(0));
        assertFalse(embedded.getBoolean(1));
        jsonObject = new JSONObject();
        jsonObject.add("foo", embedded);
        assertEquals(jsonObject.size(), 1);
        assertTrue(jsonObject.contains("foo"));
        embedded = jsonObject.getJSONArray("foo");
        assertNotNull(embedded);
        assertEquals(embedded.size(), 4);
        assertTrue(embedded.getBoolean(0));
        assertFalse(embedded.getBoolean(1));
    }

    @Test
    public void testToString() throws JSONException {
        String source = "{\"foo\": [] }";
        JSONObject jsonObject = new JSONObject(source);
        assertEquals(jsonObject.toString(), "{\"foo\": []}");
        assertEquals(jsonObject.toString(2), "{\n  \"foo\": []\n}");
        source = "{\"foo\": [true, false, null, \"\"] }";
        jsonObject = new JSONObject(source);
        assertEquals(jsonObject.toString(), "{\"foo\": [true, false, null, \"\"]}");
        assertEquals(jsonObject.toString(2), "{\n  \"foo\": [\n    true, \n    false, \n    null, \n    \"\"\n  ]\n}");
    }
}
