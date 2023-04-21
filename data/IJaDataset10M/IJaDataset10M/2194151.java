package de.dgrid.bisgrid.common.bpel.adapter.openesb;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import javax.xml.namespace.QName;
import oasisNamesTcEntityXmlnsXmlCatalog.CatalogDocument;
import org.apache.log4j.Logger;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.oasisOpen.docs.wsbpel.x20.plnktype.TRole;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TPartnerLink;
import org.xmlsoap.schemas.wsdl.DefinitionsDocument;
import org.xmlsoap.schemas.wsdl.TBinding;
import org.xmlsoap.schemas.wsdl.TPort;
import org.xmlsoap.schemas.wsdl.TService;
import com.sun.esb.management.api.deployment.DeploymentService;
import com.sun.esb.management.client.ManagementClient;
import com.sun.esb.management.client.ManagementClientFactory;
import com.sun.esb.management.common.ManagementRemoteException;
import com.sun.java.xml.ns.jbi.JbiDocument;
import com.sun.java.xml.ns.jbi.ConnectionDocument.Connection;
import com.sun.java.xml.ns.jbi.ConsumerDocument.Consumer;
import com.sun.java.xml.ns.jbi.ConsumesDocument.Consumes;
import com.sun.java.xml.ns.jbi.ProviderDocument.Provider;
import com.sun.java.xml.ns.jbi.ProvidesDocument.Provides;
import de.dgrid.bisgrid.common.BISGridNamespaces;
import de.dgrid.bisgrid.common.BISGridProperties;
import de.dgrid.bisgrid.common.bpel.BPELWorkflowEngine;
import de.dgrid.bisgrid.common.bpel.adapter.DeploymentAdapter;
import de.dgrid.bisgrid.common.bpel.adapter.DeploymentAdapterResult;
import de.dgrid.bisgrid.common.bpel.adapter.exceptions.BPELWorkflowEngineDeployFault;
import de.dgrid.bisgrid.common.bpel.adapter.exceptions.BPELWorkflowEngineUndeployFault;
import de.dgrid.bisgrid.common.exceptions.XsltProcessingException;
import de.dgrid.bisgrid.common.util.FileUtils;
import de.dgrid.bisgrid.common.util.XMLUtils;
import de.dgrid.bisgrid.common.util.BPELUtils;
import de.dgrid.bisgrid.common.xslt.XSLTProcessor;
import de.dgrid.bisgrid.services.management.deployment.PartnerLinkXSDType;
import de.dgrid.bisgrid.services.management.deployment.ProcessDocument;
import de.dgrid.bisgrid.services.management.properties.workflowManagementService.OtherFilesDocument;
import de.dgrid.bisgrid.services.management.properties.workflowManagementService.WSDLType;
import de.dgrid.bisgrid.services.management.properties.workflowManagementService.WsdlFilesDocument;
import de.dgrid.bisgrid.services.management.properties.workflowManagementService.XsdFilesDocument;

/**
 * 
 * A deployment adapter to deploy a service assembly to OpenESB (e.g. in glassfish)
 * 
 * @author gscherp
 *
 */
public class OpenESBDeploymentAdapter implements DeploymentAdapter {

    private Logger log = Logger.getLogger(this.getClass());

    private final String serviceAssemblyZipDir = "assembly";

    private final String tempDir = "work";

    private final String sunBpelEngineJarDir = tempDir + File.separator + "process";

    private final String sunHttpBindingJarDir = tempDir + File.separator + "sun-http-binding";

    private final String sunBpelEngineJar = "process.jar";

    private final String sunHttpBindingJar = "sun-http-binding.jar";

    private String mainJBIXSLT = "openesb_main_jbi_transformation.xslt";

    private String sunBpelEngineJBIXSLT = "openesb_bpel_engine_jbi_transformation.xslt";

    private String sunBpelEngineCatalogXSLT = "openesb_bpel_engine_catalog_transformation.xslt";

    private String sunHttpBindingJBIXSLT = "openesb_http_binding_jbi_transformation.xslt";

    private String sunHttpBindingCatalogXSLT = "openesb_http_binding_catalog_transformation.xslt";

    private Map<QName, TService> services = new HashMap<QName, TService>();

    private Map<String, String> bddNamespaces = new HashMap<String, String>();

    private Map<QName, TPartnerLink> bpelPartnerLinks = new HashMap<QName, TPartnerLink>();

    private Map<String, QName> partnerLinkTypesRolePortTypes = new HashMap<String, QName>();

    private Map<QName, QName> portTypeBindings = new HashMap<QName, QName>();

    public org.oasisOpen.docs.wsbpel.x20.process.executable.ProcessDocument injectAdapterSpecificCode(org.oasisOpen.docs.wsbpel.x20.process.executable.ProcessDocument bpelCode) {
        return bpelCode;
    }

