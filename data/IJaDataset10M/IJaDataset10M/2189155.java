package dbixx;

import java.util.Map;

public interface Persistent {

    public Persistent newInstance(Map map);

    public Map getAttributes(String currentTable, Search updateCriteria);
}
