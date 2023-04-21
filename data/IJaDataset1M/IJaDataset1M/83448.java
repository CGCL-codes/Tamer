package com.liferay.portlet.admin.ejb;

/**
 * <a href="AdminConfigManagerUtil.java.html"><b><i>View Source</i></b></a>
 *
 * @author  Brian Wing Shun Chan
 * @version $Revision: 1.71 $
 *
 */
public class AdminConfigManagerUtil {

    public static final String PORTLET_ID = "9";

    public static java.util.List getAdminConfig(java.lang.String companyId, java.lang.String type) throws com.liferay.portal.SystemException {
        try {
            AdminConfigManager adminConfigManager = AdminConfigManagerFactory.getManager();
            return adminConfigManager.getAdminConfig(companyId, type);
        } catch (com.liferay.portal.SystemException se) {
            throw se;
        } catch (Exception e) {
            throw new com.liferay.portal.SystemException(e);
        }
    }

    public static com.liferay.portlet.admin.model.JournalConfig getJournalConfig(java.lang.String companyId, java.lang.String portletId) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException {
        try {
            AdminConfigManager adminConfigManager = AdminConfigManagerFactory.getManager();
            return adminConfigManager.getJournalConfig(companyId, portletId);
        } catch (com.liferay.portal.PortalException pe) {
            throw pe;
        } catch (com.liferay.portal.SystemException se) {
            throw se;
        } catch (Exception e) {
            throw new com.liferay.portal.SystemException(e);
        }
    }

    public static com.liferay.portlet.admin.model.ShoppingConfig getShoppingConfig(java.lang.String companyId) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException {
        try {
            AdminConfigManager adminConfigManager = AdminConfigManagerFactory.getManager();
            return adminConfigManager.getShoppingConfig(companyId);
        } catch (com.liferay.portal.PortalException pe) {
            throw pe;
        } catch (com.liferay.portal.SystemException se) {
            throw se;
        } catch (Exception e) {
            throw new com.liferay.portal.SystemException(e);
        }
    }

    public static com.liferay.portlet.admin.model.UserConfig getUserConfig(java.lang.String companyId) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException {
        try {
            AdminConfigManager adminConfigManager = AdminConfigManagerFactory.getManager();
            return adminConfigManager.getUserConfig(companyId);
        } catch (com.liferay.portal.PortalException pe) {
            throw pe;
        } catch (com.liferay.portal.SystemException se) {
            throw se;
        } catch (Exception e) {
            throw new com.liferay.portal.SystemException(e);
        }
    }

    public static void updateJournalConfig(com.liferay.portlet.admin.model.JournalConfig journalConfig, java.lang.String portletId) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException {
        try {
            AdminConfigManager adminConfigManager = AdminConfigManagerFactory.getManager();
            adminConfigManager.updateJournalConfig(journalConfig, portletId);
        } catch (com.liferay.portal.PortalException pe) {
            throw pe;
        } catch (com.liferay.portal.SystemException se) {
            throw se;
        } catch (Exception e) {
            throw new com.liferay.portal.SystemException(e);
        }
    }

    public static void updateShoppingConfig(com.liferay.portlet.admin.model.ShoppingConfig shoppingConfig) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException {
        try {
            AdminConfigManager adminConfigManager = AdminConfigManagerFactory.getManager();
            adminConfigManager.updateShoppingConfig(shoppingConfig);
        } catch (com.liferay.portal.PortalException pe) {
            throw pe;
        } catch (com.liferay.portal.SystemException se) {
            throw se;
        } catch (Exception e) {
            throw new com.liferay.portal.SystemException(e);
        }
    }

    public static void updateUserConfig(com.liferay.portlet.admin.model.UserConfig userConfig) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException {
        try {
            AdminConfigManager adminConfigManager = AdminConfigManagerFactory.getManager();
            adminConfigManager.updateUserConfig(userConfig);
        } catch (com.liferay.portal.PortalException pe) {
            throw pe;
        } catch (com.liferay.portal.SystemException se) {
            throw se;
        } catch (Exception e) {
            throw new com.liferay.portal.SystemException(e);
        }
    }
}
