package com.hifiremote.jp1;

import java.awt.Component;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JTable;

/**
 * The Class ChoiceEditor.
 */
public class ChoiceEditor extends DefaultCellEditor {

    /**
   * Instantiates a new choice editor.
   * 
   * @param choices
   *          the choices
   */
    public ChoiceEditor(Choice[] choices) {
        this(choices, true);
    }

    /**
   * Instantiates a new choice editor.
   * 
   * @param choices
   *          the choices
   * @param allowNull
   *          the allow null
   */
    public ChoiceEditor(Choice[] choices, boolean allowNull) {
        super(new JComboBox());
        setClickCountToStart(RMConstants.ClickCountToStart);
        this.choices = choices;
        if (allowNull) {
            adjust = 1;
        }
        comboBox = (JComboBox) getComponent();
        initialize();
    }

    /**
   * Initialize.
   */
    public void initialize() {
        int visibleCount = 0;
        for (int i = 0; i < choices.length; i++) {
            if (!choices[i].isHidden()) {
                visibleCount++;
            }
        }
        visibleCount += adjust;
        Choice[] temp = new Choice[visibleCount];
        int tempIndex = 0;
        if (adjust != 0) {
            temp[0] = new Choice(-1, "");
            tempIndex = 1;
        }
        for (int i = 0; i < choices.length; i++) {
            if (!choices[i].isHidden()) {
                temp[tempIndex++] = choices[i];
            }
        }
        comboBox.setModel(new DefaultComboBoxModel(temp));
    }

    @Override
    public Object getCellEditorValue() {
        Choice temp = (Choice) super.getCellEditorValue();
        if (temp.getIndex() == -1) {
            return null;
        } else {
            return temp;
        }
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int col) {
        System.err.println("ChoiceEditor.getTableCellEditorComponent(), value=" + value + ", row=" + row);
        if (value != null) {
            Class<?> c = value.getClass();
            if (c == String.class) {
                for (int i = 0; i < choices.length; i++) {
                    Choice choice = choices[i];
                    if (choice.getText().equals(value)) {
                        comboBox.setSelectedItem(choice);
                    }
                }
            } else if (c == Choice.class) {
                comboBox.setSelectedItem(value);
            } else if (c == Integer.class) {
                comboBox.setSelectedIndex(((Integer) value).intValue() + adjust);
            }
        } else {
            comboBox.setSelectedIndex(0);
        }
        return comboBox;
    }

    /** The combo box. */
    private JComboBox comboBox = null;

    /** The choices. */
    private Choice[] choices = null;

    /** The adjust. */
    private int adjust;
}
