package com.yubuild.coreman.business.impl;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.springframework.dao.DataIntegrityViolationException;
import com.yubuild.coreman.business.UserManager;
import com.yubuild.coreman.business.exception.UserExistsException;
import com.yubuild.coreman.common.SearchData;
import com.yubuild.coreman.common.util.OrderBy;
import com.yubuild.coreman.common.util.RandomGUID;
import com.yubuild.coreman.common.util.StringUtil;
import com.yubuild.coreman.dao.ActivityDAO;
import com.yubuild.coreman.dao.ActivityLogDAO;
import com.yubuild.coreman.dao.UserDAO;
import com.yubuild.coreman.data.ActivityLog;
import com.yubuild.coreman.data.User;
import com.yubuild.coreman.data.UserCookie;
import com.yubuild.coreman.data.searchable.UserSearchable;

public class UserManagerImpl extends BaseManager implements UserManager {

    private UserDAO dao;

    private ActivityLogDAO activityLogDAO;

    public void setActivityLogDAO(ActivityLogDAO activityLogDAO) {
        this.activityLogDAO = activityLogDAO;
    }

    public void setUserDAO(UserDAO dao) {
        this.dao = dao;
    }

    public User getUser(String username) {
        return dao.getUserByUsername(username);
    }

    public User getUser(Long id) {
        return dao.getUser(id);
    }

    public SearchData getUsers(UserSearchable user) {
        SearchData searchData = new SearchData();
        searchData.setResults((List) dao.getUsers(user));
        searchData.setResultsCount((Integer) dao.getUserCount(user));
        return searchData;
    }

    public SearchData getUsers(SearchData searchData) {
        searchData.setResults((List) dao.getUsers(searchData));
        searchData.setResultsCount((Integer) dao.getUserCount((UserSearchable) searchData.getSearchCondition()));
        return searchData;
    }

    public void saveUser(User user, Long userDoItId) throws UserExistsException {
        try {
            dao.saveUser(user);
        } catch (DataIntegrityViolationException e) {
            throw new UserExistsException("User '" + user.getUsername() + "' already exists!");
        }
        ActivityLog log = new ActivityLog();
        log.setActivityId(user.getId() == null ? ActivityDAO.ADD_USER : ActivityDAO.UPDATE_USER);
        log.setUserId(userDoItId);
        log.setActivityData(user.getActivityString());
        activityLogDAO.saveActivityLog(log);
    }

    public void removeUser(String username, Long userDoItId) {
        if (this.log.isDebugEnabled()) this.log.debug("removing user: " + username);
        User user = dao.getUserByUsername(username);
        if (user != null) {
            ActivityLog log = new ActivityLog();
            log.setActivityId(ActivityDAO.DELETE_USER);
            log.setUserId(userDoItId);
            log.setActivityData(user.getActivityString());
            activityLogDAO.saveActivityLog(log);
        }
        dao.removeUser(username);
    }

    public String checkLoginCookie(String value) {
        value = StringUtil.decodeString(value);
        String values[] = StringUtils.split(value, "|");
        if (values.length == 1) return null;
        if (log.isDebugEnabled()) log.debug("looking up cookieId: " + values[1]);
        UserCookie cookie = new UserCookie();
        cookie.setUsername(values[0]);
        cookie.setCookieId(values[1]);
        cookie = dao.getUserCookie(cookie);
        if (cookie != null) {
            if (log.isDebugEnabled()) log.debug("cookieId lookup succeeded, generating new cookieId");
            return saveLoginCookie(cookie);
        }
        if (log.isDebugEnabled()) log.debug("cookieId lookup failed, returning null");
        return null;
    }

    public String createLoginCookie(String username) {
        UserCookie cookie = new UserCookie();
        cookie.setUsername(username);
        return saveLoginCookie(cookie);
    }

    private String saveLoginCookie(UserCookie cookie) {
        cookie.setCookieId((new RandomGUID()).toString());
        dao.saveUserCookie(cookie);
        return StringUtil.encodeString(cookie.getUsername() + "|" + cookie.getCookieId());
    }

    public void removeLoginCookies(String username) {
        dao.removeUserCookies(username);
    }

    public List getUserList() {
        UserSearchable userSearch = new UserSearchable();
        userSearch.setEnabled(new Boolean(true));
        OrderBy orderBy = new OrderBy();
        orderBy.setAscDecs("ACS");
        orderBy.setColumnName("firstName");
        ArrayList al = new ArrayList();
        al.add(orderBy);
        userSearch.setOrderBy(al);
        List users = (List) dao.getUsers(userSearch);
        return users;
    }
}
