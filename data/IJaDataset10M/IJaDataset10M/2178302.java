package com.wm.wmsopera.registry;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.sopera.administration.AdminFacade;
import org.sopera.metadata.Endpoint;
import org.sopera.metadata.EndpointBuilder;
import org.sopera.metadata.InteractionStyle;
import org.sopera.metadata.Operation;
import org.sopera.metadata.OperationBuilder;
import org.sopera.metadata.Service;
import org.sopera.metadata.ServiceBuilder;
import org.sopera.metadata.ServiceProvider;
import org.sopera.metadata.ServiceProviderBuilder;
import org.sopera.metadata.impl.WsdlFlavor;
import org.sopera.util.DomUtils;
import org.sopware.papi.SBBFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import com.wm.adk.log.ARTLogger;
import com.wm.app.b2b.server.ServerAPI;
import com.wm.app.b2b.server.ns.Namespace;
import com.wm.data.IData;
import com.wm.data.IDataCursor;
import com.wm.data.IDataFactory;
import com.wm.lang.ns.NSField;
import com.wm.lang.ns.NSName;
import com.wm.lang.ns.NSNode;
import com.wm.lang.ns.NSRecord;
import com.wm.lang.ns.NSService;
import com.wm.lang.ns.NSSignature;
import com.wm.lang.wsdl.WSDException;
import com.wm.wmsopera.WmSOPERA;
import com.wm.wmsopera.WmSOPERAConstants;
import com.wm.wmsopera.util.SimpleIDataUtil;
import com.wm.xsd.coder.XSDCoderException;
import com.wm.xsd.mapper.XSDMapperException;
import com.wm.xsd.util.CreateXSD;

public class SOPERAGenerator implements WmSOPERAConstants {

    public static void registerOnewaySOPERAService(AdminFacade adminFacade, String currentServiceName, String currentOperationName, String nodeName) {
        try {
            String localName = nodeName.substring(nodeName.lastIndexOf(':') + 1);
            QName qNameCurrentService = QName.valueOf(currentServiceName);
            List<Document> xsdDocuments = xsdForIsNode(nodeName);
            Document rootXsdDoc = xsdDocuments.remove(0);
            Element schemaElement = (Element) rootXsdDoc.getChildNodes().item(0);
            String targetNamespace = qNameCurrentService.getNamespaceURI() + "/documents";
            schemaElement.setAttribute("targetNamespace", targetNamespace);
            schemaElement.setAttribute("xmlns", targetNamespace);
            schemaElement.setAttribute("elementFormDefault", "unqualified");
            Element inputMessageElement = rootXsdDoc.createElementNS("http://www.w3.org/2001/XMLSchema", "xsd:element");
            inputMessageElement.setAttribute("name", localName);
            inputMessageElement.setAttribute("type", localName);
            inputMessageElement = (Element) schemaElement.appendChild(inputMessageElement);
            OperationBuilder operationBuilder = OperationBuilder.operation().named(currentOperationName).withStyle(InteractionStyle.ONEWAY).withRequestSchema(inputMessageElement);
            ServiceBuilder serviceBuilder = ServiceBuilder.service().named(qNameCurrentService);
            for (Document xsd : xsdDocuments) {
                serviceBuilder.withSchema(xsd.getDocumentElement());
            }
            serviceBuilder.add(operationBuilder.build());
            adminFacade.registerService(serviceBuilder.build());
        } catch (Exception e) {
            logException(e);
            WmSOPERA.getInstance().createAdapterException(GENERIC_SOPERA_EXCEPTION, e);
        }
    }

