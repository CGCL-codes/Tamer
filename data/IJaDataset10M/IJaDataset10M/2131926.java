package com.coyousoft.wangyu.daoimpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.sysolar.sun.config.AppContext;
import org.sysolar.sun.jdbc.core.JdbcOperations;
import com.coyousoft.wangyu.constant.Sql;
import com.coyousoft.wangyu.dao.NoticeCenterDao;
import com.coyousoft.wangyu.entity.NoticeCenter;

public final class NoticeCenterDaoImpl implements NoticeCenterDao {

    private final JdbcOperations dao = AppContext.getJdbcOperations(NoticeCenterDao.class);

    @Override
    public void create(NoticeCenter noticeCenter) throws Exception {
        this.create(noticeCenter, false);
    }

    @Override
    public void create(NoticeCenter noticeCenter, boolean setId) throws Exception {
        dao.update(Sql.NoticeCenterSql.create, noticeCenter.setDefault().toArray());
        if (setId) {
            noticeCenter.setNoticeId(dao.queryForInt(Sql.GenericSql.fetchLastInsertId));
        }
    }

    @Override
    public void create(List<NoticeCenter> noticeCenterList) throws Exception {
        if (null == noticeCenterList || noticeCenterList.size() == 0) {
            return;
        }
        List<Object[]> params = new ArrayList<Object[]>(noticeCenterList.size());
        for (NoticeCenter noticeCenter : noticeCenterList) {
            params.add(noticeCenter.setDefault().toArray());
        }
        dao.update(Sql.NoticeCenterSql.create, params);
    }

    @Override
    public int remove(Integer noticeId) throws Exception {
        return dao.update(Sql.NoticeCenterSql.remove, noticeId);
    }

    @Override
    public int update(NoticeCenter noticeCenter) throws Exception {
        List<Object> params = new ArrayList<Object>(10);
        StringBuilder buffer = new StringBuilder(512);
        buffer.append("update NOTICE_CENTER set ");
        if (null != noticeCenter.getUserId()) {
            buffer.append("USER_ID=?,");
            params.add(noticeCenter.getUserId());
        }
        if (null != noticeCenter.getNoticeType()) {
            buffer.append("NOTICE_TYPE=?,");
            params.add(noticeCenter.getNoticeType());
        }
        if (null != noticeCenter.getNoticeStatus()) {
            buffer.append("NOTICE_STATUS=?,");
            params.add(noticeCenter.getNoticeStatus());
        }
        if (null != noticeCenter.getNoticeCdate()) {
            buffer.append("NOTICE_CDATE=?,");
            params.add(noticeCenter.getNoticeCdate());
        }
        buffer.deleteCharAt(buffer.length() - 1);
        buffer.append(" where NOTICE_ID = ?");
        params.add(noticeCenter.getNoticeId());
        return dao.update(buffer.toString(), params.toArray());
    }

    @Override
    public NoticeCenter fetch(Integer noticeId) throws Exception {
        for (Map<String, Object> row : dao.queryForList(Sql.NoticeCenterSql.fetch, noticeId)) {
            return new NoticeCenter().fill(row);
        }
        return null;
    }

    @Override
    public List<NoticeCenter> export(int offset, int limit) throws Exception {
        List<NoticeCenter> list = new ArrayList<NoticeCenter>(limit);
        for (Map<String, Object> row : dao.queryForList(Sql.NoticeCenterSql.export, offset, limit)) {
            list.add(new NoticeCenter().fill(row));
        }
        return list;
    }

    @Override
    public int fetchNoticeCount(NoticeCenter notice) throws Exception {
        return dao.queryForInt(Sql.NoticeCenterSql.fetchNoticeCount, notice.getUserId(), notice.getNoticeType());
    }

    @Override
    public int removeByType(int noticeType, Integer userId) throws Exception {
        return dao.update(Sql.NoticeCenterSql.removeByType, noticeType, userId);
    }
}
