package org.openliberty.arisid.policy.application;

import java.net.URI;
import java.net.URISyntaxException;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import org.apache.axiom.om.OMAttribute;
import org.apache.axiom.om.OMElement;
import org.apache.neethi.PolicyComponent;
import org.openliberty.arisid.stack.PolicyException;

public class DataDisplayMaskConstraint extends PrivacyConstraint {

    private static final String INVALID_MASK_ASSERTION_URI = "Invalid Data Display Mask Constraint URI";

    protected static final String NAMESPACE = PolicyUtil.AppIdPolicy_NS;

    protected static final String ELEMENT = "DataDisplayMaskConstraint";

    protected static final String VALUE_ATTR = "Pattern";

    public static final QName qelement = new QName(NAMESPACE, ELEMENT);

    public static final String URI_PROPAGATE_REQUESTOR = "urn:liberty:names:1.0:igf:pri:propagate:requestor";

    private final String _mask;

    public DataDisplayMaskConstraint(String maskPattern) {
        this._mask = maskPattern;
        this._issuer = null;
    }

    public DataDisplayMaskConstraint(String maskPattern, URI issuer) {
        this._mask = maskPattern;
        this._issuer = issuer;
    }

    public DataDisplayMaskConstraint(OMElement element) throws PolicyException {
        OMAttribute attr = element.getAttribute(new QName(VALUE_ATTR));
        if (attr != null) {
            String pattern = attr.getAttributeValue();
            if (pattern != null) {
                this._mask = pattern;
            } else this._mask = null;
        } else throw new PolicyException("Invalid DataDisplayMaskConstraint. Missing 'Pattern' attribute.");
        attr = element.getAttribute(new QName(ISSUER_ATTR));
        if (attr != null) {
            String issuer = attr.getAttributeValue();
            if (issuer != null) {
                try {
                    this._issuer = new URI(issuer);
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                    throw new PolicyException(INVALID_ISSUER_URI);
                }
            } else this._issuer = null;
        } else this._issuer = null;
    }

    public String getDataDisplayMask() {
        return this._mask;
    }

    public QName getName() {
        return qelement;
    }

    public PolicyComponent normalize() {
        return this;
    }

    public void serialize(XMLStreamWriter xmlwriter) throws XMLStreamException {
        super.serialize(xmlwriter);
        if (this._mask != null) {
            xmlwriter.writeStartElement(NAMESPACE, ELEMENT);
            xmlwriter.writeAttribute(VALUE_ATTR, this._mask.toString());
            if (this.getIssuerUri() != null) {
                xmlwriter.writeAttribute(ISSUER_ATTR, this.getIssuerUri().toString());
            }
            xmlwriter.writeEndElement();
        } else throw new XMLStreamException(INVALID_MASK_ASSERTION_URI);
    }

    public boolean equal(PolicyComponent policyAssertion) {
        if (!policyAssertion.getClass().equals(this.getClass())) return false;
        DataDisplayMaskConstraint pa = (DataDisplayMaskConstraint) policyAssertion;
        return (this.getDataDisplayMask().equals(pa.getDataDisplayMask()));
    }

    public String toString() {
        return "DataDisplayMaskConstraint: pattern=" + (this._mask == null ? "undefined" : this._mask) + " issuer=" + (this._issuer == null ? "undefined" : this._issuer);
    }
}
