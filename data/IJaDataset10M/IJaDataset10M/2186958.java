package net.sourceforge.squirrel_sql.plugins.db2;

import net.sourceforge.squirrel_sql.fw.sql.DatabaseObjectType;

/**
 * Some types that are created by the Informix plugin.
 * @author manningr
 *
 */
public interface IObjectTypes {

    DatabaseObjectType TRIGGER_PARENT = DatabaseObjectType.createNewDatabaseObjectType("Trigger");

    DatabaseObjectType VIEW_PARENT = DatabaseObjectType.createNewDatabaseObjectType("View");

    DatabaseObjectType INDEX_PARENT = DatabaseObjectType.createNewDatabaseObjectType("Indices");

    DatabaseObjectType SEQUENCE_PARENT = DatabaseObjectType.createNewDatabaseObjectType("Sequences");
}
