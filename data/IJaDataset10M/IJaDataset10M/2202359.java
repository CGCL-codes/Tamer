package tests.com.ivis.xprocess.abbot.explorerview;

import junit.extensions.ForkedPDETestCase;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import tests.com.ivis.xprocess.ui.util.DatasourceHelper;
import tests.com.ivis.xprocess.ui.util.EditorHelper;
import tests.com.ivis.xprocess.ui.util.ElementCreationException;
import tests.com.ivis.xprocess.ui.util.ExplorerTreeHelper;
import tests.com.ivis.xprocess.ui.util.Helper;
import tests.com.ivis.xprocess.ui.util.OrganizationHelper;
import tests.com.ivis.xprocess.ui.util.ProcessHelper;
import tests.com.ivis.xprocess.ui.util.ProjectHelper;
import tests.com.ivis.xprocess.ui.util.RequiredResourceHelper;
import tests.com.ivis.xprocess.ui.util.SpecificHierarchy;
import tests.com.ivis.xprocess.ui.util.TaskHelper;
import tests.com.ivis.xprocess.ui.util.UITestUtil;
import abbot.finder.matchers.swt.NameMatcher;
import abbot.finder.swt.BasicFinder;
import abbot.finder.swt.MultipleWidgetsFoundException;
import abbot.finder.swt.WidgetNotFoundException;
import abbot.tester.swt.WidgetTester;
import com.ivis.xprocess.core.impl.PortfolioImpl;
import com.ivis.xprocess.ui.UIPlugin;
import com.ivis.xprocess.ui.properties.ActionMessages;
import com.ivis.xprocess.ui.properties.MakeActiveInActiveMessages;
import com.ivis.xprocess.ui.util.IconManager;
import com.ivis.xprocess.ui.util.TestHarness;
import com.ivis.xprocess.ui.util.debug.DebugUtil;
import com.ivis.xprocess.ui.widgets.CheckboxTreeViewerComposite;
import com.ivis.xprocess.util.License;
import com.ivis.xprocess.util.LicensingEnums.LicenseType;

public class TestMakeActiveInActive extends ForkedPDETestCase {

    private UITestUtil uitu;

