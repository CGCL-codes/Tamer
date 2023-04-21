package com.googlecode.janrain4j.api.engage.response;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.Map;
import org.junit.Test;
import com.googlecode.janrain4j.json.JSONObject;

public class AllMappingsResponseTest {

    private String json = null;

    private AllMappingsResponse response = null;

    @Test
    public void testMultipleMappings() throws Exception {
        json = "{\n" + "  \"mappings\": {\n" + "    \"1\": [\n" + "      \"http:\\/\\/cygnus.myopenid.com\\/\"\n" + "    ],\n" + "    \"2\": [\n" + "      \"http:\\/\\/brianellin.com\\/\",\n" + "      \"http:\\/\\/brian.myopenid.com\\/\"\n" + "    ]\n" + "  },\n" + "  \"stat\": \"ok\"\n" + "}";
        response = new AllMappingsResponse(json);
        assertEquals(json, response.getResponseAsJSON());
        assertEquals(new JSONObject(json).toString(), response.getResponseAsJSONObject().toString());
        Map<String, List<String>> allMappings = response.getAllMappings();
        assertEquals(2, allMappings.size());
        assertTrue(allMappings.containsKey("1"));
        assertEquals(1, allMappings.get("1").size());
        assertTrue(allMappings.get("1").contains("http://cygnus.myopenid.com/"));
        assertTrue(allMappings.containsKey("2"));
        assertEquals(2, allMappings.get("2").size());
        assertTrue(allMappings.get("2").contains("http://brianellin.com/"));
        assertTrue(allMappings.get("2").contains("http://brian.myopenid.com/"));
    }

    @Test
    public void testSingleMapping() throws Exception {
        json = "{\n" + "  \"mappings\": {\n" + "    \"1\": [\n" + "      \"http:\\/\\/cygnus.myopenid.com\\/\"\n" + "    ]\n" + "  },\n" + "  \"stat\": \"ok\"\n" + "}";
        response = new AllMappingsResponse(json);
        assertEquals(json, response.getResponseAsJSON());
        assertEquals(new JSONObject(json).toString(), response.getResponseAsJSONObject().toString());
        Map<String, List<String>> allMappings = response.getAllMappings();
        assertEquals(1, allMappings.size());
        assertTrue(allMappings.containsKey("1"));
        assertEquals(1, allMappings.get("1").size());
        assertTrue(allMappings.get("1").contains("http://cygnus.myopenid.com/"));
    }

    @Test
    public void testNoMappings() throws Exception {
        json = "{\n" + "  \"mappings\": {\n" + "  },\n" + "  \"stat\": \"ok\"\n" + "}";
        response = new AllMappingsResponse(json);
        assertEquals(json, response.getResponseAsJSON());
        assertEquals(new JSONObject(json).toString(), response.getResponseAsJSONObject().toString());
        Map<String, List<String>> allMappings = response.getAllMappings();
        assertEquals(0, allMappings.size());
    }

    @Test
    public void testSerializable() throws Exception {
        json = "{\n" + "  \"mappings\": {\n" + "    \"1\": [\n" + "      \"http:\\/\\/cygnus.myopenid.com\\/\"\n" + "    ],\n" + "    \"2\": [\n" + "      \"http:\\/\\/brianellin.com\\/\",\n" + "      \"http:\\/\\/brian.myopenid.com\\/\"\n" + "    ]\n" + "  },\n" + "  \"stat\": \"ok\"\n" + "}";
        response = new AllMappingsResponse(json);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(out);
        oos.writeObject(response);
        oos.close();
        assertTrue(out.toByteArray().length > 0);
        assertNotNull(response.getResponseAsJSONObject());
    }
}
