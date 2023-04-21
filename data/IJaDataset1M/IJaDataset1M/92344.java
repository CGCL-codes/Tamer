package net.assimilator.core;

import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;

/**
 * The ServiceElement object provides context on how to provision and instantiate 
 * services in the architecture. 
 */
public class ServiceElement implements Serializable {

    static final long serialVersionUID = 1L;

    /**
     * The EXTERNAL type indicates that the ServiceElement is not a provisionable
     * component and must be instantiated using external mechanisms
     */
    public static final int EXTERNAL = 0;

    /**
     * The DYNAMIC type indicates that the ServiceElement will be provisioned to
     * available ServiceBeanInstantiator instances that support the service's
     * operational criteria up to the amount specified by the number of planned
     * instances.
     */
    public static final int DYNAMIC = 1;

    /**
     * The FIXED type indicates that the ServiceElement will be provisioned to
     * <i>every</i> ServiceBeanInstantiator instance that supports the ServiceBean
     * operational criteria
     */
    public static final int FIXED = 2;

    /** Provision type, default to DYNAMIC */
    private int provisionType = ServiceElement.DYNAMIC;

    /** The ServiceBeanConfig, providing configuration information for the 
     * ServiceBean */
    private ServiceBeanConfig sbConfig;

    /** Array of AssociationDescriptor instances, describing associations the 
     * ServiceElement has to other services */
    private AssociationDescriptor[] associations;

    /** The ClassBundle for the ServiceBean */
    private ClassBundle componentBundle;

    /** Array of ClassBundles containing the export codebase */
    private ClassBundle[] exportBundles;

    /** Collection of provisionable PlatformCapability objects  */
    private Collection provisionableCapabilities = new ArrayList();

    /** The ServiceLevelAgreements object defines system and service level 
     * objectives that are to be monitored, metered and acted on by policy 
     * handlers */
    private ServiceLevelAgreements slAgreements;

    /** Use the name in addition to public interfaces to track the service */
    private boolean matchOnName = false;

    /** Whether to automatically advertise the service as part of service
     * instantiation */
    private boolean autoAdvertise = true;

    /**
     * Whether to acquire the DiscoveryManagement for the service from a shared
     * pool of DiscoveryManagement instances
     */
    private boolean discoPool = true;

    /** Number of planned instances */
    private int planned;

    /** Maximum number of services per container */
    private int MaxPerContainer;

    /** Number of actual instances */
    private int actual;

    /** Array of machines that have been identified as part of a cluster of
     * machine used as targets for provisioning */
    private String[] machineCluster;

    /** FaultDetectionHandler ClassBundle */
    private ClassBundle fdhBundle;

    /**
     * Construct a ServiceElement
     * 
     * @param provisionType EXTERNAL, DYNAMIC or FIXED
     * @param sbConfig The ServiceBeanConfig to set
     * @param slAgreements Attributes relating to SLAs
     * @param exports ClassBundle[] of JARs to use as the export codebase  
     * @param fdhBundle ClassBundle for the FaultDetectionHandler   
     */
    public ServiceElement(int provisionType, ServiceBeanConfig sbConfig, ServiceLevelAgreements slAgreements, ClassBundle[] exports, ClassBundle fdhBundle) {
        this(provisionType, sbConfig, slAgreements, exports, fdhBundle, null);
    }

    /**
     * Construct a ServiceElement
     * 
     * @param provisionType EXTERNAL, DYNAMIC or FIXED
     * @param sbConfig The ServiceBeanConfig to set
     * @param slAgreements Attributes relating to SLAs
     * @param exports ClassBundle[] of JARs to use as the export codebase
     * @param fdhBundle ClassBundle for the FaultDetectionHandler
     * @param componentBundle The ClassBundle for the ServiceBean component
     */
    public ServiceElement(int provisionType, ServiceBeanConfig sbConfig, ServiceLevelAgreements slAgreements, ClassBundle[] exports, ClassBundle fdhBundle, ClassBundle componentBundle) {
        if (provisionType < EXTERNAL && provisionType > FIXED) throw new IllegalArgumentException("unknown provisionType : " + provisionType);
        if (sbConfig == null) throw new NullPointerException("sbConfig is null");
        if (slAgreements == null) throw new NullPointerException("slAgreements is null");
        if (exports == null) throw new NullPointerException("exports is null");
        if (fdhBundle == null) throw new NullPointerException("fdhBundle is null");
        this.fdhBundle = fdhBundle;
        this.provisionType = provisionType;
        exportBundles = new ClassBundle[exports.length];
        System.arraycopy(exports, 0, exportBundles, 0, exports.length);
        this.componentBundle = componentBundle;
        setServiceBeanConfig(sbConfig);
        this.slAgreements = slAgreements;
    }

