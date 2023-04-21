package net.assimilator.opstring;

import java.net.URL;
import java.util.*;
import net.assimilator.core.AssociationDescriptor;
import net.assimilator.core.ClassBundle;
import net.assimilator.core.ServiceLevelAgreements;
import net.assimilator.log.LoggerConfig;

/**
 * A ParsedService represents the descriptive attributes of a service that has
 * been parsed.
 */
public class ParsedService extends GlobalAttrs {

    static final String DYNAMIC = "dynamic";

    static final String EXTERNAL = "external";

    static final String FIXED = "fixed";

    /** Name of the service */
    private String name;

    /** Comment for the service */
    private String comment;

    /** OperationalString name for the service */
    private String opStringName;

    /** Array of ClassBundle objects for the Interface classes */
    private ClassBundle[] interfaceBundle;

    /** Number of services to maintain */
    private int maintain = 0;

    /** Max number of services per container */
    private int MaxPerContainer = 0;

    /** Flag to determine if the service requires to match-on-name instead of
     * just by interfaces */
    private boolean useName = false;

    /** Flag to determine the provision type, dynamic or external. True is
     * dynamic */
    private String provisionType;

    /** Flag to determine if the service should be automatically advertised when
     * provisioned */
    private boolean autoAdvertise = false;

    /** Flag to determine if the service should use DiscoveryManagement pooling */
    private boolean discoPool = true;

    /** Collection of service associations */
    private Vector associations = new Vector();

    /** Component ClassBundle */
    private ClassBundle componentBundle;

    /** FaultDetectionHandler ClassBundle */
    private ClassBundle fdhBundle;

    /** ServiceLevelAgreements object */
    private ServiceLevelAgreements slaAgreements;

    /**
     * Create a ParsedService with a descriptive name
     * 
     * @param name The name of the service
     * @param global GlobalAttrs containing attributes that should be applied to 
     * the ParsedService. 
     */
    public ParsedService(String name, GlobalAttrs global) {
        super();
        this.name = name;
        if (global.cluster.size() > 0) cluster.addAll(global.cluster);
        if (global.getCodebase() != null) setCodebase(global.getCodebase());
        this.configParms.addAll(global.configParms);
        this.groupList.addAll(global.groupList);
        this.locatorList.addAll(global.locatorList);
        this.logConfigs.addAll(global.logConfigs);
        if (global.organization != null) this.organization = global.organization;
        this.initParms.putAll(global.initParms);
    }

    /**
     * Set auto advertise
     * 
     * @param value String value for autoAdvertise. If "yes" then true,
     * otherwise false
     */
    public void setAutoAdvertise(String value) {
        if (value.equals("yes")) autoAdvertise = true; else autoAdvertise = false;
    }

    /**
     * Get auto advertise
     * 
     * @return autoAdvertise
     */
    public boolean getAutoAdvertise() {
        return (autoAdvertise);
    }

    /**
     * Set discoPool
     * 
     * @param value String value for discoPool. If "yes" then true,
     * otherwise false
     */
    public void setDiscoveryManagementPooling(String value) {
        if (value.equals("yes")) discoPool = true; else discoPool = false;
    }

    /**
     * Get discoPool
     * 
     * @return discoPool
     */
    public boolean getDiscoveryManagementPooling() {
        return (discoPool);
    }

    /**
     * Set the name for the ParsedService
     * 
     * @param name The name of the service
     */
    public void setName(String name) {
        this.name = name.trim();
    }

    /**
     * Get the name that the ParsedService has been constructed with
     * 
     * @return The name of the service
     */
    public String getName() {
        return (name);
    }

    /**
     * Set the comment
     * 
     * @param comment The comment for the service
     */
    public void setComment(String comment) {
        this.comment = comment.trim();
    }

    /**
     * Get the comment
     * 
     * @return The comment for the service
     */
    public String getComment() {
        return (comment);
    }

    /**
     * Set the OperationalString name
     * 
     * @param opStringName The OperationalString name the service belongs to
     */
    public void setOperationalStringName(String opStringName) {
        this.opStringName = opStringName;
    }

    /**
     * Get the OperationalString name
     * 
     * @return The OperationalString name the service belongs to
     */
    public String getOperationalStringName() {
        return (opStringName);
    }

