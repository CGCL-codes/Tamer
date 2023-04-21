package it.imolinfo.jbi4ejb.processor;

import it.imolinfo.jbi4ejb.Logger;
import it.imolinfo.jbi4ejb.LoggerFactory;
import it.imolinfo.jbi4ejb.exception.Jbi4EjbException;
import it.imolinfo.jbi4ejb.jbi.endpoint.Jbi4EjbEndpoint;
import java.util.Iterator;
import javax.jbi.messaging.NormalizedMessage;
import javax.wsdl.Message;
import javax.wsdl.Operation;
import javax.wsdl.Port;
import javax.wsdl.PortType;
import javax.wsdl.Service;
import javax.xml.namespace.QName;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.sun.jbi.nms.wsdl11wrapper.HelperFactory;
import com.sun.jbi.nms.wsdl11wrapper.WrapperParser;
import com.sun.jbi.nms.wsdl11wrapper.WrapperProcessingException;

/**
 *  Message denormalizer class. Extract from the NRM message the message part.
 * 
 * @author <a href="mailto:mpiraccini@imolinfo.it">Marco Piraccini</a>
 */
public class Jbi4EjbDenormalizer {

    /** The Constant LOG. */
    private static final Logger LOG = LoggerFactory.getLogger(Jbi4EjbDenormalizer.class);

    /** The wrapper parser. */
    private WrapperParser wrapperParser = null;

    /** The m trans. */
    private Transformer mTrans = null;

    /**
     * Instantiates a new denormalizer.
     *             
     * @throws Jbi4EjbException
     *                       if some problem occurs in denormalizing 
     */
    public Jbi4EjbDenormalizer() throws Jbi4EjbException {
        try {
            wrapperParser = HelperFactory.createParser();
        } catch (WrapperProcessingException ex) {
            throw new Jbi4EjbException("Failed to create WrapperParser", ex);
        }
        try {
            TransformerFactory factory = TransformerFactory.newInstance();
            mTrans = factory.newTransformer();
        } catch (TransformerFactoryConfigurationError ex) {
            String msg = ex.getMessage();
            LOG.error(msg);
            throw new Jbi4EjbException(ex);
        } catch (TransformerConfigurationException e) {
            String msg = e.getMessage();
            LOG.error(msg);
            throw new Jbi4EjbException(e);
        }
    }

    /**denormalize
     * Denormalize the message to the source. This implementation takes the
     * first (shuold be th only) part
     * 
     * @param normalizedMessage
     *            the message to denormalize
     * @param endpoint
     *            the endpoint invoked
     * @param operation
     *            the opration invoked
     * 
     * @return the <code>Jbi4EjbMessage containing the source.
     * 
     * @throws Jbi4EjbException
     *             if some problem occurs in message denormalizing
     */
    public Jbi4EjbMessage denormalize(NormalizedMessage normalizedMessage, Jbi4EjbEndpoint endpoint, QName operation) throws Jbi4EjbException {
        try {
            final Service service = endpoint.getDefinition().getService(endpoint.getServiceName());
            final Port port = service.getPort(QName.valueOf(endpoint.getEndpointName()).getLocalPart());
            final PortType portType = port.getBinding().getPortType();
            final Iterator it = portType.getOperations().iterator();
            Message wsdlMessage = null;
            while (it.hasNext()) {
                final Operation op = (Operation) it.next();
                if (op.getName().equals(operation.toString()) || op.getName().equals(operation.getLocalPart())) {
                    wsdlMessage = op.getInput().getMessage();
                }
            }
            final DOMResult result = new DOMResult();
            final Source src = normalizedMessage.getContent();
            if (src != null) {
                final TransformerFactory fact = TransformerFactory.newInstance();
                final Transformer transformer = fact.newTransformer();
                transformer.transform(src, result);
            }
            Node node = result.getNode();
            Document normalizedDoc = null;
            if (node instanceof Document) {
                normalizedDoc = (Document) node;
            } else {
                normalizedDoc = ((Element) node).getOwnerDocument();
            }
            wrapperParser.parse(normalizedDoc, endpoint.getDefinition());
            Source source = normalizedMessage.getContent();
            if (source instanceof DOMSource) {
                node = ((DOMSource) source).getNode();
            } else {
                DOMResult domResult = new DOMResult();
                mTrans.transform(source, domResult);
                node = domResult.getNode();
            }
            if (node instanceof Document) {
                wrapperParser.parse((Document) node, wsdlMessage);
            } else {
                wrapperParser.parse(node.getOwnerDocument(), wsdlMessage);
            }
            if (wrapperParser.getNoOfParts() != 0) {
                String[] partNames = wrapperParser.getPartNames();
                NodeList nodes = wrapperParser.getPartNodes(partNames[0]);
                if (nodes == null || nodes.getLength() == 0) {
                    throw new Jbi4EjbException("Unable to find valid part during denormalization");
                }
                return new Jbi4EjbMessage(new DOMSource(nodes.item(0)), true);
            } else {
                return new Jbi4EjbMessage(new DOMSource((Document) node), false);
            }
        } catch (TransformerException ex) {
            String msg = ex.getMessage();
            LOG.error(msg);
            throw new Jbi4EjbException(ex);
        } catch (WrapperProcessingException ex) {
            String msg = ex.getMessage();
            LOG.error(msg);
            throw new Jbi4EjbException(ex);
        }
    }
}
