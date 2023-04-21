package com.liferay.portal.model.impl;

import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.model.impl.BaseModelImpl;
import com.liferay.portal.util.PropsUtil;
import com.liferay.util.XSSUtil;
import java.io.Serializable;
import java.sql.Types;

/**
 * <a href="UserGroupModelImpl.java.html"><b><i>View Source</i></b></a>
 *
 * <p>
 * ServiceBuilder generated this class. Modifications in this class will be overwritten
 * the next time is generated.
 * </p>
 *
 * <p>
 * This class is a model that represents the <code>UserGroup</code> table in the
 * database.
 * </p>
 *
 * @author Brian Wing Shun Chan
 *
 * @see com.liferay.portal.service.model.UserGroup
 * @see com.liferay.portal.service.model.UserGroupModel
 * @see com.liferay.portal.service.model.impl.UserGroupImpl
 *
 */
public class UserGroupModelImpl extends BaseModelImpl {

    public static String TABLE_NAME = "UserGroup";

    public static Object[][] TABLE_COLUMNS = { { "userGroupId", new Integer(Types.BIGINT) }, { "companyId", new Integer(Types.BIGINT) }, { "parentUserGroupId", new Integer(Types.BIGINT) }, { "name", new Integer(Types.VARCHAR) }, { "description", new Integer(Types.VARCHAR) } };

    public static String TABLE_SQL_CREATE = "create table UserGroup (userGroupId LONG not null primary key,companyId LONG,parentUserGroupId LONG,name VARCHAR(75) null,description STRING null)";

    public static String TABLE_SQL_DROP = "drop table UserGroup";

    public static boolean XSS_ALLOW_BY_MODEL = GetterUtil.getBoolean(PropsUtil.get("xss.allow.com.liferay.portal.model.UserGroup"), XSS_ALLOW);

    public static boolean XSS_ALLOW_NAME = GetterUtil.getBoolean(PropsUtil.get("xss.allow.com.liferay.portal.model.UserGroup.name"), XSS_ALLOW_BY_MODEL);

    public static boolean XSS_ALLOW_DESCRIPTION = GetterUtil.getBoolean(PropsUtil.get("xss.allow.com.liferay.portal.model.UserGroup.description"), XSS_ALLOW_BY_MODEL);

    public static long LOCK_EXPIRATION_TIME = GetterUtil.getLong(PropsUtil.get("lock.expiration.time.com.liferay.portal.model.UserGroupModel"));

    public UserGroupModelImpl() {
    }

    public long getPrimaryKey() {
        return _userGroupId;
    }

    public void setPrimaryKey(long pk) {
        setUserGroupId(pk);
    }

    public Serializable getPrimaryKeyObj() {
        return new Long(_userGroupId);
    }

    public long getUserGroupId() {
        return _userGroupId;
    }

    public void setUserGroupId(long userGroupId) {
        if (userGroupId != _userGroupId) {
            _userGroupId = userGroupId;
        }
    }

    public long getCompanyId() {
        return _companyId;
    }

    public void setCompanyId(long companyId) {
        if (companyId != _companyId) {
            _companyId = companyId;
        }
    }

    public long getParentUserGroupId() {
        return _parentUserGroupId;
    }

    public void setParentUserGroupId(long parentUserGroupId) {
        if (parentUserGroupId != _parentUserGroupId) {
            _parentUserGroupId = parentUserGroupId;
        }
    }

    public String getName() {
        return GetterUtil.getString(_name);
    }

    public void setName(String name) {
        if (((name == null) && (_name != null)) || ((name != null) && (_name == null)) || ((name != null) && (_name != null) && !name.equals(_name))) {
            if (!XSS_ALLOW_NAME) {
                name = XSSUtil.strip(name);
            }
            _name = name;
        }
    }

    public String getDescription() {
        return GetterUtil.getString(_description);
    }

    public void setDescription(String description) {
        if (((description == null) && (_description != null)) || ((description != null) && (_description == null)) || ((description != null) && (_description != null) && !description.equals(_description))) {
            if (!XSS_ALLOW_DESCRIPTION) {
                description = XSSUtil.strip(description);
            }
            _description = description;
        }
    }

    public Object clone() {
        UserGroupImpl clone = new UserGroupImpl();
        clone.setUserGroupId(getUserGroupId());
        clone.setCompanyId(getCompanyId());
        clone.setParentUserGroupId(getParentUserGroupId());
        clone.setName(getName());
        clone.setDescription(getDescription());
        return clone;
    }

    public int compareTo(Object obj) {
        if (obj == null) {
            return -1;
        }
        UserGroupImpl userGroup = (UserGroupImpl) obj;
        int value = 0;
        value = getName().compareTo(userGroup.getName());
        if (value != 0) {
            return value;
        }
        return 0;
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        UserGroupImpl userGroup = null;
        try {
            userGroup = (UserGroupImpl) obj;
        } catch (ClassCastException cce) {
            return false;
        }
        long pk = userGroup.getPrimaryKey();
        if (getPrimaryKey() == pk) {
            return true;
        } else {
            return false;
        }
    }

    public int hashCode() {
        return (int) getPrimaryKey();
    }

    private long _userGroupId;

    private long _companyId;

    private long _parentUserGroupId;

    private String _name;

    private String _description;
}
