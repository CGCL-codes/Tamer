package org.apache.shindig.server.endtoend;

import org.apache.shindig.auth.BasicSecurityToken;
import org.apache.shindig.auth.BasicSecurityTokenDecoder;
import org.apache.shindig.auth.SecurityToken;
import org.apache.shindig.common.JsonAssert;
import org.apache.shindig.common.crypto.BlobCrypterException;
import com.gargoylesoftware.htmlunit.CollectingAlertHandler;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HTMLParserListener;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;

/**
 * Base class for end-to-end tests.
 */
public class EndToEndTest {

    private static final String[] EXPECTED_RESOURCES = { "fetchPersonTest.xml", "fetchPeopleTest.xml", "errorTest.xml", "cajaTest.xml", "failCajaTest.xml", "osapi/personTest.xml", "osapi/peopleTest.xml", "osapi/activitiesTest.xml", "osapi/appdataTest.xml", "osapi/batchTest.xml", "testframework.js" };

    private static EndToEndServer server = null;

    private WebClient webClient;

    private CollectingAlertHandler alertHandler;

    private SecurityToken token;

    private String language;

    @Test
    public void checkResources() throws Exception {
        for (String resource : EXPECTED_RESOURCES) {
            String url = EndToEndServer.SERVER_URL + '/' + resource;
            Page p = webClient.getPage(url);
            assertEquals("Failed to load test resource " + url, 200, p.getWebResponse().getStatusCode());
        }
    }

    @Test
    public void fetchPerson() throws Exception {
        executeAllPageTests("fetchPersonTest");
    }

    @Test
    public void fetchPeople() throws Exception {
        executeAllPageTests("fetchPeopleTest");
    }

    @Test
    public void messageBundles() throws Exception {
        executeAllPageTests("messageBundle");
    }

    @Test
    public void caja() throws Exception {
        executeAllPageTests("cajaTest.xml");
    }

    @Test
    public void testMakeRequest() throws Exception {
        executeAllPageTests("makeRequestTest");
    }

    @Test
    public void messageBundlesRtl() throws Exception {
        language = "ar";
        executeAllPageTests("messageBundle");
    }

    @Test
    public void notFoundError() throws Exception {
        server.setDataServiceError(HttpServletResponse.SC_NOT_FOUND, "Not Found");
        executePageTest("errorTest", "notFoundError");
    }

    @Test
    public void badRequest() throws Exception {
        server.setDataServiceError(HttpServletResponse.SC_BAD_REQUEST, "Bad Request");
        executePageTest("errorTest", "badRequestError");
    }

    @Test
    public void internalError() throws Exception {
        server.setDataServiceError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Server Error");
        executePageTest("errorTest", "internalError");
    }

    @Test
    public void testTemplates() throws Exception {
        executeAllPageTests("opensocial-templates/ost_test");
    }

    @Test
    @Ignore("per jasvir, this test is failing with webclient for an unknown reason")
    public void testFailCaja() throws Exception {
        HtmlPage page = executePageTest("failCajaTest", null);
        NodeList bodyList = page.getElementsByTagName("body");
        assertEquals(bodyList.getLength(), 1);
        DomNode body = (DomNode) bodyList.item(0);
        assertEquals(body.getChildNodes().getLength(), 2);
        assertEquals(body.getFirstChild().getNodeName(), "pre");
        assertEquals(body.getLastChild().getNodeName(), "script");
    }

    @Test
    public void testPipelining() throws Exception {
        HtmlPage page = executePageTest("pipeliningTest", null);
        JSONArray array = new JSONArray(page.asText());
        assertEquals(3, array.length());
        Map<String, JSONObject> jsonObjects = Maps.newHashMap();
        for (int i = 0; i < array.length(); i++) {
            JSONObject jsonObj = array.getJSONObject(i);
            assertTrue(jsonObj.has("id"));
            jsonObjects.put(jsonObj.getString("id"), jsonObj);
        }
        JSONObject me = jsonObjects.get("me").getJSONObject("data");
        assertEquals("Digg", me.getJSONObject("name").getString("familyName"));
        JSONObject json = jsonObjects.get("json").getJSONObject("data");
        JSONObject expected = new JSONObject("{content: {key: 'value'}, status: 200}");
        JsonAssert.assertJsonObjectEquals(expected, json);
        JsonAssert.assertObjectEquals("{id: 'var', data: 'value'}", jsonObjects.get("var"));
    }

    @Test
    public void testOsapiPeople() throws Exception {
        executeAllPageTests("osapi/peopleTest");
    }

    @Test
    public void testOsapiPerson() throws Exception {
        executeAllPageTests("osapi/personTest");
    }

    @Test
    public void testOsapiActivities() throws Exception {
        executeAllPageTests("osapi/activitiesTest");
    }

    @Test
    public void testOsapiAppdata() throws Exception {
        executeAllPageTests("osapi/appdataTest");
    }

    @Test
    public void testOsapiBatch() throws Exception {
        executeAllPageTests("osapi/batchTest");
    }

