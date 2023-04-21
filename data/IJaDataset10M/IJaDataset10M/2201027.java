package gov.dept.rmv.sts.client;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.impl.builder.StAXOMBuilder;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.ConfigurationContextFactory;
import org.apache.neethi.Policy;
import org.apache.neethi.PolicyEngine;
import org.apache.rahas.RahasConstants;
import org.apache.rahas.Token;
import org.apache.rahas.TrustUtil;
import org.apache.rahas.client.STSClient;
import org.apache.ws.secpolicy.SP11Constants;

public class RMVSTSClient {

    private String policyXml = null;

    private String stsPolicyXml = null;

    private String stsEndpoint = null;

    private String repository = null;

    public static void main(String[] args) {
        try {
            new RMVSTSClient("repo/policy.xml", "repo/sts_policy.xml", "http://localhost:8080/axis2/services/RMV-STS", "repo").getRequiredTokens();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
	 * 
	 * @param servicePolicyXml
	 * @param stsPolicyXml
	 * @param stsEndpoint
	 * @param repository
	 */
    public RMVSTSClient(String servicePolicyXml, String stsPolicyXml, String stsEndpoint, String repository) {
        this.policyXml = servicePolicyXml;
        this.stsPolicyXml = stsPolicyXml;
        this.stsEndpoint = stsEndpoint;
        this.repository = repository;
    }

    /**
	 * 
	 * @return
	 * @throws Exception
	 */
    public Token getRequiredTokens() throws Exception {
        ConfigurationContext ctx = null;
        ctx = ConfigurationContextFactory.createConfigurationContextFromFileSystem(this.repository, null);
        STSClient stsClient = new STSClient(ctx);
        stsClient.setRstTemplate(getRSTTemplate());
        String action = TrustUtil.getActionValue(RahasConstants.VERSION_05_02, RahasConstants.RST_ACTION_ISSUE);
        stsClient.setAction(action);
        Token responseToken;
        responseToken = stsClient.requestSecurityToken(loadPolicy(this.policyXml), this.stsEndpoint, loadPolicy(this.stsPolicyXml), null);
        System.out.println("\n############################# Requested Token ###################################\n");
        System.out.println(responseToken.getToken().toString());
        return responseToken;
    }

    private Policy loadPolicy(String xmlPath) throws Exception {
        StAXOMBuilder builder = new StAXOMBuilder(xmlPath);
        return PolicyEngine.getPolicy(builder.getDocumentElement());
    }

    private OMElement getRSTTemplate() throws Exception {
        OMFactory fac = OMAbstractFactory.getOMFactory();
        OMElement elem = fac.createOMElement(SP11Constants.REQUEST_SECURITY_TOKEN_TEMPLATE);
        TrustUtil.createTokenTypeElement(RahasConstants.VERSION_05_02, elem).setText(RahasConstants.TOK_TYPE_SAML_10);
        TrustUtil.createKeyTypeElement(RahasConstants.VERSION_05_02, elem, RahasConstants.KEY_TYPE_SYMM_KEY);
        TrustUtil.createKeySizeElement(RahasConstants.VERSION_05_02, elem, 256);
        OMElement claims = TrustUtil.createClaims(RahasConstants.VERSION_05_02, elem, RahasConstants.WS_FED_AUTH_CLAIM_DIALECT);
        TrustUtil.createAuthClaimType(RahasConstants.VERSION_05_02, claims, "http://schemas.xmlsoap.org/claims/Group", "Registar");
        OMElement authAdditionalContext = TrustUtil.createAuthAdditionalContext(RahasConstants.VERSION_05_02, elem);
        TrustUtil.createAuthContextItem(RahasConstants.VERSION_05_02, authAdditionalContext, "http://www.rmv.lk/renew/tracking/for", "EchoGreenTest");
        return elem;
    }
}
