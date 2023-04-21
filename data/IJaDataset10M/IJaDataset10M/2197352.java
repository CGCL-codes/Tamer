package com.jbergin;

import com.gargoylesoftware.htmlunit.MockWebConnection;
import java.net.URL;

public class JavascriptTest extends HtmlFixtureTestCase {

    private static final String html = "<html id=\"htmlTag\">" + " <head id=\"headTag\">" + "   <title id=\"titleTag\">This is the Title</title>" + "   <script>" + "     function processClick()" + "     {" + "       document.f.t.value = \"button clicked\";" + "     }" + "   </script>" + " </head>" + "<body id=\"bodyTag\">" + " This is the body" + "<form name=\"f\">" + "  <input type=\"button\" name=\"b\" value=\"Click\" onclick=\"processClick()\"/>" + "  <input type=\"text\" cols=\"30\" name=\"t\" value=\"initial\"/>" + "</form>" + "</body>" + "</html>";

    private static final String htmlWithError = "<html id=\"htmlTag\">" + " <head id=\"headTag\">" + "   <title id=\"titleTag\">This is the Title</title>" + "   <script>" + "     function processClick()" + "     {" + "       document.f.doesnotexist.value = \"button clicked\";" + "     }" + "   </script>" + " </head>" + "<body id=\"bodyTag\">" + " This is the body" + "<form name=\"f\">" + "  <input type=\"button\" name=\"b\" value=\"Click\" onclick=\"processClick()\"/>" + "  <input type=\"text\" cols=\"30\" name=\"t\" value=\"initial\"/>" + "</form>" + "</body>" + "</html>";

    private static final String htmlAlert = "<html id=\"htmlTag\">" + " <head id=\"headTag\">" + "   <title id=\"titleTag\">This is the Title</title>" + "   <script>" + "     function processClick()" + "     {" + "       alert(\"button clicked\");" + "     }" + "   </script>" + " </head>" + "<body id=\"bodyTag\">" + " This is the body" + "<form name=\"f\">" + "  <input type=\"button\" name=\"b\" value=\"Click\" onclick=\"processClick()\"/>" + "</form>" + "</body>" + "</html>";

    private static final String htmlNoAlert = "<html id=\"htmlTag\">" + " <head id=\"headTag\">" + "   <title id=\"titleTag\">This is the Title</title>" + "   <script>" + "     function processClick()" + "     {" + "       return;" + "     }" + "   </script>" + " </head>" + "<body id=\"bodyTag\">" + " This is the body" + "<form name=\"f\">" + "  <input type=\"button\" name=\"b\" value=\"Click\" onclick=\"processClick()\"/>" + "</form>" + "</body>" + "</html>";

    public JavascriptTest(String name) {
        super(name);
    }

    void makeMockWebConnection() throws Exception {
        mockWebConnection = new MockWebConnection();
        mockWebConnection.setResponse(new URL("http://first/"), html);
        mockWebConnection.setResponse(new URL("http://second/"), htmlWithError);
        mockWebConnection.setResponse(new URL("http://third/"), htmlAlert);
        mockWebConnection.setResponse(new URL("http://fourth/"), htmlNoAlert);
    }

    public void testExecuteJavascript() throws Exception {
        String cells = "<td>Escape HTML</td><td>off</td></tr>" + "<tr><td>Element Focus</td><td>t</td><td>input</td></tr>" + "<tr><td>Text</td><td>initial</td></tr>" + "<tr><td>Focus</td><td>thePage</td></tr>" + "<tr><td>Element Focus</td><td>b</td><td>input</td></tr>" + "<tr><td>Click</td><td>thePage</td><td></td></tr>" + "<tr><td>Element Focus</td><td>t</td><td>input</td></tr>" + "<tr><td>Text</td><td>button clicked</td></tr>";
        prepareTest("http://first", cells, standardSetup);
        assertRight(getCellByRowCol(4, 2));
        assertRight(getCellByRowCol(5, 2));
        assertRight(getCellByRowCol(6, 2));
        assertRight(getCellByRowCol(7, 2));
        assertRight(getCellByRowCol(8, 1));
        assertRight(getCellByRowCol(9, 2));
        assertRight(getCellByRowCol(10, 2));
    }

