package net.sf.istcontract.wsimport.server;

import com.sun.istack.Nullable;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import net.sf.istcontract.wsimport.addressing.W3CAddressingConstants;
import net.sf.istcontract.wsimport.api.server.DocumentAddressResolver;
import net.sf.istcontract.wsimport.api.server.PortAddressResolver;
import net.sf.istcontract.wsimport.api.server.SDDocument;
import net.sf.istcontract.wsimport.api.server.WSEndpoint;
import net.sf.istcontract.wsimport.util.xml.XMLStreamReaderToXMLStreamWriter;
import net.sf.istcontract.wsimport.wsdl.parser.WSDLConstants;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;

/**
 * Patches WSDL with the correct endpoint address and the relative paths
 * to other documents.
 *
 * @author Jitendra Kotamraju
 * @author Kohsuke Kawaguchi
 */
final class WSDLPatcher extends XMLStreamReaderToXMLStreamWriter {

    private static final String NS_XSD = "http://www.w3.org/2001/XMLSchema";

    private static final QName SCHEMA_INCLUDE_QNAME = new QName(NS_XSD, "include");

    private static final QName SCHEMA_IMPORT_QNAME = new QName(NS_XSD, "import");

    private static final QName SCHEMA_REDEFINE_QNAME = new QName(NS_XSD, "redefine");

    private static final Logger logger = Logger.getLogger(net.sf.istcontract.wsimport.util.Constants.LoggingDomain + ".wsdl.patcher");

    /**
     * {@link WSEndpoint} that owns the WSDL we are patching right now.
     */
    private final WSEndpointImpl<?> endpoint;

    /**
     * Document that is being patched.
     */
    private final SDDocumentImpl current;

    private final DocumentAddressResolver resolver;

    private final PortAddressResolver portAddressResolver;

    private String targetNamespace;

    private QName serviceName;

    private QName portName;

    private enum EPR_ADDRESS_STATE {

        IN, OUT, DONE
    }

    private EPR_ADDRESS_STATE eprAddressState = EPR_ADDRESS_STATE.OUT;

    /**
     * Creates a {@link WSDLPatcher} for patching WSDL.
     *
     * @param endpoint
     *      The endpoint that we are patchinig WSDL for. This object is consulted
     *      to check other {@link SDDocument}s. Must not be null.
     * @param current
     *      The document that we are patching. Must not be null.
     * @param portAddressResolver
     *      address of the endpoint is resolved using this resolver.
     * @param resolver
     *      Consulted to generate references among  {@link SDDocument}s.
     *      Must not be null.
     */
    public WSDLPatcher(WSEndpointImpl<?> endpoint, SDDocumentImpl current, PortAddressResolver portAddressResolver, DocumentAddressResolver resolver) {
        this.endpoint = endpoint;
        this.current = current;
        this.portAddressResolver = portAddressResolver;
        this.resolver = resolver;
    }

    @Override
    protected void handleAttribute(int i) throws XMLStreamException {
        QName name = in.getName();
        String attLocalName = in.getAttributeLocalName(i);
        if ((name.equals(SCHEMA_INCLUDE_QNAME) && attLocalName.equals("schemaLocation")) || (name.equals(SCHEMA_IMPORT_QNAME) && attLocalName.equals("schemaLocation")) || (name.equals(SCHEMA_REDEFINE_QNAME) && attLocalName.equals("schemaLocation")) || (name.equals(WSDLConstants.QNAME_IMPORT) && attLocalName.equals("location"))) {
            String relPath = in.getAttributeValue(i);
            String actualPath = getPatchedImportLocation(relPath);
            if (actualPath == null) {
                return;
            }
            logger.fine("Fixing the relative location:" + relPath + " with absolute location:" + actualPath);
            writeAttribute(i, actualPath);
            return;
        }
        if (name.equals(WSDLConstants.NS_SOAP_BINDING_ADDRESS) || name.equals(WSDLConstants.NS_SOAP12_BINDING_ADDRESS)) {
            if (attLocalName.equals("location")) {
                String value = getAddressLocation();
                if (value != null) {
                    logger.fine("Fixing service:" + serviceName + " port:" + portName + " address with " + value);
                    writeAttribute(i, value);
                    return;
                }
            }
        }
        super.handleAttribute(i);
    }

    /**
     * Writes out an {@code i}-th attribute but with a different value.
     * @param i attribute index
     * @param value attribute value
     * @throws XMLStreamException when an error encountered while writing attribute
     */
    private void writeAttribute(int i, String value) throws XMLStreamException {
        String nsUri = in.getAttributeNamespace(i);
        if (nsUri != null) out.writeAttribute(in.getAttributePrefix(i), nsUri, in.getAttributeLocalName(i), value); else out.writeAttribute(in.getAttributeLocalName(i), value);
    }

    @Override
    protected void handleStartElement() throws XMLStreamException {
        QName name = in.getName();
        if (name.equals(WSDLConstants.QNAME_DEFINITIONS)) {
            String value = in.getAttributeValue(null, "targetNamespace");
            if (value != null) {
                targetNamespace = value;
            }
        } else if (name.equals(WSDLConstants.QNAME_SERVICE)) {
            String value = in.getAttributeValue(null, "name");
            if (value != null) {
                serviceName = new QName(targetNamespace, value);
            }
        } else if (name.equals(WSDLConstants.QNAME_PORT)) {
            String value = in.getAttributeValue(null, "name");
            if (value != null) {
                portName = new QName(targetNamespace, value);
            }
        } else if (name.equals(W3CAddressingConstants.WSA_ADDRESS_QNAME)) {
            eprAddressState = EPR_ADDRESS_STATE.IN;
        }
        super.handleStartElement();
    }

    @Override
    protected void handleEndElement() throws XMLStreamException {
        QName name = in.getName();
        if (name.equals(W3CAddressingConstants.WSA_ADDRESS_QNAME)) {
            eprAddressState = EPR_ADDRESS_STATE.OUT;
        }
        super.handleEndElement();
    }

    @Override
    protected void handleCharacters() throws XMLStreamException {
        if (eprAddressState == EPR_ADDRESS_STATE.IN) {
            String value = getAddressLocation();
            if (value != null) {
                logger.fine("Fixing EPR Address for service:" + serviceName + " port:" + portName + " address with " + value);
                out.writeCharacters(value);
                eprAddressState = EPR_ADDRESS_STATE.DONE;
            }
        }
        if (eprAddressState != EPR_ADDRESS_STATE.DONE) {
            super.handleCharacters();
        }
    }

    /**
     * Returns the location to be placed into the generated document.
     *
     * @param relPath relative URI to be resolved
     * @return
     *      null to leave it to the "implicit reference".
     */
    @Nullable
    private String getPatchedImportLocation(String relPath) {
        try {
            ServiceDefinitionImpl def = endpoint.getServiceDefinition();
            assert def != null;
            URL ref = new URL(current.getURL(), relPath);
            SDDocument refDoc = def.getBySystemId(ref);
            if (refDoc == null) return relPath;
            return resolver.getRelativeAddressFor(current, refDoc);
        } catch (MalformedURLException mue) {
            return null;
        }
    }

    /**
     * For the given service, port names it matches the correct endpoint and
     * reutrns its endpoint address
     *
     * @return returns the resolved endpoint address
     */
    private String getAddressLocation() {
        return (portAddressResolver == null || portName == null) ? null : portAddressResolver.getAddressFor(serviceName, portName.getLocalPart());
    }
}
