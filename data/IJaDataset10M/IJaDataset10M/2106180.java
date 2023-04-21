package org.ws4d.java.configuration;

import org.ws4d.java.communication.CommunicationBinding;
import org.ws4d.java.communication.DiscoveryBinding;
import org.ws4d.java.structures.ArrayList;
import org.ws4d.java.structures.HashMap;
import org.ws4d.java.structures.Iterator;
import org.ws4d.java.structures.List;

/**
 * 
 */
public class BindingProperties implements PropertiesHandler {

    public static final String PROP_BINDING_ID = "BindingId";

    public static final Integer DEFAULT_BINDING_ID = new Integer(-1);

    private HashMap bindings = new HashMap();

    private HashMap discoveryBindings = new HashMap();

    private static String className = null;

    BindingProperties() {
        super();
        className = this.getClass().getName();
    }

    public static BindingProperties getInstance() {
        return (BindingProperties) Properties.forClassName(Properties.BINDING_PROPERTIES_HANDLER_CLASS);
    }

    /**
	 * Returns class name, if object of this class was already created, else
	 * null.
	 * 
	 * @return Class name, if object of this class was already created, else
	 *         null.
	 */
    public static String getClassName() {
        return className;
    }

    public void setProperties(PropertyHeader header, Property property) {
    }

    public void finishedSection(int depth) {
    }

    void addCommunicationBinding(Integer bindingId, ArrayList binding) {
        bindings.put(bindingId, binding);
    }

    void addDiscoveryBinding(Integer bindingId, DiscoveryBinding binding) {
        discoveryBindings.put(bindingId, binding);
    }

    /**
	 * Returns a List with bindings.
	 * 
	 * @param bindingId List with Bindings
	 * @return
	 */
    public List getCommunicationBinding(Integer bindingId) {
        return (ArrayList) bindings.get(bindingId);
    }

    public CommunicationBinding getDiscoveryBinding(Integer bindingId) {
        return (CommunicationBinding) discoveryBindings.get(bindingId);
    }

    public String toString() {
        StringBuffer out = new StringBuffer(50 * bindings.size());
        for (Iterator it = bindings.entrySet().iterator(); it.hasNext(); ) {
            HashMap.Entry entry = (HashMap.Entry) it.next();
            out.append(entry.getKey() + "=" + entry.getValue() + " | ");
        }
        return out.toString();
    }
}
