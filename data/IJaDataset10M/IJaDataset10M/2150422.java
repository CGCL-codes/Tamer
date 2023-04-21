package tests.com.ivis.xprocess.abbot.profileshortcuts;

import junit.extensions.ForkedPDETestCase;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import tests.com.ivis.xprocess.ui.util.EditorHelper;
import tests.com.ivis.xprocess.ui.util.ExplorerTreeHelper;
import tests.com.ivis.xprocess.ui.util.Helper;
import tests.com.ivis.xprocess.ui.util.SpecificHierarchy;
import tests.com.ivis.xprocess.ui.util.UITestUtil;
import tests.com.ivis.xprocess.ui.util.ViewHelper;
import abbot.finder.matchers.swt.NameMatcher;
import abbot.finder.matchers.swt.TextMatcher;
import abbot.finder.swt.BasicFinder;
import abbot.finder.swt.MultipleWidgetsFoundException;
import abbot.finder.swt.WidgetNotFoundException;
import abbot.tester.swt.WidgetTester;
import com.ivis.xprocess.ui.properties.ProcessMessages;
import com.ivis.xprocess.ui.properties.WizardMessages;
import com.ivis.xprocess.ui.util.TestHarness;

/**
 * Process Toolbar tests.
 */
public class TestProcessToolbar extends ForkedPDETestCase {

    private UITestUtil uitu;

    public TestProcessToolbar(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        super.setUp();
        uitu = UITestUtil.getInstance();
        uitu.ensureXProcessIsStarted();
    }

    protected void tearDown() throws Exception {
        ViewHelper.switchToProcessEngineer();
        super.tearDown();
    }

    public void testNewProcess() throws WidgetNotFoundException, MultipleWidgetsFoundException {
        uitu.ensureDatasourceAvailable("testNewProcess");
        String processName = Helper.getNewProcessName();
        ViewHelper.switchToProcessEngineer();
        createProcess(processName);
        Button openExplorerButton = (Button) uitu.getBasicFinder().find(new NameMatcher(TestHarness.TOOLBAR_OPENEXPLORER));
        uitu.getWidgetTester().actionClick(openExplorerButton);
        uitu.delay(Helper.MEDIUM_DELAY);
        assertTrue(ExplorerTreeHelper.treeItemExists(Helper.getRootElementName(), processName));
        ExplorerTreeHelper.deleteItem(Helper.getRootElementName() + "/" + processName);
    }

    public void testNewRoletype() throws WidgetNotFoundException, MultipleWidgetsFoundException {
        uitu.ensureDatasourceAvailable("testNewRoletype");
        String processName = Helper.getNewProcessName();
        String roletypeName = Helper.getNewRoleTypeName();
        ViewHelper.switchToProcessEngineer();
        createProcess(processName);
        Button newButton = (Button) uitu.getBasicFinder().find(new NameMatcher(TestHarness.TOOLBAR_NEWROLETYPEBUTTON));
        uitu.getWidgetTester().actionClick(newButton);
        WidgetTester.waitForFrameShowing(WizardMessages.roletype_wizard_windowtitle);
        uitu.delay(Helper.SHORT_DELAY);
        Button browseButton = (Button) uitu.getBasicFinder().find(new NameMatcher(TestHarness.NEWROLETYPE_PROCESS_BUTTON));
        uitu.getButtonTester().actionClick(browseButton);
        WidgetTester.waitForFrameShowing(WizardMessages.process_selectiondialog_title);
        uitu.delay(Helper.SHORT_DELAY);
        SpecificHierarchy specificHierarchy = new SpecificHierarchy(uitu.getDisplay(), uitu.getFocusedShell());
        BasicFinder specificFinder = new BasicFinder(specificHierarchy, BasicFinder.BFS);
        TableItem tableItem = (TableItem) specificFinder.find(new TextMatcher(processName));
        uitu.getTableTester().actionClick(tableItem, 3, 3);
        uitu.clickOKButton();
        uitu.delay(Helper.SHORT_DELAY);
        Text roletypeNameField = (Text) uitu.getBasicFinder().find(new NameMatcher(TestHarness.NEWROLETYPE_NAMEFIELD));
        uitu.getTextTester().actionEnterText(roletypeNameField, roletypeName);
        uitu.clickFinishButton();
        Button openExplorerButton = (Button) uitu.getBasicFinder().find(new NameMatcher(TestHarness.TOOLBAR_OPENEXPLORER));
        uitu.getWidgetTester().actionClick(openExplorerButton);
        uitu.delay(Helper.MEDIUM_DELAY);
        assertTrue(ExplorerTreeHelper.treeItemExists(Helper.getRootElementName(), processName));
        assertTrue(ExplorerTreeHelper.treeItemExists(Helper.getRootElementName() + "/" + processName, roletypeName));
        ExplorerTreeHelper.deleteItem(Helper.getRootElementName() + "/" + processName);
    }

