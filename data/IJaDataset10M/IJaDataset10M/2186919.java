package org.fest.swing.driver;

import javax.swing.JInternalFrame;
import org.fest.swing.edt.GuiTask;
import static org.fest.swing.edt.GuiActionRunner.execute;

/**
 * Understands a task that sends a <code>{@link JInternalFrame}</code> to the front. This task is executed in the event
 * dispatch thread.
 *
 * @author Yvonne Wang
 */
final class JInternalFrameMoveToFrontTask {

    static void moveToFront(final JInternalFrame internalFrame) {
        execute(new GuiTask() {

            protected void executeInEDT() {
                internalFrame.toFront();
            }
        });
    }

    private JInternalFrameMoveToFrontTask() {
    }
}
