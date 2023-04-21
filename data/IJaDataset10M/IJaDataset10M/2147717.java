package org.personalsmartspace.ipojo;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.felix.ipojo.metadata.Element;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.SynchronousBundleListener;
import org.osgi.service.cm.ConfigurationException;
import org.personalsmartspace.ipojo.parser.PSSManifestMetadataParser;
import org.personalsmartspace.ipojo.parser.PSSParseException;
import org.personalsmartspace.ipojo.parser.PSSParseUtils;
import org.personalsmartspace.ipojo.pssintegrator.api.pss2ipojo.IExternalPSS2Ipojo;
import org.personalsmartspace.ipojo.pssintegrator.api.pss2ipojo.IPss2IpojoEventinfo;
import org.personalsmartspace.ipojo.pssintegrator.api.pss2ipojo.PSSIPojoConstants;
import org.personalsmartspace.ipojo.pssintegrator.api.pss2ipojo.PSS_2_IPojoException;
import org.personalsmartspace.ipojo.util.PSSLogger;
import org.personalsmartspace.pss_sm_api.impl.ServiceMgmtException;
import org.personalsmartspace.sre.api.pss3p.IServiceIdentifier;

/**
 * @author pcheev1X
 * 
 */
public class PSSExtender implements SynchronousBundleListener, BundleActivator {

    /**
     * Enables the PSS iPOJO internal dispatcher. This internal dispatcher helps
     * the OSGi framework to support large scale applications. The internal
     * dispatcher is disabled by default.
     */
    static boolean DISPATCHER_ENABLED = true;

    /**
     * Property allowing to set if the internal dispatcher is enabled or
     * disabled. Possible value are either <code>true</code> or
     * <code>false</code>.
     */
    private static final String ENABLING_DISPATCHER = "ipojo.internal.dispatcher";

    /**
     * iPOJO Component Type and Instance declaration header.
     */
    private static final String IPOJO_HEADER = "iPOJO-Components";

    /**
     * iPOJO Extension declaration header.
     */
    private static final String IPOJO_EXTENSION = "IPOJO-Extension";

    /**
     * The iPOJO Extender logger.
     */
    private PSSLogger m_logger;

    /**
     * The Bundle Context of the iPOJO Core bundle.
     */
    private PSSSuperBundleContextImpl m_context;

    /**
     * The instance creator used to create instances. (Singleton)
     */
    private PSSInstanceCreator m_creator;

    /**
     * The iPOJO Bundle.
     */
    private Bundle m_bundle;

    /**
     * The list of factory types.
     */
    private List m_factoryTypes = new ArrayList();

    /**
     * The list of unbound types. A type is unbound if the matching extension is
     * not deployed.
     */
    private final List m_unboundTypes = new ArrayList();

    /**
     * The thread analyzing arriving bundles and creating iPOJO contributions.
     */
    private final CreatorThread m_thread = new CreatorThread();

    PSS2IPojoBundleEventListener listener;

    private PSSStaticServiceFinderImpl serviceFinder;

    /**
     * Bundle Listener Notification.
     * 
     * @param event
     *            the bundle event.
     * @see org.osgi.framework.BundleListener#bundleChanged(org.osgi.framework.BundleEvent)
     */
    public void bundleChanged(final BundleEvent event) {
        if (event.getBundle() == m_bundle) {
            return;
        }
        IExternalPSS2Ipojo external = null;
        try {
            external = serviceFinder.getExternalPSS2Ipojo();
        } catch (ServiceMgmtException e) {
            m_logger.log(PSSLogger.ERROR, "failed to get ExternalPSS2Ipojo() Service");
        }
        m_thread.externalPSS2IpojoService = external;
        switch(event.getType()) {
            case BundleEvent.STARTED:
                m_thread.addBundle(event.getBundle());
                break;
            case BundleEvent.STOPPING:
                m_thread.removeBundle(event.getBundle());
                closeManagementFor(event.getBundle());
                break;
            default:
                break;
        }
    }

