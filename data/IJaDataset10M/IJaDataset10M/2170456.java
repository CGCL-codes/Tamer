package com.identisense.data.dao;

import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import com.identisense.data.datasource.DataSourceCache;
import com.identisense.data.datasource.DataSourceCache.DataSourceName;

public abstract class BaseJdbcDao {

    private JdbcTemplate jdbcTemplate = new JdbcTemplate();

    private DataSourceCache dataSourceCache;

    public JdbcTemplate getJdbcTemplate() {
        if (jdbcTemplate.getDataSource() == null) {
            DataSource ds = getDataSourceCache().getDataSource(getDataSourceName());
            jdbcTemplate.setDataSource(ds);
        }
        return jdbcTemplate;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public DataSourceName getDataSourceName() {
        return DataSourceCache.DataSourceName.CAMEO;
    }

    public DataSourceCache getDataSourceCache() {
        return dataSourceCache;
    }

    public void setDataSourceCache(DataSourceCache dataSourceCache) {
        this.dataSourceCache = dataSourceCache;
    }
}
