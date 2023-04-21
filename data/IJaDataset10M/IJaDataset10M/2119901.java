package org.exist.util.serializer;

import java.io.Writer;
import java.util.Properties;
import javax.xml.transform.TransformerException;
import org.exist.memtree.NodeImpl;
import org.exist.memtree.ReferenceNode;
import org.exist.storage.DBBroker;
import org.exist.storage.serializers.Serializer;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;

/**
 * @author wolf
 */
public class ExtendedDOMSerializer extends DOMSerializer {

    private DBBroker broker;

    /**
     * 
     */
    public ExtendedDOMSerializer(DBBroker broker) {
        super();
        this.broker = broker;
    }

    /**
     * @param writer
     * @param outputProperties
     */
    public ExtendedDOMSerializer(DBBroker broker, Writer writer, Properties outputProperties) {
        super(writer, outputProperties);
        this.broker = broker;
    }

    @Override
    protected void startNode(Node node) throws TransformerException {
        if (node.getNodeType() == NodeImpl.REFERENCE_NODE) {
            SAXSerializer handler = (SAXSerializer) SerializerPool.getInstance().borrowObject(SAXSerializer.class);
            handler.setReceiver(receiver);
            Serializer serializer = broker.getSerializer();
            serializer.setSAXHandlers(handler, handler);
            try {
                serializer.setProperties(outputProperties);
                serializer.setProperty(Serializer.GENERATE_DOC_EVENTS, "false");
            } catch (SAXNotRecognizedException e) {
            } catch (SAXNotSupportedException e) {
            }
            try {
                serializer.toSAX(((ReferenceNode) node).getReference());
            } catch (SAXException e) {
                throw new TransformerException(e.getMessage(), e);
            } finally {
                SerializerPool.getInstance().returnObject(handler);
            }
        } else super.startNode(node);
    }
}