    public void testExecuteJavascriptOff() throws Exception {
        String cells = "<td>Escape HTML</td><td>off</td></tr>" + "<tr><td>Javascript</td><td>off</td></tr>" + "<tr><td>Element Focus</td><td>t</td><td>input</td></tr>" + "<tr><td>Text</td><td>initial</td></tr>" + "<tr><td>Focus</td><td>thePage</td></tr>" + "<tr><td>Element Focus</td><td>b</td><td>input</td></tr>" + "<tr><td>Click</td><td>thePage</td><td></td></tr>" + "<tr><td>Element Focus</td><td>t</td><td>input</td></tr>" + "<tr><td>Text</td><td>initial</td></tr>";
        prepareTest("http://first", cells, standardSetup);
        assertRight(getCellByRowCol(5, 2));
        assertRight(getCellByRowCol(6, 2));
        assertRight(getCellByRowCol(7, 2));
        assertRight(getCellByRowCol(8, 2));
        assertRight(getCellByRowCol(9, 1));
        assertRight(getCellByRowCol(10, 2));
        assertRight(getCellByRowCol(11, 2));
    }

    public void testShowJavascriptException() throws Exception {
        String cells = "<td>Escape HTML</td><td>off</td></tr>" + "<tr><td>Element Focus</td><td>t</td><td>input</td></tr>" + "<tr><td>Text</td><td>initial</td></tr>" + "<tr><td>Focus</td><td>thePage</td></tr>" + "<tr><td>Element Focus</td><td>b</td><td>input</td></tr>" + "<tr><td>Click</td><td>thePage</td><td></td></tr>";
        prepareTest("http://second", cells, standardSetup);
        assertRight(getCellByRowCol(4, 2));
        assertRight(getCellByRowCol(5, 2));
        assertRight(getCellByRowCol(6, 2));
        assertRight(getCellByRowCol(7, 2));
        assertError(getCellByRowCol(8, 1));
    }

    public void testIgnoreJavascriptException() throws Exception {
        String cells = "<td>Escape HTML</td><td>off</td></tr>" + "<tr><td>Show Javascript Errors</td><td>off</td></tr>" + "<tr><td>Element Focus</td><td>t</td><td>input</td></tr>" + "<tr><td>Text</td><td>initial</td></tr>" + "<tr><td>Focus</td><td>thePage</td></tr>" + "<tr><td>Element Focus</td><td>b</td><td>input</td></tr>" + "<tr><td>Click</td><td>thePage</td><td></td></tr>" + "<tr><td>Element Focus</td><td>t</td><td>input</td></tr>" + "<tr><td>Text</td><td>initial</td></tr>";
        prepareTest("http://second", cells, standardSetup);
        assertRight(getCellByRowCol(5, 2));
        assertRight(getCellByRowCol(6, 2));
        assertRight(getCellByRowCol(7, 2));
        assertRight(getCellByRowCol(8, 2));
        assertRight(getCellByRowCol(9, 1));
        assertRight(getCellByRowCol(10, 2));
        assertRight(getCellByRowCol(11, 2));
    }

    public void testJavascriptMessage() throws Exception {
        String cells = "<td>Escape HTML</td><td>off</td></tr>" + "<tr><td>Focus</td><td>thePage</td></tr>" + "<tr><td>Element Focus</td><td>b</td><td>input</td></tr>" + "<tr><td>Click</td><td>thePage</td><td></td></tr>" + "<tr><td>Javascript Message</td><td>button clicked</td></tr>";
        prepareTest("http://third", cells, standardSetup);
        assertRight(getCellByRowCol(5, 2));
        assertRight(getCellByRowCol(6, 1));
        assertRight(getCellByRowCol(7, 2));
    }

