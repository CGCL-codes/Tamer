package com.liferay.portal.model.impl;

import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.model.impl.BaseModelImpl;
import com.liferay.portal.util.PropsUtil;
import com.liferay.util.XSSUtil;
import java.io.Serializable;
import java.sql.Types;
import java.util.Date;

/**
 * <a href="SubscriptionModelImpl.java.html"><b><i>View Source</i></b></a>
 *
 * <p>
 * ServiceBuilder generated this class. Modifications in this class will be overwritten
 * the next time is generated.
 * </p>
 *
 * <p>
 * This class is a model that represents the <code>Subscription</code> table in
 * the database.
 * </p>
 *
 * @author Brian Wing Shun Chan
 *
 * @see com.liferay.portal.service.model.Subscription
 * @see com.liferay.portal.service.model.SubscriptionModel
 * @see com.liferay.portal.service.model.impl.SubscriptionImpl
 *
 */
public class SubscriptionModelImpl extends BaseModelImpl {

    public static String TABLE_NAME = "Subscription";

    public static Object[][] TABLE_COLUMNS = { { "subscriptionId", new Integer(Types.BIGINT) }, { "companyId", new Integer(Types.BIGINT) }, { "userId", new Integer(Types.BIGINT) }, { "userName", new Integer(Types.VARCHAR) }, { "createDate", new Integer(Types.TIMESTAMP) }, { "modifiedDate", new Integer(Types.TIMESTAMP) }, { "classNameId", new Integer(Types.BIGINT) }, { "classPK", new Integer(Types.BIGINT) }, { "frequency", new Integer(Types.VARCHAR) } };

    public static String TABLE_SQL_CREATE = "create table Subscription (subscriptionId LONG not null primary key,companyId LONG,userId LONG,userName VARCHAR(75) null,createDate DATE null,modifiedDate DATE null,classNameId LONG,classPK LONG,frequency VARCHAR(75) null)";

    public static String TABLE_SQL_DROP = "drop table Subscription";

    public static boolean XSS_ALLOW_BY_MODEL = GetterUtil.getBoolean(PropsUtil.get("xss.allow.com.liferay.portal.model.Subscription"), XSS_ALLOW);

    public static boolean XSS_ALLOW_USERNAME = GetterUtil.getBoolean(PropsUtil.get("xss.allow.com.liferay.portal.model.Subscription.userName"), XSS_ALLOW_BY_MODEL);

    public static boolean XSS_ALLOW_FREQUENCY = GetterUtil.getBoolean(PropsUtil.get("xss.allow.com.liferay.portal.model.Subscription.frequency"), XSS_ALLOW_BY_MODEL);

    public static long LOCK_EXPIRATION_TIME = GetterUtil.getLong(PropsUtil.get("lock.expiration.time.com.liferay.portal.model.SubscriptionModel"));

    public SubscriptionModelImpl() {
    }

    public long getPrimaryKey() {
        return _subscriptionId;
    }

    public void setPrimaryKey(long pk) {
        setSubscriptionId(pk);
    }

    public Serializable getPrimaryKeyObj() {
        return new Long(_subscriptionId);
    }

    public long getSubscriptionId() {
        return _subscriptionId;
    }

    public void setSubscriptionId(long subscriptionId) {
        if (subscriptionId != _subscriptionId) {
            _subscriptionId = subscriptionId;
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

    public long getUserId() {
        return _userId;
    }

    public void setUserId(long userId) {
        if (userId != _userId) {
            _userId = userId;
        }
    }

    public String getUserName() {
        return GetterUtil.getString(_userName);
    }

    public void setUserName(String userName) {
        if (((userName == null) && (_userName != null)) || ((userName != null) && (_userName == null)) || ((userName != null) && (_userName != null) && !userName.equals(_userName))) {
            if (!XSS_ALLOW_USERNAME) {
                userName = XSSUtil.strip(userName);
            }
            _userName = userName;
        }
    }

    public Date getCreateDate() {
        return _createDate;
    }

    public void setCreateDate(Date createDate) {
        if (((createDate == null) && (_createDate != null)) || ((createDate != null) && (_createDate == null)) || ((createDate != null) && (_createDate != null) && !createDate.equals(_createDate))) {
            _createDate = createDate;
        }
    }

    public Date getModifiedDate() {
        return _modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        if (((modifiedDate == null) && (_modifiedDate != null)) || ((modifiedDate != null) && (_modifiedDate == null)) || ((modifiedDate != null) && (_modifiedDate != null) && !modifiedDate.equals(_modifiedDate))) {
            _modifiedDate = modifiedDate;
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

    public String getFrequency() {
        return GetterUtil.getString(_frequency);
    }

    public void setFrequency(String frequency) {
        if (((frequency == null) && (_frequency != null)) || ((frequency != null) && (_frequency == null)) || ((frequency != null) && (_frequency != null) && !frequency.equals(_frequency))) {
            if (!XSS_ALLOW_FREQUENCY) {
                frequency = XSSUtil.strip(frequency);
            }
            _frequency = frequency;
        }
    }

    public Object clone() {
        SubscriptionImpl clone = new SubscriptionImpl();
        clone.setSubscriptionId(getSubscriptionId());
        clone.setCompanyId(getCompanyId());
        clone.setUserId(getUserId());
        clone.setUserName(getUserName());
        clone.setCreateDate(getCreateDate());
        clone.setModifiedDate(getModifiedDate());
        clone.setClassNameId(getClassNameId());
        clone.setClassPK(getClassPK());
        clone.setFrequency(getFrequency());
        return clone;
    }

    public int compareTo(Object obj) {
        if (obj == null) {
            return -1;
        }
        SubscriptionImpl subscription = (SubscriptionImpl) obj;
        long pk = subscription.getPrimaryKey();
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
        SubscriptionImpl subscription = null;
        try {
            subscription = (SubscriptionImpl) obj;
        } catch (ClassCastException cce) {
            return false;
        }
        long pk = subscription.getPrimaryKey();
        if (getPrimaryKey() == pk) {
            return true;
        } else {
            return false;
        }
    }

    public int hashCode() {
        return (int) getPrimaryKey();
    }

    private long _subscriptionId;

    private long _companyId;

    private long _userId;

    private String _userName;

    private Date _createDate;

    private Date _modifiedDate;

    private long _classNameId;

    private long _classPK;

    private String _frequency;
}
