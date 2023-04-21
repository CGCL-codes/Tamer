package com.wangyu001.daoimpl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.sysolar.sun.config.AppContext;
import org.sysolar.sun.jdbc.core.JdbcOperations;
import com.wangyu001.constant.Sql;
import com.wangyu001.dao.UserUrlCatDao;
import com.wangyu001.entity.UserUrl;
import com.wangyu001.entity.UserUrlCat;

public final class UserUrlCatDaoImpl implements UserUrlCatDao {

    private static JdbcOperations dao = AppContext.getJdbcOperations(UserUrlCatDaoImpl.class);

    public Long create(UserUrlCat userUrlCat, boolean returnId) throws SQLException {
        if (null == userUrlCat.getCatCdate()) {
            userUrlCat.setCatCdate(new Date());
        }
        if (null == userUrlCat.getCatOrder()) {
            userUrlCat.setCatOrder(0);
        }
        if (null == userUrlCat.getPrivateUrlCount()) {
            userUrlCat.setPrivateUrlCount(0L);
        }
        if (null == userUrlCat.getProtectedUrlCount()) {
            userUrlCat.setProtectedUrlCount(0L);
        }
        if (null == userUrlCat.getPublicUrlCount()) {
            userUrlCat.setPublicUrlCount(0L);
        }
        if (null == userUrlCat.getCatShowNum()) {
            userUrlCat.setCatShowNum(12);
        }
        if (null == userUrlCat.getCatCreateType()) {
            userUrlCat.setCatCreateType(UserUrlCat.CAT_CREATE_TYPE_UNLINIT);
        }
        if (null == userUrlCat.getCatShowType()) {
            userUrlCat.setCatShowType(UserUrlCat.CAT_SHOW_TYPE_PUBLIC);
        }
        if (null == userUrlCat.getCatColNum()) {
            userUrlCat.setCatColNum(UserUrlCat.CAT_COL_NUM_FIRST);
        }
        this.batchPlusCatOrder(userUrlCat.getUserId(), userUrlCat.getCatColNum(), userUrlCat.getCatOrder());
        dao.update(Sql.UserUrlCatSql.create, userUrlCat.getCatId(), userUrlCat.getUserId(), userUrlCat.getCatName(), userUrlCat.getCatCreateType(), userUrlCat.getCatShowType(), userUrlCat.getCatShowNum(), userUrlCat.getCatColNum(), userUrlCat.getCatOrder(), userUrlCat.getPrivateUrlCount(), userUrlCat.getProtectedUrlCount(), userUrlCat.getPublicUrlCount(), userUrlCat.getCatCdate(), userUrlCat.getCatUdate());
        if (returnId) {
            return dao.queryForLong(Sql.GenericSql.fetchLastInsertId);
        }
        return null;
    }

    public boolean remove(Long catId) throws SQLException {
        return dao.update(Sql.UserUrlCatSql.remove, catId) > 0;
    }

    public boolean update(UserUrlCat userUrlCat) throws SQLException {
        if (null == userUrlCat.getCatUdate()) userUrlCat.setCatUdate(new Date());
        return dao.update(Sql.UserUrlCatSql.update, userUrlCat.getCatName(), userUrlCat.getCatShowType(), userUrlCat.getCatShowNum(), userUrlCat.getCatUdate(), userUrlCat.getCatId()) > 0;
    }

    public boolean updateCatOrder(Long catId, Integer catOrder) throws SQLException {
        return dao.update(Sql.UserUrlCatSql.updateCatOrder, catOrder, catId) > 0;
    }

    public boolean updateCatOrderAndCatColNum(Long catId, Integer catColNum, Integer catOrder) throws SQLException {
        return dao.update(Sql.UserUrlCatSql.updateCatOrderAndCatColNum, catColNum, catOrder, catId) > 0;
    }

    public boolean batchPlusCatOrder(Long userId, Integer catColNum, Integer beginCatOrder) throws SQLException {
        return dao.update(Sql.UserUrlCatSql.batchPlusCatOrder01, userId, catColNum, beginCatOrder) > 0;
    }

    public boolean batchPlusCatOrder(Long userId, Integer catColNum, Integer beginCatOrder, Integer endCatOrder) throws SQLException {
        return dao.update(Sql.UserUrlCatSql.batchPlusCatOrder02, userId, catColNum, beginCatOrder, endCatOrder) > 0;
    }

    public boolean batchMinusCatOrder(Long userId, Integer catColNum, Integer beginCatOrder) throws SQLException {
        return dao.update(Sql.UserUrlCatSql.batchMinusCatOrder01, userId, catColNum, beginCatOrder) > 0;
    }

    public boolean batchMinusCatOrder(Long userId, Integer catColNum, Integer beginCatOrder, Integer endCatOrder) throws SQLException {
        return dao.update(Sql.UserUrlCatSql.batchMinusCatOrder02, userId, catColNum, beginCatOrder, endCatOrder) > 0;
    }

    public boolean updateUrlCount(Long catId) throws SQLException {
        return dao.update(Sql.UserUrlCatSql.updateUrlCount, catId, catId, catId, catId) > 0;
    }

