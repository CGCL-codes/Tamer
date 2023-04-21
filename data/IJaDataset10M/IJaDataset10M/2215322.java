package fi.arcusys.cygnus.model.delegate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.faces.model.SelectItem;
import fi.arcusys.qnet.common.model.Address;
import fi.arcusys.qnet.common.model.Contact;
import fi.arcusys.qnet.common.model.Organization;
import fi.arcusys.qnet.common.model.UserAccount;
import fi.arcusys.qnet.common.model.UserPreference;
import fi.arcusys.qnet.common.model.delegate.UserAccountDelegate;

/**
 * Helper delegate to {@link User} for UI.
 * 
 * @author mikko
 *
 */
public class UserAccountBean extends UserAccountDelegate {

    private static final long serialVersionUID = 2L;

    private String newPassword;

    private String newPasswordAgain;

    private Long organizationId;

    private UserType userType = null;

    public UserAccountBean() {
        super(new UserAccount());
    }

    public UserAccountBean(UserAccount user) {
        super(user);
        Organization org = getOrganization();
        setOrganizationId(null != org ? org.getId() : null);
    }

    public boolean isNewEntity() {
        return null == super.getId() || 0 == super.getId().longValue();
    }

    public UserType getUserType() {
        if (null == userType) {
            userType = UserType.forUser(getEntity());
        }
        return userType;
    }

    public void setUserType(UserType ut) {
        this.userType = ut;
    }

    /**
	 * A null-safe override of {@link UserAccountDelegate#getPreferences()}.
	 * 
	 * @return Map of of user preferences (never null)
	 */
    @SuppressWarnings("deprecation")
    @Override
    public Map<String, UserPreference> getPreferences() {
        Map<String, UserPreference> m = super.getPreferences();
        if (null == m) {
            m = new HashMap<String, UserPreference>();
            setPreferences(m);
        }
        return m;
    }

    /** Address as AddressBean or null if Address is null */
    public AddressBean getAddressBean() {
        Address a = super.getAddress();
        return (null != a) ? new AddressBean(a) : null;
    }

    /** For nullifying expiration date from UI */
    public boolean isNeverExpires() {
        return (null == getValidUntil());
    }

    public void setNeverExpires(boolean neverExpires) {
        if (neverExpires) {
            setValidUntil(null);
        }
    }

    /** 
	 * Indicates if the user password is currently set.
	 * @return
	 */
    public boolean isPasswordSet() {
        String p = super.getPassword();
        return (null != p) && p.length() > 0;
    }

    /** New password */
    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    /** New password again */
    public String getNewPasswordAgain() {
        return newPasswordAgain;
    }

    public void setNewPasswordAgain(String newPasswordAgain) {
        this.newPasswordAgain = newPasswordAgain;
    }

    /** Organization.id for UI to set*/
    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public String getUserColor() {
        Map<String, UserPreference> preferences = getPreferences();
        UserPreference up = preferences.get("userColor");
        return (null != up) ? up.getValue() : null;
    }

    /**
	 * If the underlying <code>User</code> is persistent, this
	 * method set the actual "userColor" setting. Otherwise it
	 * set a temporary value.
	 * @param color
	 */
    public void setUserColor(String color) {
        Map<String, UserPreference> preferences = getPreferences();
        UserPreference up = preferences.get("userColor");
        if (null == up) {
            up = new UserPreference(getEntity(), "userColor");
            preferences.put("userColor", up);
        }
        up.setValue(color);
    }

    /** 
	 * A lazy-initializing override of {@link #getAddress()} that
	 * never returns null.
	 */
    @Override
    public Address getAddress() {
        Address a = super.getAddress();
        if (null == a) {
            a = new Address();
            super.setAddress(a);
        }
        return a;
    }

    /** 
	 * A lazy-initializing override of {@link #getContact()} that
	 * never returns null.
	 */
    @Override
    public Contact getContact() {
        Contact c = super.getContact();
        if (null == c) {
            c = new Contact();
            super.setContact(c);
        }
        return c;
    }

    /** List of possible (all) user types */
    public UserType[] getPossibleUserTypes() {
        return new UserType[] { UserType.SUBCONTRACTOR, UserType.CLIENT, UserType.MANAGER };
    }

    /** List of possible (all) user types */
    public List<SelectItem> getPossibleUserTypesAsSelectItems() {
        ArrayList<SelectItem> l = new ArrayList<SelectItem>();
        for (UserType ut : getPossibleUserTypes()) {
            SelectItem si = new SelectItem();
            si.setValue(ut);
            si.setLabel(ut.getDisplayName());
            l.add(si);
        }
        return l;
    }
}