    public void testJavascriptMessageFail() throws Exception {
        String cells = "<td>Escape HTML</td><td>off</td></tr>" + "<tr><td>Focus</td><td>thePage</td></tr>" + "<tr><td>Element Focus</td><td>b</td><td>input</td></tr>" + "<tr><td>Click</td><td>thePage</td><td></td></tr>" + "<tr><td>Javascript Message</td><td>foo</td></tr>";
        prepareTest("http://third", cells, standardSetup);
        assertRight(getCellByRowCol(5, 2));
        assertRight(getCellByRowCol(6, 1));
        assertWrong(getCellByRowCol(7, 2));
    }

    public void testJavascriptMessageFail2() throws Exception {
        String cells = "<td>Escape HTML</td><td>off</td></tr>" + "<tr><td>Focus</td><td>thePage</td></tr>" + "<tr><td>Element Focus</td><td>b</td><td>input</td></tr>" + "<tr><td>Click</td><td>thePage</td><td></td></tr>" + "<tr><td>Javascript Message</td><td>foo</td></tr>";
        prepareTest("http://fourth", cells, standardSetup);
        assertRight(getCellByRowCol(5, 2));
        assertRight(getCellByRowCol(6, 1));
        assertWrong(getCellByRowCol(7, 2));
    }

    public void testJavascriptMessageBlank() throws Exception {
        String cells = "<td>Escape HTML</td><td>off</td></tr>" + "<tr><td>Focus</td><td>thePage</td></tr>" + "<tr><td>Element Focus</td><td>b</td><td>input</td></tr>" + "<tr><td>Click</td><td>thePage</td><td></td></tr>" + "<tr><td>Javascript Message</td><td>$blank$</td></tr>";
        prepareTest("http://fourth", cells, standardSetup);
        assertRight(getCellByRowCol(5, 2));
        assertRight(getCellByRowCol(6, 1));
        assertRight(getCellByRowCol(7, 2));
    }

    public void testJavascriptMessageBlankFail() throws Exception {
        String cells = "<td>Escape HTML</td><td>off</td></tr>" + "<tr><td>Focus</td><td>thePage</td></tr>" + "<tr><td>Element Focus</td><td>b</td><td>input</td></tr>" + "<tr><td>Click</td><td>thePage</td><td></td></tr>" + "<tr><td>Javascript Message</td><td>$blank$</td></tr>";
        prepareTest("http://third", cells, standardSetup);
        assertRight(getCellByRowCol(5, 2));
        assertRight(getCellByRowCol(6, 1));
        assertWrong(getCellByRowCol(7, 2));
    }

    public void testSetScriptPreProcessor() throws Exception {
        String cells = "<td>Escape HTML</td><td>off</td></tr>" + "<tr><td>Focus</td><td>thePage</td></tr>" + "<tr><td>Element Focus</td><td>b</td><td>input</td></tr>" + "<tr><td>Click</td><td>thePage</td><td></td></tr>" + "<tr><td>Javascript Message</td><td>button clicked with preprocessed script</td></tr>" + "<tr><td>Set Script Preprocessor</td><td>com.jbergin.NullScriptPreProcessor</td></tr>" + "<tr><td>Focus</td><td>http://third</td></tr>" + "<tr><td>Element Focus</td><td>b</td><td>input</td></tr>" + "<tr><td>Click</td><td>thePage</td><td></td></tr>" + "<tr><td>Javascript Message</td><td>button clicked</td></tr>";
        prepareTest("http://third", cells, "<td>Set Script Preprocessor</td><td>com.jbergin.UnitTestScriptPreProcessor</td>");
        assertRight(getCellByRowCol(5, 2));
        assertRight(getCellByRowCol(6, 1));
        assertRight(getCellByRowCol(7, 2));
        assertRight(getCellByRowCol(12, 2));
    }
}
