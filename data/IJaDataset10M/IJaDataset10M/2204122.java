package org.apache.axis.deployment.wsdd;

import org.w3c.dom.Element;
import javax.xml.namespace.QName;

/**
 *
 */
public class WSDDFaultFlow extends WSDDChain {

    /**
     * Default constructor
     */
    public WSDDFaultFlow() {
    }

    /**
     *
     * @param e (Element) XXX
     * @throws WSDDException XXX
     */
    public WSDDFaultFlow(Element e) throws WSDDException {
        super(e);
    }

    protected QName getElementName() {
        return WSDDConstants.QNAME_FAULTFLOW;
    }
}
