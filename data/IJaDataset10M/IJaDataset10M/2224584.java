package edu.ncssm.iwp.ui.widgets;

import javax.swing.*;
import java.awt.event.ItemListener;
import java.awt.BorderLayout;

public class GInput_Selector extends GInput {

    private static final long serialVersionUID = 1L;

    JComboBox comboBox;

    public GInput_Selector(String iLabel, String[] iValues) {
        super(iLabel);
        comboBox = new JComboBox(iValues);
        setLayout(new BorderLayout());
        if (isLabelDefined()) {
            add(BorderLayout.WEST, getLabel());
        }
        add(BorderLayout.CENTER, comboBox);
    }

    public void setSelected(String iValue) {
        comboBox.setSelectedItem(iValue);
    }

    public String getSelected() {
        for (int i = 0; i < comboBox.getItemCount(); i++) {
        }
        return (String) (comboBox.getSelectedItem());
    }

    public String getValue() {
        return getSelected();
    }

    public void addItemListener(ItemListener i) {
        comboBox.addItemListener(i);
    }
}
