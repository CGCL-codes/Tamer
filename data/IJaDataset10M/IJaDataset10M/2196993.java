package org.fest.swing.query;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.fest.swing.annotation.RunsInEDT;
import org.fest.swing.core.Robot;
import org.fest.swing.edt.FailOnThreadViolationRepaintManager;
import org.fest.swing.edt.GuiQuery;
import org.fest.swing.test.core.MethodInvocations;
import org.fest.swing.test.data.BooleanProvider;
import org.fest.swing.test.swing.TestWindow;
import static org.fest.assertions.Assertions.assertThat;
import static org.fest.swing.core.BasicRobot.robotWithNewAwtHierarchy;
import static org.fest.swing.edt.GuiActionRunner.execute;
import static org.fest.swing.test.core.TestGroups.*;
import static org.fest.swing.test.task.ComponentSetEnabledTask.setEnabled;

/**
 * Tests for <code>{@link ComponentEnabledQuery}</code>.
 *
 * @author Alex Ruiz
 * @author Yvonne Wang
 */
@Test(groups = { GUI, ACTION })
public class ComponentEnabledQueryTest {

    private Robot robot;

    private MyWindow window;

    @BeforeClass
    public void setUpOnce() {
        FailOnThreadViolationRepaintManager.install();
    }

    @BeforeMethod
    public void setUp() {
        robot = robotWithNewAwtHierarchy();
        window = MyWindow.createNew();
        robot.showWindow(window);
    }

    @AfterMethod
    public void tearDown() {
        robot.cleanUp();
    }

    @Test(dataProvider = "booleans", dataProviderClass = BooleanProvider.class, groups = { GUI, ACTION })
    public void shouldIndicateIfComponentIsEnabled(final boolean enabled) {
        setEnabled(window, enabled);
        robot.waitForIdle();
        window.startRecording();
        assertThat(ComponentEnabledQuery.isEnabled(window)).isEqualTo(enabled);
        window.requireInvoked("isEnabled");
    }

    private static class MyWindow extends TestWindow {

        private static final long serialVersionUID = 1L;

        @RunsInEDT
        static MyWindow createNew() {
            return execute(new GuiQuery<MyWindow>() {

                protected MyWindow executeInEDT() {
                    return new MyWindow();
                }
            });
        }

        private boolean recording;

        private final MethodInvocations methodInvocations = new MethodInvocations();

        private MyWindow() {
            super(ComponentEnabledQueryTest.class);
        }

        @Override
        public boolean isEnabled() {
            if (recording) methodInvocations.invoked("isEnabled");
            return super.isEnabled();
        }

        void startRecording() {
            recording = true;
        }

        MethodInvocations requireInvoked(String methodName) {
            return methodInvocations.requireInvoked(methodName);
        }
    }
}
