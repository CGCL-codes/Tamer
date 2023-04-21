package com.sardak.blogoommer.ui;

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;
import com.sardak.blogoommer.ui.util.ButtonHelper;
import com.sardak.blogoommer.ui.util.Constants;

/**
 * Basic implementation of a form-based dialog box.
 * @author Ren� Ghosh
 */
public abstract class BlogoommerDialog extends JDialog implements ActionListener {

    protected static Map TEXTFIELDMAP = new HashMap();

    protected static Map COMBOFIELDMAP = new HashMap();

    protected static Map CHECKBOXFIELDMAP = new HashMap();

    private Container parent;

    /**
	 * Constructor I
	 */
    public BlogoommerDialog(JFrame parent) {
        super(parent);
        this.parent = parent;
        commonConstructor();
    }

    /**
	 * Constructor II
	 */
    public BlogoommerDialog(JDialog parent) {
        super(parent, "");
        this.parent = parent;
        commonConstructor();
    }

    /**
	 * show the dialog
	 */
    public void show() {
        Point parentLocation = parent.getLocation();
        int width = getWidth();
        int height = getHeight();
        int parentWidth = parent.getWidth();
        int parentHeight = parent.getHeight();
        int x = parentLocation.x + (int) (parentWidth / 2 - width / 2);
        int y = parentLocation.y + (int) (parentHeight / 2 - height / 2);
        setLocation(x, y);
        super.show();
    }

    /**
	 * set the value of a text component
	 */
    protected void setTextValue(String key, String value) {
        JTextComponent tc = ((JTextComponent) TEXTFIELDMAP.get(key));
        tc.setText(value);
        if (tc instanceof JTextArea) {
            ((JTextArea) tc).setRows(5);
        }
    }

    /**
	 * set the value of a combo component
	 */
    protected void setComboValue(String key, String value) {
        JComboBox box = ((JComboBox) COMBOFIELDMAP.get(key));
        for (int i = 0; i < box.getItemCount(); i++) {
            String cValue = (String) box.getItemAt(i);
            if (cValue.equals(value)) {
                box.setSelectedIndex(i);
            }
        }
    }

    /**
	 * get the value of a component
	 */
    protected String getTextValue(String key) {
        return ((JTextComponent) TEXTFIELDMAP.get(key)).getText();
    }

    /**
	 * set the enabled value of a text component
	 */
    protected void enableText(String key, boolean enabled) {
        ((JTextComponent) TEXTFIELDMAP.get(key)).setEnabled(enabled);
    }

    /**
	 * get the value of a checkBox component
	 */
    protected boolean getCheckBoxValue(String key) {
        return ((JCheckBox) CHECKBOXFIELDMAP.get(key)).isSelected();
    }

    /**
	 * get the value of a checkBox component
	 */
    protected void setCheckBoxValue(String key, boolean selected) {
        ((JCheckBox) CHECKBOXFIELDMAP.get(key)).setSelected(selected);
    }

    /**
	 * get checkbox component
	 */
    protected JCheckBox getCheckBoxComponent(String key) {
        return ((JCheckBox) CHECKBOXFIELDMAP.get(key));
    }

    /**
	 * get the selected index of a combo component
	 */
    protected int getComboValue(String key) {
        return ((JComboBox) COMBOFIELDMAP.get(key)).getSelectedIndex();
    }

    /**
	 * code comon to all ocnstrructors this class
	 */
    private void commonConstructor() {
        setModal(true);
        build();
        setResizable(true);
        pack();
    }

    protected abstract void load();

    protected abstract void build();

    protected abstract void save();

    /**
	 * build a button panel
	 */
    protected JPanel buildButtonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(Box.createHorizontalGlue());
        JButton okButton = ButtonHelper.buildButton(Constants.OK, this);
        JButton cancelButton = ButtonHelper.buildButton(Constants.CANCEL, this);
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
        return buttonPanel;
    }

    /**
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
    public abstract void actionPerformed(ActionEvent actionEvent);

    /**
	 * add a label
	 */
    protected void addLabel(String label, GridBagLayout layout) {
        GridBagConstraints constraints = new GridBagConstraints();
        JLabel component = new JLabel(Constants.get(label));
        constraints.insets = new Insets(4, 4, 4, 4);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 0.3;
        layout.setConstraints(component, constraints);
        getContentPane().add(component);
    }

    /**
	 * add a component to the form
	 */
    private void addComponent(GridBagLayout layout, JComponent component) {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.weightx = 0.7;
        if (!(component instanceof JScrollPane)) {
            constraints.fill = GridBagConstraints.BOTH;
        }
        constraints.insets = new Insets(4, 4, 4, 4);
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.fill = GridBagConstraints.BOTH;
        layout.setConstraints(component, constraints);
        getContentPane().add(component);
    }

    /**
	 * add a combo box to a form
	 */
    private void addComboComponent(String mapKey, GridBagLayout layout, JComboBox field) {
        COMBOFIELDMAP.put(mapKey, field);
        addComponent(layout, field);
    }

    /**
	 * add a checkbox to a form
	 */
    private void addCheckboxComponent(String mapKey, GridBagLayout layout, JCheckBox field) {
        CHECKBOXFIELDMAP.put(mapKey, field);
        addComponent(layout, field);
    }

    /**
	 * add a text component to a form
	 */
    private void addTextComponent(String mapKey, GridBagLayout layout, JTextComponent field) {
        JComponent component = null;
        if (field instanceof JTextArea) {
            component = new JScrollPane(field);
        } else {
            component = field;
        }
        TEXTFIELDMAP.put(mapKey, field);
        addComponent(layout, component);
    }

    /**
	 * add a text field
	 */
    protected void addTextField(String mapKey, GridBagLayout layout) {
        JTextField field = new JTextField(30);
        addTextComponent(mapKey, layout, field);
    }

    /**
	 * add a password text field
	 */
    protected void addPasswordField(String mapKey, GridBagLayout layout) {
        JPasswordField field = new JPasswordField(30);
        addTextComponent(mapKey, layout, field);
    }

    /**
	 * add a text area
	 */
    protected void addTextAreaField(String mapKey, GridBagLayout layout) {
        JTextArea field = new JTextArea(5, 30);
        addTextComponent(mapKey, layout, field);
    }

    /**
	 * add a text area
	 */
    protected void addComboBox(String mapKey, GridBagLayout layout, String[] values) {
        JComboBox box = new JComboBox(values);
        addComboComponent(mapKey, layout, box);
    }

    /**
	 * add a text area
	 */
    protected void addCheckBox(String mapKey, GridBagLayout layout, boolean selected) {
        JCheckBox box = new JCheckBox("", selected);
        addCheckboxComponent(mapKey, layout, box);
    }

    /**
	 * add a bottom panel to a grigbaglayour
	 */
    protected void addPanelToBottom(JPanel panel, GridBagLayout layout) {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.weightx = 1.0;
        constraints.insets = new Insets(4, 4, 4, 4);
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        layout.setConstraints(panel, constraints);
        getContentPane().add(panel);
    }

    /**
	 * get parent container
	 */
    public Container getParent() {
        return parent;
    }

    /**
	 * set parent container
	 */
    public void setParent(Container parent) {
        this.parent = parent;
    }
}