    public UserUrlCat fetch(Long catId) throws SQLException {
        for (Map<String, Object> row : dao.queryForList(Sql.UserUrlCatSql.fetch, catId)) {
            return new UserUrlCat().fill(row);
        }
        return null;
    }

    public List<UserUrlCat> fetchListForSelf(Long userId) throws SQLException {
        return this.assembleList(dao.queryForList(Sql.UserUrlCatSql.fetchListForSelf, userId));
    }

    public List<UserUrlCat> fetchListForPeople(Long userId) throws SQLException {
        return this.assembleList(dao.queryForList(Sql.UserUrlCatSql.fetchListForPeople, userId));
    }

    private List<UserUrlCat> assembleList(List<Map<String, Object>> mapList) throws SQLException {
        Map<Long, UserUrlCat> userUrlCatMap = new LinkedHashMap<Long, UserUrlCat>();
        UserUrlCat userUrlCat = new UserUrlCat();
        List<UserUrl> userUrlList = null;
        for (Map<String, Object> row : mapList) {
            if (!userUrlCatMap.containsKey(userUrlCat.fill(row).getCatId())) {
                userUrlCatMap.put(userUrlCat.getCatId(), new UserUrlCat().fill(row));
            }
            UserUrl userUrl = new UserUrl().fill(row);
            if (null != userUrl.getUserUrlId()) {
                userUrlList = userUrlCatMap.get(userUrlCat.getCatId()).getUserUrlList();
                if (userUrlList.size() < userUrlCat.getCatShowNum()) {
                    userUrlList.add(userUrl);
                }
            }
        }
        return new ArrayList<UserUrlCat>(userUrlCatMap.values());
    }

    /**
     * 根据创建类别和用户ID获取分类列表.
     * @author jn
     * @since 2009年7月30日11:23:01
     * @param catCreateType
     * @param userId
     * @return
     */
    public List<UserUrlCat> fetchListByCreateType(Integer catCreateType, Long userId) throws SQLException {
        List<UserUrlCat> catList = new ArrayList<UserUrlCat>(1);
        for (Map<String, Object> row : dao.queryForList(Sql.UserUrlCatSql.fetchListByCreateType, catCreateType, userId)) {
            catList.add(new UserUrlCat().fill(row));
        }
        return catList;
    }

    /**
     * 根据分类名获取分类列表
     * @author jn
     * @since 2009年7月30日12:57:00
     * @param catName
     * @param userId
     * @return
     * @throws SQLException
     */
    public List<UserUrlCat> fetchListByCatName(String catName, Long userId) throws SQLException {
        List<UserUrlCat> catList = new ArrayList<UserUrlCat>(1);
        for (Map<String, Object> row : dao.queryForList(Sql.UserUrlCatSql.fetchListByCatName, catName, userId)) {
            catList.add(new UserUrlCat().fill(row));
        }
        return catList;
    }

    /**
     * 获取该用户下该列的最大order对象
     * @author jn
     * @since 2009年7月30日22:43:52
     * @param catColNum
     * @param userId
     * @return
     * @throws SQLException
     */
    public UserUrlCat fetchMaxOrderCatOnColumn(Integer catColNum, Long userId) throws SQLException {
        List<UserUrlCat> catList = new ArrayList<UserUrlCat>(1);
        for (Map<String, Object> row : dao.queryForList(Sql.UserUrlCatSql.fetchMaxOrderCatOnColumn, catColNum, userId)) {
            catList.add(new UserUrlCat().fill(row));
        }
        if (catList.size() == 0) {
            return new UserUrlCat().setCatOrder(-1);
        } else {
            return catList.get(0);
        }
    }

    public List<UserUrlCat> fetchCatListOnlyByUserId(Long userId, int showLevel) throws SQLException {
        List<UserUrlCat> catList = new ArrayList<UserUrlCat>(10);
        String sql = Sql.UserUrlCatSql.fetchCatListOnlyByUserId;
        for (Map<String, Object> row : dao.queryForList(sql, userId, showLevel)) {
            UserUrlCat userUrlCat = new UserUrlCat();
            userUrlCat.fill(row);
            int urlCount = (int) (userUrlCat.getPrivateUrlCount() + userUrlCat.getProtectedUrlCount() + userUrlCat.getPublicUrlCount());
            userUrlCat.setTotalUrlCount(urlCount);
            catList.add(userUrlCat);
        }
        return catList;
    }

    public List<UserUrlCat> export(int offset, int limit) throws SQLException {
        List<UserUrlCat> list = new ArrayList<UserUrlCat>(limit);
        for (Map<String, Object> row : dao.queryForList(Sql.UserUrlCatSql.export, offset, limit)) {
            list.add(new UserUrlCat().fill(row));
        }
        return list;
    }

    @Override
    public Map<Integer, Integer> fetchMaxCatOrder(Long userId) throws SQLException {
        Map<Integer, Integer> map = new HashMap<Integer, Integer>(3);
        UserUrlCat userUrlCat = new UserUrlCat();
        for (Map<String, Object> row : dao.queryForList(Sql.UserUrlCatSql.fetchMaxCatOrder, userId)) {
            userUrlCat.fill(row);
            map.put(userUrlCat.getCatColNum(), userUrlCat.getCatOrder());
        }
        return map;
    }
}
