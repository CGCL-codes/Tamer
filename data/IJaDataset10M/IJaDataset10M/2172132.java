package com.liferay.portal.model;

import com.liferay.portal.model.BaseModel;

/**
 * <a href="LayoutSetModel.java.html"><b><i>View Source</i></b></a>
 *
 * <p>
 * ServiceBuilder generated this class. Modifications in this class will be overwritten
 * the next time is generated.
 * </p>
 *
 * <p>
 * This interface is a model that represents the <code>LayoutSet</code> table in
 * the database.
 * </p>
 *
 * @author Brian Wing Shun Chan
 *
 * @see com.liferay.portal.service.model.LayoutSet
 * @see com.liferay.portal.service.model.impl.LayoutSetImpl
 * @see com.liferay.portal.service.model.impl.LayoutSetModelImpl
 *
 */
public interface LayoutSetModel extends BaseModel {

    public long getPrimaryKey();

    public void setPrimaryKey(long pk);

    public long getLayoutSetId();

    public void setLayoutSetId(long layoutSetId);

    public long getGroupId();

    public void setGroupId(long groupId);

    public long getCompanyId();

    public void setCompanyId(long companyId);

    public boolean getPrivateLayout();

    public boolean isPrivateLayout();

    public void setPrivateLayout(boolean privateLayout);

    public boolean getLogo();

    public boolean isLogo();

    public void setLogo(boolean logo);

    public long getLogoId();

    public void setLogoId(long logoId);

    public String getThemeId();

    public void setThemeId(String themeId);

    public String getColorSchemeId();

    public void setColorSchemeId(String colorSchemeId);

    public String getWapThemeId();

    public void setWapThemeId(String wapThemeId);

    public String getWapColorSchemeId();

    public void setWapColorSchemeId(String wapColorSchemeId);

    public String getCss();

    public void setCss(String css);

    public int getPageCount();

    public void setPageCount(int pageCount);

    public String getVirtualHost();

    public void setVirtualHost(String virtualHost);
}
