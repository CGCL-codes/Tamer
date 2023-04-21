package com.wangyu001.daoimpl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.sysolar.sun.config.AppContext;
import org.sysolar.sun.jdbc.core.JdbcOperations;
import org.sysolar.sun.mvc.support.Pager;
import com.wangyu001.constant.Sql;
import com.wangyu001.constant.C.TopUserCondition;
import com.wangyu001.dao.WangyuUserDao;
import com.wangyu001.entity.WangyuUser;
import com.wangyu001.util.ListUtil;

public final class WangyuUserDaoImpl implements WangyuUserDao {

    private static JdbcOperations dao = AppContext.getJdbcOperations(WangyuUserDaoImpl.class);

    public Long create(WangyuUser wangyuUser, boolean returnId) throws SQLException {
        Date date = new Date();
        if (null == wangyuUser.getUserType()) {
            wangyuUser.setUserType(0);
        }
        if (null == wangyuUser.getUserPhotoUploaded()) {
            wangyuUser.setUserPhotoUploaded(0);
        }
        if (null == wangyuUser.getUrlCatNum()) {
            wangyuUser.setUrlCatNum(0);
        }
        if (null == wangyuUser.getUrlNum()) {
            wangyuUser.setUrlNum(0);
        }
        if (null == wangyuUser.getWebCatNum()) {
            wangyuUser.setWebCatNum(0);
        }
        if (null == wangyuUser.getWebNum()) {
            wangyuUser.setWebNum(0);
        }
        if (null == wangyuUser.getUserVisitedNum()) {
            wangyuUser.setUserVisitedNum(0L);
        }
        if (null == wangyuUser.getUserLoginNum()) {
            wangyuUser.setUserLoginNum(0L);
        }
        if (null == wangyuUser.getUserSkinId()) {
            wangyuUser.setUserSkinId(1L);
        }
        if (null == wangyuUser.getUserLastLoginDate()) {
            wangyuUser.setUserLastLoginDate(date);
        }
        if (null == wangyuUser.getUserCdate()) {
            wangyuUser.setUserCdate(date);
        }
        if (null == wangyuUser.getUserLastVisitDate()) {
            wangyuUser.setUserLastVisitDate(date);
        }
        dao.update(Sql.WangyuUserSql.create, wangyuUser.getUserId(), wangyuUser.getUserNickname(), wangyuUser.getUserType(), wangyuUser.getUserStatus(), wangyuUser.getUserPhotoUploaded(), wangyuUser.getUrlCatNum(), wangyuUser.getUrlNum(), wangyuUser.getWebCatNum(), wangyuUser.getWebNum(), wangyuUser.getUserVisitedNum(), wangyuUser.getUserLoginNum(), wangyuUser.getUserSkinId(), wangyuUser.getUserSign(), wangyuUser.getUserLastLoginDate(), wangyuUser.getUserCdate(), wangyuUser.getUserUdate(), wangyuUser.getUserLastVisitDate());
        if (returnId) {
            return dao.queryForLong(Sql.GenericSql.fetchLastInsertId);
        }
        return null;
    }

    public boolean remove(Long wangyuUserId) throws SQLException {
        return dao.update(Sql.WangyuUserSql.remove, wangyuUserId) > 0;
    }

    public WangyuUser fetch(Long wangyuUserId) throws SQLException {
        for (Map<String, Object> row : dao.queryForList(Sql.WangyuUserSql.fetch, wangyuUserId)) {
            return new WangyuUser().fill(row);
        }
        return null;
    }

    public Pager<WangyuUser> fetchTopList(Pager<WangyuUser> pager, int sex, int orderBy) throws SQLException {
        List<WangyuUser> wangyuUserList = new ArrayList<WangyuUser>(20);
        String sql = Sql.WangyuUserSql.fetchTopList;
        if (sex == TopUserCondition.SEX_ALL) {
            sql = sql.replace("where SEX = ?", "");
        }
        if (orderBy == TopUserCondition.ORDERBY_CDATE) {
            sql = sql.replace("USER_LAST_VISIT_DATE desc ,", "");
            sql = sql.replace(",URL_NUM desc ,WEB_NUM desc", "");
        } else if (orderBy == TopUserCondition.ORDERBY_DEFAULT) {
            sql = sql.replace(",USER_CDATE desc ,URL_NUM desc ,WEB_NUM desc", "");
        } else if (orderBy == TopUserCondition.ORDERBY_URLNUM) {
            sql = sql.replace("USER_LAST_VISIT_DATE desc ,USER_CDATE desc ,", "");
            sql = sql.replace(",WEB_NUM desc", "");
        } else if (orderBy == TopUserCondition.ORDERBY_WEBNUM) {
            sql = sql.replace("USER_LAST_VISIT_DATE desc ,USER_CDATE desc ,URL_NUM desc ,", "");
        }
        List<Map<String, Object>> queryResult = null;
        if (sex == TopUserCondition.SEX_ALL) {
            queryResult = dao.queryForList(sql, pager.getOffset(), pager.getPageSize());
        } else {
            queryResult = dao.queryForList(sql, sex, pager.getOffset(), pager.getPageSize());
        }
        for (Map<String, Object> row : queryResult) {
            wangyuUserList.add(new WangyuUser().fill(row));
        }
        return pager.fill(this.fetchTopCount(sex), wangyuUserList);
    }