    public DeploymentAdapterResult deploy(String workingDirectory, String processName, String bpelProcessName, org.oasisOpen.docs.wsbpel.x20.process.executable.ProcessDocument bpelProcess, de.dgrid.bisgrid.services.management.deployment.ProcessDocument deploymentDescriptor, de.dgrid.bisgrid.services.management.properties.workflowManagementService.WsdlFilesDocument wsdls, de.dgrid.bisgrid.services.management.properties.workflowManagementService.XsdFilesDocument xsds, de.dgrid.bisgrid.services.management.properties.workflowManagementService.OtherFilesDocument otherFiles) throws BPELWorkflowEngineDeployFault {
        String assemblyDir = workingDirectory + File.separator + this.serviceAssemblyZipDir;
        String assemblyMETAINFDir = workingDirectory + File.separator + this.serviceAssemblyZipDir + File.separator + "META-INF";
        String bpelEngineDir = workingDirectory + File.separator + this.sunBpelEngineJarDir;
        String bpelEngineMETAINFDir = workingDirectory + File.separator + this.sunBpelEngineJarDir + File.separator + "META-INF";
        String httpBindingDir = workingDirectory + File.separator + this.sunHttpBindingJarDir;
        String httpBindingMETAINFDir = workingDirectory + File.separator + this.sunHttpBindingJarDir + File.separator + "META-INF";
        this.analyzeFiles(bpelProcess, deploymentDescriptor, wsdls, xsds, otherFiles);
        this.performModifications(bpelProcess, deploymentDescriptor, wsdls, xsds, otherFiles);
        JbiDocument mainJBI = this.generateMainJBI(deploymentDescriptor);
        JbiDocument bpelEngineJBI = this.generateSunBpelEngineJBI(deploymentDescriptor);
        CatalogDocument bpelEngineCatalog = this.generateSunBpelEngineCatalog(deploymentDescriptor);
        JbiDocument httpBindingJBI = this.generateSunHttpBindingJBI(deploymentDescriptor);
        CatalogDocument httpBindingCatalog = this.generateSunHttpBindingCatalog(deploymentDescriptor);
        Map<String, XmlObject> files = new HashMap<String, XmlObject>();
        files.put(assemblyMETAINFDir + File.separator + "jbi.xml", mainJBI);
        files.put(bpelEngineDir + File.separator + deploymentDescriptor.getProcess().getBpelDescription().getLocation(), bpelProcess);
        files.put(bpelEngineMETAINFDir + File.separator + "jbi.xml", bpelEngineJBI);
        files.put(bpelEngineMETAINFDir + File.separator + "catalog.xml", bpelEngineCatalog);
        files.put(httpBindingMETAINFDir + File.separator + "jbi.xml", httpBindingJBI);
        files.put(httpBindingMETAINFDir + File.separator + "catalog.xml", httpBindingCatalog);
        for (WSDLType wsdl : wsdls.getWsdlFiles().getWsdlArray()) {
            DefinitionsDocument wsdlDocument = DefinitionsDocument.Factory.newInstance();
            wsdlDocument.setDefinitions(wsdl.getDefinitions());
            files.put(bpelEngineDir + File.separator + wsdl.getName(), wsdlDocument);
            files.put(httpBindingDir + File.separator + bpelProcess.getProcess().getName() + File.separator + wsdl.getName(), wsdlDocument);
        }
        this.writeFiles(files);
        Vector<String[]> archives = new Vector<String[]>();
        archives.add(new String[] { assemblyDir + File.separator + this.sunBpelEngineJar, bpelEngineDir });
        archives.add(new String[] { assemblyDir + File.separator + this.sunHttpBindingJar, httpBindingDir });
        archives.add(new String[] { workingDirectory + File.separator + bpelProcess.getProcess().getName() + ".zip", assemblyDir });
        this.createJarArchives(archives);
        String result = this.deployAssembly(workingDirectory + File.separator + bpelProcess.getProcess().getName() + ".zip");
        if (!result.equals(bpelProcess.getProcess().getName())) {
            throw new BPELWorkflowEngineDeployFault("Deployment failed :" + result);
        }
        return new DeploymentAdapterResult("bpelProcessName", result);
    }

    public DeploymentAdapterResult undeploy(String workingDirectory, String processName, String bpelProcessName, String bpelEngineProcessName) throws BPELWorkflowEngineUndeployFault {
        String result = this.undeployAssembly(bpelProcessName);
        log.debug("Cleaning working directory");
        if (!FileUtils.deleteDirectory(workingDirectory)) {
            throw new BPELWorkflowEngineUndeployFault("Could not clean working directory");
        }
        return new DeploymentAdapterResult(bpelProcessName, result);
    }

