package org.openliberty.arisid.policy.neethi;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import org.apache.axiom.om.OMAttribute;
import org.apache.axiom.om.OMElement;
import org.apache.neethi.PolicyComponent;
import org.openliberty.arisid.policy.ILocalizationConstraint;
import org.openliberty.arisid.stack.PolicyException;

public class LocalizationConstraint extends PrivacyConstraint implements ILocalizationConstraint {

    private static final String INVALID_PROPAGATE_ASSERTION_URI = "Invalid Propagate Constraint URI";

    /**
	 * Creates a localization constraint request.
	 * 
	 * @param languageCode
	 *            Language token as defined by RFC3066
	 *            http://www.w3.org/TR/xmlschema-2/#RFC3066
	 * @throws PolicyException
	 *             LanguageCode must not be null or empty string.
	 */
    public static LocalizationConstraint createLocalizationConstraint(String languageCode) throws PolicyException {
        return new LocalizationConstraint(languageCode);
    }

    private final String _language;

    /**
	 * Defines a localization constraint request.
	 * 
	 * @param languageCode
	 *            Language token as defined by RFC3066
	 *            http://www.w3.org/TR/xmlschema-2/#RFC3066
	 * @throws PolicyException
	 *             LanguageCode must not be null or empty string.
	 */
    private LocalizationConstraint(String languageCode) throws PolicyException {
        if (languageCode == null || languageCode.length() == 0) throw new PolicyException("Invalid language code specified.");
        this._language = languageCode;
        this._issuer = null;
    }

    public LocalizationConstraint(OMElement element) throws PolicyException {
        OMAttribute attr = element.getAttribute(new QName(VALUE_ATTR));
        if (attr != null) {
            String lang = attr.getAttributeValue();
            if (lang != null) {
                this._language = lang;
            } else this._language = null;
        } else throw new PolicyException("Invalid localization constraint. Missing 'language' attribute.");
    }

    public String getLanguage() {
        return this._language;
    }

    public QName getName() {
        return qelement;
    }

    public PolicyComponent normalize() {
        return this;
    }

    public void serialize(XMLStreamWriter xmlwriter) throws XMLStreamException {
        super.serialize(xmlwriter);
        if (this._language != null) {
            xmlwriter.writeStartElement(NAMESPACE, ELEMENT);
            xmlwriter.writeAttribute(VALUE_ATTR, this._language);
            xmlwriter.writeEndElement();
        } else throw new XMLStreamException(INVALID_PROPAGATE_ASSERTION_URI);
    }

    public boolean equal(PolicyComponent policyAssertion) {
        if (!policyAssertion.getClass().equals(this.getClass())) return false;
        ILocalizationConstraint pa = (ILocalizationConstraint) policyAssertion;
        return (this.getLanguage().equals(pa.getLanguage()));
    }

    public String toString() {
        return "LocalizationConstraint: language=" + (this._language == null ? "undefined" : this._language) + " issuer=" + (this._issuer == null ? "undefined" : this._issuer);
    }
}
