package org.one.stone.soup.xapp.swing.components;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JRadioButton;
import org.one.stone.soup.xapp.components.XappField;
import org.one.stone.soup.xapp.components.XappRadioButton;
import org.one.stone.soup.xapp.components.XappRadioSelectionField;

/**
 * @author nikcross
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class XappSwingRadioSelectionField implements XappField, XappRadioSelectionField {

    private String alias;

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public class XappSwingRadioButton implements XappRadioButton {

        private String alias;

        public String getAlias() {
            return alias;
        }

        public void setAlias(String alias) {
            this.alias = alias;
        }

        private String value;

        private String name;

        private JRadioButton ui;

        private XappRadioSelectionField group;

        private XappSwingRadioButton(String name, XappRadioSelectionField group) {
            this.name = name;
            this.group = group;
            ui = new JRadioButton();
            ui.setName(name);
        }

        public String getName() {
            return name;
        }

        public void setData(String value) {
            ui.setText(value);
        }

        public String getData() {
            return ui.getText();
        }

        public JComponent getUI() {
            return ui;
        }

        public void addActionListener(ActionListener actionListener) {
        }

        public void setBackground(Color color) {
        }

        public void setForeground(Color color) {
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setTip(String text) {
        }

        public void setEditable(boolean state) {
        }

        public void setEnabled(boolean state) {
        }
    }

    private String name;

    private ButtonGroup options = new ButtonGroup();

    public XappSwingRadioSelectionField(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setData(String value) {
    }

    public XappRadioButton addOption(String name) {
        XappSwingRadioButton button = new XappSwingRadioButton(name, this);
        options.add((AbstractButton) button.getUI());
        return button;
    }

    public String getData() {
        return ((XappRadioButton) options.getSelection().getSelectedObjects()[0]).getData();
    }

    public void actionPerformed(ActionEvent e) {
    }

    public void addActionListener(ActionListener actionListener) {
    }

    public void setTip(String text) {
    }

    public void setBackground(Color color) {
    }

    public void setForeground(Color color) {
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEditable(boolean state) {
    }

    public void setEnabled(boolean state) {
    }
}
