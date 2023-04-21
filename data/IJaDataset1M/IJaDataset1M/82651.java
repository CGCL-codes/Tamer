package nl.hoepsch.json.object;

import static org.testng.Assert.*;
import nl.hoepsch.json.JSONException;
import nl.hoepsch.json.JSONObject;
import org.testng.annotations.Test;

public class JSONObjectFaultyMemberTest extends BaseJSONTest {

    @Test
    public void testFaultyMember() {
        String source;
        @SuppressWarnings("unused") JSONObject jsonObject;
        try {
            source = "{ bla: \"some value\"}";
            jsonObject = new JSONObject(source);
            fail("Expected JSON Exception");
        } catch (JSONException e) {
        }
        try {
            source = "{ \"bla: \"some value\"}";
            jsonObject = new JSONObject(source);
            fail("Expected JSON Exception");
        } catch (JSONException e) {
        }
        try {
            source = "{\"foo\": }";
            jsonObject = new JSONObject(source);
            fail("Expected JSON Exception");
        } catch (JSONException e) {
        }
        try {
            source = "{\"foo\": false,  }";
            jsonObject = new JSONObject(source);
            fail("Expected JSON Exception");
        } catch (JSONException e) {
        }
        try {
            source = "{\"foo\": { }";
            jsonObject = new JSONObject(source);
            fail("Expected JSON Exception");
        } catch (JSONException e) {
        }
    }

    @Test
    public void testFaultyContents() {
        String source;
        @SuppressWarnings("unused") JSONObject jsonObject;
        try {
            source = "{ \"bla\": \"some value}";
            jsonObject = new JSONObject(source);
            fail("Expected JSON Exception");
        } catch (JSONException e) {
        }
        try {
            source = "{ \"bla\": \"some value, \"foo\":true}";
            jsonObject = new JSONObject(source);
            fail("Expected JSON Exception");
        } catch (JSONException e) {
        }
        try {
            source = "{\"foo\": flase  }";
            jsonObject = new JSONObject(source);
            fail("Expected JSON Exception");
        } catch (JSONException e) {
        }
        try {
            source = "{\"foo\": treu  }";
            jsonObject = new JSONObject(source);
            fail("Expected JSON Exception");
        } catch (JSONException e) {
        }
        try {
            source = "{\"foo\": nllu}";
            jsonObject = new JSONObject(source);
            fail("Expected JSON Exception");
        } catch (JSONException e) {
        }
    }
}
