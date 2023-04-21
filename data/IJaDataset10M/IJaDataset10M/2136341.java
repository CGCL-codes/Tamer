package skribler.util;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;

@SuppressWarnings("serial")
public class BeeperAction extends AbstractAction {

    private String message = null;

    public BeeperAction(String message) {
        this.message = message;
    }

    public void actionPerformed(ActionEvent e) {
        Toolkit.getDefaultToolkit().beep();
        System.out.println("*** " + message + " ***");
    }
}
