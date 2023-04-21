package com.liferay.portal.model;

import java.util.Date;
import com.liferay.portal.util.PropsUtil;
import com.liferay.util.GetterUtil;
import com.liferay.util.Xss;

/**
 * <a href="PasswordTrackerModel.java.html"><b><i>View Source</i></b></a>
 *
 * @author  Brian Wing Shun Chan
 * @version $Revision: 1.11 $
 *
 */
public class PasswordTrackerModel extends BaseModel {

    public static boolean CACHEABLE = GetterUtil.get(PropsUtil.get("value.object.cacheable.com.liferay.portal.model.PasswordTracker"), VALUE_OBJECT_CACHEABLE);

    public static int MAX_SIZE = GetterUtil.get(PropsUtil.get("value.object.max.size.com.liferay.portal.model.PasswordTracker"), VALUE_OBJECT_MAX_SIZE);

    public static boolean XSS_ALLOW_BY_MODEL = GetterUtil.get(PropsUtil.get("xss.allow.com.liferay.portal.model.PasswordTracker"), XSS_ALLOW);

    public static boolean XSS_ALLOW_PASSWORDTRACKERID = GetterUtil.get(PropsUtil.get("xss.allow.com.liferay.portal.model.PasswordTracker.passwordTrackerId"), XSS_ALLOW_BY_MODEL);

    public static boolean XSS_ALLOW_USERID = GetterUtil.get(PropsUtil.get("xss.allow.com.liferay.portal.model.PasswordTracker.userId"), XSS_ALLOW_BY_MODEL);

    public static boolean XSS_ALLOW_PASSWORD = GetterUtil.get(PropsUtil.get("xss.allow.com.liferay.portal.model.PasswordTracker.password"), XSS_ALLOW_BY_MODEL);

    public static long LOCK_EXPIRATION_TIME = GetterUtil.getLong(PropsUtil.get("lock.expiration.time.com.liferay.portal.model.PasswordTrackerModel"));

    public PasswordTrackerModel() {
    }

    public PasswordTrackerModel(String passwordTrackerId) {
        _passwordTrackerId = passwordTrackerId;
        setNew(true);
    }

    public PasswordTrackerModel(String passwordTrackerId, String userId, Date createDate, String password) {
        _passwordTrackerId = passwordTrackerId;
        _userId = userId;
        _createDate = createDate;
        _password = password;
    }

    public String getPrimaryKey() {
        return _passwordTrackerId;
    }

    public String getPasswordTrackerId() {
        return _passwordTrackerId;
    }

    public void setPasswordTrackerId(String passwordTrackerId) {
        if (((passwordTrackerId == null) && (_passwordTrackerId != null)) || ((passwordTrackerId != null) && (_passwordTrackerId == null)) || ((passwordTrackerId != null) && (_passwordTrackerId != null) && !passwordTrackerId.equals(_passwordTrackerId))) {
            if (!XSS_ALLOW_PASSWORDTRACKERID) {
                passwordTrackerId = Xss.strip(passwordTrackerId);
            }
            _passwordTrackerId = passwordTrackerId;
            setModified(true);
        }
    }

    public String getUserId() {
        return _userId;
    }

    public void setUserId(String userId) {
        if (((userId == null) && (_userId != null)) || ((userId != null) && (_userId == null)) || ((userId != null) && (_userId != null) && !userId.equals(_userId))) {
            if (!XSS_ALLOW_USERID) {
                userId = Xss.strip(userId);
            }
            _userId = userId;
            setModified(true);
        }
    }

    public Date getCreateDate() {
        return _createDate;
    }

    public void setCreateDate(Date createDate) {
        if (((createDate == null) && (_createDate != null)) || ((createDate != null) && (_createDate == null)) || ((createDate != null) && (_createDate != null) && !createDate.equals(_createDate))) {
            _createDate = createDate;
            setModified(true);
        }
    }

    public String getPassword() {
        return _password;
    }

    public void setPassword(String password) {
        if (((password == null) && (_password != null)) || ((password != null) && (_password == null)) || ((password != null) && (_password != null) && !password.equals(_password))) {
            if (!XSS_ALLOW_PASSWORD) {
                password = Xss.strip(password);
            }
            _password = password;
            setModified(true);
        }
    }

    public BaseModel getProtected() {
        return null;
    }

    public void protect() {
    }

    public Object clone() {
        return new PasswordTracker(getPasswordTrackerId(), getUserId(), getCreateDate(), getPassword());
    }

    public int compareTo(Object obj) {
        if (obj == null) {
            return -1;
        }
        PasswordTracker passwordTracker = (PasswordTracker) obj;
        int value = 0;
        value = getUserId().compareTo(passwordTracker.getUserId());
        value = value * -1;
        if (value != 0) {
            return value;
        }
        value = getCreateDate().compareTo(passwordTracker.getCreateDate());
        value = value * -1;
        if (value != 0) {
            return value;
        }
        return 0;
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        PasswordTracker passwordTracker = null;
        try {
            passwordTracker = (PasswordTracker) obj;
        } catch (ClassCastException cce) {
            return false;
        }
        String pk = passwordTracker.getPrimaryKey();
        if (getPrimaryKey().equals(pk)) {
            return true;
        } else {
            return false;
        }
    }

    public int hashCode() {
        return getPrimaryKey().hashCode();
    }

    private String _passwordTrackerId;

    private String _userId;

    private Date _createDate;

    private String _password;
}
