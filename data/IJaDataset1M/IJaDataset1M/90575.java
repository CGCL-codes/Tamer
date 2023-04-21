package eu.soa4all.execution.mapping.tools.xml;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;
import eu.soa4all.execution.mapping.resources.AbstractService;
import eu.soa4all.execution.mapping.resources.ServiceOperation;
import eu.soa4all.execution.mapping.resources.ServiceParameter;
import eu.soa4all.execution.mapping.tools.utils.ResourcesUtils;

public class Sawsdl2AbstractService {

    static Logger log = Logger.getLogger(Sawsdl2AbstractService.class);

    private MyHandler myHandler;

    private List<ServiceParameter> serviceParameters;

    AbstractService abstractService;

    List<ServiceOperation> serviceOperations;

    public Sawsdl2AbstractService() {
        this.myHandler = new MyHandler();
        log.trace("new handler");
    }

    public AbstractService parseSawsdl(String sawsdlPath) {
        try {
            this.abstractService = new AbstractService("CreatedBySawsdl2AbstractService", sawsdlPath);
            SAXParserFactory factory = SAXParserFactory.newInstance();
            factory.setValidating(false);
            factory.setNamespaceAware(true);
            SAXParser saxParser = factory.newSAXParser();
            saxParser.parse(new File(sawsdlPath), this.myHandler);
            serviceParameters = this.myHandler.getSpsHandler();
            this.serviceOperations = ResourcesUtils.generateServiceOperations(serviceParameters);
            this.abstractService.setServiceOperations(serviceOperations);
            this.abstractService.setLocation(this.myHandler.getLocation());
            this.abstractService.setTargetNamespace(this.myHandler.getNameSpace());
            return this.abstractService;
        } catch (Exception e) {
            log.error("Error Files:" + sawsdlPath);
            e.printStackTrace();
            return null;
        }
    }

    static class MyHandler extends DefaultHandler {

        private String location;

        private String nameSpace;

        private List<ServiceParameter> serviceParameterListHandler;

        private ServiceParameter serviceParameterHandler;

        private boolean isTypes;

        private boolean isOutput;

        private int countElement;

        private boolean isElement;

        private boolean isComplex;

        private boolean isSimple;

        private ServiceParameter tmpServiceParameterHandler;

        private String nameRoot;

        private String modelReferenceRoot;

        private String sawsdlNameSpace;

        private boolean isPortType;

        private boolean isMessage;

        private boolean isService;

        private boolean isOperation;

        public void startDocument() {
            log.trace("Start Parsing");
            isTypes = false;
            countElement = 0;
            isElement = false;
            isComplex = false;
            isSimple = false;
            isOperation = false;
            isPortType = false;
            isMessage = false;
            isService = false;
            serviceParameterListHandler = new ArrayList<ServiceParameter>();
        }

