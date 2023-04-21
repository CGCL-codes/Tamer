package org.xaware.ide.xadev.conversion;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.wsdl.Binding;
import javax.wsdl.BindingOperation;
import javax.wsdl.Definition;
import javax.wsdl.Input;
import javax.wsdl.Message;
import javax.wsdl.Operation;
import javax.wsdl.Output;
import javax.wsdl.Part;
import javax.wsdl.PortType;
import javax.wsdl.WSDLException;
import javax.wsdl.extensions.mime.MIMEContent;
import javax.wsdl.extensions.mime.MIMEMimeXml;
import javax.wsdl.extensions.mime.MIMEMultipartRelated;
import javax.wsdl.extensions.mime.MIMEPart;
import javax.wsdl.extensions.soap.SOAPBinding;
import javax.wsdl.extensions.soap.SOAPBody;
import javax.wsdl.extensions.soap.SOAPOperation;
import javax.wsdl.factory.WSDLFactory;
import javax.wsdl.xml.WSDLReader;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.apache.xmlbeans.XmlException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.ibm.wsdl.extensions.mime.MIMEContentImpl;
import com.ibm.wsdl.extensions.mime.MIMEMimeXmlImpl;
import org.xaware.ide.xadev.datamodel.InputParameterData;
import org.xaware.server.data.soap.MimeContent;
import org.xaware.server.data.soap.MimeMultiPart;
import org.xaware.server.data.soap.MimePart;
import org.xaware.server.data.soap.MimeXml;
import org.xaware.server.data.soap.SoapEnv;
import org.xaware.shared.util.XAwareConstants;
import org.xaware.shared.util.logging.XAwareLogger;

/**
 * This class is used to manipulate wsdl4j objects built from a wsdl file
 * @author jtarnowski
 */
public class WSDLSOAPHelper {

    /** Input or output constants */
    public static final int INPUT = 0;

    public static final int OUTPUT = 1;

    /** WSDL flavors */
    public static final int DOC_LIT = 0;

    public static final int RPC_LIT = 1;

    public static final int RPC_ENC = 2;

    /** Constants for building SOAP Envelope and body */
    public static final String SOAP_START = "<SOAPENV:Envelope xmlns:SOAPENV=\"" + XAwareConstants.SOAPENVNamespace.getURI() + "\"><SOAPENV:Body >";

    public static final String ENCODED_SOAP_START = "<SOAPENV:Envelope xmlns:SOAPENV=\"" + XAwareConstants.SOAPENVNamespace.getURI() + "\"><SOAPENV:Body " + " xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"" + " SOAPENV:encodingStyle=\"";

    public static final String SOAP_END = "</SOAPENV:Body></SOAPENV:Envelope>";

    public static final String XSD_NAME = "import";

    public static final String XSD_URI = "http://www.w3.org/2001/XMLSchema";

    /** Logger for XAware. */
    private static final XAwareLogger logger = XAwareLogger.getXAwareLogger(WSDLSOAPHelper.class.getName());

    /** The current wsdl instance */
    private Definition theDef = null;

    /**
	 * Constructor that creates a Definition
	 * @param wsdlFilePath - String with the path & file
	 * @throws WSDLException
	 */
    public WSDLSOAPHelper(final String wsdlFilePath) throws WSDLException {
        final WSDLFactory fact = WSDLFactory.newInstance();
        final WSDLReader reader = fact.newWSDLReader();
        theDef = reader.readWSDL(wsdlFilePath);
    }

    /**
	 * Get the Definition in wsdl4j format
	 * @return Definition
	 */
    public Definition getDef() {
        return theDef;
    }