    /**
     * Get the provision type set for this service. The provision type will
     * either be EXTERNAL, DYNAMIC or FIXED
     */
    public int getProvisionType() {
        return (provisionType);
    }

    /**
     * Get the name of the ServiceElement
     * 
     * @return The name of the ServiceElement
     */
    public String getName() {
        return (sbConfig.getName());
    }

    /**
     * Set the number of instances of this service that should exist on the
     * network.
     * 
     * @param planned The number of planned instances
     */
    public void setPlanned(int planned) {
        this.planned = planned;
    }

    /**
     * Increment the number of instances of this service that should exist on
     * the network by one
     */
    public void incrementPlanned() {
        planned++;
    }

    /**
     * Decrement the number of instances of this service that should exist on
     * the network by one. If the planned attribute is zero, this operational
     * does nothing
     */
    public void decrementPlanned() {
        if (planned > 0) planned--;
    }

    /**
     * Get the number of instances of this service that should exist on the
     * network.
     * 
     * @return int The number of instances of this service that should exist on
     * the network
     */
    public int getPlanned() {
        return (planned);
    }

    /**
     * Set the actual
     * 
     * @param actual The amount of services discovered
     */
    public void setActual(int actual) {
        this.actual = actual;
    }

    /**
     * Get the actual amount of services discovered
     */
    public int getActual() {
        return (actual);
    }

    /**
     * Set the maximum number of instances of this service that should exist on
     * any given machine
     * 
     * @param MaxPerContainer The maximum number of service that should exist on
     * any given machine
     */
    public void setMaxPerContainer(int MaxPerContainer) {
        this.MaxPerContainer = MaxPerContainer;
    }

    /**
     * Get the maximum number of instances of this service that should exist on
     * any given container
     * 
     * @return The maximum number of service that should exist on any
     * given container. If the MaxPerContainer value has not been set, the value
     * will be -1
     */
    public int getMaxPerContainer() {
        return (MaxPerContainer);
    }

    /**
     * Set the machineCluster property
     * 
     * @param machineCluster The cluster of targeted machine(s) to provision
     * the ServiceBean to. Array elements will be either the hostname or an IP
     * Address of a machine.
     */
    public void setCluster(String[] machineCluster) {
        this.machineCluster = machineCluster;
    }

    /**
     * Get the cluster of targeted machine(s) to provision the ServiceBean to.
     * Array elements will be either the hostname or an IP Address of a machine.
     * 
     * @return An array of machines to provision the ServiceBean to
     */
    public String[] getCluster() {
        if (machineCluster == null) return (new String[0]);
        String[] cluster = new String[machineCluster.length];
        System.arraycopy(machineCluster, 0, cluster, 0, machineCluster.length);
        return (cluster);
    }

    /**
     * Get the FaultDetectionHandler ClassBundle
     * 
     * @return The ClassBundle providing attributes on the
     * FaultDetectionHandler to use
     */
    public ClassBundle getFaultDetectionHandlerBundle() {
        return (fdhBundle);
    }

    /**
     * This method sets the instance of <code>ServiceBeanConfig</code> as 
     * an attribute of the <code>ServiceElement</code> object.
     *
     * @param sbConfig The ServiceBeanConfig to set
     */
    public void setServiceBeanConfig(ServiceBeanConfig sbConfig) {
        if (sbConfig == null) throw new NullPointerException("sbConfig is null");
        this.sbConfig = sbConfig;
    }

    /**
     * This method returns an instance of the <code>ServiceBeanConfig</code> 
     * object created for this service as part of this Operational String<br>
     *
     * @return The ServiceBeanConfig object for this ServiceElement
     */
    public ServiceBeanConfig getServiceBeanConfig() {
        return (sbConfig);
    }

    /**
     * Get the name of the OperationalString
     * 
     * @return Name of the OperationalString
     */
    public String getOperationalStringName() {
        return (sbConfig.getOperationalStringName());
    }

    /**
     * Set whether to automatically advertise the service as part of service
     * instantiation
     * 
     * @param autoAdvertise If true, automatically advertise the ServiceBean when
     * instantiated
     */
    public void setAutoAdvertise(boolean autoAdvertise) {
        this.autoAdvertise = autoAdvertise;
    }

    /**
     * Get whether to automatically advertise the service as part of service
     * instantiation
     * 
     * @return If true, automatically advertise the ServiceBean when
     * instantiated
     */
    public boolean getAutoAdvertise() {
        return (autoAdvertise);
    }

