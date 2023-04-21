package edu.upmc.opi.caBIG.caTIES.client.vr.desktop.views;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import edu.upmc.opi.caBIG.caTIES.client.vr.utils.widgets.JMessageBar;

/**
 * ComboBox+Button Task 
 */
public class CaTIES_ComboBoxButtonTask extends CaTIES_ComboBoxTask implements CaTIES_ButtonTask {

    /**
     * The buttonlabel.
     */
    String buttonlabel = "";

    JMessageBar message;

    /**
     * The button.
     */
    protected JButton button;

    /**
     * The Constructor.
     */
    public CaTIES_ComboBoxButtonTask() {
        super(new ArrayList());
        this.setButtonVisible(true);
    }

    /**
     * The Constructor.
     * 
     * @param inlist the inlist
     */
    public CaTIES_ComboBoxButtonTask(List inlist) {
        super(inlist);
        this.setButtonVisible(false);
    }

    /**
     * The Constructor.
     * 
     * @param buttonlabel the buttonlabel
     * @param inlist the inlist
     */
    public CaTIES_ComboBoxButtonTask(List inlist, String buttonlabel) {
        super(inlist);
        this.buttonlabel = buttonlabel;
        initGUI();
    }

    /**
     * Gets the combo box.
     * 
     * @return the combo box
     */
    public JComboBox getComboBox() {
        return combo;
    }

    /**
     * Inits the GUI.
     */
    protected void initGUI() {
        button = new JButton(buttonlabel);
        message = new JMessageBar();
        JPanel p = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(2, 2, 2, 0);
        p.add(combo, c);
        c.insets = new Insets(2, 3, 2, 0);
        p.add(button, c);
        c.insets = new Insets(2, 2, 2, 0);
        p.add(message, c);
        this.setLayout(new BorderLayout());
        this.add(p, BorderLayout.WEST);
        button.setPreferredSize(new Dimension(75, 20));
        p.setOpaque(false);
        message.setOpaque(false);
        this.setOpaque(false);
    }

    /**
     * Sets the button text.
     * 
     * @param s the s
     */
    public void setButtonText(String s) {
        button.setText(s);
    }

    /**
     * Adds the button action listener.
     * 
     * @param a the a
     */
    public void addButtonActionListener(ActionListener a) {
        button.addActionListener(a);
    }

    /**
     * Sets the button visible.
     * 
     * @param visible the visible
     */
    public void setButtonVisible(boolean visible) {
        button.setVisible(visible);
    }

    /**
     * Sets the button enabled.
     * 
     * @param enable the enable
     */
    public void setButtonEnabled(boolean enable) {
        button.setEnabled(enable);
    }

    /**
     * Gets the button.
     * 
     * @return the button
     */
    public JButton getButton() {
        return button;
    }

    public void setMessage(int type, String msg) {
        message.setMessage(type, msg);
    }
}
