package org.hsqldb;

import org.hsqldb.HsqlNameManager.HsqlName;
import org.hsqldb.error.Error;
import org.hsqldb.error.ErrorCode;
import org.hsqldb.types.CharacterType;
import org.hsqldb.types.Charset;
import org.hsqldb.types.DateTimeType;
import org.hsqldb.types.NumberType;
import org.hsqldb.types.Type;
import org.hsqldb.types.Types;
import org.hsqldb.types.UserTypeModifier;

/**
 * Invariant schema objects.
 *
 * @author Fred Toussi (fredt@users dot sourceforge.net)
 * @version 1.9.0
 * @since 1.9.0
 */
public class SqlInvariants {

    /**
     * The role name reserved for authorization of INFORMATION_SCHEMA and
     * system objects.
     */
    public static final String SYSTEM_AUTHORIZATION_NAME = "_SYSTEM";

    /** The role name reserved for ADMIN users. */
    public static final String DBA_ADMIN_ROLE_NAME = "DBA";

    /** The role name allowing schema creation for users. */
    public static final String SCHEMA_CREATE_ROLE_NAME = "CREATE_SCHEMA";

    /** The role name allowing switching authorisation for users. */
    public static final String CHANGE_AUTH_ROLE_NAME = "CHANGE_AUTHORIZATION";

    public static final String SYSTEM_SUBQUERY = "SYSTEM_SUBQUERY";

    /** The role name reserved for the special PUBLIC pseudo-user. */
    public static final String PUBLIC_ROLE_NAME = "PUBLIC";

    public static final String SYSTEM_SCHEMA = "SYSTEM_SCHEMA";

    public static final String LOBS_SCHEMA = "SYSTEM_LOBS";

    public static final String DEFINITION_SCHEMA = "DEFINITION_SCHEMA";

    public static final String INFORMATION_SCHEMA = "INFORMATION_SCHEMA";

    public static final String SQLJ_SCHEMA = "SQLJ";

    public static final String PUBLIC_SCHEMA = "PUBLIC";

    public static final String CLASSPATH_NAME = "CLASSPATH";

    public static final String MODULE = "MODULE";

    public static final String DUAL = "DUAL";

    public static final String DUMMY = "DUMMY";

    public static final HsqlName INFORMATION_SCHEMA_HSQLNAME;

    public static final HsqlName SYSTEM_SCHEMA_HSQLNAME;

    public static final HsqlName LOBS_SCHEMA_HSQLNAME;

    public static final HsqlName SQLJ_SCHEMA_HSQLNAME;

    public static final HsqlName SYSTEM_SUBQUERY_HSQLNAME;

    public static final HsqlName MODULE_HSQLNAME;

    public static final HsqlName DUAL_TABLE_HSQLNAME;

    public static final HsqlName DUAL_COLUMN_HSQLNAME;

    static {
        INFORMATION_SCHEMA_HSQLNAME = HsqlNameManager.newSystemObjectName(INFORMATION_SCHEMA, SchemaObject.SCHEMA);
        SYSTEM_SCHEMA_HSQLNAME = HsqlNameManager.newSystemObjectName(SYSTEM_SCHEMA, SchemaObject.SCHEMA);
        LOBS_SCHEMA_HSQLNAME = HsqlNameManager.newSystemObjectName(LOBS_SCHEMA, SchemaObject.SCHEMA);
        SQLJ_SCHEMA_HSQLNAME = HsqlNameManager.newSystemObjectName(SQLJ_SCHEMA, SchemaObject.SCHEMA);
        SYSTEM_SUBQUERY_HSQLNAME = HsqlNameManager.newSystemObjectName(SYSTEM_SUBQUERY, SchemaObject.TABLE);
        MODULE_HSQLNAME = HsqlNameManager.newSystemObjectName(MODULE, SchemaObject.SCHEMA);
        DUAL_TABLE_HSQLNAME = HsqlNameManager.newSystemObjectName(DUAL, SchemaObject.SCHEMA);
        DUAL_COLUMN_HSQLNAME = HsqlNameManager.newSystemObjectName(DUMMY, SchemaObject.SCHEMA);
        SYSTEM_SUBQUERY_HSQLNAME.setSchemaIfNull(SYSTEM_SCHEMA_HSQLNAME);
    }

    public static final void checkSchemaNameNotSystem(String name) {
        if (isSystemSchemaName(name)) {
            throw Error.error(ErrorCode.X_42503, name);
        }
    }

    public static final boolean isSystemSchemaName(String name) {
        if (SqlInvariants.DEFINITION_SCHEMA.equals(name) || SqlInvariants.INFORMATION_SCHEMA.equals(name) || SqlInvariants.SYSTEM_SCHEMA.equals(name) || SqlInvariants.SQLJ_SCHEMA.equals(name)) {
            return true;
        }
        return false;
    }

    public static final boolean isLobsSchemaName(String name) {
        if (SqlInvariants.LOBS_SCHEMA.equals(name)) {
            return true;
        }
        return false;
    }

    public static final boolean isSchemaNameSystem(HsqlName name) {
        if (name.schema != null) {
            name = name.schema;
        }
        if (SqlInvariants.INFORMATION_SCHEMA_HSQLNAME.equals(name) || SqlInvariants.SYSTEM_SCHEMA_HSQLNAME.equals(name) || SqlInvariants.SQLJ_SCHEMA_HSQLNAME.equals(name)) {
            return true;
        }
        return false;
    }
}
