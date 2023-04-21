package com.bluemarsh.jswat.core.actions;

import java.awt.event.ActionEvent;
import javax.swing.Action;
import javax.swing.KeyStroke;
import javax.swing.text.JTextComponent;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;

/**
 * Pastes text from the system clipboard.
 *
 * @author Nathan Fiedler
 */
public class PasteAction extends ContextAwareTextAction {

    /** silence the compiler warnings */
    private static final long serialVersionUID = 1L;

    /**
     * Creates a new instance of PasteAction.
     */
    public PasteAction() {
        super(NbBundle.getMessage(PasteAction.class, "LBL_PasteAction_Name"));
        putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control V"));
    }

    public void actionPerformed(ActionEvent event) {
        JTextComponent target = getText(event);
        if (target != null) {
            target.paste();
        }
    }

    public Action createContextAwareInstance(Lookup lookup) {
        PasteAction action = new PasteAction();
        action.setLookup(lookup);
        return action;
    }

    public boolean isEnabled() {
        JTextComponent text = getText();
        if (text != null) {
            return text.isEditable();
        }
        return super.isEnabled();
    }
}
