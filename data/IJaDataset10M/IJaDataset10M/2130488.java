package org.atricore.idbus.capabilities.openid.ui.panel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.form.AjaxFormValidatingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.target.basic.RedirectRequestTarget;
import org.apache.wicket.util.time.Duration;
import org.apache.wicket.util.value.ValueMap;
import org.apache.wicket.validation.validator.UrlValidator;
import org.atricore.idbus.capabilities.sso.support.auth.AuthnCtxClass;
import org.atricore.idbus.capabilities.sso.support.binding.SSOBinding;
import org.atricore.idbus.capabilities.sso.ui.panel.BaseSignInPanel;
import org.atricore.idbus.kernel.main.federation.metadata.EndpointDescriptor;
import org.atricore.idbus.kernel.main.mediation.*;
import org.atricore.idbus.kernel.main.mediation.camel.component.binding.AbstractMediationHttpBinding;
import org.atricore.idbus.kernel.main.mediation.claim.*;
import org.atricore.idbus.kernel.main.util.UUIDGenerator;

/**
 * Sign-in panel for OpenID authentication for collecting user's OpenID.
 *
 * @author <a href="mailto:gbrigandi@atricore.org">Gianluca Brigandi</a>
 */
public class OpenIDSignInPanel extends BaseSignInPanel {

    private static final Log logger = LogFactory.getLog(OpenIDSignInPanel.class);

    private static final long serialVersionUID = 1L;

    /**
     * Field for user name.
     */
    private RequiredTextField<String> openid;

    private ClaimsRequest claimsRequest;

    private MessageQueueManager artifactQueueManager;

    private IdentityMediationUnitRegistry idsuRegistry;

    private AjaxButton submit;

    /**
     * Sign in form.
     */
    public final class OpenIDSignInForm extends StatelessForm<Void> {

        private static final long serialVersionUID = 1L;

        /**
         * El-cheapo model for form.
         */
        private final ValueMap properties = new ValueMap();

        /**
         * Constructor.
         *
         * @param id id of the form component
         */
        public OpenIDSignInForm(final String id) {
            super(id);
            add(openid = new RequiredTextField<String>("openid", new PropertyModel<String>(properties, "openid")));
            openid.setType(String.class);
            openid.add(new UrlValidator());
            openid.add(new AjaxFormComponentUpdatingBehavior("onchange") {

                @Override
                protected void onUpdate(AjaxRequestTarget target) {
                    submit.setEnabled(true);
                    target.addComponent(submit);
                }
            });
            openid.setOutputMarkupId(true);
        }
    }

    /**
     * @param id See Component constructor
     * @see org.apache.wicket.Component#Component(String)
     */
    public OpenIDSignInPanel(final String id, ClaimsRequest claimsRequest, MessageQueueManager artifactQueueManager, final IdentityMediationUnitRegistry idsuRegistry) {
        super(id);
        this.claimsRequest = claimsRequest;
        this.artifactQueueManager = artifactQueueManager;
        this.idsuRegistry = idsuRegistry;
        final FeedbackPanel feedback = new FeedbackPanel("feedback");
        feedback.setOutputMarkupId(true);
        add(feedback);
        OpenIDSignInForm form = new OpenIDSignInForm("signInForm");
        AjaxFormValidatingBehavior.addToAllFormComponents(form, "onkeyup", Duration.ONE_SECOND);
        form.setOutputMarkupId(true);
        submit = new AjaxButton("apply", form) {

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                target.addComponent(feedback);
                try {
                    String claimsConsumerUrl = signIn(getOpenid());
                    onSignInSucceeded(claimsConsumerUrl);
                } catch (Exception e) {
                    onSignInFailed();
                }
            }

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
                target.addComponent(feedback);
            }
        };
        submit.setEnabled(false);
        form.add(submit);
        add(form);
    }

    /**
     * Removes persisted form data for the signin panel (forget me)
     */
    public final void forgetMe() {
        getPage().removePersistedFormData(OpenIDSignInForm.class, true);
    }

    /**
     * Convenience method to access the openid.
     *
     * @return The user name
     */
    public String getOpenid() {
        return openid.getDefaultModelObjectAsString();
    }

    /**
     * Convenience method set persistence for openid and password.
     *
     * @param enable Whether the fields should be persistent
     */
    public void setPersistent(final boolean enable) {
        openid.setPersistent(enable);
    }

    /**
     * Sign in user if possible.
     *
     * @param openid The openid
     * @return True if signin was successful
     */
    public String signIn(String openid) throws Exception {
        UUIDGenerator idGenerator = new UUIDGenerator();
        logger.info("Claims Request = " + claimsRequest);
        ClaimSet claims = new ClaimSetImpl();
        claims.addClaim(new ClaimImpl("openid", openid));
        ClaimsResponse response = new ClaimsResponseImpl(idGenerator.generateId(), null, claimsRequest.getId(), claims, claimsRequest.getRelayState());
        EndpointDescriptor claimsEndpoint = resolveClaimsEndpoint(claimsRequest, AuthnCtxClass.OPENID_AUTHN_CTX);
        if (claimsEndpoint == null) {
            logger.error("No claims endpoint found!");
        }
        Channel claimsChannel = claimsRequest.getClaimsChannel();
        claimsChannel = getNonSerializedChannel(claimsChannel);
        String claimsEndpointUrl = null;
        if (claimsChannel != null) {
            MediationBindingFactory f = claimsChannel.getIdentityMediator().getBindingFactory();
            MediationBinding b = f.createBinding(SSOBinding.SSO_ARTIFACT.getValue(), claimsRequest.getClaimsChannel());
            claimsEndpointUrl = claimsEndpoint.getResponseLocation();
            if (claimsEndpointUrl == null) claimsEndpointUrl = claimsEndpoint.getLocation();
            if (b instanceof AbstractMediationHttpBinding) {
                AbstractMediationHttpBinding httpBinding = (AbstractMediationHttpBinding) b;
                claimsEndpointUrl = claimsEndpoint.getResponseLocation();
            } else {
                logger.warn("Cannot delegate URL construction to binding, non-http binding found " + b);
                claimsEndpointUrl = claimsEndpoint.getResponseLocation() != null ? claimsEndpoint.getResponseLocation() : claimsEndpoint.getLocation();
            }
        } else {
            logger.warn("Cannot delegate URL construction to binding, valid definition of channel " + claimsRequest.getClaimsChannel().getName() + " not found ...");
            claimsEndpointUrl = claimsEndpoint.getResponseLocation() != null ? claimsEndpoint.getResponseLocation() : claimsEndpoint.getLocation();
        }
        if (logger.isDebugEnabled()) logger.debug("Using claims endpoint URL [" + claimsEndpointUrl + "]");
        Artifact a = artifactQueueManager.pushMessage(response);
        claimsEndpointUrl += "?SSOArt=" + a.getContent();
        if (logger.isDebugEnabled()) logger.debug("Returning claims to " + claimsEndpointUrl);
        return claimsEndpointUrl;
    }

    protected void onSignInFailed() {
        error(getLocalizer().getString("signInFailed", this, "Sign in failed"));
    }

    protected void onSignInSucceeded(String claimsConsumerUrl) {
        getRequestCycle().setRequestTarget(new RedirectRequestTarget(claimsConsumerUrl));
    }
}
