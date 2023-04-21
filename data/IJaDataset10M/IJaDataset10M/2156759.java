package org.wtc.snippets.eclipse;

import static com.windowtester.runtime.swt.locator.eclipse.EclipseLocators.view;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.TreeItem;
import com.windowtester.runtime.IUIContext;
import com.windowtester.runtime.WT;
import com.windowtester.runtime.WaitTimedOutException;
import com.windowtester.runtime.WidgetSearchException;
import com.windowtester.runtime.condition.ICondition;
import com.windowtester.runtime.locator.IWidgetLocator;
import com.windowtester.runtime.locator.IWidgetReference;
import com.windowtester.runtime.swt.UITestCaseSWT;
import com.windowtester.runtime.swt.condition.eclipse.ProjectExistsCondition;
import com.windowtester.runtime.swt.condition.shell.ShellDisposedCondition;
import com.windowtester.runtime.swt.condition.shell.ShellShowingCondition;
import com.windowtester.runtime.swt.condition.eclipse.ActiveEditorCondition;
import com.windowtester.runtime.swt.locator.ButtonLocator;
import com.windowtester.runtime.swt.locator.CTabItemLocator;
import com.windowtester.runtime.swt.locator.LabeledTextLocator;
import com.windowtester.runtime.swt.locator.MenuItemLocator;
import com.windowtester.runtime.swt.locator.TreeItemLocator;

/**
 * A snippet that demonstrates verifying tree contents (presence and absence).
 * <p>
 * {@link #oneTimeSetup()} creates a java class and {@link #testSourceDeleteRemovesBinary()} verifies that 
 * deleting the source file causes the class file to get removed as well.
 * <p>
 * To run this snippet, you may need to increase your heap size.  It was developed using the following VM
 * args:
 * <pre>
 * -Xms256m
 * -Xmx512m
 * </pre>
 * 
 * 
 * @author Phil Quitslund
 *
 */
public class Snippet004VerifyTreeContents extends UITestCaseSWT {

    private abstract static class EqualsCondition implements ICondition {

        private Object expected;

        public EqualsCondition is(Object expected) {
            this.expected = expected;
            return this;
        }

        public boolean test() {
            Object actual;
            try {
                actual = getActual();
            } catch (Exception e) {
                return false;
            }
            if (actual == null) return false;
            return getExpected().equals(actual);
        }

        protected Object getExpected() {
            return expected;
        }

        protected abstract Object getActual() throws Exception;

        @Override
        public String toString() {
            try {
                return " for [" + getActual() + "] to equal: [" + getExpected() + "]";
            } catch (Exception e) {
                return " <condition failed due to exception>: " + e.getMessage();
            }
        }
    }

    private final class NumberOfClasses extends EqualsCondition {

        protected Object getActual() throws WidgetSearchException {
            return getAllClasses().length;
        }
    }

    private final class NumberOfSources extends EqualsCondition {

        protected Object getActual() throws WidgetSearchException {
            return getAllSources().length;
        }
    }

    private NumberOfClasses numberOfClasses() {
        return new NumberOfClasses();
    }

    private NumberOfSources numberOfSources() {
        return new NumberOfSources();
    }

    private static final String SEP_SRC_FOLDER_CREATION_LABEL = "&Create separate folders for sources and class files";

    @Override
    protected void oneTimeSetup() throws Exception {
        closeWelcomePageIfNecessary();
        createProject();
        createClass();
        openNavigatorView();
    }

    private void closeWelcomePageIfNecessary() throws WidgetSearchException {
        IWidgetLocator[] welcomeTab = getUI().findAll(new CTabItemLocator("Welcome"));
        if (welcomeTab.length == 0) return;
        getUI().close(welcomeTab[0]);
    }

    private void createProject() throws WidgetSearchException {
        createJavaProject(getProjectName());
    }

    private void createJavaProject(String projectName) throws WidgetSearchException {
        IUIContext ui = getUI();
        ui.click(new MenuItemLocator("File/New/Project..."));
        ui.wait(new ShellShowingCondition("New Project"));
        ui.click(new TreeItemLocator("Java/Java Project"));
        ui.click(new ButtonLocator("Next >"));
        ui.enterText(projectName);
        ui.click(new ButtonLocator(SEP_SRC_FOLDER_CREATION_LABEL));
        ui.click(new ButtonLocator("Finish"));
        ui.wait(new ShellDisposedCondition("New Java Project"));
        ui.wait(new ProjectExistsCondition(projectName, true));
    }

    private String getProjectName() {
        return getClass().getSimpleName() + "Project";
    }

