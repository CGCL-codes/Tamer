package org.opensourcephysics.ejs.control;

import java.util.Collection;
import org.opensourcephysics.controls.ParsableTextArea;
import org.opensourcephysics.ejs.control.swing.ControlInputArea;
import org.opensourcephysics.ejs.control.swing.ControlTextArea;
import org.opensourcephysics.ejs.control.value.StringValue;
import org.opensourcephysics.ejs.control.value.Value;

/**
 * A blend of GroupControl and org.opensourcephysics.control.Control
 */
public class EjsControl extends GroupControl implements org.opensourcephysics.controls.Control {

    static String _RETURN_ = System.getProperty("line.separator");

    private ControlTextArea messageArea = null;

    private ParsableTextArea inputArea = null;

    private StringValue strValue = new StringValue("");

    /**
   * The EjsControl constructor.
   * @param     Object _simulation  The simulation that will receive the control's actions.
   */
    public EjsControl(Object _simulation) {
        super(_simulation);
    }

    public EjsControl(Object _simulation, String _replaceName, java.awt.Frame _replaceOwnerFrame) {
        super(_simulation, _replaceName, _replaceOwnerFrame);
    }

    public EjsControl() {
        super();
    }

    public ControlElement addObject(Object _object, String _classname, String _propList) {
        ControlElement control = super.addObject(_object, _classname, _propList);
        if (control instanceof ControlTextArea) {
            messageArea = (ControlTextArea) control;
        } else if (control instanceof ControlInputArea) {
            inputArea = (ParsableTextArea) ((ControlInputArea) control).getVisual();
        }
        return control;
    }

    public void reset() {
        clearValues();
        clearMessages();
        super.reset();
    }

    /**
 * Locks the control's interface. Values sent to the control will not
 * update the display until the control is unlocked.
 *
 * @param lock boolean
 */
    public void setLockValues(boolean lock) {
    }

    /**
 *  Reads the current property names.
 *
 * @return      the property names
 */
    public Collection getPropertyNames() {
        return variableTable.keySet();
    }

    public void clearValues() {
        if (inputArea != null) {
            inputArea.setText("");
            inputArea.setCaretPosition(inputArea.getText().length());
        }
    }

    public void clearMessages() {
        if (messageArea != null) {
            messageArea.clear();
        }
    }

    public void println(String s) {
        print(s + _RETURN_);
    }

    public void println() {
        println("");
    }

    public void print(String s) {
        if (messageArea != null) {
            messageArea.print(s);
        } else {
            System.out.print(s);
        }
    }

    public void calculationDone(String message) {
        println(message);
    }

    public void setValue(String _variable, Value _value) {
        if (!isVariableRegistered(_variable) && inputArea != null) {
            inputArea.setValue(_variable, _value.getString());
        } else {
            super.setValue(_variable, _value);
        }
    }

    public Value getValue(String _variable) {
        if (!isVariableRegistered(_variable) && inputArea != null) {
            try {
                strValue.value = inputArea.getValue(_variable);
                return strValue;
            } catch (org.opensourcephysics.controls.VariableNotFoundException e) {
            }
        }
        return super.getValue(_variable);
    }
}
