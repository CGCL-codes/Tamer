package com.volantis.mcs.protocols.html.htmlversion3_2;

import com.volantis.mcs.dom.Document;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.dom.debug.DOMUtilities;
import com.volantis.mcs.protocols.ProtocolConfigurationImpl;
import com.volantis.mcs.protocols.html.HTMLVersion3_2Configuration;
import com.volantis.mcs.protocols.trans.StyleEmulationVisitor;
import com.volantis.mcs.protocols.trans.StyleEmulationVisitorTestAbstract;
import org.apache.log4j.Category;
import org.xml.sax.XMLReader;

/**
 * A test case for {@link StyleEmulationVisitor} when configured for HTML 3.2
 * processing.
 */
public class HTML3_2StyleEmulationVisitorTestCase extends StyleEmulationVisitorTestAbstract {

    protected void setUp() throws Exception {
        Category.getRoot().removeAllAppenders();
        super.setUp();
        enableLog4jDebug();
    }

    protected ProtocolConfigurationImpl getProtocolConfiguration() {
        return new HTMLVersion3_2Configuration();
    }

    protected String getParagraphElement() {
        return "p";
    }

    protected String getExpectedPushMatchingParentElementDownSimple() {
        final String expected = "<b>textA</b>" + "<a>" + "<b>" + "txt-1" + "<ANTI-B>txt-3</ANTI-B>" + "txt-2" + "</b>" + "</a>" + "<b>textE</b>";
        return expected;
    }

    protected String getExpectedPushMatchingParentElementDown() {
        final String expected = "<i>" + "<b>textA</b>" + "<a>" + "<b>" + "txt-1" + "<ANTI-B>txt-3</ANTI-B>" + "txt-2" + "</b>" + "</a>" + "<b>textE</b>" + "</i>";
        return expected;
    }

    protected String getExpectedAtomicity() {
        String expected = "<b>" + "Block text before" + "</b>" + "<a href=\"...\">" + "<b>" + "Bold link text 1" + "</b>" + "Normal link Text" + "<b>" + "Bold link text 2" + "</b>" + "</a>" + "<b>" + "Bold text after" + "</b>";
        return expected;
    }

    protected String getExpectedAtomicityNested() {
        String expected = "<i>" + "<b>" + "Block text before" + "</b>" + "<a href=\"...\">" + "<b>" + "Bold link text 1" + "</b>" + "Normal link Text" + "<b>" + "Bold link text 2" + "</b>" + "</a>" + "<b>" + "Bold text after" + "</b>" + "</i>";
        return expected;
    }

    /**
     * Create expected results for {@link #testTransformAtomicity}.
     */
    protected String getExpectedTransformList() {
        String expected = "<b>" + "Text Before" + "</b>" + "<ol>" + "<li>" + "<b>List Item 1</b>" + "</li>" + "<li>" + "<b>List Item 2</b>" + "</li>" + "</ol>" + "<b>" + "Text After" + "</b>";
        return expected;
    }

    protected String getExpectedTransformSimpleContainment() {
        String expected = "<table>" + "<tr>" + "<td>" + "<b>" + "Bold" + "</b>" + "</td>" + "<td>" + "<b>" + "All Bold" + "</b>" + "</td>" + "</tr>" + "</table>";
        return expected;
    }

    /**
     * Test transformation using with nested font elements.
     * @todo all nodes are not visited.
     */
    public void testTransformNestedFonts() throws Exception {
        String input = "<table>" + "<font color=\"red\">" + "<tr>" + "<td>" + "<font family=\"arial\">" + "Red Arial" + "</font>" + "</td>" + "</tr>" + "<tr>" + "<font size=\"small\">" + "<td>" + "Red Small" + "<font color=\"blue\">" + "Blue Small" + "</font>" + "</td>" + "</font>" + "</tr>" + "</font>" + "</table>";
        String expected = "<table>" + "<tr>" + "<td>" + "<font color=\"red\" family=\"arial\">Red Arial</font>" + "</td>" + "</tr>" + "<tr>" + "<td>" + "<font color=\"red\" size=\"small\">Red Small</font>" + "<font color=\"blue\" size=\"small\">Blue Small</font>" + "</td>" + "</tr>" + "</table>";
        doTest(input, expected);
    }