    private void createClass() throws WidgetSearchException {
        createJavaClass(getProjectName() + "/src", getJavaClassName());
    }

    private void openNavigatorView() throws WidgetSearchException {
        IUIContext ui = getUI();
        ui.click(new MenuItemLocator("Window/Show View/Navigator"));
        ui.wait(view("Navigator").isVisible());
    }

    private void createJavaClass(String sourceFolder, String className) throws WidgetSearchException {
        IUIContext ui = getUI();
        ui.click(new MenuItemLocator("File/New/Class"));
        ui.wait(new ShellShowingCondition("New Java Class"));
        ui.click(2, new LabeledTextLocator("Source fol&der:"));
        ui.enterText(sourceFolder);
        ui.click(2, new LabeledTextLocator("Na&me:"));
        ui.enterText(className);
        ui.assertThat(new ButtonLocator("Finish").isEnabled());
        ui.click(new ButtonLocator("Finish"));
        ui.wait(new ShellDisposedCondition("New Java Class"));
        ui.wait(ActiveEditorCondition.forName(className + ".java"));
    }

    public void testSourceDeleteRemovesBinary() throws Exception {
        verifySourceAndClassFilesExist();
        deleteClass();
        verifyClassAndSourceFilesDoNotExisit();
    }

    private void verifyClassAndSourceFilesDoNotExisit() {
        IUIContext ui = getUI();
        ui.assertThat(numberOfClasses().is(0));
        ui.assertThat(numberOfSources().is(0));
    }

    private void verifySourceAndClassFilesExist() throws WaitTimedOutException, WidgetSearchException {
        IUIContext ui = getUI();
        ui.assertThat(numberOfClasses().is(1));
        assertEquals(getJavaClassName() + ".class", getAllClasses()[0]);
        ui.assertThat(numberOfSources().is(1));
        assertEquals(getJavaClassName() + ".java", getAllSources()[0]);
    }

    private String[] getAllClasses() throws WidgetSearchException {
        IUIContext ui = getUI();
        IWidgetReference root = (IWidgetReference) ui.click(new TreeItemLocator(getProjectName() + "/bin/").in(view("Navigator")));
        expand(root);
        IWidgetLocator[] classes = ui.findAll(new TreeItemLocator(getProjectName() + "/bin/.*"));
        String[] classNames = new String[classes.length];
        for (int i = 0; i < classNames.length; i++) {
            classNames[i] = getText((TreeItem) (((IWidgetReference) classes[i]).getWidget()));
        }
        return classNames;
    }

    private String[] getAllSources() throws WidgetSearchException {
        IUIContext ui = getUI();
        IWidgetReference root = (IWidgetReference) ui.click(new TreeItemLocator(getProjectName() + "/src/").in(view("Navigator")));
        expand(root);
        IWidgetLocator[] sources = ui.findAll(new TreeItemLocator(getProjectName() + "/src/.*"));
        String[] sourceNames = new String[sources.length];
        for (int i = 0; i < sourceNames.length; i++) {
            sourceNames[i] = getText((TreeItem) (((IWidgetReference) sources[i]).getWidget()));
        }
        return sourceNames;
    }

    private void expand(final IWidgetReference treeNode) throws WidgetSearchException {
        IUIContext ui = getUI();
        ui.click(treeNode);
        ui.keyClick(WT.ARROW_RIGHT);
        ui.wait(new ICondition() {

            public boolean test() {
                return isExpanded((TreeItem) treeNode.getWidget());
            }
        });
    }

    private boolean isExpanded(final TreeItem item) {
        final boolean[] expanded = new boolean[1];
        Display.getDefault().syncExec(new Runnable() {

            public void run() {
                expanded[0] = item.getExpanded();
            }
        });
        return expanded[0];
    }

    private String getText(final TreeItem item) {
        final String[] text = new String[1];
        Display.getDefault().syncExec(new Runnable() {

            public void run() {
                text[0] = item.getText();
            }
        });
        return text[0];
    }

    private void deleteClass() throws WidgetSearchException, WaitTimedOutException {
        IUIContext ui = getUI();
        ui.contextClick(new TreeItemLocator(getProjectName() + "/src/" + getJavaClassName() + ".java").in(view("Navigator")), "Delete");
        ui.wait(new ShellShowingCondition("Delete Resources"));
        ui.click(new ButtonLocator("OK"));
        ui.wait(new ShellDisposedCondition("Delete Resources"));
    }

    private String getJavaClassName() {
        return "TestClass";
    }
}
