package org.dolmen.orm.support;

import javax.sql.DataSource;
import org.dolmen.orm.OrmSession;
import org.dolmen.orm.OrmSessionFactory;

public abstract class BaseOrmSessionManager {

    private DataSource fDataSource;

    private OrmSessionFactory fSessionFactory;

    public abstract OrmSession getSession();

    public void setDataSource(DataSource aDataSource) {
        assert aDataSource != null;
        fDataSource = aDataSource;
    }

    public DataSource getDataSource() {
        return fDataSource;
    }

    public OrmSessionFactory getSessionFactory() {
        return fSessionFactory;
    }

    public void setSessionFactory(OrmSessionFactory aSessionFactory) {
        fSessionFactory = aSessionFactory;
    }
}
