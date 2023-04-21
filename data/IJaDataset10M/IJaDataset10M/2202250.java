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
import com.coyousoft.wangyu.dao.CoreUrlDao;
import com.coyousoft.wangyu.entity.CoreUrl;
import com.coyousoft.wangyu.entity.Domain;
import com.coyousoft.wangyu.entity.WangyuUser;

public final class CoreUrlDaoImpl implements CoreUrlDao {

    private final JdbcOperations dao = AppContext.getJdbcOperations(CoreUrlDao.class);

    @Override
    public void create(CoreUrl coreUrl) throws Exception {
        this.create(coreUrl, false);
    }

    @Override
    public void create(CoreUrl coreUrl, boolean setId) throws Exception {
        dao.update(Sql.CoreUrlSql.create, coreUrl.setDefault().toArray());
        if (setId) {
            coreUrl.setCoreUrlId(dao.queryForInt(Sql.GenericSql.fetchLastInsertId));
        }
    }

    @Override
    public void create(List<CoreUrl> coreUrlList) throws Exception {
        if (null == coreUrlList || coreUrlList.size() == 0) {
            return;
        }
        List<Object[]> params = new ArrayList<Object[]>(coreUrlList.size());
        for (CoreUrl coreUrl : coreUrlList) {
            params.add(coreUrl.setDefault().toArray());
        }
        dao.update(Sql.CoreUrlSql.create, params);
    }

    @Override
    public int remove(Integer coreUrlId) throws Exception {
        return dao.update(Sql.CoreUrlSql.remove, coreUrlId);
    }

    @Override
    public int update(CoreUrl coreUrl) throws Exception {
        List<Object> params = new ArrayList<Object>(10);
        StringBuilder buffer = new StringBuilder(512);
        buffer.append("update CORE_URL set ");
        if (null != coreUrl.getDomainId()) {
            buffer.append("DOMAIN_ID=?,");
            params.add(coreUrl.getDomainId());
        }
        if (null != coreUrl.getCoreUrlName()) {
            buffer.append("CORE_URL_NAME=?,");
            params.add(coreUrl.getCoreUrlName());
        }
        if (null != coreUrl.getCoreUrlHref()) {
            buffer.append("CORE_URL_HREF=?,");
            params.add(coreUrl.getCoreUrlHref());
        }
        if (null != coreUrl.getCoreUrlDesc()) {
            buffer.append("CORE_URL_DESC=?,");
            params.add(coreUrl.getCoreUrlDesc());
        }
        if (null != coreUrl.getCoreUrlStatus()) {
            buffer.append("CORE_URL_STATUS=?,");
            params.add(coreUrl.getCoreUrlStatus());
        }
        if (null != coreUrl.getCoreUrlStoreNum()) {
            buffer.append("CORE_URL_STORE_NUM=?,");
            params.add(coreUrl.getCoreUrlStoreNum());
        }
        if (null != coreUrl.getCoreUrlShareNum()) {
            buffer.append("CORE_URL_SHARE_NUM=?,");
            params.add(coreUrl.getCoreUrlShareNum());
        }
        if (null != coreUrl.getCoreUrlPostNum()) {
            buffer.append("CORE_URL_POST_NUM=?,");
            params.add(coreUrl.getCoreUrlPostNum());
        }
        if (null != coreUrl.getCoreUrlClickNum()) {
            buffer.append("CORE_URL_CLICK_NUM=?,");
            params.add(coreUrl.getCoreUrlClickNum());
        }
        if (null != coreUrl.getCoreUrlDigNum()) {
            buffer.append("CORE_URL_DIG_NUM=?,");
            params.add(coreUrl.getCoreUrlDigNum());
        }
        if (null != coreUrl.getCoreUrlScore()) {
            buffer.append("CORE_URL_SCORE=?,");
            params.add(coreUrl.getCoreUrlScore());
        }
        if (null != coreUrl.getCoreUrlHrefType()) {
            buffer.append("CORE_URL_HREF_TYPE=?,");
            params.add(coreUrl.getCoreUrlHrefType());
        }
        if (null != coreUrl.getCoreUrlImgType()) {
            buffer.append("CORE_URL_IMG_TYPE=?,");
            params.add(coreUrl.getCoreUrlImgType());
        }
        if (null != coreUrl.getFirstStoreUserId()) {
            buffer.append("FIRST_STORE_USER_ID=?,");
            params.add(coreUrl.getFirstStoreUserId());
        }
        if (null != coreUrl.getFirstStoreDate()) {
            buffer.append("FIRST_STORE_DATE=?,");
            params.add(coreUrl.getFirstStoreDate());
        }
        if (null != coreUrl.getLastStoreUserId()) {
            buffer.append("LAST_STORE_USER_ID=?,");
            params.add(coreUrl.getLastStoreUserId());
        }
        if (null != coreUrl.getLastStoreDate()) {
            buffer.append("LAST_STORE_DATE=?,");
            params.add(coreUrl.getLastStoreDate());
        }
        if (null != coreUrl.getFirstShareUserId()) {
            buffer.append("FIRST_SHARE_USER_ID=?,");
            params.add(coreUrl.getFirstShareUserId());
        }
        if (null != coreUrl.getFirstShareDate()) {
            buffer.append("FIRST_SHARE_DATE=?,");
            params.add(coreUrl.getFirstShareDate());
        }
        if (null != coreUrl.getLastShareDate()) {
            buffer.append("LAST_SHARE_DATE=?,");
            params.add(coreUrl.getLastShareDate());
        }
        if (null != coreUrl.getLastShareUserId()) {
            buffer.append("LAST_SHARE_USER_ID=?,");
            params.add(coreUrl.getLastShareUserId());
        }
        if (null != coreUrl.getCoreUrlCdate()) {
            buffer.append("CORE_URL_CDATE=?,");
            params.add(coreUrl.getCoreUrlCdate());
        }
        if (null != coreUrl.getCoreUrlUdate()) {
            buffer.append("CORE_URL_UDATE=?,");
            params.add(coreUrl.getCoreUrlUdate());
        }
        buffer.deleteCharAt(buffer.length() - 1);
        buffer.append(" where CORE_URL_ID = ?");
        params.add(coreUrl.getCoreUrlId());
        return dao.update(buffer.toString(), params.toArray());
    }

