package com.volantis.mcs.eclipse.ab.editors.devices;

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.jface.util.ListenerList;
import org.jdom.Element;
import com.volantis.devrep.repository.api.devices.DeviceRepositorySchemaConstants;

/**
 * Provides an abstract implementation for the <code>PolicyValueModifier</code>
 * interface.
 */
public abstract class AbstractPolicyValueModifier implements PolicyValueModifier {

    /**
     * The Element representing a policy that is being modified.
     */
    protected Element policyElement;

    /**
     * Used to listeners that have been registered with this class.
     */
    protected ListenerList listeners;

    /**
     * Initializes a <code>AbstractPolicyValueModifier</code> instance
     */
    public AbstractPolicyValueModifier() {
        listeners = new ListenerList();
    }

    /**
     * Fires a ModifyText event to all registered listeners. The ModifyEvent
     * object that is fired contains the subclasse's control in its widget
     * field, and the actual text in its data field.
     *
     * This method is final to help prevent more complexity being incurred with
     * event notification.
     */
    protected final void fireModifyEvent(Object newValue) {
        Object[] interested = listeners.getListeners();
        if (interested != null && interested.length > 0) {
            Event event = new Event();
            event.data = newValue;
            event.widget = getControl();
            ModifyEvent me = new ModifyEvent(event);
            for (int i = 0; i < interested.length; i++) {
                ((ModifyListener) interested[i]).modifyText(me);
            }
        }
    }

    public void setPolicy(Element element) {
        if (element == null) {
            throw new IllegalArgumentException("element cannot be null");
        }
        if (!DeviceRepositorySchemaConstants.POLICY_ELEMENT_NAME.equals(element.getName())) {
            throw new IllegalArgumentException("Expected a policy element but got a '" + element.getName() + "' element");
        }
        setModifiableElement(element);
    }

    /**
     * This sets the modifiable element that this modifier is operating on.
     * Note this will be either a "policy" element or a "field" element.
     * @param element the modifiable element
     */
    final void setModifiableElement(Element element) {
        this.policyElement = element;
        refreshControl();
    }

    /**
     * Subclasses need to implements this so that the underlying SWT control
     * displays the underlying elements value
     */
    abstract void refreshControl();

    public Element getPolicy() {
        return policyElement;
    }

    public void addModifyListener(ModifyListener listener) {
        if (listener != null) {
            listeners.add(listener);
        }
    }

    public void removeModifyListener(ModifyListener listener) {
        if (listener != null) {
            listeners.remove(listener);
        }
    }
}
