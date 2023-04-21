package tests.com.ivis.xprocess.abbot.workflow;

import junit.extensions.ForkedPDETestCase;
import org.eclipse.swt.widgets.Text;
import tests.com.ivis.xprocess.ui.util.EditorHelper;
import tests.com.ivis.xprocess.ui.util.ElementCreationException;
import tests.com.ivis.xprocess.ui.util.ExplorerTreeHelper;
import tests.com.ivis.xprocess.ui.util.Helper;
import tests.com.ivis.xprocess.ui.util.ProcessHelper;
import tests.com.ivis.xprocess.ui.util.UITestUtil;
import abbot.finder.swt.MultipleWidgetsFoundException;
import abbot.finder.swt.WidgetNotFoundException;

public class TestUpdateStateSet extends ForkedPDETestCase {

    private UITestUtil uitu;

    public TestUpdateStateSet(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        super.setUp();
        uitu = UITestUtil.getInstance();
        uitu.ensureXProcessIsStarted();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testUpdateStateset() throws ElementCreationException, WidgetNotFoundException, MultipleWidgetsFoundException {
        uitu.ensureDatasourceAvailable("testUpdateStateset");
        String processName = ProcessHelper.getNewProcessName();
        String workflowPackageName = ProcessHelper.getNewWorkflowPackageName();
        String stateName1 = ProcessHelper.getNewStateName();
        ProcessHelper.createProcess(processName);
        ProcessHelper.createWorkflowPackage(processName, workflowPackageName);
        assertTrue("workflow package created", ExplorerTreeHelper.treeItemExists(Helper.getRootElementName() + "/" + processName, workflowPackageName));
        String stateSetName = Helper.getNewStateSetName();
        String pathToWorkflowPackage = Helper.getRootElementName() + "/" + processName + "/" + workflowPackageName;
        String ognlRule = "name == \"123\" ";
        ProcessHelper.createStateSet(pathToWorkflowPackage, stateSetName, "True", "Task");
        assertTrue(ExplorerTreeHelper.treeItemExists(pathToWorkflowPackage, stateSetName));
        ProcessHelper.createState(pathToWorkflowPackage + "/" + stateSetName, stateName1, ognlRule);
        assertTrue(ExplorerTreeHelper.treeItemExists(pathToWorkflowPackage + "/" + stateSetName, stateName1));
        String newStatesetName = "changed " + stateSetName;
        ExplorerTreeHelper.editTreeItemInplace(pathToWorkflowPackage + "/" + stateSetName, newStatesetName);
        assertTrue(ExplorerTreeHelper.treeItemExists(pathToWorkflowPackage, newStatesetName));
        assertTrue(ExplorerTreeHelper.treeItemDoesNotExists(pathToWorkflowPackage, stateSetName));
        EditorHelper.openEditor(pathToWorkflowPackage, newStatesetName);
        Text nameTextField = (Text) EditorHelper.getWidgetFromEditor("NAME", newStatesetName);
        assertEquals(newStatesetName, uitu.getTextTester().getText(nameTextField));
        ExplorerTreeHelper.deleteItem(pathToWorkflowPackage + "/" + newStatesetName);
        assertTrue(ExplorerTreeHelper.treeItemDoesNotExists(pathToWorkflowPackage, newStatesetName));
        ExplorerTreeHelper.deleteItem(Helper.getRootElementName() + "/" + processName);
    }
}
