package net.interfax.impl;

import java.util.HashMap;
import java.util.Map;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @see http://www.interfax.net/en/dev/webservice/reference/faxstatus
 * @author DL
 */
public class SendCharFaxResponse implements XmlDeserializable {

    public SendCharFaxResponse() {
    }

    /**
	 * In case of successful submission - the value contains the TransactionID.
	 * In case of a failure, a negative value is returned.
	 * @see http://www.interfax.net/en/dev/webservice/reference/web-service-return-codes
	 */
    public long sendCharFaxResult;

    @Override
    public void fromXml(Node node) {
        final NodeList childNodes = node.getChildNodes();
        final int numChildNodes = childNodes.getLength();
        final Map<String, String> values = new HashMap<String, String>(numChildNodes);
        for (int i = 0; i < numChildNodes; ++i) {
            final Node childNode = childNodes.item(i);
            values.put(childNode.getNodeName(), childNode.getTextContent());
        }
        sendCharFaxResult = Long.parseLong(values.get("SendCharFaxResult"));
    }
}
