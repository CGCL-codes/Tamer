package org.autocomplete;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * An implementation of the AbstractAutoCompleteAdaptor that is suitable for JComboBox.
 */
public class ComboBoxAdaptor extends AbstractAutoCompleteAdaptor implements ActionListener {

    /**
     * the combobox being adapted
     */
    private JComboBox comboBox;

    /**
     * Creates a new ComobBoxAdaptor for the given combobox.
     *
     * @param comboBox the combobox that should be adapted
     */
    public ComboBoxAdaptor(JComboBox comboBox) {
        this.comboBox = comboBox;
        comboBox.addActionListener(this);
    }

    public void actionPerformed(ActionEvent actionEvent) {
        markEntireText();
    }

    public int getItemCount() {
        return comboBox.getItemCount();
    }

    public Object getItem(int index) {
        return comboBox.getItemAt(index);
    }

    public void setSelectedItem(Object item) {
        comboBox.setSelectedItem(item);
    }

    public Object getSelectedItem() {
        return comboBox.getModel().getSelectedItem();
    }

    public JTextComponent getTextComponent() {
        return (JTextComponent) comboBox.getEditor().getEditorComponent();
    }
}