    @Override
    public CoreUrl fetch(Integer coreUrlId) throws Exception {
        for (Map<String, Object> row : dao.queryForList(Sql.CoreUrlSql.fetch, coreUrlId)) {
            return new CoreUrl().fill(row);
        }
        return null;
    }

    @Override
    public List<CoreUrl> export(int offset, int limit) throws Exception {
        List<CoreUrl> list = new ArrayList<CoreUrl>(limit);
        for (Map<String, Object> row : dao.queryForList(Sql.CoreUrlSql.export, offset, limit)) {
            list.add(new CoreUrl().fill(row));
        }
        return list;
    }

    @Override
    public int updateClickNum(Integer count, Integer coreUrlId) throws Exception {
        return dao.update(Sql.CoreUrlSql.updateClickNum, count, coreUrlId);
    }

    @Override
    public CoreUrl fetchByHref(String coreUrlHref) throws Exception {
        for (Map<String, Object> row : dao.queryForList(Sql.CoreUrlSql.fetchByHref, coreUrlHref)) {
            CoreUrl coreUrl = new CoreUrl().fill(row);
            Domain domain = new Domain().fill(row);
            coreUrl.setDomain(domain);
            return coreUrl;
        }
        return null;
    }

    @Override
    public List<CoreUrl> fetchListByInfoStatus(int status, int limit) throws Exception {
        List<CoreUrl> list = new ArrayList<CoreUrl>(limit);
        for (Map<String, Object> row : dao.queryForList(Sql.CoreUrlSql.fetchListByInfoStatus, status, limit)) {
            list.add(new CoreUrl().fill(row));
        }
        return list;
    }

