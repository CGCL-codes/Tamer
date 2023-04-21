package org.apache.ws.jaxme.xs.xml.impl;

import org.apache.ws.jaxme.xs.xml.XsObject;

/**
 * @author <a href="mailto:joe@ispsoft.de">Jochen Wiedmann</a>
 */
public class XsTLocalAllElementImpl extends XsTLocalElementImpl {

    protected XsTLocalAllElementImpl(XsObject pParent) {
        super(pParent);
    }

    public void setMaxOccurs(String pValue) {
        boolean valid = !"unbounded".equals(pValue);
        if (valid) {
            try {
                int i = Integer.parseInt(pValue);
                valid = i == 0 || i == 1;
            } catch (Exception e) {
                valid = false;
            }
        }
        if (!valid) {
            throw new IllegalArgumentException("Invalid value for 'maxOccurs': " + pValue + "; must be 0 or 1");
        }
        super.setMaxOccurs(pValue);
    }

    public void setMinOccurs(int pValue) {
        if (pValue != 0 && pValue != 1) {
            throw new IllegalArgumentException("Invalid value for 'minOccurs': " + pValue + "; must be 0 or 1");
        }
        super.setMinOccurs(pValue);
    }
}
