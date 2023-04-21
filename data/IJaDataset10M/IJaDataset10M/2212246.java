package com.volantis.mcs.protocols.menu.shared.model;

import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.TestDOMOutputBuffer;
import com.volantis.mcs.protocols.assets.ScriptAssetReference;
import com.volantis.mcs.protocols.assets.TextAssetReference;
import com.volantis.mcs.protocols.assets.implementation.LiteralScriptAssetReference;
import com.volantis.mcs.protocols.assets.implementation.LiteralTextAssetReference;
import com.volantis.mcs.protocols.menu.model.ElementDetails;
import com.volantis.mcs.protocols.menu.model.EventType;
import com.volantis.mcs.protocols.menu.model.MenuEntry;
import com.volantis.mcs.protocols.menu.model.MenuItem;
import com.volantis.mcs.protocols.menu.model.MenuLabel;

/**
 * This class tests a concrete implementation of the
 * {@link com.volantis.mcs.protocols.menu.model.Menu} interface.  Since
 * it is part of a class hierarchy, the test case is also part of a (test)
 * hierarchy.
 */
public class ConcreteMenuTestCase extends AbstractMenuEntryTestAbstract {

    /**
     * A string used throughout the tests to name a menu
     */
    private final String testTitle = "Menu Title";

    /**
      * This method tests the getSize() method in the MenuItemGroup interface
      * as well as the add() and method in ConcreteMenuItemGroup.
      */
    public void testGetSize() {
        ConcreteMenu menu = MenuModelHelper.createMenu(false, testTitle);
        AbstractMenuEntry[] entries = MenuModelHelper.createEntryArray();
        assertEquals("Should be no items in the list", 0, menu.getSize());
        menu.add(entries[0]);
        assertEquals("Should be one item in the list", 1, menu.getSize());
        menu.add(entries[1]);
        assertEquals("Should be many items in the list", 2, menu.getSize());
        menu.add(entries[2]);
        assertEquals("Should be many items in the list", 3, menu.getSize());
    }

    /**
      * This method tests the get() method
      */
    public void testGet() {
        ConcreteMenu itemGroup = MenuModelHelper.createMenu(true, testTitle);
        int maxItems = itemGroup.getSize();
        MenuEntry item = itemGroup.get(1);
        assertNotNull("Menu item should not be null", item);
        try {
            item = itemGroup.get(maxItems + 1);
            fail("get() call should have thrown an exception");
        } catch (IndexOutOfBoundsException iob) {
        }
    }

    /**
     * This method tests the get and set help methods
     */
    public void testHelp() {
        ConcreteMenu item = MenuModelHelper.createMenu(false, testTitle);
        TextAssetReference testHelp = item.getHelp();
        assertNull("Should be no help", testHelp);
        TextAssetReference help = new LiteralTextAssetReference("Help Object");
        item.setHelp(help);
        testHelp = item.getHelp();
        assertNotNull("Should be a help", testHelp);
        assertEquals("Helps should be the same", help, testHelp);
    }

    /**
     * This method tests the get and set error methods
     */
    public void testErrorMessage() {
        ConcreteMenu item = MenuModelHelper.createMenu(false, testTitle);
        TextAssetReference testError = item.getErrorMessage();
        assertNull("Should be no error message", testError);
        TextAssetReference error = new LiteralTextAssetReference("Error Message Object");
        item.setErrorMessage(error);
        testError = item.getErrorMessage();
        assertNotNull("Should be an error message", testError);
        assertEquals("Error messages should be the same", error, testError);
    }

    /**
     * This method tests the get and set label methods
     */
    public void testLabel() {
        ConcreteMenu item = MenuModelHelper.createMenu(false, testTitle);
        MenuLabel testLabel = item.getLabel();
        assertNull("Should be no label", testLabel);
        MenuLabel label = MenuModelHelper.createMenuLabel(null);
        item.setLabel(label);
        testLabel = item.getLabel();
        assertNotNull("Should be a label", testLabel);
        assertEquals("Labels should be the same", label, testLabel);
    }