        public void startElement(String uri, String localName, String qName, Attributes attributes) {
            log.trace("Element: " + qName);
            log.debug("uri: " + uri + " localName: " + localName + " qName: " + qName);
            if ((uri.equals("http://schemas.xmlsoap.org/wsdl/") || uri.equals("http://schemas.xmlsoap.org/wsdl")) && localName.equals("definitions")) {
                log.debug("attributes.getLength():" + attributes.getLength());
                String targetNamespace = attributes.getValue("", "targetNamespace");
                setNameSpace(targetNamespace);
                try {
                    for (int attributesIndex = 0; attributesIndex < attributes.getLength(); attributesIndex++) {
                        String xmlNameSpace = attributes.getValue(attributesIndex);
                        log.debug("xmlNameSpace: " + xmlNameSpace);
                        if (xmlNameSpace.equals("http://www.w3.org/ns/sawsdl")) sawsdlNameSpace = xmlNameSpace; else if (xmlNameSpace.equals("http://www.w3.org/ns/sawsdl/")) sawsdlNameSpace = xmlNameSpace;
                    }
                } catch (Exception ex) {
                    log.error("", ex);
                }
            }
            if ((uri.equals("http://schemas.xmlsoap.org/wsdl/") || uri.equals("http://schemas.xmlsoap.org/wsdl")) && localName.equals("types") || isTypes) {
                isTypes = true;
                log.trace("wsdl:types Element: " + qName);
                if ((uri.equals("http://www.w3.org/2001/XMLSchema/") || uri.equals("http://www.w3.org/2001/XMLSchema")) && localName.equals("element")) {
                    countElement += 1;
                }
                if (((uri.equals("http://www.w3.org/2001/XMLSchema/") || uri.equals("http://www.w3.org/2001/XMLSchema")) && localName.equals("element") || isElement) && !isComplex && !isSimple) {
                    isElement = true;
                    log.trace("xs:element: " + qName);
                    if (countElement == 1 && (uri.equals("http://www.w3.org/2001/XMLSchema/") || uri.equals("http://www.w3.org/2001/XMLSchema")) && localName.equals("element")) {
                        log.trace("Elemento xs:element 1 Livello: " + qName);
                        serviceParameterHandler = new ServiceParameter();
                        modelReferenceRoot = attributes.getValue("http://www.w3.org/ns/sawsdl/", "modelReference");
                        serviceParameterHandler.setModelReference(modelReferenceRoot);
                        nameRoot = attributes.getValue("name");
                        serviceParameterHandler.setName(nameRoot);
                        serviceParameterHandler.setNode(qName);
                        serviceParameterHandler.setIsLeaf(false);
                        serviceParameterListHandler.add(serviceParameterHandler);
                    }
                    if (countElement == 2 && (uri.equals("http://www.w3.org/2001/XMLSchema/") || uri.equals("http://www.w3.org/2001/XMLSchema")) && localName.equals("element")) {
                        log.trace("Elemento xs:element 2 Livello: " + qName);
                        serviceParameterHandler = new ServiceParameter();
                        serviceParameterHandler.setIsLeaf(true);
                        serviceParameterHandler.setNode(qName);
                        serviceParameterHandler.setNameRoot(nameRoot);
                        serviceParameterHandler.setModelReferenceRoot(modelReferenceRoot);
                        serviceParameterHandler.setModelReference(attributes.getValue("http://www.w3.org/ns/sawsdl/", "modelReference"));
                        serviceParameterHandler.setLiftingSchemaMapping(attributes.getValue("http://www.w3.org/ns/sawsdl/", "liftingSchemaMapping"));
                        serviceParameterHandler.setLoweringSchemaMapping(attributes.getValue("http://www.w3.org/ns/sawsdl/", "loweringSchemaMapping"));
                        serviceParameterHandler.setName(attributes.getValue("name"));
                        serviceParameterHandler.setType(attributes.getValue("type"));
                        serviceParameterHandler.setMinOccurs(attributes.getValue("minOccurs"));
                        serviceParameterHandler.setMaxOccurs(attributes.getValue("maxOccurs"));
                        serviceParameterListHandler.add(serviceParameterHandler);
                    }
                }
                if (((uri.equals("http://www.w3.org/2001/XMLSchema/") || uri.equals("http://www.w3.org/2001/XMLSchema")) && localName.equals("complexType") || isComplex) && !isElement && !isSimple) {
                    isComplex = true;
                    log.trace("xs:complexType: " + qName);
                    if ((uri.equals("http://www.w3.org/2001/XMLSchema/") || uri.equals("http://www.w3.org/2001/XMLSchema")) && localName.equals("complexType")) {
                        log.trace("Elemento xs:complexType: " + qName);
                        serviceParameterHandler = new ServiceParameter();
                        modelReferenceRoot = attributes.getValue("http://www.w3.org/ns/sawsdl/", "modelReference");
                        serviceParameterHandler.setModelReference(modelReferenceRoot);
                        nameRoot = attributes.getValue("name");
                        serviceParameterHandler.setName(nameRoot);
                        serviceParameterHandler.setNode(qName);
                        serviceParameterHandler.setIsLeaf(false);
                        serviceParameterListHandler.add(serviceParameterHandler);
                    }
                    if ((uri.equals("http://www.w3.org/2001/XMLSchema/") || uri.equals("http://www.w3.org/2001/XMLSchema")) && localName.equals("element")) {
                        log.trace("Elemento xs:element in xs:complexType: " + qName);
                        serviceParameterHandler = new ServiceParameter();
                        serviceParameterHandler.setIsLeaf(true);
                        serviceParameterHandler.setNode(qName);
                        serviceParameterHandler.setNameRoot(nameRoot);
                        serviceParameterHandler.setModelReferenceRoot(modelReferenceRoot);
                        serviceParameterHandler.setModelReference(attributes.getValue("http://www.w3.org/ns/sawsdl/", "modelReference"));
                        serviceParameterHandler.setLiftingSchemaMapping(attributes.getValue("http://www.w3.org/ns/sawsdl/", "liftingSchemaMapping"));
                        serviceParameterHandler.setLoweringSchemaMapping(attributes.getValue("http://www.w3.org/ns/sawsdl/", "loweringSchemaMapping"));
                        serviceParameterHandler.setName(attributes.getValue("name"));
                        serviceParameterHandler.setType(attributes.getValue("type"));
                        serviceParameterHandler.setMinOccurs(attributes.getValue("minOccurs"));
                        serviceParameterHandler.setMaxOccurs(attributes.getValue("maxOccurs"));
                        serviceParameterListHandler.add(serviceParameterHandler);
                    }
                }
                if (((uri.equals("http://www.w3.org/2001/XMLSchema/") || uri.equals("http://www.w3.org/2001/XMLSchema")) && localName.equals("simpleType") || isSimple) && !isElement && !isComplex) {
                    isSimple = true;
                    log.trace("xs:simpleType: " + qName);
                    if ((uri.equals("http://www.w3.org/2001/XMLSchema/") || uri.equals("http://www.w3.org/2001/XMLSchema")) && localName.equals("simpleType")) {
                        log.trace("Elemento xs:simpleType: " + qName);
                        serviceParameterHandler = new ServiceParameter();
                        modelReferenceRoot = attributes.getValue("http://www.w3.org/ns/sawsdl/", "modelReference");
                        serviceParameterHandler.setModelReference(modelReferenceRoot);
                        nameRoot = attributes.getValue("name");
                        serviceParameterHandler.setName(nameRoot);
                        serviceParameterHandler.setNode(qName);
                        serviceParameterHandler.setIsLeaf(false);
                        serviceParameterListHandler.add(serviceParameterHandler);
                    }
                    if ((uri.equals("http://www.w3.org/2001/XMLSchema/") || uri.equals("http://www.w3.org/2001/XMLSchema")) && localName.equals("element")) {
                        log.trace("Elemento xs:element in xs:simpleType: " + qName);
                        serviceParameterHandler = new ServiceParameter();
                        serviceParameterHandler.setIsLeaf(true);
                        serviceParameterHandler.setNode(qName);
                        serviceParameterHandler.setNameRoot(nameRoot);
                        serviceParameterHandler.setModelReferenceRoot(modelReferenceRoot);
                        serviceParameterHandler.setModelReference(attributes.getValue("http://www.w3.org/ns/sawsdl/", "modelReference"));
                        serviceParameterHandler.setLiftingSchemaMapping(attributes.getValue("http://www.w3.org/ns/sawsdl/", "liftingSchemaMapping"));
                        serviceParameterHandler.setLoweringSchemaMapping(attributes.getValue("http://www.w3.org/ns/sawsdl/", "loweringSchemaMapping"));
                        serviceParameterHandler.setName(attributes.getValue("name"));
                        serviceParameterHandler.setType(attributes.getValue("type"));
                        serviceParameterHandler.setMinOccurs(attributes.getValue("minOccurs"));
                        serviceParameterHandler.setMaxOccurs(attributes.getValue("maxOccurs"));
                        serviceParameterListHandler.add(serviceParameterHandler);
                    }
                }
            }
            if ((uri.equals("http://schemas.xmlsoap.org/wsdl/") || uri.equals("http://schemas.xmlsoap.org/wsdl")) && localName.equals("portType") || isPortType) {
                isPortType = true;
                log.trace("wsdl:portType Element: " + qName);
                if ((uri.equals("http://schemas.xmlsoap.org/wsdl/") || uri.equals("http://schemas.xmlsoap.org/wsdl")) && localName.equals("operation")) {
                    isOperation = true;
                    log.trace("Elemento wsdl:operation: " + qName);
                    serviceParameterHandler = new ServiceParameter();
                    serviceParameterHandler.setModelReference("");
                    nameRoot = attributes.getValue("name");
                    serviceParameterHandler.setName(nameRoot);
                    serviceParameterHandler.setNode(qName);
                    serviceParameterHandler.setIsLeaf(false);
                    tmpServiceParameterHandler = serviceParameterHandler;
                    serviceParameterListHandler.add(serviceParameterHandler);
                }
                if ((uri.equals("http://www.w3.org/ns/sawsdl/") || uri.equals("http://www.w3.org/ns/sawsdl")) && localName.equals("attrExtensions") && isOperation) {
                    log.trace("sawsdl:attrExtensions Element: " + qName);
                    modelReferenceRoot = attributes.getValue("http://www.w3.org/ns/sawsdl/", "modelReference");
                    int tmpPosition = serviceParameterListHandler.indexOf(tmpServiceParameterHandler);
                    tmpServiceParameterHandler = serviceParameterListHandler.remove(tmpPosition);
                    tmpServiceParameterHandler.setModelReference(modelReferenceRoot);
                    serviceParameterListHandler.add(tmpServiceParameterHandler);
                }
                if ((uri.equals("http://schemas.xmlsoap.org/wsdl/") || uri.equals("http://schemas.xmlsoap.org/wsdl")) && localName.equals("input")) {
                    log.trace("Elemento xs:element in wsdl:input: " + qName);
                    serviceParameterHandler = new ServiceParameter();
                    serviceParameterHandler.setIsLeaf(true);
                    serviceParameterHandler.setNode(qName);
                    serviceParameterHandler.setNameRoot(nameRoot);
                    serviceParameterHandler.setModelReferenceRoot(modelReferenceRoot);
                    serviceParameterHandler.setName(attributes.getValue("message"));
                    serviceParameterListHandler.add(serviceParameterHandler);
                }
                if ((uri.equals("http://schemas.xmlsoap.org/wsdl/") || uri.equals("http://schemas.xmlsoap.org/wsdl")) && localName.equals("output")) {
                    log.trace("Elemento xs:element in wsdl:output: " + qName);
                    serviceParameterHandler = new ServiceParameter();
                    serviceParameterHandler.setIsLeaf(true);
                    serviceParameterHandler.setNode(qName);
                    serviceParameterHandler.setNameRoot(nameRoot);
                    serviceParameterHandler.setModelReferenceRoot(modelReferenceRoot);
                    serviceParameterHandler.setOutput(true);
                    serviceParameterHandler.setName(attributes.getValue("message"));
                    serviceParameterListHandler.add(serviceParameterHandler);
                }
            }
            if ((uri.equals("http://schemas.xmlsoap.org/wsdl/") || uri.equals("http://schemas.xmlsoap.org/wsdl")) && localName.equals("message") || isMessage) {
                isMessage = true;
                log.trace("wsdl:message Element: " + qName);
                if ((uri.equals("http://schemas.xmlsoap.org/wsdl/") || uri.equals("http://schemas.xmlsoap.org/wsdl")) && localName.equals("message")) {
                    log.trace("Elemento wsdl:message: " + qName);
                    serviceParameterHandler = new ServiceParameter();
                    nameRoot = attributes.getValue("name");
                    serviceParameterHandler.setName(nameRoot);
                    serviceParameterHandler.setNode(qName);
                    serviceParameterHandler.setIsLeaf(false);
                    serviceParameterListHandler.add(serviceParameterHandler);
                }
                if ((uri.equals("http://schemas.xmlsoap.org/wsdl/") || uri.equals("http://schemas.xmlsoap.org/wsdl")) && localName.equals("part")) {
                    log.trace("Elemento xs:element in wsdl:part: " + qName);
                    serviceParameterHandler = new ServiceParameter();
                    serviceParameterHandler.setIsLeaf(true);
                    serviceParameterHandler.setNode(qName);
                    serviceParameterHandler.setNameRoot(nameRoot);
                    serviceParameterHandler.setName(attributes.getValue("element"));
                    serviceParameterListHandler.add(serviceParameterHandler);
                }
            }
            if ((uri.equals("http://schemas.xmlsoap.org/wsdl/") || uri.equals("http://schemas.xmlsoap.org/wsdl")) && localName.equals("service") || isService) {
                isService = true;
                log.trace("wsdl:service Element: " + qName);
                if ((uri.equals("http://schemas.xmlsoap.org/wsdl/") || uri.equals("http://schemas.xmlsoap.org/wsdl")) && localName.equals("service")) {
                    log.trace("Elemento wsdl:service " + qName);
                    serviceParameterHandler = new ServiceParameter();
                    nameRoot = attributes.getValue("name");
                    serviceParameterHandler.setName(nameRoot);
                    serviceParameterHandler.setNode(qName);
                    serviceParameterHandler.setIsLeaf(false);
                    serviceParameterListHandler.add(serviceParameterHandler);
                }
                if (uri.equals("http://schemas.xmlsoap.org/wsdl/soap/") && localName.equals("address")) {
                    log.trace("Elemento xs:element in wsdl:part: " + qName);
                    serviceParameterHandler = new ServiceParameter();
                    serviceParameterHandler.setIsLeaf(true);
                    serviceParameterHandler.setNode(qName);
                    serviceParameterHandler.setNameRoot(nameRoot);
                    setLocation(attributes.getValue("location"));
                    log.debug("---------------------------");
                    log.debug("Location:" + attributes.getValue("location"));
                    log.debug("---------------------------");
                    serviceParameterListHandler.add(serviceParameterHandler);
                }
            }
        }

