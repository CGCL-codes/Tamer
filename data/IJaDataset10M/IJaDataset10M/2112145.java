package ru.adv.db.create;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ru.adv.db.config.ConfigObject;

/**
 * Method Object for creating and dropping constraints.
 */
class Constraints extends CreationStuff {

    Constraints(DBCreate dbc) {
        super(dbc);
    }

    void drop(Collection<ConfigObject> objects) throws DBCreateException {
        if (isLoggingEnabled()) {
            getLog().setDropConstrainsException(DBCreateLog.STARTED);
        }
        actionMessage("Dropping constraints");
        for (ConfigObject co : objects) {
            String table = co.getName();
            try {
                getDBAdapter().dropConstraints(getDBConnect(), getConfig().getSchemaName(), table);
                if (co.isTree()) {
                    getDBAdapter().dropConstraints(getDBConnect(), getConfig().getSchemaName(), co.getExtendedTreeTableName());
                }
            } catch (Exception e) {
                DBCreateException exception = new DBCreateException(DBCreateException.DBC_CANT_DROP_CONSTRAINS, "Cannot drop constrans", e, table);
                if (isLoggingEnabled()) {
                    getLog().setDropConstrainsException(exception);
                }
                throw exception;
            }
        }
        if (isLoggingEnabled()) {
            getLog().setDropConstrainsException(DBCreateLog.OK);
        }
        ok();
    }

    void create(Collection<ConfigObject> objects) throws DBCreateException {
        if (isLoggingEnabled()) {
            getLog().setCreateConstrainsException(DBCreateLog.STARTED);
        }
        actionMessage("Creating constraints");
        for (ConfigObject o : sortToSaveOrder(objects)) {
            if (o.isTable() && !o.isSystem()) {
                try {
                    if (getDBAdapter().isSupportConstraints()) {
                        getDBAdapter().createConstraints(getDBConnect(), o);
                    }
                } catch (Exception e) {
                    DBCreateException exception = new DBCreateException(DBCreateException.DBC_CANT_CREATE_CONSTRAINS, "Cannot create constrans", e, o.getName());
                    if (isLoggingEnabled()) {
                        getLog().setCreateConstrainsException(exception);
                    }
                    throw exception;
                }
            }
        }
        if (isLoggingEnabled()) {
            getLog().setCreateConstrainsException(DBCreateLog.OK);
        }
        ok();
    }

    private List<ConfigObject> sortToSaveOrder(Collection<ConfigObject> objects) {
        Map<String, ConfigObject> mapCo = new HashMap<String, ConfigObject>();
        for (ConfigObject co : objects) {
            mapCo.put(co.getRealName(), co);
        }
        List<String> orderedNames = getConfig().getSaveOrder(mapCo.keySet());
        List<ConfigObject> orderedList = new ArrayList<ConfigObject>();
        for (String name : orderedNames) {
            orderedList.add(mapCo.get(name));
        }
        return orderedList;
    }
}
