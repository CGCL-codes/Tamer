package spaken.ui.swing.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.KeyStroke;
import spaken.model.Command;
import spaken.model.CommandHistory;
import spaken.model.CommandListener;

public class UndoAction extends AbstractAction implements CommandListener {

    private CommandHistory history;

    public UndoAction(CommandHistory history) {
        super("Undo");
        this.history = history;
        history.addListener(this);
        this.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_Z, ActionEvent.META_MASK));
        update();
    }

    public void actionPerformed(ActionEvent e) {
        history.undo();
    }

    public void commandExecuted(Command c) {
        update();
    }

    public void commandRedone(Command c) {
        update();
    }

    public void commandUndone(Command c) {
        update();
    }

    private void update() {
        setEnabled(history.getUndoes().size() > 1);
    }
}
