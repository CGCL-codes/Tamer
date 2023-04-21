package frostcode.icetasks.gui.task.dnd;

import javax.swing.JComponent;
import javax.swing.TransferHandler;

public class TaskTransferHandler extends TransferHandler {

    private static final long serialVersionUID = 1L;

    @Override
    public int getSourceActions(JComponent arg0) {
        return MOVE;
    }
}
