package storm.description.table;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TableManager {

    Map<String, TableDescription> tables = new HashMap<String, TableDescription>();

    public void eraseAll() {
        tables.clear();
    }

    public void addTables(Collection<TableDescription> defs) {
        for (TableDescription tableDescription : defs) {
            tables.put(tableDescription.getType(), tableDescription);
        }
    }

    public void removeTable(TableDescription tableDescriptor) {
        tables.remove(tableDescriptor.getType());
    }

    public void removeTable(String name) {
        tables.remove(name);
    }

    public void addTable(TableDescription tableDescriptor) {
        tables.put(tableDescriptor.getType(), tableDescriptor);
    }

    public TableDescription getTable(String name) {
        return tables.get(name);
    }

    public Collection<TableDescription> getTables() {
        return tables.values();
    }

    public void list() {
        Collection<TableDescription> tableDefs = tables.values();
        for (TableDescription tableDescription : tableDefs) {
            tableDescription.list();
        }
    }
}