    public static void reconfigureFactory(PSSIPojoFactory factory, Dictionary updateDict) {
        try {
            if (factory != null) {
                Set col = factory.m_componentInstances.keySet();
                Iterator it = col.iterator();
                String name;
                if (factory.m_componentDesc != null) {
                    Dictionary dict = factory.m_componentDesc.getPropertiesToPublish();
                    if (dict != null) {
                        if (it.hasNext()) {
                            while (it.hasNext()) {
                                PSSComponentInstance comp = (PSSComponentInstance) (factory.m_componentInstances.get(it.next()));
                                name = comp.getInstanceName();
                                updateDict.put("instance.name", name);
                                factory.updateReg(dict, updateDict);
                            }
                        } else {
                            factory.updateReg(dict, updateDict);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Ends the iPOJO Management for the given bundle. Generally the bundle is
     * leaving. This method stops every factories declared is the bundle and
     * disposed every declared instances.
     * 
     * @param bundle
     *            the bundle.
     */
    private void closeManagementFor(Bundle bundle) {
        List toRemove = new ArrayList();
        m_creator.removeInstancesFromBundle(bundle.getBundleId());
        for (int k = 0; k < m_factoryTypes.size(); k++) {
            ManagedAbstractFactoryType mft = (ManagedAbstractFactoryType) m_factoryTypes.get(k);
            if (mft.m_created != null) {
                List cfs = (List) mft.m_created.remove(bundle);
                for (int i = 0; cfs != null && i < cfs.size(); i++) {
                    PSSIPojoFactory factory = (PSSIPojoFactory) cfs.get(i);
                    m_creator.removeFactory(factory);
                    factory.stop();
                }
            }
            if (mft.m_bundle == bundle) {
                if (mft.m_created != null) {
                    Iterator iterator = mft.m_created.keySet().iterator();
                    while (iterator.hasNext()) {
                        Bundle key = (Bundle) iterator.next();
                        List list = (List) mft.m_created.get(key);
                        for (int i = 0; i < list.size(); i++) {
                            PSSIPojoFactory factory = (PSSIPojoFactory) list.get(i);
                            factory.stop();
                            m_unboundTypes.add(new UnboundComponentType(mft.m_type, factory.m_componentMetadata, factory.getBundleContext().getBundle()));
                        }
                    }
                }
                toRemove.add(mft);
            }
        }
        for (int i = 0; i < toRemove.size(); i++) {
            ManagedAbstractFactoryType mft = (ManagedAbstractFactoryType) toRemove.get(i);
            m_logger.log(PSSLogger.INFO, "The factory type: " + mft.m_type + " is no more available");
            mft.m_bundle = null;
            mft.m_clazz = null;
            mft.m_created = null;
            mft.m_type = null;
            m_factoryTypes.remove(mft);
        }
    }

    /**
     * Modifies the given bundle.
     * 
     * @param bundle
     *            the bundle.
     * @throws PSSMissingHandlerException
     * @throws PSSUnacceptableConfiguration
     */
    public void modifyManagementFor(Bundle bundle, Dictionary updateDict) {
        List toRemove = new ArrayList();
        Dictionary dict = null;
        for (int k = 0; k < m_factoryTypes.size(); k++) {
            ManagedAbstractFactoryType mft = (ManagedAbstractFactoryType) m_factoryTypes.get(k);
            if (mft.m_created != null) {
                List cfs = (List) mft.m_created.get(bundle);
                for (int i = 0; cfs != null && i < cfs.size(); i++) {
                    PSSIPojoFactory factory = (PSSIPojoFactory) cfs.get(i);
                    reconfigureFactory(factory, updateDict);
                }
            }
            if (mft.m_bundle == bundle) {
                if (mft.m_created != null) {
                    Iterator iterator = mft.m_created.keySet().iterator();
                    while (iterator.hasNext()) {
                        Bundle key = (Bundle) iterator.next();
                        List list = (List) mft.m_created.get(key);
                        for (int i = 0; i < list.size(); i++) {
                            PSSIPojoFactory factory = (PSSIPojoFactory) list.get(i);
                            reconfigureFactory(factory, updateDict);
                        }
                    }
                }
            }
        }
    }

    /**
     * Describes the iPOJO Management for the given bundle. This method
     * describes every factories declared is the bundle and describes every
     * declared instances.
     * 
     * @param bundle
     *            the bundle.
     */
    private String describeManagementFor(Bundle bundle) {
        StringBuffer description = new StringBuffer("\nDescribe the bundle  :\n");
        List toDescribe = new ArrayList();
        description.append(m_creator.describeInstancesFromBundle(bundle.getBundleId()));
        for (int k = 0; k < m_factoryTypes.size(); k++) {
            ManagedAbstractFactoryType mft = (ManagedAbstractFactoryType) m_factoryTypes.get(k);
            if (mft.m_created != null) {
                List cfs = (List) mft.m_created.get(bundle);
                for (int i = 0; cfs != null && i < cfs.size(); i++) {
                    PSSIPojoFactory factory = (PSSIPojoFactory) cfs.get(i);
                    description.append(m_creator.describeFactory(bundle.getBundleId(), factory));
                }
            }
            if (mft.m_bundle == bundle) {
                if (mft.m_created != null) {
                    Iterator iterator = mft.m_created.keySet().iterator();
                    while (iterator.hasNext()) {
                        Bundle key = (Bundle) iterator.next();
                        List list = (List) mft.m_created.get(key);
                        for (int i = 0; i < list.size(); i++) {
                            PSSIPojoFactory factory = (PSSIPojoFactory) list.get(i);
                            factory.describe();
                        }
                    }
                }
                toDescribe.add(mft);
            }
        }
        for (int i = 0; i < toDescribe.size(); i++) {
            ManagedAbstractFactoryType mft = (ManagedAbstractFactoryType) toDescribe.get(i);
            description.append("The factory type: " + mft.m_type);
            description.append("mft.m_clazz");
            description.append("type " + mft.m_type);
            ;
        }
        return description.toString();
    }

    /**
     * Checks if the given bundle is an iPOJO bundle, and begin the iPOJO
     * management is true.
     * 
     * @param bundle
     *            the bundle to check.
     */
    private void startManagementFor(Bundle bundle) {
        Dictionary dict = bundle.getHeaders();
        String typeHeader = (String) dict.get(IPOJO_EXTENSION);
        if (typeHeader != null) {
            parseAbstractFactoryType(bundle, typeHeader);
        }
        String header = (String) dict.get(IPOJO_HEADER);
        if (header != null) {
            try {
                parse(bundle, header);
            } catch (IOException e) {
                m_logger.log(PSSLogger.ERROR, "An exception occurs during the parsing of the bundle " + bundle.getBundleId(), e);
            } catch (PSSParseException e) {
                m_logger.log(PSSLogger.ERROR, "A parse exception occurs during the parsing of the bundle " + bundle.getBundleId(), e);
            }
        }
    }

    /**
     * Parses an IPOJO-Extension manifest header and then creates iPOJO
     * extensions (factory types).
     * 
     * @param bundle
     *            the bundle containing the header.
     * @param header
     *            the header to parse.
     */
    private void parseAbstractFactoryType(Bundle bundle, String header) {
        String[] arr = PSSParseUtils.split(header, ",");
        for (int i = 0; arr != null && i < arr.length; i++) {
            String[] arr2 = PSSParseUtils.split(arr[i], ":");
            String type = arr2[0];
            Class clazz;
            try {
                clazz = bundle.loadClass(arr2[1]);
            } catch (ClassNotFoundException e) {
                m_logger.log(PSSLogger.ERROR, "Cannot load the extension " + type, e);
                return;
            }
            ManagedAbstractFactoryType mft = new ManagedAbstractFactoryType(clazz, type, bundle);
            m_factoryTypes.add(mft);
            m_logger.log(PSSLogger.DEBUG, "New factory type available: " + type);
            for (int j = m_unboundTypes.size() - 1; j >= 0; j--) {
                UnboundComponentType unbound = (UnboundComponentType) m_unboundTypes.get(j);
                if (unbound.m_type.equals(type)) {
                    createAbstractFactory(unbound.m_bundle, unbound.m_description);
                    m_unboundTypes.remove(unbound);
                }
            }
        }
    }

    /**
     * Parses the internal metadata (from the manifest (in the iPOJO-Components
     * property)). This methods creates factories and add instances to the
     * instance creator.
     * 
     * @param bundle
     *            the owner bundle.
     * @param components
     *            The iPOJO Header String.
     * @throws IOException
     *             if the manifest can not be found
     * @throws PSSParseException
     *             if the parsing process failed
     */
    private void parse(Bundle bundle, String components) throws IOException, PSSParseException {
        PSSManifestMetadataParser parser = new PSSManifestMetadataParser();
        parser.parseHeader(components);
        Element[] metadata = parser.getComponentsMetadata();
        for (int i = 0; i < metadata.length; i++) {
            createAbstractFactory(bundle, metadata[i]);
        }
        Dictionary[] instances = parser.getInstances();
        for (int i = 0; instances != null && i < instances.length; i++) {
            m_creator.addInstance(instances[i], bundle.getBundleId());
        }
    }

    /**
     * iPOJO Start method.
     * 
     * @param context
     *            the iPOJO bundle context.
     * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
     */
    public void start(BundleContext theContext) {
        m_logger = new PSSLogger(theContext, "PSS IPOJO-Extender");
        m_context = new PSSSuperBundleContextImpl(theContext, m_logger);
        m_bundle = new PSSSuperBundleImpl(m_context.getBundle(), m_logger);
        m_creator = new PSSInstanceCreator(m_context);
        setUpPSS2Ipojo(theContext);
        enablingDispatcher(m_context, m_logger);
        if (DISPATCHER_ENABLED) {
            PSSEventDispatcher.create(m_context);
        }
        startManagementFor(m_bundle);
        new Thread(m_thread, "PSS Extender").start();
        synchronized (this) {
            m_context.addBundleListener(this);
            for (int i = 0; i < m_context.getBundles().length; i++) {
                if (m_context.getBundles()[i].getState() == Bundle.ACTIVE) {
                    m_thread.addBundle(m_context.getBundles()[i]);
                }
            }
        }
        m_logger.log(PSSLogger.INFO, "PSS iPOJO Runtime started");
    }

    /**
     * SetUp Trackers of Persist Framework
     * 
     * @param context
     *            The OSGI Context
     */
    protected void setUpPSS2Ipojo(BundleContext context) {
        this.serviceFinder = PSSStaticServiceFinderImpl.getInstance();
        this.serviceFinder.initialise(context);
        this.serviceFinder.openServiceFinders();
        registerListener(context);
    }

    protected void cleanUpPSS2Ipojo() {
        this.serviceFinder.closeServiceFinders();
        this.serviceFinder = null;
    }

    /**
     * Stops the iPOJO Bundle.
     * 
     * @param context
     *            the bundle context.
     * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
     */
    public void stop(BundleContext context) {
        m_thread.stop();
        m_context.removeBundleListener(this);
        if (DISPATCHER_ENABLED) {
            PSSEventDispatcher.dispose();
        }
        for (int k = 0; k < m_factoryTypes.size(); k++) {
            ManagedAbstractFactoryType mft = (ManagedAbstractFactoryType) m_factoryTypes.get(k);
            if (mft.m_created != null) {
                Iterator iterator = mft.m_created.keySet().iterator();
                while (iterator.hasNext()) {
                    Bundle key = (Bundle) iterator.next();
                    List list = (List) mft.m_created.get(key);
                    for (int i = 0; i < list.size(); i++) {
                        PSSIPojoFactory factory = (PSSIPojoFactory) list.get(i);
                        m_creator.removeFactory(factory);
                        factory.dispose();
                    }
                }
            }
        }
        m_factoryTypes = null;
        m_creator = null;
        cleanUpPSS2Ipojo();
        m_logger.log(PSSLogger.INFO, "PSS iPOJO Runtime stopped");
    }

    /**
     * Enables or disables the internal dispatcher, so sets the
     * {@link Extender#DISPATCHER_ENABLED} flag. This method checks if the
     * {@link Extender#ENABLING_DISPATCHER} property is set to <code>true</code>
     * . Otherwise, the internal dispatcher is disabled. The property can be set
     * as a system property (<code>ipojo.internal.dispatcher</code>) or inside
     * the iPOJO bundle manifest (<code>ipojo-internal-dispatcher</code>).
     * 
     * @param context
     *            the bundle context.
     * @param logger
     *            the logger to indicates if the internal dispatcher is set.
     */
    private static void enablingDispatcher(BundleContext context, PSSLogger logger) {
        String flag = context.getProperty(ENABLING_DISPATCHER);
        if (flag == null) {
            String key = ENABLING_DISPATCHER.replace('.', '-');
            flag = (String) context.getBundle().getHeaders().get(key);
        }
        if (flag != null) {
            if (flag.equalsIgnoreCase("true")) {
                PSSExtender.DISPATCHER_ENABLED = true;
                logger.log(PSSLogger.INFO, "iPOJO Internal Event Dispatcher enables");
                return;
            }
        }
        PSSExtender.DISPATCHER_ENABLED = false;
        logger.log(PSSLogger.INFO, "iPOJO Internal Event Dispatcher disables");
    }

    /**
     * Adds a component factory to the factory list.
     * 
     * @param metadata
     *            the new component metadata.
     * @param bundle
     *            the bundle.
     */
    private void createAbstractFactory(Bundle bundle, Element metadata) {
        ManagedAbstractFactoryType factoryType = null;
        for (int i = 0; i < m_factoryTypes.size(); i++) {
            ManagedAbstractFactoryType type = (ManagedAbstractFactoryType) m_factoryTypes.get(i);
            if (type.m_type.equals(metadata.getName())) {
                factoryType = type;
                break;
            }
        }
        if (factoryType == null) {
            m_logger.log(PSSLogger.WARNING, "Type of component not available: " + metadata.getName());
            m_unboundTypes.add(new UnboundComponentType(metadata.getName(), metadata, bundle));
            return;
        }
        Class clazz = factoryType.m_clazz;
        try {
            Constructor cst = clazz.getConstructor(new Class[] { BundleContext.class, Element.class });
            PSSIPojoFactory factory = (PSSIPojoFactory) cst.newInstance(new Object[] { getBundleContext(bundle), metadata });
            if (factoryType.m_created == null) {
                factoryType.m_created = new HashMap();
                List list = new ArrayList();
                list.add(factory);
                factoryType.m_created.put(bundle, list);
            } else {
                List list = (List) factoryType.m_created.get(bundle);
                if (list == null) {
                    list = new ArrayList();
                    list.add(factory);
                    factoryType.m_created.put(bundle, list);
                } else {
                    list.add(factory);
                }
            }
            factory.start();
            m_creator.addFactory(factory);
        } catch (SecurityException e) {
            m_logger.log(PSSLogger.ERROR, "Cannot instantiate an abstract factory from " + clazz.getName(), e);
        } catch (NoSuchMethodException e) {
            m_logger.log(PSSLogger.ERROR, "Cannot instantiate an abstract factory from " + clazz.getName() + ": the given class constructor cannot be found");
        } catch (IllegalArgumentException e) {
            m_logger.log(PSSLogger.ERROR, "Cannot instantiate an abstract factory from " + clazz.getName(), e);
        } catch (InstantiationException e) {
            m_logger.log(PSSLogger.ERROR, "Cannot instantiate an abstract factory from " + clazz.getName(), e);
        } catch (IllegalAccessException e) {
            m_logger.log(PSSLogger.ERROR, "Cannot instantiate an abstract factory from " + clazz.getName(), e);
        } catch (InvocationTargetException e) {
            m_logger.log(PSSLogger.ERROR, "Cannot instantiate an abstract factory from " + clazz.getName(), e.getTargetException());
        }
    }

    /**
     * Structure storing an iPOJO extension.
     */
    private static final class ManagedAbstractFactoryType {

        /**
         * The type (i.e.) name of the extension.
         */
        String m_type;

        /**
         * The abstract Factory class.
         */
        Class m_clazz;

        /**
         * The bundle object containing the declaration of the extension.
         */
        Bundle m_bundle;

        /**
         * The factories created by this extension.
         */
        private Map m_created;

        /**
         * Creates a ManagedAbstractFactoryType.
         * 
         * @param factory
         *            the abstract factory class.
         * @param type
         *            the name of the extension.
         * @param bundle
         *            the bundle declaring the extension.
         */
        protected ManagedAbstractFactoryType(Class factory, String type, Bundle bundle) {
            m_bundle = bundle;
            m_clazz = factory;
            m_type = type;
        }
    }

    /**
     * Structure storing unbound component type declarations. Unbound means that
     * there is no extension able to manage the extension.
     */
    private static final class UnboundComponentType {

        /**
         * The component type description.
         */
        private final Element m_description;

        /**
         * The bundle declaring this type.
         */
        private final Bundle m_bundle;

        /**
         * The required extension name.
         */
        private final String m_type;

        /**
         * Creates a UnboundComponentType.
         * 
         * @param description
         *            the description of the component type.
         * @param bundle
         *            the bundle declaring this type.
         * @param type
         *            the required extension name.
         */
        protected UnboundComponentType(String type, Element description, Bundle bundle) {
            m_type = type;
            m_description = description;
            m_bundle = bundle;
        }
    }

    /**
     * Computes the bundle context from the bundle class by introspection.
     * 
     * @param bundle
     *            the bundle.
     * @return the bundle context object or <code>null</code> if not found.
     */
    public BundleContext getBundleContext(Bundle bundle) {
        PSSSuperBundleContextImpl bundlecontext = null;
        ;
        if (bundle == null) {
            return null;
        }
        Method meth = null;
        try {
            meth = bundle.getClass().getMethod("getBundleContext", new Class[0]);
        } catch (SecurityException e) {
        } catch (NoSuchMethodException e) {
        }
        if (meth == null) {
            try {
                meth = bundle.getClass().getMethod("getContext", new Class[0]);
            } catch (SecurityException e) {
            } catch (NoSuchMethodException e) {
            }
        }
        if (meth != null) {
            if (!meth.isAccessible()) {
                meth.setAccessible(true);
            }
            try {
                BundleContext context = (BundleContext) meth.invoke(bundle, new Object[0]);
                return new PSSSuperBundleContextImpl(context, m_logger);
            } catch (IllegalArgumentException e) {
                m_logger.log(PSSLogger.ERROR, "Cannot get the BundleContext by invoking " + meth.getName(), e);
                return null;
            } catch (IllegalAccessException e) {
                m_logger.log(PSSLogger.ERROR, "Cannot get the BundleContext by invoking " + meth.getName(), e);
                return null;
            } catch (InvocationTargetException e) {
                m_logger.log(PSSLogger.ERROR, "Cannot get the BundleContext by invoking " + meth.getName(), e.getTargetException());
                return null;
            }
        }
        Field[] fields = bundle.getClass().getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            if (BundleContext.class.isAssignableFrom(fields[i].getType())) {
                if (!fields[i].isAccessible()) {
                    fields[i].setAccessible(true);
                }
                try {
                    BundleContext context = (BundleContext) fields[i].get(bundle);
                    return new PSSSuperBundleContextImpl(context, m_logger);
                } catch (IllegalArgumentException e) {
                    m_logger.log(PSSLogger.ERROR, "Cannot get the BundleContext by invoking " + fields[i].getName(), e);
                    return null;
                } catch (IllegalAccessException e) {
                    m_logger.log(PSSLogger.ERROR, "Cannot get the BundleContext by invoking " + fields[i].getName(), e);
                    return null;
                }
            }
        }
        m_logger.log(PSSLogger.ERROR, "Cannot find the BundleContext for " + bundle.getSymbolicName(), null);
        return null;
    }

    /**
     * The creator thread analyzes arriving bundles to create iPOJO
     * contribution.
     */
    private class CreatorThread implements Runnable {

        /**
         * Is the creator thread started?
         */
        private boolean m_started = true;

        /**
         * Is the externalPSS2IpojoService started?
         */
        public IExternalPSS2Ipojo externalPSS2IpojoService = null;

        /**
         * The list of bundle that are going to be analyzed.
         */
        private List m_bundles = new ArrayList();

        /**
         * A bundle is arriving. This method is synchronized to avoid concurrent
         * modification of the waiting list.
         * 
         * @param bundle
         *            the new bundle
         */
        public synchronized void addBundle(Bundle bundle) {
            m_bundles.add(bundle);
            notifyAll();
            m_logger.log(PSSLogger.DEBUG, "Creator thread is going to analyze the bundle " + bundle.getBundleId() + " List : " + m_bundles);
        }

        /**
         * A bundle is leaving. If the bundle was not already processed, the
         * bundle is remove from the waiting list. This method is synchronized
         * to avoid concurrent modification of the waiting list.
         * 
         * @param bundle
         *            the leaving bundle.
         */
        public synchronized void removeBundle(Bundle bundle) {
            m_bundles.remove(bundle);
        }

        /**
         * Stops the creator thread.
         */
        public synchronized void stop() {
            m_started = false;
            m_bundles.clear();
            notifyAll();
        }

        /**
         * Creator thread's run method. While the list is not empty, the thread
         * launches the bundle analyzing on the next bundle. When the list is
         * empty, the thread sleeps until the arrival of a new bundle or until
         * iPOJO stops.
         * 
         * @see java.lang.Runnable#run()
         */
        public void run() {
            m_logger.log(PSSLogger.DEBUG, "Creator thread is starting");
            boolean started;
            synchronized (this) {
                started = m_started;
            }
            while (started) {
                Bundle bundle;
                synchronized (this) {
                    while (m_started && (m_bundles.isEmpty() || externalPSS2IpojoService == null)) {
                        try {
                            m_logger.log(PSSLogger.DEBUG, "Creator thread is waiting - Nothing to do");
                            wait();
                        } catch (InterruptedException e) {
                        }
                    }
                    if (!m_started) {
                        m_logger.log(PSSLogger.DEBUG, "Creator thread is stopping");
                        return;
                    } else {
                        bundle = (Bundle) m_bundles.remove(0);
                    }
                }
                m_logger.log(PSSLogger.DEBUG, "Creator thread is processing " + bundle.getBundleId());
                try {
                    startManagementFor(bundle);
                } catch (Throwable e) {
                    m_logger.log(PSSLogger.ERROR, "An error occurs when analyzing the content or starting the management of " + bundle.getBundleId(), e);
                }
                synchronized (this) {
                    started = m_started;
                }
            }
        }
    }

    public void modifyAffectedBundle(String eventType, IPss2IpojoEventinfo eventinfo) {
        try {
            IServiceIdentifier serviceId = eventinfo.getServiceID();
            m_logger.log(PSSLogger.DEBUG, "modifyAffectedBundle service  : " + serviceId.getLocalServiceId());
            Bundle bundle = null;
            bundle = this.m_thread.externalPSS2IpojoService.getBundleFromServiceIdentifier(serviceId);
            ServiceReference ref = this.m_thread.externalPSS2IpojoService.getReferenceFromIdentifier(serviceId);
            if (bundle == m_bundle) {
                return;
            }
            if (PSSIPojoConstants.PSS_TO_IPOJO_MODIFIED_SERVICE_EVENTYTPE.compareTo(eventType) == 0) {
                Dictionary updateDict = eventinfo.getProperties();
                this.modifyManagementFor(bundle, updateDict);
            }
            if (PSSIPojoConstants.PSS_TO_IPOJO_ADDBUNDLE_EVENTTYPE.compareTo(eventType) == 0) {
                m_thread.addBundle(bundle);
            } else if (PSSIPojoConstants.PSS_TO_IPOJO_REMOVEBUNDLE_EVENTTYPE.compareTo(eventType) == 0) {
                m_thread.removeBundle(bundle);
                closeManagementFor(bundle);
            }
        } catch (PSS_2_IPojoException e) {
            m_logger.log(PSSLogger.ERROR, "modifyAffectedBundle Failed to get Bundle id for Service : ");
            ;
        }
    }

    /**
     * @param context
     * @see org.personalsmartspace.pss_sm_synchroniser.impl.LocalServices
     */
    private void registerListener(BundleContext context) {
        listener = new PSS2IPojoBundleEventListener(context, this);
        try {
            this.serviceFinder.getEventMgr().registerListener(listener, listener.getEventTypes(), null);
        } catch (ServiceMgmtException e) {
            m_logger.log(PSSLogger.ERROR, "Failed to register PSS2IPojoBundleEventListener Listener with event manager");
            ;
        }
    }
}
