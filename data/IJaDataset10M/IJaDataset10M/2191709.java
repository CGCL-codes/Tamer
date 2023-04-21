package com.kongur.star.venus.dao;

import java.sql.SQLException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ibatis.SqlMapClientCallback;
import org.springframework.orm.ibatis.SqlMapClientTemplate;
import com.ibatis.sqlmap.client.SqlMapExecutor;
import com.kongur.star.venus.common.page.Paginable;
import com.kongur.star.venus.common.page.SimplePage;

/**
 * @author gaojf
 * @version $Id: BaseDAO.java,v 0.1 2012-3-28 ����05:03:35 gaojf Exp $
 */
public abstract class BaseDAO<E> {

    protected int batchSize = 500;

    @Autowired
    private SqlMapClientTemplate sqlMapClientTemplate;

    public SqlMapClientTemplate getSqlMapClientTemplate() {
        return sqlMapClientTemplate;
    }

    /**
     * ��ݷ�ҳ������ȡ��ҳ��Ϣ, ����page����ĿǰĬ�ϲ���SimplePage��������
     * 
     * @param <T>
     * @param page ��ҳ��Ϣ
     * @param qTotalCount
     * @param qPagination
     */
    @SuppressWarnings("unchecked")
    public final <T> Paginable<T> paginate(Paginable<T> page, String qTotalCount, String qPagination) {
        if (!(page instanceof SimplePage)) {
            throw new IllegalArgumentException("'page' argument is unsupport class type, " + "it must be " + SimplePage.class.getName() + " or subclass");
        }
        if (page instanceof SimplePage) {
            SimplePage simplePage = (SimplePage) page;
            if (simplePage.isDisablePagination()) {
                simplePage.setData(this.getSqlMapClientTemplate().queryForList(qPagination, page));
                return simplePage;
            }
        }
        int totalCount = (Integer) getSqlMapClientTemplate().queryForObject(qTotalCount, page);
        if (totalCount > 0) {
            SimplePage<T> _page = (SimplePage<T>) page;
            _page.setTotalCount(totalCount);
            _page.setData(this.getSqlMapClientTemplate().queryForList(qPagination, page));
        }
        return page;
    }

    /**
     * ����Ҫ��ҳ���������Ϣ (���Բ���Ҫ������)
     * 
     * @param <T>
     * @param page
     * @param qPagination
     */
    @SuppressWarnings("unchecked")
    public final <T> Paginable<T> paginate(Paginable<T> page, String qPagination) {
        if (!(page instanceof SimplePage)) {
            throw new IllegalArgumentException("'page' argument is unsupport class type, " + "it must be " + SimplePage.class.getName() + " or subclass");
        }
        List<T> result = getSqlMapClientTemplate().queryForList(qPagination, page);
        if (result != null) {
            SimplePage<T> _page = (SimplePage<T>) page;
            _page.setPageSize(result.size());
            _page.setTotalCount(result.size());
            _page.setData(result);
        }
        return page;
    }

    public Long executeInsert(String statementName, E parameterObject) {
        return (Long) getSqlMapClientTemplate().insert(statementName, parameterObject);
    }

    public Integer executeUpdate(String statementName, E parameterObject) {
        return (int) getSqlMapClientTemplate().update(statementName, parameterObject);
    }

    @SuppressWarnings("unchecked")
    public List<E> selectObject(String statementName, E ob) {
        return (List<E>) getSqlMapClientTemplate().queryForList(statementName, ob);
    }

    @SuppressWarnings("unchecked")
    public E selectObjectById(String statementName, long id) {
        return (E) getSqlMapClientTemplate().queryForObject(statementName, id);
    }

    public int batchInsert(final String statement, final List<?> insertList) {
        return batchExecute(statement, insertList, new Callback() {

            @Override
            public void callback(String statement, Object param, SqlMapExecutor executor) throws SQLException {
                executor.insert(statement, param);
            }
        });
    }

    public int batchExecute(final String statement, final List<?> executeList, final Callback callback) {
        Integer rows = getSqlMapClientTemplate().execute(new SqlMapClientCallback<Integer>() {

            @Override
            public Integer doInSqlMapClient(SqlMapExecutor executor) throws SQLException {
                executor.startBatch();
                for (Object obj : executeList) {
                    callback.callback(statement, obj, executor);
                }
                return executor.executeBatch();
            }
        });
        return rows == null ? 0 : rows.intValue();
    }

    private interface Callback {

        void callback(String statement, Object param, SqlMapExecutor executor) throws SQLException;
    }
}
