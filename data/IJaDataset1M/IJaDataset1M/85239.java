package org.nakedobjects.remoting.client.transaction;

import java.util.ArrayList;
import java.util.List;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.runtime.transaction.NakedObjectTransactionAbstract;
import org.nakedobjects.runtime.transaction.NakedObjectTransactionManager;
import org.nakedobjects.runtime.transaction.messagebroker.MessageBroker;
import org.nakedobjects.runtime.transaction.updatenotifier.UpdateNotifier;

/**
 * Encapsulates a transaction occurring on the client, where each of the actions (add, remove and change) are
 * then passed to the server as an atomic unit. 
 * 
 * <p>
 * Each action is captured as an {@link ClientTransactionEvent} object.
 */
public class ClientSideTransaction extends NakedObjectTransactionAbstract {

    private final List<ClientTransactionEvent> transactionEvents = new ArrayList<ClientTransactionEvent>();

    public ClientSideTransaction(NakedObjectTransactionManager transactionManager, MessageBroker messageBroker, UpdateNotifier updateNotifier) {
        super(transactionManager, messageBroker, updateNotifier);
    }

    /**
     * Add an event to transaction for the adding of the specified object.
     */
    public void addMakePersistent(final NakedObject object) {
        add(new ClientTransactionEvent(object, ClientTransactionEvent.ADD));
    }

    /**
     * Add an event to transaction for the updating of the specified object.
     */
    public void addObjectChanged(final NakedObject object) {
        add(new ClientTransactionEvent(object, ClientTransactionEvent.CHANGE));
    }

    /**
     * Add an event to transaction for the destruction of the specified object.
     */
    public void addDestroyObject(final NakedObject object) {
        add(new ClientTransactionEvent(object, ClientTransactionEvent.DELETE));
    }

    /**
     * Return all the events for the transaction.
     */
    public ClientTransactionEvent[] getEntries() {
        return transactionEvents.toArray(new ClientTransactionEvent[] {});
    }

    /**
     * Returns true if there are no entries, hence the transaction is not used.
     */
    public boolean isEmpty() {
        return transactionEvents.size() == 0;
    }

    @Override
    protected void doFlush() {
    }

    /**
     * TODO: need to restore the state of all involved objects 
     */
    @Override
    public void doAbort() {
    }

    private void add(final ClientTransactionEvent entry) {
        if (!transactionEvents.contains(entry)) {
            transactionEvents.add(entry);
        }
    }
}