        public void endElement(String uri, String localName, String qName) {
            log.trace("</" + qName + ">");
            if (qName == "xs:element" & isElement & !isComplex & !isSimple) {
                if (countElement == 1) {
                    isElement = false;
                }
            }
            if (qName == "xs:element") {
                countElement -= 1;
            }
            if (qName == "xs:complexType" & isComplex & !isElement & !isSimple) {
                isComplex = false;
            }
            if (qName == "xs:simpleType" & isSimple & !isElement & !isComplex) {
                isSimple = false;
            }
            if (qName == "wsdl:types") {
                isTypes = false;
                log.trace("CLOSE wsdl:types Element: " + qName);
            }
            if (qName == "wsdl:message") {
                isMessage = false;
                log.trace("CLOSE wsdl:message Element: " + qName);
            }
            if (qName == "wsdl:portType") {
                isPortType = false;
                log.trace("CLOSE wsdl:portType Element: " + qName);
            }
            if (qName == "wsdl:operation") {
                isOperation = false;
                log.trace("CLOSE wsdl:portType Element: " + qName);
            }
        }

        public void endDocument() {
            log.trace("End Parsing");
        }

        /**
		 * @return the serviceParameters
		 */
        public List<ServiceParameter> getSpsHandler() {
            return this.serviceParameterListHandler;
        }

