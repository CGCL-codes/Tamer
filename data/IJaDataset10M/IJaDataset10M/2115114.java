package org.jumpmind.symmetric.ddlutils.sqlite;

import java.sql.Types;
import org.apache.ddlutils.Platform;
import org.apache.ddlutils.PlatformInfo;
import org.apache.ddlutils.platform.PlatformImplBase;

/**
 * The platform implementation for the SQLite database.
 * 
 * @version $Revision: 231306 $
 */
public class SqLitePlatform extends PlatformImplBase implements Platform {

    /** Database name of this platform. */
    public static final String DATABASENAME = "SQLite3";

    /** The standard H2 driver. */
    public static final String JDBC_DRIVER = "org.sqlite.JDBC";

    /**
     * Creates a new instance of the H2 platform.
     */
    public SqLitePlatform() {
        PlatformInfo info = getPlatformInfo();
        info.setNonPKIdentityColumnsSupported(false);
        info.setIdentityOverrideAllowed(false);
        info.setSystemForeignKeyIndicesAlwaysNonUnique(true);
        info.setNullAsDefaultValueRequired(false);
        info.addNativeTypeMapping(Types.ARRAY, "BINARY", Types.BINARY);
        info.addNativeTypeMapping(Types.DISTINCT, "BINARY", Types.BINARY);
        info.addNativeTypeMapping(Types.NULL, "BINARY", Types.BINARY);
        info.addNativeTypeMapping(Types.REF, "BINARY", Types.BINARY);
        info.addNativeTypeMapping(Types.STRUCT, "BINARY", Types.BINARY);
        info.addNativeTypeMapping(Types.DATALINK, "BINARY", Types.BINARY);
        info.addNativeTypeMapping(Types.BIT, "BOOLEAN", Types.BIT);
        info.addNativeTypeMapping(Types.TINYINT, "SMALLINT", Types.TINYINT);
        info.addNativeTypeMapping(Types.SMALLINT, "SMALLINT", Types.SMALLINT);
        info.addNativeTypeMapping(Types.BINARY, "BINARY", Types.BINARY);
        info.addNativeTypeMapping(Types.BLOB, "BLOB", Types.BLOB);
        info.addNativeTypeMapping(Types.CLOB, "CLOB", Types.CLOB);
        info.addNativeTypeMapping(Types.FLOAT, "DOUBLE", Types.DOUBLE);
        info.addNativeTypeMapping(Types.JAVA_OBJECT, "OTHER");
        info.setDefaultSize(Types.CHAR, Integer.MAX_VALUE);
        info.setDefaultSize(Types.VARCHAR, Integer.MAX_VALUE);
        info.setDefaultSize(Types.BINARY, Integer.MAX_VALUE);
        info.setDefaultSize(Types.VARBINARY, Integer.MAX_VALUE);
        setSqlBuilder(new SqLiteBuilder(this));
        setModelReader(new SqLiteModelReader(this));
    }

    /**
     * {@inheritDoc}
     */
    public String getName() {
        return DATABASENAME;
    }
}
