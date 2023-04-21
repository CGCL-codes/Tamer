package org.middleheaven.persistance.db.datasource;

import javax.sql.DataSource;

public interface DataSourceProvider {

    public DataSource getDataSource();
}
