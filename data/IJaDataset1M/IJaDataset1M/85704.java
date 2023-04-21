package tests.com.ivis.xprocess.abbot.editors;

import java.util.Hashtable;
import junit.extensions.ForkedPDETestCase;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import tests.com.ivis.xprocess.ui.util.EditorHelper;
import tests.com.ivis.xprocess.ui.util.ExplorerTreeHelper;
import tests.com.ivis.xprocess.ui.util.GatewayTypeHelper;
import tests.com.ivis.xprocess.ui.util.Helper;
import tests.com.ivis.xprocess.ui.util.UITestUtil;
import abbot.finder.matchers.swt.NameMatcher;
import abbot.finder.swt.MultipleWidgetsFoundException;
import abbot.finder.swt.WidgetNotFoundException;
import com.ivis.xprocess.ui.properties.ActionMessages;
import com.ivis.xprocess.ui.util.TestHarness;

public class TestGatewayTypeEditor extends ForkedPDETestCase {

    private UITestUtil uitu;

    public TestGatewayTypeEditor(String name) {
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

    public void testGatewayTypeQuestionCreation() throws WidgetNotFoundException, MultipleWidgetsFoundException {
        uitu.ensureDatasourceAvailable("testGatewayTypeQuestionCreation");
        String gatewayTypeName = Helper.getNewGatewayTypeName();
        String processName = Helper.getRootProcess();
        GatewayTypeHelper.createGatewayType(gatewayTypeName, processName);
        assertTrue("gateway created", ExplorerTreeHelper.treeItemExists(Helper.getRootElementName() + "/" + processName, gatewayTypeName));
        Button addQuestionButton = (Button) uitu.getBasicFinder().find(new NameMatcher(TestHarness.ADDQUESTIONBUTTON));
        uitu.getButtonTester().actionClick(addQuestionButton);
        uitu.delay(Helper.SHORT_DELAY);
        uitu.getButtonTester().actionClick(addQuestionButton);
        Table questionTable = (Table) uitu.getBasicFinder().find(new NameMatcher(TestHarness.QUESTIONTABLE));
        assertEquals(2, uitu.getTableTester().getItemCount(questionTable));
        GatewayTypeHelper.checkQuestionRow(0, new String[] { "1", "New Question1" });
        GatewayTypeHelper.checkQuestionRow(1, new String[] { "2", "New Question2" });
        EditorHelper.save();
        GatewayTypeHelper.changeQuestionText(gatewayTypeName, 0, "Test 1");
        GatewayTypeHelper.checkQuestionRow(0, new String[] { "1", "Test 1" });
        GatewayTypeHelper.checkQuestionRow(1, new String[] { "2", "New Question2" });
        EditorHelper.save();
        ExplorerTreeHelper.deleteItem(Helper.getRootElementName() + "/" + processName + "/" + gatewayTypeName);
        assertTrue(ExplorerTreeHelper.treeItemDoesNotExists(Helper.getRootElementName() + "/" + processName, gatewayTypeName));
    }

    public void testGatewayTypeQuestionRemoval() throws WidgetNotFoundException, MultipleWidgetsFoundException {
        uitu.ensureDatasourceAvailable("testGatewayTypeQuestionRemoval");
        String gatewayTypeName = Helper.getNewGatewayTypeName();
        String processName = Helper.getRootProcess();
        GatewayTypeHelper.createGatewayType(gatewayTypeName, processName);
        assertTrue("gateway created", ExplorerTreeHelper.treeItemExists(Helper.getRootElementName() + "/" + processName, gatewayTypeName));
        Button addQuestionButton = (Button) uitu.getBasicFinder().find(new NameMatcher(TestHarness.ADDQUESTIONBUTTON));
        uitu.getButtonTester().actionClick(addQuestionButton);
        uitu.delay(Helper.SHORT_DELAY);
        uitu.getButtonTester().actionClick(addQuestionButton);
        Table questionTable = (Table) uitu.getBasicFinder().find(new NameMatcher(TestHarness.QUESTIONTABLE));
        assertEquals(2, uitu.getTableTester().getItemCount(questionTable));
        GatewayTypeHelper.checkQuestionRow(0, new String[] { "1", "New Question1" });
        GatewayTypeHelper.checkQuestionRow(1, new String[] { "2", "New Question2" });
        EditorHelper.save();
        TableItem actualRow = uitu.getTableTester().getItem(questionTable, 0);
        uitu.getTableItemTester().actionClick(actualRow, 3, 3, "BUTTON3");
        uitu.getTableItemTester().actionClick(actualRow, 3, 3, "BUTTON3");
        uitu.delay(Helper.SHORT_DELAY);
        Menu menu = uitu.getTableTester().getMenu(questionTable);
        Hashtable<?, ?> menuItems = uitu.getMenuTester().hashMenuItemsByText(menu);
        MenuItem menuItem = (MenuItem) menuItems.get(ActionMessages.action_remove);
        if (menuItem == null) {
            throw new WidgetNotFoundException("Unable to locate the Delete Question menu - " + ActionMessages.action_remove);
        }
        uitu.getMenuItemTester().actionClick(menuItem);
        uitu.delay(Helper.SHORT_DELAY);
        assertEquals(1, uitu.getTableTester().getItemCount(questionTable));
        GatewayTypeHelper.checkQuestionRow(0, new String[] { "2", "New Question2" });
        EditorHelper.save();
        ExplorerTreeHelper.deleteItem(Helper.getRootElementName() + "/" + processName + "/" + gatewayTypeName);
        assertTrue(ExplorerTreeHelper.treeItemDoesNotExists(Helper.getRootElementName() + "/" + processName, gatewayTypeName));
    }

    public void testGatewayTypeQuestionRenumbering() throws WidgetNotFoundException, MultipleWidgetsFoundException {
        uitu.ensureDatasourceAvailable("testGatewayTypeQuestionRenumbering");
        String gatewayTypeName = Helper.getNewGatewayTypeName();
        String processName = Helper.getRootProcess();
        GatewayTypeHelper.createGatewayType(gatewayTypeName, processName);
        assertTrue("gateway created", ExplorerTreeHelper.treeItemExists(Helper.getRootElementName() + "/" + processName, gatewayTypeName));
        Button addQuestionButton = (Button) uitu.getBasicFinder().find(new NameMatcher(TestHarness.ADDQUESTIONBUTTON));
        uitu.getButtonTester().actionClick(addQuestionButton);
        uitu.delay(Helper.SHORT_DELAY);
        uitu.getButtonTester().actionClick(addQuestionButton);
        Table questionTable = (Table) uitu.getBasicFinder().find(new NameMatcher(TestHarness.QUESTIONTABLE));
        assertEquals(2, uitu.getTableTester().getItemCount(questionTable));
        GatewayTypeHelper.checkQuestionRow(0, new String[] { "1", "New Question1" });
        GatewayTypeHelper.checkQuestionRow(1, new String[] { "2", "New Question2" });
        EditorHelper.save();
        GatewayTypeHelper.changeQuestionNumber(1, "1");
        GatewayTypeHelper.checkQuestionRow(0, new String[] { "1", "New Question2" });
        GatewayTypeHelper.checkQuestionRow(1, new String[] { "2", "New Question1" });
        EditorHelper.save();
        GatewayTypeHelper.changeQuestionNumber(1, "-10");
        GatewayTypeHelper.checkQuestionRow(0, new String[] { "1", "New Question1" });
        GatewayTypeHelper.checkQuestionRow(1, new String[] { "2", "New Question2" });
        EditorHelper.save();
        GatewayTypeHelper.changeQuestionNumber(0, "10");
        GatewayTypeHelper.checkQuestionRow(0, new String[] { "1", "New Question2" });
        GatewayTypeHelper.checkQuestionRow(1, new String[] { "2", "New Question1" });
        EditorHelper.save();
        ExplorerTreeHelper.deleteItem(Helper.getRootElementName() + "/" + processName + "/" + gatewayTypeName);
        assertTrue(ExplorerTreeHelper.treeItemDoesNotExists(Helper.getRootElementName() + "/" + processName, gatewayTypeName));
    }

    public void testGatewayTypeAnswerCreation() throws WidgetNotFoundException, MultipleWidgetsFoundException {
        uitu.ensureDatasourceAvailable("testGatewayTypeAnswerCreation");
        String gatewayTypeName = Helper.getNewGatewayTypeName();
        String processName = Helper.getRootProcess();
        GatewayTypeHelper.createGatewayType(gatewayTypeName, processName);
        assertTrue("gateway created", ExplorerTreeHelper.treeItemExists(Helper.getRootElementName() + "/" + processName, gatewayTypeName));
        Button addQuestionButton = (Button) uitu.getBasicFinder().find(new NameMatcher(TestHarness.ADDQUESTIONBUTTON));
        uitu.getButtonTester().actionClick(addQuestionButton);
        uitu.delay(Helper.SHORT_DELAY);
        Button addAnswerButton = (Button) uitu.getBasicFinder().find(new NameMatcher(TestHarness.ADDANSWERBUTTON));
        uitu.getButtonTester().actionClick(addAnswerButton);
        Table answerTable = (Table) uitu.getBasicFinder().find(new NameMatcher(TestHarness.ANSWERTABLE));
        assertEquals(3, uitu.getTableTester().getItemCount(answerTable));
        GatewayTypeHelper.checkAnswerRow(0, new String[] { "1", "Yes" });
        GatewayTypeHelper.checkAnswerRow(1, new String[] { "2", "No" });
        GatewayTypeHelper.checkAnswerRow(2, new String[] { "3", "New Answer" });
        EditorHelper.save();
        ExplorerTreeHelper.deleteItem(Helper.getRootElementName() + "/" + processName + "/" + gatewayTypeName);
        assertTrue(ExplorerTreeHelper.treeItemDoesNotExists(Helper.getRootElementName() + "/" + processName, gatewayTypeName));
    }

    public void testGatewayTypeAnswerRemoval() throws WidgetNotFoundException, MultipleWidgetsFoundException {
        uitu.ensureDatasourceAvailable("testGatewayTypeAnswerRemoval");
        String gatewayTypeName = Helper.getNewGatewayTypeName();
        String processName = Helper.getRootProcess();
        GatewayTypeHelper.createGatewayType(gatewayTypeName, processName);
        assertTrue("gateway created", ExplorerTreeHelper.treeItemExists(Helper.getRootElementName() + "/" + processName, gatewayTypeName));
        Button addQuestionButton = (Button) uitu.getBasicFinder().find(new NameMatcher(TestHarness.ADDQUESTIONBUTTON));
        uitu.getButtonTester().actionClick(addQuestionButton);
        uitu.delay(Helper.SHORT_DELAY);
        EditorHelper.save();
        Table answerTable = (Table) uitu.getBasicFinder().find(new NameMatcher(TestHarness.ANSWERTABLE));
        TableItem actualRow = uitu.getTableTester().getItem(answerTable, 0);
        uitu.getTableItemTester().actionClick(actualRow, 3, 3, "BUTTON3");
        uitu.getTableItemTester().actionClick(actualRow, 3, 3, "BUTTON3");
        uitu.delay(Helper.SHORT_DELAY);
        Menu menu = uitu.getTableTester().getMenu(answerTable);
        Hashtable<?, ?> menuItems = uitu.getMenuTester().hashMenuItemsByText(menu);
        MenuItem menuItem = (MenuItem) menuItems.get(ActionMessages.action_remove);
        if (menuItem == null) {
            throw new WidgetNotFoundException("Unable to locate the Delete Answer menu");
        }
        uitu.getMenuItemTester().actionClick(menuItem);
        uitu.delay(Helper.SHORT_DELAY);
        assertEquals(1, uitu.getTableTester().getItemCount(answerTable));
        GatewayTypeHelper.checkAnswerRow(0, new String[] { "2", "No" });
        EditorHelper.save();
        ExplorerTreeHelper.deleteItem(Helper.getRootElementName() + "/" + processName + "/" + gatewayTypeName);
        assertTrue(ExplorerTreeHelper.treeItemDoesNotExists(Helper.getRootElementName() + "/" + processName, gatewayTypeName));
    }

    public void testGatewayTypeAnswerRenumbering() throws WidgetNotFoundException, MultipleWidgetsFoundException {
        uitu.ensureDatasourceAvailable("testGatewayTypeQuestionRenumbering");
        String gatewayTypeName = Helper.getNewGatewayTypeName();
        String processName = Helper.getRootProcess();
        GatewayTypeHelper.createGatewayType(gatewayTypeName, processName);
        assertTrue("gateway created", ExplorerTreeHelper.treeItemExists(Helper.getRootElementName() + "/" + processName, gatewayTypeName));
        Button addQuestionButton = (Button) uitu.getBasicFinder().find(new NameMatcher(TestHarness.ADDQUESTIONBUTTON));
        uitu.getButtonTester().actionClick(addQuestionButton);
        EditorHelper.save();
        GatewayTypeHelper.changeAnswerNumber(1, "1");
        GatewayTypeHelper.checkAnswerRow(0, new String[] { "1", "No" });
        GatewayTypeHelper.checkAnswerRow(1, new String[] { "2", "Yes" });
        EditorHelper.save();
        GatewayTypeHelper.changeAnswerNumber(1, "-10");
        GatewayTypeHelper.checkAnswerRow(0, new String[] { "1", "Yes" });
        GatewayTypeHelper.checkAnswerRow(1, new String[] { "2", "No" });
        EditorHelper.save();
        GatewayTypeHelper.changeAnswerNumber(0, "10");
        GatewayTypeHelper.checkAnswerRow(0, new String[] { "1", "No" });
        GatewayTypeHelper.checkAnswerRow(1, new String[] { "2", "Yes" });
        EditorHelper.save();
        ExplorerTreeHelper.deleteItem(Helper.getRootElementName() + "/" + processName + "/" + gatewayTypeName);
        assertTrue(ExplorerTreeHelper.treeItemDoesNotExists(Helper.getRootElementName() + "/" + processName, gatewayTypeName));
    }
}
