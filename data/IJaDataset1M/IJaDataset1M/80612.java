package com.liferay.portal.model;

import com.liferay.portal.model.BaseModel;

/**
 * <a href="PermissionModel.java.html"><b><i>View Source</i></b></a>
 *
 * <p>
 * ServiceBuilder generated this class. Modifications in this class will be overwritten
 * the next time is generated.
 * </p>
 *
 * <p>
 * This interface is a model that represents the <code>Permission_</code> table
 * in the database.
 * </p>
 *
 * @author Brian Wing Shun Chan
 *
 * @see com.liferay.portal.service.model.Permission
 * @see com.liferay.portal.service.model.impl.PermissionImpl
 * @see com.liferay.portal.service.model.impl.PermissionModelImpl
 *
 */
public interface PermissionModel extends BaseModel {

    public long getPrimaryKey();

    public void setPrimaryKey(long pk);

    public long getPermissionId();

    public void setPermissionId(long permissionId);

    public long getCompanyId();

    public void setCompanyId(long companyId);

    public String getActionId();

    public void setActionId(String actionId);

    public long getResourceId();

    public void setResourceId(long resourceId);
}
