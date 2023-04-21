package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import htroot.json.JsonTest;
import org.junit.Test;
import tests.utils.AbstractServerTest;

public class JsonTester extends AbstractServerTest {

    @Test
    public void test() throws Exception {
        String jsonRequest = new String(getRequest("/json/JsonTest"));
        logger.debug("Got: " + jsonRequest);
        assertTrue(jsonRequest.contains("66"));
        assertTrue(jsonRequest.contains("99"));
        String postData = "{ myInt: 33, myInt2: 77}";
        String afterPostData = new String(postRequest("/json/JsonTest", postData));
        assertEquals(JsonTest.myStaticInt, 33);
        assertEquals(JsonTest.myStaticInt2, 77);
        logger.debug("Got: " + afterPostData);
        assertTrue(afterPostData.contains("66"));
        assertTrue(afterPostData.contains("99"));
    }
}
