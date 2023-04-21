package com.sitescape.team.liferay.security.auth;

import com.liferay.util.Validator;

public class ScreenNameValidator extends com.liferay.portal.security.auth.ScreenNameValidator {

    public boolean validate(long companyId, String screenName) {
        if (Validator.isNull(screenName) || (screenName.indexOf("/") != -1) || (screenName.indexOf("\\") != -1) || (screenName.indexOf("*") != -1) || (screenName.indexOf("?") != -1) || (screenName.indexOf("\"") != -1) || (screenName.indexOf("<") != -1) || (screenName.indexOf(">") != -1) || (screenName.indexOf(";") != -1) || (screenName.indexOf("|") != -1)) {
            return false;
        } else {
            return true;
        }
    }
}