    /**
     * Test transformation using with nested font elements.
     * @todo all nodes are not visited.
     */
    public void testTransformNestedEmbeddedFonts() throws Exception {
        String input = "<body>" + "<font color=\"red\" family=\"arial\">" + "X" + "<b>" + "<u>" + "<strike>" + "Y" + "<font color=\"blue\" size=\"small\">" + "small blue text" + "</font>" + "Z" + "</strike>" + "text2" + "</u>" + "text1" + "</b>" + "</font>" + "</body>";
        String expected = "<body>" + "<font color=\"red\" family=\"arial\">X</font>" + "<b>" + "<u>" + "<strike>" + "<font color=\"red\" family=\"arial\">Y</font>" + "<font color=\"blue\" family=\"arial\" size=\"small\">small blue text</font>" + "<font color=\"red\" family=\"arial\">Z</font>" + "</strike>" + "<font color=\"red\" family=\"arial\">text2</font>" + "</u>" + "<font color=\"red\" family=\"arial\">text1</font>" + "</b>" + "</body>";
        doTest(input, expected);
    }

    /**
     * Test transformation using containment inversion (complex containment)
     * Note that b,i,u elements may not contain p elements. p elements may
     * contain b,i,u elements.
     */
    public void testTransformComplexContainment() throws Exception {
        String input = "<b>" + "<p>" + "<i>" + "<u>" + "<p>" + "Alpha" + "</p>" + "</u>" + "</i>" + "</p>" + "<strike>" + "<p>" + "<i>Beta</i>" + "</p>" + "</strike>" + "</b>";
        String expected = "<p>" + "<p>" + "<b>" + "<i>" + "<u>" + "Alpha" + "</u>" + "</i>" + "</b>" + "</p>" + "</p>" + "<p>" + "<strike>" + "<b>" + "<i>Beta</i>" + "</b>" + "</strike>" + "</p>";
        doTest(input, expected);
    }

    /**
     * Test the visitor's transformation on complex input dom.
     * @todo all nodes are not visited.
     */
    public void testVisitComplex() throws Exception {
        String input = "<body>" + "<b>" + "<font size='big'>" + "<u>" + "<i>" + "big, bold, underline and italic" + "</i>" + "</u>" + "<b>" + "big, bold" + "</b>" + "<table border='1'>" + "<i>" + "<font size='big'>" + "<caption>" + "<font color='green'>" + "<ANTI-I>" + "big, bold, green caption" + "</ANTI-I>" + "</font>" + "</caption>" + "<tr debug='true'>" + "<font size='small' family='arial'>" + "<td>" + "small, arial, italic, bold (cell-1)" + "</td>" + "<td>" + "<font color='red'>" + "<u>" + "small, arial, italic, bold, red, underline (cell-2)" + "</u>" + "<ANTI-B>" + "<ANTI-I>" + "small, arial, red (cell-2)" + "</ANTI-I>" + "</ANTI-B>" + "</font>" + "</td>" + "</font>" + "</tr>" + "<tr>" + "<td colspan='2'>" + "<i>" + "<ANTI-I>" + "bold, big cell-1and2 row-2" + "</ANTI-I>" + "bold, italic, big cell-1and2 row-2" + "</i>" + "</td>" + "</tr>" + "</font>" + "</i>" + "</table>" + "</font>" + "<b>" + "<ANTI-I>" + "<i>" + "<u>" + "<ANTI-B>" + "<table border='1'>" + "<ANTI-U>" + "<tr>" + "<td>" + "<ANTI-I>" + "not italic cell-1" + "</ANTI-I>" + "</td>" + "<td>" + "<i>" + "italic cell-2" + "</i>" + "</td>" + "</tr>" + "<caption>" + "<b>" + "bold, italic caption text" + "</b>" + "</caption>" + "</ANTI-U>" + "</table>" + "italic underline (not bold)" + "</ANTI-B>" + "</u>" + "italics" + "</i>" + "not italics" + "</ANTI-I>" + "</b>" + "</b>" + "</body>";
        String expected = "<body>" + "<u>" + "<b>" + "<font size=\"big\">" + "<i>big, bold, underline and italic</i>" + "</font>" + "</b>" + "</u>" + "<font size=\"big\">" + "<b>big, bold</b>" + "</font>" + "<table border=\"1\">" + "<caption>" + "<b>" + "<font color=\"green\" size=\"big\">" + "big, bold, green caption" + "</font>" + "</b>" + "</caption>" + "<tr debug=\"true\">" + "<td>" + "<font family=\"arial\" size=\"small\">" + "<i>" + "<b>" + "small, arial, italic, bold (cell-1)" + "</b>" + "</i>" + "</font>" + "</td>" + "<td>" + "<i>" + "<b>" + "<font color=\"red\" family=\"arial\" size=\"small\">" + "<u>small, arial, italic, bold, red, underline (cell-2)</u>" + "</font>" + "</b>" + "</i>" + "<font color=\"red\" family=\"arial\" size=\"small\">" + "small, arial, red (cell-2)" + "</font>" + "</td>" + "</tr>" + "<tr>" + "<td colspan=\"2\">" + "<b>" + "<font size=\"big\">" + "bold, big cell-1and2 row-2" + "</font>" + "</b>" + "<i>" + "<b>" + "<font size=\"big\">" + "bold, italic, big cell-1and2 row-2" + "</font>" + "</b>" + "</i>" + "</td>" + "</tr>" + "</table>" + "<table border=\"1\">" + "<tr>" + "<td>not italic cell-1</td>" + "<td>" + "<i>italic cell-2</i>" + "</td>" + "</tr>" + "<caption>" + "<i>" + "<b>bold, italic caption text</b>" + "</i>" + "</caption>" + "</table>" + "<u>" + "<i>italic underline (not bold)</i>" + "</u>" + "<b>" + "<i>italics</i>not italics" + "</b>" + "</body>";
        doTest(input, expected);
    }

