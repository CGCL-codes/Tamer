package abbot.swt.swtexamples.test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.eclipse.swt.SWT;
import org.eclipse.swt.examples.controlexample.ControlExample;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Widget;
import abbot.swt.finder.MultipleFoundException;
import abbot.swt.finder.NotFoundException;
import abbot.swt.junit.extensions.SWTTestCase;
import abbot.swt.matcher.WidgetTextMatcher;
import abbot.swt.script.Condition;
import abbot.swt.tester.GroupTester;
import abbot.swt.tester.ShellTester;
import abbot.swt.tester.WidgetTester;
import abbot.swt.utilities.Wait;

public class ControlExampleTest extends SWTTestCase {

    private Shell shell;

    private String shellTitle;

    private ControlExample example;

    private final Method getResourceString;

    private final Method setShellSize;

    public ControlExampleTest(String name) {
        super(name);
        try {
            getResourceString = ControlExample.class.getDeclaredMethod("getResourceString", String.class);
            getResourceString.setAccessible(true);
            setShellSize = ControlExample.class.getDeclaredMethod("setShellSize", ControlExample.class, Shell.class);
            setShellSize.setAccessible(true);
        } catch (SecurityException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    protected void setUp() throws Exception {
        super.setUp();
        syncExec(new Runnable() {

            public void run() {
                shell = new Shell(getDisplay(), SWT.SHELL_TRIM);
                shell.setLayout(new FillLayout());
                example = new ControlExample(shell);
                shellTitle = getResourceString("window.title");
                shell.setText(shellTitle);
                setShellSize();
                shell.open();
            }
        });
        ShellTester.waitVisible(shellTitle);
    }

    protected void tearDown() throws Exception {
        syncExec(new Runnable() {

            public void run() {
                if (shell != null & !shell.isDisposed()) example.dispose();
                shell.close();
            }
        });
        ShellTester.getShellTester().waitForDispose(shell, 3000);
        super.tearDown();
    }

    private String getResourceString(String key) {
        try {
            return (String) getResourceString.invoke(null, key);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private void setShellSize() {
        try {
            setShellSize.invoke(null, example, shell);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        ControlExample.main(args);
    }

    private static class ClickListener implements Listener {

        private boolean gotEvent;

        public synchronized void handleEvent(Event event) {
            gotEvent = true;
        }

        public synchronized boolean gotEvent() {
            return gotEvent;
        }

        public synchronized void reset() {
            gotEvent = false;
        }
    }

    public void testTextButtons() throws NotFoundException, MultipleFoundException {
        Group textButtonGroup = (Group) getFinder().find(shell, new WidgetTextMatcher(getResourceString("Text_Buttons")));
        Button button = (Button) getFinder().find(textButtonGroup, new WidgetTextMatcher(getResourceString("One")));
        checkClick(button);
        button = (Button) getFinder().find(textButtonGroup, new WidgetTextMatcher(getResourceString("Two")));
        checkClick(button);
        button = (Button) getFinder().find(textButtonGroup, new WidgetTextMatcher(getResourceString("Three")));
        checkClick(button);
        Group imageButtonGroup = (Group) getFinder().find(shell, new WidgetTextMatcher(getResourceString("Image_Buttons")));
        Control[] buttons = GroupTester.getGroupTester().getChildren(imageButtonGroup);
        for (int i = 0; i < buttons.length; i++) {
            button = (Button) buttons[i];
            checkClick(button);
        }
        Group imagetextButtonGroup = (Group) getFinder().find(shell, new WidgetTextMatcher(getResourceString("Image_Text_Buttons")));
        button = (Button) getFinder().find(imagetextButtonGroup, new WidgetTextMatcher(getResourceString("One")));
        checkClick(button);
        button = (Button) getFinder().find(imagetextButtonGroup, new WidgetTextMatcher(getResourceString("Two")));
        checkClick(button);
        button = (Button) getFinder().find(imagetextButtonGroup, new WidgetTextMatcher(getResourceString("Three")));
        checkClick(button);
    }

    private void checkClick(Widget widget) {
        final WidgetTester tester = WidgetTester.getWidgetTester();
        final ClickListener listener = new ClickListener();
        tester.addListener(widget, SWT.MouseDown, listener);
        try {
            tester.click(widget);
            Wait.wait(new Condition() {

                public boolean test() {
                    return listener.gotEvent();
                }
            }, 5000);
        } finally {
            tester.removeListener(widget, SWT.MouseDown, listener);
        }
    }
}