    public void testNewPattern() throws WidgetNotFoundException, MultipleWidgetsFoundException {
        uitu.ensureDatasourceAvailable("testNewPattern");
        String processName = Helper.getNewProcessName();
        String patternName = Helper.getNewPatternName();
        ViewHelper.switchToProcessEngineer();
        createProcess(processName);
        Button newButton = (Button) uitu.getBasicFinder().find(new NameMatcher(TestHarness.TOOLBAR_NEWPATTERNBUTTON));
        uitu.getWidgetTester().actionClick(newButton);
        WidgetTester.waitForFrameShowing(WizardMessages.pattern_wizard_windowtitle);
        uitu.delay(Helper.SHORT_DELAY);
        Button browseButton = (Button) uitu.getBasicFinder().find(new NameMatcher(TestHarness.NEWPATTERN_PROCESS_BUTTON));
        uitu.getButtonTester().actionClick(browseButton);
        WidgetTester.waitForFrameShowing(WizardMessages.process_selectiondialog_title);
        uitu.delay(Helper.SHORT_DELAY);
        SpecificHierarchy specificHierarchy = new SpecificHierarchy(uitu.getDisplay(), uitu.getFocusedShell());
        BasicFinder specificFinder = new BasicFinder(specificHierarchy, BasicFinder.BFS);
        TableItem tableItem = (TableItem) specificFinder.find(new TextMatcher(processName));
        uitu.getTableTester().actionClick(tableItem, 3, 3);
        uitu.clickOKButton();
        uitu.delay(Helper.SHORT_DELAY);
        Text patternNameField = (Text) uitu.getBasicFinder().find(new NameMatcher(TestHarness.NEW_PATTERN_NAMEFIELD));
        uitu.getTextTester().actionEnterText(patternNameField, patternName);
        uitu.clickFinishButton();
        Button openExplorerButton = (Button) uitu.getBasicFinder().find(new NameMatcher(TestHarness.TOOLBAR_OPENEXPLORER));
        uitu.getWidgetTester().actionClick(openExplorerButton);
        uitu.delay(Helper.MEDIUM_DELAY);
        assertTrue(ExplorerTreeHelper.treeItemExists(Helper.getRootElementName(), processName));
        assertTrue(ExplorerTreeHelper.treeItemExists(Helper.getRootElementName() + "/" + processName, patternName));
        ExplorerTreeHelper.deleteItem(Helper.getRootElementName() + "/" + processName);
    }

