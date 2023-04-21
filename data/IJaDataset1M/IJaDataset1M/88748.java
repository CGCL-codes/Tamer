package org.web3d.x3d.palette;

import java.awt.event.ActionEvent;
import java.io.IOException;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.text.JTextComponent;
import org.netbeans.editor.Utilities;
import org.netbeans.spi.palette.PaletteActions;
import org.netbeans.spi.palette.PaletteController;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.text.ActiveEditorDrop;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;

public class X3DPaletteActions extends PaletteActions {

    /** Creates a new instance of FormPaletteProvider */
    public X3DPaletteActions() {
    }

    public Action[] getImportActions() {
        return new Action[0];
    }

    public Action[] getCustomCategoryActions(Lookup category) {
        return new Action[0];
    }

    public Action[] getCustomItemActions(Lookup item) {
        return new Action[0];
    }

    public Action[] getCustomPaletteActions() {
        return new Action[0];
    }

    public Action getPreferredAction(Lookup item) {
        return new X3DPaletteInsertAction(item);
    }

    private static class X3DPaletteInsertAction extends AbstractAction {

        private Lookup item;

        X3DPaletteInsertAction(Lookup item) {
            this.item = item;
        }

        public void actionPerformed(ActionEvent e) {
            ActiveEditorDrop drop = (ActiveEditorDrop) item.lookup(ActiveEditorDrop.class);
            JTextComponent target = Utilities.getFocusedComponent();
            if (target == null) {
                String msg = NbBundle.getMessage(X3DPaletteActions.class, "MSG_ErrorNoFocusedDocument");
                DialogDisplayer.getDefault().notify(new NotifyDescriptor.Message(msg, NotifyDescriptor.ERROR_MESSAGE));
                return;
            }
            try {
                drop.handleTransfer(target);
            } finally {
                Utilities.requestFocus(target);
            }
            try {
                PaletteController pc = X3DPaletteFactory.getPalette();
                pc.clearSelection();
            } catch (IOException ioe) {
            }
        }
    }
}
