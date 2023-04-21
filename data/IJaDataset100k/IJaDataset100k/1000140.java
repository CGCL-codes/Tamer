package org.jumpmind.db.platform.mysql;

import javax.sql.DataSource;
import org.apache.commons.lang.StringUtils;
import org.jumpmind.db.platform.AbstractJdbcDatabasePlatform;
import org.jumpmind.db.platform.DatabaseNamesConstants;
import org.jumpmind.db.platform.DatabasePlatformSettings;

public class MySqlDatabasePlatform extends AbstractJdbcDatabasePlatform {

    public static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";

    public static final String JDBC_DRIVER_OLD = "org.gjt.mm.mysql.Driver";

    public static final String JDBC_SUBPROTOCOL = "mysql";

    public MySqlDatabasePlatform(DataSource dataSource, DatabasePlatformSettings settings) {
        super(dataSource, overrideSettings(settings));
        ddlReader = new MySqlDdlReader(this);
        ddlBuilder = new MySqlDdlBuilder();
    }

    @Override
    protected void createSqlTemplate() {
        this.sqlTemplate = new MySqlJdbcSqlTemplate(dataSource, settings, null);
    }

    protected static DatabasePlatformSettings overrideSettings(DatabasePlatformSettings settings) {
        settings.setFetchSize(Integer.MIN_VALUE);
        return settings;
    }

    public String getName() {
        return DatabaseNamesConstants.MYSQL;
    }

    public String getDefaultSchema() {
        return null;
    }

    public String getDefaultCatalog() {
        if (StringUtils.isBlank(defaultCatalog)) {
            defaultCatalog = getSqlTemplate().queryForObject("select database()", String.class);
        }
        return defaultCatalog;
    }
}
