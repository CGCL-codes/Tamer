package net.sourceforge.squirrel_sql.plugins.db2.exp;

import java.sql.SQLException;
import net.sourceforge.squirrel_sql.fw.sql.DatabaseObjectInfo;
import net.sourceforge.squirrel_sql.fw.sql.IDatabaseObjectInfo;
import net.sourceforge.squirrel_sql.fw.sql.SQLDatabaseMetaData;
import net.sourceforge.squirrel_sql.plugins.db2.IObjectTypes;

/**
 * This class stores information about an Informix Trigger parent. This just
 * stores info about the table that the trigger relates to.
 *
 * @author manningr
 */
public class TriggerParentInfo extends DatabaseObjectInfo {

    public interface IPropertyNames {

        String SIMPLE_NAME = "simpleName";

        String TABLE_INFO = "tableInfo";
    }

    private final IDatabaseObjectInfo _tableInfo;

    public TriggerParentInfo(IDatabaseObjectInfo tableInfo, String schema, SQLDatabaseMetaData md) throws SQLException {
        super(null, schema, "TRIGGER", IObjectTypes.TRIGGER_PARENT, md);
        _tableInfo = tableInfo;
    }

    public IDatabaseObjectInfo getTableInfo() {
        return _tableInfo;
    }
}
