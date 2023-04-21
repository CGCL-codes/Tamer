package com.coyousoft.wangyu.daoimpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.sysolar.sun.config.AppContext;
import org.sysolar.sun.jdbc.core.JdbcOperations;
import org.sysolar.sun.mvc.support.Pager;
import com.coyousoft.wangyu.constant.Sql;
import com.coyousoft.wangyu.dao.UserVisitLogDao;
import com.coyousoft.wangyu.entity.UserVisitLog;

public final class UserVisitLogDaoImpl implements UserVisitLogDao {

    private final JdbcOperations dao = AppContext.getJdbcOperations(UserVisitLogDao.class);

    @Override
    public void create(UserVisitLog userVisitLog) throws Exception {
        this.create(userVisitLog, false);
    }

    @Override
    public void create(UserVisitLog userVisitLog, boolean setId) throws Exception {
        dao.update(Sql.UserVisitLogSql.create, userVisitLog.setDefault().toArray());
        if (setId) {
            userVisitLog.setLogId(dao.queryForLong(Sql.GenericSql.fetchLastInsertId));
        }
    }

    @Override
    public void create(List<UserVisitLog> userVisitLogList) throws Exception {
        if (null == userVisitLogList || userVisitLogList.size() == 0) {
            return;
        }
        List<Object[]> params = new ArrayList<Object[]>(userVisitLogList.size());
        for (UserVisitLog userVisitLog : userVisitLogList) {
            params.add(userVisitLog.setDefault().toArray());
        }
        dao.update(Sql.UserVisitLogSql.create, params);
    }

    @Override
    public int remove(Long logId) throws Exception {
        return dao.update(Sql.UserVisitLogSql.remove, logId);
    }

    @Override
    public int update(UserVisitLog userVisitLog) throws Exception {
        List<Object> params = new ArrayList<Object>(10);
        StringBuilder buffer = new StringBuilder(512);
        buffer.append("update USER_VISIT_LOG set ");
        if (null != userVisitLog.getFromUserId()) {
            buffer.append("FROM_USER_ID=?,");
            params.add(userVisitLog.getFromUserId());
        }
        if (null != userVisitLog.getToUserId()) {
            buffer.append("TO_USER_ID=?,");
            params.add(userVisitLog.getToUserId());
        }
        if (null != userVisitLog.getVisitNum()) {
            buffer.append("VISIT_NUM=?,");
            params.add(userVisitLog.getVisitNum());
        }
        if (null != userVisitLog.getFirstVisitDate()) {
            buffer.append("FIRST_VISIT_DATE=?,");
            params.add(userVisitLog.getFirstVisitDate());
        }
        if (null != userVisitLog.getLastVisitDate()) {
            buffer.append("LAST_VISIT_DATE=?,");
            params.add(userVisitLog.getLastVisitDate());
        }
        buffer.deleteCharAt(buffer.length() - 1);
        buffer.append(" where LOG_ID = ?");
        params.add(userVisitLog.getLogId());
        return dao.update(buffer.toString(), params.toArray());
    }

    @Override
    public UserVisitLog fetch(Long logId) throws Exception {
        for (Map<String, Object> row : dao.queryForList(Sql.UserVisitLogSql.fetch, logId)) {
            return new UserVisitLog().fill(row);
        }
        return null;
    }

    @Override
    public List<UserVisitLog> export(int offset, int limit) throws Exception {
        List<UserVisitLog> list = new ArrayList<UserVisitLog>(limit);
        for (Map<String, Object> row : dao.queryForList(Sql.UserVisitLogSql.export, offset, limit)) {
            list.add(new UserVisitLog().fill(row));
        }
        return list;
    }

    @Override
    public List<UserVisitLog> fetchByToUserId(Integer userId, Pager pager) throws Exception {
        List<UserVisitLog> list = new ArrayList<UserVisitLog>(pager.getPageSize());
        for (Map<String, Object> row : dao.queryForList(Sql.UserVisitLogSql.fetchByToUserId, userId, pager.getOffset(), pager.getPageSize())) {
            list.add(new UserVisitLog().fill(row, true));
        }
        return list;
    }

    @Override
    public int updateVisitNum(Integer fromUserId, Integer toUserId) throws Exception {
        return dao.update(Sql.UserVisitLogSql.updateVisitNum, new Date(), fromUserId, toUserId);
    }

    @Override
    public int fetchCountByToUserId(Integer userId) throws Exception {
        return dao.queryForInt(Sql.UserVisitLogSql.fetchCountByToUserId, userId);
    }

    @Override
    public List<UserVisitLog> fetchByFromUserId(Integer userId, Pager pager) throws Exception {
        List<UserVisitLog> list = new ArrayList<UserVisitLog>(pager.getPageSize());
        for (Map<String, Object> row : dao.queryForList(Sql.UserVisitLogSql.fetchByFromUserId, userId, pager.getOffset(), pager.getPageSize())) {
            list.add(new UserVisitLog().fill(row, true));
        }
        return list;
    }

    @Override
    public int fetchCountByFromUserId(Integer userId) throws Exception {
        return dao.queryForInt(Sql.UserVisitLogSql.fetchCountByFromUserId, userId);
    }
}
