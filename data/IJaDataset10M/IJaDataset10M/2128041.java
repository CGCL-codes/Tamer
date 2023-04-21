package com.jvito.gui;

import java.awt.Component;
import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JPasswordField;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import com.jvito.parameter.ParameterTypeDynamicCategory;
import com.rapidminer.parameter.ParameterTypeBoolean;
import com.rapidminer.parameter.ParameterTypeCategory;
import com.rapidminer.parameter.ParameterTypeDouble;
import com.rapidminer.parameter.ParameterTypeInt;
import com.rapidminer.parameter.ParameterTypePassword;
import com.rapidminer.parameter.ParameterTypeString;
import com.rapidminer.parameter.ParameterTypeStringCategory;

/**
 * An editor and renderer for the <code>Inspector</code>.
 * 
 * @author Daniel Hakenjos
 * @version $Id: InspectorValueEditor.java,v 1.3 2008/04/12 14:28:11 djhacker Exp $
 * 
 */
public class InspectorValueEditor extends DefaultCellEditor implements TableCellEditor, TableCellRenderer {

    private static final long serialVersionUID = 6546640806515689883L;

    public InspectorValueEditor(ParameterTypeBoolean type) {
        super(new JCheckBox());
        ((JCheckBox) editorComponent).setBackground(javax.swing.UIManager.getColor("Table.cellBackground"));
        ((JCheckBox) editorComponent).setToolTipText(type.getDescription() + " (" + type.getRange() + ")");
        ((JCheckBox) editorComponent).setHorizontalAlignment(SwingConstants.CENTER);
    }

    /**
	 * Inits this InspectorValueEditor</code>.
	 * 
	 * @param type
	 */
    public InspectorValueEditor(ParameterTypeInt type) {
        super(new JTextField());
        JTextField field = (JTextField) editorComponent;
        field.setToolTipText(type.getDescription() + " (" + type.getRange() + ")");
        field.setHorizontalAlignment(SwingConstants.LEFT);
    }

    /**
	 * Inits this InspectorValueEditor</code>.
	 * 
	 * @param type
	 */
    public InspectorValueEditor(final ParameterTypeDouble type) {
        super(new JTextField());
        JTextField field = (JTextField) editorComponent;
        field.setToolTipText(type.getDescription() + " (" + type.getRange() + ")");
        field.setHorizontalAlignment(SwingConstants.LEFT);
        this.delegate = new EditorDelegate() {

            private static final long serialVersionUID = 7767215615665031011L;

            @Override
            public void setValue(Object x) {
                super.setValue(x);
                if (x != null) {
                    if ((x instanceof Double) || (x instanceof String)) ((JTextField) editorComponent).setText(x.toString()); else throw new IllegalArgumentException("Illegal value class for double parameter: " + x.getClass().getName());
                }
            }

            @Override
            public Object getCellEditorValue() {
                try {
                    double d = Double.parseDouble(((JTextField) editorComponent).getText());
                    if (d < type.getMinValue()) d = type.getMinValue();
                    if (d > type.getMaxValue()) d = type.getMaxValue();
                    return new Double(d);
                } catch (NumberFormatException e) {
                    return type.getDefaultValue();
                }
            }
        };
        ((JTextField) editorComponent).addActionListener(delegate);
    }

    /**
	 * Inits this InspectorValueEditor</code>.
	 * 
	 * @param type
	 */
    public InspectorValueEditor(ParameterTypeString type) {
        super(new JTextField());
        JTextField field = (JTextField) editorComponent;
        field.setToolTipText(type.getDescription() + " (" + type.getRange() + ")");
        field.setHorizontalAlignment(SwingConstants.LEFT);
    }

    /**
	 * Inits this InspectorValueEditor</code>.
	 * 
	 * @param type
	 */
    public InspectorValueEditor(ParameterTypeCategory type) {
        super(new JComboBox(type.getValues()));
        JComboBox box = (JComboBox) editorComponent;
        box.setBackground(javax.swing.UIManager.getColor("Table.cellBackground"));
        box.setToolTipText(type.getDescription() + " (" + type.getRange() + ")");
        ((JComboBox) editorComponent).removeItemListener(this.delegate);
        this.delegate = new EditorDelegate() {

            private static final long serialVersionUID = 3864112914058601058L;

            @Override
            public void setValue(Object x) {
                super.setValue(x);
                ((JComboBox) editorComponent).setSelectedIndex(((Integer) x).intValue());
            }

            @Override
            public Object getCellEditorValue() {
                return new Integer(((JComboBox) editorComponent).getSelectedIndex());
            }
        };
        ((JComboBox) editorComponent).addItemListener(delegate);
    }

    /**
	 * Inits this InspectorValueEditor</code>.
	 * 
	 * @param type
	 */
    public InspectorValueEditor(ParameterTypeDynamicCategory type) {
        super(new JComboBox(type.getValues()));
        JComboBox box = (JComboBox) editorComponent;
        box.setSelectedIndex(0);
        box.setBackground(javax.swing.UIManager.getColor("Table.cellBackground"));
        box.setToolTipText(type.getDescription() + " (" + type.getRange() + ")");
    }

    /**
	 * Inits this InspectorValueEditor</code>.
	 * 
	 * @param type
	 */
    public InspectorValueEditor(ParameterTypeStringCategory type) {
        super(new JComboBox(type.getValues()));
        JComboBox box = (JComboBox) editorComponent;
        box.setEditable(true);
        box.setBackground(javax.swing.UIManager.getColor("Table.cellBackground"));
        box.setToolTipText(type.getDescription() + " (" + type.getRange() + ")");
    }

    /**
	 * Inits this InspectorValueEditor</code>.
	 * 
	 * @param type
	 */
    public InspectorValueEditor(ParameterTypePassword type) {
        super(new JPasswordField(""));
        JPasswordField field = (JPasswordField) editorComponent;
        field.setToolTipText(type.getDescription() + " (" + type.getRange() + ")");
        field.setHorizontalAlignment(SwingConstants.LEFT);
    }

    /**
	 * Gets the renderer-Component for the <code>Inspector</code>.
	 * 
	 * @see javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object,
	 *      boolean, boolean, int, int)
	 * @return Returns the component from the <code>TableCellEditor</code>.
	 */
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        return getTableCellEditorComponent(table, value, isSelected, row, column);
    }
}