    @Test
    public void testTemplateRewrite() throws Exception {
        HtmlPage page = executePageTest("templateRewriter", null);
        Element attrs = page.getElementById("attrs");
        List<Element> attrsList = getChildrenByTagName(attrs, "li");
        assertEquals(3, attrsList.size());
        Element element = page.getElementById("id0");
        assertNotNull(element);
        assertEquals("Jane", element.getTextContent().trim());
        element = page.getElementById("id2");
        assertNotNull(element);
        assertEquals("Maija", element.getTextContent().trim());
        Element repeat = page.getElementById("repeatTag");
        List<Element> repeatList = getChildrenByTagName(repeat, "li");
        assertEquals(1, repeatList.size());
        assertEquals("George", repeatList.get(0).getTextContent().trim());
        Element ifTag = page.getElementById("ifTag");
        List<Element> ifList = getChildrenByTagName(ifTag, "li");
        assertEquals(3, ifList.size());
        assertEquals(1, page.getElementsByTagName("b").getLength());
        assertEquals(1, getChildrenByTagName(ifList.get(2), "b").size());
        Element jsonPipeline = page.getElementById("json");
        assertEquals("value", jsonPipeline.getTextContent().trim());
        Element textPipeline = page.getElementById("text");
        assertEquals("{\"key\": \"value\"}", textPipeline.getTextContent().trim());
        Element oncreateSpan = page.getElementById("mutate");
        assertEquals("mutated", oncreateSpan.getTextContent().trim());
        assertEquals("45", page.getElementById("sum").getTextContent().trim());
        assertEquals("25", page.getElementById("max").getTextContent().trim());
    }

    @Test
    public void testTemplateLibrary() throws Exception {
        HtmlPage page = executeAllPageTests("templateLibrary");
        assertTrue(page.asXml().contains("p {color: red}"));
        Node paragraph = page.getElementsByTagName("p").item(0);
        assertEquals("Hello world", paragraph.getTextContent().trim());
    }

    private List<Element> getChildrenByTagName(Element parent, String name) {
        List<Element> elements = Lists.newArrayList();
        for (Node child = parent.getFirstChild(); child != null; child = child.getNextSibling()) {
            if (child instanceof Element && name.equals(child.getNodeName())) {
                elements.add((Element) child);
            }
        }
        return elements;
    }

    @BeforeClass
    public static void setUpOnce() throws Exception {
        server = new EndToEndServer();
        server.start();
    }

    @AfterClass
    public static void tearDownOnce() throws Exception {
        server.stop();
    }

    @Before
    public void setUp() throws Exception {
        webClient = new WebClient();
        webClient.setAjaxController(new NicelyResynchronizingAjaxController());
        webClient.setHTMLParserListener(HTMLParserListener.LOG_REPORTER);
        alertHandler = new CollectingAlertHandler();
        webClient.setAlertHandler(alertHandler);
        token = createToken("canonical", "john.doe");
        language = null;
    }

    @After
    public void tearDown() {
        server.clearDataServiceError();
    }

    /**
   * Verify that the Javascript completed running.  This ensures that
   * logic errors or exceptions don't get treated as success.
   */
    @After
    public void verifyTestsFinished() {
        String testMethod = null;
        for (String alert : alertHandler.getCollectedAlerts()) {
            if (testMethod == null) {
                assertFalse("Test method omitted", "FINISHED".equals(alert));
                testMethod = alert;
            } else {
                assertEquals("test method " + testMethod + " did not finish", "FINISHED", alert);
                testMethod = null;
            }
        }
        assertNull("test method " + testMethod + " did not finish", testMethod);
    }

    /**
   * Executes a page test by loading the HTML page.
   * @param testName name of the test, which must match a gadget XML file
   *     name in test/resources/endtoend (minus .xml).
   * @param testMethod name of the javascript method to execute
   * @return the parsed HTML page
   */
    private HtmlPage executePageTest(String testName, String testMethod) throws IOException {
        String gadgetUrl = EndToEndServer.SERVER_URL + '/' + testName + ".xml";
        String url = EndToEndServer.GADGET_BASEURL + "?url=" + URLEncoder.encode(gadgetUrl, "UTF-8");
        BasicSecurityTokenDecoder decoder = new BasicSecurityTokenDecoder();
        url += "&st=" + URLEncoder.encode(decoder.encodeToken(token), "UTF-8");
        if (testMethod != null) {
            url += "&testMethod=" + URLEncoder.encode(testMethod, "UTF-8");
        }
        url += "&nocache=1";
        if (language != null) {
            url += "&lang=" + language;
        }
        Page page = webClient.getPage(url);
        if (!(page instanceof HtmlPage)) {
            fail("Got wrong page type. Was: " + page.getWebResponse().getContentType());
        }
        return (HtmlPage) page;
    }

    /**
   * Executes all page test in a single XML file.
   * @param testName name of the test, which must match a gadget XML file
   *     name in test/resources/endtoend (minus .xml).
   * @throws IOException
   */
    private HtmlPage executeAllPageTests(String testName) throws IOException {
        return executePageTest(testName, "all");
    }

    private BasicSecurityToken createToken(String owner, String viewer) throws BlobCrypterException {
        return new BasicSecurityToken(owner, viewer, "test", "domain", "appUrl", "1", "default", null);
    }
}
