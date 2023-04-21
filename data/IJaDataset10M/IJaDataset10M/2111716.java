package com.coyousoft.wangyu.support;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.coyousoft.wangyu.entity.WangyuUser;

/**
 * SessionListener 既是 session 监听器，用以监听 session 的创建与销毁，又是 session 容器，记录登录状态
 * 的用户的 session 。
 */
public final class SessionListener implements HttpSessionListener {

    private static Map<Integer, HttpSession> sessionMap = new HashMap<Integer, HttpSession>(10000);

    private static final Log log = LogFactory.getLog(SessionListener.class);

    private static final boolean bDebug;

    static {
        bDebug = log.isDebugEnabled();
    }

    /**
     * 在容器里记录登录状态的用户的 session 。
     * 
     * @param userId
     * @param session
     */
    public static void setSession(Integer userId, HttpSession session) {
        sessionMap.put(userId, session);
    }

    /**
     * 当用户手动退出以及session失效时，从容器里清除 session 。
     * 
     * @param userId
     * @return
     */
    public static HttpSession removeSession(Integer userId) {
        return sessionMap.remove(userId);
    }

    public void sessionCreated(HttpSessionEvent event) {
    }

    /**
     * 当 session 失效时，从容器里移除登录状态的 session 。
     */
    public void sessionDestroyed(HttpSessionEvent event) {
        HttpSession session = event.getSession();
        WangyuUser wangyuUser = (WangyuUser) session.getAttribute(WangyuUser.KEY_IN_SESSION);
        if (wangyuUser.getLoginStatus() == WangyuUser.LOGIN_STATUS_YES) {
            removeSession(wangyuUser.getUserId());
        }
        if (bDebug) log.debug("sid=" + session.getId() + ", " + wangyuUser.toString());
    }
}