    /**
     * Set whether to acquire the DiscoveryManagement for the service from a shared
     * pool of DiscoveryManagement instances. Sharing DiscoveryManagement instances
     * results in optimizing the creation of DiscoveryManagement instances
     *  
     * Note: Use of shared (pooled) DiscoveryManagement instances must be used with 
     * care as changing the settings of the returned DiscoveryManagement instance may 
     * present problems for other users of DiscoveryManagement instance and is not 
     * advised.
     * 
     * @param discoPool If true, get the DiscoveryManagement instance for the 
     * service from a pool of available DiscoveryManagement instances
     */
    public void setDiscoveryManagementPooling(boolean discoPool) {
        this.discoPool = discoPool;
    }

    /**
     * Get whether to acquire the DiscoveryManagement for the service from a shared
     * pool of DiscoveryManagement instances. Sharing DiscoveryManagement instances
     * results in optimizing the creation of DiscoveryManagement instances
     * 
     * Note: Use of shared (pooled) DiscoveryManagement instances must be used with 
     * care as changing the settings of the returned DiscoveryManagement instance may 
     * present problems for other users of DiscoveryManagement instance and is not 
     * advised.
     * 
     * @return If true, get the DiscoveryManagement instance for the service from a 
     * pool of available DiscoveryManagement instances
     */
    public boolean getDiscoveryManagementPooling() {
        return (discoPool);
    }

    /**
     * Set whether to use the name of the service is used in addition to the 
     * interfaces implemented by the service or service proxy to track service 
     * instances. 
     * 
     * @param matchOnName If true to use the name returned by the 
     * <code>getName</code> method in addition to the interfaces implemented by 
     * the service or service proxy to track service instances.
     */
    public void setMatchOnName(boolean matchOnName) {
        this.matchOnName = matchOnName;
    }

    /**
     * If this method returns true then the name of the service is used in
     * addition to the interfaces implemented by the service or service proxy to
     * track service instances. If this method returns false, then only the
     * interfaces will be used.
     * 
     * @return boolean true to use the name returned by the <code>getName</code>
     * method
     */
    public boolean getMatchOnName() {
        return (matchOnName);
    }

    /**
     * Set the component ClassBundle
     * 
     * @param componentBundle The ClassBundle identifying the class and required 
     * resources to load the ServiceBean
     */
    public void setComponentBundle(ClassBundle componentBundle) {
        this.componentBundle = componentBundle;
    }

    /**
     * Get the component ClassBundle
     * 
     * @return The ClassBundle identifying the class and required resources to
     * load the ServiceBean
     */
    public ClassBundle getComponentBundle() {
        return (componentBundle);
    }

    /**
     * Get the the Array of ClassBundle objects for the public interfaces the
     * service implements
     * 
     * @return Array of ClassBundle objects for the public interfaces the 
     * service implements
     */
    public ClassBundle[] getExportBundles() {
        ClassBundle[] bundle = new ClassBundle[exportBundles.length];
        System.arraycopy(exportBundles, 0, bundle, 0, exportBundles.length);
        return (bundle);
    }

    /**
     * Get the export URLs. Helper method to get the array of URLs from the
     * array of ClassBundle objcts representing the export class(es) and searchpaths
     * for those classes
     * 
     * @return Array of URLs of the export classes. A new array is allocate each
     * time this method is called
     */
    public URL[] getExportURLs() {
        ArrayList list = new ArrayList();
        for (int i = 0; i < exportBundles.length; i++) {
            URL[] urls = exportBundles[i].getJARs();
            for (int j = 0; j < urls.length; j++) {
                list.add(urls[j]);
            }
        }
        return ((URL[]) list.toArray(new URL[list.size()]));
    }

    /**
     * Returns the ServiceLevelAgreements object.
     * 
     * @return The ServiceLevelAgreements object defines system and service level 
     * objectives that are to be monitored, metered and acted on by policy handlers
     */
    public ServiceLevelAgreements getServiceLevelAgreements() {
        return (slAgreements);
    }

    /**
     * Set the associations for the service
     * 
     * @param assocDescs An Array of AssociationDescriptor objects
     */
    public void setAssociationDescriptors(AssociationDescriptor[] assocDescs) {
        if (assocDescs != null) {
            this.associations = new AssociationDescriptor[assocDescs.length];
            System.arraycopy(assocDescs, 0, associations, 0, associations.length);
        }
    }

    /**
     * Get the associations for the service
     * 
     * @return Array of AssociationDescriptor objects. If there are no 
     * AssociationDescriptor objects, this method returns a zero-length array
     */
    public AssociationDescriptor[] getAssociationDescriptors() {
        if (associations == null) return (new AssociationDescriptor[0]);
        AssociationDescriptor[] aDescs = new AssociationDescriptor[associations.length];
        System.arraycopy(associations, 0, aDescs, 0, associations.length);
        return (aDescs);
    }

