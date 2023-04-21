package com.esri.gpt.server.openls.provider.components;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import com.esri.gpt.framework.util.Val;
import com.esri.gpt.framework.xml.DomUtil;
import com.esri.gpt.framework.xml.XmlIoUtil;
import com.esri.gpt.server.csw.provider.components.ISupportedValues;
import com.esri.gpt.server.csw.provider.components.OperationResponse;
import com.esri.gpt.server.csw.provider.components.OwsException;
import com.esri.gpt.server.csw.provider.components.ParseHelper;
import com.esri.gpt.server.csw.provider.components.ServiceProperties;
import com.esri.gpt.server.csw.provider.components.ValidationHelper;

/**
 * Request handler.
 */
public class RequestHandler {

    /** The Logger. */
    private static Logger LOGGER = Logger.getLogger(RequestHandler.class.getName());

    /** instance variables ====================================================== */
    private OperationContext operationContext;

    private boolean wasSoap = false;

    /** Default constructor */
    public RequestHandler() {
    }

    /**
   * Gets the operation context.
   * @return the operation context
   */
    public OperationContext getOperationContext() {
        return this.operationContext;
    }

    /**
   * Sets the operation context.
   * @param context the operation context
   */
    public void setOperationContext(OperationContext context) {
        this.operationContext = context;
    }

    /**
   * Gets the flag indicating a SOAP based request.
   * @return <code>true</code> is this was a SOAP based request
   */
    public boolean getWasSoap() {
        return this.wasSoap;
    }

    /**
   * Sets the flag indicating a SOAP based request.
   * @param wasSoap <code>true</code> is this was a SOAP based request
   */
    public void setWasSoap(boolean wasSoap) {
        this.wasSoap = wasSoap;
    }

    /**
   * Handles a URL based request (HTTP GET).
   * @param request the HTTP request
   * @throws Exception if a processing exception occurs
   */
    public OperationResponse handleGet(HttpServletRequest request) throws Exception {
        LOGGER.finer("Handling XLS request URL...");
        OperationContext context = this.getOperationContext();
        IProviderFactory factory = context.getProviderFactory();
        ServiceProperties svcProps = context.getServiceProperties();
        ParseHelper pHelper = new ParseHelper();
        ValidationHelper vHelper = new ValidationHelper();
        String locator;
        String[] parsed;
        ISupportedValues supported;
        String opName = null;
        IOperationProvider opProvider = null;
        locator = "request";
        parsed = pHelper.getParameterValues(request, locator);
        supported = svcProps.getSupportedValues(XlsConstants.Parameter_OperationName);
        opName = vHelper.validateValue(supported, locator, parsed, true);
        context.setOperationName(opName);
        if (opName != null) {
            opProvider = factory.makeOperationProvider(context, opName);
        }
        if (opProvider == null) {
            if ((opName == null) || (opName.length() == 0)) {
                throw new OwsException(OwsException.OWSCODE_MissingParameterValue, "request", "The request parameter was missing.");
            } else {
                throw new OwsException(OwsException.OWSCODE_OperationNotSupported, "request", "Unsupported operation: " + opName);
            }
        }
        this.parseServiceAndVersion(context, request);
        opProvider.handleGet(context, request);
        return context.getOperationResponse();
    }