    /**
     * This method tests the get, set and remove event handler methods
     */
    public void testEventHandler() {
        ConcreteMenu item = MenuModelHelper.createMenu(false, testTitle);
        EventType blur = EventType.ON_BLUR;
        EventType click = EventType.ON_CLICK;
        EventType mouse = EventType.ON_MOUSE_OVER;
        EventType focus = EventType.ON_FOCUS;
        ScriptAssetReference handlerOne = new LiteralScriptAssetReference("Handle blurrred things");
        ScriptAssetReference handlerTwo = new LiteralScriptAssetReference("Handle clicked things");
        ScriptAssetReference handlerThree = new LiteralScriptAssetReference("Handle mouse over things");
        ScriptAssetReference handlerFour = new LiteralScriptAssetReference("Handle focus things");
        Object handler;
        handler = item.getEventHandler(blur);
        assertNull("Should be no handler", handler);
        try {
            item.getEventHandler(null);
            fail("Null event handler types should cause an exception");
        } catch (IllegalArgumentException iae) {
        }
        try {
            item.setEventHandler(blur, handlerOne);
            fail("Not allowed to use blur events on menus");
        } catch (IllegalArgumentException iae) {
        }
        try {
            item.setEventHandler(focus, handlerFour);
            fail("Not allowed to use focus events on menus");
        } catch (IllegalArgumentException iae) {
        }
        item.setEventHandler(click, handlerTwo);
        handler = item.getEventHandler(click);
        assertNotNull("Should have a handler", handler);
        assertEquals("Handlers should be the same", handler, handlerTwo);
        handler = item.getEventHandler(mouse);
        assertNull("Should be no handler", handler);
        item.setEventHandler(mouse, handlerThree);
        handler = item.getEventHandler(mouse);
        assertNotNull("Should have a handler", handler);
        assertEquals("Handlers should be the same", handler, handlerThree);
        item.removeEventHandler(click);
    }

    /**
     * Test the get and set title methods.
     */
    public void testTitle() {
        ConcreteMenu item = MenuModelHelper.createMenu(false, null);
        String testTitle = item.getTitle();
        assertNull("Should be no title", testTitle);
        String title = "title";
        item.setTitle(title);
        testTitle = item.getTitle();
        assertNotNull("Should be a title", testTitle);
        assertEquals("Titles should be the same", title, testTitle);
    }

    /**
     * Tests the get and set prompt methods
     */
    public void testPrompt() {
        ConcreteMenu item = MenuModelHelper.createMenu(false, null);
        TextAssetReference testPrompt = item.getPrompt();
        assertNull("Should be no prompt", testPrompt);
        TextAssetReference prompt = new LiteralTextAssetReference("Prompt Object");
        item.setPrompt(prompt);
        testPrompt = item.getPrompt();
        assertNotNull("Should be a prompt", testPrompt);
        assertEquals("Prompts should be the same", prompt, testPrompt);
    }

    /**
     * Tests the retrieval of a menu as a menu item for displaying
     */
    public void testGetAsMenuItem() {
        DOMOutputBuffer buffer = new TestDOMOutputBuffer();
        buffer.appendEncoded("test");
        ConcreteMenuLabel menuLabel = MenuModelHelper.createMenuLabel(buffer);
        String title = "test menu";
        ConcreteMenu menu = MenuModelHelper.createMenu(false, title);
        menu.setLabel(menuLabel);
        MenuItem response = menu.getAsMenuItem();
        assertNull("The menu should be top level", response);
        menu.setContainer((MenuEntry) createTestInstance(MenuModelHelper.createElementDetails()));
        response = menu.getAsMenuItem();
        assertNotNull("The menu should be a sub menu", response);
        assertSame("Labels should be the same", menuLabel, response.getLabel());
        assertEquals("Titles should match", title, response.getTitle());
    }

    protected AbstractModelElement createTestInstance(ElementDetails elementDetails) {
        return new ConcreteMenu(elementDetails);
    }
}
