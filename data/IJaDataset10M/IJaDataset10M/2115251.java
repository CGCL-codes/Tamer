package com.germinus.xpression.groupware.web;

import com.germinus.xpression.cms.jcr.JCRUtil;
import com.germinus.xpression.groupware.CommunityManagementListener;
import com.germinus.xpression.groupware.CommunityManager;
import com.germinus.xpression.groupware.LiferayCommunityManagementListener;
import com.germinus.xpression.groupware.util.GroupwareManagerRegistry;
import com.germinus.xpression.groupware.util.JCRGroupwareUtil;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Registers the groupware namespasce.
 *
 * @author Acheca
 */
public class GroupwareInitializer implements ServletContextListener {

    private static CommunityManagementListener communityManagementListener;

    private static CommunityManager communityManager = GroupwareManagerRegistry.getCommunityManager();

    public void contextInitialized(ServletContextEvent servletContext) {
        JCRGroupwareUtil.init();
        communityManagementListener = new LiferayCommunityManagementListener();
        communityManager.addCommunityManagementListener(communityManagementListener);
    }

    public void contextDestroyed(ServletContextEvent servletContext) {
        communityManager.removeCommunityManagementListener(communityManagementListener);
    }
}
