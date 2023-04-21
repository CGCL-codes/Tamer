package simtools.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBox;

/**
 * Action Check box. Useful to define anonymous class doing something when the button 
 * is (de)selected.
 * Use apply() to avoid duplicate code
 */
public abstract class ActionCheckBox extends JCheckBox implements ActionListener {

    public ActionCheckBox() {
        this("", false);
    }

    public ActionCheckBox(String text) {
        this(text, false);
    }

    public ActionCheckBox(String text, boolean state) {
        super(text, state);
        addActionListener(this);
    }

    /** apply its state to enable/disable other components */
    public void apply() {
        actionPerformed(new ActionEvent(this, 0, "apply"));
    }
}
