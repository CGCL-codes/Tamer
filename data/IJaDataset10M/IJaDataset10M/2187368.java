package civquest.ruleset;

import java.util.Hashtable;

public class Table {

    Hashtable data;

    public Table() {
        data = new Hashtable();
    }

    public Object get(String col, String row) {
        return data.get(col + ":" + row);
    }

    public void set(String col, String row, String val) {
        data.put(col + ":" + row, val);
    }
}