    /**
	 * Get the style - rpc or document - from a Binding
	 * @param curBinding Binding
	 * @return String
	 */
    public String getSOAPStyle(final Binding curBinding, final Operation oper) {
        String style = "";
        final BindingOperation bindingOper = curBinding.getBindingOperation(oper.getName(), null, null);
        List elems = bindingOper.getExtensibilityElements();
        for (int i = 0; i < elems.size(); i++) {
            final Object obj = elems.get(i);
            if (obj instanceof SOAPOperation) {
                final SOAPOperation soapOperation = (SOAPOperation) obj;
                style = soapOperation.getStyle();
            }
        }
        if (style == null || style.length() == 0) {
            elems = curBinding.getExtensibilityElements();
            for (int i = 0; i < elems.size(); i++) {
                final Object obj = elems.get(i);
                if (obj instanceof SOAPBinding) {
                    final SOAPBinding soapBinding = (SOAPBinding) obj;
                    style = soapBinding.getStyle();
                }
            }
        }
        if (style == null || style.length() == 0) {
            style = "document";
        }
        return style;
    }

    /**
	 * Get the use - encoded or literal - from a Binding and Operation
	 * @param curBinding - Binding
	 * @param oper - Operation
	 * @return String
	 */
    public String getSOAPUse(final Binding curBinding, final Operation oper) {
        String use = null;
        final SOAPBody body = WSDLUtil.getSOAPBody(curBinding, oper);
        if (body != null) {
            use = body.getUse();
            if (use == null) {
                use = "literal";
            }
        }
        return use;
    }

    /**
	 * Get the input message when mime is specified in the wsdl
	 * @param curBinding - Binding
	 * @param oper - Operation
	 * @return MimeMultiPart
	 */
    public MimeMultiPart getMimeInput(final Binding curBinding, final Operation oper) {
        MimeMultiPart multiPart = null;
        final java.util.List bindingOps = curBinding.getBindingOperations();
        for (int i = 0; i < bindingOps.size(); i++) {
            final BindingOperation bindingObj = (BindingOperation) bindingOps.get(i);
            if (bindingObj.getName().equals(oper.getName())) {
                final java.util.List extElemList = bindingObj.getBindingInput().getExtensibilityElements();
                for (int j = 0; j < extElemList.size(); j++) {
                    final Object obj = extElemList.get(j);
                    if (extElemList.get(j) instanceof MIMEMultipartRelated) {
                        final MIMEMultipartRelated multi = (MIMEMultipartRelated) obj;
                        multiPart = getMimeMultiPart(multi);
                        break;
                    }
                }
            }
        }
        if (multiPart != null) {
            final List attachments = multiPart.getAttachments();
            for (final Iterator iter = attachments.iterator(); iter.hasNext(); ) {
                final Object obj = iter.next();
                if (obj instanceof MimeContent) {
                    final MimeContent content = (MimeContent) obj;
                    final String encoding = getAttachmentEncoding(oper.getName(), content.getPartName(), "out");
                    content.setEncoding(encoding);
                }
            }
        }
        return multiPart;
    }

    /**
	 * Get the return when mime is specified
	 * @param curBinding - Binding
	 * @param oper - Operation
	 * @return MimeMultiPart
	 */
    public MimeMultiPart getMimeOutput(final Binding curBinding, final Operation oper) {
        MimeMultiPart multiPart = null;
        final java.util.List bindingOps = curBinding.getBindingOperations();
        for (int i = 0; i < bindingOps.size(); i++) {
            final BindingOperation bindingObj = (BindingOperation) bindingOps.get(i);
            if (bindingObj.getName().equals(oper.getName())) {
                final java.util.List extElemList = bindingObj.getBindingOutput().getExtensibilityElements();
                for (int j = 0; j < extElemList.size(); j++) {
                    final Object obj = extElemList.get(j);
                    if (extElemList.get(j) instanceof MIMEMultipartRelated) {
                        final MIMEMultipartRelated multi = (MIMEMultipartRelated) obj;
                        multiPart = getMimeMultiPart(multi);
                        break;
                    }
                }
            }
        }
        if (multiPart != null) {
            final List attachments = multiPart.getAttachments();
            for (final Iterator iter = attachments.iterator(); iter.hasNext(); ) {
                final Object obj = iter.next();
                if (obj instanceof MimeContent) {
                    final MimeContent content = (MimeContent) obj;
                    final String encoding = getAttachmentEncoding(oper.getName(), content.getPartName(), "out");
                    content.setEncoding(encoding);
                }
            }
        }
        return multiPart;
    }

