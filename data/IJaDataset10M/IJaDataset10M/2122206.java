package gov.noaa.gdsg.xmldbremote.service;

import org.xmldb.api.base.Resource;
import org.xmldb.api.modules.XMLResource;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.base.ErrorCodes;
import org.xmldb.api.base.Collection;
import gov.noaa.gdsg.xmldbremote.service.transport.ResourceTransport;
import gov.noaa.gdsg.xmldbremote.service.transport.BaseTransport;
import org.w3c.dom.Node;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.xml.sax.InputSource;
import java.io.StringWriter;
import java.io.StringReader;
import java.io.IOException;

/**
 * This object is deisnged to keep track of Resource objects
 * @version $Id: ResourceHandler.java,v 1.2 2004/12/29 01:06:46 mrxtravis Exp $
 * @author  tns
 */
public class ResourceHandler extends BaseHandler {

    /** Creates a new instance of ResourceHandler */
    public ResourceHandler() {
    }

    /**
     * Creates the Resource Transport object.
     * @param object The object in which to create a lightweight transport object.
     * @throws org.xmldb.api.base.XMLDBException IF something goes wrong.
     * @return The new transport object.
     */
    protected BaseTransport createTransportObject(Object object) throws XMLDBException {
        XMLResource resource = (XMLResource) object;
        ResourceTransport transport = new ResourceTransport();
        transport.setResourceType(resource.getResourceType());
        transport.setId(resource.getId());
        transport.setRootNodeName(resource.getContentAsDOM().getNodeName());
        return transport;
    }

    /**
     * Returns the resource content as an XML String.
     * @param transport The object representing the object in which the contents
     * will be extracted.
     * @throws org.xmldb.api.base.XMLDBException All exceptions are caught and rethrown.
     * @return A string representing the resource.
     */
    public String getContentAsDOMText(ResourceTransport transport) throws XMLDBException {
        try {
            Resource resource = (Resource) this.getObjectFromSession(transport);
            if (!(resource instanceof XMLResource)) {
                throw new XMLDBException(ErrorCodes.NOT_IMPLEMENTED, "Resource of the service must be of type XMLResource");
            }
            XMLResource xResource = (XMLResource) resource;
            Node node = xResource.getContentAsDOM();
            DOMSource source = new DOMSource();
            source.setNode(node);
            StreamResult result = new StreamResult();
            StringWriter writer = new StringWriter();
            result.setWriter(writer);
            TransformerFactory factory = new org.apache.xalan.processor.TransformerFactoryImpl();
            factory.newTransformer().transform(source, result);
            return writer.getBuffer().toString();
        } catch (javax.xml.transform.TransformerConfigurationException e) {
            throw new XMLDBException(ErrorCodes.VENDOR_ERROR, e.toString());
        } catch (javax.xml.transform.TransformerException e) {
            throw new XMLDBException(ErrorCodes.VENDOR_ERROR, e.toString());
        }
    }

    /**
     * Returns the document id.
     * @param transport The transport representing the object in which to tget a document id.
     * @throws org.xmldb.api.base.XMLDBException If the wrapped method throws one.
     * @return Whatever is returned by the wrapped method.
     */
    public String getDocumentId(ResourceTransport transport) throws XMLDBException {
        XMLResource resource = (XMLResource) this.getObjectFromSession(transport);
        return resource.getDocumentId();
    }

    /**
     * Sets the resource content as the specified DOM String.
     * @param transport The transport object representing the actual object.
     * @param xml The XML to set as DOM.
     * @throws org.xmldb.api.base.XMLDBException All exceptions are caught and rethrown.
     */
    public void setContentAsDOMText(ResourceTransport transport, String xml) throws XMLDBException {
        try {
            XMLResource resource = (XMLResource) this.getObjectFromSession(transport);
            Node doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new StringReader(xml)));
            resource.setContentAsDOM(doc);
        } catch (javax.xml.parsers.ParserConfigurationException e) {
            throw new XMLDBException(ErrorCodes.VENDOR_ERROR, e.toString());
        } catch (org.xml.sax.SAXException e) {
            throw new XMLDBException(ErrorCodes.VENDOR_ERROR, e.toString());
        } catch (java.io.IOException e) {
            throw new XMLDBException(ErrorCodes.VENDOR_ERROR, e.toString());
        }
    }

    /**
     * Wrappes the {@see Resrouce#getParentCollection} method.
     * @param transport The object representing the actual object to work on.
     * @throws org.xmldb.api.base.XMLDBException If the wrapped method throws one.
     * @return Whatever the mapped method throws.
     */
    public Collection getParentCollection(ResourceTransport transport) throws XMLDBException {
        Resource resource = (Resource) this.getObjectFromSession(transport);
        return resource.getParentCollection();
    }
}
