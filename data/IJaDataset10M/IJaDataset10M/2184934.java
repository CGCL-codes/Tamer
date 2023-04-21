package simis.parser;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.*;
import org.xml.sax.*;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import simis.appLayer.slaAgents.lib.serviceDescriptionDocuments.*;

public class Parser {

    private HashMap<String, ArrayList<Node>> node = new HashMap<String, ArrayList<Node>>();

    /** 
	  * Constructor 
	  */
    public Parser() {
    }

    /**
	  * Method for parsing an XML file, with validation
	  * 
	  * TODO: parse xsdFile for validation from xmlFile
	  * 		 -> only xmlFile required as an Argument,
	  * 	  
	  * @param String xmlFile /home/user/xml/example.xsd 
	  * 	   String xsdFile /home/user/xsd/example.xsd  use / as seperator
	  * @throws SAXException, IOException, ParserConfigurationException 
	  */
    public <T> T parse(String xmlFile, String xsdFile) throws SAXException, IOException, ParserConfigurationException {
        validate(xmlFile, xsdFile);
        return parseNoVal(xmlFile, xsdFile);
    }

    /**
	  * Method for parsing an XML file, without validation
	  * 
	  * TODO: parse xsdFile for validation from xmlFile
	  * 		 -> only xmlFile required as an Argument,
	
	  * @param String xmlFile /home/user/xml/example.xsd 
	  * 	   String xsdFile /home/user/xsd/example.xsd  use / as seperator
	  * @throws SAXException, IOException, ParserConfigurationException 
	  */
    public <T> T parseNoVal(String xmlFile, String xsdFile) throws SAXException, IOException, ParserConfigurationException {
        String arrXsd[];
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(new File(xmlFile));
        arrXsd = xsdFile.split("/");
        if (arrXsd.length > 0) xsdFile = arrXsd[(arrXsd.length - 1)];
        System.out.println(xsdFile);
        if (xsdFile.equals("ExtendedSLATemplateSchema.xsd")) {
            return (T) extendedSLATemplateSchema(document);
        } else if (xsdFile.equals("ServiceIdentificatorSchema.xsd")) {
            return (T) serviceIdentificatorSchema(document);
        } else if (xsdFile.equals("ServiceTypeSchema.xsd")) {
            return (T) serviceTypeSchema(document);
        } else {
        }
        return null;
    }

    /**
	 * helper method for getting a List with all relevant Node(s) 
	 * 
	 * @param document  the given xml document (dom)
	 * @param parentTag the Tag of the relevant Nodes Parent
	 * @param nodeTag the Tag of the relevant Node
	 * @return all matching Nodes
	 */
    private ArrayList<Node> getElement(Document document, String parentTag, String nodeTag) {
        ArrayList<Node> ndList = new ArrayList<Node>();
        NodeList tmpNdList = document.getElementsByTagName(nodeTag);
        for (int i = 0; i < tmpNdList.getLength(); i++) {
            if (tmpNdList.item(i).getParentNode().getNodeName() == parentTag) {
                ndList.add(tmpNdList.item(i));
            }
        }
        return ndList;
    }

