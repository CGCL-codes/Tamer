package com.liferay.portal.model.impl;

import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.model.impl.BaseModelImpl;
import com.liferay.portal.util.PropsUtil;
import com.liferay.util.XSSUtil;
import java.io.Serializable;
import java.sql.Types;

/**
 * <a href="ListTypeModelImpl.java.html"><b><i>View Source</i></b></a>
 *
 * <p>
 * ServiceBuilder generated this class. Modifications in this class will be overwritten
 * the next time is generated.
 * </p>
 *
 * <p>
 * This class is a model that represents the <code>ListType</code> table in the
 * database.
 * </p>
 *
 * @author Brian Wing Shun Chan
 *
 * @see com.liferay.portal.service.model.ListType
 * @see com.liferay.portal.service.model.ListTypeModel
 * @see com.liferay.portal.service.model.impl.ListTypeImpl
 *
 */
public class ListTypeModelImpl extends BaseModelImpl {

    public static String TABLE_NAME = "ListType";

    public static Object[][] TABLE_COLUMNS = { { "listTypeId", new Integer(Types.INTEGER) }, { "name", new Integer(Types.VARCHAR) }, { "type_", new Integer(Types.VARCHAR) } };

    public static String TABLE_SQL_CREATE = "create table ListType (listTypeId INTEGER not null primary key,name VARCHAR(75) null,type_ VARCHAR(75) null)";

    public static String TABLE_SQL_DROP = "drop table ListType";

    public static boolean XSS_ALLOW_BY_MODEL = GetterUtil.getBoolean(PropsUtil.get("xss.allow.com.liferay.portal.model.ListType"), XSS_ALLOW);

    public static boolean XSS_ALLOW_NAME = GetterUtil.getBoolean(PropsUtil.get("xss.allow.com.liferay.portal.model.ListType.name"), XSS_ALLOW_BY_MODEL);

    public static boolean XSS_ALLOW_TYPE = GetterUtil.getBoolean(PropsUtil.get("xss.allow.com.liferay.portal.model.ListType.type"), XSS_ALLOW_BY_MODEL);

    public static long LOCK_EXPIRATION_TIME = GetterUtil.getLong(PropsUtil.get("lock.expiration.time.com.liferay.portal.model.ListTypeModel"));

    public ListTypeModelImpl() {
    }

    public int getPrimaryKey() {
        return _listTypeId;
    }

    public void setPrimaryKey(int pk) {
        setListTypeId(pk);
    }

    public Serializable getPrimaryKeyObj() {
        return new Integer(_listTypeId);
    }

    public int getListTypeId() {
        return _listTypeId;
    }

    public void setListTypeId(int listTypeId) {
        if (listTypeId != _listTypeId) {
            _listTypeId = listTypeId;
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

    public String getType() {
        return GetterUtil.getString(_type);
    }

    public void setType(String type) {
        if (((type == null) && (_type != null)) || ((type != null) && (_type == null)) || ((type != null) && (_type != null) && !type.equals(_type))) {
            if (!XSS_ALLOW_TYPE) {
                type = XSSUtil.strip(type);
            }
            _type = type;
        }
    }

    public Object clone() {
        ListTypeImpl clone = new ListTypeImpl();
        clone.setListTypeId(getListTypeId());
        clone.setName(getName());
        clone.setType(getType());
        return clone;
    }

    public int compareTo(Object obj) {
        if (obj == null) {
            return -1;
        }
        ListTypeImpl listType = (ListTypeImpl) obj;
        int value = 0;
        value = getName().toLowerCase().compareTo(listType.getName().toLowerCase());
        if (value != 0) {
            return value;
        }
        return 0;
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        ListTypeImpl listType = null;
        try {
            listType = (ListTypeImpl) obj;
        } catch (ClassCastException cce) {
            return false;
        }
        int pk = listType.getPrimaryKey();
        if (getPrimaryKey() == pk) {
            return true;
        } else {
            return false;
        }
    }

    public int hashCode() {
        return (int) getPrimaryKey();
    }

    private int _listTypeId;

    private String _name;

    private String _type;
}
