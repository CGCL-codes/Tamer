package pool;

import java.util.Hashtable;

/**
 *
 * @author Raimon Bosch
 */
public class PoolContainer {

    private Hashtable<String, Pool> container;

    int last_id;

    public PoolContainer() {
        container = new Hashtable<String, Pool>();
        last_id = 1;
    }

    public Pool getPool(String id) {
        return container.get(id);
    }

    public void insert(Pool pool) {
        container.put("" + last_id, pool);
        last_id++;
    }
}
