package com.liferay.portal.service.ejb;

import com.liferay.portal.service.PluginSettingLocalService;
import com.liferay.portal.service.PluginSettingLocalServiceFactory;
import javax.ejb.CreateException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

/**
 * <a href="PluginSettingLocalServiceEJBImpl.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class PluginSettingLocalServiceEJBImpl implements PluginSettingLocalService, SessionBean {

    public java.util.List dynamicQuery(com.liferay.portal.kernel.dao.DynamicQueryInitializer queryInitializer) throws com.liferay.portal.SystemException {
        return PluginSettingLocalServiceFactory.getTxImpl().dynamicQuery(queryInitializer);
    }

    public java.util.List dynamicQuery(com.liferay.portal.kernel.dao.DynamicQueryInitializer queryInitializer, int begin, int end) throws com.liferay.portal.SystemException {
        return PluginSettingLocalServiceFactory.getTxImpl().dynamicQuery(queryInitializer, begin, end);
    }

    public void checkPermission(java.lang.String userId, java.lang.String pluginId, java.lang.String pluginType) throws com.liferay.portal.PortalException {
        PluginSettingLocalServiceFactory.getTxImpl().checkPermission(userId, pluginId, pluginType);
    }

    public com.liferay.portal.model.PluginSetting getDefaultPluginSetting() {
        return PluginSettingLocalServiceFactory.getTxImpl().getDefaultPluginSetting();
    }

    public com.liferay.portal.model.PluginSetting getPluginSetting(java.lang.String companyId, java.lang.String pluginId, java.lang.String pluginType) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException {
        return PluginSettingLocalServiceFactory.getTxImpl().getPluginSetting(companyId, pluginId, pluginType);
    }

    public boolean hasPermission(java.lang.String userId, java.lang.String pluginId, java.lang.String pluginType) {
        return PluginSettingLocalServiceFactory.getTxImpl().hasPermission(userId, pluginId, pluginType);
    }

    public com.liferay.portal.model.PluginSetting updatePluginSetting(java.lang.String companyId, java.lang.String pluginId, java.lang.String pluginType, java.lang.String roles, boolean active) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException {
        return PluginSettingLocalServiceFactory.getTxImpl().updatePluginSetting(companyId, pluginId, pluginType, roles, active);
    }

    public void ejbCreate() throws CreateException {
    }

    public void ejbRemove() {
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public SessionContext getSessionContext() {
        return _sc;
    }

    public void setSessionContext(SessionContext sc) {
        _sc = sc;
    }

    private SessionContext _sc;
}