    /**
	 * Get the mime and put in a MimeMultiPart
	 * @param multi - MIMEMultipartRelated wsdl4j object
	 * @return MimeMultiPart
	 */
    MimeMultiPart getMimeMultiPart(final MIMEMultipartRelated multi) {
        final MimeMultiPart multiPart = new MimeMultiPart();
        multiPart.setMimeParts(new ArrayList());
        final List parts = multi.getMIMEParts();
        for (int k = 0; k < parts.size(); k++) {
            final MIMEPart part = (MIMEPart) parts.get(k);
            final MimePart newPart = new MimePart();
            multiPart.addMimePart(newPart);
            newPart.setContent(new ArrayList());
            final List elements = part.getExtensibilityElements();
            for (int x = 0; x < elements.size(); x++) {
                final Object mimeObj = elements.get(x);
                if (mimeObj instanceof SOAPBody) {
                    final SoapEnv content = new SoapEnv();
                    newPart.addContent(content);
                } else if (mimeObj instanceof MIMEContent) {
                    final MIMEContent item = (MIMEContentImpl) mimeObj;
                    final MimeContent content = new MimeContent();
                    content.setPartName(item.getPart());
                    content.setMimeType(item.getType());
                    newPart.addContent(content);
                } else if (mimeObj instanceof MIMEMimeXmlImpl) {
                    final MIMEMimeXml item = (MIMEMimeXmlImpl) mimeObj;
                    final MimeXml xml = new MimeXml();
                    xml.setPartName(item.getPart());
                    newPart.addContent(xml);
                }
            }
        }
        return multiPart;
    }

    /**
	 * Get the encoding attribute of an attachment if available
	 * @param opName - String
	 * @param partName - String
	 * @param inOrOut - String
	 * @return String (possibly "" but not null)
	 */
    String getAttachmentEncoding(final String opName, final String partName, final String inOrOut) {
        String encoding = "";
        Message mess = null;
        final Map portMap = theDef.getPortTypes();
        final Collection values = portMap.values();
        for (final Iterator iter = values.iterator(); iter.hasNext(); ) {
            final PortType portType = (PortType) iter.next();
            final Operation op = portType.getOperation(opName, null, null);
            if (op != null && op.getName().equals(opName)) {
                if ("in".equals(inOrOut)) {
                    mess = op.getInput().getMessage();
                } else {
                    mess = op.getOutput().getMessage();
                }
                break;
            }
        }
        if (mess != null) {
            final Part part = mess.getPart(partName);
            if (part != null) {
                final QName typeName = part.getTypeName();
                if (typeName != null) {
                    encoding = typeName.getLocalPart();
                }
            }
        }
        return encoding;
    }

    public String buildSOAPEnvelope(final String payload, final String use, final Binding binding, final Operation oper) {
        final StringBuffer buff = new StringBuffer();
        if (!"encoded".equals(use)) {
            buff.append(SOAP_START);
        } else {
            buff.append(ENCODED_SOAP_START);
            String encoding = "http://schemas.xmlsoap.org/soap/encoding/";
            final SOAPBody body = WSDLUtil.getSOAPBody(binding, oper);
            if (body != null) {
                final List encodings = body.getEncodingStyles();
                if (encodings.size() > 0) {
                    encoding = (String) encodings.get(0);
                }
            }
            buff.append(encoding);
            buff.append("\">");
        }
        buff.append(payload);
        buff.append(SOAP_END);
        return buff.toString();
    }

