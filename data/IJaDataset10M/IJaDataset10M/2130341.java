package org.atricore.idbus.capabilities.josso.main.producers;

import org.apache.camel.Endpoint;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.atricore.idbus.capabilities.josso.main.JossoAuthnContext;
import org.atricore.idbus.capabilities.josso.main.JossoConstants;
import org.atricore.idbus.capabilities.josso.main.JossoException;
import org.atricore.idbus.capabilities.sso.support.auth.AuthnCtxClass;
import org.atricore.idbus.common.sso._1_0.protocol.CredentialType;
import org.atricore.idbus.capabilities.sso.support.binding.SSOBinding;
import org.atricore.idbus.capabilities.sso.support.metadata.SSOService;
import org.atricore.idbus.common.sso._1_0.protocol.RequestAttributeType;
import org.atricore.idbus.common.sso._1_0.protocol.SPInitiatedAuthnRequestType;
import org.atricore.idbus.kernel.main.authn.Constants;
import org.atricore.idbus.kernel.main.authn.util.CipherUtil;
import org.atricore.idbus.kernel.main.federation.metadata.EndpointDescriptor;
import org.atricore.idbus.kernel.main.mediation.IdentityMediationException;
import org.atricore.idbus.kernel.main.mediation.MediationMessageImpl;
import org.atricore.idbus.kernel.main.mediation.binding.BindingChannel;
import org.atricore.idbus.kernel.main.mediation.camel.component.binding.CamelMediationExchange;
import org.atricore.idbus.kernel.main.mediation.camel.component.binding.CamelMediationMessage;
import org.atricore.idbus.kernel.main.mediation.endpoint.IdentityMediationEndpoint;
import org.atricore.idbus.kernel.main.util.UUIDGenerator;
import org.oasis_open.docs.wss._2004._01.oasis_200401_wss_wssecurity_secext_1_0.AttributedString;
import org.oasis_open.docs.wss._2004._01.oasis_200401_wss_wssecurity_secext_1_0.UsernameTokenType;
import javax.xml.namespace.QName;
import java.net.URLDecoder;

/**
 * JOSSO 1.1 Binding single signon producer
 *
 * @author <a href="mailto:sgonzalez@atricore.org">Sebastian Gonzalez Oyuela</a>
 * @version $Id$
 */
public class SingleSignOnProducer extends AbstractJossoProducer {

    private static final Log logger = LogFactory.getLog(SingleSignOnProducer.class);

    private UUIDGenerator uuidGenerator = new UUIDGenerator();

    public SingleSignOnProducer(Endpoint endpoint) {
        super(endpoint);
    }

    /**
     */
    protected void doProcess(CamelMediationExchange exchange) throws Exception {
        CamelMediationMessage in = (CamelMediationMessage) exchange.getIn();
        BindingChannel bChannel = (BindingChannel) channel;
        String backTo = in.getMessage().getState().getTransientVariable(JossoConstants.JOSSO_BACK_TO_VAR);
        String cmd = in.getMessage().getState().getTransientVariable(JossoConstants.JOSSO_CMD_VAR);
        String appId = in.getMessage().getState().getTransientVariable(JossoConstants.JOSSO_APPID_VAR);
        String idpAliasB64 = in.getMessage().getState().getTransientVariable(JossoConstants.JOSSO_IDPALIAS_VAR);
        String idpAlias = null;
        String username = in.getMessage().getState().getTransientVariable(JossoConstants.JOSSO_USERNAME_VAR);
        String password = in.getMessage().getState().getTransientVariable(JossoConstants.JOSSO_PASSWORD_VAR);
        JossoAuthnContext authnCtx = (JossoAuthnContext) in.getMessage().getState().getLocalVariable("urn:org:atricore:idbus:capabilities:josso:authnCtx");
        if (idpAliasB64 != null) {
            idpAlias = URLDecoder.decode(new String(CipherUtil.decodeBase64(idpAliasB64)), "UTF-8");
            if (logger.isDebugEnabled()) logger.debug("Using received idp alias " + idpAlias);
        }
        if (idpAlias == null) {
            idpAlias = authnCtx != null ? authnCtx.getIdpAlias() : null;
            if (logger.isDebugEnabled()) logger.debug("Using previous idp alias " + idpAlias);
        }
        if (logger.isDebugEnabled()) logger.debug("Starting JOSSO 1 SSO, requester [" + appId + "] cmd [" + cmd + "], " + "back_to [" + backTo + "] idpAlias [" + idpAlias + "]");
        BindingChannel spChannel = resolveSpBindingChannel(bChannel, appId);
        EndpointDescriptor destination = resolveSPInitiatedSSOEndpointDescriptor(exchange, spChannel);
        SPInitiatedAuthnRequestType request = buildAuthnRequest(exchange, idpAlias);
        authnCtx = new JossoAuthnContext();
        authnCtx.setAppId(appId);
        authnCtx.setSsoBackTo(backTo);
        authnCtx.setIdpAlias(idpAlias);
        authnCtx.setAuthnRequest(request);
        in.getMessage().getState().setLocalVariable("urn:org:atricore:idbus:capabilities:josso:authnCtx", authnCtx);
        CamelMediationMessage out = (CamelMediationMessage) exchange.getOut();
        out.setMessage(new MediationMessageImpl(request.getID(), request, "SSOAuthnRequest", null, destination, in.getMessage().getState()));
        exchange.setOut(out);
    }