    /**
	 * Create a new SOPERA service with exactly one operation that wraps an existing 
	 * IS service. The service description of the SOPERA service will mirror the 
	 * IS service's signature. 
	 * 
	 * @param adminFacade
	 * @param currentServiceName
	 * @param currentOperationName
	 * @param currentISName
	 * @param usePlainXml 
	 */
    public static void registerSOPERAService(AdminFacade adminFacade, String currentServiceName, String currentOperationName, String currentISName, boolean usePlainXml) {
        try {
            String localServiceName = currentISName.substring(currentISName.lastIndexOf(':') + 1);
            QName qNameCurrentService = QName.valueOf(currentServiceName);
            String targetNamespace = qNameCurrentService.getNamespaceURI() + "/documents";
            List<Document> xsdDocuments;
            if (usePlainXml) {
                xsdDocuments = new ArrayList<Document>();
                xsdDocuments.add(staticXsdForPlainXml(targetNamespace));
            } else {
                xsdDocuments = xsdForIsNode(currentISName);
            }
            Document rootXsdDocument = xsdDocuments.remove(0);
            Element schemaElement = (Element) rootXsdDocument.getChildNodes().item(0);
            String inputName = getParameterName(currentISName, localServiceName, true);
            String outputName = getParameterName(currentISName, localServiceName, false);
            schemaElement.setAttribute("targetNamespace", targetNamespace);
            schemaElement.setAttribute("xmlns", targetNamespace);
            schemaElement.setAttribute("elementFormDefault", "unqualified");
            Element inputMessageElement = null;
            Element outputMessageElement = null;
            NodeList elements = schemaElement.getChildNodes();
            for (int i = 0; i < elements.getLength(); i++) {
                Node n = elements.item(i);
                if (!(n instanceof Element)) continue;
                Element ele = (Element) n;
                if (!"http://www.w3.org/2001/XMLSchema".equals(ele.getNamespaceURI()) || !"element".equals(ele.getLocalName())) continue;
                String elementName = ele.getAttribute("name");
                if (inputMessageElement == null && inputName.equals(elementName)) {
                    inputMessageElement = ele;
                }
                if (outputName != null && outputMessageElement == null && outputName.equals(elementName)) {
                    outputMessageElement = ele;
                }
            }
            if (inputMessageElement == null) {
                inputMessageElement = rootXsdDocument.createElementNS("http://www.w3.org/2001/XMLSchema", "xsd:element");
                String localName;
                if (inputName.contains(":")) {
                    localName = inputName.split(":")[1];
                } else {
                    localName = inputName;
                }
                inputMessageElement.setAttribute("name", localName);
                inputMessageElement.setAttribute("type", inputName);
                inputMessageElement = (Element) schemaElement.appendChild(inputMessageElement);
            }
            if (!usePlainXml && (outputName == null || isEmptyMessageSchema(schemaElement, outputName))) {
                OperationBuilder operationBuilder = OperationBuilder.operation().named(currentOperationName).withStyle(InteractionStyle.ONEWAY).withRequestSchema(inputMessageElement);
                ServiceBuilder serviceBuilder = ServiceBuilder.service().named(qNameCurrentService);
                for (Document xsd : xsdDocuments) {
                    serviceBuilder.withSchema(xsd.getDocumentElement());
                }
                serviceBuilder.add(operationBuilder.build());
                adminFacade.registerService(serviceBuilder.build());
            } else {
                if (outputMessageElement == null) {
                    outputMessageElement = rootXsdDocument.createElementNS("http://www.w3.org/2001/XMLSchema", "xsd:element");
                    String localName;
                    if (outputName.contains(":")) {
                        localName = outputName.split(":")[1];
                    } else {
                        localName = outputName;
                    }
                    outputMessageElement.setAttribute("name", localName);
                    outputMessageElement.setAttribute("type", outputName);
                    outputMessageElement = (Element) schemaElement.appendChild(outputMessageElement);
                }
                OperationBuilder operationBuilder = OperationBuilder.operation().named(currentOperationName).withStyle(InteractionStyle.REQUEST_RESPONSE).withRequestSchema(inputMessageElement).withResponseSchema(outputMessageElement);
                ServiceBuilder serviceBuilder = ServiceBuilder.service().named(qNameCurrentService);
                for (Document xsd : xsdDocuments) {
                    serviceBuilder.withSchema(xsd.getDocumentElement());
                }
                Operation op = operationBuilder.build();
                serviceBuilder.add(op);
                Service service = serviceBuilder.build();
                adminFacade.registerService(service);
            }
        } catch (Exception e) {
            logException(e);
            WmSOPERA.getInstance().createAdapterException(GENERIC_SOPERA_EXCEPTION, e);
        }
    }

    private static String getParameterName(String currentISName, String localServiceName, boolean isInput) {
        NSService nsService = (NSService) Namespace.current().getNode(NSName.create(currentISName));
        NSSignature sig = nsService.getSignature();
        NSRecord parameterRecord;
        if (isInput) {
            parameterRecord = sig.getInput();
        } else {
            parameterRecord = sig.getOutput();
        }
        if (parameterRecord == null) return null;
        if (parameterRecord.getFieldCount() == 1) {
            return parameterRecord.getField(0).getName();
        } else if (parameterRecord.getFieldCount() == 2 && parameterRecord.getFieldByName("debug") != null) {
            NSField[] fields = parameterRecord.getFields();
            for (NSField field : fields) {
                if (!"debug".equals(field.getName())) return field.getName();
            }
            return null;
        } else if (parameterRecord.getFieldCount() == 0) {
            return null;
        } else {
            return isInput ? localServiceName + "Input" : localServiceName + "Output";
        }
    }

