package org.aacc.administrationpanel.jasper.misc;

import java.awt.GridBagConstraints;
import java.util.HashMap;
import javax.swing.JComponent;
import javax.swing.JTextField;
import net.sf.jasperreports.engine.JRParameter;

/**
 * This class wraps any-typed parameter that is not wanted to be set up in 
 * the GUI. The component is a disabled JTextField.
 * @author Alvaro
 */
public class EmptyParameterWrapper extends JasperParameterWrapper {

    private JTextField field;

    public EmptyParameterWrapper(JRParameter parameter) {
        super(parameter);
    }

    @Override
    public JComponent getComponent() {
        if (field == null) {
            field = new JTextField("");
            initValues(field);
        }
        getConstraints().gridx = 1;
        getConstraints().weightx = 1.0;
        getConstraints().gridwidth = 3;
        getConstraints().fill = GridBagConstraints.HORIZONTAL;
        return field;
    }

    @Override
    protected void initValues(JComponent component) {
        super.initValues(component);
        component.setEnabled(false);
    }

    @Override
    public Object getValue() {
        return "";
    }

    @Override
    public void addValuesTo(HashMap<String, Object> map) {
    }

    @Override
    public boolean validate() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
