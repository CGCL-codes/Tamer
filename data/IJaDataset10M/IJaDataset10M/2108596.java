package org.apache.rahas;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.neethi.Policy;
import org.apache.rahas.PWCallback;
import org.apache.rampart.handler.config.InflowConfiguration;
import org.apache.rampart.handler.config.OutflowConfiguration;
import org.apache.ws.secpolicy.SP12Constants;
import org.apache.ws.secpolicy.SPConstants;
import org.opensaml.XML;
import javax.xml.namespace.QName;

/**
 * RahasSAMLTokenTest with the WS-SX namespaces
 */
public class RahasSAMLTokenV1205Test extends TestClient {

    /**
     * @param name
     */
    public RahasSAMLTokenV1205Test(String name) {
        super(name);
    }

    public OMElement getRequest() {
        try {
            OMElement rstElem = TrustUtil.createRequestSecurityTokenElement(RahasConstants.VERSION_05_12);
            TrustUtil.createRequestTypeElement(RahasConstants.VERSION_05_12, rstElem, RahasConstants.REQ_TYPE_ISSUE);
            OMElement tokenTypeElem = TrustUtil.createTokenTypeElement(RahasConstants.VERSION_05_12, rstElem);
            tokenTypeElem.setText(RahasConstants.TOK_TYPE_SAML_10);
            TrustUtil.createAppliesToElement(rstElem, "http://localhost:5555/axis2/services/SecureService", this.getWSANamespace());
            TrustUtil.createKeyTypeElement(RahasConstants.VERSION_05_12, rstElem, RahasConstants.KEY_TYPE_SYMM_KEY);
            TrustUtil.createKeySizeElement(RahasConstants.VERSION_05_12, rstElem, 256);
            return rstElem;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public OutflowConfiguration getClientOutflowConfiguration() {
        OutflowConfiguration ofc = new OutflowConfiguration();
        ofc.setActionItems("Signature Encrypt Timestamp");
        ofc.setUser("alice");
        ofc.setSignaturePropFile("rahas/rahas-sec.properties");
        ofc.setPasswordCallbackClass(PWCallback.class.getName());
        return ofc;
    }

    public InflowConfiguration getClientInflowConfiguration() {
        InflowConfiguration ifc = new InflowConfiguration();
        ifc.setActionItems("Signature Encrypt Timestamp");
        ifc.setPasswordCallbackClass(PWCallback.class.getName());
        ifc.setSignaturePropFile("rahas/rahas-sec.properties");
        return ifc;
    }

    public String getServiceRepo() {
        return "rahas_service_repo_1";
    }

    public void validateRsponse(OMElement resp) {
        OMElement rstr = resp.getFirstChildWithName(new QName(RahasConstants.WST_NS_05_12, RahasConstants.LocalNames.REQUEST_SECURITY_TOKEN_RESPONSE));
        assertNotNull("RequestedSecurityToken missing", rstr);
        OMElement rst = rstr.getFirstChildWithName(new QName(RahasConstants.WST_NS_05_12, RahasConstants.IssuanceBindingLocalNames.REQUESTED_SECURITY_TOKEN));
        assertNotNull("RequestedSecurityToken missing", rst);
        OMElement elem = rst.getFirstChildWithName(new QName(XML.SAML_NS, "Assertion"));
        assertNotNull("Missing SAML Assertoin", elem);
    }

    public String getRequestAction() throws TrustException {
        return TrustUtil.getActionValue(RahasConstants.VERSION_05_12, RahasConstants.RST_ACTION_ISSUE);
    }

    public Policy getServicePolicy() throws Exception {
        return this.getPolicy("test-resources/rahas/policy/service-policy-symm-binding.xml");
    }

    public Policy getSTSPolicy() throws Exception {
        return this.getPolicy("test-resources/rahas/policy/sts-policy-asymm-binding.xml");
    }

    public OMElement getRSTTemplate() throws TrustException {
        OMFactory factory = OMAbstractFactory.getOMFactory();
        OMElement elem = factory.createOMElement(SP12Constants.REQUEST_SECURITY_TOKEN_TEMPLATE);
        TrustUtil.createTokenTypeElement(RahasConstants.VERSION_05_12, elem).setText(RahasConstants.TOK_TYPE_SAML_10);
        TrustUtil.createKeyTypeElement(RahasConstants.VERSION_05_12, elem, RahasConstants.KEY_TYPE_SYMM_KEY);
        TrustUtil.createKeySizeElement(RahasConstants.VERSION_05_12, elem, 256);
        return elem;
    }

    public int getTrstVersion() {
        return RahasConstants.VERSION_05_12;
    }
}
