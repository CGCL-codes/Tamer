package tudolist.gui.action;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import tudolist.gui.Images;

public class HelpAction extends AbstractAction {

    public HelpAction() {
        super("Help", Images.HELP);
    }

    public void actionPerformed(ActionEvent e) {
        System.exit(0);
    }
}
