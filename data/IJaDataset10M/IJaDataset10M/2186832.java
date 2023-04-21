package org.openliberty.igf.attributeService.schema;

import org.apache.axiom.om.OMElement;
import org.apache.neethi.Policy;
import org.apache.neethi.PolicyEngine;
import org.openliberty.igf.attributeService.policy.application.CarmlPolUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * PolicyDef is a container for WS-Policy declaration.
 * 
 * PolicyDef is actually a handler interfacing between the CARML object model and Apache Neethi's WS-Policy implementation.
 */
public class PolicyDef {

    public static final String POLICY_ELEMENT = "Policy";

    public static final String POLREF_ELEMENT = "PolicyReference";

    public static final String URI_ATTR = "URI";

    private Policy _policy;

    public PolicyDef(Policy policy) {
        this._policy = policy;
    }

    /**
	 * Construct a PolicyDef object (and its {@link org.apache.neethi.Policy}) using a DOM Node that represent the XML node containing the WS-Policy element. 
	 * @param policyNode
	 * @throws Exception
	 */
    public PolicyDef(Node policyNode) throws Exception {
        OMElement element = CarmlPolUtil.convertDomToOm((Element) policyNode);
        this._policy = PolicyEngine.getPolicy(element);
    }

    public String getName() {
        return (this._policy == null ? null : this._policy.getName());
    }

    public void appendChildren(Node parent) {
        if (this._policy == null) return;
        Element polElem = CarmlPolUtil.policyToElement(this._policy);
        Document doc = parent.getOwnerDocument();
        doc.adoptNode(polElem);
        parent.appendChild(polElem);
    }

    public Policy getPolicy() {
        return this._policy;
    }

    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("Policy: ").append(this.getName());
        return buf.toString();
    }
}