    /**
     * Set the Interfaces as ClassBundle object containing the className and
     * resources required to load each class
     * 
     * @param bundles Array of ClassBundle objects
     */
    public void setInterfaceBundles(ClassBundle[] bundles) {
        if (bundles == null) {
            interfaceBundle = new ClassBundle[0];
        } else {
            interfaceBundle = new ClassBundle[bundles.length];
            System.arraycopy(bundles, 0, interfaceBundle, 0, bundles.length);
        }
    }

    /**
     * Get the Interfaces as ClassBundle object containing the className and
     * resources required to load each class
     * 
     * @return Array of ClassBundle objects
     */
    public ClassBundle[] getInterfaceBundles() {
        ClassBundle[] bundle = new ClassBundle[interfaceBundle.length];
        System.arraycopy(interfaceBundle, 0, bundle, 0, interfaceBundle.length);
        return (bundle);
    }

    /**
     * Set the Component as ClassBundle object containing the className and
     * resources required to load the component
     * 
     * @param bundle ClassBundle for the component
     */
    public void setComponentBundle(ClassBundle bundle) {
        componentBundle = bundle;
    }

    /**
     * Get the Component as ClassBundle object containing the className and
     * resources required to load the component
     * 
     * @return ClassBundle for the component
     */
    public ClassBundle getComponentBundle() {
        return (componentBundle);
    }

    /**
     * Set the maintain value
     * 
     * @param maintain The number of service instances to maintain
     */
    public void setMaintain(String maintain) {
        this.maintain = new Integer(maintain).intValue();
    }

    /**
     * Get the maintain
     * 
     * @return The number of services to maintain
     */
    public int getMaintain() {
        return (maintain);
    }

    /**
     * Set the MaxPerContainer value
     * 
     * @param MaxPerContainer The maximum number of service instances per
     * container
     */
    public void setMaxPerContainer(String MaxPerContainer) {
        this.MaxPerContainer = new Integer(MaxPerContainer).intValue();
    }

    /**
     * Get the MaxPerContainer value
     * 
     * @return The maximum number of service instances per container. If the
     * MaxPerContainer value is 0, then return -1
     */
    public int getMaxPerContainer() {
        return ((MaxPerContainer == 0 ? -1 : MaxPerContainer));
    }

    /**
     * Add an AssociationDescriptor
     * 
     * @param a The AssociationDescriptor to add.
     * @throws Exception If the AssociationDescriptor already exists (as
     * defined by the equals method of an AssociationDescriptor), then an
     * Exception is thrown
     */
    public void addAssociationDescriptor(AssociationDescriptor a) throws Exception {
        if (associations.contains(a)) throw new Exception("Duplicate AssociationDescriptor [" + a.getName() + "]");
        associations.add(a);
    }

    /**
     * Get all AssociationDescriptors
     * 
     * @return An array of AssociationDescriptor components. This method will 
     * return a new array each time. If there are no AssociationDescriptor 
     * components then a zero-length array will be returned
     */
    public AssociationDescriptor[] getAssociationDescriptors() {
        return ((AssociationDescriptor[]) associations.toArray(new AssociationDescriptor[associations.size()]));
    }

    /**
     * Set whether the provisioning system should match on the service's name as
     * well as it's interfaces
     * 
     * @param value If "yes", then true, otherwise false
     */
    public void setMatchOnName(String value) {
        if (value.equals("yes")) useName = true;
    }

    /**
     * Get whether the provisioning system whould match on the service's name as
     * well as it's interfaces
     * 
     * @return If true then match on name as well as interfaces,
     * othwerwise just match on interfaces
     */
    public boolean getMatchOnName() {
        return (useName);
    }

    /**
     * Set the provisioning type of the service
     * 
     * @param value If the value is "dynamic", then the type maps to
     * ServiceElement.DYNAMIC, otherwise
     * ServiceElement.FIXED
     */
    public void setProvisionType(String value) {
        if (value.equals(DYNAMIC) || value.equals(EXTERNAL) || value.equals(FIXED)) provisionType = value; else throw new IllegalArgumentException(value + " : unknown provision type");
    }