    /**
     * @return
     */
    protected SPInitiatedAuthnRequestType buildAuthnRequest(CamelMediationExchange exchange, String idpAlias) {
        CamelMediationMessage in = (CamelMediationMessage) exchange.getIn();
        SPInitiatedAuthnRequestType req = new SPInitiatedAuthnRequestType();
        req.setID(uuidGenerator.generateId());
        String cmd = in.getMessage().getState().getTransientVariable(JossoConstants.JOSSO_CMD_VAR);
        req.setPassive(cmd != null && cmd.equals("login_optional"));
        RequestAttributeType idpAliasAttr = new RequestAttributeType();
        idpAliasAttr.setName("atricore_idp_alias");
        idpAliasAttr.setValue(idpAlias);
        req.getRequestAttribute().add(idpAliasAttr);
        if (logger.isDebugEnabled()) logger.debug(JossoConstants.JOSSO_CMD_VAR + "='" + cmd + "'");
        String username = in.getMessage().getState().getTransientVariable(JossoConstants.JOSSO_USERNAME_VAR);
        String password = in.getMessage().getState().getTransientVariable(JossoConstants.JOSSO_PASSWORD_VAR);
        for (String tvarName : in.getMessage().getState().getTransientVarNames()) {
            RequestAttributeType a = new RequestAttributeType();
            a.setName(tvarName);
            a.setValue(in.getMessage().getState().getTransientVariable(tvarName));
        }
        if (username != null && password != null) {
            if (logger.isDebugEnabled()) logger.debug("Initializing Authnentiation request w/credentials");
            UsernameTokenType usernameToken = new UsernameTokenType();
            AttributedString usernameString = new AttributedString();
            usernameString.setValue(username);
            usernameToken.setUsername(usernameString);
            usernameToken.getOtherAttributes().put(new QName(Constants.PASSWORD_NS), password);
            CredentialType ct = new CredentialType();
            ct.setAny(usernameToken);
            req.getCredentials().add(ct);
            if (logger.isDebugEnabled()) logger.debug("Received basic credentials for user " + username + ", forcing authentication");
            req.setForceAuthn(true);
            req.setAuthnCtxClass(AuthnCtxClass.ATC_SP_PASSWORD_AUTHN_CTX.getValue());
        } else if (username != null && cmd != null && cmd.equals("impersonate")) {
            if (logger.isDebugEnabled()) logger.debug("Initializing Authnentiation request for impersonation");
            UsernameTokenType usernameToken = new UsernameTokenType();
            AttributedString usernameString = new AttributedString();
            usernameString.setValue(username);
            usernameToken.setUsername(usernameString);
            usernameToken.getOtherAttributes().put(new QName(Constants.IMPERSONATE_NS), password);
            CredentialType ct = new CredentialType();
            ct.setAny(usernameToken);
            req.setAuthnCtxClass(AuthnCtxClass.ATC_SP_IMPERSONATE_AUTHN_CTX.getValue());
            req.setForceAuthn(true);
            req.getCredentials().add(ct);
        }
        return req;
    }

    protected EndpointDescriptor resolveSPInitiatedSSOEndpointDescriptor(CamelMediationExchange exchange, BindingChannel sp) throws JossoException {
        try {
            logger.debug("Looking for " + SSOService.SPInitiatedSingleSignOnService.toString());
            for (IdentityMediationEndpoint endpoint : sp.getEndpoints()) {
                logger.debug("Processing endpoint : " + endpoint.getType() + "[" + endpoint.getBinding() + "]");
                if (endpoint.getType().equals(SSOService.SPInitiatedSingleSignOnService.toString())) {
                    if (endpoint.getBinding().equals(SSOBinding.SSO_ARTIFACT.getValue())) {
                        return sp.getIdentityMediator().resolveEndpoint(sp, endpoint);
                    }
                }
            }
        } catch (IdentityMediationException e) {
            throw new JossoException(e);
        }
        throw new JossoException("No SP endpoint found for SP Initiated SSO using SSO Artifact binding");
    }
}
