package org.kablink.teaming.web.servlet.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.kablink.teaming.security.accesstoken.AccessTokenManager;
import org.kablink.teaming.util.SpringContextUtil;

public class ContextListener implements ServletContextListener {

    public void contextInitialized(ServletContextEvent sce) {
        AccessTokenManager accessTokenManager = (AccessTokenManager) SpringContextUtil.getBean("accessTokenManager");
        accessTokenManager.destroyAllTokenInfoSession();
        accessTokenManager.destroyAllTokenInfoRequest();
        accessTokenManager.destroyAllTokenInfoApplication();
    }

    public void contextDestroyed(ServletContextEvent sce) {
    }
}
