package org.fest.swing.query;

import java.awt.Color;
import java.awt.Dimension;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.fest.swing.annotation.RunsInEDT;
import org.fest.swing.core.Robot;
import org.fest.swing.edt.FailOnThreadViolationRepaintManager;
import org.fest.swing.edt.GuiQuery;
import org.fest.swing.test.core.MethodInvocations;
import org.fest.swing.test.swing.TestWindow;
import static java.awt.Color.BLUE;
import static org.fest.assertions.Assertions.assertThat;
import static org.fest.swing.core.BasicRobot.robotWithNewAwtHierarchy;
import static org.fest.swing.edt.GuiActionRunner.execute;
import static org.fest.swing.test.core.TestGroups.*;

/**
 * Tests for <code>{@link ComponentBackgroundQuery}</code>.
 *
 * @author Alex Ruiz
 * @author Yvonne Wang
 */
@Test(groups = { GUI, ACTION })
public class ComponentBackgroundQueryTest {

    private static final Color BACKGROUND = BLUE;

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

    public void shouldReturnComponentBackground() {
        window.startRecording();
        assertThat(ComponentBackgroundQuery.backgroundOf(window)).isEqualTo(BACKGROUND);
        window.requireInvoked("getBackground");
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
            super(ComponentBackgroundQueryTest.class);
            setBackground(BACKGROUND);
            setPreferredSize(new Dimension(500, 300));
        }

        @Override
        public Color getBackground() {
            if (recording) methodInvocations.invoked("getBackground");
            return super.getBackground();
        }

        void startRecording() {
            recording = true;
        }

        MethodInvocations requireInvoked(String methodName) {
            return methodInvocations.requireInvoked(methodName);
        }
    }
}
