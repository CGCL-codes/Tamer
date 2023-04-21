package com.liferay.portal.security.auth;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

/**
 * <a href="PrincipalThreadLocal.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class PrincipalThreadLocal {

    public static String getName() {
        String name = (String) _threadLocal.get();
        if (_log.isDebugEnabled()) {
            _log.debug("getName " + name);
        }
        return name;
    }

    public static void setName(String name) {
        if (_log.isDebugEnabled()) {
            _log.debug("setName " + name);
        }
        _threadLocal.set(name);
    }

    private static Log _log = LogFactoryUtil.getLog(PrincipalThreadLocal.class);

    private static ThreadLocal _threadLocal = new ThreadLocal();
}
