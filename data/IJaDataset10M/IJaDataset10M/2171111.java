package org.jumpmind.symmetric.ddl.platform.firebird;

import java.sql.Types;
import org.jumpmind.symmetric.ddl.PlatformInfo;
import org.jumpmind.symmetric.ddl.platform.PlatformImplBase;

/**
 * The platform implementation for the Firebird database.
 * It is assumed that the database is configured with sql dialect 3!
 * 
 * @version $Revision: 231306 $
 */
public class FirebirdPlatform extends PlatformImplBase {

    /** Database name of this platform. */
    public static final String DATABASENAME = "Firebird";

    /** The standard Firebird jdbc driver. */
    public static final String JDBC_DRIVER = "org.firebirdsql.jdbc.FBDriver";

    /** The subprotocol used by the standard Firebird driver. */
    public static final String JDBC_SUBPROTOCOL = "firebirdsql";

    /**
     * Creates a new Firebird platform instance.
     */
    public FirebirdPlatform() {
        PlatformInfo info = getPlatformInfo();
        info.setMaxIdentifierLength(31);
        info.setSystemForeignKeyIndicesAlwaysNonUnique(true);
        info.setCommentPrefix("/*");
        info.setCommentSuffix("*/");
        info.addNativeTypeMapping(Types.ARRAY, "BLOB", Types.LONGVARBINARY);
        info.addNativeTypeMapping(Types.BINARY, "BLOB", Types.LONGVARBINARY);
        info.addNativeTypeMapping(Types.BIT, "SMALLINT", Types.SMALLINT);
        info.addNativeTypeMapping(Types.CLOB, "BLOB SUB_TYPE TEXT", Types.LONGVARCHAR);
        info.addNativeTypeMapping(Types.DISTINCT, "BLOB", Types.LONGVARBINARY);
        info.addNativeTypeMapping(Types.BLOB, "BLOB", Types.LONGVARBINARY);
        info.addNativeTypeMapping(Types.DOUBLE, "DOUBLE PRECISION");
        info.addNativeTypeMapping(Types.FLOAT, "DOUBLE PRECISION", Types.DOUBLE);
        info.addNativeTypeMapping(Types.JAVA_OBJECT, "BLOB", Types.LONGVARBINARY);
        info.addNativeTypeMapping(Types.LONGVARBINARY, "BLOB", Types.LONGVARBINARY);
        info.addNativeTypeMapping(Types.LONGVARCHAR, "BLOB SUB_TYPE TEXT");
        info.addNativeTypeMapping(Types.NULL, "BLOB", Types.LONGVARBINARY);
        info.addNativeTypeMapping(Types.OTHER, "BLOB", Types.LONGVARBINARY);
        info.addNativeTypeMapping(Types.REAL, "FLOAT");
        info.addNativeTypeMapping(Types.REF, "BLOB", Types.LONGVARBINARY);
        info.addNativeTypeMapping(Types.STRUCT, "BLOB", Types.LONGVARBINARY);
        info.addNativeTypeMapping(Types.TINYINT, "SMALLINT", Types.SMALLINT);
        info.addNativeTypeMapping(Types.VARBINARY, "BLOB", Types.LONGVARBINARY);
        info.addNativeTypeMapping("BOOLEAN", "SMALLINT", "SMALLINT");
        info.addNativeTypeMapping("DATALINK", "BLOB", "LONGVARBINARY");
        info.setDefaultSize(Types.VARCHAR, 254);
        info.setDefaultSize(Types.CHAR, 254);
        setSqlBuilder(new FirebirdBuilder(this));
        setModelReader(new FirebirdModelReader(this));
    }

    /**
     * {@inheritDoc}
     */
    public String getName() {
        return DATABASENAME;
    }
}
