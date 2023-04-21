package cmd;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import visualisation.JProgDVBTimer;

@SuppressWarnings("serial")
public class AlwaysOnTopCmd extends AbstractAction {

    private JProgDVBTimer frame;

    public AlwaysOnTopCmd(JProgDVBTimer comp) {
        frame = comp;
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        frame.setAlwaysOnTop(!frame.isAlwaysOnTop());
    }
}
