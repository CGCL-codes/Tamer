package com.horstmann.violet.framework.propertyeditor.customeditor;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyEditorSupport;
import javax.swing.JComboBox;
import com.horstmann.violet.product.diagram.abstracts.property.ChoiceList;

/**
 * A property editor for the ChoiceList type.
 * 
 * @author Alexandre de Pellegrin
 */
public class ChoiceListEditor extends PropertyEditorSupport {

    public boolean supportsCustomEditor() {
        return true;
    }

    public Component getCustomEditor() {
        ChoiceList list = (ChoiceList) getValue();
        final JComboBox comboBox = new JComboBox(list.getList());
        comboBox.setSelectedItem(list.getSelectedItem());
        comboBox.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                String selected = (String) comboBox.getSelectedItem();
                ChoiceList list = (ChoiceList) getValue();
                list.setSelectedItem(selected);
            }
        });
        return comboBox;
    }
}
