package org.zkoss.zul;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.zkoss.testing.TargetClass;
import org.zkoss.testing.TestFor;
import org.zkoss.zk.ui.ComponentNotFoundException;

/**
 * This TestCase tests {@link org.zkoss.zul.Window}. 
 * @author Dennis.Chen
 *
 */
@TargetClass(target = Window.class)
public class UTWindow001 {

    boolean hitException;

    /**
	 * @throws java.lang.Exception
	 */
    @Before
    public void setUp() throws Exception {
    }

    /**
	 * @throws java.lang.Exception
	 */
    @After
    public void tearDown() throws Exception {
    }

    /**
	 * Test property : title
	 *
	 */
    @Test
    @TestFor(property = "title")
    public void testPTitle() {
        Window window = new Window();
        window.setTitle("title");
        assertEquals("title", window.getTitle());
        window.setTitle(null);
        assertEquals("", window.getTitle());
        window.setTitle("");
        window = new Window("title", "normal", true);
        assertEquals("title", window.getTitle());
        window = new Window(null, "normal", true);
        assertEquals("", window.getTitle());
    }

    /**
	 * Test property: border
	 *
	 */
    @Test
    @TestFor(property = "border")
    public void testPBorder() {
        Window window = new Window();
        window.setBorder("normal");
        assertEquals("normal", window.getBorder());
        window.setBorder("0");
        assertEquals("none", window.getBorder());
        window.setBorder(null);
        assertEquals("none", window.getBorder());
        window = new Window("title", "normal", true);
        assertEquals("normal", window.getBorder());
        window = new Window("title", "0", true);
        assertEquals("none", window.getBorder());
        window = new Window("title", null, true);
        assertEquals("none", window.getBorder());
    }

    /**
	 * Test property : closable
	 *
	 */
    @Test
    @TestFor(property = "closable")
    public void testPClosable() {
        Window window = new Window();
        window.setClosable(true);
        assertEquals(true, window.isClosable());
        window.setClosable(false);
        assertEquals(false, window.isClosable());
        window = new Window("title", "normal", true);
        assertEquals(true, window.isClosable());
        window = new Window("title", "normal", false);
        assertEquals(false, window.isClosable());
    }

    /**
	 * Test method: {@link org.zkoss.zul.Window#getFellow(String)}
	 *
	 */
    @Test
    @TestFor(method = "getFellow")
    public void testMGetFellow() {
        Window w1 = new Window();
        w1.setId("w1");
        Window w2 = new Window();
        w2.setId("w2");
        Window w3 = new Window();
        w3.setId("w3");
        w2.setParent(w1);
        w3.setParent(w2);
        Label lab1 = new Label();
        lab1.setId("lab1");
        Label lab2 = new Label();
        lab2.setId("lab2");
        Label lab3 = new Label();
        lab3.setId("lab3");
        lab1.setParent(w1);
        lab2.setParent(w2);
        lab3.setParent(w3);
        assertSame(w1.getFellow("w1"), w1);
        assertSame(w1.getFellow("lab1"), lab1);
        assertSame(w1.getFellow("w2"), w2);
        assertSame(w2.getFellow("w2"), w2);
        assertSame(w2.getFellow("lab2"), lab2);
        assertSame(w2.getFellow("w3"), w3);
        assertSame(w3.getFellow("w3"), w3);
        assertSame(w3.getFellow("lab3"), lab3);
        try {
            w1.getFellow("xyz");
        } catch (ComponentNotFoundException x) {
            hitException = true;
        } finally {
            assertTrue(hitException);
            hitException = false;
        }
    }

    /**
	 * Test method:{@link org.zkoss.zul.Window#getFellowIfAny(String)}
	 */
    @Test
    @TestFor(method = "getFellowIfAny")
    public void testMGetFellowIfAny() {
        Window w1 = new Window();
        w1.setId("w1");
        Window w2 = new Window();
        w2.setId("w2");
        Window w3 = new Window();
        w3.setId("w3");
        w2.setParent(w1);
        w3.setParent(w2);
        Label lab1 = new Label();
        lab1.setId("lab1");
        Label lab2 = new Label();
        lab2.setId("lab2");
        Label lab3 = new Label();
        lab3.setId("lab3");
        lab1.setParent(w1);
        lab2.setParent(w2);
        lab3.setParent(w3);
        assertSame(w1.getFellowIfAny("w1"), w1);
        assertSame(w1.getFellowIfAny("lab1"), lab1);
        assertSame(w1.getFellowIfAny("w2"), w2);
        assertSame(w2.getFellowIfAny("w2"), w2);
        assertSame(w2.getFellowIfAny("lab2"), lab2);
        assertSame(w2.getFellowIfAny("w3"), w3);
        assertSame(w3.getFellowIfAny("w3"), w3);
        assertSame(w3.getFellowIfAny("lab3"), lab3);
        assertNull(w1.getFellowIfAny("xyz"));
    }
}
