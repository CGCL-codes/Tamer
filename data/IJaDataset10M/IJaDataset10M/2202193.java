package com.codebitches.spruce.module.bb.web.spring;

import com.codebitches.spruce.module.bb.domain.hibernate.SprucebbUser;

/**
 * @author Stuart Eccles
 */
public class RegisterUserForm {

    private SprucebbUser user;

    private boolean acceptTermsAndConditions;

    private String repeatedPassword;

    /**
	 * @return Returns the acceptTermsAndConditions.
	 */
    public boolean isAcceptTermsAndConditions() {
        return acceptTermsAndConditions;
    }

    /**
	 * @param acceptTermsAndConditions The acceptTermsAndConditions to set.
	 */
    public void setAcceptTermsAndConditions(boolean acceptTermsAndConditions) {
        this.acceptTermsAndConditions = acceptTermsAndConditions;
    }

    /**
	 * @return Returns the repeatedPassword.
	 */
    public String getRepeatedPassword() {
        return repeatedPassword;
    }

    /**
	 * @param repeatedPassword The repeatedPassword to set.
	 */
    public void setRepeatedPassword(String repeatedPassword) {
        this.repeatedPassword = repeatedPassword;
    }

    /**
	 * @return Returns the user.
	 */
    public SprucebbUser getUser() {
        return user;
    }

    /**
	 * @param user The user to set.
	 */
    public void setUser(SprucebbUser user) {
        this.user = user;
    }
}
