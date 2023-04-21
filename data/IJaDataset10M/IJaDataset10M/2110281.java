package org.apache.struts2.showcase.chat;

import javax.servlet.http.HttpSession;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;

/**
 * Authenticate showcase chat example, make sure everyone have a username.
 */
public class ChatInterceptor implements Interceptor {

    private static final Log _log = LogFactory.getLog(ChatInterceptor.class);

    private static final long serialVersionUID = 1L;

    public static final String CHAT_USER_SESSION_KEY = "ChatUserSessionKey";

    public void destroy() {
    }

    public void init() {
    }

    public String intercept(ActionInvocation invocation) throws Exception {
        HttpSession session = (HttpSession) ActionContext.getContext().get(ActionContext.SESSION);
        User chatUser = (User) session.getAttribute(CHAT_USER_SESSION_KEY);
        if (chatUser == null) {
            _log.debug("Chat user not logged in");
            return Action.LOGIN;
        }
        return invocation.invoke();
    }
}