    /**
   * Handles an XML based request (normally HTTP POST).
   * @param xml the XML
   * @throws Exception if a processing exception occurs
   */
    public OperationResponse handleXML(String xml) throws Exception {
        LOGGER.finer("Handling XLS request XML...");
        OperationContext context = this.getOperationContext();
        IProviderFactory factory = context.getProviderFactory();
        Node root = null;
        String nsSoapUri = null;
        String nsSoapPfx = null;
        try {
            context.getRequestOptions().setRequestXml(xml);
            Document dom = this.loadDom(xml);
            context.getRequestOptions().setRequestDom(dom);
            XlsNamespaces ns = new XlsNamespaces();
            XPath xpath = XPathFactory.newInstance().newXPath();
            xpath.setNamespaceContext(ns.makeNamespaceContext());
            Node requestNd = null;
            String expr = "/SOAP-ENV:Envelope | /soap:Envelope | /soap_2003_05:Envelope";
            Node ndSoapEnv = (Node) xpath.evaluate(expr, dom, XPathConstants.NODE);
            if (ndSoapEnv != null) {
                this.setWasSoap(true);
                nsSoapUri = ndSoapEnv.getNamespaceURI();
                nsSoapPfx = ndSoapEnv.lookupPrefix(nsSoapUri);
                expr = "//SOAP-ENV:Body | //soap:Body | //soap_2003_05:Body";
                Node ndSoapBody = (Node) xpath.evaluate(expr, ndSoapEnv, XPathConstants.NODE);
                if (ndSoapBody == null) {
                    throw new OwsException(OwsException.OWSCODE_MissingParameterValue, "Envelope", "The SOAP body was missing.");
                } else {
                    NodeList nl = ndSoapBody.getChildNodes();
                    for (int i = 0; i < nl.getLength(); i++) {
                        if (nl.item(i).getNodeType() == Node.ELEMENT_NODE) {
                            root = nl.item(i);
                            break;
                        }
                    }
                    if (root == null) {
                        throw new OwsException(OwsException.OWSCODE_MissingParameterValue, "Body", "No CSW operation was located within the soap body.");
                    }
                }
            }
            String opName = null;
            if (!this.getWasSoap()) {
                NodeList nl = dom.getChildNodes();
                for (int i = 0; i < nl.getLength(); i++) {
                    if (nl.item(i).getNodeType() == Node.ELEMENT_NODE) {
                        root = nl.item(i);
                        break;
                    }
                }
                if (root != null) {
                    NodeList children = root.getChildNodes();
                    for (int i = 0; i < children.getLength(); i++) {
                        Node child = children.item(i);
                        if (child.getNodeType() == Node.ELEMENT_NODE && child.getLocalName().equalsIgnoreCase("request")) {
                            requestNd = child;
                            break;
                        }
                    }
                }
                if (requestNd != null) {
                    NodeList children = requestNd.getChildNodes();
                    for (int i = 0; i < children.getLength(); i++) {
                        Node child = children.item(i);
                        if (child.getNodeType() == Node.ELEMENT_NODE && (child.getLocalName().equalsIgnoreCase("GetPortrayMapCapabilitiesRequest") || child.getLocalName().equalsIgnoreCase("PortrayMapRequest") || child.getLocalName().equalsIgnoreCase("DetermineRouteRequest") || child.getLocalName().equalsIgnoreCase("DirectoryRequest") || child.getLocalName().equalsIgnoreCase("GeocodeRequest") || child.getLocalName().equalsIgnoreCase("ReverseGeocodeRequest"))) {
                            opName = child.getLocalName();
                            break;
                        }
                    }
                }
            }
            IOperationProvider opProvider = null;
            String namespace = root.getNamespaceURI();
            if (XlsNamespaces.URI_XLS.equals(namespace)) {
                context.setOperationName(opName);
                if (opName != null) {
                    opProvider = factory.makeOperationProvider(context, opName);
                }
            }
            if (opProvider == null) {
                throw new OwsException(OwsException.OWSCODE_OperationNotSupported, "root-node", "Unsupported operation: " + opName);
            }
            this.parseServiceAndVersion(context, requestNd, xpath);
            opProvider.handleXML(context, requestNd, xpath);
        } catch (Exception e) {
            if (this.getWasSoap()) {
                this.generateSoapResponse(context, nsSoapUri, nsSoapPfx, e);
                return context.getOperationResponse();
            } else {
                throw e;
            }
        }
        if (this.getWasSoap()) {
            this.generateSoapResponse(context, nsSoapUri, nsSoapPfx, null);
        }
        return context.getOperationResponse();
    }

    /**
   * Wraps the response within a SOAp envelope.
   * <br/>If the exception argument is null, the operation response is wrapped.
   * <br/>If the exception argument is not null, an OWS exception report is wrapped.
   * <br/>The OperationResponse.responseXml is reset to the SOAP response string.
   * @param context the operation context
   * @param nsSoapUri the SOAP namespace URI
   * @param nsSoapPfx the SOAP namespace prefix
   * @param exception an exception condition that should be wrapped as an OWS exception report
   * @throws Exception Exception if a processing exception occurs
   */
    protected void generateSoapResponse(OperationContext context, String nsSoapUri, String nsSoapPfx, Exception exception) throws Exception {
        if (Val.chkStr(nsSoapPfx).length() == 0) {
            nsSoapPfx = "soap";
        }
        Document domSoap = DomUtil.newDocument();
        Node ndSoapEnv = domSoap.createElementNS(nsSoapUri, nsSoapPfx + ":Envelope");
        Node ndSoapBody = domSoap.createElementNS(nsSoapUri, nsSoapPfx + ":Body");
        domSoap.appendChild(ndSoapEnv);
        ndSoapEnv.appendChild(ndSoapBody);
        OperationResponse opResponse = context.getOperationResponse();
        if (exception != null) {
            if (exception instanceof OwsException) {
                OwsException ows = (OwsException) exception;
                LOGGER.finer("Invalid XLS request: " + exception.getMessage());
                opResponse.setResponseXml(ows.getReport());
            } else {
                OwsException ows = new OwsException(exception);
                LOGGER.log(Level.WARNING, exception.toString(), exception);
                opResponse.setResponseXml(ows.getReport());
            }
        }
        try {
            Document domCsw = DomUtil.makeDomFromString(opResponse.getResponseXml(), true);
            NodeList nl = domCsw.getChildNodes();
            for (int i = 0; i < nl.getLength(); i++) {
                if (nl.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    Node cswRoot = nl.item(i);
                    Node ndImported = domSoap.importNode(cswRoot, true);
                    ndSoapBody.appendChild(ndImported);
                    opResponse.setResponseXml(XmlIoUtil.domToString(domSoap));
                    break;
                }
            }
        } catch (Exception e) {
            Node ndFault = domSoap.createElementNS(nsSoapUri, nsSoapPfx + ":Fault");
            Node ndCode = domSoap.createElement("faultcode");
            Node ndReason = domSoap.createElement("faultstring");
            Node ndDetail = domSoap.createElementNS(nsSoapUri, nsSoapPfx + ":Detail");
            Node ndMessage = domSoap.createElement("message");
            ndFault.appendChild(ndCode);
            ndCode.setTextContent("Server");
            ndFault.appendChild(ndReason);
            ndReason.setTextContent("An error occurred while generating the SOAP body.");
            ndFault.appendChild(ndDetail);
            ndDetail.appendChild(ndMessage);
            ndMessage.setTextContent(e.toString());
            ndSoapBody.appendChild(ndFault);
            opResponse.setResponseXml(XmlIoUtil.domToString(domSoap));
        }
    }

