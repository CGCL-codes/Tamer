package guineu.parameters.parametersType;

import guineu.main.GuineuCore;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JPanel;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;

/**
 */
public class RTToleranceComponent extends JPanel implements ActionListener {

    private static final String toleranceTypes[] = { "absolute", "relative (%)" };

    private JFormattedTextField toleranceField;

    private JComboBox toleranceType;

    public RTToleranceComponent() {
        super(new BorderLayout());
        toleranceField = new JFormattedTextField(GuineuCore.getRTFormat());
        toleranceField.setColumns(6);
        add(toleranceField, BorderLayout.CENTER);
        toleranceType = new JComboBox(toleranceTypes);
        toleranceType.addActionListener(this);
        add(toleranceType, BorderLayout.EAST);
    }

    public void setValue(RTTolerance value) {
        if (value.isAbsolute()) {
            toleranceType.setSelectedIndex(0);
            DefaultFormatterFactory fact = new DefaultFormatterFactory(new NumberFormatter(GuineuCore.getRTFormat()));
            toleranceField.setFormatterFactory(fact);
            toleranceField.setValue(value.getTolerance());
        } else {
            toleranceType.setSelectedIndex(1);
            DefaultFormatterFactory fact = new DefaultFormatterFactory(new NumberFormatter(NumberFormat.getNumberInstance()));
            toleranceField.setFormatterFactory(fact);
            toleranceField.setValue(value.getTolerance() * 100);
        }
    }

    public RTTolerance getValue() {
        int index = toleranceType.getSelectedIndex();
        Number tol = (Number) toleranceField.getValue();
        if (tol == null) return null;
        double toleranceValue = tol.doubleValue();
        if (index == 1) toleranceValue /= 100;
        RTTolerance value = new RTTolerance(index <= 0, toleranceValue);
        return value;
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        Object src = event.getSource();
        if (src == toleranceType) {
            if (toleranceType.getSelectedIndex() <= 0) {
                DefaultFormatterFactory fact = new DefaultFormatterFactory(new NumberFormatter(GuineuCore.getRTFormat()));
                toleranceField.setFormatterFactory(fact);
            } else {
                DefaultFormatterFactory fact = new DefaultFormatterFactory(new NumberFormatter(NumberFormat.getNumberInstance()));
                toleranceField.setFormatterFactory(fact);
            }
        }
    }
}
