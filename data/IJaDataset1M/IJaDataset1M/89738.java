package org.fest.swing.driver;

import javax.swing.JTabbedPane;
import org.fest.swing.annotation.RunsInEDT;
import org.fest.swing.edt.GuiTask;
import static org.fest.swing.edt.GuiActionRunner.execute;

/**
 * Understands a task that selects the tab with the given index in a <code>{@link JTabbedPane}</code>. This task is
 * executed in the event dispatch thread.
 *
 * @author Alex Ruiz
 * @author Yvonne Wang
 */
final class JTabbedPaneSelectTabTask {

    @RunsInEDT
    static void setSelectedTab(final JTabbedPane tabbedPane, final int index) {
        execute(new GuiTask() {

            protected void executeInEDT() {
                tabbedPane.setSelectedIndex(index);
            }
        });
    }

    private JTabbedPaneSelectTabTask() {
    }
}
