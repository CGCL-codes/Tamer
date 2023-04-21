package watij.elements;

import watij.WatijTestCase;
import static watij.finders.SymbolFactory.xpath;

public class LinksXpathTest extends WatijTestCase {

    protected void setUp() throws Exception {
        super.setUp();
        ie.goTo(HTML_ROOT + "links1.html");
    }

    public void testLinkExists() throws Exception {
        assertTrue(ie.link(xpath, "//A[contains(.,'test1')]").exists());
        assertTrue(ie.link(xpath, "//A[contains(., /TEST/i)]").exists());
        assertFalse(ie.link(xpath, "//A[contains(.,'missing')]").exists());
        assertFalse(ie.link(xpath, "//A[@url='alsomissing.html']").exists());
        assertTrue(ie.link(xpath, "//A[@id='link_id']").exists());
        assertFalse(ie.link(xpath, "//A[@id='alsomissing']").exists());
        assertTrue(ie.link(xpath, "//A[@name='link_name']").exists());
        assertFalse(ie.link(xpath, "//A[@name='alsomissing']").exists());
        assertTrue(ie.link(xpath, "//A[@title='link_title']").exists());
    }

    public void testLinkClick() throws Exception {
        ie.link(xpath, "//A[contains(.,'test1')]").click();
        assertTrue(ie.text().contains("Links2-Pass"));
    }
}
