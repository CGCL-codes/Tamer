package org.personalsmartspace.ipojo.composite.service.provides;

import java.util.Comparator;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import org.personalsmartspace.ipojo.PSSServiceContext;
import org.personalsmartspace.ipojo.composite.PSSCompositeManager;
import org.personalsmartspace.ipojo.util.PSSDependencyStateListener;
import org.personalsmartspace.ipojo.util.PSSLogger;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Filter;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.personalsmartspace.ipojo.util.PSSDependencyModel;

/**
 * Export an service from the scope to the parent context.
 * @author <a href="mailto:patx.cheevers@intel.com">Felix Project Team</a>
 */
public class PSSServiceExporter extends PSSDependencyModel {

    /**
     * The logger for the factory (and all component instances).
     */
    protected final PSSLogger m_logger;

    /**
     * Destination context.
     */
    private BundleContext m_destination;

    /**
     * Composite Manager.
     */
    private PSSCompositeManager m_manager;

    /**
     * Map of service reference - service registration storing exported services.
     */
    private Map m_registrations = new HashMap();

    /**
     * Constructor.
     * 
     * @param specification : exported service specification.
     * @param filter : LDAP filter
     * @param multiple : is the export an aggregate export?
     * @param optional : is the export optional?
     * @param cmp : comparator to use in the dependency
     * @param policy : binding policy.
     * @param from : internal service context
     * @param dest : parent bundle context
     * @param listener : dependency lifecycle listener to notify when the dependency state change. 
     * @param manager : composite manager
     */
    public PSSServiceExporter(Class specification, Filter filter, boolean multiple, boolean optional, Comparator cmp, int policy, PSSServiceContext from, BundleContext dest, PSSDependencyStateListener listener, PSSCompositeManager manager) {
        super(specification, multiple, optional, filter, cmp, policy, from, listener, manager);
        m_logger = new PSSLogger(from, specification.getName());
        m_destination = dest;
        m_manager = manager;
    }

    /**
     * Transform service reference property in a dictionary.
     * instance.name and factory.name are injected too.
     * @param ref : the service reference.
     * @return the dictionary containing all property of the given service reference.
     */
    private Dictionary getProps(ServiceReference ref) {
        Properties prop = new Properties();
        String[] keys = ref.getPropertyKeys();
        for (int i = 0; i < keys.length; i++) {
            prop.put(keys[i], ref.getProperty(keys[i]));
        }
        prop.put("instance.name", m_manager.getInstanceName());
        prop.put("factory.name", m_manager.getFactory().getName());
        return prop;
    }

    /**
     * Stop an exporter.
     * Remove the service listener
     * Unregister all exported services.
     */
    public void stop() {
        super.stop();
        Set refs = m_registrations.keySet();
        Iterator iterator = refs.iterator();
        while (iterator.hasNext()) {
            ServiceReference ref = (ServiceReference) iterator.next();
            ServiceRegistration reg = (ServiceRegistration) m_registrations.get(ref);
            try {
                reg.unregister();
            } catch (IllegalStateException e) {
            }
        }
        m_registrations.clear();
    }

    /**
     * A service has been injected. Register it.
     * @param reference : the new reference.
     * @see org.personalsmartspace.ipojo.util.TrackerCustomizer#addedService(org.osgi.framework.ServiceReference)
     */
    public void onServiceArrival(ServiceReference reference) {
        pssIpojoLog("onServiceArrival", reference, null);
        Object svc = getService(reference);
        ServiceRegistration reg = m_destination.registerService(getSpecification().getName(), svc, getProps(reference));
        m_registrations.put(reference, reg);
    }

    /**
     * An exported service was modified.
     * @param reference : modified reference
     * @see org.personalsmartspace.ipojo.util.TrackerCustomizer#modifiedService(org.osgi.framework.ServiceReference, java.lang.Object)
     */
    public void onServiceModification(ServiceReference reference) {
        pssIpojoLog("onServiceModification", reference, null);
        ServiceRegistration reg = (ServiceRegistration) m_registrations.get(reference);
        if (reg != null) {
            reg.setProperties(getProps(reference));
        }
    }

    /**
     * An exported service disappears.
     * @param reference : service reference
     * @see org.personalsmartspace.ipojo.util.TrackerCustomizer#removedService(org.osgi.framework.ServiceReference, java.lang.Object)
     */
    public void onServiceDeparture(ServiceReference reference) {
        pssIpojoLog("onServiceDepartuire", reference, null);
        ServiceRegistration reg = (ServiceRegistration) m_registrations.get(reference);
        if (reg != null) {
            reg.unregister();
        }
        m_registrations.remove(reference);
    }

    /**
     * On Dependency Reconfiguration notification method.
     * @param departs : leaving service references.
     * @param arrivals : new injected service references.
     * @see org.personalsmartspace.ipojo.util.DependencyModel#onDependencyReconfiguration(org.osgi.framework.ServiceReference[], org.osgi.framework.ServiceReference[])
     */
    public void onDependencyReconfiguration(ServiceReference[] departs, ServiceReference[] arrivals) {
        throw new UnsupportedOperationException("Dynamic dependency reconfiguration is not supported by service exporter");
    }

    public void pssIpojoLog(String msg, ServiceReference ref, Object obj) {
        m_logger.log(PSSLogger.ERROR, ref.getProperty("factory.name") + "whilst listening for :" + getTracker().getListenerFilter());
    }

    @Override
    public String describe() {
        StringBuffer description = new StringBuffer("\nService Exporter");
        description.append(super.describe());
        return description.toString();
    }

    @Override
    public void reJigProperties() {
    }
}