    /**
     * Ensure that deeply nested anti-elements are always visited/processed.
     * <p>
     * This was added for VBM:2004080905.
     */
    public void testVisitHTML3_2() throws Exception {
        String input = "<html>" + "<head>" + "<link href=\"/volantis/MCSCSS?theme=%2Fmain.mthm\" rel=\"stylesheet\" type=\"text/css\"/>" + "</head>" + "<body>" + "<a href=\"xdime_a_0.jsp\">" + "<font color=\"#999999\">" + "<ANTI-U><ANTI-STRIKE>" + "relative url" + "</ANTI-STRIKE></ANTI-U>" + "</font>" + "</a>" + "<br/>" + "<a href=\"/orangetc/xdime/portal/xdime_a_0.jsp\">" + "<font color=\"#999999\">" + "<ANTI-U><ANTI-STRIKE>" + "server relative url (same dir)" + "</ANTI-STRIKE></ANTI-U>" + "</font>" + "</a>" + "<br/>" + "<a href=\"/orangetc/xdime/catalog/xdime_page_0.jsp\">" + "<font color=\"#999999\">" + "<ANTI-U><ANTI-STRIKE>" + "server relative url (different dir)" + "</ANTI-STRIKE></ANTI-U>" + "</font>" + "</a>" + "<br/>" + "<a href=\"/orangetc/tli/ut_device.jsp\">" + "<font color=\"#999999\">" + "<ANTI-U><ANTI-STRIKE>" + "server relative url (not in service)" + "</ANTI-STRIKE></ANTI-U>" + "</font>" + "</a>" + "<br/>" + "<a href=\"http://www.google.com\">" + "<font color=\"#999999\">" + "<ANTI-U><ANTI-STRIKE>" + "absolute url" + "</ANTI-STRIKE></ANTI-U>" + "</font>" + "</a>" + "<br/>" + "</body>" + "</html>";
        String expected = "<html>" + "<head>" + "<link href=\"/volantis/MCSCSS?theme=%2Fmain.mthm\" rel=\"stylesheet\" type=\"text/css\"/>" + "</head>" + "<body>" + "<a href=\"xdime_a_0.jsp\">" + "<font color=\"#999999\">relative url</font>" + "</a>" + "<br/>" + "<a href=\"/orangetc/xdime/portal/xdime_a_0.jsp\">" + "<font color=\"#999999\">server relative url (same dir)</font>" + "</a>" + "<br/>" + "<a href=\"/orangetc/xdime/catalog/xdime_page_0.jsp\">" + "<font color=\"#999999\">server relative url (different dir)</font>" + "</a>" + "<br/>" + "<a href=\"/orangetc/tli/ut_device.jsp\">" + "<font color=\"#999999\">server relative url (not in service)</font>" + "</a>" + "<br/>" + "<a href=\"http://www.google.com\">" + "<font color=\"#999999\">absolute url</font>" + "</a>" + "<br/>" + "</body>" + "</html>";
        doTest(input, expected);
    }

