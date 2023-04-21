package org.jumpmind.db.platform.hsqldb;

import java.sql.Types;
import javax.sql.DataSource;
import org.jumpmind.db.platform.AbstractJdbcDatabasePlatform;
import org.jumpmind.db.platform.DatabasePlatformSettings;

public class HsqlDbPlatform extends AbstractJdbcDatabasePlatform {

    public static final String DATABASENAME = "HsqlDb";

    public static final String JDBC_DRIVER = "org.hsqldb.jdbcDriver";

    public static final String JDBC_SUBPROTOCOL = "hsqldb";

    public HsqlDbPlatform(DataSource dataSource, DatabasePlatformSettings settings) {
        super(dataSource, settings);
        info.setNonPKIdentityColumnsSupported(false);
        info.setIdentityOverrideAllowed(false);
        info.setSystemForeignKeyIndicesAlwaysNonUnique(true);
        info.addNativeTypeMapping(Types.ARRAY, "LONGVARBINARY", Types.LONGVARBINARY);
        info.addNativeTypeMapping(Types.BLOB, "LONGVARBINARY", Types.LONGVARBINARY);
        info.addNativeTypeMapping(Types.CLOB, "LONGVARCHAR", Types.LONGVARCHAR);
        info.addNativeTypeMapping(Types.DISTINCT, "LONGVARBINARY", Types.LONGVARBINARY);
        info.addNativeTypeMapping(Types.FLOAT, "DOUBLE", Types.DOUBLE);
        info.addNativeTypeMapping(Types.JAVA_OBJECT, "OBJECT");
        info.addNativeTypeMapping(Types.NULL, "LONGVARBINARY", Types.LONGVARBINARY);
        info.addNativeTypeMapping(Types.REF, "LONGVARBINARY", Types.LONGVARBINARY);
        info.addNativeTypeMapping(Types.STRUCT, "LONGVARBINARY", Types.LONGVARBINARY);
        info.addNativeTypeMapping(Types.TINYINT, "SMALLINT", Types.SMALLINT);
        info.addNativeTypeMapping("BIT", "BOOLEAN", "BOOLEAN");
        info.addNativeTypeMapping("DATALINK", "LONGVARBINARY", "LONGVARBINARY");
        info.setDefaultSize(Types.CHAR, Integer.MAX_VALUE);
        info.setDefaultSize(Types.VARCHAR, Integer.MAX_VALUE);
        info.setDefaultSize(Types.BINARY, Integer.MAX_VALUE);
        info.setDefaultSize(Types.VARBINARY, Integer.MAX_VALUE);
        info.setStoresUpperCaseInCatalog(true);
        info.setNonBlankCharColumnSpacePadded(true);
        info.setBlankCharColumnSpacePadded(true);
        info.setCharColumnSpaceTrimmed(false);
        info.setEmptyStringNulled(false);
        ddlReader = new HsqlDbDdlReader(this);
        ddlBuilder = new HsqlDbBuilder(this);
    }

    @Override
    protected void createSqlTemplate() {
        this.sqlTemplate = new HsqlDbJdbcSqlTemplate(dataSource, settings, null);
    }

    public String getName() {
        return DATABASENAME;
    }

    public String getDefaultCatalog() {
        return null;
    }

    public String getDefaultSchema() {
        return null;
    }
}
