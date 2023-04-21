package application.actions;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import application.visual.AddConnectionDialog;

public class AddConnectionAction extends AbstractAction {

    public void actionPerformed(ActionEvent e) {
        new AddConnectionDialog().setVisible(true);
    }
}