    @Override
    public void updateCoreUrlInfoById(CoreUrl coreUrl) throws Exception {
        dao.update(Sql.CoreUrlSql.updateCoreUrlInfoById, coreUrl.getCoreUrlName(), coreUrl.getCoreUrlDesc(), coreUrl.getCoreUrlId(), coreUrl.getCoreUrlInfoStatus(), new Date(), coreUrl.getCoreUrlId());
    }

    @Override
    public List<CoreUrl> fetchList4Image(int limit) throws Exception {
        List<CoreUrl> list = new ArrayList<CoreUrl>(limit);
        for (Map<String, Object> row : dao.queryForList(Sql.CoreUrlSql.fetchList4Image, limit)) {
            list.add(new CoreUrl().fill(row));
        }
        return list;
    }

    @Override
    public void updateCoreUrlImgTypeById(Integer coreUrlId, int imgType) throws Exception {
        dao.update(Sql.CoreUrlSql.updateCoreUrlImgTypeById, imgType, coreUrlId);
    }

    @Override
    public List<CoreUrl> fetchHotUrlList(Pager pager, String tag, int order) throws Exception {
        String sql = Sql.CoreUrlSql.fetchHotUrlList;
        List<Map<String, Object>> result = null;
        if (tag == null || "".equals(tag)) {
            sql = Sqler.remove(sql, 1, 5);
        }
        if (order == 1) {
            sql = Sqler.remove(sql, 3, 4);
        } else if (order == 2) {
            sql = Sqler.remove(sql, 2, 4);
        } else if (order == 3) {
            sql = Sqler.remove(sql, 2, 3);
        }
        if (tag == null || "".equals(tag)) {
            result = dao.queryForList(sql, pager.getOffset(), pager.getPageSize());
        } else {
            result = dao.queryForList(sql, tag + "%", pager.getOffset(), pager.getPageSize());
        }
        List<CoreUrl> list = new ArrayList<CoreUrl>(pager.getPageSize());
        for (Map<String, Object> row : result) {
            CoreUrl coreUrl = new CoreUrl().fill(row);
            Domain domain = new Domain().fill(row);
            WangyuUser firstStoreUser = new WangyuUser().fill(row);
            coreUrl.setWangyuUser(firstStoreUser);
            coreUrl.setDomain(domain);
            list.add(coreUrl);
        }
        return list;
    }

    @Override
    public int fetchHotUrlCount(String tag) throws Exception {
        int result = 0;
        String sql = Sql.CoreUrlSql.fetchHotUrlCount;
        if (tag == null || "".equals(tag)) {
            sql = Sqler.remove(sql, 1, 2);
            result = dao.queryForInt(sql);
        } else {
            result = dao.queryForInt(sql, tag + "%");
        }
        return result;
    }

    @Override
    public void updateFirstStore(CoreUrl coreUrl) throws Exception {
        dao.update(Sql.CoreUrlSql.updateFirstStore, coreUrl.getFirstStoreUserId(), coreUrl.getFirstStoreDate(), coreUrl.getLastStoreUserId(), coreUrl.getLastStoreDate(), coreUrl.getCoreUrlId());
    }

    @Override
    public void updateLastStore(CoreUrl coreUrl) throws Exception {
        dao.update(Sql.CoreUrlSql.updateLastStore, coreUrl.getLastStoreUserId(), coreUrl.getLastStoreDate(), coreUrl.getCoreUrlId());
    }

    @Override
    public void updateName$Des(CoreUrl coreUrl) throws Exception {
        dao.update(Sql.CoreUrlSql.updateName$Des, coreUrl.getCoreUrlName(), coreUrl.getCoreUrlDesc(), CoreUrl.INFO_STATUS_ADMIN_EDITED, new Date(), coreUrl.getCoreUrlId());
    }

    @Override
    public void updateCoreUrlInfoStatus(Integer coreUrlId, int infoStatus) throws Exception {
        dao.update(Sql.CoreUrlSql.updateCoreUrlInfoStatus, infoStatus, coreUrlId, coreUrlId);
    }

    @Override
    public void updateCoreUrlStoreNumById(Integer coreUrlId) throws Exception {
        dao.update(Sql.CoreUrlSql.updateCoreUrlStoreNumById, coreUrlId, coreUrlId);
    }
}
