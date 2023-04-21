package org.personalsmartspace.ipojo.handlers.providedservice;

import java.util.Dictionary;
import java.util.Properties;
import org.osgi.framework.ServiceReference;
import org.personalsmartspace.ipojo.util.PSSProperty;

/**
 * Provided Service Description.
 * 
 * @author <a href="mailto:patx.cheevers@intel.com">Felix Project Team</a>
 */
public class PSSProvidedServiceDescription {

    /**
     * State : the service is unregistered.
     */
    public static final int UNREGISTERED = PSSProvidedService.UNREGISTERED;

    /**
     * State : the service is registered.
     */
    public static final int REGISTERED = PSSProvidedService.REGISTERED;

    /**
     * The describe provided service.
     */
    private PSSProvidedService m_ps;

    /**
     * Constructor.
     * @param ps the described provided service.
     */
    public PSSProvidedServiceDescription(PSSProvidedService ps) {
        m_ps = ps;
    }

    /**
     * Gets the list of provided service specifications.
     * @return the provided contract name.
     */
    public String[] getServiceSpecifications() {
        return m_ps.getServiceSpecifications();
    }

    /**
     * Gets the list of properties.
     * A copy of the actual property set is returned.
     * @return the properties.
     */
    public Properties getProperties() {
        Properties props = new Properties();
        PSSProperty[] ps = m_ps.getProperties();
        for (int i = 0; i < ps.length; i++) {
            if (ps[i].getValue() != PSSProperty.NO_VALUE) {
                props.put(ps[i].getName(), ps[i].getValue());
            }
        }
        return props;
    }

    /**
     * Adds and Updates service properties.
     * Existing properties are updated. 
     * New ones are added.
     * @param props the new properties
     */
    public void addProperties(Dictionary props) {
        m_ps.addProperties(props);
    }

    /**
     * Removes service properties.
     * @param props the properties to remove
     */
    public void removeProperties(Dictionary props) {
        m_ps.deleteProperties(props);
    }

    /**
     * Gets provided service state.
     * @return the state of the provided service (UNREGISTERED | REGISTRED).
     */
    public int getState() {
        return m_ps.getState();
    }

    /**
     * Gets the service reference.
     * @return the service reference (null if the service is unregistered).
     */
    public ServiceReference getServiceReference() {
        return m_ps.getServiceReference();
    }

    /**
     * Gets the 'main' service object.
     * @return the 'main' service object or <code>null</code>
     * if no service object are created.
     */
    public Object getService() {
        Object[] objs = m_ps.getInstanceManager().getPojoObjects();
        if (objs == null) {
            return null;
        } else {
            return objs[0];
        }
    }

    public Object[] getServices() {
        return m_ps.getInstanceManager().getPojoObjects();
    }
}
