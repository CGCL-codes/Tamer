package freemind.modes.actions;

import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import freemind.modes.ControllerAdapter;

public class CopySingleAction extends AbstractAction {

    private final ControllerAdapter controller;

    public CopySingleAction(ControllerAdapter controller) {
        super(controller.getText("copy_single"));
        this.controller = controller;
        setEnabled(false);
    }

    public void actionPerformed(ActionEvent e) {
        if (controller.getMap() != null) {
            Transferable copy = controller.getModel().copySingle();
            if (copy != null) {
                controller.getClipboard().setContents(copy, null);
            }
        }
    }
}
