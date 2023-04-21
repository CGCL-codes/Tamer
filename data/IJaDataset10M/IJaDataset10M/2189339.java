package org.atricore.idbus.capabilities.sso.main.claims;

import org.apache.camel.builder.RouteBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.atricore.idbus.capabilities.sso.main.SSOException;
import org.atricore.idbus.capabilities.sso.main.common.AbstractSSOMediator;
import org.atricore.idbus.capabilities.sso.support.binding.SSOBinding;
import org.atricore.idbus.kernel.main.federation.metadata.EndpointDescriptor;
import org.atricore.idbus.kernel.main.mediation.IdentityMediationException;
import org.atricore.idbus.kernel.main.mediation.claim.ClaimChannel;
import org.atricore.idbus.kernel.main.mediation.endpoint.IdentityMediationEndpoint;
import java.util.Collection;

/**
 * @org.apache.xbean.XBean element="claims-mediator"
 *
 * @author <a href="mailto:sgonzalez@atricore.org">Sebastian Gonzalez Oyuela</a>
 * @version $Id: SSOClaimsMediator.java 1359 2009-07-19 16:57:57Z sgonzalez $
 */
public class SSOClaimsMediator extends AbstractSSOMediator {

    private static final Log logger = LogFactory.getLog(SSOClaimsMediator.class);

    private String basicAuthnUILocation;

    private String twoFactorAuthnUILocation;

    private String openIDUILocation;

    /**
     * @org.apache.xnean.Property alias="login-form-location"
     */
    public String getBasicAuthnUILocation() {
        return basicAuthnUILocation;
    }

    public void setBasicAuthnUILocation(String basicAuthnUILocation) {
        this.basicAuthnUILocation = basicAuthnUILocation;
    }

    public String getTwoFactorAuthnUILocation() {
        return twoFactorAuthnUILocation;
    }

    public void setTwoFactorAuthnUILocation(String twoFactorAuthnUILocation) {
        this.twoFactorAuthnUILocation = twoFactorAuthnUILocation;
    }

    public String getOpenIDUILocation() {
        return openIDUILocation;
    }

    public void setOpenIDUILocation(String openIDUILocation) {
        this.openIDUILocation = openIDUILocation;
    }

    @Override
    protected RouteBuilder createClaimRoutes(final ClaimChannel claimChannel) throws Exception {
        return new RouteBuilder() {

            @Override
            public void configure() throws Exception {
                Collection<IdentityMediationEndpoint> endpoints = claimChannel.getEndpoints();
                if (endpoints == null) throw new IdentityMediationException("No endpoints defined for claims channel : " + claimChannel.getName());
                for (IdentityMediationEndpoint endpoint : endpoints) {
                    SSOBinding binding = SSOBinding.asEnum(endpoint.getBinding());
                    EndpointDescriptor ed = resolveEndpoint(claimChannel, endpoint);
                    switch(binding) {
                        case SSO_ARTIFACT:
                        case SSO_POST:
                        case SS0_REDIRECT:
                            from("idbus-http:" + ed.getLocation()).process(new LoggerProcessor(getLogger())).to("direct:" + ed.getName());
                            from("idbus-bind:camel://direct:" + ed.getName() + "?binding=" + ed.getBinding() + "&channelRef=" + claimChannel.getName()).process(new LoggerProcessor(getLogger())).to("sso-claim:" + ed.getType() + "?channelRef=" + claimChannel.getName() + "&endpointRef=" + endpoint.getName());
                            if (ed.getResponseLocation() != null) {
                                from("idbus-http:" + ed.getResponseLocation()).process(new LoggerProcessor(getLogger())).to("direct:" + ed.getName() + "-response");
                                from("idbus-bind:camel://direct:" + ed.getName() + "-response" + "?binding=" + ed.getBinding() + "&channelRef=" + claimChannel.getName()).process(new LoggerProcessor(getLogger())).to("sso-claim:" + ed.getType() + "?channelRef=" + claimChannel.getName() + "&endpointRef=" + endpoint.getName() + "&response=true");
                            }
                            break;
                        case SAMLR2_LOCAL:
                        case SSO_LOCAL:
                            from("direct:" + ed.getLocation()).to("direct:" + ed.getName() + "-local");
                            from("idbus-bind:camel://direct:" + ed.getName() + "-local" + "?binding=" + ed.getBinding() + "&channelRef=" + claimChannel.getName()).process(new LoggerProcessor(getLogger())).to("sso-claim:" + ed.getType() + "?channelRef=" + claimChannel.getName() + "&endpointRef=" + endpoint.getName());
                            if (ed.getResponseLocation() != null) {
                                from("direct:" + ed.getLocation()).to("direct:" + ed.getName() + "-local-resp");
                                from("idbus-bind:camel://direct:" + ed.getName() + "-local-resp" + "?binding=" + ed.getBinding() + "&channelRef=" + claimChannel.getName()).process(new LoggerProcessor(getLogger())).to("sso-claim:" + ed.getType() + "?channelRef=" + claimChannel.getName() + "&endpointRef=" + endpoint.getName() + "&response=true");
                            }
                            break;
                        default:
                            throw new SSOException("Unsupported SSOBinding " + binding.getValue());
                    }
                }
            }
        };
    }
}
