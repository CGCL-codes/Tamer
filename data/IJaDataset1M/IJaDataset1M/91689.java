package com.liferay.portal.security.pwd;

import com.liferay.portal.PortalException;
import com.liferay.portal.SystemException;
import com.liferay.portal.model.PasswordPolicy;

/**
 * <a href="BasicToolkit.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public abstract class BasicToolkit {

    public abstract String generate();

    public void validate(String password1, String password2, PasswordPolicy passwordPolicy) throws PortalException, SystemException {
        validate(0, password1, password2, passwordPolicy);
    }

    public abstract void validate(long userId, String password1, String password2, PasswordPolicy passwordPolicy) throws PortalException, SystemException;
}
