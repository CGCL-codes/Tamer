package abbot.swt.finder.test;

import junit.framework.AssertionFailedError;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Slider;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Widget;
import abbot.swt.display.DisplayUtil;
import abbot.swt.display.Result;
import abbot.swt.finder.Finder;
import abbot.swt.finder.FinderException;
import abbot.swt.finder.WidgetFinderImpl;
import abbot.swt.hierarchy.Hierarchy;
import abbot.swt.hierarchy.WidgetHierarchyImpl;
import abbot.swt.junit.extensions.SWTTestCase;
import abbot.swt.matcher.ClassMatcher;
import abbot.swt.matcher.Matcher;
import abbot.swt.matcher.StringDataMatcher;
import abbot.swt.matcher.WidgetText2Matcher;
import abbot.swt.matcher.WidgetTextMatcher;
import abbot.swt.tester.ButtonTester;
import abbot.swt.tester.ItemTester;
import abbot.swt.tester.LabelTester;
import abbot.swt.tester.ShellTester;
import abbot.swt.tester.SliderTester;
import abbot.swt.tester.TableItemTester;
import abbot.swt.tester.TableTester;
import abbot.swt.tester.WidgetTester;

public class ExtraFinderTest extends SWTTestCase {

    private Hierarchy<Widget> hierarchy;

    private Finder<Widget> finder;

    private FinderTestApplication testApplication;

    private ShellTester shellTester;

    private ButtonTester buttonTester;

    private ItemTester itemTester;

    private LabelTester labelTester;

    private SliderTester sliderTester;

    public void setUp() {
        hierarchy = WidgetHierarchyImpl.getHierarchy(getDisplay());
        finder = new WidgetFinderImpl(hierarchy);
        testApplication = DisplayUtil.syncExec(getDisplay(), new Result<FinderTestApplication>() {

            public FinderTestApplication result() {
                FinderTestApplication application = new FinderTestApplication(getDisplay());
                application.open();
                return application;
            }
        });
        ShellTester.waitVisible(FinderTestApplication.SHELL_TITLE);
        shellTester = ShellTester.getShellTester();
        buttonTester = ButtonTester.getButtonTester();
        itemTester = ItemTester.getItemTester();
        labelTester = LabelTester.getLabelTester();
        sliderTester = SliderTester.getSliderTester();
        WidgetTextMatcher textMatcher = new WidgetTextMatcher(FinderTestApplication.SHELL_TITLE);
        Shell shell = (Shell) find(textMatcher);
        assertEquals(FinderTestApplication.SHELL_TITLE, shellTester.getText(shell));
    }

    public void tearDown() {
        if (testApplication != null) {
            getDisplay().syncExec(new Runnable() {

                public void run() {
                    testApplication.close();
                }
            });
            testApplication = null;
        }
    }

    private Widget find(Matcher<Widget> matcher) {
        try {
            return finder.find(matcher);
        } catch (FinderException exception) {
            throw new AssertionFailedError("unexpected search exception: " + exception);
        }
    }

    private Widget find(Widget widget, Matcher<Widget> matcher) {
        try {
            return finder.find(widget, matcher);
        } catch (FinderException exception) {
            throw new AssertionFailedError("unexpected search exception: " + exception);
        }
    }

    public void testMenuItemFind() {
        for (int i = 0; i < FinderTestApplication.MENUBARITEMS_COUNT; i++) {
            String text = FinderTestApplication.getMenuItemName(i);
            MenuItem menuItem = (MenuItem) find(new WidgetTextMatcher(text));
            assertEquals(text, itemTester.getText(menuItem));
        }
    }

    public void testLabelFind() {
        for (int i = 0; i < FinderTestApplication.TABITEMS_COUNT; i++) {
            String text = FinderTestApplication.getTabLabelText(i);
            Label label = (Label) find(new WidgetTextMatcher(text, false));
            assertEquals(text, labelTester.getText(label));
        }
    }

    public void testTableItemFind() {
        for (int i = 0; i < FinderTestApplication.TABLEITEMS_COUNT; i++) {
            String expectedText = FinderTestApplication.getItemText(i, i);
            TableItem tableItem = (TableItem) find(new WidgetText2Matcher(expectedText));
            assertTrue(hasText(tableItem, expectedText));
        }
    }

    private boolean hasText(TableItem item, String expectedText) {
        TableItemTester tableItemTester = TableItemTester.getTableItemTester();
        Table table = tableItemTester.getParent(item);
        TableTester tableTester = (TableTester) WidgetTester.getTester(table);
        int n = tableTester.getColumnCount(table);
        for (int i = 0; i < n; i++) {
            String gotText = tableItemTester.getText(item, i);
            if (expectedText.equals(gotText)) return true;
        }
        return false;
    }

    public void testTableColumnFind() {
        TableColumn[] tableColumns = new TableColumn[FinderTestApplication.TABLECOLUMNS_COUNT];
        for (int i = 0; i < tableColumns.length; i++) {
            tableColumns[i] = (TableColumn) find(new WidgetTextMatcher("Column " + i, false));
            assertEquals("Column " + i, itemTester.getText(tableColumns[i]));
        }
    }

    public void testButtonFind() {
        Group group = (Group) find(new StringDataMatcher(FinderTestApplication.NAME_KEY, FinderTestApplication.GROUP_NAME));
        for (int i = 0; i < FinderTestApplication.GROUPBUTTONS_COUNT; i++) {
            String expectedText = FinderTestApplication.getButtonText(i);
            Button button = (Button) find(group, new WidgetTextMatcher(expectedText, true));
            assertEquals(expectedText, buttonTester.getText(button));
        }
    }

    public void testSliderFind() {
        Group group = (Group) find(new StringDataMatcher(FinderTestApplication.NAME_KEY, FinderTestApplication.GROUP_NAME));
        Slider slider = (Slider) find(group, new ClassMatcher<Widget>(Slider.class));
        assertEquals(FinderTestApplication.SLIDER_TOOLTIP, sliderTester.getToolTipText(slider));
    }

    public void testTableColumnFindByText() {
        for (int i = 0; i < FinderTestApplication.TABLECOLUMNS_COUNT; i++) {
            String expected = FinderTestApplication.getTableColumnText(i);
            TableColumn tableColumn = (TableColumn) find(new WidgetTextMatcher(expected, false));
            assertEquals(expected, itemTester.getText(tableColumn));
        }
    }

    public void testTableColumnFindByName() {
        for (int i = 0; i < FinderTestApplication.TABLECOLUMNS_COUNT; i++) {
            String expected = FinderTestApplication.getTableColumnText(i);
            TableColumn tableColumn = (TableColumn) find(new StringDataMatcher(FinderTestApplication.NAME_KEY, expected, false));
            assertEquals(expected, itemTester.getText(tableColumn));
        }
    }
}
