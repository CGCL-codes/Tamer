package uncertain.testcase.dbsample;

import java.util.*;

public class Database {

    String name;

    HashMap table_map = new HashMap();

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    public void addTable(Table t) {
        table_map.put(t.getName(), t);
    }

    public Table getTable(String name) {
        return (Table) table_map.get(name);
    }

    public Collection getTables() {
        return table_map.values();
    }
}