    /**
   * Loads an XML string into an XML Document.
   * @param xml the document XML string
   * @return the document
   * @throws OwsException if the document fails to load
   */
    protected Document loadDom(String xml) throws OwsException {
        String sErrorMsg = "Unable to parse incoming XML document.";
        try {
            xml = Val.chkStr(xml);
            if (xml.length() == 0) {
                sErrorMsg = "The incoming XML document was empty.";
            }
            return DomUtil.makeDomFromString(xml, true);
        } catch (ParserConfigurationException e) {
            throw new OwsException(sErrorMsg, e);
        } catch (SAXException e) {
            throw new OwsException(sErrorMsg, e);
        } catch (IOException e) {
            throw new OwsException(sErrorMsg, e);
        }
    }

    /**
   * Parses the service name and version for a URL based request (HTTP GET).
   * @param context the operation context
   * @param request the HTTP request
   * @throws OwsException if validation fails
   * @throws XPathExpressionException if an XPath related exception occurs
   */
    public void parseServiceAndVersion(OperationContext context, HttpServletRequest request) throws OwsException {
        LOGGER.finer("Parsing request URL for service and version...");
        ServiceProperties svcProps = context.getServiceProperties();
        ParseHelper pHelper = new ParseHelper();
        ValidationHelper vHelper = new ValidationHelper();
        String locator;
        String[] parsed;
        ISupportedValues supported;
        locator = "service";
        parsed = pHelper.getParameterValues(request, locator);
        supported = svcProps.getSupportedValues(XlsConstants.Parameter_Service);
        String service = vHelper.validateValue(supported, locator, parsed, true);
        svcProps.setServiceName(service);
        locator = "acceptVersions";
        parsed = pHelper.getParameterValues(request, locator, ",");
        supported = svcProps.getSupportedValues(XlsConstants.Parameter_Version);
        String version = Val.chkStr(vHelper.negotiateValue(supported, locator, parsed, false));
        if (version.length() > 0) {
            svcProps.setServiceVersion(version);
        } else {
            locator = "version";
            parsed = pHelper.getParameterValues(request, locator);
            supported = svcProps.getSupportedValues(XlsConstants.Parameter_Version);
            version = vHelper.validateValue(supported, locator, parsed, false);
            svcProps.setServiceVersion(version);
        }
    }

    /**
   * Parses the service name and version an XML based request (normally HTTP POST).
   * @param context the operation context
   * @param root the root node
   * @param xpath an XPath to enable queries (properly configured with name spaces)
   * @throws OwsException if validation fails
   * @throws XPathExpressionException if an XPath related exception occurs
   */
    public void parseServiceAndVersion(OperationContext context, Node root, XPath xpath) throws OwsException, XPathExpressionException {
        LOGGER.finer("Parsing request XML for service and version...");
        ServiceProperties svcProps = context.getServiceProperties();
        ParseHelper pHelper = new ParseHelper();
        ValidationHelper vHelper = new ValidationHelper();
        String locator;
        String[] parsed;
        ISupportedValues supported;
        locator = "@version";
        parsed = pHelper.getParameterValues(root, xpath, locator);
        supported = svcProps.getSupportedValues(XlsConstants.Parameter_Version);
        String version = vHelper.validateValue(supported, locator, parsed, false);
        svcProps.setServiceVersion(version);
    }
}
