package tests.com.ivis.xprocess.abbot.editors;

import junit.extensions.ForkedPDETestCase;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Text;
import tests.com.ivis.xprocess.ui.util.EditorHelper;
import tests.com.ivis.xprocess.ui.util.ExplorerTreeHelper;
import tests.com.ivis.xprocess.ui.util.Helper;
import tests.com.ivis.xprocess.ui.util.OrganizationHelper;
import tests.com.ivis.xprocess.ui.util.ProjectHelper;
import tests.com.ivis.xprocess.ui.util.TaskHelper;
import tests.com.ivis.xprocess.ui.util.UITestUtil;
import abbot.finder.swt.MultipleWidgetsFoundException;
import abbot.finder.swt.WidgetNotFoundException;
import abbot.tester.swt.Robot;
import abbot.tester.swt.RunnableWithResult;
import abbot.tester.swt.WidgetTester;
import com.ivis.xprocess.ui.properties.ActionMessages;

/**
 * The purpose of thes tests is to check the Close, Make Active/Inactive and Not
 * to be Scheduled buttons, when the editor is dirty then the 'cannot proceed'
 * dialog should appear when the buttons are used.
 *
 */
public class TestTaskEditorButtons extends ForkedPDETestCase {

    private UITestUtil uitu;

    public TestTaskEditorButtons(String name) {
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

    public void testCloseButton() throws WidgetNotFoundException, MultipleWidgetsFoundException {
        uitu.ensureDatasourceAvailable("testCloseButton");
        uitu.delay(Helper.SHORT_DELAY);
        String projectName = Helper.getNewProjectName();
        ProjectHelper.createProject(projectName);
        String taskName = Helper.getNewTaskName();
        TaskHelper.createTask(taskName, Helper.getRootElementName() + "/" + projectName);
        assertTrue("Task created", ExplorerTreeHelper.treeItemExists(Helper.getRootElementName() + "/" + projectName + "/" + Helper.getRootTaskPlain(), taskName));
        Text taskNameField = (Text) EditorHelper.getWidgetFromEditor("NAME", taskName);
        EditorHelper.setTextInField("changed " + taskName, taskNameField);
        Button closeButton = (Button) EditorHelper.getWidgetFromEditor("CLOSEBUTTON", taskName);
        uitu.getButtonTester().actionClick(closeButton);
        WidgetTester.waitForFrameShowing(ActionMessages.action_close_task);
        uitu.clickOKButton();
        Button notToBeScheduled = (Button) EditorHelper.getWidgetFromEditor("NOTTOBESCEHDULED", taskName);
        assertFalse(uitu.getButtonTester().getSelection(notToBeScheduled));
        ExplorerTreeHelper.deleteItem(Helper.getRootElementName() + "/" + projectName);
    }

    public void testUncloseButton() throws WidgetNotFoundException, MultipleWidgetsFoundException {
        uitu.ensureDatasourceAvailable("testUncloseButton");
        uitu.delay(Helper.SHORT_DELAY);
        String projectName = Helper.getNewProjectName();
        ProjectHelper.createProject(projectName);
        String taskName = Helper.getNewTaskName();
        TaskHelper.createTask(taskName, Helper.getRootElementName() + "/" + projectName);
        assertTrue("Task created", ExplorerTreeHelper.treeItemExists(Helper.getRootElementName() + "/" + projectName + "/" + Helper.getRootTaskPlain(), taskName));
        TaskHelper.closeTaskViaEditorButton(Helper.getRootElementName() + "/" + projectName + "/" + Helper.getRootTaskPlain(), taskName, true);
        uitu.delay(Helper.MEDIUM_DELAY);
        Button uncloseButton = (Button) EditorHelper.getWidgetFromEditor("CLOSEBUTTON", taskName);
        assertEquals(ActionMessages.action_unclose, uitu.getButtonTester().getText(uncloseButton));
        Button notToBeScheduled = (Button) EditorHelper.getWidgetFromEditor("NOTTOBESCEHDULED", taskName);
        assertFalse(isButtonEnabled(notToBeScheduled));
        uitu.delay(Helper.SHORT_DELAY);
        Button makeActiveButton = (Button) EditorHelper.getWidgetFromEditor("MAKEACTIVEINACTIVE", taskName);
        assertFalse(isButtonEnabled(makeActiveButton));
        uitu.delay(Helper.SHORT_DELAY);
        Text taskNameField = (Text) EditorHelper.getWidgetFromEditor("NAME", taskName);
        EditorHelper.setTextInField("changed " + taskName, taskNameField);
        uitu.delay(Helper.SHORT_DELAY);
        uitu.getButtonTester().actionClick(uncloseButton);
        uitu.delay(Helper.SHORT_DELAY);
        WidgetTester.waitForFrameShowing(ActionMessages.action_unclose_task);
        uitu.clickOKButton();
        EditorHelper.save();
        uitu.delay(Helper.SHORT_DELAY);
        uncloseButton = (Button) EditorHelper.getWidgetFromEditor("CLOSEBUTTON", taskName);
        uitu.getButtonTester().actionClick(uncloseButton);
        uitu.delay(Helper.MEDIUM_DELAY);
        notToBeScheduled = (Button) EditorHelper.getWidgetFromEditor("NOTTOBESCEHDULED", taskName);
        assertTrue(isButtonEnabled(notToBeScheduled));
        makeActiveButton = (Button) EditorHelper.getWidgetFromEditor("MAKEACTIVEINACTIVE", taskName);
        assertFalse(isButtonEnabled(makeActiveButton));
        ExplorerTreeHelper.deleteItem(Helper.getRootElementName() + "/" + projectName);
    }

    private boolean isButtonEnabled(final Button button) {
        Boolean result = (Boolean) Robot.syncExec(uitu.getDisplay(), new RunnableWithResult() {

            public Object runWithResult() {
                return button.isEnabled();
            }
        });
        return result;
    }

    public void testNotToBeScheduledButton() throws WidgetNotFoundException, MultipleWidgetsFoundException {
        uitu.ensureDatasourceAvailable("testNotToBeScheduledButton");
        uitu.delay(Helper.SHORT_DELAY);
        String projectName = Helper.getNewProjectName();
        ProjectHelper.createProject(projectName);
        String taskName = Helper.getNewTaskName();
        TaskHelper.createTask(taskName, Helper.getRootElementName() + "/" + projectName);
        assertTrue("Task created", ExplorerTreeHelper.treeItemExists(Helper.getRootElementName() + "/" + projectName + "/" + Helper.getRootTaskPlain(), taskName));
        Button notToBeScheduled = (Button) EditorHelper.getWidgetFromEditor("NOTTOBESCEHDULED", taskName);
        assertEquals(ActionMessages.action_remove_from_plan, uitu.getButtonTester().getText(notToBeScheduled));
        Text taskNameField = (Text) EditorHelper.getWidgetFromEditor("NAME", taskName);
        EditorHelper.setTextInField("changed " + taskName, taskNameField);
        uitu.getButtonTester().actionClick(notToBeScheduled);
        WidgetTester.waitForFrameShowing(ActionMessages.action_remove_from_plan);
        uitu.clickOKButton();
        EditorHelper.save();
        notToBeScheduled = (Button) EditorHelper.getWidgetFromEditor("NOTTOBESCEHDULED", taskName);
        uitu.getButtonTester().actionClick(notToBeScheduled);
        uitu.delay(Helper.MEDIUM_DELAY);
        notToBeScheduled = (Button) EditorHelper.getWidgetFromEditor("NOTTOBESCEHDULED", taskName);
        assertEquals(ActionMessages.action_add_to_plan, uitu.getButtonTester().getText(notToBeScheduled));
        taskNameField = (Text) EditorHelper.getWidgetFromEditor("NAME", taskName);
        EditorHelper.setTextInField("changed again " + taskName, taskNameField);
        uitu.getButtonTester().actionClick(notToBeScheduled);
        WidgetTester.waitForFrameShowing(ActionMessages.action_add_to_plan);
        uitu.clickOKButton();
        EditorHelper.save();
        ExplorerTreeHelper.deleteItem(Helper.getRootElementName() + "/" + projectName);
    }

    public void testMakeActiveButton() throws WidgetNotFoundException, MultipleWidgetsFoundException {
        uitu.ensureDatasourceAvailable("testMakeActiveButton");
        uitu.delay(Helper.SHORT_DELAY);
        String personName = Helper.getNewPersonName();
        OrganizationHelper.createPerson(personName, OrganizationHelper.getDefaultOrganization());
        String projectName = Helper.getNewProjectName();
        ProjectHelper.createProject(projectName);
        String taskName = Helper.getNewTaskName();
        TaskHelper.createTask(taskName, Helper.getRootElementName() + "/" + projectName);
        assertTrue("Task created", ExplorerTreeHelper.treeItemExists(Helper.getRootElementName() + "/" + projectName + "/" + Helper.getRootTaskPlain(), taskName));
        Button makeActiveButton = (Button) EditorHelper.getWidgetFromEditor("MAKEACTIVEINACTIVE", taskName);
        assertFalse(isButtonEnabled(makeActiveButton));
        uitu.delay(Helper.SHORT_DELAY);
        ProjectHelper.addPersonToProject(projectName, personName);
        ProjectHelper.delayUntilAllSavesCompleted();
        ProjectHelper.delayUntilSchedulingCompleted();
        makeActiveButton = (Button) EditorHelper.getWidgetFromEditor("MAKEACTIVEINACTIVE", taskName);
        assertTrue(isButtonEnabled(makeActiveButton));
        uitu.delay(Helper.SHORT_DELAY);
        Text taskNameField = (Text) EditorHelper.getWidgetFromEditor("NAME", taskName);
        EditorHelper.setTextInField("changed " + taskName, taskNameField);
        uitu.getButtonTester().actionClick(makeActiveButton);
        WidgetTester.waitForFrameShowing(ActionMessages.make_active);
        uitu.clickOKButton();
        EditorHelper.save();
        makeActiveButton = (Button) EditorHelper.getWidgetFromEditor("MAKEACTIVEINACTIVE", taskName);
        uitu.getButtonTester().actionClick(makeActiveButton);
        uitu.delay(Helper.MEDIUM_DELAY);
        makeActiveButton = (Button) EditorHelper.getWidgetFromEditor("MAKEACTIVEINACTIVE", taskName);
        assertEquals(ActionMessages.make_inactive, uitu.getButtonTester().getText(makeActiveButton));
        Button notToBeScheduled = (Button) EditorHelper.getWidgetFromEditor("NOTTOBESCEHDULED", taskName);
        assertFalse(isButtonEnabled(notToBeScheduled));
        ExplorerTreeHelper.deleteItem(Helper.getRootElementName() + "/" + projectName);
        ExplorerTreeHelper.deleteItem(Helper.getRootElementName() + "/" + OrganizationHelper.getDefaultOrganization() + "/" + personName);
    }
}
