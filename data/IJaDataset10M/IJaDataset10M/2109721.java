package org.mobicents.slee.container.deployment.jboss;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.slee.resource.ConfigProperties;
import javax.slee.resource.ResourceAdaptorID;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import org.jboss.logging.Logger;
import org.mobicents.slee.container.deployment.jboss.action.ActivateResourceAdaptorEntityAction;
import org.mobicents.slee.container.deployment.jboss.action.BindLinkNameAction;
import org.mobicents.slee.container.deployment.jboss.action.CreateResourceAdaptorEntityAction;
import org.mobicents.slee.container.deployment.jboss.action.DeactivateResourceAdaptorEntityAction;
import org.mobicents.slee.container.deployment.jboss.action.ManagementAction;
import org.mobicents.slee.container.deployment.jboss.action.RemoveResourceAdaptorEntityAction;
import org.mobicents.slee.container.deployment.jboss.action.UnbindLinkNameAction;
import org.mobicents.slee.container.management.ResourceManagement;
import org.mobicents.slee.container.management.jmx.editors.ComponentIDPropertyEditor;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * Parses a deploy-config.xml file, creating management actions from its
 * content.
 * 
 * @author martins
 * 
 */
public class DeployConfigParser {

    private static Logger logger = Logger.getLogger(DeployConfigParser.class);

    private Map<String, Collection<ManagementAction>> postInstallActions;

    private Map<String, Collection<ManagementAction>> preUninstallActions;

    public DeployConfigParser(InputStream deployConfigInputStream, ResourceManagement resourceManagement) throws SAXException, ParserConfigurationException, IOException {
        parseDeployConfig(deployConfigInputStream, resourceManagement);
    }

    /**
	 * Parser for the deployment config xml.
	 * 
	 * @throws SAXException
	 * @throws ParserConfigurationException
	 * @throws IOException
	 * 
	 * @throws Exception
	 */
    private void parseDeployConfig(InputStream deployConfigInputStream, ResourceManagement resourceManagement) throws SAXException, ParserConfigurationException, IOException {
        if (deployConfigInputStream == null) {
            throw new NullPointerException("null deploy config input stream");
        }
        Document doc = null;
        try {
            SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = schemaFactory.newSchema(DeployConfigParser.class.getClassLoader().getResource("deploy-config.xsd"));
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setSchema(schema);
            DocumentBuilder builder = factory.newDocumentBuilder();
            builder.setErrorHandler(new ErrorHandler() {

                public void error(SAXParseException e) throws SAXException {
                    logger.error("Error parsing deploy-config.xml", e);
                    return;
                }

                public void fatalError(SAXParseException e) throws SAXException {
                    logger.error("Fatal error parsing deploy-config.xml", e);
                    return;
                }

                public void warning(SAXParseException e) throws SAXException {
                    logger.warn("Warning parsing deploy-config.xml", e);
                    return;
                }
            });
            doc = builder.parse(deployConfigInputStream);
        } finally {
            try {
                deployConfigInputStream.close();
            } catch (IOException e) {
                logger.error("failed to close deploy config input stream", e);
            }
        }
        Map<String, Collection<ManagementAction>> postInstallActions = new HashMap<String, Collection<ManagementAction>>();
        Map<String, Collection<ManagementAction>> preUninstallActions = new HashMap<String, Collection<ManagementAction>>();
        NodeList raEntities = doc.getElementsByTagName("ra-entity");
        String raId = null;
        Collection<ManagementAction> cPostInstallActions = new ArrayList<ManagementAction>();
        Collection<ManagementAction> cPreUninstallActions = new ArrayList<ManagementAction>();
        for (int i = 0; i < raEntities.getLength(); i++) {
            Element raEntity = (Element) raEntities.item(i);
            ComponentIDPropertyEditor cidpe = new ComponentIDPropertyEditor();
            cidpe.setAsText(raEntity.getAttribute("resource-adaptor-id"));
            raId = cidpe.getValue().toString();
            String entityName = raEntity.getAttribute("entity-name");
            NodeList propsNodeList = raEntity.getElementsByTagName("properties");
            if (propsNodeList.getLength() > 1) {
                logger.warn("Invalid ra-entity element, has more than one properties child. Reading only first.");
            }
            ConfigProperties props = new ConfigProperties();
            Element propsNode = (Element) propsNodeList.item(0);
            if (propsNode != null) {
                NodeList propsList = propsNode.getElementsByTagName("property");
                for (int j = 0; j < propsList.getLength(); j++) {
                    Element property = (Element) propsList.item(j);
                    String propertyName = property.getAttribute("name");
                    String propertyType = property.getAttribute("type");
                    String propertyValue = property.getAttribute("value");
                    props.addProperty(new ConfigProperties.Property(propertyName, propertyType, ConfigProperties.Property.toObject(propertyType, propertyValue)));
                }
            }
            cidpe.setAsText(raEntity.getAttribute("resource-adaptor-id"));
            ResourceAdaptorID componentID = (ResourceAdaptorID) cidpe.getValue();
            cPostInstallActions.add(new CreateResourceAdaptorEntityAction(componentID, entityName, props, resourceManagement));
            cPostInstallActions.add(new ActivateResourceAdaptorEntityAction(entityName, resourceManagement));
            NodeList links = raEntity.getElementsByTagName("ra-link");
            for (int j = 0; j < links.getLength(); j++) {
                String linkName = ((Element) links.item(j)).getAttribute("name");
                cPostInstallActions.add(new BindLinkNameAction(linkName, entityName, resourceManagement));
                cPreUninstallActions.add(new UnbindLinkNameAction(linkName, resourceManagement));
            }
            cPreUninstallActions.add(new DeactivateResourceAdaptorEntityAction(entityName, resourceManagement));
            cPreUninstallActions.add(new RemoveResourceAdaptorEntityAction(entityName, resourceManagement));
            if (raId != null) {
                if (postInstallActions.containsKey(raId)) {
                    postInstallActions.get(raId).addAll(cPostInstallActions);
                } else {
                    postInstallActions.put(raId, cPostInstallActions);
                }
                if (preUninstallActions.containsKey(raId)) {
                    preUninstallActions.get(raId).addAll(cPreUninstallActions);
                } else {
                    preUninstallActions.put(raId, cPreUninstallActions);
                }
            }
            cPostInstallActions = new ArrayList<ManagementAction>();
            cPreUninstallActions = new ArrayList<ManagementAction>();
            raId = null;
        }
        this.postInstallActions = Collections.unmodifiableMap(postInstallActions);
        this.preUninstallActions = Collections.unmodifiableMap(preUninstallActions);
    }

    public Map<String, Collection<ManagementAction>> getPostInstallActions() {
        return postInstallActions;
    }

    public Map<String, Collection<ManagementAction>> getPreUninstallActions() {
        return preUninstallActions;
    }
}
