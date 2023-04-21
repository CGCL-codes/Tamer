package sk.fiit.mitandao.modules.inputs.dbreader.laststep;

import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * This is the last screen that the user will see. 
 * It only shows the message with "Database configuration finished, click next to continue".
 * 
 * One day it will maybe show the user settings
 * 
 * @author Tomas Jelinek
 *
 */
public class FinishPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    public FinishPanel() {
        add(new JLabel("Database configuration finished, click next to continue."));
    }
}
