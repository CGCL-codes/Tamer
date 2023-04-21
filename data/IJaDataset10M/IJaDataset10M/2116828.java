package net.sf.traser.messaging;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import net.sf.traser.messaging.XsdConstants;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.impl.builder.StAXOMBuilder;

/**
 * This class represents a message type, it delegates methods to the 
 * encapsulated uri corresponding to the namespace and location of the schema of 
 * the message type.
 * @author Marcell Szathmári
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
     * The description of this 
     */
    private final OMElement annotation;

    /**
     * The soap action of the message type.
     */
    private final QName soapAction;

    /**
     * Message type is not default constructible.
     */
    private MessageType() {
        throw new AssertionError("Message type is not default constructible.");
    }

    /**
     * 
     * @param msgTypeElement
     */
    public MessageType(OMElement msgTypeElement) {
        QName msgTypeName;
        QName respTypeName;
        OMElement tmpInElement = msgTypeElement.getFirstChildWithName(new QName("element-in"));
        msgTypeName = new QName(tmpInElement.getAttributeValue(new QName("schema")), tmpInElement.getText());
        schema = new Direction(msgTypeName);
        respTypeName = null;
        OMElement tmpOutElement = msgTypeElement.getFirstChildWithName(new QName("element-out"));
        if (tmpOutElement != null) {
            respTypeName = new QName(tmpOutElement.getAttributeValue(new QName("schema")), tmpOutElement.getText());
        }
        if (respTypeName != null) {
            responseSchema = new Direction(respTypeName);
        } else {
            responseSchema = null;
        }
        needsStoring = false;
        if ("true".equals(msgTypeElement.getAttributeValue(new QName("store")))) {
            needsStoring = true;
        }
        OMElement result = msgTypeElement.getFirstChildWithName(new QName("response"));
        responseQuery = null;
        if (result != null) {
            responseQuery = obtainQuery(result);
        }
        Iterator<?> propIter = msgTypeElement.getChildrenWithName(new QName("propagation"));
        propagationQueries = new LinkedHashSet<String>();
        OMElement propag;
        while (propIter.hasNext()) {
            propag = (OMElement) propIter.next();
            String proQuery = obtainQuery(propag);
            propagationQueries.add(proQuery);
        }
        annotation = msgTypeElement.getFirstChildWithName(XsdConstants.ANN);
        OMElement actionEl = msgTypeElement.getFirstChildWithName(new QName("action"));
        if (actionEl == null) {
            soapAction = new QName("urn:service");
        } else {
            soapAction = QName.valueOf(actionEl.getText());
        }
    }

    /**
     * Decodes an Xquery expression from the config file. Decoding is necessary
     * because the exression can contain the <code><</code> and <code>></code> 
     * comparison operators that otherwise would confuse the XML parser.
     * 
     * The <,<=,=,!=,>=,> arithmetic operators should be replaced in the 
     * configuration file with the &lt;lt>, &lt;lte>, &lt;eq>, &lt;ne>, &lt;gte>, &lt;gt> 
     * @param el the configuration entry of an XQuery expression.
     * @return the expression in proper string representation.
     */
    private String obtainQuery(OMElement el) {
        return el.getText();
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
     * The index of the message type in the message catalog that is consistent 
     * throughout the current run.
     */
    private int catalogIndex;

    /**
     * Set the catalog index of this message type.
     * @param index the index of this message type in the message catalog 
     * containing it.
     */
    public void setCatalogIndex(int index) {
        catalogIndex = index;
    }

    /**
     * Get the catalog index of this message type.
     * @return the index of this message type in the message catalog containing 
     * it.
     */
    public int getCatalogIndex() {
        return catalogIndex;
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
     * Returns the set of XQuery queries producing the identifiers 
     * the message needs to be propagated to.
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

    /**
     * Returns the documentation element for a given locale, or for the english 
     * locale if not found, or one without a language specification if none is
     * found.
     * @param locale the locale to find the documentation for.
     * @return the documentation element.
     */
    private OMElement getDocumentation(Locale locale) {
        if (annotation == null) {
            return null;
        }
        OMElement def = null;
        Iterator<OMElement> docs = annotation.getChildrenWithName(XsdConstants.DOC);
        String language = locale.getLanguage();
        while (docs.hasNext()) {
            OMElement doc = docs.next();
            String lang = doc.getAttributeValue(XsdConstants.LANG);
            if (lang == null) {
                if (def == null) {
                    def = doc;
                }
            } else {
                if (language.equals(lang)) {
                    return doc;
                } else if ("en".equals(lang)) {
                    def = doc;
                }
            }
        }
        return def;
    }

    /**
     * Returns a short descriptive name for the message type to be displayed on
     * user forms.
     * @param locale 
     * @return a short descriptive name.
     */
    public String getShortName(Locale locale) {
        OMElement doc = getDocumentation(locale);
        if (doc == null) {
            return schema.name.getLocalPart();
        }
        OMElement name = doc.getFirstChildWithName(XsdConstants.CCTS_NAME);
        if (name == null) {
            return schema.name.getLocalPart();
        } else {
            return name.getText();
        }
    }

    /**
     * Returns the description of this message type.
     * @param locale 
     * @return
     */
    public String getDescription(Locale locale) {
        OMElement doc = getDocumentation(locale);
        if (doc == null) {
            return "";
        }
        OMElement desc = doc.getFirstChildWithName(XsdConstants.CCTS_DEFINITION);
        if (desc == null) {
            return doc.getText();
        } else {
            return desc.getText();
        }
    }

    /**
     * Returns the soap action associated with this message type.
     * @return the QName of the action
     */
    public QName getAction() {
        return soapAction;
    }
}
