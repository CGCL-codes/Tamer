package com.rapidminer.gui.properties.celleditors.value;

import java.awt.Component;
import java.awt.event.ActionEvent;
import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JTable;
import com.rapidminer.gui.tools.ResourceAction;
import com.rapidminer.gui.wizards.ConfigurationWizardCreator;
import com.rapidminer.operator.Operator;
import com.rapidminer.parameter.ParameterTypeConfiguration;

/**
 * Cell editor consisting of a simple button which opens a configuration wizard for 
 * the corresponding operator.
 * 
 * @author Ingo Mierswa
 */
public class ConfigurationWizardValueCellEditor extends AbstractCellEditor implements PropertyValueCellEditor {

    private static final long serialVersionUID = -7163760967040772736L;

    private final transient ParameterTypeConfiguration type;

    private final JButton button;

    public ConfigurationWizardValueCellEditor(ParameterTypeConfiguration type) {
        this.type = type;
        button = new JButton(new ResourceAction(true, "wizard." + type.getWizardCreator().getI18NKey()) {

            private static final long serialVersionUID = 5340097986173787690L;

            @Override
            public void actionPerformed(ActionEvent e) {
                buttonPressed();
            }
        });
        button.setToolTipText(type.getDescription());
    }

    /** Does nothing. */
    public void setOperator(Operator operator) {
    }

    private void buttonPressed() {
        ConfigurationWizardCreator creator = type.getWizardCreator();
        if (creator != null) creator.createConfigurationWizard(type, type.getWizardListener());
    }

    public Object getCellEditorValue() {
        return null;
    }

    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int col) {
        return button;
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        return getTableCellEditorComponent(table, value, isSelected, row, column);
    }

    public boolean useEditorAsRenderer() {
        return true;
    }

    @Override
    public boolean rendersLabel() {
        return true;
    }
}
