package tests.com.ivis.xprocess.abbot.vcs;

import java.util.Hashtable;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.TreeItem;
import tests.com.ivis.xprocess.ui.util.DatasourceHelper;
import tests.com.ivis.xprocess.ui.util.EditorHelper;
import tests.com.ivis.xprocess.ui.util.ExplorerTreeHelper;
import tests.com.ivis.xprocess.ui.util.Helper;
import tests.com.ivis.xprocess.ui.util.OrganizationHelper;
import tests.com.ivis.xprocess.ui.util.PersonalPlannerHelper;
import tests.com.ivis.xprocess.ui.util.ProjectHelper;
import tests.com.ivis.xprocess.ui.util.TaskHelper;
import tests.com.ivis.xprocess.ui.util.UITestUtil;
import tests.com.ivis.xprocess.ui.util.VCSHelper;
import abbot.finder.matchers.swt.NameMatcher;
import abbot.finder.swt.MultipleWidgetsFoundException;
import abbot.finder.swt.WidgetNotFoundException;
import abbot.tester.swt.WidgetTester;
import com.ivis.xprocess.ui.UIPlugin;
import com.ivis.xprocess.ui.properties.ActionMessages;
import com.ivis.xprocess.ui.properties.DialogMessages;
import com.ivis.xprocess.ui.util.DatasourceUtil;
import com.ivis.xprocess.ui.util.TestHarness;
import com.ivis.xprocess.util.Day;

public class TestStickyAssignments extends TestVCSInteraction {

    private UITestUtil uitu;

    private String personName;

    private String projectName;

    public TestStickyAssignments(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        super.setUp();
        uitu = UITestUtil.getInstance();
        uitu.ensureXProcessIsStarted();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        UIPlugin.closeDataSource(null);
        VCSHelper.nukeTopLevelFolder(repositoryURL, USERNAME, PASSWORD, "testStickyAssignmentsWithManualCommit");
        DatasourceHelper.nukeData(DatasourceUtil.getDatasourceRoot() + "\\testStickyAssignmentsWithManualCommit");
        DatasourceHelper.nukeData(DatasourceUtil.getDatasourceRoot() + "\\testStickyAssignmentsWithManualCommit2");
        VCSHelper.nukeTopLevelFolder(repositoryURL, USERNAME, PASSWORD, "testStickyAssignmentsWithAutoCommit");
        DatasourceHelper.nukeData(DatasourceUtil.getDatasourceRoot() + "\\testStickyAssignmentsWithAutoCommit");
        DatasourceHelper.nukeData(DatasourceUtil.getDatasourceRoot() + "\\testStickyAssignmentsWithAutoCommit2");
        VCSHelper.nukeTopLevelFolder(repositoryURL, USERNAME, PASSWORD, "testStickyOverheadAssignmentsWithAutoCommit");
        DatasourceHelper.nukeData(DatasourceUtil.getDatasourceRoot() + "\\testStickyOverheadAssignmentsWithAutoCommit");
        DatasourceHelper.nukeData(DatasourceUtil.getDatasourceRoot() + "\\testStickyOverheadAssignmentsWithAutoCommit2");
        VCSHelper.nukeTopLevelFolder(repositoryURL, USERNAME, PASSWORD, "testStickyClosedTaskAssignmentsWithAutoCommit");
        DatasourceHelper.nukeData(DatasourceUtil.getDatasourceRoot() + "\\testStickyClosedTaskAssignmentsWithAutoCommit");
        DatasourceHelper.nukeData(DatasourceUtil.getDatasourceRoot() + "\\testStickyClosedTaskAssignmentsWithAutoCommit2");
        System.setProperty(Helper.PRETEND_TO_BE_AT_MIDNIGHT, "false");
        VCSHelper.enableAutoCommit(true);
        VCSHelper.enableAutoUpdate(true);
        Helper.enableAutoScheduling(true);
    }

    private void setUpProject() throws WidgetNotFoundException, MultipleWidgetsFoundException {
        DatasourceHelper.makeServerScheduler("The Scheduler Server", true);
        assertingThatSchedulerServerIsSetup("The Scheduler Server");
        personName = Helper.getNewPersonName();
        OrganizationHelper.createPerson(personName, OrganizationHelper.getDefaultOrganization());
        projectName = Helper.getNewProjectName();
        ProjectHelper.createProject(projectName);
        ProjectHelper.addPersonToProject(projectName, personName);
    }