    public TestMakeActiveInActive(String name) {
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

    /**
     * Create a Task within a Project Add a Resource to the project By adding
     * the Resource the Project should have been schedule so that the Task is
     * assigned to the Resource Make the Task active Check the Task is Active
     * Reload Check the Task is Active Make the Task inactive Check the Task is
     * InActive Reload Check the Task is InActive
     *
     * The action of making the task active/inactive should not involve the
     * wizard since the Task should only have one Required Resource.
     *
     * @throws MultipleWidgetsFoundException
     * @throws WidgetNotFoundException
     */
    public void testSimpleMakeActiveInActive() throws WidgetNotFoundException, MultipleWidgetsFoundException {
        uitu.ensureDatasourceAvailable("testSimpleMakeActiveInActive");
        String datasourceName = UIPlugin.getDataSource().getLabel();
        String personName = Helper.getNewPersonName();
        OrganizationHelper.createPerson(personName, OrganizationHelper.getDefaultOrganization());
        assertTrue("person created", ExplorerTreeHelper.treeItemExists(Helper.getRootElementName() + "/" + OrganizationHelper.getDefaultOrganization(), personName));
        String projectName = Helper.getNewProjectName();
        ProjectHelper.createProject(projectName);
        String taskName = Helper.getNewTaskName();
        TaskHelper.createTask(taskName, Helper.getRootElementName() + "/" + projectName);
        ProjectHelper.addPersonToProject(projectName, personName);
        String pathToTask = Helper.getRootElementName() + "/" + projectName + "/" + Helper.getRootTaskPlain() + "/" + taskName;
        TaskHelper.makeActive(pathToTask);
        uitu.delay(Helper.MEDIUM_DELAY);
        EditorHelper.switchToTab("RESOURCESTAB");
        uitu.delay(Helper.SHORT_DELAY);
        RequiredResourceHelper.checkAssignedResourceRow(taskName, new String[] { "Participant", personName, "MANUAL", "0.0", "16.0", "16.0" }, 0);
        Table assignedResourceTable = (Table) EditorHelper.getWidgetFromEditor(TestHarness.ASSIGNED_RESOURCES_TABLE, taskName);
        TableItem firstRow = uitu.getTableTester().getItem(assignedResourceTable, 0);
        Image activeColumnImage = uitu.getTableItemTester().getImage(firstRow, 0);
        assertNotNull(activeColumnImage);
        assertTrue(ExplorerTreeHelper.compareImages(IconManager.getInstance().getActiveImage(), activeColumnImage, false));
        DatasourceHelper.reloadDatasource(datasourceName);
        EditorHelper.openEditor(Helper.getRootElementName() + "/" + projectName + "/" + Helper.getRootTaskPlain(), taskName);
        EditorHelper.switchToTab("RESOURCETAB");
        uitu.delay(Helper.SHORT_DELAY);
        RequiredResourceHelper.checkAssignedResourceRow(taskName, new String[] { "Participant", personName, "MANUAL", "0.0", "16.0", "16.0" }, 0);
        assignedResourceTable = (Table) EditorHelper.getWidgetFromEditor(TestHarness.ASSIGNED_RESOURCES_TABLE, taskName);
        firstRow = uitu.getTableTester().getItem(assignedResourceTable, 0);
        activeColumnImage = uitu.getTableItemTester().getImage(firstRow, 0);
        assertNotNull(activeColumnImage);
        assertTrue(ExplorerTreeHelper.compareImages(IconManager.getInstance().getActiveImage(), activeColumnImage, false));
        TaskHelper.makeInActive(pathToTask);
        RequiredResourceHelper.checkAssignedResourceRow(taskName, new String[] { "Participant", personName, "MANUAL", "0.0", "16.0", "16.0" }, 0);
        assignedResourceTable = (Table) EditorHelper.getWidgetFromEditor(TestHarness.ASSIGNED_RESOURCES_TABLE, taskName);
        firstRow = uitu.getTableTester().getItem(assignedResourceTable, 0);
        activeColumnImage = uitu.getTableItemTester().getImage(firstRow, 0);
        assertNull(activeColumnImage);
        uitu.delay(Helper.MEDIUM_DELAY);
        TreeItem taskTreeItem = ExplorerTreeHelper.getTreeItem(pathToTask, "/");
        Image taskImageInExplorerView = uitu.getTreeItemTester().getImage(taskTreeItem);
        assertFalse(ExplorerTreeHelper.compareImages(IconManager.getInstance().getActiveImage(), taskImageInExplorerView, false));
        DatasourceHelper.reloadDatasource(datasourceName);
        uitu.delay(Helper.MEDIUM_DELAY);
        ProjectHelper.delayUntilSchedulingCompleted();
        EditorHelper.openEditor(Helper.getRootElementName() + "/" + projectName + "/" + Helper.getRootTaskPlain(), taskName);
        EditorHelper.switchToTab("RESOURCETAB");
        uitu.delay(Helper.SHORT_DELAY);
        RequiredResourceHelper.checkAssignedResourceRow(taskName, new String[] { "Participant", personName, "MANUAL", "0.0", "16.0", "16.0" }, 0);
        assignedResourceTable = (Table) EditorHelper.getWidgetFromEditor(TestHarness.ASSIGNED_RESOURCES_TABLE, taskName);
        firstRow = uitu.getTableTester().getItem(assignedResourceTable, 0);
        activeColumnImage = uitu.getTableItemTester().getImage(firstRow, 0);
        assertNull(activeColumnImage);
        taskTreeItem = ExplorerTreeHelper.getTreeItem(pathToTask, "/");
        taskImageInExplorerView = uitu.getTreeItemTester().getImage(taskTreeItem);
        assertFalse(ExplorerTreeHelper.compareImages(IconManager.getInstance().getActiveImage(), taskImageInExplorerView, false));
        ExplorerTreeHelper.deleteItem(Helper.getRootElementName() + "/" + projectName);
        ExplorerTreeHelper.deleteItem(Helper.getRootElementName() + "/" + OrganizationHelper.getDefaultOrganization() + "/" + personName);
    }

    /**
     * Create a Task within a Project Set the Max concurrent to two on the Task
     * Add two Resources to the project Adding the Resources will have
     * rescheduled so that the Task is assigned to the Resources Make the Task
     * active Check the Task is Active Reload Check the Task is Active Make the
     * Task Inactive
     *
     * The action of making the task active/inactive should involve the wizard
     * since the Task will have one Required Resource, with a max concurrent of
     * 2. Making the Task InActive will also requirea wizard since all the
     * Required Resources are active.
     *
     * @throws MultipleWidgetsFoundException
     * @throws WidgetNotFoundException
     */
    public void testMakeActiveWithTwoRequiredResources() throws WidgetNotFoundException, MultipleWidgetsFoundException {
        uitu.ensureDatasourceAvailable("testMakeActiveWithTwoRequiredResources");
        String datasourceName = UIPlugin.getDataSource().getLabel();
        String roletypeName = Helper.getNewRoleTypeName();
        ProcessHelper.createRoleType(roletypeName, Helper.getRootProcess());
        String personName1 = Helper.getNewPersonName();
        OrganizationHelper.createPerson(personName1, OrganizationHelper.getDefaultOrganization());
        assertTrue("person 1 created", ExplorerTreeHelper.treeItemExists(Helper.getRootElementName() + "/" + OrganizationHelper.getDefaultOrganization(), personName1));
        String personName2 = Helper.getNewPersonName();
        OrganizationHelper.createPerson(personName2, OrganizationHelper.getDefaultOrganization());
        assertTrue("person 2 created", ExplorerTreeHelper.treeItemExists(Helper.getRootElementName() + "/" + OrganizationHelper.getDefaultOrganization(), personName2));
        OrganizationHelper.createRole(roletypeName, Helper.getRootProcess(), personName2);
        String projectName = Helper.getNewProjectName();
        ProjectHelper.createProject(projectName);
        String taskName = Helper.getNewTaskName();
        TaskHelper.createTask(taskName, Helper.getRootElementName() + "/" + projectName);
        String pathToTask = Helper.getRootElementName() + "/" + projectName + "/" + Helper.getRootTaskPlain() + "/" + taskName;
        RequiredResourceHelper.addRoleTypeToTask(roletypeName, taskName, projectName, Helper.getRootElementName() + "/" + projectName + "/" + Helper.getRootTaskPlain());
        EditorHelper.switchToTab("RESOURCETAB");
        RequiredResourceHelper.setNominalAmount(taskName, 1, 50);
        uitu.delay(Helper.SHORT_DELAY);
        RequiredResourceHelper.checkRequiredResourceRow(taskName, new String[] { "1", "1", "Participant", "50.0", "0.0", "8.0" }, 0);
        RequiredResourceHelper.checkRequiredResourceRow(taskName, new String[] { "2", "1", roletypeName, "50.0", "0.0", "8.0" }, 1);
        ProjectHelper.addPersonToProject(projectName, personName1);
        RequiredResourceHelper.assignRoleToTask(personName1 + " - " + PortfolioImpl.PARTICIPANT_ROLETYPE_NAME, taskName, projectName);
        ProjectHelper.addRoleToProject(projectName, Helper.getRootElementName() + "/" + OrganizationHelper.getDefaultOrganization() + "/" + personName2, personName2, roletypeName, true);
        TaskHelper.makeActive(pathToTask);
        WidgetTester.waitForFrameShowing(MakeActiveInActiveMessages.maketaskactiveinactive_wizard_title);
        uitu.delay(Helper.SHORT_DELAY);
        SpecificHierarchy specificHierarchy = new SpecificHierarchy(uitu.getDisplay(), uitu.getFocusedShell());
        BasicFinder specificFinder = new BasicFinder(specificHierarchy, BasicFinder.BFS);
        CheckboxTreeViewerComposite checkboxList = (CheckboxTreeViewerComposite) specificFinder.find(new NameMatcher(TestHarness.MAKEACTIVE_INACTIVE_CHECKLIST));
        assertEquals(2, uitu.getTreeTester().getItemCount(checkboxList.getTree()));
        Button selectAllButton = (Button) specificFinder.find(new NameMatcher(TestHarness.CHECKBOX_TREE_SELECT_ALL));
        uitu.getTextTester().actionClick(selectAllButton);
        uitu.clickFinishButton();
        uitu.delay(Helper.MEDIUM_DELAY);
        ProjectHelper.delayUntilSchedulingCompleted();
        RequiredResourceHelper.checkAssignedResourceRow(taskName, new String[] { "Participant", personName1, "MANUAL", "0.0", "8.0", "8.0" }, 0);
        Table assignedResourceTable = (Table) EditorHelper.getWidgetFromEditor(TestHarness.ASSIGNED_RESOURCES_TABLE, taskName);
        TableItem firstRow = uitu.getTableTester().getItem(assignedResourceTable, 0);
        Image activeColumnImage = uitu.getTableItemTester().getImage(firstRow, 0);
        assertNotNull(activeColumnImage);
        RequiredResourceHelper.checkAssignedResourceRow(taskName, new String[] { roletypeName, personName2, "MANUAL", "0.0", "8.0", "8.0" }, 1);
        TableItem secondRow = uitu.getTableTester().getItem(assignedResourceTable, 1);
        activeColumnImage = uitu.getTableItemTester().getImage(secondRow, 0);
        assertNotNull(activeColumnImage);
        assertTrue(ExplorerTreeHelper.compareImages(IconManager.getInstance().getActiveImage(), activeColumnImage, false));
        DatasourceHelper.reloadDatasource(datasourceName);
        uitu.delay(Helper.MEDIUM_DELAY);
        ProjectHelper.delayUntilSchedulingCompleted();
        EditorHelper.openEditor(Helper.getRootElementName() + "/" + projectName + "/" + Helper.getRootTaskPlain(), taskName);
        EditorHelper.switchToTab("RESOURCETAB");
        RequiredResourceHelper.checkRequiredResourceRow(taskName, new String[] { "1", "1", "Participant", "50.0", "0.0", "8.0" }, 0);
        RequiredResourceHelper.checkRequiredResourceRow(taskName, new String[] { "2", "1", roletypeName, "50.0", "0.0", "8.0" }, 1);
        RequiredResourceHelper.checkAssignedResourceRow(taskName, new String[] { "Participant", personName1, "MANUAL", "0.0", "8.0", "8.0" }, 0);
        assignedResourceTable = (Table) EditorHelper.getWidgetFromEditor(TestHarness.ASSIGNED_RESOURCES_TABLE, taskName);
        firstRow = uitu.getTableTester().getItem(assignedResourceTable, 0);
        activeColumnImage = uitu.getTableItemTester().getImage(firstRow, 0);
        assertNotNull(activeColumnImage);
        RequiredResourceHelper.checkAssignedResourceRow(taskName, new String[] { roletypeName, personName2, "MANUAL", "0.0", "8.0", "8.0" }, 1);
        secondRow = uitu.getTableTester().getItem(assignedResourceTable, 1);
        activeColumnImage = uitu.getTableItemTester().getImage(secondRow, 0);
        assertNotNull(activeColumnImage);
        assertTrue(ExplorerTreeHelper.compareImages(IconManager.getInstance().getActiveImage(), activeColumnImage, false));
        TaskHelper.makeInActive(pathToTask);
        WidgetTester.waitForFrameShowing(MakeActiveInActiveMessages.maketaskactiveinactive_wizard_title);
        uitu.delay(Helper.SHORT_DELAY);
        specificHierarchy = new SpecificHierarchy(uitu.getDisplay(), uitu.getFocusedShell());
        specificFinder = new BasicFinder(specificHierarchy, BasicFinder.BFS);
        checkboxList = (CheckboxTreeViewerComposite) specificFinder.find(new NameMatcher(TestHarness.MAKEACTIVE_INACTIVE_CHECKLIST));
        assertEquals(2, uitu.getTreeTester().getItemCount(checkboxList.getTree()));
        Button deselectAllButton = (Button) specificFinder.find(new NameMatcher(TestHarness.CHECKBOX_TREE_DESELECT_ALL));
        uitu.getTextTester().actionClick(deselectAllButton);
        uitu.clickFinishButton();
        uitu.delay(Helper.MEDIUM_DELAY);
        ProjectHelper.delayUntilSchedulingCompleted();
        RequiredResourceHelper.checkAssignedResourceRow(taskName, new String[] { "Participant", personName1, "MANUAL", "0.0", "8.0", "8.0" }, 0);
        assignedResourceTable = (Table) EditorHelper.getWidgetFromEditor(TestHarness.ASSIGNED_RESOURCES_TABLE, taskName);
        firstRow = uitu.getTableTester().getItem(assignedResourceTable, 0);
        activeColumnImage = uitu.getTableItemTester().getImage(firstRow, 0);
        assertNull(activeColumnImage);
        uitu.delay(Helper.SHORT_DELAY);
        RequiredResourceHelper.checkAssignedResourceRow(taskName, new String[] { roletypeName, personName2, "MANUAL", "0.0", "8.0", "8.0" }, 1);
        secondRow = uitu.getTableTester().getItem(assignedResourceTable, 1);
        activeColumnImage = uitu.getTableItemTester().getImage(secondRow, 0);
        assertNull(activeColumnImage);
        TreeItem taskTreeItem = ExplorerTreeHelper.getTreeItem(pathToTask, "/");
        Image taskImageInExplorerView = uitu.getTreeItemTester().getImage(taskTreeItem);
        assertFalse(ExplorerTreeHelper.compareImages(IconManager.getInstance().getActiveImage(), taskImageInExplorerView, false));
        ExplorerTreeHelper.deleteItem(Helper.getRootElementName() + "/" + projectName);
        ExplorerTreeHelper.deleteItem(Helper.getRootElementName() + "/" + OrganizationHelper.getDefaultOrganization() + "/" + personName1);
        ExplorerTreeHelper.deleteItem(Helper.getRootElementName() + "/" + OrganizationHelper.getDefaultOrganization() + "/" + personName2);
    }

    public void testCannotMakeNewTaskOnManuallyAssignedTask() throws WidgetNotFoundException, MultipleWidgetsFoundException, ElementCreationException {
        uitu.ensureDatasourceAvailable("testCannotMakeNewTaskOnManuallyAssignedTask");
        String[] strings = ProjectHelper.createWorkingProject();
        String projectName = strings[0];
        String personName = strings[3];
        String taskName = strings[4];
        String organizationName = strings[5];
        EditorHelper.openEditor(Helper.getRootElementName() + "/" + organizationName, personName);
        Tree tree = (Tree) uitu.getBasicFinder().find(new NameMatcher(TestHarness.PERSONAL_PLANNER_TREE));
        TreeItem[] treeItems = uitu.getTreeTester().getItems(tree);
        uitu.getTreeItemTester().actionFocus(treeItems[0]);
        uitu.getTreeItemTester().actionKey(SWT.ARROW_RIGHT, uitu.getDisplay());
        uitu.getTreeItemTester().actionKey(SWT.ARROW_RIGHT, uitu.getDisplay());
        TreeItem[] treeItems2 = uitu.getTreeTester().getSelection(tree);
        uitu.getTreeItemTester().actionClick(treeItems2[0]);
        uitu.delay(700);
        uitu.getTreeItemTester().actionClick(treeItems2[0]);
        uitu.getTreeItemTester().actionKey(SWT.TAB, uitu.getDisplay());
        uitu.getTreeItemTester().actionKey(SWT.DEL, uitu.getDisplay());
        uitu.getTreeItemTester().actionKeyString("4", uitu.getDisplay());
        uitu.getTreeItemTester().actionKey(SWT.CR, uitu.getDisplay());
        EditorHelper.save();
        String[] manuallyAssignedTaskMenu;
        if (License.getLicense().getLicenseType().equals(LicenseType.INTERNAL) && DebugUtil.isInDebug()) {
            manuallyAssignedTaskMenu = new String[] { "Debug Dialog...", "Open XPX", "Personal Planner", "Remove from Plan", "Open Hierarchy Diagram", "Add/Remove GatewayType", "Categorize...", "Importance.Low (Uncategorized)", "Importance.Medium", "Importance.High", "Importance", "Process Diagram", "Reference", "Manage Categories...", "Categories Viewer", "Open...", "New", "Set Targets", "Close Task", "Show In", "Rename", "Set Target End to", "Set Target Start to", "Start Date...", "End Date...", "Forecast Start", "Forecast End", "Forecast End 75", "Forecast End 95", "Target Start", "Remove Target", "Target End", "Delete", "Gantt Chart", "Create Pattern From...", "Make Parent", "Make Active", "UI Action", "Run diagnostics...", "Set effort to match size", "Copy", "Paste", "Monitors..." };
        } else {
            manuallyAssignedTaskMenu = new String[] { "Remove from Plan", "Personal Planner", "Open Hierarchy Diagram", "Add/Remove GatewayType", "Categorize...", "Importance.Low (Uncategorized)", "Importance.Medium", "Importance", "Importance.High", "Process Diagram", "Reference", "Manage Categories...", "Categories Viewer", "Open...", "New", "Set Targets", "Close Task", "Show In", "Rename", "Set Target End to", "Set Target Start to", "Start Date...", "End Date...", "Forecast Start", "Forecast End", "Forecast End 75", "Forecast End 95", "Target Start", "Remove Target", "Target End", "Delete", "Gantt Chart", "Create Pattern From...", "Make Parent", "Make Active", "UI Action", "Run diagnostics...", "Set effort to match size", "Copy", "Paste", "Monitors..." };
        }
        assertTrue(Helper.checkContextMenuItemsOnTreeItem(Helper.getRootElementName() + "/" + projectName + Helper.getRootTask() + taskName, manuallyAssignedTaskMenu, TestHarness.EXPLORER_TREE));
        ProjectHelper.deleteWorkingProject(strings);
    }

    /**
     * While the Task is part of a dirty editor, you should not be able to Make
     * it active.
     *
     * @throws WidgetNotFoundException
     * @throws MultipleWidgetsFoundException
     */
    public void testMakeActiveWhileDirty() throws WidgetNotFoundException, MultipleWidgetsFoundException {
        uitu.ensureDatasourceAvailable("testMakeActiveWhileDirty");
        String personName = Helper.getNewPersonName();
        OrganizationHelper.createPerson(personName, OrganizationHelper.getDefaultOrganization());
        assertTrue("person created", ExplorerTreeHelper.treeItemExists(Helper.getRootElementName() + "/" + OrganizationHelper.getDefaultOrganization(), personName));
        String projectName = Helper.getNewProjectName();
        ProjectHelper.createProject(projectName);
        String taskName = Helper.getNewTaskName();
        TaskHelper.createTask(taskName, Helper.getRootElementName() + "/" + projectName);
        ProjectHelper.addPersonToProject(projectName, personName);
        EditorHelper.openEditor(Helper.getRootElementName() + "/" + projectName + "/" + Helper.getRootTaskPlain(), taskName);
        Text text = (Text) EditorHelper.getWidgetFromEditor("NAME", taskName);
        EditorHelper.setTextInField("change taskname", text);
        String pathToTask = Helper.getRootElementName() + "/" + projectName + "/" + Helper.getRootTaskPlain() + "/" + taskName;
        TaskHelper.makeActive(pathToTask);
        WidgetTester.waitForFrameShowing(ActionMessages.make_active);
        uitu.clickOKButton();
        uitu.delay(Helper.SHORT_DELAY);
        ExplorerTreeHelper.deleteItem(Helper.getRootElementName() + "/" + OrganizationHelper.getDefaultOrganization() + "/" + personName);
        ExplorerTreeHelper.deleteItem(Helper.getRootElementName() + "/" + projectName);
    }

    /**
     * While the Task is part of a dirty editor, you should not be able to Make
     * it inactive.
     *
     * @throws WidgetNotFoundException
     * @throws MultipleWidgetsFoundException
     */
    public void testMakeInActiveWhileDirty() throws WidgetNotFoundException, MultipleWidgetsFoundException {
        uitu.ensureDatasourceAvailable("testMakeInActiveWhileDirty");
        String personName = Helper.getNewPersonName();
        OrganizationHelper.createPerson(personName, OrganizationHelper.getDefaultOrganization());
        assertTrue("person created", ExplorerTreeHelper.treeItemExists(Helper.getRootElementName() + "/" + OrganizationHelper.getDefaultOrganization(), personName));
        String projectName = Helper.getNewProjectName();
        ProjectHelper.createProject(projectName);
        String taskName = Helper.getNewTaskName();
        TaskHelper.createTask(taskName, Helper.getRootElementName() + "/" + projectName);
        ProjectHelper.addPersonToProject(projectName, personName);
        String pathToTask = Helper.getRootElementName() + "/" + projectName + "/" + Helper.getRootTaskPlain() + "/" + taskName;
        TaskHelper.makeActive(pathToTask);
        EditorHelper.openEditor(Helper.getRootElementName() + "/" + projectName + "/" + Helper.getRootTaskPlain(), taskName);
        Text text = (Text) EditorHelper.getWidgetFromEditor("NAME", taskName);
        EditorHelper.setTextInField("change taskname", text);
        TaskHelper.makeInActive(pathToTask);
        WidgetTester.waitForFrameShowing(ActionMessages.make_inactive);
        uitu.clickOKButton();
        uitu.delay(Helper.SHORT_DELAY);
        ExplorerTreeHelper.deleteItem(Helper.getRootElementName() + "/" + OrganizationHelper.getDefaultOrganization() + "/" + personName);
        ExplorerTreeHelper.deleteItem(Helper.getRootElementName() + "/" + projectName);
    }
}
