package org.zeroexchange.web.components.user;

import java.io.Serializable;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.validation.EqualPasswordInputValidator;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.validation.validator.EmailAddressValidator;
import org.zeroexchange.exception.BusinessLogicException;
import org.zeroexchange.model.location.Location;
import org.zeroexchange.web.Messages;
import org.zeroexchange.web.UIUtils;
import org.zeroexchange.web.components.GeneralFeedbackPanel;
import org.zeroexchange.web.components.location.LocationSelector;

/**
 * @author black
 *
 */
public abstract class AbstractUserProfilePanel extends Panel {

    private static final long serialVersionUID = 1L;

    private static final Log log = LogFactory.getLog(AbstractUserProfilePanel.class);

    private static final String MKEY_SUBMIT = "profile.submit";

    /** Field for password input */
    protected PasswordTextField passwordField;

    /** Field for password confirmation */
    protected PasswordTextField rePasswordField;

    protected LocationSelector locationSelector;

    protected Form<ProfileData> form;

    /** Form data */
    protected abstract class ProfileData implements Serializable {

        private static final long serialVersionUID = 1L;

        private String password;

        private String rePassword;

        private String email;

        private Location location;

        public String getPassword() {
            return password;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getRePassword() {
            return rePassword;
        }

        public Location getLocation() {
            return location;
        }

        public void setLocation(Location location) {
            this.location = location;
        }
    }

    public AbstractUserProfilePanel(String id) {
        super(id);
        ProfileData profileData = createProfileData();
        form = new Form<ProfileData>("profileForm", new CompoundPropertyModel<ProfileData>(profileData)) {

            private static final long serialVersionUID = 1L;

            @Override
            protected void onSubmit() {
                try {
                    updateUser(getModelObject());
                    onFormProcessed();
                } catch (BusinessLogicException e) {
                    log.error("", e);
                    error(getString(Messages.INTERNAL_ERROR));
                }
            }
        };
        form.setOutputMarkupId(true);
        form.add(UIUtils.createValidatableField((passwordField = new PasswordTextField("password"))));
        form.add(UIUtils.createValidatableField((rePasswordField = new PasswordTextField("rePassword"))));
        form.add(UIUtils.createValidatableField(new RequiredTextField<String>("email").add(EmailAddressValidator.getInstance())));
        form.add(UIUtils.createValidatableField(new LocationSelector("location")));
        form.add(new Button("profileSubmit", new ResourceModel(MKEY_SUBMIT)));
        form.add(new EqualPasswordInputValidator(passwordField, rePasswordField));
        add(form);
        add(new GeneralFeedbackPanel("messagesPanel"));
    }

    protected abstract <T extends ProfileData> T createProfileData();

    protected abstract void onFormProcessed();

    protected abstract void updateUser(ProfileData userData);
}
