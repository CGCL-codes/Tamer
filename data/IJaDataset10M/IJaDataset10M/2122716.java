package com.mobfee.business.dao.impl;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import com.mobfee.business.dao.IAccountDao;
import com.mobfee.business.dao.LogonFailedException;
import com.mobfee.business.dao.AddUserFailedException;
import com.mobfee.domain.UserProfile;

public class AccountDao extends HibernateDaoSupport implements IAccountDao {

    private ResourceBundleMessageSource messageSource;

    public void setMessageSource(ResourceBundleMessageSource messageSource) {
        this.messageSource = messageSource;
    }

    private String encode(String str) {
        StringBuffer buf = new StringBuffer();
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(str.getBytes());
            byte bytes[] = md5.digest();
            for (int i = 0; i < bytes.length; i++) {
                String s = Integer.toHexString(bytes[i] & 0xff);
                if (s.length() == 1) {
                    buf.append("0");
                }
                buf.append(s);
            }
        } catch (Exception ex) {
        }
        return buf.toString();
    }

    @Override
    public UserProfile logon(String username, String password) throws LogonFailedException {
        List userList = getHibernateTemplate().find("from UserProfile as up where up.username=?", username);
        if (userList.size() > 0) {
            com.mobfee.dao.hibernate.UserProfile up = (com.mobfee.dao.hibernate.UserProfile) userList.get(0);
            if (up.getPassword().equals(encode(password))) {
                return up.createDomainObject();
            }
            throw new LogonFailedException(messageSource.getMessage("EXC-001", null, Locale.getDefault()));
        } else {
            throw new LogonFailedException(messageSource.getMessage("EXC-000", new String[] { username }, Locale.getDefault()));
        }
    }

    @Override
    public Long addUser(UserProfile user) throws AddUserFailedException {
        if (user.getUsername().isEmpty() || user.getPassword().isEmpty()) {
            throw new AddUserFailedException(messageSource.getMessage("EXC-003", null, Locale.getDefault()));
        }
        List userList = getHibernateTemplate().find("from UserProfile as up where up.username=?", user.getUsername());
        if (userList.size() > 0) {
            throw new AddUserFailedException(messageSource.getMessage("EXC-002", new String[] { user.getUsername() }, Locale.getDefault()));
        }
        String password = encode(user.getPassword());
        user.setType(1);
        user.setPoints(0);
        user.setRegdate(new Date());
        user.setPassword(password);
        return (Long) getHibernateTemplate().save(user.createHibernateObject());
    }

    @Override
    public UserProfile getUser(String username) {
        List userList = getHibernateTemplate().find("from UserProfile as user where user.username = ?", username);
        if (userList.size() > 0) {
            com.mobfee.dao.hibernate.UserProfile user = (com.mobfee.dao.hibernate.UserProfile) userList.get(0);
            return user.createDomainObject();
        }
        return null;
    }

    @Override
    public UserProfile getUser(long userId) {
        List userList = getHibernateTemplate().find("from UserProfile as user where user.userId = ?", userId);
        if (userList.size() > 0) {
            com.mobfee.dao.hibernate.UserProfile user = (com.mobfee.dao.hibernate.UserProfile) userList.get(0);
            return user.createDomainObject();
        }
        return null;
    }

    @Override
    public void updateUser(UserProfile user) {
        com.mobfee.dao.hibernate.UserProfile hUser = (com.mobfee.dao.hibernate.UserProfile) getHibernateTemplate().get(com.mobfee.dao.hibernate.UserProfile.class, user.getUserId());
        if (hUser != null) {
            hUser.setUsername(user.getUsername());
            hUser.setPassword(encode(user.getPassword()));
            getHibernateTemplate().update(hUser);
        }
    }

    @Override
    public boolean checkUsername(String username) {
        List userList = getHibernateTemplate().find("from UserProfile as up where up.username=?", username);
        if (userList.size() > 0) {
            return true;
        } else return false;
    }
}