    /**
     * Set the SystemRequirement downloads for a resource.
     * 
     * @param downloadableCapabilities The downloadableCapabilities
     */
    public void setProvisionablePlatformCapabilities(Collection downloadableCapabilities) {
        synchronized (provisionableCapabilities) {
            provisionableCapabilities.clear();
            provisionableCapabilities.addAll(downloadableCapabilities);
        }
    }

    /**
     * Get the provisionable SystemRequirement instances
     * 
     * @return A Collection of SystemRequirement instances which
     * need to be provisioned in order for the ServiceBean to be instantiated. A
     * new Collection is created each time this method is called. If there are
     * no provisionable SystemRequirement instances, this method returns an
     * empty Collection.
     */
    public Collection getProvisionablePlatformCapabilities() {
        Collection collection = new ArrayList();
        synchronized (provisionableCapabilities) {
            collection.addAll(provisionableCapabilities);
        }
        return (collection);
    }

    /**
     * Recompute message digests for contained ClassBundle instances
     */
    public void recomputeMessageDigests() throws Exception {
        if (componentBundle != null) componentBundle.recomputeMessageDigests();
        for (int i = 0; i < exportBundles.length; i++) exportBundles[i].recomputeMessageDigests();
    }

    /**
     * Override hashCode
     */
    public int hashCode() {
        int hc = 17;
        hc = 37 * hc + getName().hashCode();
        hc = 37 * hc + getOperationalStringName().hashCode();
        for (int i = 0; i < exportBundles.length; i++) hc = 37 * hc + exportBundles[i].hashCode();
        hc = 37 * hc + (componentBundle == null ? 0 : componentBundle.hashCode());
        return (hc);
    }

    /**
     * Override equals
     */
    public boolean equals(Object obj) {
        if (this == obj) return (true);
        if (!(obj instanceof ServiceElement)) {
            return (false);
        }
        ServiceElement that = (ServiceElement) obj;
        if (this.getName().equals(that.getName()) && this.getOperationalStringName().equals(that.getOperationalStringName())) {
            if (this.exportBundles.length != that.exportBundles.length) {
                return (false);
            }
            for (int i = 0; i < this.exportBundles.length; i++) {
                boolean matched = false;
                for (int j = 0; j < that.exportBundles.length; j++) {
                    if (this.exportBundles[i].equals(that.exportBundles[j])) matched = true;
                }
                if (!matched) {
                    return (false);
                }
            }
            if (this.componentBundle == null && that.componentBundle == null) return (true);
            if (this.componentBundle != null && that.componentBundle != null) {
                return (this.componentBundle.equals(that.componentBundle));
            }
        }
        return (false);
    }

    public static void main(String[] args) {
        try {
            ServiceBeanConfig sbc = new ServiceBeanConfig(new java.util.HashMap(), new String[] { "-" });
            ClassBundle export1 = new ClassBundle("net.assimilator.resources.servicecore.Service");
            export1.addJAR(new URL("http://10.1.1.3:9000/assimilator-dl.jar"));
            export1.addJAR(new URL("http://10.1.1.3:9000/service-dl.jar"));
            ClassBundle impl = new ClassBundle("com.foo.ExampleImpl");
            impl.addJAR(new URL("http://10.1.1.3:9000/assimilator.jar"));
            impl.addJAR(new URL("http://10.1.1.3:9000/service.jar"));
            String fdh = "net.assimilator.resources.client.StandardFaultDetectionHandler";
            ServiceElement s1 = new ServiceElement(ServiceElement.DYNAMIC, sbc, new ServiceLevelAgreements(), new ClassBundle[] { export1 }, new ClassBundle(fdh), impl);
            ClassBundle export2 = new ClassBundle("net.assimilator.resources.servicecore.Service");
            export2.addJAR(new URL("http://10.1.1.3:9000/assimilator-dl.jar"));
            export2.addJAR(new URL("http://10.1.1.4:9000/service-dl.jar"));
            ServiceElement s2 = new ServiceElement(ServiceElement.DYNAMIC, sbc, new ServiceLevelAgreements(), new ClassBundle[] { export2 }, new ClassBundle(fdh), impl);
            System.out.println("s1 equal s2 ? " + s1.equals(s2));
            System.out.println("s2 equal s1 ? " + s2.equals(s1));
            System.out.println("s1 equal s1 ? " + s1.equals(s1));
            System.out.println("s2 equal s2 ? " + s2.equals(s2));
            System.out.println("s1 hash : " + s1.hashCode());
            System.out.println("s2 hash : " + s2.hashCode());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
