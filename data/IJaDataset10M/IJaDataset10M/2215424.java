package common;

import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;

public class IntField {

    public static final Dimension INT_FIELD_DIM = new Dimension(50, StringField.TEXT_FLD_HEIGHT);

    private JPanel m_panel;

    private JFormattedTextField m_intField;

    public interface INotifyChange {

        void changedValue(int newValue);
    }

    public IntField(String label, int minVal, int maxVal, int defVal, final INotifyChange notifyChange) {
        this(label, minVal, maxVal, defVal, notifyChange, false);
    }

    public IntField(String label, int minVal, int maxVal, int defVal, final INotifyChange notifyChange, boolean floatLayoutPanel) {
        m_panel = floatLayoutPanel ? new JPanel() : Panels.createInputBoxPanel(300);
        m_panel.add(new JLabel(label));
        m_intField = getIntField(minVal, maxVal, defVal);
        m_intField.addPropertyChangeListener("value", new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                Object source = evt.getSource();
                if (source == m_intField) {
                    notifyChange.changedValue(((Number) m_intField.getValue()).intValue());
                }
            }
        });
        m_panel.add(m_intField);
    }

    public JPanel getPanel() {
        return m_panel;
    }

    public void setValue(int newValue) {
        m_intField.setValue(newValue);
    }

    private static JFormattedTextField getIntField(int minValue, int maxValue, int defValue) {
        NumberFormatter numFormatter = new NumberFormatter();
        numFormatter.setValueClass(Integer.class);
        numFormatter.setMaximum(new Integer(maxValue));
        numFormatter.setMinimum(new Integer(minValue));
        DefaultFormatterFactory formatterFactory = new DefaultFormatterFactory(numFormatter);
        JFormattedTextField intFld = new JFormattedTextField(formatterFactory);
        intFld.setValue(new Integer(defValue));
        intFld.setPreferredSize(INT_FIELD_DIM);
        intFld.setMaximumSize(INT_FIELD_DIM);
        return intFld;
    }
}