    private int fetchTopCount(int sex) throws SQLException {
        int count = 0;
        String sql = Sql.WangyuUserSql.fetchTopCount;
        if (sex == TopUserCondition.SEX_ALL) {
            sql = sql.replace(" where SEX = ?", "");
            count = dao.queryForInt(sql);
        } else {
            count = dao.queryForInt(Sql.WangyuUserSql.fetchTopCount, sex);
        }
        return count;
    }

    public List<WangyuUser> fetchListForImg(int limit) throws SQLException {
        List<WangyuUser> wangyuUserList = new ArrayList<WangyuUser>(20);
        for (Map<String, Object> row : dao.queryForList(Sql.WangyuUserSql.fetchListForImg, limit)) {
            wangyuUserList.add(new WangyuUser().fill(row));
        }
        ListUtil.shuffle(wangyuUserList);
        while (wangyuUserList.size() > 0 && wangyuUserList.size() < limit) {
            int size = wangyuUserList.size();
            for (int i = 0; i < size && wangyuUserList.size() < limit; i++) {
                wangyuUserList.add(wangyuUserList.get(i));
            }
        }
        return wangyuUserList;
    }

    public boolean updateNickname(WangyuUser wangyuUser) throws SQLException {
        return dao.update(Sql.WangyuUserSql.updateNickname, wangyuUser.getUserNickname(), wangyuUser.getUserId()) > 0;
    }

    public boolean updateUserSign(WangyuUser wangyuUser) throws SQLException {
        return dao.update(Sql.WangyuUserSql.updateUserSign, wangyuUser.getUserSign(), wangyuUser.getUserId()) > 0;
    }

    public boolean updateUserPhoto(WangyuUser wangyuUser) throws SQLException {
        return dao.update(Sql.WangyuUserSql.updateUserPhoto, wangyuUser.getUserId()) > 0;
    }

    public boolean updateUserLoginNum(Long userId) throws SQLException {
        return dao.update(Sql.WangyuUserSql.updateUserLoginNum, new Date(), userId) > 0;
    }

    public boolean updateUserVisitedNum(Long userId) throws SQLException {
        return dao.update(Sql.WangyuUserSql.updateUserVisitedNum, userId) > 0;
    }

    public boolean updateUrlCatNum(Long userId) throws SQLException {
        return dao.update(Sql.WangyuUserSql.updateUrlCatNum, userId, userId) > 0;
    }

    public boolean updateUrlNum(Long userId) throws SQLException {
        return dao.update(Sql.WangyuUserSql.updateUrlNum, userId, userId) > 0;
    }

    @Override
    public int updateUrlCatNumAndUrlNum(Long userId) throws SQLException {
        return dao.update(Sql.WangyuUserSql.updateUrlCatNumAndUrlNum, userId, userId, userId);
    }

    public List<WangyuUser> export(int offset, int limit) throws SQLException {
        List<WangyuUser> list = new ArrayList<WangyuUser>(limit);
        for (Map<String, Object> row : dao.queryForList(Sql.WangyuUserSql.export, offset, limit)) {
            list.add(new WangyuUser().fill(row));
        }
        return list;
    }

    public void updateLastVisitDateById(WangyuUser wangyuUser) throws SQLException {
        dao.update(Sql.WangyuUserSql.updateLastVisitDateById, wangyuUser.getUserLastVisitDate(), wangyuUser.getUserId());
    }

    @Override
    public List<WangyuUser> fetchPopularUser(Date datePoint, int selfClickNum, int limit) throws SQLException {
        List<WangyuUser> list = new ArrayList<WangyuUser>(limit);
        for (Map<String, Object> row : dao.queryForList(Sql.WangyuUserSql.fetchPopularUser, datePoint, selfClickNum, limit)) {
            list.add(new WangyuUser().fill(row));
        }
        return list;
    }
}
