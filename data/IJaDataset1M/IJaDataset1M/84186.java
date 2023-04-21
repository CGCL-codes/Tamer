package org.apache.ws.jaxme.xs.xml.impl;

import org.apache.ws.jaxme.xs.parser.impl.LocSAXException;
import org.apache.ws.jaxme.xs.xml.XsETotalDigits;
import org.apache.ws.jaxme.xs.xml.XsObject;
import org.xml.sax.SAXException;

/** <p>Implementation of <code>xs:totalDigits</code>,
 * following this specification:
 * <pre>
 *  &lt;xs:element name="totalDigits" id="totalDigits"&gt;
 *    &lt;xs:annotation&gt;
 *      &lt;xs:documentation
 *        source="http://www.w3.org/TR/xmlschema-2/#element-totalDigits"/&gt;
 *    &lt;/xs:annotation&gt;
 *    &lt;xs:complexType&gt;
 *      &lt;xs:complexContent&gt;
 *        &lt;xs:restriction base="xs:numFacet"&gt;
 *          &lt;xs:sequence&gt;
 *            &lt;xs:element ref="xs:annotation" minOccurs="0"/&gt;
 *          &lt;/xs:sequence&gt;
 *          &lt;xs:attribute name="value" type="xs:positiveInteger" use="required"/&gt;
 *        &lt;/xs:restriction&gt;
 *      &lt;/xs:complexContent&gt;
 *    &lt;/xs:complexType&gt;
 *  &lt;/xs:element&gt;
 * </pre></p>
 *
 * @author <a href="mailto:joe@ispsoft.de">Jochen Wiedmann</a>
 */
public class XsETotalDigitsImpl extends XsTNumFacetImpl implements XsETotalDigits {

    protected XsETotalDigitsImpl(XsObject pParent) {
        super(pParent);
    }

    public void setValue(long pValue) throws SAXException {
        if (pValue <= 0) {
            throw new LocSAXException("The 'value' attribute must be > 0.", getLocator());
        }
        super.setValue(pValue);
    }

    public String getFacetName() {
        return "totalDigits";
    }
}
