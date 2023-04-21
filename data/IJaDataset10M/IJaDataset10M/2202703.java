package com.coyousoft.adsys.daoimpl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.sysolar.sun.config.AppContext;
import org.sysolar.sun.jdbc.core.JdbcOperations;
import com.coyousoft.adsys.constant.Sql;
import com.coyousoft.adsys.dao.AdvertDao;
import com.coyousoft.adsys.entity.Advert;

public final class AdvertDaoImpl implements AdvertDao {

    private static JdbcOperations dao = AppContext.getJdbcOperations(AdvertDaoImpl.class);

    public Long create(Advert advert, boolean returnId) throws SQLException {
        advert.setDefault();
        dao.update(Sql.AdvertSql.create, advert.getAdvertId(), advert.getAdvertTitle(), advert.getAdvertContent(), advert.getAdvertTag(), advert.getAdvertType(), advert.getAdvertBeginDate(), advert.getAdvertEndDate(), advert.getAdvertStatus(), advert.getAdvertEmailNum(), advert.getAdvertEmailReceiverNum(), advert.getAdvertEmailOpenedNum(), advert.getAdvertUrlClickedNum(), advert.getAdvertCreatedDate(), advert.getAdvertCategory(), advert.getTargetSex(), advert.getTargetAgeFrom(), advert.getTargetAgeTo(), advert.getTargetKey());
        if (returnId) {
            return dao.queryForLong(Sql.GenericSql.fetchLastInsertId);
        }
        return null;
    }

    public boolean updateAdvertContent(String advertContent, Long advertId) throws SQLException {
        return dao.update(Sql.AdvertSql.updateAdvertContent, advertContent, advertId) > 0;
    }

    public boolean remove(Long advertId) throws SQLException {
        return dao.update(Sql.AdvertSql.remove, advertId) > 0;
    }

    public Advert fetch(Long advertId) throws SQLException {
        for (Map<String, Object> row : dao.queryForList(Sql.AdvertSql.fetch, advertId)) {
            return new Advert().fill(row);
        }
        return null;
    }

    public List<Advert> fetchList(int limit) throws SQLException {
        List<Advert> list = new ArrayList<Advert>(limit);
        Date now = new Date();
        for (Map<String, Object> row : dao.queryForList(Sql.AdvertSql.fetchList, now, now, limit)) {
            list.add(new Advert().fill(row));
        }
        return list;
    }

    public List fetchEmailList() throws SQLException {
        List list = new ArrayList();
        Date rightNow = new Date();
        for (Map<String, Object> row : dao.queryForList(Sql.AdvertSql.fetchEmailList)) {
            list.add(row);
        }
        return list;
    }
}
