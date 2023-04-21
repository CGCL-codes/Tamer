package com.esri.gpt.server.csw.provider.local;

import com.esri.gpt.catalog.discovery.Discoverable;
import com.esri.gpt.framework.util.Val;
import com.esri.gpt.server.csw.provider.components.OperationContext;
import com.esri.gpt.server.csw.provider.components.OwsException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import org.w3c.dom.Node;

/**
 * Super-class for adapting CSW operation components to the local discovery model.
 */
public class DiscoveryAdapter {

    /** instance variables ====================================================== */
    private DiscoveryContext discoveryContext;

    /** 
   * Constructs with a supplied operation context
   * @param context the operation context
   */
    public DiscoveryAdapter(OperationContext context) {
        String key = "DiscoveryAdapter.DiscoveryContext";
        DiscoveryContext dCtx = (DiscoveryContext) context.getAdditionalProperties().get(key);
        if (dCtx == null) {
            dCtx = new DiscoveryContext(context);
            context.getAdditionalProperties().put(key, dCtx);
        }
        this.discoveryContext = dCtx;
    }

    /**
   * Gets the active discovery context.
   * @return the active discovery context
   */
    public DiscoveryContext getDiscoveryContext() {
        return this.discoveryContext;
    }

    /**
   * Parses a parent node for an associated property name (ogc:PropertyName).
   * @param parent the parent of the property to locate
   * @param xpath an XPath to enable queries (properly configured with name spaces)
   * @return a discoverable associated with the property name
   * @throws OwsException if validation fails
   * @throws XPathExpressionException if an XPath related exception occurs
   */
    public Discoverable parsePropertyName(Node parent, XPath xpath) throws OwsException, XPathExpressionException {
        String locator = "PropertyName";
        Node ndPropName = (Node) xpath.evaluate("ogc:PropertyName", parent, XPathConstants.NODE);
        if (ndPropName == null) {
            String msg = "The parameter was not found";
            throw new OwsException(OwsException.OWSCODE_MissingParameterValue, locator, msg);
        }
        String sPropName = Val.chkStr(ndPropName.getTextContent());
        if (sPropName.length() == 0) {
            String msg = "The parameter value was empty.";
            throw new OwsException(OwsException.OWSCODE_InvalidParameterValue, locator, msg);
        }
        Discoverable discoverable = this.getDiscoveryContext().findDiscoverable(sPropName);
        if (discoverable == null) {
            String msg = sPropName + " is not a supported queryable.";
            throw new OwsException(OwsException.OWSCODE_InvalidParameterValue, locator, msg);
        }
        return discoverable;
    }
}