    /**
	 * Register a new SOPERA service provider for a given service and exactly one operation.
	 * 
	 * The name of the provider will be derived from the name of the SOPERA service. 
	 * 
	 * @param adminFacade
	 * @param currentServiceName
	 * @param currentOperationName
	 */
    public static void registerSOPERAServiceProvider(AdminFacade adminFacade, String currentServiceName, String currentOperationName, SOPERAProviderConfiguration configuration) {
        ClassLoader currentContextClassloader = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(SBBFactory.class.getClassLoader());
        try {
            QName qNameServiceName = QName.valueOf(currentServiceName);
            QName generatedProviderName = generateProviderName(qNameServiceName);
            Endpoint endpoint;
            if (configuration instanceof HttpSOPERAProviderConfiguration) {
                HttpSOPERAProviderConfiguration httpConfiguration = (HttpSOPERAProviderConfiguration) configuration;
                String port;
                String prefix;
                if (httpConfiguration.useHttps()) {
                    prefix = "https://";
                    port = Integer.toString(Integer.parseInt(httpConfiguration.getPort()) - 1);
                } else {
                    prefix = "http://";
                    port = httpConfiguration.getPort();
                }
                endpoint = EndpointBuilder.forHttp().withURL(prefix + ServerAPI.getServerName() + ":" + port + "/soap/" + qNameServiceName.getLocalPart() + "WmSOPERAGeneratedProvider").exposes(adminFacade.getService(qNameServiceName).getOperation(currentOperationName)).build();
            } else {
                WebMethodsJmsSOPERAProviderConfiguration wmConfiguration = (WebMethodsJmsSOPERAProviderConfiguration) configuration;
                endpoint = EndpointBuilder.forWebMethodsJms().destinationStyle(wmConfiguration.getStyle()).jndiProviderUrl("wmjmsnaming://" + wmConfiguration.getBrokerName() + "@" + wmConfiguration.getBrokerHost() + ":" + wmConfiguration.getBrokerPort()).jndiDestinationName("soperaReqCallQueue").userId("admin").build();
            }
            ServiceProvider provider = ServiceProviderBuilder.provider().withId(generatedProviderName).forService(adminFacade.getService(qNameServiceName)).withEndpoint(endpoint).build();
            adminFacade.registerServiceProvider(qNameServiceName, provider);
        } catch (Exception e) {
            WmSOPERA.getInstance().createAdapterException(GENERIC_SOPERA_EXCEPTION, e);
        } finally {
            Thread.currentThread().setContextClassLoader(currentContextClassloader);
        }
    }

    /**
	 * Automatically generate a SOPERA service provider name for a given SOPERA service. 
	 * 
	 * @param qNameServiceName
	 * @return
	 */
    public static QName generateProviderName(QName qNameServiceName) {
        QName generatedProviderName = QName.valueOf("{" + qNameServiceName.getNamespaceURI() + "/" + qNameServiceName.getLocalPart() + "}" + "WmSOPERAGeneratedProvider");
        return generatedProviderName;
    }

    private static Document staticXsdForPlainXml(String targetNamespace) throws ParserConfigurationException, SAXException, IOException {
        String schemaString = "<xsd:schema targetNamespace=\"" + targetNamespace + "\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\">" + "<xsd:element name=\"requestStream\">" + "<xsd:complexType>" + "<xsd:sequence>" + "<xsd:any maxOccurs=\"unbounded\" minOccurs=\"0\"/>" + "</xsd:sequence>" + "</xsd:complexType>" + "</xsd:element>" + "<xsd:element name=\"responseStream\">" + "<xsd:complexType>" + "<xsd:sequence>" + "<xsd:any maxOccurs=\"unbounded\" minOccurs=\"0\"/>" + "</xsd:sequence>" + "</xsd:complexType>" + "</xsd:element>" + "</xsd:schema>";
        return createXmlDocument(schemaString);
    }

