package org.oasis.wsrp.test.impl.message;

import java.util.HashMap;
import java.util.Map;
import javax.xml.transform.TransformerException;
import org.eclipse.wst.wsi.internal.core.WSIException;
import org.eclipse.wst.wsi.internal.core.profile.validator.impl.BaseMessageValidator;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * The locales with values in PortletDescriptionResponse.resourceList 
 * should all be in the list of locales requested via the desiredLocales 
 * parameter.
 *
 * @author Julie MacNaught (jmacna@us.ibm.com)
 * @author Martin Fanta (Martin.Fanta@cz.ibm.com)
 *
 */
public class RP1130 extends BaseRP {

    public RP1130(BaseMessageValidator impl) {
        super(impl);
    }

    protected Map desiredLocalesMap = new HashMap();

    protected void processGetPortletDescriptionRequest(Document reqDoc) throws WSIException, TransformerException {
        NodeList desiredLocalesNodes = NodeUtils.getTextNodes(reqDoc, "desiredLocales");
        int desiredLocalesCount = desiredLocalesNodes.getLength();
        Node desiredLocaleNode;
        String desiredLocale;
        for (int ii = 0; ii < desiredLocalesCount; ii++) {
            desiredLocaleNode = desiredLocalesNodes.item(ii);
            if (desiredLocaleNode != null) {
                desiredLocale = desiredLocaleNode.getNodeValue();
                desiredLocalesMap.put(desiredLocale, desiredLocale);
            }
        }
    }

    protected void processGetPortletDescriptionResponse(Document respDoc) throws WSIException, TransformerException {
        Node resListNode = NodeUtils.getNode(respDoc, "resourceList");
        if (resListNode != null) {
            int failedCount = 0;
            NodeList valuesNodes = NodeUtils.getNodes(resListNode, "values");
            int valueCount = valuesNodes.getLength();
            Node valueNode;
            String lang;
            for (int ii = 0; ii < valueCount; ii++) {
                valueNode = valuesNodes.item(ii);
                lang = NodeUtils.getAttributeValue(valueNode, "xml:lang");
                if (desiredLocalesMap.get(lang) == null) {
                    failedCount++;
                }
            }
            if (failedCount > 0) {
                warn("" + failedCount + " value(s) from the resourceList were in a locale not specified among desiredLocales");
            } else if (valueCount > 0) {
                pass();
            }
        }
    }
}
