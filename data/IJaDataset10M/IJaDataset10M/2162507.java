package org.yccheok.jstock.gui.table;

import java.awt.Color;
import java.awt.Component;
import javax.swing.DefaultCellEditor;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

public abstract class GenericEditor extends DefaultCellEditor {

    Class[] argTypes = new Class[] { String.class };

    java.lang.reflect.Constructor constructor;

    Object value;

    public GenericEditor() {
        super(new JTextField());
        getComponent().setName("Table.editor");
    }

    public abstract boolean validate(Object value);

    public abstract boolean isEmptyAllowed();

    @Override
    public boolean stopCellEditing() {
        String s = (String) super.getCellEditorValue();
        if ("".equals(s)) {
            if (isEmptyAllowed()) {
                if (constructor.getDeclaringClass() == String.class) {
                    value = s;
                }
                super.stopCellEditing();
            } else {
                ((JComponent) getComponent()).setBorder(new LineBorder(Color.red));
                return false;
            }
        }
        try {
            value = constructor.newInstance(new Object[] { s });
        } catch (Exception e) {
            ((JComponent) getComponent()).setBorder(new LineBorder(Color.red));
            return false;
        }
        if (false == validate(value)) {
            ((JComponent) getComponent()).setBorder(new LineBorder(Color.red));
            return false;
        }
        return super.stopCellEditing();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        this.value = null;
        ((JComponent) getComponent()).setBorder(new LineBorder(Color.black));
        try {
            Class type = table.getColumnClass(column);
            if (type == Object.class) {
                type = String.class;
            }
            constructor = type.getConstructor(argTypes);
        } catch (Exception e) {
            return null;
        }
        return super.getTableCellEditorComponent(table, value, isSelected, row, column);
    }

    @Override
    public Object getCellEditorValue() {
        return value;
    }
}
