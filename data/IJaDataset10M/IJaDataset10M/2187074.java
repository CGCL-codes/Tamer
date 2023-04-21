package app.actions;

import app.frames.DatabaseForm;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JFrame;

/**
 *  Spusteni dialogu nastaveni databaze
 * @author Dejvino
 */
public class DatabaseAction extends AbstractAction {

    private JFrame frame = null;

    public DatabaseAction(JFrame frame) {
        this.frame = frame;
    }

    public void actionPerformed(ActionEvent arg0) {
        if (frame != null) frame.setEnabled(false);
        new DatabaseForm(frame).setVisible(true);
    }
}
