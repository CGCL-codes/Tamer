package net.sf.fedbridge.integration.tomcat.valves;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import net.sf.fedbridge.common.saml2.exceptions.AssertionExpiredException;
import net.sf.fedbridge.common.util.SAML2AssertionUtil;
import net.sf.fedbridge.common.util.SAML2HttpUtil;
import net.sf.fedbridge.common.util.SAML2RedirectEncoderUtil;
import net.sf.fedbridge.common.util.SAML2Util;
import net.sf.fedbridge.sp.core.SAML2AuthnRequestCreator;
import org.apache.catalina.Session;
import org.apache.catalina.authenticator.Constants;
import org.apache.catalina.authenticator.FormAuthenticator;
import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.Response;
import org.apache.catalina.deploy.LoginConfig;
import org.apache.catalina.realm.GenericPrincipal;
import org.apache.log4j.Logger;
import org.opensaml.common.SAMLObject;
import org.opensaml.saml2.core.Assertion;
import org.opensaml.saml2.core.Attribute;
import org.opensaml.saml2.core.AttributeStatement;
import org.opensaml.saml2.core.Status;
import org.opensaml.saml2.core.StatusCode;
import org.opensaml.saml2.core.Subject;
import org.opensaml.saml2.metadata.Endpoint;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.schema.XSString;

/** 
 * FORM Authenticator with SP Redirect Semantics Added
 * @author osdchicago@users.sourceforge.net
 */
public class SPRedirectFormAuthenticator extends FormAuthenticator {

    protected static Logger log = Logger.getLogger(SPRedirectFormAuthenticator.class);

    private String spURN = null;

    private String spURL = null;

    private String spResponseURL = null;

    private String idpURL = null;

    public void setSpURN(String name) {
        this.spURN = name;
    }

    public void setSpURL(String url) {
        this.spURL = url;
    }

    public void setSpResponseURL(String url) {
        this.spResponseURL = url;
    }

    public void setIdpURL(String url) {
        this.idpURL = url;
    }

    @Override
    public boolean authenticate(Request request, Response response, LoginConfig loginConfig) throws IOException {
        log.debug("Inside the authenticate method");
        Principal principal = request.getUserPrincipal();
        if (principal != null) {
            if (log.isTraceEnabled()) log.trace("Already authenticated '" + principal.getName() + "'");
            return true;
        }
        Session session = request.getSessionInternal(true);
        try {
            Principal p = process(request, response);
            if (p == null) {
                createSAMLRequestMessage("someuser", response);
                return false;
            }
            String username = p.getName();
            String password = "FED_IDENTITY";
            session.setNote(Constants.SESS_USERNAME_NOTE, username);
            session.setNote(Constants.SESS_PASSWORD_NOTE, password);
            request.setUserPrincipal(p);
            register(request, response, p, Constants.FORM_METHOD, username, password);
            return true;
        } catch (AssertionExpiredException aie) {
            log.debug("Assertion has expired. Issuing a new saml2 request to the IDP");
            try {
                createSAMLRequestMessage("someuser", response);
            } catch (Exception e) {
                log.trace("Exception:", e);
                e.printStackTrace();
            }
            return false;
        } catch (Exception e) {
            log.debug("Exception :", e);
            e.printStackTrace();
        }
        return super.authenticate(request, response, loginConfig);
    }

    private void createSAMLRequestMessage(String username, Response response) throws Exception {
        SAML2AuthnRequestCreator creator = new SAML2AuthnRequestCreator(this.spURL, this.spURN);
        Endpoint endpoint = SAML2Util.buildEndpoint(idpURL, spResponseURL);
        SAMLObject samlMessage = creator.createSAMLAuthenticationRequest(username, this.idpURL);
        SAML2RedirectEncoderUtil.encode(response, endpoint, "relay", samlMessage);
    }

    private Principal process(Request request, Response response) throws Exception {
        validateConfiguration();
        String username = null;
        if (SAML2HttpUtil.hasSAML(request)) {
            try {
                SAMLObject samlObject = SAML2HttpUtil.decodeSAMLMessageFromHttpRequest(request);
                org.opensaml.saml2.core.Response samlResponse = null;
                if (samlObject instanceof org.opensaml.saml2.core.Response) {
                    samlResponse = (org.opensaml.saml2.core.Response) samlObject;
                    Status samlStatus = samlResponse.getStatus();
                    if (!StatusCode.SUCCESS_URI.equals(samlStatus.getStatusCode().getValue())) throw new RuntimeException("Unauthorized at the SP");
                    List<Assertion> assertions = samlResponse.getAssertions();
                    Assertion assertion = assertions.get(0);
                    boolean isValidAssertion = SAML2AssertionUtil.isValidAssertion(assertion.getConditions());
                    if (!isValidAssertion) throw new AssertionExpiredException("Assertion Expired");
                    Subject samlSubject = assertion.getSubject();
                    username = samlSubject.getNameID().getValue();
                    List<String> roles = new ArrayList<String>();
                    List<AttributeStatement> attribStatements = assertion.getAttributeStatements();
                    for (AttributeStatement ast : attribStatements) {
                        List<Attribute> attributes = ast.getAttributes();
                        for (Attribute att : attributes) {
                            List<XMLObject> attribValues = att.getAttributeValues();
                            for (XMLObject xo : attribValues) {
                                if (xo instanceof XSString) {
                                    String role = ((XSString) (xo)).getValue().trim();
                                    roles.add(role);
                                }
                            }
                        }
                    }
                    return new GenericPrincipal(request.getContext().getRealm(), username, null, roles);
                }
            } catch (Exception e) {
                log.debug("Exception ", e);
                e.printStackTrace();
            }
        }
        return null;
    }

    private void validateConfiguration() {
        if (this.spURL == null) throw new RuntimeException("SPURL is not configured");
        if (this.spResponseURL == null) this.spResponseURL = spURL = "/response";
        if (this.idpURL == null) throw new RuntimeException("IDPURL is not configured");
    }
}
