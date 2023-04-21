package edu.uiuc.ncsa.security.delegation.client.storage;

import edu.uiuc.ncsa.security.delegation.client.ClientTransaction;
import edu.uiuc.ncsa.security.delegation.storage.impl.TransactionMemoryStore;
import java.util.HashMap;

/**
* <p>Created by Jeff Gaynor<br>
* on 12/14/11 at  2:39 PM
*/
public class ClientTransactionMemoryStore<V extends ClientTransaction> extends TransactionMemoryStore<V> implements ClientTransactionStore<V> {

    HashMap<String, V> idIndex = new HashMap<String, V>();

    public ClientTransactionMemoryStore() {
        super();
    }

    void updateClientKeyIndex(V t) {
        if (t.getClientKey() == null) return;
        if (!idIndex.containsKey(t.getClientKey())) {
            idIndex.put(t.getClientKey(), t);
        }
    }

    @Override
    public void save(V t) {
        super.save(t);
        updateClientKeyIndex(t);
    }

    @Override
    public void update(V t) {
        super.update(t);
        updateClientKeyIndex((V) t);
    }

    @Override
    public void register(V transaction) {
        super.register(transaction);
        updateClientKeyIndex((V) transaction);
    }

    public V getByClientKey(String identifier) {
        return idIndex.get(identifier);
    }

    public V create() {
        return (V) new ClientTransaction();
    }
}