    /**
	 * Build a String representing a SOAP request body 
	 * @param oper - Operation
	 * @param wsdlType
	 * @param paramsOut - List to hold generated Input parameters
	 * @return String
	 */
    public String buildRequestBody(final Binding binding, final Operation oper, final int wsdlType, final List paramsOut) throws IOException, XmlException {
        String payload = "";
        final Input input = oper.getInput();
        final Message message = input.getMessage();
        final List parts = message.getOrderedParts(null);
        if (wsdlType == DOC_LIT) {
            payload = buildDocLitSoapBody(parts, oper, paramsOut, INPUT);
        } else if (wsdlType == RPC_LIT) {
            payload = buildRpcSoapBody(parts, binding, oper, paramsOut, false, INPUT);
        } else if (wsdlType == RPC_ENC) {
            payload = buildRpcSoapBody(parts, binding, oper, paramsOut, true, INPUT);
        }
        return payload;
    }

    /**
	 * Build a document/literal String representing a SOAP return section 
	 * @param oper - Operation
	 * @return String
	 */
    public String buildDocLiteralReturnBody(final Operation oper) throws IOException, XmlException {
        final Output output = oper.getOutput();
        final Message message = output.getMessage();
        final List parts = message.getOrderedParts(null);
        final String payload = buildDocLitSoapBody(parts, oper, null, OUTPUT);
        return payload;
    }

    public String buildRpcReturnBody(final Binding binding, final Operation oper) throws IOException, XmlException {
        final Output output = oper.getOutput();
        final Message message = output.getMessage();
        final List parts = message.getOrderedParts(null);
        final String payload = buildRpcSoapBody(parts, binding, oper, null, false, OUTPUT);
        return payload;
    }

    /**
	 * Build the SOAP request or return Body from a List of Parts
	 * @param parts - List
	 * @param paramsOut - List
	 * @return String of XML
	 * @throws IOException
	 * @throws XmlException
	 */
    private String buildDocLitSoapBody(final List parts, final Operation operation, final List paramsOut, final int inOrOut) throws IOException, XmlException {
        if (parts == null || parts.size() == 0) {
            return "";
        }
        final StringBuffer buff = new StringBuffer();
        final List schemas = WSDLUtil.getSchemaClones(theDef);
        final List schemaStrings = getSchemaStrings(schemas);
        for (final Iterator iter = parts.iterator(); iter.hasNext(); ) {
            String elementName = null;
            final Part part = (Part) iter.next();
            final QName qName = part.getElementName();
            if (qName != null) {
                elementName = qName.getLocalPart();
                if ("string".equals(elementName)) {
                    final String partName = part.getName();
                    buff.append('<').append(partName).append(">%").append(partName).append("%</").append(partName).append('>');
                    if (paramsOut != null) {
                        final InputParameterData data = new InputParameterData();
                        data.setName(partName);
                        data.setType(elementName);
                        paramsOut.add(data);
                    }
                } else {
                    buff.append(SchemaUtil.getTheInstanceXml(elementName, schemaStrings, paramsOut, false));
                }
            }
        }
        return buff.toString();
    }

    /**
	 * build a soap body from rpc
	 * @param parts - Parts - wsdl4j children of the Message 
	 * @param operationName - String
	 * @param paramsOut - List to add params to
	 * @return String - the body XML
	 * @throws IOException
	 * @throws XmlException
	 */
    private String buildRpcSoapBody(final List parts, final Binding binding, final Operation operation, final List paramsOut, final boolean isRpcEncRequest, final int inOrOut) throws IOException, XmlException {
        String payload = "";
        final SOAPBody body = WSDLUtil.getSOAPBody(binding, operation);
        final String target = body.getNamespaceURI();
        if (parts == null || parts.size() == 0) {
            return this.buildPayloadWithoutParams(operation, target, inOrOut);
        }
        String operationName = operation.getName();
        if (OUTPUT == inOrOut) {
            operationName += "Response";
        }
        final List schemas = WSDLUtil.getSchemaClones(theDef);
        final List schemaStrings = getSchemaStrings(schemas);
        final Element ourSchema = this.buildRpcSchema(operationName, target, parts);
        final StringWriter writer = new StringWriter();
        final XMLSerializer serializer = new XMLSerializer(writer, new OutputFormat());
        try {
            serializer.asDOMSerializer();
            serializer.serialize(ourSchema);
            final String str = writer.toString();
            schemaStrings.add(str);
        } catch (final Exception e) {
            e.printStackTrace();
        }
        payload += SchemaUtil.getTheInstanceXml(operationName, schemaStrings, paramsOut, isRpcEncRequest);
        return payload;
    }

