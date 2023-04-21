package org.slasoi.gslam.main.activator.impl;

import java.util.Enumeration;
import java.util.Hashtable;
import org.slasoi.gslam.core.authorization.Authorization;
import org.slasoi.gslam.core.context.GenericSLAManagerUtils;
import org.slasoi.gslam.core.context.SLAManagerContext;
import org.slasoi.gslam.core.monitoring.IMonitoringManager;
import org.slasoi.gslam.core.negotiation.ISyntaxConverter;
import org.slasoi.gslam.core.negotiation.ISyntaxConverter.SyntaxConverterType;
import org.slasoi.gslam.core.negotiation.ProtocolEngine;
import org.slasoi.gslam.core.negotiation.SLARegistry;
import org.slasoi.gslam.core.negotiation.SLATemplateRegistry;
import org.slasoi.gslam.core.negotiation.ServiceManagerRegistry;
import org.slasoi.gslam.core.pac.ProvisioningAdjustment;
import org.slasoi.gslam.core.poc.PlanningOptimization;
import org.slasoi.sa.pss4slam.core.ServiceAdvertisement;

/**
 * Implementation of SLAManagerContext. This class aims the integration of generic-components ( SyntaxConverter,
 * SLARegistry, SLATemplateRegistry, ProtocolEngine and Monitoring Manager ) of any SLAManager.<br>
 * <br>
 * 
 * @author Miguel Rojas (UDO)
 * 
 */
public class GenericSLAManagerContextImpl implements SLAManagerContext {

    public GenericSLAManagerContextImpl(String id, String epr, String wsprefix) {
        ID = id;
        EPR = epr;
        wsPrefix = wsprefix;
    }

    public void setProperties(Hashtable<String, String> props) {
        this.properties = props;
    }

    public void setGroupID(String groupID) {
        this.groupID = groupID;
    }

    public String getEPR() throws SLAManagerContextException {
        return EPR;
    }

    public String getWSPrefix() throws SLAManagerContextException {
        return wsPrefix;
    }

    public Hashtable<ISyntaxConverter.SyntaxConverterType, ISyntaxConverter> getSyntaxConverters() throws SLAManagerContextException {
        if (syntaxconverters == null) throw new SLAManagerContextException("Generic SyntaxConverters have not been initialized");
        return syntaxconverters;
    }

    public IMonitoringManager getMonitorManager() throws SLAManagerContextException {
        if (monitormanager == null) throw new SLAManagerContextException("Generic MonitorManager has not been initialized");
        return monitormanager;
    }

    public PlanningOptimization getPlanningOptimization() throws SLAManagerContextException {
        if (poc == null) throw new SLAManagerContextException("The PlanningOptimization component has not been initialized");
        return poc;
    }

    public ProtocolEngine getProtocolEngine() throws SLAManagerContextException {
        if (protocolengine == null) throw new SLAManagerContextException("The ProtocolEngine component has not been initialized");
        return protocolengine;
    }

    public ProvisioningAdjustment getProvisioningAdjustment() throws SLAManagerContextException {
        if (pac == null) throw new SLAManagerContextException("The ProvisioningAdjustment component has not been initialized");
        return pac;
    }

    public String getSLAManagerID() throws SLAManagerContextException {
        if (ID == null) throw new SLAManagerContextException("The SLA-Manager ID has not been initialized");
        return ID;
    }

    public Hashtable<String, String> getProperties() throws SLAManagerContextException {
        return properties;
    }

    public String getGroupID() throws SLAManagerContextException {
        return groupID;
    }

    public SLARegistry getSLARegistry() throws SLAManagerContextException {
        if (slaregistry == null) throw new SLAManagerContextException("The SLARegistry component has not been initialized");
        return slaregistry;
    }

    public SLATemplateRegistry getSLATemplateRegistry() throws SLAManagerContextException {
        if (slatemplateregistry == null) throw new SLAManagerContextException("The SLATemplateRegistry component has not been initialized");
        return slatemplateregistry;
    }

    public ServiceAdvertisement getServiceAdvertisement() throws SLAManagerContextException {
        if (serviceadvertisement == null) throw new SLAManagerContextException("The ServiceAdvertisement component has not been initialized");
        return serviceadvertisement;
    }

