package org.exmaralda.partitureditor.partiture.eventActions;

import org.exmaralda.partitureditor.partiture.*;
import javax.swing.Action;
import javax.swing.KeyStroke;
import java.awt.event.KeyEvent;
import java.awt.Toolkit;
import org.exmaralda.partitureditor.partiture.undo.UndoInformation;

/**
 *
 * @author  thomas
 */
public class EditEventAction extends org.exmaralda.partitureditor.partiture.AbstractTableAction {

    /** Creates a new instance of EditEventAction */
    public EditEventAction(PartitureTableWithActions t) {
        super("Event properties...", t);
        this.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
    }

    public void actionPerformed(java.awt.event.ActionEvent actionEvent) {
        System.out.println("editEventAction!");
        editEvent();
        table.transcriptionChanged = true;
    }

    private void editEvent() {
        UndoInformation undoEditCellInfo = new UndoInformation(table, "Edit event");
        if (table.undoEnabled) {
            if (table.selectionStartCol < table.getModel().getNumColumns() - 2) {
                undoEditCellInfo.memorizeCell(table);
            } else {
                undoEditCellInfo.memorizeRegion(table, table.getModel().lower(table.selectionStartCol), table.getNumColumns());
            }
        }
        boolean done = table.getModel().editEvent(table.selectionStartRow, table.selectionStartCol, table.parent, table.generalPurposeFontName);
        if (done) {
            table.addUndo(undoEditCellInfo);
        }
    }
}
