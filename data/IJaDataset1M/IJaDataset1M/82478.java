package org.mpn.contacts.framework.ui;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * todo [!] Create javadocs for org.mpn.contacts.framework.ui.UiComboBoxEditable here
 *
 * @author <a href="mailto:pmoukhataev@jnetx.ru">Pavel Moukhataev</a>
 * @version $Revision$
 */
public class UiComboBoxAbstract<Type> extends UiComponent<Type, JComboBox> {

    public JComboBox createUiComponent() {
        JComboBox comboBox = new JComboBox();
        comboBox.setEditable(false);
        comboBox.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                notifyListeners(UiComboBoxAbstract.this);
            }
        });
        return comboBox;
    }

    public Type getData() {
        return (Type) uiComponent.getSelectedItem();
    }

    public void setData(Type data) {
        for (int i = 0; i < uiComponent.getItemCount(); i++) {
            UiComboBoxItem comboBoxItem = (UiComboBoxItem) uiComponent.getItemAt(i);
            if (comboBoxItem.equals(data)) {
                uiComponent.setSelectedItem(comboBoxItem);
                return;
            }
        }
    }

    public Type getDefaultValue() {
        return null;
    }
}
