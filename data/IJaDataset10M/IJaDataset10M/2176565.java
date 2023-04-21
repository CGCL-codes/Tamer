package client.gui.listeners;

import java.awt.event.ActionEvent;
import javax.swing.JFrame;

public class HelpButtonOK extends MainActionListener {

    public HelpButtonOK(JFrame f) {
        component = f;
    }

    public void actionPerformed(ActionEvent arg0) {
        ((JFrame) component).setVisible(false);
    }
}
