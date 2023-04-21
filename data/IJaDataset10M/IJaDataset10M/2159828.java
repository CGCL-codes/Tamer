package com.liferay.portal.model;

import com.liferay.portal.model.BaseModel;

/**
 * <a href="PluginSettingModel.java.html"><b><i>View Source</i></b></a>
 *
 * <p>
 * ServiceBuilder generated this class. Modifications in this class will be overwritten
 * the next time is generated.
 * </p>
 *
 * <p>
 * This interface is a model that represents the <code>PluginSetting</code> table
 * in the database.
 * </p>
 *
 * @author Brian Wing Shun Chan
 *
 * @see com.liferay.portal.service.model.PluginSetting
 * @see com.liferay.portal.service.model.impl.PluginSettingImpl
 * @see com.liferay.portal.service.model.impl.PluginSettingModelImpl
 *
 */
public interface PluginSettingModel extends BaseModel {

    public long getPrimaryKey();

    public void setPrimaryKey(long pk);

    public long getPluginSettingId();

    public void setPluginSettingId(long pluginSettingId);

    public long getCompanyId();

    public void setCompanyId(long companyId);

    public String getPluginId();

    public void setPluginId(String pluginId);

    public String getPluginType();

    public void setPluginType(String pluginType);

    public String getRoles();

    public void setRoles(String roles);

    public boolean getActive();

    public boolean isActive();

    public void setActive(boolean active);
}
