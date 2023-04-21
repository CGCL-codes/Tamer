package org.fest.swing.query;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.fest.swing.annotation.RunsInEDT;
import org.fest.swing.edt.FailOnThreadViolationRepaintManager;
import org.fest.swing.edt.GuiQuery;
import org.fest.swing.lock.ScreenLock;
import org.fest.swing.test.core.MethodInvocations;
import org.fest.swing.test.swing.TestWindow;
import static org.fest.assertions.Assertions.assertThat;
import static org.fest.swing.edt.GuiActionRunner.execute;
import static org.fest.swing.test.core.TestGroups.*;

/**
 * Tests for <code>{@link ComponentShowingQuery}</code>.
 *
 * @author Alex Ruiz
 * @author Yvonne Wang
 */
@Test(groups = { GUI, ACTION })
public class ComponentShowingQueryTest {

    private MyWindow window;

    @BeforeClass
    public void setUpOnce() {
        FailOnThreadViolationRepaintManager.install();
    }

    @BeforeMethod
    public void setUp() {
        ScreenLock.instance().acquire(this);
        window = MyWindow.createNew();
    }

    @AfterMethod
    public void tearDown() {
        window.destroy();
        ScreenLock.instance().release(this);
    }

    public void shouldIndicateIfComponentIsNotShowing() {
        window.startRecording();
        assertThat(ComponentShowingQuery.isShowing(window)).isFalse();
        window.requireInvoked("isShowing");
    }

    public void shouldIndicateIfComponentIsShowing() {
        window.display();
        window.startRecording();
        assertThat(ComponentShowingQuery.isShowing(window)).isTrue();
        window.requireInvoked("isShowing");
    }

    private static class MyWindow extends TestWindow {

        private static final long serialVersionUID = 1L;

        private boolean recording;

        private final MethodInvocations methodInvocations = new MethodInvocations();

        @RunsInEDT
        static MyWindow createNew() {
            return execute(new GuiQuery<MyWindow>() {

                protected MyWindow executeInEDT() {
                    return new MyWindow();
                }
            });
        }

        private MyWindow() {
            super(ComponentShowingQueryTest.class);
        }

        @Override
        public boolean isShowing() {
            if (recording) methodInvocations.invoked("isShowing");
            return super.isShowing();
        }

        void startRecording() {
            recording = true;
        }

        MethodInvocations requireInvoked(String methodName) {
            return methodInvocations.requireInvoked(methodName);
        }
    }
}
