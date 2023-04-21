package org.apache.ws.jaxme.xs.xml.impl;

import org.apache.ws.jaxme.xs.xml.XsAGOccurs;
import org.apache.ws.jaxme.xs.xml.XsEAny;
import org.apache.ws.jaxme.xs.xml.XsObject;

/** <p>Implementation of the <code>xs:any</code> element, as
 * specified by the following:
 * <pre>
 *  &lt;xs:element name="any" id="any"&gt;
 *    &lt;xs:annotation&gt;
 *      &lt;xs:documentation source="http://www.w3.org/TR/xmlschema-1/#element-any"/&gt;
 *    &lt;/xs:annotation&gt;
 *    &lt;xs:complexType&gt;
 *      &lt;xs:complexContent&gt;
 *        &lt;xs:extension base="xs:wildcard"&gt;
 *          &lt;xs:attributeGroup ref="xs:occurs"/&gt;
 *        &lt;/xs:extension&gt;
 *      &lt;/xs:complexContent&gt;
 *    &lt;/xs:complexType&gt;
 *  &lt;/xs:element&gt;
 * </pre></p>
 * @author <a href="mailto:joe@ispsoft.de">Jochen Wiedmann</a>
 */
public class XsEAnyImpl extends XsTWildcardImpl implements XsEAny {

    private XsAGOccurs occurs = getObjectFactory().newXsAGOccurs(this);

    protected XsEAnyImpl(XsObject pParent) {
        super(pParent);
    }

    public void setMaxOccurs(String pMaxOccurs) {
        occurs.setMaxOccurs(pMaxOccurs);
    }

    public int getMaxOccurs() {
        return occurs.getMaxOccurs();
    }

    public void setMinOccurs(int pMinOccurs) {
        occurs.setMinOccurs(pMinOccurs);
    }

    public int getMinOccurs() {
        return occurs.getMinOccurs();
    }
}
