package edu.udo.scaffoldhunter.plugins.datacalculation.impl.example5;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.util.Set;
import javax.swing.DefaultListModel;
import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import edu.udo.scaffoldhunter.model.PropertyType;
import edu.udo.scaffoldhunter.model.db.PropertyDefinition;
import edu.udo.scaffoldhunter.plugins.PluginSettingsPanel;

/**
 * @author Philipp Lewe
 * 
 */
public class Example5CalcPluginSettingsPanel extends PluginSettingsPanel {

    private Example5CalcPluginArguments arguments;

    private JCheckBox checkbox;

    private JList list;

    Example5CalcPluginSettingsPanel(final Example5CalcPluginArguments arguments, Set<PropertyDefinition> availableProperties) {
        this.arguments = arguments;
        checkbox = new JCheckBox("if selected, 'new property = old value + 1.0', otherwise 'new property = old value - 0.1'", arguments.isCheckboxChecked());
        checkbox.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                arguments.setCheckboxIsChecked(checkbox.isSelected());
            }
        });
        add(checkbox);
        DefaultListModel model = new DefaultListModel();
        list = new JList(model);
        for (PropertyDefinition propertyDefinition : availableProperties) {
            if (propertyDefinition.getPropertyType() == PropertyType.NumProperty) {
                model.addElement(propertyDefinition);
            }
        }
        list.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting() && !list.isSelectionEmpty()) {
                    PropertyDefinition chosenProperty = (PropertyDefinition) list.getSelectedValue();
                    arguments.setPropDef(chosenProperty);
                } else {
                    arguments.setPropDef(null);
                }
            }
        });
        if (arguments.getPropDef() != null) {
            list.setSelectedValue(arguments.getPropDef(), true);
        } else {
            list.setSelectedIndex(0);
        }
        add(list);
    }

    @Override
    public Serializable getSettings() {
        return null;
    }

    @Override
    public Object getArguments() {
        return arguments;
    }
}
