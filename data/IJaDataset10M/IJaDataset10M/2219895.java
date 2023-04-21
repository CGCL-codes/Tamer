package org.libreplan.web.users;

import static org.libreplan.web.I18nHelper._;
import java.util.List;
import org.apache.commons.logging.LogFactory;
import org.libreplan.business.common.exceptions.InstanceNotFoundException;
import org.libreplan.business.common.exceptions.ValidationException;
import org.libreplan.business.users.entities.Profile;
import org.libreplan.business.users.entities.User;
import org.libreplan.business.users.entities.UserRole;
import org.libreplan.web.common.BaseCRUDController;
import org.libreplan.web.common.Util;
import org.libreplan.web.common.components.Autocomplete;
import org.libreplan.web.common.entrypoints.EntryPointsHandler;
import org.libreplan.web.common.entrypoints.IURLHandlerRegistry;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Constraint;
import org.zkoss.zul.Textbox;

/**
 * Controller for CRUD actions over a {@link User}
 *
 * @author Jacobo Aragunde Perez <jaragunde@igalia.com>
 */
@SuppressWarnings("serial")
public class UserCRUDController extends BaseCRUDController<User> implements IUserCRUDController {

    private static final org.apache.commons.logging.Log LOG = LogFactory.getLog(UserCRUDController.class);

    private IUserModel userModel;

    private Textbox passwordBox;

    private Textbox passwordConfirmationBox;

    private Combobox userRolesCombo;

    private Autocomplete profileAutocomplete;

    private IURLHandlerRegistry URLHandlerRegistry;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        final EntryPointsHandler<IUserCRUDController> handler = URLHandlerRegistry.getRedirectorFor(IUserCRUDController.class);
        handler.register(this, page);
        passwordBox = (Textbox) editWindow.getFellowIfAny("password");
        passwordConfirmationBox = (Textbox) editWindow.getFellowIfAny("passwordConfirmation");
        profileAutocomplete = (Autocomplete) editWindow.getFellowIfAny("profileAutocomplete");
        userRolesCombo = (Combobox) editWindow.getFellowIfAny("userRolesCombo");
        appendAllUserRoles(userRolesCombo);
    }

    /**
     * Appends the existing UserRoles to the Combobox passed.
     * @param combo
     */
    private void appendAllUserRoles(Combobox combo) {
        for (UserRole role : UserRole.values()) {
            Comboitem item = combo.appendItem(_(role.getDisplayName()));
            item.setValue(role);
        }
    }

    public List<User> getUsers() {
        return userModel.getUsers();
    }

    protected void save() throws ValidationException {
        userModel.confirmSave();
        PasswordUtil.showOrHideDefaultPasswordWarnings();
    }

    public User getUser() {
        return userModel.getUser();
    }

    public List<UserRole> getRoles() {
        return userModel.getRoles();
    }

    public void addSelectedRole() {
        Comboitem comboItem = userRolesCombo.getSelectedItem();
        if (comboItem != null) {
            addRole((UserRole) comboItem.getValue());
        }
    }

    public void addRole(UserRole role) {
        userModel.addRole(role);
        Util.reloadBindings(editWindow);
    }

    public void removeRole(UserRole role) {
        userModel.removeRole(role);
        Util.reloadBindings(editWindow);
    }

    public List<Profile> getProfiles() {
        return userModel.getProfiles();
    }

    public void addSelectedProfile() {
        Comboitem comboItem = profileAutocomplete.getSelectedItem();
        if (comboItem != null) {
            addProfile((Profile) comboItem.getValue());
        }
    }

    public void addProfile(Profile profile) {
        userModel.addProfile(profile);
        Util.reloadBindings(editWindow);
    }

    public void removeProfile(Profile profile) {
        userModel.removeProfile(profile);
        Util.reloadBindings(editWindow);
    }

    /**
     * Tells the XXXModel to set the password attribute of the inner
     * {@ link User} object.
     *
     * @param password String with the <b>unencrypted</b> password.
     */
    public void setPassword(String password) {
        userModel.setPassword(password);
        ((Textbox) editWindow.getFellowIfAny("passwordConfirmation")).clearErrorMessage(true);
    }

    public Constraint validatePasswordConfirmation() {
        return new Constraint() {

            @Override
            public void validate(Component comp, Object value) throws WrongValueException {
                ((Textbox) comp).setRawValue(value);
                if (!((String) value).equals(passwordBox.getValue())) {
                    throw new WrongValueException(comp, _("passwords don't match"));
                }
            }
        };
    }

    @Override
    protected String getEntityType() {
        return _("User");
    }

    @Override
    protected String getPluralEntityType() {
        return _("Users");
    }

    @Override
    protected void initCreate() {
        userModel.initCreate();
        passwordBox.setConstraint("no empty:" + _("The password for a new user cannot be empty"));
        passwordBox.setRawValue("");
        passwordConfirmationBox.setRawValue("");
    }

    @Override
    protected void initEdit(User user) {
        userModel.initEdit(user);
        passwordBox.setConstraint((Constraint) null);
        passwordBox.setValue("");
        passwordConfirmationBox.setValue("");
    }

    @Override
    protected User getEntityBeingEdited() {
        return userModel.getUser();
    }

    @Override
    protected void delete(User user) throws InstanceNotFoundException {
        userModel.confirmRemove(user);
    }

    public boolean isLdapUser() {
        User user = userModel.getUser();
        if (user == null) {
            return false;
        }
        return !user.isLibrePlanUser();
    }
}
