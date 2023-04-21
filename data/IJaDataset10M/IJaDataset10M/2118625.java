package org.apache.ws.jaxme.xs.impl;

import org.apache.ws.jaxme.xs.XSObject;
import org.apache.ws.jaxme.xs.XSOpenAttrs;
import org.apache.ws.jaxme.xs.xml.XsTOpenAttrs;
import org.xml.sax.Attributes;

/**
 * @author <a href="mailto:joe@ispsoft.de">Jochen Wiedmann</a>
 */
public class XSOpenAttrsImpl extends XSObjectImpl implements XSOpenAttrs {

    private final Attributes openAttributes;

    public XSOpenAttrsImpl(XSObject pParent, XsTOpenAttrs pXsOpenAttrs) {
        super(pParent, pXsOpenAttrs);
        openAttributes = pXsOpenAttrs.getOpenAttributes();
    }

    public Attributes getOpenAttributes() {
        return openAttributes;
    }
}