    /**
     * Test the pushing of the font element into a table with 2 rows.
     */
    public void testPushFontIntoTableWith2Rows() throws Exception {
        StyleEmulationVisitor visitor = createStyleVisitor();
        String input = "<table>" + "<font color=\"red\">" + "<tr>" + "<td><font>row-1-column-1</font></td>" + "<td>row-1-column-2</td>" + "</tr>" + "<tr>" + "<td>row-2-column-1</td>" + "<td>row-2-column-2</td>" + "</tr>" + "</font>" + "</table>";
        XMLReader reader = DOMUtilities.getReader();
        Document dom = DOMUtilities.read(reader, input);
        Element element = (Element) dom.getRootElement().getHead();
        Element atomicElement = (Element) element.getHead().getNext();
        pushCounterpartElementDown(visitor, atomicElement, "font");
        String actual = DOMUtilities.toString(dom, encoder);
        String expected = "<table>" + "<tr>" + "<td>" + "<font color=\"red\">" + "<font>row-1-column-1</font>" + "</font>" + "</td>" + "<td>" + "<font color=\"red\">row-1-column-2</font>" + "</td>" + "</tr>" + "<tr>" + "<td>" + "<font color=\"red\">row-2-column-1</font>" + "</td>" + "<td>" + "<font color=\"red\">row-2-column-2</font>" + "</td>" + "</tr>" + "</table>";
        verifyDOMMatches(null, expected, actual);
    }

    /**
     * Test the pushing of style elements down (nested stylistic elements)
     */
    public void testPushStyleElementsDown() throws Exception {
        StyleEmulationVisitor visitor = createStyleVisitor();
        String input = "<table>" + "<font color=\"red\">" + "<b>" + "<tr>" + "<td>" + "<font>" + "row-1-column-1" + "</font>" + "</td>" + "<td>" + "row-1-column-2" + "</td>" + "</tr>" + "<tr>" + "<td>" + "row-2-column-1" + "</td>" + "<td>" + "row-2-column-2" + "</td>" + "</tr>" + "</b>" + "</font>" + "</table>";
        XMLReader reader = DOMUtilities.getReader();
        Document dom = DOMUtilities.read(reader, input);
        Element element = dom.getRootElement();
        pushStyleElementsDown(visitor, element);
        String actual = DOMUtilities.toString(dom, encoder);
        String expected = "<table>" + "<tr>" + "<td>" + "<font color=\"red\">" + "<b>" + "<font>row-1-column-1</font>" + "</b>" + "</font>" + "</td>" + "<td>" + "<font color=\"red\">" + "<b>" + "row-1-column-2" + "</b>" + "</font>" + "</td>" + "</tr>" + "<tr>" + "<td>" + "<font color=\"red\">" + "<b>" + "row-2-column-1" + "</b>" + "</font>" + "</td>" + "<td>" + "<font color=\"red\">" + "<b>" + "row-2-column-2" + "</b>" + "</font>" + "</td>" + "</tr>" + "</table>";
        verifyDOMMatches(null, expected, actual);
    }

    /**
     * Ensure that deeply nested anti-elements are always visited/processed.
     */
    public void testNestedStylisticElements() throws Exception {
        String input = "<html>" + "<ANTI-I>" + "<i>" + "<u>" + "<ANTI-B>" + "<table border='1'>" + "<ANTI-U>" + "<tr>" + "<td>" + "<ANTI-I>" + "not italic cell-1" + "</ANTI-I>" + "</td>" + "<td>" + "<i>italic cell-2</i>" + "</td>" + "</tr>" + "<caption>" + "<b>bold, italic caption text</b>" + "</caption>" + "</ANTI-U>" + "</table>italic underline (not bold)" + "</ANTI-B>" + "</u>italics" + "</i>not italics" + "</ANTI-I>" + "</html>";
        String expected = "<html>" + "<table border=\"1\">" + "<tr>" + "<td>not italic cell-1</td>" + "<td><i>italic cell-2</i></td>" + "</tr>" + "<caption>" + "<i>" + "<b>bold, italic caption text</b>" + "</i>" + "</caption>" + "</table>" + "<u>" + "<i>italic underline (not bold)</i>" + "</u>" + "<i>italics</i>not italics" + "</html>";
        doTest(input, expected);
    }

    /**
     * Test pushing elements down to all children.
     */
    public void testPushElementDownToAllChildren() throws Exception {
        super.testPushElementDownToAllChildren();
        String input;
        String expected;
        input = "<table>" + "<tr>" + "<td>" + "<font size='small'>" + "r1c1" + "</font>" + "</td>" + "</tr>" + "</table>";
        expected = "<table><tr><td><font color=\"red\" size=\"big\">" + "<font size=\"small\">r1c1</font></font></td></tr></table>";
        Element font = domFactory.createElement();
        font.setName("font");
        font.setAttribute("size", "big");
        font.setAttribute("color", "red");
        doTestPushElementDownToAllChildren(input, expected, font, true);
    }

