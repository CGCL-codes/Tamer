package org.fest.swing.hierarchy;

import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.fest.swing.annotation.RunsInEDT;
import org.fest.swing.edt.FailOnThreadViolationRepaintManager;
import org.fest.swing.edt.GuiQuery;
import org.fest.swing.listener.WeakEventListener;
import org.fest.swing.lock.ScreenLock;
import org.fest.swing.test.awt.ToolkitStub;
import org.fest.swing.test.swing.TestWindow;
import static java.awt.AWTEvent.*;
import static org.fest.assertions.Assertions.assertThat;
import static org.fest.swing.edt.GuiActionRunner.execute;
import static org.fest.swing.hierarchy.JFrameContentPaneQuery.contentPaneOf;
import static org.fest.swing.test.core.TestGroups.GUI;
import static org.fest.util.Arrays.array;

/**
 * Tests for <code>{@link NewHierarchy}</code>.
 *
 * @author Alex Ruiz
 * @author Yvonne Wang
 */
@Test(groups = GUI)
public class NewHierarchyTest {

    private static final long EVENT_MASK = WINDOW_EVENT_MASK | COMPONENT_EVENT_MASK;

    private ToolkitStub toolkit;

    private WindowFilter filter;

    private MyWindow window;

    @BeforeClass
    public void setUpOnce() {
        FailOnThreadViolationRepaintManager.install();
    }

    @BeforeMethod
    public void setUp() {
        ScreenLock.instance().acquire(this);
        toolkit = ToolkitStub.createNew();
        window = MyWindow.createAndShow();
        filter = new WindowFilter();
    }

    @AfterMethod
    public void tearDown() {
        try {
            window.destroy();
        } finally {
            ScreenLock.instance().release(this);
        }
    }

    public void shouldIgnoreExistingComponentsAndAddTransientWindowListenerToToolkit() {
        new NewHierarchy(toolkit, filter, true);
        assertThat(filter.isIgnored(window)).isTrue();
        assertThatTransientWindowListenerWasAddedToToolkit();
    }

    public void shouldNotIgnoreExistingComponentsAndAddTransientWindowListenerToToolkit() {
        new NewHierarchy(toolkit, filter, false);
        assertThat(filter.isIgnored(window)).isFalse();
        assertThatTransientWindowListenerWasAddedToToolkit();
    }

    private void assertThatTransientWindowListenerWasAddedToToolkit() {
        List<WeakEventListener> eventListeners = toolkit.eventListenersUnderEventMask(EVENT_MASK, WeakEventListener.class);
        assertThat(eventListeners).hasSize(1);
        WeakEventListener weakEventListener = eventListeners.get(0);
        assertThat(weakEventListener.underlyingListener()).isInstanceOf(TransientWindowListener.class);
    }

    public void shouldReturnNoChildrenIfComponentIsFiltered() {
        NewHierarchy hierarchy = new NewHierarchy(toolkit, filter, true);
        assertThat(hierarchy.childrenOf(window)).isEmpty();
    }

    public void shouldReturnUnfilteredChildrenOfUnfilteredComponent() {
        NewHierarchy hierarchy = new NewHierarchy(toolkit, filter, false);
        filter.ignore(window.textField);
        assertThat(hierarchy.childrenOf(contentPaneOf(window))).containsOnly(window.comboBox);
    }

    public void shouldNotContainFilteredComponent() {
        NewHierarchy hierarchy = new NewHierarchy(toolkit, filter, true);
        assertThat(hierarchy.contains(window)).isFalse();
    }

    public void shouldContainUnfilteredComponent() {
        NewHierarchy hierarchy = new NewHierarchy(toolkit, filter, false);
        assertThat(hierarchy.contains(window)).isTrue();
    }

    public void shouldNotContainFilteredWindowsInRootWindows() {
        NewHierarchy hierarchy = new NewHierarchy(toolkit, filter, true);
        assertThat(hierarchy.roots()).excludes(window);
    }

    public void shouldContainUnfilteredWindowsInRootWindows() {
        NewHierarchy hierarchy = new NewHierarchy(toolkit, filter, false);
        assertThat(hierarchy.roots()).contains(window);
    }

    public void shouldRecognizeGivenComponent() {
        NewHierarchy hierarchy = new NewHierarchy(toolkit, filter, true);
        assertThat(hierarchy.roots()).excludes(window);
        hierarchy.recognize(window);
        assertThat(hierarchy.roots()).contains(window);
    }

    private static class MyWindow extends TestWindow {

        private static final long serialVersionUID = 1L;

        @RunsInEDT
        static MyWindow createAndShow() {
            return execute(new GuiQuery<MyWindow>() {

                protected MyWindow executeInEDT() {
                    return display(new MyWindow());
                }
            });
        }

        final JComboBox comboBox = new JComboBox(array("One", "Two"));

        final JTextField textField = new JTextField(20);

        private MyWindow() {
            super(NewHierarchyTest.class);
            addComponents(comboBox, textField);
        }
    }
}
