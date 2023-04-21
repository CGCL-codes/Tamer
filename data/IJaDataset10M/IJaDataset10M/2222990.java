package org.fest.swing.driver;

import javax.swing.JTable;
import org.fest.swing.data.TableCell;
import org.fest.swing.edt.GuiTask;
import static org.fest.swing.edt.GuiActionRunner.execute;

/**
 * Understands a task that selects multiple cells in a <code>{@link JTable}</code>. This task is executed in the event
 * dispatch thread.
 *
 * @author Alex Ruiz
 * @author Yvonne Wang
 */
final class JTableSelectCellsTask {

    static void selectCells(final JTable table, final TableCell from, final TableCell to) {
        execute(new GuiTask() {

            protected void executeInEDT() {
                table.setColumnSelectionInterval(from.column, to.column);
                table.setRowSelectionInterval(from.row, to.row);
            }
        });
    }

    private JTableSelectCellsTask() {
    }
}
