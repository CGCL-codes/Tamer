package net.sourceforge.squirrel_sql.client.plugin;

import java.sql.SQLException;
import java.sql.Statement;
import net.sourceforge.squirrel_sql.fw.sql.SQLConnection;
import net.sourceforge.squirrel_sql.client.session.ISession;

/**
 * Describes a type of object in the database.
 */
public class DefaultPluginDatabaseObjectType implements IPluginDatabaseObjectType {

    private final String _name;

    public DefaultPluginDatabaseObjectType(String name) {
        if (name == null || name.trim().length() == 0) {
            throw new IllegalArgumentException("Null or empty object type name");
        }
        _name = name;
    }

    /** Name. E.G. Trigger */
    public String getName() {
        return _name;
    }

    /**
     * Return all the objects in the database for the passed type.
     */
    public IPluginDatabaseObject[] getObjects(ISession session, SQLConnection conn, Statement stmt) throws SQLException {
        return null;
    }

    public IPluginDatabaseObjectPanelWrapper createPanel() {
        return null;
    }
}
