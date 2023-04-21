package net.livebookstore.dao.jdbc;

import java.io.Serializable;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

/**
 * Jdbc implementation of GenericDao.
 * 
 * @author Xuefeng
 * 
 * @deprecated
 */
public class GenericJdbcDao<T> extends JdbcDaoSupport {

    private final DomainRowMapper mapper;

    private final Class<T> clazz;

    private final String columnList;

    private final String parameterList;

    public GenericJdbcDao(Class<T> clazz) {
        this.clazz = clazz;
        this.mapper = new DomainRowMapper(clazz);
        StringBuffer columns = new StringBuffer();
        StringBuffer parameters = new StringBuffer();
        for (String c : mapper.getColumns()) {
            columns.append(',').append(c);
            parameters.append(",?");
        }
        columnList = columns.substring(1);
        parameterList = parameters.substring(1);
    }

    public void create(T t) {
        Object[] values = new Object[mapper.getColumns().length];
        int n = 0;
        for (String c : mapper.getColumns()) {
            values[n] = mapper.getValue(t, c);
            n++;
        }
        getJdbcTemplate().update("insert into " + mapper.getTable() + "(" + columnList + ") values (" + parameterList + ")", values);
    }

    public void delete(T t) {
        getJdbcTemplate().update("delete from " + mapper.getTable() + " where " + mapper.getId() + "=?", new Object[] { mapper.getValue(t, mapper.getId()) });
    }

    public Object query(Serializable id) {
        return getJdbcTemplate().queryForObject("select " + columnList + " from " + mapper.getTable() + " where " + mapper.getId() + "=?", new Object[] { id }, mapper);
    }

    public void update(T t) {
        StringBuffer updateSql = new StringBuffer();
        updateSql.append("update ").append(mapper.getTable()).append(" set ");
        String[] columns = mapper.getColumns();
        for (int i = 1; i < columns.length; i++) {
            updateSql.append(columns[i]).append("=?,");
        }
        updateSql.deleteCharAt(updateSql.length() - 1);
        updateSql.append(" where ").append(mapper.getId() + "=?");
        Object[] values = new Object[columns.length];
        for (int i = 0; i < values.length - 1; i++) {
            values[i] = mapper.getValue(t, columns[i + 1]);
        }
        values[values.length - 1] = mapper.getValue(t, mapper.getId());
        getJdbcTemplate().update(updateSql.toString(), new Object[] { mapper.getValue(t, mapper.getId()) });
    }
}
