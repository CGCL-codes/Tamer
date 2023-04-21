package org.fest.swing.driver;

import javax.swing.JOptionPane;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.fest.swing.annotation.RunsInEDT;
import org.fest.swing.core.Robot;
import org.fest.swing.edt.FailOnThreadViolationRepaintManager;
import org.fest.swing.edt.GuiQuery;
import org.fest.swing.test.core.MethodInvocations;
import static javax.swing.JOptionPane.INFORMATION_MESSAGE;
import static org.fest.assertions.Assertions.assertThat;
import static org.fest.swing.core.BasicRobot.robotWithNewAwtHierarchy;
import static org.fest.swing.edt.GuiActionRunner.execute;
import static org.fest.swing.test.core.TestGroups.*;
import static org.fest.swing.test.swing.JOptionPaneLauncher.launch;

/**
 * Tests for <code>{@link JOptionPaneMessageTypeQuery}</code>.
 *
 * @author Alex Ruiz
 * @author Yvonne Wang
 */
@Test(groups = { GUI, ACTION })
public class JOptionPaneMessageTypeQueryTest {

    private Robot robot;

    private MyOptionPane optionPane;

    @BeforeClass
    public void setUpOnce() {
        FailOnThreadViolationRepaintManager.install();
    }

    @BeforeMethod
    public void setUp() {
        robot = robotWithNewAwtHierarchy();
        optionPane = MyOptionPane.createNew();
        launch(optionPane);
    }

    @AfterMethod
    public void tearDown() {
        robot.cleanUp();
    }

    public void shouldReturnMessageTypeOfJOptionPane() {
        optionPane.startRecording();
        assertThat(JOptionPaneMessageTypeQuery.messageTypeOf(optionPane)).isEqualTo(INFORMATION_MESSAGE);
        optionPane.requireInvoked("getMessageType");
    }

    private static class MyOptionPane extends JOptionPane {

        private static final long serialVersionUID = 1L;

        private boolean recording;

        private final MethodInvocations methodInvocations = new MethodInvocations();

        @RunsInEDT
        static MyOptionPane createNew() {
            return execute(new GuiQuery<MyOptionPane>() {

                protected MyOptionPane executeInEDT() {
                    return new MyOptionPane();
                }
            });
        }

        private MyOptionPane() {
            super("Hello World", INFORMATION_MESSAGE);
        }

        @Override
        public int getMessageType() {
            if (recording) methodInvocations.invoked("getMessageType");
            return super.getMessageType();
        }

        void startRecording() {
            recording = true;
        }

        MethodInvocations requireInvoked(String methodName) {
            return methodInvocations.requireInvoked(methodName);
        }
    }
}