    /**
	 * reading the content from dom-tree and generating returning an ExtendedSLATemplate Object
	 * @param document
	 */
    private ExtendedSLATemplate extendedSLATemplateSchema(Document document) {
        HashMap<String, SLO> sloSet = new HashMap<String, SLO>();
        HashMap<String, NegotiableSLO> negotiableSLOs = new HashMap<String, NegotiableSLO>();
        RoleContext serviceProvider;
        RoleContext serviceConsumer;
        List<String> generalRestrictions = new Vector<String>();
        List<AttributeRestriction> attributeRestrictions;
        List<ProtocolStep> serviceConsumerProcess = new Vector<ProtocolStep>();
        boolean defined;
        String matchingrule;
        String negotiationTransparency;
        String negotiationContent;
        String statusTransparency;
        String statusContent;
        String slaTemplateID;
        node.clear();
        node.put("slaTemplateID", getElement(document, "ExtendedSLATemplate", "slaTemplateID"));
        node.put("SLO", getElement(document, "slaTemplate", "SLO"));
        node.put("serviceProvider", getElement(document, "context", "serviceProvider"));
        node.put("serviceConsumer", getElement(document, "context", "serviceConsumer"));
        node.put("negotiableSLO", getElement(document, "negotiationObject", "negotiableSLO"));
        node.put("generalRestriction", getElement(document, "offerRestrictions", "generalRestriction"));
        node.put("attributeRestriction", getElement(document, "offerRestrictions", "attributeRestriction"));
        node.put("offerAllocationPolicy", getElement(document, "negotiationProtocol", "offerAllocationPolicy"));
        node.put("negotiationTransparency", getElement(document, "informationPolicy", "negotiationTransparency"));
        node.put("negotiationContent", getElement(document, "informationPolicy", "negotiationContent"));
        node.put("statusTransparency", getElement(document, "informationPolicy", "statusTransparency"));
        node.put("statusContent", getElement(document, "informationPolicy", "statusContent"));
        node.put("process_serviceConsumer", getElement(document, "process", "serviceConsumer"));
        sloSet = getSloSet();
        serviceProvider = generateRoleContext("serviceProvider");
        serviceConsumer = generateRoleContext("serviceConsumer");
        negotiableSLOs = getNegotiableSLOs();
        generalRestrictions = getGeneralRestrictions();
        attributeRestrictions = getAttributeRestrictions();
        matchingrule = node.get("offerAllocationPolicy").get(0).getChildNodes().item(1).getFirstChild().getNodeValue();
        defined = getDefined();
        negotiationTransparency = getValue("negotiationTransparency");
        negotiationContent = getValue("negotiationContent");
        statusTransparency = getValue("statusTransparency");
        statusContent = getValue("statusContent");
        serviceConsumerProcess = getServiceConsumerProcess();
        slaTemplateID = getValue("slaTemplateID");
        return new ExtendedSLATemplate(slaTemplateID, new SLATemplate(sloSet), new NegotiationProtocol(new Context(serviceProvider, serviceConsumer), new NegotiationObject(negotiableSLOs), new OfferRestrictions(generalRestrictions, attributeRestrictions), new OfferAllocationPolicy(defined, matchingrule), new InformationPolicy(negotiationTransparency, negotiationContent, statusTransparency, statusContent), new NegotiationProcess(serviceConsumerProcess)));
    }

    /**
     * helper method for NegotiationProcess(serviceConsumerProcess)
     * @return
     */
    private List<ProtocolStep> getServiceConsumerProcess() {
        EventActionType event = null;
        List<ProtocolStep> serviceConsumerProcess = new Vector<ProtocolStep>();
        List<EventActionType> possibleActions = new Vector<EventActionType>();
        String messageType = null;
        String from = null;
        String to = null;
        for (Node i : node.get("process_serviceConsumer")) {
            for (int j = 0; j < i.getChildNodes().getLength(); j++) {
                if (i.getChildNodes().item(j).getNodeName().equals("protocolStep")) {
                    for (int m = 0; m < i.getChildNodes().item(j).getChildNodes().getLength(); m++) {
                        if (i.getChildNodes().item(j).getChildNodes().item(m).getNodeName().equals("event") || i.getChildNodes().item(j).getChildNodes().item(m).getNodeName().equals("possibleAction")) {
                            for (int n = 0; n < i.getChildNodes().item(j).getChildNodes().item(m).getChildNodes().getLength(); n++) {
                                if (i.getChildNodes().item(j).getChildNodes().item(m).getChildNodes().item(n).getNodeName().equals("messageType")) {
                                    messageType = i.getChildNodes().item(j).getChildNodes().item(m).getChildNodes().item(n).getFirstChild().getNodeValue();
                                }
                            }
                            for (int n = 0; n < i.getChildNodes().item(j).getChildNodes().item(m).getAttributes().getLength(); n++) {
                                if (i.getChildNodes().item(j).getChildNodes().item(m).getAttributes().item(n).getNodeName().equals("from")) {
                                    from = i.getChildNodes().item(j).getChildNodes().item(m).getAttributes().item(n).getNodeValue();
                                } else if (i.getChildNodes().item(j).getChildNodes().item(m).getAttributes().item(n).getNodeName().equals("to")) {
                                    to = i.getChildNodes().item(j).getChildNodes().item(m).getAttributes().item(n).getNodeValue();
                                }
                            }
                            if (i.getChildNodes().item(j).getChildNodes().item(m).getNodeName().equals("event")) {
                                event = new EventActionType(messageType, from, to);
                            } else {
                                possibleActions.add(new EventActionType(messageType, from, to));
                            }
                        }
                    }
                }
            }
            serviceConsumerProcess.add(new ProtocolStep(event, possibleActions));
        }
        return serviceConsumerProcess;
    }

    /**
     *  helper method for getting defined value in offerAllocationPolicy
     * 
     * @return
     */
    private boolean getDefined() {
        boolean defined;
        if (node.get("offerAllocationPolicy").get(0).getAttributes().item(0).getNodeValue().equals("defined")) {
            defined = true;
        } else {
            defined = false;
        }
        return defined;
    }

