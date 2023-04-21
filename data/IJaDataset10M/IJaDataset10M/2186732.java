package com.gorillalogic.test.cases.faces;

import java.io.IOException;
import java.net.MalformedURLException;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Logger;
import org.jaxen.JaxenException;
import org.xml.sax.SAXException;
import com.gargoylesoftware.htmlunit.DefaultCredentialsProvider;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gorillalogic.test.cases.faces.FacesClient.Explorer;
import com.gorillalogic.test.cases.faces.FacesClient.Field;
import com.gorillalogic.test.cases.faces.FacesClient.Form;
import com.gorillalogic.test.cases.faces.FacesClient.Link;
import com.gorillalogic.test.cases.faces.FacesClient.Table;
import com.gorillalogic.test.cases.faces.FacesClient.Table.Row;
import com.gorillalogic.test.cases.faces.FacesClient.DropDown;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * @author Stu
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class GouseTest extends FacesBaseTest {

    static Logger logger = Logger.getLogger(GouseTest.class);

    static {
    }

    public static String USER = "guest";

    public static String PASSWORD = "guest";

    public static String ADMIN_USER = "admin";

    public static String ADMIN_PASSWORD = "";

    private static boolean isResetting = false;

    /**
	 * Constructor for FacesTest.
	 * @param arg0
	 */
    public GouseTest(String test) {
        super(test);
        logger.addAppender(new org.apache.log4j.net.SyslogAppender());
    }

    protected void setUp() throws Exception {
        super.setUp();
        CLIENT.login(USER, PASSWORD);
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        if (!isResetting) {
            CLIENT.logout();
        } else {
            isResetting = false;
        }
    }

    public void testLogin() throws JaxenException {
        assertNotNull("Explorer not found", CLIENT.getExplorer());
    }

    public void testOpenModule() throws FailingHttpStatusCodeException, MalformedURLException, IOException {
        CLIENT.openWelcomeModule(SCRIPTS + "/" + "scripts/modules/gouse/gouse_magicdraw.gxm");
    }

    public void testReLogin() throws JaxenException, MalformedURLException, IOException {
        CLIENT.login("gorilla", "g");
        assertNotNull("Explorer not found", CLIENT.getExplorer());
    }

    public void testUseCase() throws IOException, JaxenException, SAXException {
        testReLogin();
        Explorer exp = CLIENT.getExplorer();
        exp.click("CheckOut");
        String tmpname = null;
        Form form = null;
        Table table = null;
        Field field = null;
        assertUsecaseStep("ReviewOrder");
        tmpname = "Order";
        table = CLIENT.getTable(tmpname);
        assertNotNull("Table \"" + tmpname + "\" not found", table);
        table.clickButton("Continue");
        assertUsecaseStep("ProvidePaymentDetails");
        tmpname = "Payment";
        form = CLIENT.getForm(tmpname);
        assertNotNull("Form \"" + tmpname + "\" not found", form);
        tmpname = "cardNumber";
        field = form.getField(tmpname);
        assertNotNull("Field \"" + tmpname + "\" not found", field);
        field.set("0123456789");
        form.setField("expiry", "0123");
        form.clickButton("Continue");
        assertUsecaseStep("EnterAuthorizationCode");
        tmpname = "Payment";
        form = CLIENT.getForm(tmpname);
        assertNotNull("Form \"" + tmpname + "\" not found", form);
        tmpname = "authorizationCode";
        field = form.getField(tmpname);
        assertNotNull("Field \"" + tmpname + "\" not found", field);
        field.set("bananas");
        form.clickButton("Continue");
        assertUsecaseStep("ReviewBillingAndShipping");
        tmpname = "order";
        form = CLIENT.getForm(tmpname);
        assertNotNull("Form \"" + tmpname + "\" not found", form);
        tmpname = "status";
        field = form.getField(tmpname);
        assertNotNull("Field \"" + tmpname + "\" not found", field);
        form.clickButton("Submit");
        assertUsecaseStep("Completed");
        tmpname = "Order";
        form = CLIENT.getForm(tmpname);
        assertNotNull("Form \"" + tmpname + "\" not found", form);
        tmpname = "status";
        field = form.getField(tmpname);
        assertNotNull("Field \"" + tmpname + "\" not found", field);
        form.clickButton("Continue Shopping");
        assertNotNull("Explorer not found", CLIENT.getExplorer());
        CLIENT.save();
    }

    public void testImmediate() throws IOException, JaxenException, SAXException {
        testReLogin();
        Explorer exp = CLIENT.getExplorer();
        exp.click("CheckOut");
        String tmpname = null;
        Form form = null;
        Table table = null;
        Field field = null;
        assertUsecaseStep("ReviewOrder");
        tmpname = "Order";
        table = CLIENT.getTable(tmpname);
        assertNotNull("Table \"" + tmpname + "\" not found", table);
        table.clickButton("TestImmediate");
        assertUsecaseStep("ErrOnExit");
        tmpname = "Order";
        form = CLIENT.getForm(tmpname);
        assertNotNull("Form \"" + tmpname + "\" not found", table);
        tmpname = "date";
        field = form.getField(tmpname);
        assertNotNull("Field \"" + tmpname + "\" not found", field);
        field.set("0123456789");
        form.clickButton("ImmediateCancel");
        assertUsecaseStep("Canceled");
        tmpname = "Order";
        form = CLIENT.getForm(tmpname);
        assertNotNull("Form \"" + tmpname + "\" not found", table);
        form.clickButton("Continue");
        assertNotNull("Explorer not found", CLIENT.getExplorer());
        CLIENT.save();
    }

    public void testRestoreContext() throws IOException, JaxenException, SAXException {
        testReLogin();
        Explorer exp = CLIENT.getExplorer();
        exp.click("CheckOut");
        String tmpname = null;
        Form form = null;
        Table table = null;
        Field field = null;
        assertUsecaseStep("ReviewOrder");
        tmpname = "Order";
        table = CLIENT.getTable(tmpname);
        table.clickButton("TestRestoreContext");
        assertUsecaseStep("RequiresOrderContext");
        tmpname = "Order";
        form = CLIENT.getForm(tmpname);
        form.clickButton("CdThenError");
        assertUsecaseStep("RequiresOrderContext");
        tmpname = "Order";
        form = CLIENT.getForm(tmpname);
        form.clickButton("Cancel");
        assertUsecaseStep("Canceled");
        tmpname = "Order";
        form = CLIENT.getForm(tmpname);
        form.clickButton("Continue");
        assertNotNull("Explorer not found", CLIENT.getExplorer());
        CLIENT.save();
    }

    public void assertUsecaseStep(String stepname) throws JaxenException, IOException {
        HtmlElement el = CLIENT.getUsecaseTitle();
        assertNotNull("usecaseTitle Element not found", el);
        String eltext = el.asText();
        assertEquals("usecase stepname " + stepname + " not found in: " + eltext, true, eltext.indexOf(stepname) > 0);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite("GouseTest");
        suite.addTest(new GouseTest("testLogin"));
        suite.addTest(new GouseTest("testOpenModule"));
        suite.addTest(new GouseTest("testUseCase"));
        suite.addTest(new GouseTest("testImmediate"));
        suite.addTest(new GouseTest("testRestoreContext"));
        return suite;
    }

    public static void configure(String server, String context, String home) {
        SERVER = server;
        CONTEXT = context;
        CLIENT = new FacesClient(server, context);
    }

    public static void main(String[] args) throws MalformedURLException, IOException, SAXException {
        if (args.length > 0) {
            configure(args[0], args[1], args[2]);
        }
        junit.textui.TestRunner.run(suite());
    }
}
