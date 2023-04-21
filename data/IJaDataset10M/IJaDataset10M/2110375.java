package ide;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.undo.CannotRedoException;

/**
 * @author peter
 *
 */
public class JaguarRedoAction extends AbstractAction {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private JaguarIDE ide;

    public JaguarRedoAction(JaguarIDE ide) {
        super("Redo");
        setEnabled(false);
        this.ide = ide;
    }

    public void actionPerformed(ActionEvent e) {
        try {
            ide.undo.redo();
        } catch (CannotRedoException ex) {
            System.out.println("Unable to redo: " + ex);
            ex.printStackTrace();
        }
        updateRedoState();
        ide.undoAction.updateUndoState();
    }

    protected void updateRedoState() {
        if (ide.undo.canRedo()) {
            setEnabled(true);
            putValue(Action.NAME, ide.undo.getRedoPresentationName());
        } else {
            setEnabled(false);
            putValue(Action.NAME, "Redo");
        }
    }
}
