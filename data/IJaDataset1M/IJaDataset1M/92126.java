package gpsxml.io.database;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author PLAYER, Keith Ralph
 */
public class DatabaseSchema {

    public static final String FEATURES = "features";

    public static final String GROUPS = "groups";

    public static final String PROC = "proc";

    public static final String EXPERIMENT = "experiment";

    public static final String ITEMS = "items";

    private Map<String, Table> tableMap = new HashMap<String, Table>();

    /** Creates a new instance of DatabaseSchema */
    public DatabaseSchema() {
    }

    /**
     *  Sets the table definition for the given name.
     *  @param table the table object
     */
    public void setTable(Table table) {
        tableMap.put(table.getName(), table);
    }

    public Table getFeatures() {
        return tableMap.get(FEATURES);
    }

    public Table getGroups() {
        return tableMap.get(GROUPS);
    }

    public Table getItems() {
        return tableMap.get(ITEMS);
    }

    public Table getProc() {
        return tableMap.get(PROC);
    }

    public Table getExperiment() {
        return tableMap.get(EXPERIMENT);
    }

    public Collection<Table> getAllTables() {
        return tableMap.values();
    }

    public String getDropFeatures() {
        return null;
    }
}
