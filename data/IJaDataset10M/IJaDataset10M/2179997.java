package org.xmlhammer.gui.acceptance;

import java.io.File;
import javax.swing.tree.MutableTreeNode;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.xmlhammer.gui.output.ResultTreeModel;
import org.xmlhammer.gui.output.ValueNode;

public class XPathTest extends ProjectTestCase {

    private XPathTest(String test) {
        super(test);
    }

    public void openProject() throws Exception {
        openProject(new File("src/test/resources/projects/xpath/xpath.xhp"));
    }

    public void openNamespacesProject() throws Exception {
        openProject(new File("src/test/resources/projects/xpath/xpath-namespaces.xhp"));
    }

    public void openNumberProject() throws Exception {
        openProject(new File("src/test/resources/projects/xpath/xpath-number.xhp"));
    }

    public void openBooleanProject() throws Exception {
        openProject(new File("src/test/resources/projects/xpath/xpath-boolean.xhp"));
    }

    public void execute() {
        super.execute();
        ResultTreeModel model = getApplication().getProjectsView().getSelectedView().getResultPanel().getModel();
        assertEquals(8, ((MutableTreeNode) model.getRoot()).getChildAt(0).getChildCount());
    }

    public void executeNumber() {
        super.execute();
        ResultTreeModel model = getApplication().getProjectsView().getSelectedView().getResultPanel().getModel();
        assertTrue(((MutableTreeNode) model.getRoot()).getChildAt(0).getChildAt(0) instanceof ValueNode);
        assertEquals("2.0", ((ValueNode) ((MutableTreeNode) model.getRoot()).getChildAt(0).getChildAt(0)).getValue());
    }

    public void executeBoolean() {
        super.execute();
        ResultTreeModel model = getApplication().getProjectsView().getSelectedView().getResultPanel().getModel();
        assertTrue(((MutableTreeNode) model.getRoot()).getChildAt(0).getChildAt(0) instanceof ValueNode);
        assertEquals("true", ((ValueNode) ((MutableTreeNode) model.getRoot()).getChildAt(0).getChildAt(0)).getValue());
    }

    public static Test suite() {
        TestSuite suite = new TestSuite("XPath Projects");
        suite.addTest(new XPathTest("openProject"));
        suite.addTest(new XPathTest("initialPage"));
        suite.addTest(new XPathTest("hasChanged"));
        suite.addTest(new XPathTest("execute"));
        suite.addTest(new XPathTest("closeProject"));
        suite.addTest(new XPathTest("openNamespacesProject"));
        suite.addTest(new XPathTest("initialPage"));
        suite.addTest(new XPathTest("hasChanged"));
        suite.addTest(new XPathTest("execute"));
        suite.addTest(new XPathTest("closeProject"));
        suite.addTest(new XPathTest("openNumberProject"));
        suite.addTest(new XPathTest("initialPage"));
        suite.addTest(new XPathTest("hasChanged"));
        suite.addTest(new XPathTest("executeNumber"));
        suite.addTest(new XPathTest("closeProject"));
        suite.addTest(new XPathTest("openBooleanProject"));
        suite.addTest(new XPathTest("initialPage"));
        suite.addTest(new XPathTest("hasChanged"));
        suite.addTest(new XPathTest("executeBoolean"));
        suite.addTest(new XPathTest("closeProject"));
        suite.addTest(new XPathTest("closeApplication"));
        return suite;
    }
}
