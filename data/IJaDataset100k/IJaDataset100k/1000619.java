package org.openliberty.arisid.policy.neethi;

import java.net.URI;
import java.util.Iterator;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import org.apache.axiom.om.OMElement;
import org.apache.neethi.Assertion;
import org.apache.neethi.Policy;
import org.apache.neethi.PolicyEngine;
import org.apache.neethi.PolicyReference;
import org.openliberty.arisid.policy.IArisWSPolicy;
import org.openliberty.arisid.policy.IAssertion;
import org.openliberty.arisid.policy.IPolicy;
import org.openliberty.arisid.policy.IPolicyComponent;
import org.openliberty.arisid.policy.PolicyHandler;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * PolicyDef is a container for WS-Policy declaration.
 * 
 * PolicyDef is actually a handler interfacing between the CARML object model and Apache Neethi's WS-Policy implementation.
 */
public class PolicyDef implements IPolicy {

    private Policy _policy;

    private URI _uri;

    private PolicyReference _polref;

    public static PolicyDef createPolicyDef(Policy policy) {
        return new PolicyDef(policy);
    }

    public static PolicyDef createPolicyDef(URI policyReference) {
        return new PolicyDef(policyReference);
    }

    private PolicyDef(URI policyReference) {
        this._uri = policyReference;
        this._polref = new PolicyReference();
        this._polref.setURI(this._uri.toString());
        this._policy = this._polref.getRemoteReferencedPolicy(this._uri.toString());
    }

    private PolicyDef(Policy policy) {
        this._uri = null;
        this._polref = null;
        this._policy = policy;
    }

    /**
	 * Construct a PolicyDef object (and its {@link org.apache.neethi.Policy}) using a DOM Node that represent the XML node containing the WS-Policy element. 
	 * @param policyNode
	 * @throws Exception
	 */
    public PolicyDef(Node policyNode) throws Exception {
        String elname = policyNode.getNodeName();
        if (elname.equals(IPolicy.POLREF_ELEMENT)) {
            Element elem = (Element) policyNode;
            String str_uri = elem.getAttribute("URI");
            if (str_uri != null) this._uri = new URI(str_uri);
            OMElement element = PolicyImpl.convertDomToOm((Element) policyNode);
            this._polref = PolicyEngine.getPolicyReference(element);
            this._policy = this._polref.getRemoteReferencedPolicy(this._uri.toString());
        } else {
            OMElement element = PolicyImpl.convertDomToOm((Element) policyNode);
            this._policy = PolicyEngine.getPolicy(element);
            this._polref = null;
            this._uri = null;
        }
    }

    public String getName() {
        return (this._policy == null ? null : this._policy.getName());
    }

    public void setName(String name) {
        this._policy.setName(name);
    }

    /**
	 * Appends all document children nodes for the object to the parent
	 * specified.
	 * 
	 * @param parent
	 *            A document node parent to add the current object to.
	 */
    @SuppressWarnings("deprecation")
    public void appendChildren(Node parent) {
        if (this._policy == null) return;
        Element polElem;
        if (isReferencePolicy()) {
            polElem = PolicyHandler.getInstance().policyToElement(this._polref);
        } else polElem = PolicyHandler.getInstance().policyToElement(this._policy);
        Document doc = parent.getOwnerDocument();
        Node tempNode = doc.importNode(polElem, true);
        parent.appendChild(tempNode);
    }

    public boolean isReferencePolicy() {
        return (this._uri != null);
    }

    public Object getPolicy() {
        if (this.isReferencePolicy()) return this._polref;
        return this._policy;
    }

    public void setPolicy(Policy policy) {
        this._policy = policy;
    }

    public String toString() {
        StringBuffer buf = new StringBuffer();
        if (isReferencePolicy()) {
            buf.append("PolicyRef: ").append(this.getName());
        } else buf.append("Policy: ").append(this.getName());
        return buf.toString();
    }

    public void addAssertion(IAssertion assertion) {
        if (assertion instanceof Assertion) {
            Assertion as = (Assertion) assertion;
            this._policy.addAssertion(as);
        }
    }

    public boolean equal(IPolicyComponent policyComponent) {
        if (policyComponent instanceof PolicyDef) {
            PolicyDef pol = (PolicyDef) policyComponent;
            if (pol == null && this._policy == null) return true;
            if (pol == null || this._policy == null) return false;
            if (pol.isReferencePolicy()) {
                if (!this.isReferencePolicy()) return false;
                PolicyReference polref = (PolicyReference) pol.getPolicy();
                return (polref.equal(this._polref));
            } else {
                if (this.isReferencePolicy()) return false;
                Policy cpol = (Policy) pol.getPolicy();
                return (cpol.equals(this._policy));
            }
        }
        return false;
    }

    public short getType() {
        if (this.isReferencePolicy()) return IArisWSPolicy.TYPE_POLICY_REF;
        return IArisWSPolicy.TYPE_POLICY;
    }

    public void serialize(XMLStreamWriter writer) throws XMLStreamException {
        if (this.isReferencePolicy()) this._polref.serialize(writer); else this._policy.serialize(writer);
    }

    public void dispose() {
        if (this._policy != null) {
            Iterator<?> iter = this._policy.getPolicyComponents().iterator();
            while (iter.hasNext()) iter.remove();
            this._policy = null;
        }
        if (this._polref != null) {
            this._polref = null;
        }
        this._uri = null;
    }
}
