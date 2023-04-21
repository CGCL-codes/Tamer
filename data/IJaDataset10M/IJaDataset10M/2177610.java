package org.book4j.hibernate;

import java.util.Properties;

public class HibernateProperties extends Properties {

    private static final long serialVersionUID = 1L;

    public HibernateProperties() {
        this(false, false, false, false);
    }

    public HibernateProperties(boolean create, boolean cache, boolean showSQL, boolean update) {
        super();
        String connectionUrl = "jdbc:derby:data";
        if (create) {
            connectionUrl += ";create=true";
        }
        setProperty("hibernate.connection.url", connectionUrl);
        setProperty("hibernate.connection.driver_class", "org.apache.derby.jdbc.EmbeddedDriver");
        setProperty("hibernate.dialect", "org.hibernate.dialect.DerbyDialect");
        setProperty("hibernate.transaction.factory_class", "org.hibernate.transaction.JDBCTransactionFactory");
        setProperty("hibernate.current_session_context_class", "thread");
        setProperty("hibernate.transaction.auto_close_session", "true");
        if (cache) {
            setProperty("hibernate.cache.provider_class", "org.hibernate.cache.EhCacheProvider");
        }
        if (showSQL) {
            setProperty("hibernate.show_sql", "true");
        } else {
            setProperty("hibernate.show_sql", "false");
        }
        if (update) {
            setProperty("hibernate.hbm2ddl.auto", "update");
        } else {
            setProperty("hibernate.hbm2ddl.auto", "none");
        }
    }
}