    /**
     * helper-method for generalRestrictions
     * @return
     */
    private List<String> getGeneralRestrictions() {
        List<String> generalRestrictions = new Vector<String>();
        for (Node i : node.get("generalRestriction")) {
            generalRestrictions.add(i.getFirstChild().getNodeValue());
        }
        return generalRestrictions;
    }

    /**
     * helper method for NegotiableSLOs
     * @return Hashmap <String, NegotianleSLO>
     */
    private HashMap<String, NegotiableSLO> getNegotiableSLOs() {
        HashMap<String, NegotiableSLO> negotiableSLOs = new HashMap<String, NegotiableSLO>();
        boolean multiple;
        for (Node i : node.get("negotiableSLO")) {
            if (i.getFirstChild().getNodeValue().equals("multiple")) {
                multiple = true;
            } else {
                multiple = false;
            }
            negotiableSLOs.put(i.getAttributes().item(0).getNodeValue(), new NegotiableSLO(i.getAttributes().item(0).getNodeValue(), multiple));
        }
        return negotiableSLOs;
    }

    /**
     * helper method for getting SLOSet for SLATemplate
     * @return
     */
    private HashMap<String, SLO> getSloSet() {
        HashMap<String, SLO> sloSet = new HashMap<String, SLO>();
        for (Node i : node.get("SLO")) {
            sloSet.put(i.getAttributes().item(0).getNodeValue(), new SLO(i.getAttributes().item(0).getNodeValue(), i.getChildNodes().item(1).getFirstChild().getNodeValue()));
        }
        return sloSet;
    }

    /**
   	 * helper method for getting the value of given nodeTag
   	 * @param nodeTag
   	 * @return String 
   	 */
    private String getValue(String nodeTag) {
        return node.get(nodeTag).get(0).getFirstChild().getNodeValue();
    }

    /**
	 * helper method for AtrributeRestrictions
	 * @return
	 */
    private List<AttributeRestriction> getAttributeRestrictions() {
        String propertyID = null;
        String thresholdLowerBound = null;
        String thresholdUpperBound = null;
        String progressForm = null;
        String progressDelta = null;
        List<String> restrictionRules = new Vector<String>();
        List<AttributeRestriction> attributeRestrictions = new Vector<AttributeRestriction>();
        for (Node i : node.get("attributeRestriction")) {
            propertyID = i.getAttributes().item(0).getFirstChild().getNodeValue();
            for (int j = 0; j < i.getChildNodes().getLength(); j++) {
                if (i.getChildNodes().item(j).getNodeName().equals("progress")) {
                    for (int n = 0; n < i.getChildNodes().item(j).getChildNodes().getLength(); n++) {
                        if (i.getChildNodes().item(j).getChildNodes().item(n).getNodeName().equals("progressForm")) {
                            progressForm = i.getChildNodes().item(j).getChildNodes().item(n).getFirstChild().getNodeValue();
                        } else if (i.getChildNodes().item(j).getChildNodes().item(n).getNodeName().equals("delta")) {
                            progressDelta = i.getChildNodes().item(j).getChildNodes().item(n).getFirstChild().getNodeValue();
                        } else {
                        }
                    }
                } else if (i.getChildNodes().item(j).getNodeName().equals("threshold")) {
                    for (int n = 0; n < i.getChildNodes().item(j).getChildNodes().getLength(); n++) {
                        if (i.getChildNodes().item(j).getChildNodes().item(n).getNodeName().equals("lowerBound")) {
                            thresholdLowerBound = i.getChildNodes().item(j).getChildNodes().item(n).getFirstChild().getNodeValue();
                        } else if (i.getChildNodes().item(j).getChildNodes().item(n).getNodeName().equals("upperBound")) {
                            thresholdUpperBound = i.getChildNodes().item(j).getChildNodes().item(n).getFirstChild().getNodeValue();
                        } else {
                        }
                    }
                } else if (i.getChildNodes().item(j).getNodeName() == "restrictionRule") {
                    restrictionRules.add(i.getChildNodes().item(j).getFirstChild().getNodeValue());
                } else {
                }
            }
            attributeRestrictions.add(new AttributeRestriction(propertyID, thresholdLowerBound, thresholdUpperBound, progressForm, progressDelta, restrictionRules));
        }
        return attributeRestrictions;
    }