    /**
	 * 
	 * @param nodeName
	 * @return
	 * @throws IOException
	 * @throws XSDMapperException
	 * @throws XSDCoderException
	 * @throws WSDException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 */
    private static List<Document> xsdForIsNode(String nodeName) throws IOException, XSDMapperException, XSDCoderException, WSDException, ParserConfigurationException, SAXException {
        List<Document> parsedXsdDocuments = new ArrayList<Document>();
        NSNode nsNode = Namespace.current().getNode(NSName.create(nodeName));
        IData result = IDataFactory.create();
        CreateXSD.generateXSD(nsNode, result);
        IData xsd = SimpleIDataUtil.getIData(result, "xsd");
        String xsdSource = SimpleIDataUtil.getString(xsd, "source");
        Document rootSchemaDocument = createXmlDocument(xsdSource);
        cleanupSchema(nodeName, rootSchemaDocument);
        Set<String> importSet = extractImports(rootSchemaDocument);
        parsedXsdDocuments.add(rootSchemaDocument);
        IDataCursor xsdCursor = xsd.getCursor();
        while (xsdCursor.next("import-xsd")) {
            IData importedXsdIData = (IData) xsdCursor.getValue();
            String importedXsdSource = SimpleIDataUtil.getString(importedXsdIData, "source");
            Document importedXmlDocument = createXmlDocument(importedXsdSource);
            importSet.addAll(extractImports(importedXmlDocument));
            parsedXsdDocuments.add(importedXmlDocument);
        }
        Element e = rootSchemaDocument.getDocumentElement();
        e.setAttribute("xmlns:ns", "http://www.openapplications.org/oagis/9");
        return parsedXsdDocuments;
    }

    private static Set<String> extractImports(Document doc) {
        Element schemaElement = doc.getDocumentElement();
        HashSet<String> result = new HashSet<String>();
        NodeList importElements = schemaElement.getElementsByTagName("xsd:import");
        for (int i = 0; i < importElements.getLength(); i++) {
            Element e = (Element) importElements.item(i);
            result.add(e.getAttribute("namespace"));
            e.removeAttribute("schemaLocation");
        }
        return result;
    }

    private static Document createXmlDocument(String xsdSource) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        StringReader sr = new StringReader(xsdSource);
        Document doc = builder.parse(new InputSource(sr));
        return doc;
    }

    private static void cleanupSchema(String nodeName, Document doc) {
        String localName = nodeName.substring(nodeName.lastIndexOf(':') + 1);
        Element schemaElement = (Element) doc.getElementsByTagName("xsd:schema").item(0);
        removeEnvElement(schemaElement);
        NodeList complexTypes = schemaElement.getElementsByTagName("xsd:complexType");
        for (int i = 0; i < complexTypes.getLength(); i++) {
            Element ct = (Element) complexTypes.item(i);
            String name = ct.getAttributes().getNamedItem("name").getNodeValue();
            if ("envelope".equals(name)) {
                ct.getParentNode().removeChild(ct);
                break;
            } else if (localName.equals(name)) {
                Element seqElement = DomUtils.getFirstChildNamed("http://www.w3.org/2001/XMLSchema", "sequence", ct);
                removeEnvElement(seqElement);
            }
        }
    }

    private static void removeEnvElement(Element schemaElement) {
        NodeList elements = schemaElement.getElementsByTagName("xsd:element");
        for (int i = 0; i < elements.getLength(); i++) {
            Element ele = (Element) elements.item(i);
            Node attribute = ele.getAttributes().getNamedItem("name");
            if (attribute == null) continue;
            String name = attribute.getNodeValue();
            if ("_env".equals(name)) {
                ele.getParentNode().removeChild(ele);
                break;
            }
        }
    }

    /**
	 * 
	 * @param outputName 
	 * @param messageSchema
	 * @return
	 */
    private static boolean isEmptyMessageSchema(Element schemaElement, String outputName) {
        NodeList children = schemaElement.getChildNodes();
        Element outputMessageComplexType = null;
        int max = children.getLength();
        for (int i = 0; i < max; i++) {
            Node current = children.item(i);
            NamedNodeMap allAttributes = current.getAttributes();
            if (allAttributes == null) continue;
            Node attributeNode = allAttributes.getNamedItem("name");
            if (attributeNode == null) continue;
            String nameAttribute = attributeNode.getNodeValue();
            if (outputName.equals(nameAttribute)) {
                outputMessageComplexType = (Element) current;
            }
        }
        if (outputMessageComplexType == null) return true;
        return false;
    }

    protected static void logException(Throwable e) {
        ARTLogger logger = WmSOPERA.getInstance().retrieveLogger();
        logger.logError(LOGGING_MSG, e.getMessage());
        StackTraceElement[] sts = e.getStackTrace();
        for (StackTraceElement ele : sts) {
            logger.logError(LOGGING_MSG, ele.toString());
        }
        Throwable cause = e.getCause();
        if (cause != null) {
            logger.logError(LOGGING_MSG, "Caused by:");
            logException(cause);
        }
    }

    protected static void logError(String msg) {
        WmSOPERA.getInstance().retrieveLogger().logError(LOGGING_MSG, msg);
    }
}
