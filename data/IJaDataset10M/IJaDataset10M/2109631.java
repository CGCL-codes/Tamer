package com.avaje.ebean.server.transaction;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import com.avaje.ebean.internal.TransactionEventTable;

/**
 * Holds information for a transaction that is sent around the cluster.
 * <p>
 * Holds a collection of RemoteBeanPersist objects used to notify
 * BeanPersistListeners and potentially a TransactionEventTable.
 * </p>
 */
public final class RemoteTransactionEvent implements Serializable {

    private static final long serialVersionUID = 5790053761599631177L;

    private ArrayList<RemoteBeanPersist> list;

    private TransactionEventTable tableEvents;

    public RemoteTransactionEvent() {
    }

    /**
	 * Return true if this has some bean persist or table events.
	 */
    public boolean hasEvents() {
        return (list != null && !list.isEmpty()) || tableEvents != null;
    }

    /**
	 * Set the table events.
	 */
    public void setTableEvents(TransactionEventTable tableEvents) {
        this.tableEvents = tableEvents;
    }

    /**
	 * Return the table events if there where any.
	 */
    public TransactionEventTable getTableEvents() {
        return tableEvents;
    }

    /**
	 * Add a Insert Update or Delete payload.
	 */
    public void add(RemoteBeanPersist payload) {
        if (list == null) {
            list = new ArrayList<RemoteBeanPersist>();
        }
        list.add(payload);
    }

    /**
	 * Return the list of RemoteListenerPayload.
	 */
    public List<RemoteBeanPersist> getBeanPersistList() {
        return list;
    }
}
