package com.coyousoft.wangyu.daoimpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.sysolar.sun.config.AppContext;
import org.sysolar.sun.jdbc.core.JdbcOperations;
import org.sysolar.sun.jdbc.support.Sqler;
import org.sysolar.sun.mvc.support.Pager;
import com.coyousoft.wangyu.constant.Sql;
import com.coyousoft.wangyu.dao.WangyuUserDao;
import com.coyousoft.wangyu.entity.WangyuUser;

public final class WangyuUserDaoImpl implements WangyuUserDao {

    private final JdbcOperations dao = AppContext.getJdbcOperations(WangyuUserDao.class);

    @Override
    public void create(WangyuUser wangyuUser) throws Exception {
        this.create(wangyuUser, false);
    }

    @Override
    public void create(WangyuUser wangyuUser, boolean setId) throws Exception {
        dao.update(Sql.WangyuUserSql.create, wangyuUser.setDefault().toArray());
        if (setId) {
            wangyuUser.setUserId(dao.queryForInt(Sql.GenericSql.fetchLastInsertId));
        }
    }

    @Override
    public void create(List<WangyuUser> wangyuUserList) throws Exception {
        if (null == wangyuUserList || wangyuUserList.size() == 0) {
            return;
        }
        List<Object[]> params = new ArrayList<Object[]>(wangyuUserList.size());
        for (WangyuUser wangyuUser : wangyuUserList) {
            params.add(wangyuUser.setDefault().toArray());
        }
        dao.update(Sql.WangyuUserSql.create, params);
    }

    @Override
    public int remove(Integer userId) throws Exception {
        return dao.update(Sql.WangyuUserSql.remove, userId);
    }

    @Override
    public int update(WangyuUser wangyuUser) throws Exception {
        List<Object> params = new ArrayList<Object>(10);
        StringBuilder buffer = new StringBuilder(512);
        buffer.append("update WANGYU_USER set ");
        if (null != wangyuUser.getUserNickname()) {
            buffer.append("USER_NICKNAME=?,");
            params.add(wangyuUser.getUserNickname());
        }
        if (null != wangyuUser.getUserSign()) {
            buffer.append("USER_SIGN=?,");
            params.add(wangyuUser.getUserSign());
        }
        if (null != wangyuUser.getUserType()) {
            buffer.append("USER_TYPE=?,");
            params.add(wangyuUser.getUserType());
        }
        if (null != wangyuUser.getLoginStatus()) {
            buffer.append("LOGIN_STATUS=?,");
            params.add(wangyuUser.getLoginStatus());
        }
        if (null != wangyuUser.getPhotoStatus()) {
            buffer.append("PHOTO_STATUS=?,");
            params.add(wangyuUser.getPhotoStatus());
        }
        if (null != wangyuUser.getUrlCatNum()) {
            buffer.append("URL_CAT_NUM=?,");
            params.add(wangyuUser.getUrlCatNum());
        }
        if (null != wangyuUser.getUrlNum()) {
            buffer.append("URL_NUM=?,");
            params.add(wangyuUser.getUrlNum());
        }
        if (null != wangyuUser.getWebCatNum()) {
            buffer.append("WEB_CAT_NUM=?,");
            params.add(wangyuUser.getWebCatNum());
        }
        if (null != wangyuUser.getWebNum()) {
            buffer.append("WEB_NUM=?,");
            params.add(wangyuUser.getWebNum());
        }
        if (null != wangyuUser.getLoginNum()) {
            buffer.append("LOGIN_NUM=?,");
            params.add(wangyuUser.getLoginNum());
        }
        if (null != wangyuUser.getVisitedNum()) {
            buffer.append("VISITED_NUM=?,");
            params.add(wangyuUser.getVisitedNum());
        }
        if (null != wangyuUser.getSkinId()) {
            buffer.append("SKIN_ID=?,");
            params.add(wangyuUser.getSkinId());
        }
        if (null != wangyuUser.getLastLoginDate()) {
            buffer.append("LAST_LOGIN_DATE=?,");
            params.add(wangyuUser.getLastLoginDate());
        }
        if (null != wangyuUser.getLastVisitDate()) {
            buffer.append("LAST_VISIT_DATE=?,");
            params.add(wangyuUser.getLastVisitDate());
        }
        if (null != wangyuUser.getUserCdate()) {
            buffer.append("USER_CDATE=?,");
            params.add(wangyuUser.getUserCdate());
        }
        if (null != wangyuUser.getUserUdate()) {
            buffer.append("USER_UDATE=?,");
            params.add(wangyuUser.getUserUdate());
        }
        buffer.deleteCharAt(buffer.length() - 1);
        buffer.append(" where USER_ID = ?");
        params.add(wangyuUser.getUserId());
        return dao.update(buffer.toString(), params.toArray());
    }

    @Override
    public WangyuUser fetch(Integer userId) throws Exception {
        for (Map<String, Object> row : dao.queryForList(Sql.WangyuUserSql.fetch, userId)) {
            return new WangyuUser().fill(row);
        }
        return null;
    }

    @Override
    public List<WangyuUser> export(int offset, int limit) throws Exception {
        List<WangyuUser> list = new ArrayList<WangyuUser>(limit);
        for (Map<String, Object> row : dao.queryForList(Sql.WangyuUserSql.export, offset, limit)) {
            list.add(new WangyuUser().fill(row));
        }
        return list;
    }

