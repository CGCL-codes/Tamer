package functional.src.edu.eps.ceu.vista;

import edu.eps.ceu.consultaplus.Login;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JTextField;
import javax.swing.event.InternalFrameEvent;
import edu.eps.ceu.consultaplus.Main;
import junit.extensions.jfcunit.finder.FrameFinder;
import junit.framework.*;
import junit.extensions.jfcunit.JFCTestCase;
import junit.extensions.jfcunit.JFCTestHelper;
import junit.extensions.jfcunit.eventdata.KeyEventData;
import junit.extensions.jfcunit.eventdata.MouseEventData;
import junit.extensions.jfcunit.eventdata.StringEventData;
import junit.extensions.jfcunit.TestHelper;
import junit.textui.TestRunner;
import junit.extensions.jfcunit.finder.ComponentFinder;
import junit.extensions.jfcunit.finder.Finder;
import junit.extensions.jfcunit.finder.DialogFinder;
import java.awt.Container;
import java.awt.event.KeyEvent;
import java.util.List;

/**
 *
 * @author Sara Garcia
 */
public class salirLoginTest extends JFCTestCase {

    /** The thread the modal dialog will execute on. */
    private Thread m_modalThread = null;

    /** Any exception thrown by the modal dialog. */
    private Exception m_lastException = null;

    /** The main object under test. */
    public Login ma = null;

    /** The Helper to be used. */
    private TestHelper m_helper = null;

    /** True if the application exited normally. */
    private boolean m_normalExit = false;

    /** Flag. True if the application has been started. */
    private volatile boolean m_started = false;

    /**
     * Main method to run this class from the command line.
     *
     * @param args   Command line arguments.
     */
    public static void main(final String[] args) {
        TestRunner.run(DesktopTest.class);
    }

    public void setUp() throws Exception {
        m_helper = new JFCTestHelper();
        m_lastException = null;
        m_modalThread = new Thread(new Runnable() {

            public void run() {
                try {
                    m_lastException = null;
                    m_started = true;
                    new Login();
                    m_normalExit = true;
                } catch (Exception e) {
                    m_lastException = e;
                }
            }
        }, "ModalThread");
        m_modalThread.start();
        Thread.currentThread().yield();
        while (!m_started) {
            Thread.currentThread().sleep(50);
        }
        Thread.currentThread().sleep(500);
        flushAWT();
        checkException();
    }

    public void testSalirLogin() {
        List dialogs = new FrameFinder("Acceso a Consultaplus").findAll();
        assertEquals("Dialog not found:", 1, dialogs.size());
        Container boton_aceptar = (Container) dialogs.get(0);
        Finder f6 = new ComponentFinder(JButton.class);
        JButton field6 = (JButton) f6.find(boton_aceptar, 0);
        assertNotNull("Could not find field", field6);
        f6 = new ComponentFinder(JButton.class);
        JButton aceptar = (JButton) f6.find(boton_aceptar, 3);
        m_helper.enterClickAndLeave(new MouseEventData(this, aceptar));
        assertTrue("Unsuccessful exit:", m_normalExit);
        checkException();
    }

    /**
     * Interrupt the modalThread if it is still running.
     * Then shutdown the fixtures used.
     *
     * @throws Exception may be thrown.
     */
    public void tearDown() throws Exception {
        if (m_lastException != null) {
            throw m_lastException;
        }
        if (m_modalThread.isAlive()) {
            m_modalThread.interrupt();
        }
        m_modalThread = null;
        ma = null;
        m_helper = null;
    }

    /**
     * Check for the occurance of a Exception on the
     * modalThread. Rethrow the exception if one was generated.
     */
    private void checkException() {
        if (m_lastException != null) {
            fail(m_lastException.toString());
        }
    }
}
