package com.liferay.portal.security.permission;

import com.liferay.portal.model.User;
import com.liferay.portal.security.auth.PrincipalThreadLocal;
import com.liferay.portal.service.UserLocalServiceUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <a href="DoAsUserThread.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public abstract class DoAsUserThread extends Thread {

    public DoAsUserThread(long userId) {
        _userId = userId;
    }

    public boolean isSuccess() {
        return _success;
    }

    public void run() {
        PermissionChecker permissionChecker = null;
        try {
            PrincipalThreadLocal.setName(_userId);
            User user = UserLocalServiceUtil.getUserById(_userId);
            permissionChecker = PermissionCheckerFactory.create(user, true);
            PermissionThreadLocal.setPermissionChecker(permissionChecker);
            doRun();
            _success = true;
        } catch (Exception e) {
            _log.error(e, e);
        } finally {
            if (permissionChecker != null) {
                try {
                    PermissionCheckerFactory.recycle(permissionChecker);
                } catch (Exception e) {
                    _log.error(e, e);
                }
            }
        }
    }

    protected abstract void doRun() throws Exception;

    private static Log _log = LogFactory.getLog(DoAsUserThread.class);

    private long _userId;

    private boolean _success;
}
