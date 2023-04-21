package org.basegen.base.communication.axis.persistence.query;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.apache.axis.encoding.DeserializationContext;
import org.apache.axis.encoding.DeserializerImpl;
import org.apache.axis.message.SOAPHandler;
import org.basegen.base.persistence.query.FetchJoin;
import org.basegen.base.communication.axis.AxisUtils;

/**
 * Base Iterator Serializer.
 */
public class FetchJoinDeserializer extends DeserializerImpl {

    /**
     * Fetch join
     */
    private FetchJoin fetchJoin;

    /**
     * Join
     */
    private String join;

    /**
     * Default constructor
     */
    public FetchJoinDeserializer() {
        fetchJoin = new FetchJoin();
        value = fetchJoin;
    }

    /**
     * On start child
     * @param namespace namespace
     * @param localName local name
     * @param prefix prefix
     * @param attributes attributes
     * @param context context
     * @return soap handler
     * @throws SAXException sax exception
     */
    public SOAPHandler onStartChild(String namespace, String localName, String prefix, Attributes attributes, DeserializationContext context) throws SAXException {
        if (localName.equals("join")) {
            join = AxisUtils.getValue(context);
        } else if (localName.equals("alias")) {
            String alias = AxisUtils.getValue(context);
            fetchJoin.add(join, alias);
        }
        return null;
    }
}
