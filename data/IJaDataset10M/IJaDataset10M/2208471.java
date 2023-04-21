package uchicago.src.reflector;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import uchicago.src.sim.engine.CustomProbeable;
import uchicago.src.sim.gui.Named;
import uchicago.src.sim.util.SimUtilities;

/**
 * Button property for introspection panels. Clicking the button should
 * bring up a new introspection frame for the wrapped object.
 */
public class PropertyButton extends JButton implements PropertyWidget {

    private String propertyName = null;

    private String value = "";

    private Object property = null;

    private boolean actionAdded = false;

    public PropertyButton(Object prop, boolean isEnabled) {
        super();
        String label = prop.getClass().toString();
        int dot = label.lastIndexOf(".");
        if (dot != -1) {
            label = label.substring(dot + 1, label.length());
        }
        super.setText(label);
        super.setEnabled(isEnabled);
        this.property = prop;
    }

    private Action probe = new AbstractAction() {

        public void actionPerformed(ActionEvent evt) {
            final IntrospectFrame spector;
            String objName = "";
            if (property instanceof Named) {
                Named n = (Named) property;
                objName = n.getName();
            }
            if (property instanceof CustomProbeable) {
                CustomProbeable cp = (CustomProbeable) property;
                spector = new IntrospectFrame(property, objName, cp.getProbedProperties());
            } else {
                spector = new IntrospectFrame(property, objName);
            }
            try {
                spector.display();
            } catch (Exception ex) {
                SimUtilities.showError("Probing error", ex);
                ex.printStackTrace();
                System.exit(0);
            }
        }
    };

    public void addActionListener(ActionListener listener) {
        if (!actionAdded) super.addActionListener(probe);
        actionAdded = true;
    }

    public void setPropertyName(String propName) {
        propertyName = propName;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setValue(Object value) {
        if (value == null) setEnabled(false); else setEnabled(true);
        property = value;
    }

    public Object getValue() {
        return value.toString();
    }
}
