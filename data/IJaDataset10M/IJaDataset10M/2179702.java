package ru.adv.db.create;

import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import ru.adv.db.config.ConfigObject;
import ru.adv.db.config.DBConfigException;
import ru.adv.db.config.ExtendedTreeTable;
import ru.adv.logger.TLogger;

/**
 * 	Method Object for dropping unused objects.
 */
class LeftObjects extends CreationStuff {

    private TLogger logger = new TLogger(LeftObjects.class);

    LeftObjects(DBCreate dbc) {
        super(dbc);
    }

    void drop() throws DBCreateException {
        if (isLoggingEnabled()) {
            getLog().setDropUnusedObjectsException(DBCreateLog.STARTED);
        }
        actionMessage("Dropping unused objects");
        dropLeftObjects();
        dropLeftExTreeTables();
        if (isLoggingEnabled()) {
            getLog().setDropUnusedObjectsException(DBCreateLog.OK);
        }
        ok();
    }

    /**
     * drop tables that exists in database and not exists in DBConfig
     */
    private void dropLeftObjects() throws DBCreateException {
        Collection registeredObjects = null;
        try {
            registeredObjects = getRegisteredObjects();
        } catch (Exception e) {
            throw newException(DBCreateException.DBC_CANT_GET_LIST_OF_OBJECTS, "Cannot get list of objects", e, null);
        }
        for (Iterator i = registeredObjects.iterator(); i.hasNext(); ) {
            RegisteredObject ro = (RegisteredObject) i.next();
            try {
                if (!getConfig().containsObject(ro.getName(), false)) {
                    ro.drop();
                    continue;
                }
                if (ro.isTypeChanged()) {
                    ro.drop();
                    continue;
                }
            } catch (DBCreateException e) {
                if (isLoggingEnabled()) {
                    getLog().setDropUnusedObjectsException(e);
                }
                throw e;
            } catch (Exception e) {
                logger.logStackTrace(e);
                DBCreateException ex = newException(DBCreateException.DBC_CANT_DROP_OBJECT, "Cannot drop object", e, ro.getName());
                throw ex;
            }
        }
    }

    private DBCreateException newException(int code, String msg, Exception e, String object) {
        DBCreateException result = new DBCreateException(code, msg, e, object);
        if (getLog() != null) {
            getLog().setDropUnusedObjectsException(result);
        }
        return result;
    }

    /**
     * drop left exteded tree tables that exists in database and not exists in DBConfig for tree objects
     */
    private void dropLeftExTreeTables() throws DBCreateException {
        try {
            final Set<String> dbUndescoreTables = new HashSet<String>();
            try {
                dbUndescoreTables.addAll(getExistentTableNames(true));
            } catch (SQLException e) {
                throw newException(DBCreateException.DBC_CANT_GET_LIST_OF_OBJECTS, "Cannot get list of objects", e, null);
            }
            List<Object[]> rows = executeQuery("SELECT tablename,extreename FROM " + getDBAdapter().getExtTreeTableName());
            for (Object[] row : rows) {
                String tableName = row[0].toString();
                String exTreeTableName = row[1].toString();
                if (!getConfig().containsObject(tableName, false) || !getConfig().getConfigObject(tableName).isTree() || isBadExtreeTableStructure(getConfig().getConfigObject(tableName))) {
                    dropExtTreeTable(dbUndescoreTables, exTreeTableName, tableName);
                }
            }
        } catch (Exception e) {
            throw newCannotDropObjects(e);
        }
    }

    private DBCreateException newCannotDropObjects(Exception e) {
        return newException(DBCreateException.DBC_CANT_DROP_OBJECTS, "Cannot drop objects", e, null);
    }

    private void dropExtTreeTable(Collection dbUndescoreTables, String exTreeTableName, String tableName) throws DBCreateException {
        try {
            if (dbUndescoreTables.contains(exTreeTableName) && getDBAdapter().tableExists(getDBConnect(), exTreeTableName)) {
                execute(getDBAdapter().getDropTableSql(exTreeTableName));
            }
        } catch (SQLException e) {
            DBCreateException ex = newException(DBCreateException.DBC_CANT_DROP_EXT_TREE, "Cannot drop table", e, tableName);
            ex.setAttr("tree-object", exTreeTableName);
            throw ex;
        }
        try {
            execute("DELETE FROM " + getDBAdapter().getExtTreeTableName() + " WHERE tablename='" + tableName + "' AND extreename='" + exTreeTableName + "'");
        } catch (SQLException e) {
            DBCreateException ex = newException(DBCreateException.DBC_CANT_UNREGISTER_EXT_TREE, "Cannot unregister table", e, tableName);
            ex.setAttr("tree-object", exTreeTableName);
            throw ex;
        }
    }

    private boolean isBadExtreeTableStructure(ConfigObject cObject) throws DBConfigException, SQLException {
        return !(new ExtendedTreeTable(cObject).checkStructure(this.getDBConnect()));
    }
}
