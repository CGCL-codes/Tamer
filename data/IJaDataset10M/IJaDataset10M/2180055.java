package com.jbergin;

import com.gargoylesoftware.htmlunit.MockWebConnection;
import java.net.URL;

public class TypesTest extends HtmlFixtureTestCase {

    private static final String html = "<html id=\"htmlTag\">" + " <head id=\"headTag\">" + "   <title id=\"titleTag\">This is the Title</title>" + " </head>" + "<body id=\"bodyTag\">" + "  <div id=\"mydiv1\"  >first div" + "   <div id=\"mysubdiv1\"  >subdiv</div>" + "  </div>" + "  <div id=\"mydiv2\"  >second div</div>" + "  <p id=\"p1\">first p</div>" + "  <div id=\"mydiv3\"  >third div</div>" + "  <p id=\"p2\">second p</div>" + "</body>" + "</html>";

    public TypesTest(String name) {
        super(name);
    }

    void makeMockWebConnection() throws Exception {
        mockWebConnection = new MockWebConnection();
        mockWebConnection.setResponse(new URL("http://first/"), html);
    }

    public void testTypes() throws Exception {
        prepareTest("http://first", "<td>Types</td><td>div</td><td>4</td>", standardSetup);
        assertRight(getCellByRowCol(3, 3));
    }

    public void testTypesQuery() throws Exception {
        prepareTest("http://first", "<td>Types</td><td>div</td><td></td>", standardSetup);
        assertIgnored(getCellByRowCol(3, 3));
        assertEquals("4", getCellByRowCol(3, 3).text());
    }

    public void testChildTypesFail() throws Exception {
        prepareTest("http://first", "<td>Child Types</td><td>div</td><td>4</td>", standardSetup);
        assertWrong(getCellByRowCol(3, 3));
    }

    public void testChildTypes() throws Exception {
        prepareTest("http://first", "<td>Type Focus</td><td>body</td></tr>" + "<tr><td>Child Types</td><td>div</td><td>3</td>", standardSetup);
        assertRight(getCellByRowCol(3, 2));
        assertRight(getCellByRowCol(4, 3));
    }
}
