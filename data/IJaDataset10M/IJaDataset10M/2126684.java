package fr.itris.glips.rtdaeditor.anim.widgets;

import javax.swing.*;
import fr.itris.glips.rtdaeditor.anim.*;
import java.awt.event.*;
import java.util.*;

/**
 * the class of the event chooser
 * @author ITRIS, Jordi SUC
 */
public class TagValuesChooser extends Widget {

    /**
	 * the combo
	 */
    private JComboBox combo = new JComboBox();

    /**
	 * the combo listener
	 */
    private ActionListener comboListener = null;

    /**
	 * the constructor of the class
	 * @param isEditor whether the widget should be used for editing or not
	 */
    protected TagValuesChooser(boolean isEditor) {
        super(isEditor);
        buildWidget();
    }

    /**
	 * builds the widget
	 */
    protected void buildWidget() {
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        add(combo);
        if (isEditor) {
            comboListener = new ActionListener() {

                public void actionPerformed(ActionEvent evt) {
                    if (getItem() != null && combo.getSelectedItem() != null) {
                        ComboListItem comboItem = (ComboListItem) combo.getSelectedItem();
                        if (comboItem != null) {
                            getItem().setValue(comboItem.getValue().toString());
                        }
                    }
                    if (validateRunnable != null) {
                        validateRunnable.run();
                    }
                }
            };
        }
    }

    @Override
    protected void setItem(EditableItem item, Runnable validateRunnable) {
        super.setItem(item, validateRunnable);
        if (isEditor) {
            combo.removeActionListener(comboListener);
            combo.removeAllItems();
            String currentValue = item.getValue();
            if (currentValue == null) {
                currentValue = "";
            }
            String valueToSelect = currentValue;
            if (valueToSelect.equals("")) {
                valueToSelect = item.getDefaultValue();
            }
            ComboListItem emptyItem = new ComboListItem("", "");
            combo.addItem(emptyItem);
            Map<String, String> possibleValues = item.getPossibleValues();
            if (possibleValues != null) {
                String label = "", value = "";
                ComboListItem comboItem = null, selectedComboItem = emptyItem;
                for (String name : possibleValues.keySet()) {
                    try {
                        label = item.getAnimationObject().getLabel(name);
                    } catch (Exception ex) {
                        label = name;
                    }
                    value = possibleValues.get(name);
                    comboItem = new ComboListItem(value, label);
                    if (currentValue.equals(value)) {
                        selectedComboItem = comboItem;
                    }
                    combo.addItem(comboItem);
                }
                combo.setSelectedItem(selectedComboItem);
            }
            combo.addActionListener(comboListener);
        } else {
            combo.removeAllItems();
            String value = item.getValue();
            if (item.getPossibleValues().size() == 0 && value != null && !value.equals("")) {
                value = "";
            }
            combo.addItem(new ComboListItem(value, value));
        }
    }
}