    /**
	 * Get the schemas for the WSDL.  Force in the root level Namespace declarations.
	 * @param List 
	 * @return List - The altered schemas in a list
	 */
    public List getSchemaStrings(final List schemas) {
        final List schemaStrings = new ArrayList();
        for (final Iterator iter = schemas.iterator(); iter.hasNext(); ) {
            final Element clone = (Element) iter.next();
            removeImports(clone);
            addNamespaceAttrs(clone);
            final StringWriter writer = new StringWriter();
            final XMLSerializer serializer = new XMLSerializer(writer, new OutputFormat());
            try {
                serializer.asDOMSerializer();
                serializer.serialize(clone);
                final String str = writer.toString();
                schemaStrings.add(str);
            } catch (final Exception e) {
                logger.warning("Error serializing schema: " + e.getMessage());
            }
        }
        return schemaStrings;
    }

    public void removeImports(final Node elem) {
        if (XSD_NAME.equals(elem.getLocalName()) && XSD_URI.equals(elem.getNamespaceURI())) {
            elem.getParentNode().removeChild(elem);
        } else {
            final NodeList list = elem.getChildNodes();
            final int length = list.getLength();
            for (int i = length - 1; i >= 0; i--) {
                final Node node = list.item(i);
                removeImports(node);
            }
        }
    }

    /**
	 * Add namespaces from the root of the WSDL
	 * @param elem - The Element to alter
	 * @return Map
	 */
    private Map addNamespaceAttrs(final Element elem) {
        final Map rc = new HashMap();
        final Map map = WSDLUtil.getNamespaces(theDef);
        final NamedNodeMap attribs = elem.getAttributes();
        for (final Iterator it = map.entrySet().iterator(); it.hasNext(); ) {
            final Map.Entry entry = (Map.Entry) it.next();
            final String key = (String) entry.getKey();
            final String value = (String) entry.getValue();
            String namespaceKey = "xmlns";
            if (key != null && key.length() > 0) {
                namespaceKey += ":" + key;
            }
            if (attribs.getNamedItem(namespaceKey) == null) {
                elem.setAttribute(namespaceKey, value);
            }
            rc.put(value, namespaceKey);
        }
        return rc;
    }