    /**
     * Creation of an Active Task that will have stickies, and the VCS
     * interaction is manual.
     *
     * @throws WidgetNotFoundException
     * @throws MultipleWidgetsFoundException
     */
    public void testStickAssignmentsWithManualCommit() throws WidgetNotFoundException, MultipleWidgetsFoundException {
        Helper.abbotSystemOut("testStickyAssignmentsWithManualCommit");
        VCSHelper.enableAutoCommit(false);
        VCSHelper.enableAutoUpdate(false);
        Helper.enableAutoScheduling(false);
        DatasourceHelper.switchToNewDatasource("testStickyAssignmentsWithManualCommit", repositoryURL, true, false);
        uitu.delay(Helper.SHORT_DELAY);
        setUpProject();
        Day startDay = ProjectHelper.startFromMonday(Helper.getRootElementName(), projectName, false);
        String activeTaskName = Helper.getNewTaskName();
        TaskHelper.createTask(activeTaskName, Helper.getRootElementName() + "/" + projectName);
        ProjectHelper.scheduleProject(projectName);
        String pathToProjectRole = Helper.getRootElementName() + "/" + projectName + "/" + Helper.getRootTaskPlain() + "/" + activeTaskName + "/" + personName + " - Participant";
        TaskHelper.makeActive(pathToProjectRole);
        ProjectHelper.scheduleProject(projectName);
        System.setProperty(Helper.PRETEND_TO_BE_AT_MIDNIGHT, "true");
        ProjectHelper.scheduleProject(projectName);
        VCSHelper.commitAll(true);
        DatasourceHelper.switchToNewDatasource("testStickyAssignmentsWithManualCommit2", repositoryURL, "testStickyAssignmentsWithManualCommit", true, true);
        uitu.delay(Helper.MEDIUM_DELAY);
        EditorHelper.openEditor(Helper.getRootElementName() + "/" + OrganizationHelper.getDefaultOrganization(), personName);
        PersonalPlannerHelper.moveToWeek(personName, startDay);
        PersonalPlannerHelper.checkRow(0, personName, activeTaskName, "8.0", "", "", "", "", "", "");
    }

    /**
     * Creation of an Active Task that will have stickies, and the VCS
     * interaction is auto.
     *
     * @throws WidgetNotFoundException
     * @throws MultipleWidgetsFoundException
     */
    public void testStickAssignmentsWithAutoCommit() throws WidgetNotFoundException, MultipleWidgetsFoundException {
        Helper.abbotSystemOut("testStickyAssignmentsWithAutoCommit");
        VCSHelper.enableAutoUpdate(false);
        Helper.enableAutoScheduling(false);
        DatasourceHelper.switchToNewDatasource("testStickyAssignmentsWithAutoCommit", repositoryURL, true, false);
        uitu.delay(Helper.SHORT_DELAY);
        setUpProject();
        Day startDay = ProjectHelper.startFromMonday(Helper.getRootElementName(), projectName, false);
        String activeTaskName = Helper.getNewTaskName();
        TaskHelper.createTask(activeTaskName, Helper.getRootElementName() + "/" + projectName);
        VCSHelper.commitAll(true);
        ProjectHelper.scheduleProject(projectName);
        String pathToProjectRole = Helper.getRootElementName() + "/" + projectName + "/" + Helper.getRootTaskPlain() + "/" + activeTaskName + "/" + personName + " - Participant";
        TaskHelper.makeActive(pathToProjectRole);
        ProjectHelper.scheduleProject(projectName);
        System.setProperty(Helper.PRETEND_TO_BE_AT_MIDNIGHT, "true");
        ProjectHelper.scheduleProject(projectName);
        VCSHelper.commitAll(true);
        Helper.delayUntilAllSavesCompleted();
        DatasourceHelper.switchToNewDatasource("testStickyAssignmentsWithAutoCommit2", repositoryURL, "testStickyAssignmentsWithAutoCommit", true, true);
        uitu.delay(Helper.SHORT_DELAY);
        EditorHelper.openEditor(Helper.getRootElementName() + "/" + OrganizationHelper.getDefaultOrganization(), personName);
        PersonalPlannerHelper.moveToWeek(personName, startDay);
        PersonalPlannerHelper.checkRow(0, personName, activeTaskName, "8.0", "", "", "", "", "", "");
    }

