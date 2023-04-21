package de.hpi.eworld.scenarios;

import java.awt.Dimension;
import javax.swing.AbstractSpinnerModel;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeListener;

public class ConfigurationPanel extends JPanel {

    private static final long serialVersionUID = -6500908648814753666L;

    public JLabel addInputComponent(String name, JComponent component) {
        JLabel fieldLabel = new JLabel(name);
        fieldLabel.setAlignmentX(LEFT_ALIGNMENT);
        add(fieldLabel);
        component.setMaximumSize(new Dimension(Integer.MAX_VALUE, component.getPreferredSize().height));
        component.setAlignmentX(LEFT_ALIGNMENT);
        add(component);
        return fieldLabel;
    }

    public JSpinner createNumberInputField(int defaultVal, int minVal, int maxVal, ChangeListener changeListener) {
        JSpinner inputField;
        AbstractSpinnerModel spinnerModel = new SpinnerNumberModel(defaultVal, minVal, maxVal, 1);
        inputField = new JSpinner(spinnerModel);
        inputField.addChangeListener(changeListener);
        return inputField;
    }
}
