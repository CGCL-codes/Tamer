package org.gbif.portal.dao.impl.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.gbif.portal.dao.IndexDataDAO;
import org.gbif.portal.model.IndexData;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

/**
 * Pure JDBC implementation
 * @author trobertson
 */
public class IndexDataDAOImpl extends JdbcDaoSupport implements IndexDataDAO {

    /**
	 * The create sql
	 */
    protected static final String CREATE_SQL = "insert into index_data(" + "resource_access_point_id," + "type," + "lower_value," + "upper_value) " + "values (:resourceAccessPointId," + ":type," + ":lowerLimit," + ":upperLimit)";

    /**
	 * The set started sql
	 */
    protected static final String SET_STARTED_SQL = "update index_data set started=now() where id=?";

    /**
	 * The set started sql
	 */
    protected static final String SET_FINISHED_SQL = "update index_data set finished=now() where id=?";

    /**
	 * The delete sql
	 */
    protected static final String DELETE_SQL = "delete from index_data where id=?";

    /**
	 * The delete unfisnihsed sql
	 */
    protected static final String DELETE_UNFINISHED_SQL = "delete from index_data where resource_access_point_id=? and finsihed is null";

    /**
	 * The delete unfisnihsed sql
	 */
    protected static final String DEACTIVATE_UNFINISHED_SQL = "update index_data set started=now(), finished=now() where resource_access_point_id=? and finished is null";

    /**
	 * The find sql
	 */
    protected static final String FIND_BY_SQL = "select id, resource_access_point_id, type, lower_value, upper_value, started, finished from index_data where resource_access_point_id=? and finished is null order by id";

    /**
	 * To make use of bean naming
	 */
    protected NamedParameterJdbcTemplate namedParameterTemplate;

    /**
	 * Reusable row mapper
	 */
    protected IndexDataRowMapper indexDataRowMapper = new IndexDataRowMapper();

    /**
	 * Utility to create a IndexDataRowMapper for a row 
	 * @author trobertson
	 */
    protected class IndexDataRowMapper implements RowMapper {

        /**
		 * The factory
		 */
        public IndexData mapRow(ResultSet rs, int rowNumber) throws SQLException {
            return new IndexData(rs.getLong("id"), rs.getLong("resource_access_point_id"), rs.getInt("type"), rs.getString("lower_value"), rs.getString("upper_value"), rs.getDate("started"), rs.getDate("finished"));
        }
    }

    /**
	 * @see org.gbif.portal.dao.IndexDataDAO#create(org.gbif.portal.model.IndexData)
	 */
    public long create(IndexData indexData) {
        SqlParameterSource namedParameters = new BeanPropertySqlParameterSource(indexData);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        getNamedParameterTemplate().update(IndexDataDAOImpl.CREATE_SQL, namedParameters, keyHolder);
        indexData.setId(keyHolder.getKey().longValue());
        return keyHolder.getKey().longValue();
    }

    /**
	 * @see org.gbif.portal.dao.IndexDataDAO#setStartAsNow(int)
	 */
    public void setStartAsNow(long id) {
        getJdbcTemplate().update(IndexDataDAOImpl.SET_STARTED_SQL, new Object[] { id });
    }

    /**
	 * @see org.gbif.portal.dao.IndexDataDAO#setFinishedAsNow(int)
	 */
    public void setFinishedAsNow(long id) {
        getJdbcTemplate().update(IndexDataDAOImpl.SET_FINISHED_SQL, new Object[] { id });
    }

    /**
	 * @see org.gbif.portal.dao.IndexDataDAO#delete(int)
	 */
    public void delete(final long id) {
        getJdbcTemplate().update(IndexDataDAOImpl.DELETE_SQL, new Object[] { id });
    }

    /**
	 * @see org.gbif.portal.dao.IndexDataDAO#deleteAllUnfinished(long)
	 */
    public void deleteAllUnfinished(final long rapid) {
        getJdbcTemplate().update(IndexDataDAOImpl.DELETE_UNFINISHED_SQL, new Object[] { rapid });
    }

    /**
	 * @see org.gbif.portal.dao.IndexDataDAO#deactivateAllUnfinished(long)
	 */
    public void deactivateAllUnfinished(final long rapid) {
        getJdbcTemplate().update(IndexDataDAOImpl.DEACTIVATE_UNFINISHED_SQL, new Object[] { rapid });
    }

    /**
	 * @see org.gbif.portal.dao.IndexDataDAO#findByResourceAccessPointId(int)
	 */
    @SuppressWarnings("unchecked")
    public List<IndexData> findByResourceAccessPointId(final long rapId) {
        return (List<IndexData>) getJdbcTemplate().query(IndexDataDAOImpl.FIND_BY_SQL, new Object[] { rapId }, new RowMapperResultSetExtractor(indexDataRowMapper, 1000));
    }

    /**
	 * @return Returns the namedParameterTemplate.
	 */
    public NamedParameterJdbcTemplate getNamedParameterTemplate() {
        return namedParameterTemplate;
    }

    /**
	 * @param namedParameterTemplate The namedParameterTemplate to set.
	 */
    public void setNamedParameterTemplate(NamedParameterJdbcTemplate namedParameterTemplate) {
        this.namedParameterTemplate = namedParameterTemplate;
    }
}