    /**
     * Creation of an Overhead Task that will have stickies, and the VCS
     * interaction is auto.
     *
     * @throws WidgetNotFoundException
     * @throws MultipleWidgetsFoundException
     */
    public void testStickAssignmentsWithAutoCommit2() throws WidgetNotFoundException, MultipleWidgetsFoundException {
        Helper.abbotSystemOut("testStickyOverheadAssignmentsWithAutoCommit");
        VCSHelper.enableAutoUpdate(false);
        Helper.enableAutoScheduling(false);
        DatasourceHelper.switchToNewDatasource("testStickyOverheadAssignmentsWithAutoCommit", repositoryURL, true, false);
        uitu.delay(Helper.SHORT_DELAY);
        setUpProject();
        Day startDay = ProjectHelper.startFromMonday(Helper.getRootElementName(), projectName, false);
        String overheadTaskName = Helper.getNewTaskName();
        TaskHelper.createOverheadTask(overheadTaskName, projectName, true);
        VCSHelper.commitAll(true);
        ProjectHelper.scheduleProject(projectName);
        System.setProperty(Helper.PRETEND_TO_BE_AT_MIDNIGHT, "true");
        ProjectHelper.scheduleProject(projectName);
        VCSHelper.commitAll(true);
        Helper.delayUntilAllSavesCompleted();
        DatasourceHelper.switchToNewDatasource("testStickyOverheadAssignmentsWithAutoCommit2", repositoryURL, "testStickyOverheadAssignmentsWithAutoCommit", true, true);
        uitu.delay(Helper.SHORT_DELAY);
        EditorHelper.openEditor(Helper.getRootElementName() + "/" + OrganizationHelper.getDefaultOrganization(), personName);
        PersonalPlannerHelper.moveToWeek(personName, startDay);
        PersonalPlannerHelper.checkRow(0, personName, overheadTaskName, "1.6", "", "", "", "", "", "");
    }

    /**
     * Creation of an Closed Task that will have stickies, and the VCS
     * interaction is auto.
     *
     * @throws WidgetNotFoundException
     * @throws MultipleWidgetsFoundException
     */
    public void testStickAssignmentsWithAutoCommit3() throws WidgetNotFoundException, MultipleWidgetsFoundException {
        Helper.abbotSystemOut("testStickyClosedTaskAssignmentsWithAutoCommit");
        VCSHelper.enableAutoUpdate(false);
        Helper.enableAutoScheduling(false);
        DatasourceHelper.switchToNewDatasource("testStickyClosedTaskAssignmentsWithAutoCommit", repositoryURL, true, false);
        uitu.delay(Helper.SHORT_DELAY);
        setUpProject();
        Day startDay = ProjectHelper.startFromMonday(Helper.getRootElementName(), projectName, false);
        String closedTaskName = Helper.getNewTaskName();
        TaskHelper.createTask(closedTaskName, Helper.getRootElementName() + "/" + projectName);
        ProjectHelper.scheduleProject(projectName);
        TaskHelper.closeTaskViaContextMenu(projectName, closedTaskName, true);
        System.setProperty(Helper.PRETEND_TO_BE_AT_MIDNIGHT, "true");
        ProjectHelper.scheduleProject(projectName);
        VCSHelper.delayUntilCommitCompleted();
        Helper.delayUntilAllSavesCompleted();
        DatasourceHelper.switchToNewDatasource("testStickyClosedTaskAssignmentsWithAutoCommit2", repositoryURL, "testStickyClosedTaskAssignmentsWithAutoCommit", true, true);
        uitu.delay(Helper.SHORT_DELAY);
        EditorHelper.openEditor(Helper.getRootElementName() + "/" + OrganizationHelper.getDefaultOrganization(), personName);
        PersonalPlannerHelper.moveToWeek(personName, startDay);
    }

    private void assertingThatSchedulerServerIsSetup(String serverSchedulerName) throws WidgetNotFoundException, MultipleWidgetsFoundException {
        TreeItem rootTreeItem = ExplorerTreeHelper.getTreeItem(Helper.getRootElementName(), "/");
        if (rootTreeItem == null) {
            throw new WidgetNotFoundException("Unable to locate the tree item within the tree");
        }
        uitu.getTreeItemTester().actionClick(rootTreeItem, 0, 0, "BUTTON3");
        uitu.delay(Helper.SHORT_DELAY);
        Menu explorerTreePopupMenu = uitu.getTreeTester().getMenu(ExplorerTreeHelper.getExplorerTree());
        final Hashtable<?, ?> menuItems = uitu.getMenuTester().hashMenuItemsByText(explorerTreePopupMenu);
        final MenuItem menuItem = (MenuItem) menuItems.get(ActionMessages.action_properties);
        uitu.getMenuItemTester().actionClick(menuItem);
        uitu.delay(Helper.SHORT_DELAY);
        WidgetTester.waitForFrameShowing(DialogMessages.datasource_properties);
        Text schedulerServerNameField = (Text) uitu.getBasicFinder().find(new NameMatcher(TestHarness.SCHEDULER_SERVER_NAMEFIELD));
        if (!uitu.getTextTester().getText(schedulerServerNameField).equals(serverSchedulerName)) {
            uitu.clickOKButton();
            assertEquals(uitu.getTextTester().getText(schedulerServerNameField), serverSchedulerName);
        }
        Button schedulerServerSwitch = (Button) uitu.getBasicFinder().find(new NameMatcher(TestHarness.SCHEDULER_SERVER_SWITCH));
        if (!uitu.getButtonTester().getSelection(schedulerServerSwitch)) {
            uitu.clickOKButton();
            assertTrue(uitu.getButtonTester().getSelection(schedulerServerSwitch));
        }
        uitu.clickOKButton();
    }
}
