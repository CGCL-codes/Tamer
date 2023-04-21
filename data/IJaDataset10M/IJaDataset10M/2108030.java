package watij.elements;

import watij.WatijTestCase;
import static watij.finders.FinderFactory.id;
import static watij.finders.FinderFactory.*;
import static watij.finders.FinderFactory.name;
import static watij.finders.FinderFactory.value;
import static watij.finders.SymbolFactory.caption;
import static watij.finders.SymbolFactory.id;
import static watij.finders.SymbolFactory.name;
import watij.runtime.ObjectDisabledException;
import watij.runtime.UnknownObjectException;
import watij.runtime.ie.IE;

public class ButtonsTest extends WatijTestCase {

    protected void setUp() throws Exception {
        super.setUp();
        ie.goTo(HTML_ROOT + "buttons1.html");
    }

    public void goToFramesPage() throws Exception {
        ie.goTo(HTML_ROOT + "frame_buttons.html");
    }

    public void testAttributes() throws Exception {
        assertEquals("b1", ie.button(id, "b2").attribute("name"));
        ie.button(id, "b2").attribute("name", "ohya");
        assertEquals("ohya", ie.button(id, "b2").attribute("name"));
    }

    public void testButtonElementDoesntExistAfterClosingBrowser() throws Exception {
        IE ie = new IE();
        ie.start(HTML_ROOT + "buttons1.html");
        Button button = ie.button(name, "buttonTag");
        assertTrue(button.exists());
        ie.close();
        assertFalse(button.exists());
    }

    public void testButtonTag() throws Exception {
        assertTrue(ie.button(name, "buttonTag").exists());
        assertEquals("I am a tag called button", ie.button(name, "buttonTag").text());
    }

    public void testButtonMultipleFinders() throws Exception {
        ie.button(name("samename"), value("samevalue"), id("sameid")).click();
        assertEquals("samevalue4", ie.textField(id, "samevaluetest").value());
        ie.button(name("samename"), value("samevalue"), id("sameid"), index(1)).click();
        assertEquals("samevalue5", ie.textField(id, "samevaluetest").value());
    }

    public void testButtonMultipleFindersBadOrder() throws Exception {
        Button button = ie.button(index(0), name("samename"), value("samevalue"), id("sameid"));
        assertFalse(button.exists());
    }

    public void testButtonProperties() throws Exception {
        assertRaisesUnknownObjectExceptionForMethodId(ie.button(name, "noName"));
        assertRaisesUnknownObjectExceptionForMethodName(ie.button(name, "noName"));
        assertRaisesUnknownObjectExceptionForMethodDisabled(ie.button(name, "noName"));
        assertRaisesUnknownObjectExceptionForMethodType(ie.button(name, "noName"));
        assertRaisesUnknownObjectExceptionForMethodValue(ie.button(name, "noName"));
        assertEquals("b1", ie.button(0).name());
        assertEquals("b2", ie.button(0).id());
        assertEquals("button", ie.button(0).type());
        assertEquals("Click Me", ie.button(0).value());
        assertEquals(false, ie.button(0).disabled());
        assertEquals("italic_button", ie.button(name, "b1").className());
        assertEquals("", ie.button(name, "b4").className());
        assertEquals("b1", ie.button(id, "b2").name());
        assertEquals("b2", ie.button(id, "b2").id());
        assertEquals("button", ie.button(id, "b2").type());
        assertEquals("b4", ie.button(1).name());
        assertEquals("b5", ie.button(1).id());
        assertEquals("button", ie.button(1).type());
        assertEquals("Disabled Button", ie.button(1).value());
        assertEquals(true, ie.button(1).disabled());
        assertEquals("", ie.button(1).title());
        assertEquals("this is button1", ie.button(0).title());
    }

    public void testButtonUsingDefault() throws Exception {
        try {
            ie.button("Missing Caption").click();
            fail("Did not throw UnknownObjectException");
        } catch (UnknownObjectException e) {
        }
        ie.button("Click Me").click();
        assertTrue(ie.text().contains("PASS"));
    }

    public void testButtonClickOnly() throws Exception {
        ie.button(caption, "Click Me").click();
        assertTrue(ie.text().contains("PASS"));
    }

    public void testButtonClick() throws Exception {
        try {
            ie.button(caption, "Missing Caption").click();
            fail("Did not throw UnknownObjectException");
        } catch (UnknownObjectException e) {
        }
        try {
            ie.button(id, "missingID").click();
            fail("Did not throw UnknownObjectException");
        } catch (UnknownObjectException e) {
        }
        try {
            ie.button(caption, "Disabled Button").click();
            fail("Did not throw ObjectDisabledException");
        } catch (ObjectDisabledException e) {
        }
        ie.button(caption, "Click Me").click();
        assertTrue(ie.text().contains("PASS"));
    }

    public void testButtonExists() throws Exception {
        assertTrue(ie.button(caption, "Click Me").exists());
        assertTrue(ie.button(caption, "Submit").exists());
        assertTrue(ie.button(name, "b1").exists());
        assertTrue(ie.button(id, "b2").exists());
        assertTrue(ie.button(caption, "/(?i:sub)/").exists());
        assertFalse(ie.button(caption, "missingcaption").exists());
        assertFalse(ie.button(name, "missingname").exists());
        assertFalse(ie.button(id, "missingid").exists());
        assertFalse(ie.button(caption, "/(?i:missing)/").exists());
    }

    public void testButtonEnabled() throws Exception {
        assertTrue(ie.button(caption, "Click Me").enabled());
        assertFalse(ie.button(caption, "Disabled Button").enabled());
        assertFalse(ie.button(name, "b4").enabled());
        assertFalse(ie.button(id, "b5").enabled());
    }

    public void testFrame() throws Exception {
        goToFramesPage();
        assertTrue(ie.frame("buttonFrame").button(caption, "Click Me").enabled());
        try {
            ie.button(caption, "Disabled Button").enabled();
            fail("Did not throw UnknownObjectException");
        } catch (UnknownObjectException e) {
        }
    }
}