        /**
		 * @return the location
		 */
        public String getLocation() {
            return this.location;
        }

        /**
		 * @param location the location to set
		 */
        public void setLocation(String location) {
            this.location = location;
        }

        /**
		 * @return the isOutput
		 */
        public boolean isOutput() {
            return isOutput;
        }

        /**
		 * @param isOutput the isOutput to set
		 */
        public void setOutput(boolean isOutput) {
            this.isOutput = isOutput;
        }

        /**
		 * @return the nameSpace
		 */
        public String getNameSpace() {
            return nameSpace;
        }

        /**
		 * @param nameSpace the nameSpace to set
		 */
        public void setNameSpace(String nameSpace) {
            this.nameSpace = nameSpace;
        }
    }

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        BasicConfigurator.configure();
        log.setLevel(Level.INFO);
        String fileTest = "testFiles\\input\\v1.0TheWeather.sawsdl";
        try {
            Sawsdl2AbstractService s2aTest = new Sawsdl2AbstractService();
            AbstractService asTest;
            asTest = s2aTest.parseSawsdl(fileTest);
            log.debug("NAME: " + asTest.getName());
            log.debug("SAWSDL: " + asTest.getSawsdlPath());
            asTest.toXMLTest();
        } catch (Exception e) {
            log.error("main SawsdlParser");
        }
    }
}
