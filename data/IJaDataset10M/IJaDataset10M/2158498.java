package net.sourceforge.ondex.taverna.wrapper;

import javax.swing.event.ChangeListener;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Christian
 */
public class ChangeFirerTest {

    public ChangeFirerTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of addChangeListener method, of class ChangeFirer.
     */
    @Test
    public void testAddChangeListener() {
        System.out.println("addChangeListener");
        ChangeListener l = new ChangeListenerStub();
        ChangeFirer instance = new ChangeFirer();
        instance.addChangeListener(l);
    }

    /**
     * Test of removeChangeListener method, of class ChangeFirer.
     */
    @Test
    public void testRemoveChangeListener() {
        System.out.println("removeChangeListener");
        ChangeListener l = new ChangeListenerStub();
        ChangeFirer instance = new ChangeFirer();
        instance.removeChangeListener(l);
    }

    @Test
    public void testFireStateChanged() {
        ChangeFirer instance = new ChangeFirer();
        instance.fireStateChanged();
        ChangeListenerStub listener = new ChangeListenerStub();
        instance.addChangeListener(listener);
        instance.fireStateChanged();
        assertEquals(instance, listener.lastSource);
    }

    @Test
    public void testFireStateChangedNoListener() {
        System.out.println("fireStateChanged");
        ChangeFirer instance = new ChangeFirer();
        instance.fireStateChanged();
    }
}