    /**
     * Test the simple font (no transformation).
     */
    public void testSimpleFont() throws Exception {
        String input = "<p>" + "<font color=\"red\">" + "<a href=\"sports.jsp\">Sports News</a>" + "<br/>" + "<a href=\"games.jsp\">Fun and Games</a>" + "</font>" + "</p>";
        String expected = input;
        doTest(input, expected);
    }

    /**
     * Test the simple font (no transformation).
     */
    public void testDivisibleStyleElements() throws Exception {
        String input = "<b>" + "<sup>" + "<ANTI-B>" + "[not bold]" + "</ANTI-B>" + "[bold]" + "</sup>" + "</b>";
        String expected = "<sup>" + "[not bold]" + "</sup>" + "<b>" + "<sup>" + "[bold]" + "</sup>" + "</b>";
        doTest(input, expected);
    }

    protected String getNestedFont() {
        return "<p>" + "<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">" + "<tr>" + "<td align=\"center\" bgcolor=\"#c6d3de\">" + "<font color=\"#ffffff\" size=\"3\">" + "<b>" + "<a href=\"/wps/portal/!ut/p/.scr/Login\">" + "<font color=\"#3366cc\">" + "<img alt=\"[Log in]\" border=\"0\" height=\"20\" src=\"/wps/themes/xdime/images/nav_login.gif\" width=\"20\"/>" + "</font>" + "</a>" + "<a href=\"/wps/portal/!ut/p/.scr/ForgotPassword\">" + "<font color=\"#000000\">" + "<img alt=\"[I forgot my password]\" border=\"0\" height=\"20\" src=\"/wps/themes/xdime/images/nav_forgot_password.gif\" width=\"23\"/>" + "</font>" + "</a>" + "</b>" + "</font>" + "</td>" + "</tr>" + "</table>" + "</p>";
    }

    /**
     * A test for VBM:2006011908.
     * <p>
     * This ensures that all font colors are renderer correctly. Previously
     * we were throwing away the #ff6600 color attached to the innermost font.
     *
     * @throws Exception
     */
    public void test2006011908() throws Exception {
        String input = "<font size=\"2\">" + "<table>" + "<tr>" + "<td>" + "<font color=\"red\">" + "<font color=\"red\">red1</font>" + "<font color=\"red\">red2</font>" + "red3" + "</font>" + "</td>" + "<td>" + "<font color=\"red\">" + "<font color=\"green\">green</font>" + "red" + "</font>" + "</td>" + "</tr>" + "</table>" + "</font>";
        String expected = "<table>" + "<tr>" + "<td>" + "<font color=\"red\" size=\"2\">" + "red1" + "red2" + "red3" + "</font>" + "</td>" + "<td>" + "<font color=\"red\" size=\"2\">" + "<font color=\"green\">green</font>" + "red" + "</font>" + "</td>" + "</tr>" + "</table>";
        doTest(input, expected);
    }

    /**
     * Test that a when a stylistic element is pushed down into more than one
     * node, iterating over it's children works appropriately.
     * <p>
     * See VBM:2006092212.
     *
     * @throws Exception
     */
    public void test2006092212() throws Exception {
        String input = "<font color='#ff3333'>\n" + "  <a href='x'>\n" + "    <ANTI-I>\n" + "      <font size='4'>m1</font>\n" + "    </ANTI-I>\n" + "  </a>\n" + "  <a href='y'>\n" + "    <ANTI-I>\n" + "      <font size='4'>m2</font>\n" + "    </ANTI-I>\n" + "  </a>\n" + "</font>";
        String expected = "<font color=\"#ff3333\">\n" + "  </font>" + "<a href=\"x\"><font color=\"#ff3333\">\n" + "    </font><font color=\"#ff3333\">\n" + "      </font><font color=\"#ff3333\" size=\"4\">m1</font><font color=\"#ff3333\">\n" + "    </font><font color=\"#ff3333\">\n" + "  </font></a>" + "<font color=\"#ff3333\">\n" + "  </font>" + "<a href=\"y\"><font color=\"#ff3333\">\n" + "    </font><font color=\"#ff3333\">\n" + "      </font><font color=\"#ff3333\" size=\"4\">m2</font><font color=\"#ff3333\">\n" + "    </font><font color=\"#ff3333\">\n" + "  </font></a>" + "<font color=\"#ff3333\">\n" + "</font>";
        doTest(input, expected);
    }
}
