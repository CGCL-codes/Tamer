package net.sf.dpdesktop.service;

import net.sf.dpdesktop.util.Property;
import net.sf.dpdesktop.util.PropertyTest;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Heiner Reinhardt
 */
public class LogItemTest {

    /**
     * Test of setContainer method, of class LogItem.
     */
    @Test
    public void testSetContainer() {
        Container get = new Container("test");
        LogItem instance = new LogItem("", "");
        instance.setContainer(get);
        assertEquals(get, instance.getContainer());
    }

    /**
     * Test of getContainer method, of class LogItem.
     */
    @Test
    public void testGetContainer() {
        LogItem instance = new LogItem("", "");
        Container expResult = null;
        Container result = instance.getContainer();
        assertEquals(expResult, result);
    }

    @Test
    public void testProperty() {
        assertEquals(LogItem.class.getSuperclass(), Property.class);
    }
}
