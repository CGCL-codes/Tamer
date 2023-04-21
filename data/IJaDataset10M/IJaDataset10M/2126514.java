package com.dotmarketing.factories;

import com.dotmarketing.beans.UserProxy;
import com.dotmarketing.db.DotHibernate;
import com.dotmarketing.util.Logger;
import com.liferay.portal.model.User;

/**
 * 
 * @author David Torres
 */
public class UserProxyFactory {

    public static UserProxy getUserProxy(String userId) {
        if (userId == null) {
            return null;
        }
        UserProxy up = null;
        try {
            DotHibernate dh = new DotHibernate(UserProxy.class);
            dh.setQuery("from user_proxy in class com.dotmarketing.beans.UserProxy where user_id = ?");
            dh.setParam(userId);
            up = (UserProxy) dh.load();
        } catch (Exception e) {
            Logger.warn(UserProxyFactory.class, "getUserProxy failed:" + e, e);
        }
        if (up.getInode() == 0) {
            up.setUserId(userId);
            InodeFactory.saveInode(up);
        }
        return up;
    }

    public static UserProxy getUserProxy(User user) {
        return getUserProxy(user.getUserId());
    }

    public static UserProxy getUserProxy(long userProxyInode) {
        return (UserProxy) InodeFactory.getInode(userProxyInode, UserProxy.class);
    }

    public static UserProxy getUserProxyByLongLiveCookie(String dotCMSID) {
        if (dotCMSID == null) {
            return null;
        }
        UserProxy up = null;
        try {
            DotHibernate dh = new DotHibernate(UserProxy.class);
            dh.setQuery("from user_proxy in class com.dotmarketing.beans.UserProxy where lower(long_lived_cookie) = lower(?)");
            dh.setParam(dotCMSID);
            up = (UserProxy) dh.load();
        } catch (Exception e) {
            Logger.warn(UserProxyFactory.class, "getUserProxyByLongLiveCookie failed:" + e, e);
        }
        return up;
    }

    public static void saveUserProxy(UserProxy userProxy) {
        InodeFactory.saveInode(userProxy);
    }
}
