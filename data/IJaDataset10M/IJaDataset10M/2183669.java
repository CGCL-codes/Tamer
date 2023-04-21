package ca.ucalgary.cpsc.ebe.fitClipse.tests.junit;

import ca.ucalgary.cpsc.ebe.fitClipse.connector.FitNesse;
import ca.ucalgary.cpsc.ebe.fitClipse.connector.ServerConfiguration;
import ca.ucalgary.cpsc.ebe.fitClipse.ui.testHierarchy.model.WikiPageModel;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

public class NewTestPageTest extends TestCase {

    private static ServerConfiguration sc = null;

    private static FitNesse fitNesse = null;

    public static void main(String[] args) {
        TestRunner.run(suite());
    }

    private static Test suite() {
        return new TestSuite(NewTestPageTest.class);
    }

    public void setUp() {
        ServerConfiguration.clear();
        sc = ServerConfiguration.getInstance();
        sc.setProjectNameSpace("TestProject");
        sc.setHost("localhost");
        sc.setWebPort("80");
        sc.setWebPath("FitClipse.ProjectS");
        fitNesse = new FitNesse(sc);
    }

    public void testNewTestPage() {
        String content = "this is new page!";
        WikiPageModel father = new WikiPageModel("TestProject", ".root");
        WikiPageModel model = new WikiPageModel("FitClipseTestPageNew1", ".root.TestProject");
        model.setParent(father);
        model.setFitTest(true);
        boolean right = fitNesse.saveGenericWikiPage(model, content);
        assertTrue(right);
        fitNesse.deleteWikiPage(model.getQName());
    }
}