    public void updateUserLoginNum(Integer userId) throws Exception {
        dao.update(Sql.WangyuUserSql.updateUserLoginNum, new Date(), userId);
    }

    public void updateUrlCatNum(Integer userId) throws Exception {
        dao.update(Sql.WangyuUserSql.updateUrlCatNum, userId, userId);
    }

    public void updateUrlNum(Integer userId) throws Exception {
        dao.update(Sql.WangyuUserSql.updateUrlNum, userId, userId);
    }

    @Override
    public void updateUserVisitedNum(Integer userId) throws Exception {
        dao.update(Sql.WangyuUserSql.updateUserVisitedNum, userId);
    }

    @Override
    public void updateNickname(WangyuUser wangyuUser) throws Exception {
        dao.update(Sql.WangyuUserSql.updateNickname, wangyuUser.getUserNickname(), wangyuUser.getUserId());
    }

    @Override
    public void updateSign(WangyuUser wangyuUser) throws Exception {
        dao.update(Sql.WangyuUserSql.updateSign, wangyuUser.getUserSign(), wangyuUser.getUserId());
    }

    @Override
    public void updatePhotoStatus(WangyuUser wangyuUser) throws Exception {
        dao.update(Sql.WangyuUserSql.updatePhotoStatus, wangyuUser.getUserId());
    }

    @Override
    public List<WangyuUser> fetchPopularUser(int selfClickNum, int limit) throws Exception {
        List<WangyuUser> list = new ArrayList<WangyuUser>(limit);
        for (Map<String, Object> row : dao.queryForList(Sql.WangyuUserSql.fetchPopularUser, limit)) {
            list.add(new WangyuUser().fill(row));
        }
        return list;
    }

    @Override
    public List<WangyuUser> fetchTopUserList(Pager pager, String tag, int order) throws Exception {
        String sql = Sql.WangyuUserSql.fetchTopUserList;
        List<Map<String, Object>> result = null;
        if (tag == null) {
            sql = Sqler.remove(sql, 1, 4);
        }
        if (order == 0) {
            sql = Sqler.remove(sql, 3);
        } else if (order == 1) {
            sql = Sqler.remove(sql, 2);
        }
        if (tag == null) {
            result = dao.queryForList(sql, pager.getOffset(), pager.getPageSize());
        } else {
            tag = tag + "%";
            result = dao.queryForList(sql, tag, tag, pager.getOffset(), pager.getPageSize());
        }
        List<WangyuUser> list = new ArrayList<WangyuUser>(pager.getPageSize());
        for (Map<String, Object> row : result) {
            list.add(new WangyuUser().fill(row));
        }
        return list;
    }

    @Override
    public int fetchTopUserCount(String tag) throws Exception {
        int result = 0;
        String sql = Sql.WangyuUserSql.fetchTopUserCount;
        if (tag == null) {
            sql = Sqler.remove(sql, 1, 4);
            result = dao.queryForInt(sql);
        } else {
            tag = tag + "%";
            result = dao.queryForInt(sql, tag, tag);
        }
        return result;
    }

    @Override
    public WangyuUser fetchByUserName(String userName) throws Exception {
        for (Map<String, Object> row : dao.queryForList(Sql.WangyuUserSql.fetchByUserName, userName)) {
            return new WangyuUser().fill(row, true, true);
        }
        return null;
    }

    @Override
    public void updateUrlCatNum$UrlNum(Integer userId) throws Exception {
        dao.update(Sql.WangyuUserSql.updateUrlCatNum$UrlNum, userId, userId, userId);
    }

    @Override
    public WangyuUser fetchByUserId(Integer userId) throws Exception {
        for (Map<String, Object> row : dao.queryForList(Sql.WangyuUserSql.fetchByUserId, userId)) {
            return new WangyuUser().fill(row, true, true);
        }
        return null;
    }

    @Override
    public List<WangyuUser> fetchActivityUserList(Pager pager) throws Exception {
        List<WangyuUser> list = new ArrayList<WangyuUser>(pager.getPageSize());
        for (Map<String, Object> row : dao.queryForList(Sql.WangyuUserSql.fetchActivityUserList, pager.getOffset(), pager.getPageSize())) {
            list.add(new WangyuUser().fill(row));
        }
        return list;
    }

    @Override
    public List<WangyuUser> fetchNewUserList(Pager pager) throws Exception {
        List<WangyuUser> list = new ArrayList<WangyuUser>(pager.getPageSize());
        for (Map<String, Object> row : dao.queryForList(Sql.WangyuUserSql.fetchNewUserList, pager.getOffset(), pager.getPageSize())) {
            list.add(new WangyuUser().fill(row));
        }
        return list;
    }

    @Override
    public void updateLastVisitDate(Integer userId) throws Exception {
        dao.update(Sql.WangyuUserSql.updateLastVisitDate, new Date(), userId);
    }

    @Override
    public void updateSkin(WangyuUser wangyuUser) throws Exception {
        dao.update(Sql.WangyuUserSql.updateSkin, wangyuUser.getSkinId(), wangyuUser.getUserId());
    }
}
