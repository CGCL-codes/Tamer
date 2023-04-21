package net.jalisq.types;

import java.util.LinkedList;
import java.util.List;

public class PhysicalColumn<Type> extends AliasableColumn<Type> implements Aliasable {

    private String name;

    private Table table;

    public PhysicalColumn(String name, Class<Type> type, Table table) {
        super(table.getSchema() + "." + table.getName() + "." + name, type);
        this.name = name;
        this.table = table;
    }

    @Override
    public List<Table> getInvolvedTables() {
        return new LinkedList<Table>() {

            {
                add(table);
            }
        };
    }

    public String getName() {
        return name;
    }

    public String getSchema() {
        return table.getSchema();
    }

    public Table getTable() {
        return table;
    }
}
