package org.openliberty.arisid.policy.neethi;

import java.net.URI;
import javax.xml.namespace.NamespaceContext;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import org.apache.neethi.Assertion;
import org.apache.neethi.PolicyComponent;
import org.openliberty.arisid.policy.IArisWSPolicy;
import org.openliberty.arisid.policy.IAssertion;
import org.openliberty.arisid.policy.IPolicyComponent;

public abstract class PrivacyConstraint implements Assertion, IAssertion {

    protected static final String INVALID_ISSUER_URI = "Invalid Issuer URI";

    protected URI _issuer = null;

    public boolean isOptional() {
        return false;
    }

    public short getType() {
        return IArisWSPolicy.TYPE_ASSERTION;
    }

    public URI getIssuerUri() {
        return this._issuer;
    }

    public boolean equal(IPolicyComponent policyAssertion) {
        if (policyAssertion instanceof PolicyComponent) return equal((PolicyComponent) policyAssertion);
        return false;
    }

    public void serialize(XMLStreamWriter xmlwriter) throws XMLStreamException {
        NamespaceContext ctx = xmlwriter.getNamespaceContext();
        if (ctx == null || ctx.getPrefix(IArisWSPolicy.AppIdPolicy_NS) == null) {
            xmlwriter.writeNamespace(IArisWSPolicy.AppIdPolicy_PREFIX, IArisWSPolicy.AppIdPolicy_NS);
            xmlwriter.setPrefix(IArisWSPolicy.AppIdPolicy_PREFIX, IArisWSPolicy.AppIdPolicy_NS);
        }
        if (ctx == null || ctx.getPrefix(IArisWSPolicy.AppAttrPolicy_NS) == null) {
            xmlwriter.writeNamespace(IArisWSPolicy.AppAttrPolicy_PREFIX, IArisWSPolicy.AppAttrPolicy_NS);
            xmlwriter.setPrefix(IArisWSPolicy.AppAttrPolicy_PREFIX, IArisWSPolicy.AppAttrPolicy_NS);
        }
    }
}
