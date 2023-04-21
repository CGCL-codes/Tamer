package ch.ledcom.utils;

import javax.swing.JPanel;
import java.awt.Component;

/**
 * a panel to easily create simple forms
 *
 * This class is supposed to help in the creation of simple forms.
 *
 * @author Chirstophe Gensoul
 */
public class FormPanel extends JPanel {

    /** should be in a constant class */
    int GAP = 10;

    /**
     * creates a form panel
     */
    public FormPanel(String title) {
        setLayout(new FormLayout(GAP, GAP));
        setBorder(LedComBorderFactory.createBorder(title));
    }

    /**
     * add the given component to the pannel, creates all the glue,
     * strut, ... needed to render it nicely. You need to add first a
     * label and right after the coresponding component (JTextField,
     * JCombo, ...)
     *
     * @param comp the component to add
     */
    public Component add(Component comp) {
        return super.add(comp);
    }
}
