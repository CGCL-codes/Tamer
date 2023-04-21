package oracle.toplink.essentials.internal.ejb.cmp3.xml.queries;

import oracle.toplink.essentials.internal.ejb.cmp3.metadata.MetadataLogger;
import oracle.toplink.essentials.internal.ejb.cmp3.metadata.queries.MetadataQueryHint;
import oracle.toplink.essentials.internal.ejb.cmp3.metadata.queries.MetadataNamedQuery;
import oracle.toplink.essentials.internal.ejb.cmp3.xml.XMLHelper;
import oracle.toplink.essentials.internal.ejb.cmp3.xml.XMLConstants;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Object to hold onto a named query metadata that came from XML.
 * 
 * @author Guy Pelletier
 * @since TopLink EJB 3.0 Reference Implementation
 */
public class XMLNamedQuery extends MetadataNamedQuery {

    /**
     * INTERNAL:
     */
    public XMLNamedQuery(Node node, XMLHelper helper) {
        setLocation(helper.getDocumentName());
        setName(helper.getNodeValue(node, XMLConstants.ATT_NAME));
        setEJBQLString(helper.getNodeTextValue(node, XMLConstants.QUERY));
        NodeList hints = helper.getNodes(node, XMLConstants.QUERY_HINT);
        if (hints != null) {
            for (int i = 0; i < hints.getLength(); i++) {
                Node hintNode = hints.item(i);
                String name = helper.getNodeValue(hintNode, XMLConstants.ATT_NAME);
                String value = helper.getNodeValue(hintNode, XMLConstants.ATT_VALUE);
                addHint(new MetadataQueryHint(name, value));
            }
        }
    }

    /**
     * INTERNAL:
     */
    public String getIgnoreLogMessageContext() {
        return MetadataLogger.IGNORE_NAMED_QUERY_ELEMENT;
    }

    /**
     * INTERNAL:
     */
    public boolean loadedFromAnnotations() {
        return false;
    }

    /**
     * INTERNAL:
     */
    public boolean loadedFromXML() {
        return true;
    }
}