    /** helper method for genrating Role Context
     * 
     * @param string
     * @return RoleContext  Object depending on given String
     */
    private RoleContext generateRoleContext(String role) {
        boolean restricted = false;
        int maxAgent = 0;
        int minAgent = 0;
        String restrictionRule = null;
        for (int i = 0; i < node.get(role).get(0).getChildNodes().getLength(); i++) {
            if (node.get(role).get(0).getChildNodes().item(i).getNodeName().equals("maximumNumberOfAgents")) {
                maxAgent = Integer.parseInt(node.get(role).get(0).getChildNodes().item(i).getFirstChild().getNodeValue());
            } else if (node.get(role).get(0).getChildNodes().item(i).getNodeName().equals("minimumNumberOfAgents")) {
                minAgent = Integer.parseInt(node.get(role).get(0).getChildNodes().item(i).getFirstChild().getNodeValue());
            } else if (node.get(role).get(0).getChildNodes().item(i).getNodeName().equals("admissionRestriction")) {
                if (node.get(role).get(0).getChildNodes().item(i).getAttributes().item(0).getNodeValue().equals("open")) {
                    restricted = true;
                } else {
                    restricted = false;
                }
                restrictionRule = node.get(role).get(0).getChildNodes().item(i).getChildNodes().item(1).getFirstChild().getNodeValue();
            } else {
            }
        }
        return new RoleContext(minAgent, maxAgent, restricted, restrictionRule);
    }

    /**
   	 * reading the content from dom-tree and generating  ServiceIdentificator object
   	 * @param document
   	 * @return
   	 * @throws DOMException
   	 * @throws MalformedURLException
   	 */
    private ServiceIdentificator serviceIdentificatorSchema(Document document) throws DOMException, MalformedURLException {
        node.clear();
        node.put("serviceID", getElement(document, "ServiceIdentificator", "serviceID"));
        node.put("serviceTypeID", getElement(document, "ServiceIdentificator", "serviceTypeID"));
        node.put("slaTemplateID", getElement(document, "ServiceIdentificator", "slaTemplateID"));
        node.put("wsdlFile", getElement(document, "ServiceIdentificator", "wsdlFile"));
        node.put("negotiationCoordinator", getElement(document, "ServiceIdentificator", "negotiationCoordinator"));
        node.put("serviceProvider", getElement(document, "ServiceIdentificator", "serviceProvider"));
        return new ServiceIdentificator(getValue("serviceID"), getValue("serviceTypeID"), getValue("slaTemplateID"), new URL(getValue("wsdlFile")), getValue("negotiationCoordinator"), getValue("serviceProvider"));
    }

    /**
	 * reading the content from dom-tree and generating returning a ServiceType Object
	 * @param document
	 * @return
	 */
    private ServiceType serviceTypeSchema(Document document) {
        node.clear();
        node.put("serviceTypeID", getElement(document, "ServiceType", "serviceTypeID"));
        node.put("serviceDescription", getElement(document, "ServiceType", "serviceDescription"));
        node.put("property", getElement(document, "ServiceType", "property"));
        return new ServiceType(getValue("serviceTypeID"), getValue("serviceDescription"), generateProperties());
    }

    /** 
	 * helper Method for generating a Property Hash for ServiceType Object
	 * 
	 * @return Property Hash
	 */
    private HashMap<String, Property> generateProperties() {
        HashMap<String, Property> properties = new HashMap<String, Property>();
        String property = null;
        String domain = null;
        String declaration = null;
        for (Node i : node.get("property")) {
            property = i.getAttributes().item(0).getNodeValue();
            for (int j = 0; j < i.getChildNodes().getLength(); j++) {
                if (i.getChildNodes().item(j).getNodeName().equals("domain")) {
                    domain = i.getChildNodes().item(j).getFirstChild().getNodeValue();
                } else if (i.getChildNodes().item(j).getNodeName().equals("declaration")) {
                    declaration = i.getChildNodes().item(j).getFirstChild().getNodeValue();
                } else {
                }
            }
            properties.put(property, new Property(property, domain, URI.create(declaration)));
        }
        return properties;
    }

    /** 
	 * Validation method checks the given xmlFile.
	 * 
	 * @param String xmlFile, String xsdFile
	 * @throws SAXException, IOException
	 */
    private void validate(String xmlFile, String xsdFile) throws SAXException, IOException {
        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = schemaFactory.newSchema(new File(xsdFile));
        Validator validator = schema.newValidator();
        validator.setErrorHandler(new XsdValidationLoggingErrorHandler());
        validator.validate(new StreamSource(new File(xmlFile)));
    }
}
