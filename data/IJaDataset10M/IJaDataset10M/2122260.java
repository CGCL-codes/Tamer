package org.gaea.ui.graphic.data.supportedclass;

import java.awt.BorderLayout;
import java.util.Vector;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Supported String UI Part
 * 
 * @author jsgoupil
 */
public class SupportedString extends org.gaea.common.supportedclass.SupportedString implements ISupportedUI {

    /** Simple textField */
    private JTextField _valueTextField;

    /**
	 * Constructor
	 */
    public SupportedString() {
        super();
    }

    public JPanel getPanel() {
        _valueTextField = new JTextField(25);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(_valueTextField, BorderLayout.CENTER);
        return panel;
    }

    public void setPanelObject(Object object) {
        _valueTextField.setText((String) object);
    }

    public Object getPanelObject(Vector<String> errorsList) {
        return _valueTextField.getText();
    }

    public void resetPanelObject() {
        _valueTextField.setText("");
    }

    public void setEnabled(boolean enabled) {
        _valueTextField.setEnabled(enabled);
    }
}
