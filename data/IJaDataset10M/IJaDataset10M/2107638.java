package org.fest.swing.driver;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.fest.swing.edt.FailOnThreadViolationRepaintManager;
import static org.fest.assertions.Assertions.assertThat;
import static org.fest.swing.test.builder.JButtons.button;
import static org.fest.swing.test.builder.JDialogs.dialog;
import static org.fest.swing.test.builder.JFrames.frame;
import static org.fest.swing.test.builder.JInternalFrames.internalFrame;

/**
 * Tests for <code>{@link ComponentMovableQuery}</code>.
 *
 * @author Alex Ruiz
 */
@Test
public class ComponentMovableQueryTest {

    @BeforeClass
    public void setUpOnce() {
        FailOnThreadViolationRepaintManager.install();
    }

    public void shouldReturnIsMovableIfComponentIsFrame() {
        assertThat(ComponentMovableQuery.isUserMovable(frame().createNew())).isTrue();
    }

    public void shouldReturnIsMovableIfComponentIsDialog() {
        assertThat(ComponentMovableQuery.isUserMovable(dialog().createNew())).isTrue();
    }

    public void shouldReturnIsMovableIfComponentIsJInternalFrame() {
        assertThat(ComponentMovableQuery.isUserMovable(internalFrame().createNew())).isTrue();
    }

    public void shouldReturnIsNotMovableIfComponentIsNotWindow() {
        assertThat(ComponentMovableQuery.isUserMovable(button().createNew())).isFalse();
    }
}
