package com.liferay.portal.model.impl;

import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.model.impl.BaseModelImpl;
import com.liferay.portal.util.PropsUtil;
import java.io.Serializable;
import java.sql.Types;

/**
 * <a href="PasswordPolicyRelModelImpl.java.html"><b><i>View Source</i></b></a>
 *
 * <p>
 * ServiceBuilder generated this class. Modifications in this class will be overwritten
 * the next time is generated.
 * </p>
 *
 * <p>
 * This class is a model that represents the <code>PasswordPolicyRel</code> table
 * in the database.
 * </p>
 *
 * @author Brian Wing Shun Chan
 *
 * @see com.liferay.portal.service.model.PasswordPolicyRel
 * @see com.liferay.portal.service.model.PasswordPolicyRelModel
 * @see com.liferay.portal.service.model.impl.PasswordPolicyRelImpl
 *
 */
public class PasswordPolicyRelModelImpl extends BaseModelImpl {

    public static String TABLE_NAME = "PasswordPolicyRel";

    public static Object[][] TABLE_COLUMNS = { { "passwordPolicyRelId", new Integer(Types.BIGINT) }, { "passwordPolicyId", new Integer(Types.BIGINT) }, { "classNameId", new Integer(Types.BIGINT) }, { "classPK", new Integer(Types.BIGINT) } };

    public static String TABLE_SQL_CREATE = "create table PasswordPolicyRel (passwordPolicyRelId LONG not null primary key,passwordPolicyId LONG,classNameId LONG,classPK LONG)";

    public static String TABLE_SQL_DROP = "drop table PasswordPolicyRel";

    public static boolean XSS_ALLOW_BY_MODEL = GetterUtil.getBoolean(PropsUtil.get("xss.allow.com.liferay.portal.model.PasswordPolicyRel"), XSS_ALLOW);

    public static long LOCK_EXPIRATION_TIME = GetterUtil.getLong(PropsUtil.get("lock.expiration.time.com.liferay.portal.model.PasswordPolicyRelModel"));

    public PasswordPolicyRelModelImpl() {
    }

    public long getPrimaryKey() {
        return _passwordPolicyRelId;
    }

    public void setPrimaryKey(long pk) {
        setPasswordPolicyRelId(pk);
    }

    public Serializable getPrimaryKeyObj() {
        return new Long(_passwordPolicyRelId);
    }

    public long getPasswordPolicyRelId() {
        return _passwordPolicyRelId;
    }

    public void setPasswordPolicyRelId(long passwordPolicyRelId) {
        if (passwordPolicyRelId != _passwordPolicyRelId) {
            _passwordPolicyRelId = passwordPolicyRelId;
        }
    }

    public long getPasswordPolicyId() {
        return _passwordPolicyId;
    }

    public void setPasswordPolicyId(long passwordPolicyId) {
        if (passwordPolicyId != _passwordPolicyId) {
            _passwordPolicyId = passwordPolicyId;
        }
    }

    public long getClassNameId() {
        return _classNameId;
    }

    public void setClassNameId(long classNameId) {
        if (classNameId != _classNameId) {
            _classNameId = classNameId;
        }
    }

    public long getClassPK() {
        return _classPK;
    }

    public void setClassPK(long classPK) {
        if (classPK != _classPK) {
            _classPK = classPK;
        }
    }

    public Object clone() {
        PasswordPolicyRelImpl clone = new PasswordPolicyRelImpl();
        clone.setPasswordPolicyRelId(getPasswordPolicyRelId());
        clone.setPasswordPolicyId(getPasswordPolicyId());
        clone.setClassNameId(getClassNameId());
        clone.setClassPK(getClassPK());
        return clone;
    }

    public int compareTo(Object obj) {
        if (obj == null) {
            return -1;
        }
        PasswordPolicyRelImpl passwordPolicyRel = (PasswordPolicyRelImpl) obj;
        long pk = passwordPolicyRel.getPrimaryKey();
        if (getPrimaryKey() < pk) {
            return -1;
        } else if (getPrimaryKey() > pk) {
            return 1;
        } else {
            return 0;
        }
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        PasswordPolicyRelImpl passwordPolicyRel = null;
        try {
            passwordPolicyRel = (PasswordPolicyRelImpl) obj;
        } catch (ClassCastException cce) {
            return false;
        }
        long pk = passwordPolicyRel.getPrimaryKey();
        if (getPrimaryKey() == pk) {
            return true;
        } else {
            return false;
        }
    }

    public int hashCode() {
        return (int) getPrimaryKey();
    }

    private long _passwordPolicyRelId;

    private long _passwordPolicyId;

    private long _classNameId;

    private long _classPK;
}
