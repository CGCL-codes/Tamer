package org.fest.swing.driver;

import javax.swing.JInternalFrame;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.fest.swing.core.Robot;
import org.fest.swing.edt.GuiTask;
import org.fest.swing.testing.BooleanProvider;
import org.fest.swing.testing.MDITestWindow;
import static org.fest.assertions.Assertions.assertThat;
import static org.fest.swing.core.RobotFixture.robotWithNewAwtHierarchy;
import static org.fest.swing.edt.GuiActionRunner.execute;
import static org.fest.swing.testing.TestGroups.*;

/**
 * Tests for <code>{@link JInternalFrameClosableQuery}</code>.
 *
 * @author Yvonne Wang
 * @author Alex Ruiz
 */
@Test(groups = { GUI, EDT_ACTION })
public class JInternalFrameClosableQueryTest {

    private Robot robot;

    private JInternalFrame internalFrame;

    @BeforeMethod
    public void setUp() {
        robot = robotWithNewAwtHierarchy();
        MDITestWindow window = MDITestWindow.createNew(getClass());
        internalFrame = window.internalFrame();
        robot.showWindow(window);
    }

    @AfterMethod
    public void tearDown() {
        robot.cleanUp();
    }

    @Test(dataProvider = "booleans", dataProviderClass = BooleanProvider.class, groups = EDT_ACTION)
    public void shouldIndicateIfJInternalFrameIsClosable(final boolean closable) {
        execute(new GuiTask() {

            protected void executeInEDT() {
                internalFrame.setClosable(closable);
            }
        });
        robot.waitForIdle();
        assertThat(JInternalFrameClosableQuery.isClosable(internalFrame)).isEqualTo(closable);
    }
}
