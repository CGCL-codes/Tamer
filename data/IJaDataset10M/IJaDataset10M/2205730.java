package com.tony.common.dao;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.ibatis.common.util.PaginatedList;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import org.springframework.util.Assert;
import com.tony.common.dao.support.Page;

/**
 * IBatis Dao�ķ��ͻ���.
 * <p/>
 * �̳���Spring��SqlMapClientDaoSupport,�ṩ��ҳ��������ɱ�ݲ�ѯ���������Է���ֵ���˷�������ת��.
 *
 * @author suwei
 * @see SqlMapClientDaoSupport
 */
@SuppressWarnings("unchecked")
public class IBatisGenericDao extends SqlMapClientDaoSupport {

    public static final String POSTFIX_INSERT = ".insert";

    public static final String POSTFIX_UPDATE = ".update";

    public static final String POSTFIX_DELETE = ".delete";

    public static final String POSTFIX_DELETE_PRIAMARYKEY = ".deleteByPrimaryKey";

    public static final String POSTFIX_SELECT = ".select";

    public static final String POSTFIX_SELECTMAP = ".selectByMap";

    public static final String POSTFIX_SELECTSQL = ".selectBySql";

    public static final String POSTFIX_COUNT = ".count";

    /**
	 * ���ID��ȡ����
	 */
    public <T> T get(Class<T> entityClass, Serializable id) {
        T o = (T) getSqlMapClientTemplate().queryForObject(entityClass.getName() + POSTFIX_SELECT, id);
        if (o == null) throw new ObjectRetrievalFailureException(entityClass, id);
        return o;
    }

    /**
	 * ��ȡȫ������
	 */
    public <T> List<T> getAll(Class<T> entityClass) {
        return getSqlMapClientTemplate().queryForList(entityClass.getName() + POSTFIX_SELECT, null);
    }

    /**
	 * ��������
	 */
    public void insert(Object o) {
        getSqlMapClientTemplate().insert(o.getClass().getName() + POSTFIX_INSERT, o);
    }

    /**
	 * �������
	 */
    public void update(Object o) {
        getSqlMapClientTemplate().update(o.getClass().getName() + POSTFIX_UPDATE, o);
    }

    /**
	 * ɾ�����
	 */
    public void remove(Object o) {
        getSqlMapClientTemplate().delete(o.getClass().getName() + POSTFIX_DELETE, o);
    }

    /**
	 * ���IDɾ�����
	 */
    public <T> void removeById(Class<T> entityClass, Serializable id) {
        getSqlMapClientTemplate().delete(entityClass.getName() + POSTFIX_DELETE_PRIAMARYKEY, id);
    }

    /**
	 * map��ѯ.
	 *
	 * @param map ��������ԵĲ�ѯ
	 */
    public <T> List<T> find(Class<T> entityClass, Map map) {
        if (map == null) return this.getSqlMapClientTemplate().queryForList(entityClass.getName() + POSTFIX_SELECT, null); else {
            map.put("findBy", "True");
            return this.getSqlMapClientTemplate().queryForList(entityClass.getName() + POSTFIX_SELECTMAP, map);
        }
    }

    /**
	 * sql ��ѯ.
	 *
	 * @param sql ֱ��sql�����(��Ҫ��ֹע��ʽ����)
	 */
    public <T> List<T> find(Class<T> entityClass, String sql) {
        Assert.hasText(sql);
        if (StringUtils.isEmpty(sql)) return this.getSqlMapClientTemplate().queryForList(entityClass.getName() + POSTFIX_SELECT, null); else return this.getSqlMapClientTemplate().queryForList(entityClass.getName() + POSTFIX_SELECTSQL, sql);
    }

    /**
	 * ��������������ֵ��ѯ����.
	 *
	 * @return ��������Ķ����б�
	 */
    public <T> List<T> findBy(Class<T> entityClass, String name, Object value) {
        Assert.hasText(name);
        Map map = new HashMap();
        map.put(name, value);
        return find(entityClass, map);
    }

    /**
	 * ��������������ֵ��ѯ����.
	 *
	 * @return ���������Ψһ����
	 */
    public <T> T findUniqueBy(Class<T> entityClass, String name, Object value) {
        Assert.hasText(name);
        Map map = new HashMap();
        try {
            PropertyUtils.getProperty(entityClass.newInstance(), name);
            map.put(name, value);
            map.put("findUniqueBy", "True");
            return (T) getSqlMapClientTemplate().queryForObject(entityClass.getName() + POSTFIX_SELECTMAP, map);
        } catch (Exception e) {
            logger.error("Error when propertie on entity," + e.getMessage(), e.getCause());
            return null;
        }
    }

    /**
	 * ��������������ֵ��Like AnyWhere��ʽ��ѯ����.
	 */
    public <T> List<T> findByLike(Class<T> entityClass, String name, String value) {
        Assert.hasText(name);
        Map map = new HashMap();
        map.put(name, value);
        map.put("findLikeBy", "True");
        return getSqlMapClientTemplate().queryForList(entityClass.getName() + POSTFIX_SELECTMAP, map);
    }

    /**
	 * �ж϶���ĳЩ���Ե�ֵ����ݿ��в������ظ�
	 *
	 * @param tableName ��ݱ�����
	 * @param names	 ��POJO�ﲻ���ظ��������б�,�Զ��ŷָ� ��"name,loginid,password" FIXME how about in different schema?
	 */
    public boolean isNotUnique(Object entity, String tableName, String names) {
        try {
            String primarykey;
            Connection con = getSqlMapClient().getCurrentConnection();
            ResultSet dbMetaData = con.getMetaData().getPrimaryKeys(con.getCatalog(), null, tableName);
            dbMetaData.next();
            if (dbMetaData.getRow() > 0) {
                primarykey = dbMetaData.getString(4);
                if (names.indexOf(primarykey) > -1) return false;
            } else {
                return true;
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            return false;
        }
        return false;
    }

    /**
	 * ��ҳ��ѯ����ʹ��PaginatedList.
	 * 
	 * @param pageNoҳ��,��1��ʼ.
	 * @return ���ܼ�¼��͵�ǰҳ��ݵ�Page����.
	 */
    public Page pagedQuery(Class entityClass, Object parameterObject, int pageNo, int pageSize) {
        Assert.isTrue(pageNo >= 1, "pageNo should start from 1");
        Integer totalCount = (Integer) this.getSqlMapClientTemplate().queryForObject(entityClass.getName() + POSTFIX_COUNT, parameterObject);
        Assert.notNull(totalCount, "totalCount Error");
        if (totalCount.intValue() == 0) {
            return new Page();
        }
        List list;
        int totalPageCount = 0;
        int startIndex = 0;
        if (pageSize > 0) {
            totalPageCount = (totalCount / pageSize);
            totalPageCount += ((totalCount % pageSize) > 0) ? 1 : 0;
            if (totalPageCount > pageNo) {
                startIndex = (pageNo - 1) * pageSize;
            } else {
                startIndex = (totalPageCount - 1) * pageSize;
            }
            list = getSqlMapClientTemplate().queryForList(entityClass.getName() + POSTFIX_SELECT, parameterObject, startIndex, pageSize);
        } else {
            list = getSqlMapClientTemplate().queryForList(entityClass.getName() + POSTFIX_SELECT, parameterObject);
        }
        return new Page(startIndex, totalCount, pageSize, list);
    }
}
