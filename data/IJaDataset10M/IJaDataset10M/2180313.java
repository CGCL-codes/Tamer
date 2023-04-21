package net.sf.webphotos.gui.util;

import java.awt.Component;
import java.awt.Rectangle;
import net.sf.webphotos.util.Util;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.*;
import org.uispec4j.Trigger;
import org.uispec4j.UISpec4J;
import org.uispec4j.Window;
import org.uispec4j.finder.ComponentMatcher;
import org.uispec4j.interception.WindowHandler;
import org.uispec4j.interception.WindowInterceptor;

/**
 * Login Interface Test
 * Requires a headless UI
 * @author Guilherme
 */
public class LoginTest {

    static {
        UISpec4J.init();
    }

    public LoginTest() {
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
     * Basic Test.
     */
    @Test
    public void testLogin_Basic() {
        System.out.println("Basic Login");
        final Login login = new Login();
        final WindowHandler windowHandler = new WindowHandler() {

            @Override
            public Trigger process(Window window) {
                return window.getButton("OK").triggerClick();
            }
        };
        interceptDialog(login, windowHandler);
        assertEquals(Util.getConfig().getString("autoPreencher.Login"), login.getUser());
        assertEquals(Util.getConfig().getString("autoPreencher.Pass"), new String(login.getPassword()));
    }

    /**
     * Writing info Test.
     */
    @Test
    public void testLogin_WritingInfo() {
        System.out.println("Writing info Login");
        final String testeLogin = "TesteLogin";
        final String testePass = "TestePass";
        final Login login = new Login();
        final WindowHandler windowHandler = new WindowHandler() {

            @Override
            public Trigger process(Window window) {
                window.getTextBox(new ComponentMatcher() {

                    @Override
                    public boolean matches(Component cmpnt) {
                        return cmpnt.getBounds().equals(new Rectangle(55, 8, 129, 20));
                    }
                }).setText(testeLogin);
                window.getPasswordField(new ComponentMatcher() {

                    @Override
                    public boolean matches(Component cmpnt) {
                        return cmpnt.getBounds().equals(new Rectangle(55, 34, 129, 20));
                    }
                }).setPassword(testePass);
                return window.getButton("OK").triggerClick();
            }
        };
        interceptDialog(login, windowHandler);
        assertEquals(testeLogin, login.getUser());
        assertEquals(testePass, new String(login.getPassword()));
    }

    /**
     * Cancel Test.
     */
    @Test
    public void testLogin_Cancel() {
        System.out.println("Cancel Login");
        final Login login = new Login();
        final WindowHandler windowHandler = new WindowHandler() {

            @Override
            public Trigger process(Window window) {
                return window.getButton("Cancel").triggerClick();
            }
        };
        interceptDialog(login, windowHandler);
        assertNull(login.getUser());
        assertNull(login.getPassword());
    }

    private void interceptDialog(final Login login, final WindowHandler windowHandler) {
        WindowInterceptor.init(new Trigger() {

            @Override
            public void run() throws Exception {
                login.show();
            }
        }).process(windowHandler).run();
    }
}
