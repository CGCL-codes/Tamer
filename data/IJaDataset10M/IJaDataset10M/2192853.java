package com.liferay.portlet.enterpriseadmin.search;

import com.liferay.portal.kernel.dao.search.RowChecker;
import com.liferay.portal.model.PasswordPolicy;
import com.liferay.portal.model.User;
import com.liferay.portal.service.UserLocalServiceUtil;
import javax.portlet.RenderResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <a href="UserPasswordPolicyChecker.java.html"><b><i>View Source</i></b></a>
 *
 * @author Scott Lee
 *
 */
public class UserPasswordPolicyChecker extends RowChecker {

    public UserPasswordPolicyChecker(RenderResponse res, PasswordPolicy passwordPolicy) {
        super(res);
        _passwordPolicy = passwordPolicy;
    }

    public boolean isChecked(Object obj) {
        User user = (User) obj;
        try {
            return UserLocalServiceUtil.hasPasswordPolicyUser(_passwordPolicy.getPasswordPolicyId(), user.getUserId());
        } catch (Exception e) {
            _log.error(e);
            return false;
        }
    }

    private static Log _log = LogFactory.getLog(UserPasswordPolicyChecker.class);

    private PasswordPolicy _passwordPolicy;
}
