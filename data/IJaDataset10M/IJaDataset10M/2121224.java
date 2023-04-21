package com.liferay.portal.model;

import com.liferay.portal.model.BaseModel;
import com.liferay.portal.service.persistence.UserGroupRolePK;

/**
 * <a href="UserGroupRoleModel.java.html"><b><i>View Source</i></b></a>
 *
 * <p>
 * ServiceBuilder generated this class. Modifications in this class will be overwritten
 * the next time is generated.
 * </p>
 *
 * <p>
 * This interface is a model that represents the <code>UserGroupRole</code> table
 * in the database.
 * </p>
 *
 * @author Brian Wing Shun Chan
 *
 * @see com.liferay.portal.service.model.UserGroupRole
 * @see com.liferay.portal.service.model.impl.UserGroupRoleImpl
 * @see com.liferay.portal.service.model.impl.UserGroupRoleModelImpl
 *
 */
public interface UserGroupRoleModel extends BaseModel {

    public UserGroupRolePK getPrimaryKey();

    public void setPrimaryKey(UserGroupRolePK pk);

    public long getUserId();

    public void setUserId(long userId);

    public long getGroupId();

    public void setGroupId(long groupId);

    public long getRoleId();

    public void setRoleId(long roleId);
}