    public void testNewGatewaytype() throws WidgetNotFoundException, MultipleWidgetsFoundException {
        uitu.ensureDatasourceAvailable("testNewGatewaytype");
        String processName = Helper.getNewProcessName();
        String gatewaytypeName = Helper.getNewGatewayTypeName();
        ViewHelper.switchToProcessEngineer();
        createProcess(processName);
        Button newButton = (Button) uitu.getBasicFinder().find(new NameMatcher(TestHarness.TOOLBAR_NEWGATEWAYTYPEBUTTON));
        uitu.getWidgetTester().actionClick(newButton);
        WidgetTester.waitForFrameShowing(WizardMessages.gatewaytype_wizard_title);
        uitu.delay(Helper.SHORT_DELAY);
        Button browseButton = (Button) uitu.getBasicFinder().find(new NameMatcher(TestHarness.NEWGATEWAYTYPE_PROCESS_BUTTON));
        uitu.getButtonTester().actionClick(browseButton);
        WidgetTester.waitForFrameShowing(WizardMessages.process_selectiondialog_title);
        uitu.delay(Helper.SHORT_DELAY);
        SpecificHierarchy specificHierarchy = new SpecificHierarchy(uitu.getDisplay(), uitu.getFocusedShell());
        BasicFinder specificFinder = new BasicFinder(specificHierarchy, BasicFinder.BFS);
        TableItem tableItem = (TableItem) specificFinder.find(new TextMatcher(processName));
        uitu.getTableTester().actionClick(tableItem, 3, 3);
        uitu.clickOKButton();
        uitu.delay(Helper.SHORT_DELAY);
        Text gatewaytypeNameField = (Text) uitu.getBasicFinder().find(new NameMatcher(TestHarness.NEWGATEWAYTYPE_NAMEFIELD));
        uitu.getTextTester().actionEnterText(gatewaytypeNameField, gatewaytypeName);
        uitu.clickFinishButton();
        Button openExplorerButton = (Button) uitu.getBasicFinder().find(new NameMatcher(TestHarness.TOOLBAR_OPENEXPLORER));
        uitu.getWidgetTester().actionClick(openExplorerButton);
        uitu.delay(Helper.MEDIUM_DELAY);
        assertTrue(ExplorerTreeHelper.treeItemExists(Helper.getRootElementName(), processName));
        assertTrue(ExplorerTreeHelper.treeItemExists(Helper.getRootElementName() + "/" + processName, gatewaytypeName));
        ExplorerTreeHelper.deleteItem(Helper.getRootElementName() + "/" + processName);
    }

    public void testSwitchDatasourceShortcut() throws WidgetNotFoundException, MultipleWidgetsFoundException {
        uitu.ensureDatasourceAvailable("testSwitchDatasourceShortcut");
        ViewHelper.switchToProcessEngineer();
        Button newButton = (Button) uitu.getBasicFinder().find(new NameMatcher(TestHarness.TOOLBAR_SWITCHDATASOURCEBUTTON));
        uitu.getWidgetTester().actionClick(newButton);
        uitu.delay(Helper.SHORT_DELAY);
        WidgetTester.waitForFrameShowing(WizardMessages.switch_xprocesssource_wizard_windowtitle);
        uitu.delay(Helper.SHORT_DELAY);
        uitu.clickCancelButton();
    }

    private void createProcess(String processName) throws WidgetNotFoundException, MultipleWidgetsFoundException {
        Button newButton = (Button) uitu.getBasicFinder().find(new NameMatcher(TestHarness.TOOLBAR_NEWPROCESSBUTTON));
        uitu.getWidgetTester().actionClick(newButton);
        WidgetTester.waitForFrameShowing(ProcessMessages.process_wizard_windowtitle);
        uitu.delay(Helper.SHORT_DELAY);
        SpecificHierarchy specificHierarchy = new SpecificHierarchy(uitu.getDisplay(), uitu.getFocusedShell());
        BasicFinder specificFinder = new BasicFinder(specificHierarchy, BasicFinder.BFS);
        Text processfieldName = (Text) specificFinder.find(new NameMatcher(TestHarness.NEWPROCESS_NAMEFIELD));
        EditorHelper.setTextInField(processName, processfieldName);
        uitu.clickFinishButton();
        uitu.delay(Helper.SHORT_DELAY);
    }
}