    /**
	 * Fabricate a schema for our message
	 * @param operationName - String holding the SOAP operation
	 * @param parts - List of wsdl4j Parts in the Message
	 * @return Element - root of our fabricated schema
	 */
    private Element buildRpcSchema(final String operationName, final String targetNamespace, final List parts) {
        Element schema = null;
        boolean haveSchemaNamespace = false;
        final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setValidating(false);
        dbf.setNamespaceAware(true);
        dbf.setIgnoringElementContentWhitespace(true);
        try {
            final DocumentBuilder builder = dbf.newDocumentBuilder();
            final Document doc = builder.newDocument();
            String schemaPrefix = WSDLUtil.getPrefix(theDef, "http://www.w3.org/2001/XMLSchema");
            String dummyPrefix = "";
            if (schemaPrefix.length() > 0) {
                haveSchemaNamespace = true;
            } else {
                dummyPrefix = "XAXSD";
                schemaPrefix = "XAXSD";
            }
            schemaPrefix += ":";
            schema = doc.createElement(schemaPrefix + "schema");
            final Map namespaces = addNamespaceAttrs(schema);
            String targetPrefix = "";
            if (targetNamespace != null) {
                schema.setAttribute("targetNamespace", targetNamespace);
                targetPrefix = WSDLUtil.getPrefix(theDef, targetNamespace);
                if (targetPrefix != null && targetPrefix.length() > 0) {
                    targetPrefix += ":";
                } else {
                    targetPrefix = "";
                }
                schema.setAttribute("xmlns" + targetPrefix, targetNamespace);
            }
            if (!haveSchemaNamespace) {
                schema.setAttribute("xmlns:" + dummyPrefix, "http://www.w3.org/2001/XMLSchema");
            }
            Element elem = doc.createElement(schemaPrefix + "element");
            elem.setAttribute("name", operationName);
            schema.appendChild(elem);
            if (parts != null && parts.size() > 0) {
                elem.setAttribute("type", targetPrefix + "XAWARE_XAWARE" + operationName + "Type");
                elem = doc.createElement(schemaPrefix + "complexType");
                elem.setAttribute("name", "XAWARE_XAWARE" + operationName + "Type");
                schema.appendChild(elem);
                final Element sequence = doc.createElement(schemaPrefix + "sequence");
                elem.appendChild(sequence);
                for (final Iterator iter = parts.iterator(); iter.hasNext(); ) {
                    String partName = "";
                    final Part part = (Part) iter.next();
                    QName qname = part.getElementName();
                    if (qname != null) {
                        partName = qname.getLocalPart();
                    } else {
                        partName = part.getName();
                    }
                    elem = doc.createElement(schemaPrefix + "element");
                    elem.setAttribute("name", partName);
                    qname = part.getTypeName();
                    if (qname != null) {
                        final String uri = qname.getNamespaceURI();
                        String prefix = WSDLUtil.getPrefix(theDef, uri);
                        if (prefix != null && prefix.length() > 0) {
                            prefix += ":";
                        } else {
                            prefix = "";
                        }
                        elem.setAttribute("type", prefix + qname.getLocalPart());
                        sequence.appendChild(elem);
                    }
                }
            }
        } catch (final ParserConfigurationException e) {
            logger.warning("Error building rpc schema: " + e.getMessage());
        }
        return schema;
    }

    /**
	 * Build SOAP payload when there are no parts
	 * @param oper
	 * @param qname
	 * @param inOrOut
	 * @return String
	 */
    String buildPayloadWithoutParams(final Operation oper, final QName qname, final int inOrOut) {
        Message mess;
        if (inOrOut == INPUT) {
            mess = oper.getInput().getMessage();
        } else {
            mess = oper.getOutput().getMessage();
        }
        String prefix = qname.getPrefix();
        final String uri = qname.getNamespaceURI();
        if (prefix == null) {
            WSDLUtil.getPrefix(theDef, uri);
        }
        if (prefix == null || prefix.length() == 0) {
            prefix = "abc";
        }
        final StringBuffer buff = new StringBuffer("<");
        buff.append(prefix).append(':').append(oper.getName());
        if (inOrOut == OUTPUT) {
            buff.append("Response");
        }
        buff.append(" xmlns:").append(prefix).append("=\"").append(uri).append("\" />");
        return buff.toString();
    }

    /**
	 * Build SOAP payload when there are no parts
	 * @param oper
	 * @param qname
	 * @param inOrOut
	 * @return String
	 */
    String buildPayloadWithoutParams(final Operation oper, final String uri, final int inOrOut) {
        Message mess;
        if (inOrOut == INPUT) {
            mess = oper.getInput().getMessage();
        } else {
            mess = oper.getOutput().getMessage();
        }
        String prefix = WSDLUtil.getPrefix(theDef, uri);
        if (prefix == null || prefix.length() == 0) {
            prefix = "abc";
        }
        final StringBuffer buff = new StringBuffer("<");
        buff.append(prefix).append(':').append(oper.getName());
        if (inOrOut == OUTPUT) {
            buff.append("Response");
        }
        buff.append(" xmlns:").append(prefix).append("=\"").append(uri).append("\" />");
        return buff.toString();
    }
}
