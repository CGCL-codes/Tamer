package org.carp.jdbc;

import java.sql.Connection;
import javax.sql.DataSource;
import org.apache.log4j.Logger;
import org.carp.exception.CarpException;
import org.carp.sql.CarpSql;
import org.carp.sql.DB2CarpSql;
import org.carp.sql.HSQLCarpSql;
import org.carp.sql.MsSqlServerCarpSql;
import org.carp.sql.MySqlCarpSql;
import org.carp.sql.OracleCarpSql;
import org.carp.sql.SqlServer2005CarpSql;

public abstract class AbstractDataSourceContext implements DataSourceContext {

    private static final Logger logger = Logger.getLogger(AbstractDataSourceContext.class);

    private Class<?> carpSqlClass;

    private String databaseName;

    private int databaseVersion;

    private DataSource dataSource;

    protected void initDatabaseInfo() throws CarpException {
        try {
            Connection conn = this.getDataSource().getConnection();
            this.databaseName = conn.getMetaData().getDatabaseProductName().toUpperCase();
            this.databaseVersion = conn.getMetaData().getDatabaseMajorVersion();
            if (logger.isDebugEnabled()) {
                logger.debug("database : " + this.getDatabaseProductName() + " , MajorVersion : " + this.databaseVersion);
            }
            conn.close();
        } catch (Exception ex) {
            throw new CarpException(ex);
        }
    }

    protected void initDialect() {
        if (this.databaseName.indexOf("DB2") != -1) {
            carpSqlClass = DB2CarpSql.class;
        } else if (this.databaseName.indexOf("ORACLE") != -1) {
            carpSqlClass = OracleCarpSql.class;
        } else if (this.databaseName.indexOf("MYSQL") != -1) {
            carpSqlClass = MySqlCarpSql.class;
        } else if (this.databaseName.indexOf("HSQL") != -1) {
            carpSqlClass = HSQLCarpSql.class;
        } else if (this.databaseName.indexOf("SQL SERVER") != -1) {
            if (this.databaseVersion >= 9) carpSqlClass = SqlServer2005CarpSql.class; else carpSqlClass = MsSqlServerCarpSql.class;
        }
    }

    public CarpSql getCarpSql(Class<?> cls) {
        CarpSql carp = null;
        try {
            carp = (CarpSql) carpSqlClass.newInstance();
            carp.setClass(cls);
            carp.setSchema(this.getCarp().getSchema());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return carp;
    }

    public DataSource getDataSource() {
        return this.dataSource;
    }

    protected void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public String getDatabaseProductName() {
        return databaseName;
    }

    protected void setDatabaseProductName(String database) {
        this.databaseName = database;
    }

    protected Class<?> getCarpSqlClass() {
        return carpSqlClass;
    }
}
