package com.volantis.mcs.eclipse.ab.editors.devices;

import java.util.Iterator;
import java.util.List;
import org.eclipse.jface.util.ListenerList;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Event;
import org.jdom.Attribute;
import org.jdom.Element;
import com.volantis.devrep.repository.api.devices.DeviceRepositorySchemaConstants;

/**
 * The SingleValuePolicyValueModifier allows a user to modify the value of a
 * single-valued policy using an SWT control. The policy element is
 * supplied with {@link #setPolicy}, and the modified policy can be
 * retrieved with {@link #getPolicy}. ModifyListeners can be added and
 * removed from a SingleValuePolicyValueModifier. When a change occurs, a
 * ModifyEvent is fired off to all registered listeners. This event object
 * contains the changed text in its data field, and the SWT control that gave
 * rise to the change in its widget field.
 */
public abstract class SingleValuePolicyValueModifier extends AbstractPolicyValueModifier {

    /**
     * Constructs a new SingleValuePolicyValueModifier.
     */
    protected SingleValuePolicyValueModifier() {
    }

    /**
     * Sets the current value of the policy element as the current
     * value of the Control used by the subclass. This is used by
     * SingleValuePolicyValueModifier to display the current policy value in
     * the control when the policy element is set with {@link #setPolicy}.
     * @param value the value to set
     */
    protected abstract void setValue(String value);

    protected void refreshControl() {
        if (!policyElement.getChildren(DeviceRepositorySchemaConstants.POLICY_VALUE_ELEMENT_NAME, policyElement.getNamespace()).isEmpty()) {
            throw new IllegalArgumentException("Element " + policyElement.getName() + " is not single-valued.");
        }
        String value = policyElement.getAttributeValue(DeviceRepositorySchemaConstants.POLICY_VALUE_ATTRIBUTE);
        setValue(value);
    }

    /**
     * Updates the policy element in response to changes in the control
     * used by SingleValuePolicyValueModifier. Subclasses should call this
     * method when notified of changes in their control.
     * @param newPolicyValue the new value for the policy element. Can be null.
     */
    protected void updatePolicyElement(String newPolicyValue) {
        boolean valueChanged = false;
        Attribute valueAttr = policyElement.getAttribute(DeviceRepositorySchemaConstants.POLICY_VALUE_ATTRIBUTE);
        if (newPolicyValue == null || newPolicyValue.length() == 0) {
            if (valueAttr != null) {
                policyElement.removeAttribute(valueAttr);
                valueChanged = true;
            }
        } else if (valueAttr == null) {
            policyElement.setAttribute(DeviceRepositorySchemaConstants.POLICY_VALUE_ATTRIBUTE, newPolicyValue);
            valueChanged = true;
        } else {
            valueAttr.setValue(newPolicyValue);
            valueChanged = true;
        }
        if (valueChanged) {
            fireModifyEvent(newPolicyValue);
        }
    }
}
