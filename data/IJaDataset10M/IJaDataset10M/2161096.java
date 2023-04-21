package net.sf.traser.common;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.impl.builder.StAXOMBuilder;

/**
 * This class represents a message type, it delegates methods to the 
 * encapsulated uri corresponding to the namespace and location of the schema of 
 * the message type.
 * @author szmarcell
 */
public class MessageType {

    /**
     * This is the field holding the value of the query used to obtain the 
     * response for the message. If there is no response, this should be null.
     */
    private String responseQuery;

    /**
     * This is the set of propagations that are to be created when the message 
     * is received.
     */
    private Set<String> propagationQueries;

    /**
     * This is the schema of the message.
     */
    private Direction schema;

    /**
     * This is the field indicating whether the message needs to be stored or 
     * not.
     */
    private boolean needsStoring;

    /**
     * This is the schema of the response message. The response schema may not 
     * be present at all, for example in case of updates.
     */
    private Direction responseSchema;

    /**
     * Creates an instance of MessageType, setting the underlying QName to type.
     * @param type the QName the message type is representing.
     */
    public MessageType(QName type) {
        this(type, null, null, false, null);
    }

    /**
     * Creates an instance of MessageType, setting the underlying QName to type.
     * @param type the QName the message type is representing.
     * @param storing flag indicating whether the message type needs to be 
     * stored
     */
    public MessageType(QName type, boolean storing) {
        this(type, null, null, storing, null);
    }

    /**
     * Creates an instance of MessageType, setting the underlying QName to type.
     * @param type the QName the message type is representing.
     * @param storing flag indicating whether the message type needs to be
     * stored.
     * @param propagation the set of queries producing the messages to be 
     * propagated.
     */
    public MessageType(QName type, boolean storing, Set<String> propagation) {
        this(type, null, null, storing, propagation);
    }

    /**
     * Creates an instance of MessageType, setting the underlying QName to type.
     * @param type the QName the message type is representing.
     * @param response the XQuery producing the response
     * @param responseType the qualified name of the response element
     * @param storing flag indicating whether the message type needs to be
     * stored
     * @param propagation the set of queries producing the messages to be 
     * propagated
     */
    public MessageType(QName type, String response, QName responseType, boolean storing, Set<String> propagation) {
        responseQuery = response;
        propagationQueries = propagation;
        schema = new Direction(type);
        if (responseType != null) {
            responseSchema = new Direction(responseType);
        } else {
            responseSchema = null;
        }
        needsStoring = storing;
    }

    /**
     * The class corresponding to a schema. It either stores the schema in 
     * memory, or obtains it from the specified URL at access time.
     */
    public static class Direction {

        /**
         * The schema represented as an XML document. 
         */
        private OMElement schema;

        /**
         * The location where the schema can be obtained from. 
         */
        private URL location;

        /**
         * This is the qualified name of the operation.
         */
        private QName name;

        /**
         * Creates a new Schema object from the specified XML document.
         * @param elementName the qualified name of the element to obtain the 
         * schema of.
         */
        public Direction(QName elementName) {
            schema = null;
            location = null;
            name = elementName;
            if (elementName != null) {
                try {
                    location = new URL(elementName.getNamespaceURI());
                } catch (MalformedURLException ex) {
                    Logger.getLogger(MessageType.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        /**
         * Returns the schema. If the schema is not present it tries to obtain 
         * it from the location specified. If it succeeds, it stores the schema 
         * in memory.
         * @return the schema as an XML document.
         */
        public OMElement getSchema() {
            if (schema == null) {
                if (location != null) {
                    try {
                        schema = new StAXOMBuilder(location.openStream()).getDocumentElement();
                    } catch (XMLStreamException ex) {
                        Logger.getLogger(MessageType.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(MessageType.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
            return schema;
        }

        /**
         * This method returns the qualified name of the operation.
         * @return the QName of the operation.
         */
        public QName getName() {
            return name;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return schema.name.hashCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object ob) {
        if (ob instanceof MessageType) {
            return schema.name.equals(((MessageType) ob).schema.name);
        } else {
            return false;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return schema.name.toString();
    }

    /**
     * Tells whether the message needs to be stored or not.
     * @return true if the message needs to be stored
     */
    public boolean isUpdate() {
        return needsStoring;
    }

    /**
     * Returns the XQuery to be evaluated to produce the response to the 
     * message.
     * @return the XQuery to evaluate.
     */
    public String getResponseXQuery() {
        return responseQuery;
    }

    /**
     * @return the inbound message direction
     */
    public Direction getInboundMessageDeclaration() {
        return schema;
    }

    /**
     * @return the response message direction
     */
    public Direction getResponseMessageDeclaration() {
        return responseSchema;
    }

    /**
     * Returns the set of XQuery queries producing the messages to be 
     * propagated.
     * @return the set of queries in String format.
     */
    public Collection<String> getPropagationXQueries() {
        return propagationQueries;
    }

    /**
     * This method returns the message type.
     * @return the namespace qualified name of the message type.
     */
    public QName getTypeName() {
        return schema.name;
    }
}