    public ServiceManagerRegistry getServiceManagerRegistry() throws SLAManagerContextException {
        if (servicemanagerregistry == null) throw new SLAManagerContextException("The ServiceManagerRegistry component has not been initialized");
        return servicemanagerregistry;
    }

    public void setPlanningOptimization(PlanningOptimization customizedPOC) {
        poc = customizedPOC;
    }

    public void setProvisioningAdjustment(ProvisioningAdjustment customizedPAC) {
        pac = customizedPAC;
    }

    public void setSLATemplateRegistry(SLATemplateRegistry slatemplateregistry) {
        this.slatemplateregistry = slatemplateregistry;
    }

    public void setSLARegistry(SLARegistry slaregistry) {
        this.slaregistry = slaregistry;
    }

    public void setProtocolEngine(ProtocolEngine protocolengine) {
        this.protocolengine = protocolengine;
    }

    public void setSyntaxConverters(Hashtable<ISyntaxConverter.SyntaxConverterType, ISyntaxConverter> converters) {
        this.syntaxconverters = converters;
    }

    public void setServiceManagerRegistry(ServiceManagerRegistry servicemanagerregistry) {
        this.servicemanagerregistry = servicemanagerregistry;
    }

    public void setServiceAdvertisement(ServiceAdvertisement serviceadvertisement) {
        this.serviceadvertisement = serviceadvertisement;
    }

    public void setMonitorManager(IMonitoringManager monitormanager) {
        this.monitormanager = monitormanager;
    }

    public GenericSLAManagerUtils getSlamUtils() {
        return slamUtils;
    }

    public void setSlamUtils(GenericSLAManagerUtils slamUtils) {
        this.slamUtils = slamUtils;
    }

    public Authorization getAuthorization() throws SLAManagerContextException {
        return authorization;
    }

    public void setAuthorization(Authorization auth) {
        authorization = auth;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("\t\t[ID]                  ='" + ID + "'\n");
        sb.append("\t\t[EPR]                 ='" + EPR + "'\n");
        sb.append("\t\t[GroupID]             ='" + groupID + "'\n");
        sb.append("\t\t[wsPrefix]            ='" + wsPrefix + "'\n");
        sb.append("\t\t[Gslam-services]      ='" + slamUtils + "'\n");
        sb.append("\t\tSLATemplateRegistry   ='" + slatemplateregistry + "'\n");
        sb.append("\t\tSLARegistry           ='" + slaregistry + "'\n");
        sb.append("\t\tProtocolEngine        ='" + protocolengine + "'\n");
        sb.append("\t\tSyntaxConverters      ='\n");
        Enumeration<SyntaxConverterType> keys = syntaxconverters.keys();
        while (keys.hasMoreElements()) {
            SyntaxConverterType type = keys.nextElement();
            ISyntaxConverter iSyntaxConverter = syntaxconverters.get(type);
            sb.append("\t\t\t\t" + type + "=" + iSyntaxConverter + "\n");
        }
        sb.append("\t\tAuthorization         ='" + authorization + "'\n");
        sb.append("\t\tServiceAdvertisement  ='" + serviceadvertisement + "'\n");
        sb.append("\t\tMonitorManager        ='" + monitormanager + "'\n");
        sb.append("\t\tPlanningOptimization  ='" + poc + "'\n");
        sb.append("\t\tProvisioningAdjustment='" + pac + "'");
        return sb.toString();
    }

    /** SLA Manager Id */
    protected String ID;

    /** Group Id of its SLA Manager */
    protected String groupID;

    /** End Point of its SLA Manager */
    protected String EPR;

    /** WebService prefix for SyntaxConverter */
    protected String wsPrefix;

    /** Extra properties for SLAM */
    protected Hashtable<String, String> properties;

    protected SLATemplateRegistry slatemplateregistry;

    protected SLARegistry slaregistry;

    protected ProtocolEngine protocolengine;

    protected ServiceManagerRegistry servicemanagerregistry;

    protected ServiceAdvertisement serviceadvertisement;

    protected IMonitoringManager monitormanager;

    protected PlanningOptimization poc;

    protected ProvisioningAdjustment pac;

    protected Authorization authorization;

    protected Hashtable<ISyntaxConverter.SyntaxConverterType, ISyntaxConverter> syntaxconverters;

    protected GenericSLAManagerUtils slamUtils;
}