    /**
     * Set the provisioning type of the service
     * 
     * @return Either ServiceElement.DYNAMIC,
     * ServiceElement.FIXED or ServiceElement.EXTERNAL
     */
    public int getProvisionType() {
        int type = net.assimilator.core.ServiceElement.EXTERNAL;
        if (provisionType.equals(DYNAMIC)) type = net.assimilator.core.ServiceElement.DYNAMIC; else if (provisionType.equals(FIXED)) type = net.assimilator.core.ServiceElement.FIXED;
        return (type);
    }

    /**
     * Set the ClassBundle for the FaultDetectionHandler
     * 
     * @param bundle The ClassBundle for the FaultDetectionHandler
     */
    public void setFaultDetectionHandlerBundle(ClassBundle bundle) {
        if (bundle != null) fdhBundle = bundle;
    }

    /**
     * Get the ClassBundle for the FaultDetectionHandler
     * 
     * @return The ClassBundle for the FaultDetectionHandler
     */
    public ClassBundle getFaultDetectionHandlerBundle() {
        return (fdhBundle);
    }

    /**
     * Set the ServiceLevelAgreements
     * 
     * @param sla The ServiceLevelAgreements for the service
     */
    public void setServiceLevelAgreements(ServiceLevelAgreements sla) {
        this.slaAgreements = sla;
    }

    /**
     * Get the ServiceLevelAgreements
     * 
     * @return The ServiceLevelAgreements for the service
     */
    public ServiceLevelAgreements getServiceLevelAgreements() {
        if (slaAgreements == null) slaAgreements = new ServiceLevelAgreements();
        return (slaAgreements);
    }

    /** 
     * Provide a String output 
     */
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("ParsedService : " + name + "\n");
        buffer.append("Use Name      : " + useName + "\n");
        buffer.append("OpString      : " + opStringName + "\n");
        buffer.append("Maintain      : " + maintain + "\n");
        buffer.append("MaxPerContainer : " + MaxPerContainer + "\n");
        buffer.append("ProvisionType : " + provisionType + "\n");
        buffer.append("AutoAdvertise : " + autoAdvertise + "\n");
        buffer.append("Comment       : " + comment + "\n");
        buffer.append("Codebase      : " + codebase + "\n");
        buffer.append("Organization  : " + organization + "\n");
        buffer.append("Groups\n");
        String[] g = getGroups();
        for (int i = 0; i < g.length; i++) buffer.append("\t" + g[i] + "\n");
        buffer.append("Locators\n");
        String[] l = getLocators();
        for (int i = 0; i < l.length; i++) buffer.append("\t" + l[i] + "\n");
        buffer.append("Implementation\n");
        if (componentBundle != null) {
            buffer.append("\t" + componentBundle.getClassName() + "\n");
            URL[] urls = componentBundle.getJARs();
            for (int i = 0; i < urls.length; i++) buffer.append("\t\t" + urls[i].toExternalForm() + "\n");
        } else {
            buffer.append("\tnull\n");
        }
        buffer.append("Interfaces\n");
        for (int i = 0; i < interfaceBundle.length; i++) {
            buffer.append("\t" + interfaceBundle[i].getClassName() + "\n");
            URL[] urls = interfaceBundle[i].getJARs();
            for (int j = 0; j < urls.length; j++) buffer.append("\t\t" + urls[j].toExternalForm() + "\n");
        }
        buffer.append("Parameters\n");
        Set keys = initParms.keySet();
        for (Iterator it = keys.iterator(); it.hasNext(); ) {
            String key = (String) it.next();
            String value = (String) initParms.get(key);
            buffer.append("\tName=" + key + " Value=" + value + "\n");
        }
        buffer.append("Configuration\n");
        String[] config = getConfigParameters();
        for (int i = 0; i < config.length; i++) buffer.append("\t" + config[i] + "\n");
        buffer.append("Cluster\n");
        for (Iterator it = cluster.iterator(); it.hasNext(); ) buffer.append("\t" + it.next() + "\n");
        buffer.append("Logging\n");
        for (Iterator it = logConfigs.iterator(); it.hasNext(); ) {
            LoggerConfig lc = (LoggerConfig) it.next();
            buffer.append(lc.toString() + "\n");
        }
        return (buffer.toString());
    }
}