    private void analyzeFiles(org.oasisOpen.docs.wsbpel.x20.process.executable.ProcessDocument bpelProcess, de.dgrid.bisgrid.services.management.deployment.ProcessDocument deploymentDescriptor, de.dgrid.bisgrid.services.management.properties.workflowManagementService.WsdlFilesDocument wsdls, de.dgrid.bisgrid.services.management.properties.workflowManagementService.XsdFilesDocument xsds, de.dgrid.bisgrid.services.management.properties.workflowManagementService.OtherFilesDocument otherFiles) throws BPELWorkflowEngineDeployFault {
        this.bddNamespaces = XMLUtils.getNamespaces(deploymentDescriptor.getProcess());
        this.bpelPartnerLinks = BPELUtils.getBpelPartnerLinks(bpelProcess);
        for (WSDLType wsdlType : wsdls.getWsdlFiles().getWsdlArray()) {
            try {
                for (TService service : wsdlType.getDefinitions().getServiceArray()) {
                    QName serviceQName = new QName(wsdlType.getDefinitions().getTargetNamespace(), service.getName());
                    this.services.put(serviceQName, service);
                }
                String rolesSearch = "declare namespace plnk2='http://docs.oasis-open.org/wsbpel/2.0/plnktype';" + "plnk2:partnerLinkType/plnk2:role";
                for (XmlObject role : wsdlType.getDefinitions().selectPath(rolesSearch)) {
                    TRole roleType = TRole.Factory.parse(role.xmlText());
                    String partnerLinkTypeName = role.getDomNode().getParentNode().getAttributes().getNamedItem("name").getNodeValue();
                    QName portType = roleType.getPortType();
                    this.partnerLinkTypesRolePortTypes.put(partnerLinkTypeName + ":" + roleType.getName(), portType);
                    for (WSDLType wsdl2 : wsdls.getWsdlFiles().getWsdlArray()) {
                        String bindingSearch = "declare namespace wsdl='" + BISGridNamespaces.WSDL_NAMESPACE + "';" + "wsdl:binding";
                        for (XmlObject binding : wsdl2.getDefinitions().selectPath(bindingSearch)) {
                            TBinding bindingType = TBinding.Factory.parse(binding.xmlText());
                            if (bindingType.getType().equals(portType)) {
                                QName bindingName = new QName(wsdl2.getDefinitions().getTargetNamespace(), bindingType.getName());
                                this.portTypeBindings.put(portType, bindingName);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                log.error("Error analyzing WSDL file " + wsdlType.getName() + " : " + e.getMessage());
                throw new BPELWorkflowEngineDeployFault("Error analyzing WSDL file " + wsdlType.getName() + " : " + e.getMessage());
            }
        }
    }

    private void performModifications(org.oasisOpen.docs.wsbpel.x20.process.executable.ProcessDocument bpelProcess, de.dgrid.bisgrid.services.management.deployment.ProcessDocument deploymentDescriptor, de.dgrid.bisgrid.services.management.properties.workflowManagementService.WsdlFilesDocument wsdls, de.dgrid.bisgrid.services.management.properties.workflowManagementService.XsdFilesDocument xsds, de.dgrid.bisgrid.services.management.properties.workflowManagementService.OtherFilesDocument otherFiles) throws BPELWorkflowEngineDeployFault {
    }

    private JbiDocument generateMainJBI(ProcessDocument deploymentDescriptor) throws BPELWorkflowEngineDeployFault {
        try {
            String stylesheet = this.mainJBIXSLT;
            log.info("generating main jbi.xml");
            log.debug("using xsl stylesheet " + stylesheet);
            InputStream xsltStream = ClassLoader.getSystemResourceAsStream(stylesheet);
            if (xsltStream == null) throw new BPELWorkflowEngineDeployFault("Xsl stylesheet not found : " + this.mainJBIXSLT);
            Document sunBpelMainJBITemp = XSLTProcessor.process(deploymentDescriptor, ClassLoader.getSystemResourceAsStream(stylesheet));
            JbiDocument sunBpelMainJBI = JbiDocument.Factory.parse(this.convertDocumentToString(sunBpelMainJBITemp));
            for (PartnerLinkXSDType pl : deploymentDescriptor.getProcess().getPartnerLinks().getPartnerLinkArray()) {
                for (PartnerLinkXSDType.MyRole mr : pl.getMyRoleArray()) {
                    QName serviceQName = mr.getService();
                    if (!this.services.containsKey(serviceQName)) {
                        throw new BPELWorkflowEngineDeployFault("Could not find service reference " + serviceQName + " from BIS-Grid deployment descriptor");
                    }
                    TService service = this.services.get(serviceQName);
                    QName partnerLinkQName = pl.getName();
                    if (!this.bpelPartnerLinks.containsKey(partnerLinkQName)) {
                        throw new BPELWorkflowEngineDeployFault("Could not find service reference " + partnerLinkQName + " from BIS-Grid deployment descriptor");
                    }
                    TPartnerLink bpelPl = this.bpelPartnerLinks.get(partnerLinkQName);
                    for (TPort servicePort : service.getPortArray()) {
                        Connection connection = sunBpelMainJBI.getJbi().getServiceAssembly().getConnections().addNewConnection();
                        Consumer consumer = connection.addNewConsumer();
                        Provider provider = connection.addNewProvider();
                        consumer.setEndpointName(XMLUtils.createXmlAnySimpleType(servicePort.getName()));
                        consumer.setServiceName(serviceQName);
                        provider.setEndpointName(XMLUtils.createXmlAnySimpleType(bpelPl.getMyRole() + "_myRole"));
                        provider.setServiceName(partnerLinkQName);
                    }
                }
                for (PartnerLinkXSDType.PartnerRole pr : pl.getPartnerRoleArray()) {
                    QName serviceQName = pr.getService();
                    if (!this.services.containsKey(serviceQName)) {
                        throw new BPELWorkflowEngineDeployFault("Could not find service reference " + serviceQName + " from BIS-Grid deployment descriptor");
                    }
                    TService service = this.services.get(serviceQName);
                    QName partnerLinkQName = pl.getName();
                    if (!this.bpelPartnerLinks.containsKey(partnerLinkQName)) {
                        throw new BPELWorkflowEngineDeployFault("Could not find service reference " + partnerLinkQName + " from BIS-Grid deployment descriptor");
                    }
                    TPartnerLink bpelPl = this.bpelPartnerLinks.get(partnerLinkQName);
                    for (TPort servicePort : service.getPortArray()) {
                        Connection connection = sunBpelMainJBI.getJbi().getServiceAssembly().getConnections().addNewConnection();
                        Consumer consumer = connection.addNewConsumer();
                        Provider provider = connection.addNewProvider();
                        provider.setEndpointName(XMLUtils.createXmlAnySimpleType(servicePort.getName()));
                        provider.setServiceName(serviceQName);
                        consumer.setEndpointName(XMLUtils.createXmlAnySimpleType(bpelPl.getPartnerRole() + "_partnerRole"));
                        consumer.setServiceName(partnerLinkQName);
                    }
                }
            }
            return sunBpelMainJBI;
        } catch (XsltProcessingException e) {
            String msg = "Could not generate main jbi.xml :" + e.getMessage();
            log.error(msg);
            throw new BPELWorkflowEngineDeployFault(msg);
        } catch (IOException e) {
            String msg = "Could not generate main jbi.xml :" + e.getMessage();
            log.error(msg);
            throw new BPELWorkflowEngineDeployFault(msg);
        } catch (XmlException e) {
            String msg = "Could not generate main jbi.xml :" + e.getMessage();
            log.error(msg);
            throw new BPELWorkflowEngineDeployFault(msg);
        }
    }

    private JbiDocument generateSunBpelEngineJBI(ProcessDocument deploymentDescriptor) throws BPELWorkflowEngineDeployFault {
        try {
            String stylesheet = this.sunBpelEngineJBIXSLT;
            log.info("generating engine jbi.xml");
            log.debug("using xsl stylesheet " + stylesheet);
            InputStream xsltStream = ClassLoader.getSystemResourceAsStream(stylesheet);
            if (xsltStream == null) throw new BPELWorkflowEngineDeployFault("Xsl stylesheet not found : " + stylesheet);
            Document sunBpelEngineJBITemp = XSLTProcessor.process(deploymentDescriptor, ClassLoader.getSystemResourceAsStream(stylesheet));
            JbiDocument sunBpelEngineJBI = JbiDocument.Factory.parse(this.convertDocumentToString(sunBpelEngineJBITemp));
            for (PartnerLinkXSDType pl : deploymentDescriptor.getProcess().getPartnerLinks().getPartnerLinkArray()) {
                QName partnerLinkQName = pl.getName();
                if (!this.bpelPartnerLinks.containsKey(partnerLinkQName)) {
                    throw new BPELWorkflowEngineDeployFault("Could not find partner link " + partnerLinkQName + " from BIS-Grid deployment descriptor");
                }
                TPartnerLink bpelPl = this.bpelPartnerLinks.get(partnerLinkQName);
                String processName = deploymentDescriptor.getProcess().getName().getLocalPart();
                String bpelFilePath = deploymentDescriptor.getProcess().getBpelDescription().getLocation();
                String providesConsumesElement = new String("<xml-fragment>" + "<ns2:display-name xmlns:ns2=\"http://www.sun.com/jbi/descriptor/service-unit\" >" + partnerLinkQName.getLocalPart() + "</ns2:display-name>" + "<ns2:process-name xmlns:ns2=\"http://www.sun.com/jbi/descriptor/service-unit\">" + processName + "</ns2:process-name>" + "<ns2:file-path xmlns:ns2=\"http://www.sun.com/jbi/descriptor/service-unit\">" + bpelFilePath + "</ns2:file-path>" + "</xml-fragment>");
                String myRole = bpelPl.getMyRole();
                if (myRole != null) {
                    Provides provides = sunBpelEngineJBI.getJbi().getServices().addNewProvides();
                    provides.set(XmlObject.Factory.parse(providesConsumesElement));
                    QName portType = this.partnerLinkTypesRolePortTypes.get(bpelPl.getPartnerLinkType().getLocalPart() + ":" + bpelPl.getMyRole());
                    provides.setEndpointName(XMLUtils.createXmlAnySimpleType(bpelPl.getMyRole() + "_myRole"));
                    provides.setInterfaceName(portType);
                    provides.setServiceName(partnerLinkQName);
                }
                String partnerRole = bpelPl.getPartnerRole();
                if (partnerRole != null) {
                    Consumes consumes = sunBpelEngineJBI.getJbi().getServices().addNewConsumes();
                    consumes.set(XmlObject.Factory.parse(providesConsumesElement));
                    QName portType = this.partnerLinkTypesRolePortTypes.get(bpelPl.getPartnerLinkType().getLocalPart() + ":" + bpelPl.getPartnerRole());
                    consumes.setEndpointName(XMLUtils.createXmlAnySimpleType(bpelPl.getPartnerRole() + "_partnerRole"));
                    consumes.setInterfaceName(portType);
                    consumes.setServiceName(partnerLinkQName);
                }
            }
            return sunBpelEngineJBI;
        } catch (XsltProcessingException e) {
            String msg = "Could not generate bpel engine jbi.xml :" + e.getMessage();
            log.error(msg);
            throw new BPELWorkflowEngineDeployFault(msg);
        } catch (IOException e) {
            String msg = "Could not generate bpel engine jbi.xml :" + e.getMessage();
            log.error(msg);
            throw new BPELWorkflowEngineDeployFault(msg);
        } catch (XmlException e) {
            String msg = "Could not generate bpel engine jbi.xml :" + e.getMessage();
            log.error(msg);
            throw new BPELWorkflowEngineDeployFault(msg);
        }
    }

    private JbiDocument generateSunHttpBindingJBI(ProcessDocument deploymentDescriptor) throws BPELWorkflowEngineDeployFault {
        try {
            String stylesheet = this.sunHttpBindingJBIXSLT;
            log.info("generating engine jbi.xml");
            log.debug("using xsl stylesheet " + stylesheet);
            InputStream xsltStream = ClassLoader.getSystemResourceAsStream(stylesheet);
            if (xsltStream == null) throw new BPELWorkflowEngineDeployFault("Xsl stylesheet not found : " + stylesheet);
            Document sunHttpBindingJBITemp = XSLTProcessor.process(deploymentDescriptor, ClassLoader.getSystemResourceAsStream(stylesheet));
            JbiDocument sunHttpBindingJBI = JbiDocument.Factory.parse(this.convertDocumentToString(sunHttpBindingJBITemp));
            for (PartnerLinkXSDType pl : deploymentDescriptor.getProcess().getPartnerLinks().getPartnerLinkArray()) {
                QName partnerLinkQName = pl.getName();
                if (!this.bpelPartnerLinks.containsKey(partnerLinkQName)) {
                    throw new BPELWorkflowEngineDeployFault("Could not find partner link " + partnerLinkQName.getPrefix() + ":" + partnerLinkQName.getLocalPart() + " from BIS-Grid deployment descriptor");
                }
                TPartnerLink bpelPl = this.bpelPartnerLinks.get(partnerLinkQName);
                String bpelPlmyRole = bpelPl.getMyRole();
                if (bpelPlmyRole != null) {
                    String bpelPlType = bpelPl.getPartnerLinkType().getLocalPart();
                    if (!this.partnerLinkTypesRolePortTypes.containsKey(bpelPlType + ":" + bpelPlmyRole)) {
                        throw new BPELWorkflowEngineDeployFault("Could not find port type for role : " + bpelPlmyRole);
                    }
                    QName bpelPLPortType = this.partnerLinkTypesRolePortTypes.get(bpelPlType + ":" + bpelPlmyRole);
                    for (PartnerLinkXSDType.MyRole myRole : pl.getMyRoleArray()) {
                        QName serviceName = myRole.getService();
                        if (!this.services.containsKey(serviceName)) {
                            throw new BPELWorkflowEngineDeployFault("Could not find service " + serviceName);
                        }
                        TService service = this.services.get(serviceName);
                        if (!this.portTypeBindings.containsKey(bpelPLPortType)) {
                            throw new BPELWorkflowEngineDeployFault("Could not binding for port type " + bpelPLPortType);
                        }
                        QName binding = this.portTypeBindings.get(bpelPLPortType);
                        for (TPort port : service.getPortArray()) {
                            if (port.getBinding().equals(binding)) {
                                Consumes consumes = sunHttpBindingJBI.getJbi().getServices().addNewConsumes();
                                consumes.setEndpointName(XMLUtils.createXmlAnySimpleType(port.getName()));
                                consumes.setInterfaceName(bpelPLPortType);
                                consumes.setServiceName(serviceName);
                            }
                        }
                    }
                }
                String bpelPlpartnerRole = bpelPl.getPartnerRole();
                if (bpelPlpartnerRole != null) {
                    String bpelPlType = bpelPl.getPartnerLinkType().getLocalPart();
                    if (!this.partnerLinkTypesRolePortTypes.containsKey(bpelPlType + ":" + bpelPlpartnerRole)) {
                        throw new BPELWorkflowEngineDeployFault("Could not find port type for role : " + bpelPlpartnerRole);
                    }
                    QName bpelPLPortType = this.partnerLinkTypesRolePortTypes.get(bpelPlType + ":" + bpelPlpartnerRole);
                    for (PartnerLinkXSDType.PartnerRole partnerRole : pl.getPartnerRoleArray()) {
                        QName serviceName = partnerRole.getService();
                        if (!this.services.containsKey(serviceName)) {
                            throw new BPELWorkflowEngineDeployFault("Could not find service " + serviceName);
                        }
                        TService service = this.services.get(serviceName);
                        if (!this.portTypeBindings.containsKey(bpelPLPortType)) {
                            throw new BPELWorkflowEngineDeployFault("Could not binding for port type " + bpelPLPortType);
                        }
                        QName binding = this.portTypeBindings.get(bpelPLPortType);
                        for (TPort port : service.getPortArray()) {
                            if (port.getBinding().equals(binding)) {
                                Provides provides = sunHttpBindingJBI.getJbi().getServices().addNewProvides();
                                provides.setEndpointName(XMLUtils.createXmlAnySimpleType(port.getName()));
                                provides.setInterfaceName(bpelPLPortType);
                                provides.setServiceName(serviceName);
                            }
                        }
                    }
                }
            }
            return sunHttpBindingJBI;
        } catch (XsltProcessingException e) {
            String msg = "Could not generate http binding jbi.xml :" + e.getMessage();
            log.error(msg);
            throw new BPELWorkflowEngineDeployFault(msg);
        } catch (IOException e) {
            String msg = "Could not generate http binding jbi.xml :" + e.getMessage();
            log.error(msg);
            throw new BPELWorkflowEngineDeployFault(msg);
        } catch (XmlException e) {
            String msg = "Could not generate http binding jbi.xml :" + e.getMessage();
            log.error(msg);
            throw new BPELWorkflowEngineDeployFault(msg);
        }
    }

    private CatalogDocument generateSunBpelEngineCatalog(ProcessDocument deploymentDescriptor) throws BPELWorkflowEngineDeployFault {
        try {
            String stylesheet = this.sunBpelEngineCatalogXSLT;
            log.info("generating bpel engine catalog.xml");
            log.debug("using xsl stylesheet " + stylesheet);
            InputStream xsltStream = ClassLoader.getSystemResourceAsStream(stylesheet);
            if (xsltStream == null) throw new BPELWorkflowEngineDeployFault("Xsl stylesheet not found : " + stylesheet);
            Document sunBpelEngineCatalogTemp = XSLTProcessor.process(deploymentDescriptor, ClassLoader.getSystemResourceAsStream(stylesheet));
            CatalogDocument sunBpelEngineCatalog = CatalogDocument.Factory.parse(this.convertDocumentToString(sunBpelEngineCatalogTemp));
            return sunBpelEngineCatalog;
        } catch (XsltProcessingException e) {
            String msg = "Could not generate bpel engine catalog.xml :" + e.getMessage();
            log.error(msg);
            throw new BPELWorkflowEngineDeployFault(msg);
        } catch (IOException e) {
            String msg = "Could not generate bpel engine catalog.xml :" + e.getMessage();
            log.error(msg);
            throw new BPELWorkflowEngineDeployFault(msg);
        } catch (XmlException e) {
            String msg = "Could not generate bpel engine catalog.xml :" + e.getMessage();
            log.error(msg);
            throw new BPELWorkflowEngineDeployFault(msg);
        }
    }

    private CatalogDocument generateSunHttpBindingCatalog(ProcessDocument deploymentDescriptor) throws BPELWorkflowEngineDeployFault {
        try {
            String stylesheet = this.sunHttpBindingCatalogXSLT;
            log.info("generating http binding catalog.xml");
            log.debug("using xsl stylesheet " + stylesheet);
            InputStream xsltStream = ClassLoader.getSystemResourceAsStream(stylesheet);
            if (xsltStream == null) throw new BPELWorkflowEngineDeployFault("Xsl stylesheet not found : " + stylesheet);
            Document sunBpelEngineCatalogTemp = XSLTProcessor.process(deploymentDescriptor, ClassLoader.getSystemResourceAsStream(stylesheet));
            CatalogDocument sunBpelEngineCatalog = CatalogDocument.Factory.parse(this.convertDocumentToString(sunBpelEngineCatalogTemp));
            return sunBpelEngineCatalog;
        } catch (XsltProcessingException e) {
            String msg = "Could not generate http binding engine catalog.xml :" + e.getMessage();
            log.error(msg);
            throw new BPELWorkflowEngineDeployFault(msg);
        } catch (IOException e) {
            String msg = "Could not generate http binding catalog.xml :" + e.getMessage();
            log.error(msg);
            throw new BPELWorkflowEngineDeployFault(msg);
        } catch (XmlException e) {
            String msg = "Could not generate http binding catalog.xml :" + e.getMessage();
            log.error(msg);
            throw new BPELWorkflowEngineDeployFault(msg);
        }
    }

    private String deployAssembly(String assemblyLocation) throws BPELWorkflowEngineDeployFault {
        log.info("Deploying " + assemblyLocation);
        DeploymentService service = null;
        String target = null;
        String assemblyName = null;
        String result = null;
        ManagementClient client = null;
        try {
            client = this.getManagementClient();
            target = client.getAdministrationService().getAdminServerName();
        } catch (ManagementRemoteException e) {
            String msg = "Could not initialize client : " + e.getMessage();
            log.error(msg);
            throw new BPELWorkflowEngineDeployFault(msg);
        }
        try {
            service = client.getDeploymentService();
            log.debug("Deploying assembly");
            if (!assemblyLocation.startsWith("/")) {
                assemblyLocation = System.getProperty("user.dir") + File.separator + assemblyLocation;
            }
            assemblyName = service.deployServiceAssembly(assemblyLocation, target);
            log.debug(assemblyName);
        } catch (ManagementRemoteException e) {
            String msg = "Could not deploy assembly : " + e.getMessage();
            log.error(msg);
            throw new BPELWorkflowEngineDeployFault(msg);
        }
        try {
            log.debug("Starting assembly");
            result = client.getRuntimeManagementService().startServiceAssembly(assemblyName, target);
            log.debug(result);
        } catch (ManagementRemoteException e) {
            String msg = "Could not start assembly : " + e.getMessage();
            log.error(msg);
            log.debug("Try to undeploy assembly");
            try {
                service.undeployServiceAssembly(assemblyName, target);
            } catch (ManagementRemoteException me) {
                log.error("Could not undeploy assembly : " + me.getMessage());
            }
            ;
            throw new BPELWorkflowEngineDeployFault(msg);
        }
        log.info("Deployment finished" + result);
        return result;
    }

    private String undeployAssembly(String assemblyName) throws BPELWorkflowEngineUndeployFault {
        log.info("Undeploying " + assemblyName);
        String target = null;
        String result = null;
        DeploymentService service = null;
        ManagementClient client = null;
        try {
            client = this.getManagementClient();
            target = client.getAdministrationService().getAdminServerName();
        } catch (ManagementRemoteException e) {
            String msg = "Could not initialize client : " + e.getMessage();
            log.error(msg);
            throw new BPELWorkflowEngineUndeployFault(msg);
        }
        try {
            log.debug("Stopping assembly");
            result = client.getRuntimeManagementService().stopServiceAssembly(assemblyName, target);
            log.debug(result);
        } catch (ManagementRemoteException e) {
            String msg = "Could not stop assembly : " + e.getMessage();
            log.error(msg);
            throw new BPELWorkflowEngineUndeployFault(msg);
        }
        try {
            log.debug("Shutting down assembly");
            result = client.getRuntimeManagementService().shutdownServiceAssembly(assemblyName, target);
            log.debug(result);
        } catch (ManagementRemoteException e) {
            String msg = "Could not shut down assembly : " + e.getMessage();
            log.error(msg);
            throw new BPELWorkflowEngineUndeployFault(msg);
        }
        try {
            service = client.getDeploymentService();
            log.debug("Undeploying assembly");
            result = service.undeployServiceAssembly(assemblyName, target);
            log.debug(result);
        } catch (ManagementRemoteException e) {
            String msg = "Could not undeploy assembly : " + e.getMessage();
            log.error(msg);
            throw new BPELWorkflowEngineUndeployFault(msg);
        }
        log.info("Undeployment finished");
        return "Undeployment finished : " + result;
    }

    private ManagementClient getManagementClient() throws ManagementRemoteException {
        int engineNumber = 0;
        BISGridProperties properties = BISGridProperties.getInstance();
        String host = properties.getBPELWorkflowEngineHost(engineNumber);
        int port = properties.getBPELWorkflowEnginePort(engineNumber);
        String username = properties.getBPELWorkflowEngineUsername(engineNumber);
        String password = properties.getBPELWorkflowEnginePassword(engineNumber);
        log.info("Using following rmi connection to OpenESB: " + username + ":xxxxxx@" + host + ":" + port);
        ManagementClient client = ManagementClientFactory.getInstance(host, port, username, password);
        if (!client.getAdministrationService().isJBIRuntimeEnabled()) {
            throw new ManagementRemoteException("OpenESB is not running properly");
        }
        return client;
    }

    private String convertDocumentToString(Document document) throws IOException {
        StringWriter strWriter = new StringWriter();
        Format format = Format.getPrettyFormat();
        XMLOutputter xmlOut = new XMLOutputter(format);
        xmlOut.output(document, strWriter);
        return strWriter.toString();
    }

    private void writeFiles(Map<String, XmlObject> files) throws BPELWorkflowEngineDeployFault {
        for (String filePath : files.keySet()) {
            XmlObject file = files.get(filePath);
            try {
                this.writeFile(filePath, file);
            } catch (IOException e) {
                String msg = "Could not write file " + filePath + ":" + e.getMessage();
                log.error(msg);
                throw new BPELWorkflowEngineDeployFault(msg);
            } catch (JDOMException e) {
                String msg = "Could not parse xml source:" + e.getMessage();
                log.error(msg);
                throw new BPELWorkflowEngineDeployFault(msg);
            }
        }
    }

    private void writeFile(String target, XmlObject file) throws IOException, JDOMException {
        this.createTargetDirectoryForFile(target);
        XMLOutputter xmlOut = new XMLOutputter();
        xmlOut.setFormat(Format.getPrettyFormat());
        xmlOut.output(new SAXBuilder().build(new StringReader(file.xmlText())), new FileWriter(target));
    }

    private void createTargetDirectoryForFile(String file) throws IOException {
        String fileDirectory = file.substring(0, file.lastIndexOf(File.separator));
        File directory = new File(fileDirectory);
        if (!directory.exists()) {
            if (!directory.mkdirs()) {
                throw new IOException("Could not create directory : " + directory.getPath());
            }
        }
    }

    private void createJarArchives(List<String[]> archives) throws BPELWorkflowEngineDeployFault {
        for (String[] jarArchive : archives) {
            String archivePath = jarArchive[0];
            String directoryPath = jarArchive[1];
            try {
                FileUtils.createJarArchive(archivePath, directoryPath);
            } catch (IOException e) {
                String msg = "Could not create archive : " + archivePath;
                log.error(msg);
                throw new BPELWorkflowEngineDeployFault(msg);
            }
        }
    }

    public String getWorkflowEndpointUrl(ProcessDocument deploymentDescriptor, org.oasisOpen.docs.wsbpel.x20.process.executable.ProcessDocument bpelFile, WsdlFilesDocument wsdlFiles, XsdFilesDocument xsdFiles, OtherFilesDocument otherFiles, BPELWorkflowEngine engine) throws Exception {
        this.analyzeFiles(bpelFile, deploymentDescriptor, wsdlFiles, xsdFiles, otherFiles);
        String workflowEndpoint = null;
        QName serviceQName = null;
        QName partnerLinkQName = null;
        for (PartnerLinkXSDType pl : deploymentDescriptor.getProcess().getPartnerLinks().getPartnerLinkArray()) {
            partnerLinkQName = pl.getName();
            if (pl.getMyRoleArray().length >= 1) {
                serviceQName = pl.getMyRoleArray()[0].getService();
                break;
            }
        }
        if (serviceQName == null || partnerLinkQName == null) {
            String msg = ("Could not determine partner link with myRole element");
            log.error(msg);
            throw new Exception(msg);
        }
        if (!this.services.containsKey(serviceQName)) {
            String msg = ("Could not find service " + serviceQName);
            log.error(msg);
            throw new Exception(msg);
        }
        TService service = this.services.get(serviceQName);
        if (!this.bpelPartnerLinks.containsKey(partnerLinkQName)) {
            String msg = ("Could not find partner link in bpel process");
            log.error(msg);
            throw new Exception(msg);
        }
        String bpelPartnerLinkMyRole = this.bpelPartnerLinks.get(partnerLinkQName).getMyRole();
        String bpelPartnerLinkTypeName = this.bpelPartnerLinks.get(partnerLinkQName).getPartnerLinkType().getLocalPart();
        if (!this.partnerLinkTypesRolePortTypes.containsKey(bpelPartnerLinkTypeName + ":" + bpelPartnerLinkMyRole)) {
            String msg = ("Could not find port type in any partner link type for role " + bpelPartnerLinkMyRole);
            log.error(msg);
            throw new Exception(msg);
        }
        QName bpelPartnerLinkMyRolePortType = this.partnerLinkTypesRolePortTypes.get(bpelPartnerLinkTypeName + ":" + bpelPartnerLinkMyRole);
        if (!this.portTypeBindings.containsKey(bpelPartnerLinkMyRolePortType)) {
            String msg = ("Could not find binding for port type " + bpelPartnerLinkMyRolePortType);
            log.error(msg);
            throw new Exception(msg);
        }
        QName bpelPartnerLinkMyRolePortTypeBinding = this.portTypeBindings.get(bpelPartnerLinkMyRolePortType);
        for (TPort servicePort : service.getPortArray()) {
            if (servicePort.getBinding().equals(bpelPartnerLinkMyRolePortTypeBinding)) {
                XmlObject[] xmlObjects = servicePort.selectChildren(new QName(BISGridNamespaces.SOAP_NAMESPACE, "address"));
                if (xmlObjects.length >= 0) {
                    workflowEndpoint = xmlObjects[0].getDomNode().getAttributes().getNamedItem("location").getNodeValue();
                    break;
                }
            }
        }
        if (workflowEndpoint == null) {
            String msg = "Could not determine apprpriate port for binding " + bpelPartnerLinkMyRolePortTypeBinding + " in service " + serviceQName;
            log.error(msg);
            throw new Exception(msg);
        }
        return workflowEndpoint;
    }
}
