package watij;

import static watij.finders.SymbolFactory.name;
import static watij.finders.SymbolFactory.text;
import watij.runtime.ie.IE;

public class ChildBrowserTest extends WatijTestCase {

    IE ie;

    protected void setUp() throws Exception {
        ie = new IE();
        ie.start();
        ie.goTo(HTML_ROOT + "new_browser.html");
    }

    protected void tearDown() throws Exception {
        ie.close();
    }

    public void testChildBrowser() throws Exception {
        ie.link(text, "New Window").click();
        IE ie_new = ie.childBrowser();
        assertTrue(ie.childBrowserCount() == 1);
        assertTrue(ie_new.text().contains("PASS"));
        ie_new.close();
        ie.waitUntilReady();
        System.out.println(ie.childBrowserCount());
        assertTrue(ie.childBrowserCount() == 0);
        ie.goTo(HTML_ROOT + "textfields1.html");
        assertTrue(ie.textField(name, "text1").exists());
    }

    public void testOpenCloseMultipleChildBrowser() throws Exception {
        ie.link(text, "New Window").click();
        IE ie_new = ie.childBrowser();
        assertTrue(ie.childBrowserCount() == 1);
        assertTrue(ie_new.text().contains("PASS"));
        ie_new.close();
        assertTrue(ie.childBrowserCount() == 0);
        ie.link(text, "New Window").click();
        ie_new = ie.childBrowser();
        assertTrue(ie.childBrowserCount() == 1);
        assertTrue(ie_new.text().contains("PASS"));
        ie_new.close();
    }

    public void testClosingMultipleWindows() throws Exception {
        ie.link(text, "Open New Browser Launcher").click();
        IE ieNewBrowserLauncher = ie.childBrowser();
        assertTrue(ieNewBrowserLauncher.containsText("Open New Browser Launcher"));
        ie.link(text, "New Window").click();
        IE ie_new = ie.childBrowser(1);
        assertTrue(ie_new.text().contains("PASS"));
        assertTrue(ie.childBrowserCount() == 2);
        ieNewBrowserLauncher.link(text, "New Window").click();
        IE ieChild = ieNewBrowserLauncher.childBrowser();
        assertTrue(ieChild.text().contains("PASS"));
        assertTrue(ieNewBrowserLauncher.childBrowserCount() == 1);
        ieNewBrowserLauncher.close();
        ie_new.close();
        ieChild.close();
        assertTrue(ie.childBrowserCount() == 0);
        ie.goTo(HTML_ROOT + "textfields1.html");
        assertTrue(ie.textField(name, "text1").exists());
    }

    public void test_child_browser_slow_window_works_with_delay() throws Exception {
        ie.span(text, "New Window Slowly").click();
        IE ie_new = ie.childBrowser();
        assertTrue(ie_new.text().contains("Blank page to fill in the frames"));
        ie_new.close();
    }

    public void testSelfClosingSubWindows() throws Exception {
        for (int i = 0; i < 5; i++) {
            ie.link(text, "Self Closer").click();
            assertEquals(1, ie.childBrowserCount());
            IE childBrowser = ie.childBrowser();
            assertTrue(childBrowser.containsText("Self-closer"));
            assertTrue(childBrowser.exists());
            childBrowser.button("Close Window").click();
            childBrowser.waitUntilClosed();
            assertFalse(childBrowser.exists());
        }
    }
}
