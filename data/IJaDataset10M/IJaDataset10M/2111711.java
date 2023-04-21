package org.openscience.jchempaint.dialog.editor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Properties;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import org.openscience.jchempaint.GT;
import org.openscience.jchempaint.JCPPropertyHandler;
import org.openscience.jchempaint.dialog.FieldTablePanel;

/**
 * JFrame that allows setting of a number of general application options.
 * 
 */
public class GeneralSettingsEditor extends FieldTablePanel implements ActionListener {

    private static final long serialVersionUID = -6796422949531937872L;

    private JCheckBox askForIOSettings;

    private JTextField undoStackSize;

    private JFrame frame;

    public GeneralSettingsEditor(JFrame frame) {
        super();
        this.frame = frame;
        constructPanel();
    }

    private void constructPanel() {
        askForIOSettings = new JCheckBox();
        addField(GT._("Ask for IO settings"), askForIOSettings);
        undoStackSize = new JTextField();
        addField(GT._("Undo/redo stack size"), undoStackSize);
    }

    public void setSettings() {
        Properties props = JCPPropertyHandler.getInstance().getJCPProperties();
        askForIOSettings.setSelected(props.getProperty("askForIOSettings", "true").equals("true"));
        undoStackSize.setText(props.getProperty("General.UndoStackSize"));
        validate();
    }

    public boolean applyChanges() {
        Properties props = JCPPropertyHandler.getInstance().getJCPProperties();
        props.setProperty("askForIOSettings", askForIOSettings.isSelected() ? "true" : "false");
        try {
            int size = Integer.parseInt(undoStackSize.getText());
            if (size < 1 || size > 100) throw new Exception("wrong number");
            props.setProperty("General.UndoStackSize", undoStackSize.getText());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, GT._("Undo/redo stack size") + " " + GT._("must be a number from 1 to 100"), GT._("Undo/redo stack size"), JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    /**
     * Required by the ActionListener interface.
     */
    public void actionPerformed(ActionEvent e) {
    }

    public JFrame getFrame() {
        return frame;
    }

    public void setFrame(JFrame frame) {
        this.frame = frame;
    }
}
