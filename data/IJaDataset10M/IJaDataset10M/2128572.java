package net.sf.sqlking.spi;

import java.util.List;
import net.sf.sqlking.api.SqlKing;
import net.sf.sqlking.api.client.SystemException;
import net.sf.sqlking.spi.builder.InsertBuilder;
import net.sf.sqlking.spi.builder.SelectEntityBuilder;
import net.sf.sqlking.spi.builder.UpdateBuilder;
import net.sf.sqlking.spi.client.EntityUtils;
import org.springframework.jdbc.core.simple.SimpleJdbcDaoSupport;

public class SqlKingBean extends SimpleJdbcDaoSupport implements SqlKing {

    @Override
    public void insert(Object entity) {
        InsertBuilder ib = new InsertBuilder(entity);
        ib.buildInsert();
        getSimpleJdbcTemplate().update(ib.getSql(), ib.getParams().toArray());
    }

    @Override
    public void insert(Object entity, Class<?>... relationClasses) {
        if (true) {
            throw new SystemException("Not yet implemented");
        }
    }

    @Override
    public long update(Object entity) {
        UpdateBuilder ib = new UpdateBuilder(entity);
        ib.buildUpdate();
        long count = getSimpleJdbcTemplate().update(ib.getSql(), ib.getParams().toArray());
        ChangeTrackerUtils.clearChangesProperties(entity);
        return count;
    }

    @SuppressWarnings("unused")
    @Override
    public long update(Object entity, Class<?>... relationClasses) {
        if (true) {
            throw new SystemException("Not yet implemented");
        }
        return 0;
    }

    @SuppressWarnings("unused")
    @Override
    public long delete(Object entity) {
        if (true) {
            throw new SystemException("Not yet implemented");
        }
        return 0;
    }

    @SuppressWarnings("unused")
    @Override
    public long delete(Object entity, Class<?>... relationClass) {
        if (true) {
            throw new SystemException("Not yet implemented");
        }
        return 0;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public <T> T selectEntity(Class<T> entityClass, Object id) {
        SelectEntityBuilder seb = new SelectEntityBuilder(entityClass, id);
        seb.buildSelectEntity();
        return (T) getSimpleJdbcTemplate().queryForObject(seb.getSql(), new EntityRowMapper(entityClass), id);
    }

    @Override
    public long selectCount(Class<?> entityClass) {
        String table = EntityUtils.getTableName(entityClass);
        String sql = "select count(*) from " + table;
        return getSimpleJdbcTemplate().queryForLong(sql);
    }

    @Override
    public void selectRelation(Object entityObject, Class<?> relationClass) {
        if (true) {
            throw new SystemException("Not yet implemented");
        }
    }

    @Override
    @SuppressWarnings("unused")
    public <T> T selectOne(String sqlName, Class<T> mappingClass, Object params) {
        if (true) {
            throw new SystemException("Not yet implemented");
        }
        return null;
    }

    @Override
    @SuppressWarnings("unused")
    public <T> List<T> selectList(String sqlName, Class<T> mappingClass, Object params) {
        if (true) {
            throw new SystemException("Not yet implemented");
        }
        return null;
    }

    @Override
    @SuppressWarnings("unused")
    public Object toChangeTracker(Object entityObject) {
        if (true) {
            throw new SystemException("Not yet implemented");
        }
        return null;
    }

    @Override
    public void applyTrackedChanges(Object entityObject) {
        if (true) {
            throw new SystemException("Not yet implemented");
        }
    }
}
