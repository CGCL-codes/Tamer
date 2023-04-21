package com.wangyu001.daoimpl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.sysolar.sun.config.AppContext;
import org.sysolar.sun.jdbc.core.JdbcOperations;
import com.wangyu001.constant.Sql;
import com.wangyu001.dao.HotWebCatDao;
import com.wangyu001.entity.HotWebCat;

public final class HotWebCatDaoImpl implements HotWebCatDao {

    private static JdbcOperations dao = AppContext.getJdbcOperations(HotWebCatDaoImpl.class);

    public Long create(HotWebCat hotWebCat, boolean returnId) throws SQLException {
        dao.update(Sql.HotWebCatSql.create, hotWebCat.getCatId(), hotWebCat.getCatName(), hotWebCat.getCatOrder(), hotWebCat.getTopCatId(), hotWebCat.getAdminId(), hotWebCat.getCatCdate(), hotWebCat.getCatUdate());
        if (returnId) {
            return dao.queryForLong(Sql.GenericSql.fetchLastInsertId);
        }
        return null;
    }

    public boolean remove(Long catId) throws SQLException {
        return dao.update(Sql.HotWebCatSql.remove, catId) > 0;
    }

    public HotWebCat fetch(Long catId) throws SQLException {
        for (Map<String, Object> row : dao.queryForList(Sql.HotWebCatSql.fetch, catId)) {
            return new HotWebCat().fill(row);
        }
        return null;
    }

    public boolean update(HotWebCat hotWebCat) throws SQLException {
        return dao.update(Sql.HotWebCatSql.update, hotWebCat.getCatName(), hotWebCat.getCatOrder(), hotWebCat.getTopCatId(), hotWebCat.getAdminId(), new Date(), hotWebCat.getCatId()) > 0;
    }

    public List<HotWebCat> fetchHotWebCatList() throws SQLException {
        List<HotWebCat> catList = new ArrayList<HotWebCat>(50);
        for (Map<String, Object> row : dao.queryForList(Sql.HotWebCatSql.fetchHotWebCatList)) {
            catList.add(new HotWebCat().fill(row));
        }
        return catList;
    }

    public boolean removeByTopCatId(Long topCatId) throws SQLException {
        return dao.update(Sql.HotWebCatSql.removeByTopCatId, topCatId) > 0;
    }

    public List<HotWebCat> export(int offset, int limit) throws SQLException {
        List<HotWebCat> list = new ArrayList<HotWebCat>(limit);
        for (Map<String, Object> row : dao.queryForList(Sql.HotWebCatSql.export, offset, limit)) {
            list.add(new HotWebCat().fill(row));
        }
        return list;
    }
}
