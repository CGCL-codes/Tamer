package org.nakedobjects.nof.persist.objectstore;

import java.util.Enumeration;
import java.util.Vector;
import org.apache.log4j.Logger;
import org.nakedobjects.noa.adapter.NakedObject;
import org.nakedobjects.nof.core.persist.TransactionException;
import org.nakedobjects.nof.core.util.ToString;
import org.nakedobjects.nof.persist.transaction.CreateObjectCommand;
import org.nakedobjects.nof.persist.transaction.DestroyObjectCommand;
import org.nakedobjects.nof.persist.transaction.PersistenceCommand;
import org.nakedobjects.nof.persist.transaction.SaveObjectCommand;
import org.nakedobjects.nof.persist.transaction.Transaction;

public class ObjectStoreTransaction implements Transaction {

    private static final Logger LOG = Logger.getLogger(ObjectStoreTransaction.class);

    private final Vector commands = new Vector();

    private boolean complete;

    private final NakedObjectStore objectStore;

    private final Vector toNotify = new Vector();

    public ObjectStoreTransaction(final NakedObjectStore objectStore) {
        this.objectStore = objectStore;
        LOG.debug("new transaction " + this);
    }

    public void abort() {
        LOG.info("abort transaction " + this);
        if (complete) {
            throw new TransactionException("Transaction already complete; cannot abort");
        }
        complete = true;
    }

    /**
     * Add the non-null command to the list of commands to execute at the end of the transaction.
     */
    public void addCommand(final PersistenceCommand command) {
        if (command == null) {
            return;
        }
        NakedObject onObject = command.onObject();
        if (command instanceof SaveObjectCommand) {
            if (alreadyHasCreate(onObject) || alreadyHasSave(onObject)) {
                LOG.debug("ignored command as object already created/saved" + command);
                return;
            }
            if (alreadyHasDestroy(onObject)) {
                LOG.info("ignored command " + command + " as object no longer exists");
                return;
            }
        }
        if (command instanceof DestroyObjectCommand) {
            if (alreadyHasCreate(onObject)) {
                removeCreate(onObject);
                LOG.info("ignored both create and destroy command " + command);
                return;
            }
            if (alreadyHasSave(onObject)) {
                removeSave(onObject);
                LOG.info("removed prior save command " + command);
            }
        }
        LOG.debug("add command " + command);
        commands.addElement(command);
    }

    void addNotify(NakedObject object) {
        LOG.debug("add notification for " + object);
        toNotify.addElement(object);
    }

    private boolean alreadyHasCommand(final Class commandClass, final NakedObject onObject) {
        return getCommand(commandClass, onObject) != null;
    }

    private boolean alreadyHasCreate(final NakedObject onObject) {
        return alreadyHasCommand(CreateObjectCommand.class, onObject);
    }

    private boolean alreadyHasDestroy(final NakedObject onObject) {
        return alreadyHasCommand(DestroyObjectCommand.class, onObject);
    }

    private boolean alreadyHasSave(final NakedObject onObject) {
        return alreadyHasCommand(SaveObjectCommand.class, onObject);
    }

    public void commit() {
        LOG.info("commit transaction " + this);
        if (complete) {
            throw new TransactionException("Transaction already complete; cannot commit");
        }
        objectStore.endTransaction();
        PersistenceCommand[] commandsArray = new PersistenceCommand[commands.size()];
        commands.copyInto(commandsArray);
        if (commandsArray.length > 0) {
            objectStore.execute(commandsArray);
        }
        complete = true;
    }

    public boolean flush() {
        LOG.info("flush transaction " + this);
        PersistenceCommand[] commandsArray = new PersistenceCommand[commands.size()];
        commands.copyInto(commandsArray);
        if (commandsArray.length > 0) {
            removeAllCommands();
            return objectStore.flush(commandsArray);
        }
        return false;
    }

    private PersistenceCommand getCommand(final Class commandClass, final NakedObject onObject) {
        for (Enumeration e = commands.elements(); e.hasMoreElements(); ) {
            PersistenceCommand command = (PersistenceCommand) e.nextElement();
            if (command.onObject().equals(onObject)) {
                if (commandClass.isAssignableFrom(command.getClass())) {
                    return command;
                }
            }
        }
        return null;
    }

    private void removeAllCommands() {
        commands.removeAllElements();
    }

    private void removeCommand(final Class commandClass, final NakedObject onObject) {
        PersistenceCommand toDelete = getCommand(commandClass, onObject);
        commands.removeElement(toDelete);
    }

    private void removeCreate(final NakedObject onObject) {
        removeCommand(CreateObjectCommand.class, onObject);
    }

    private void removeSave(final NakedObject onObject) {
        removeCommand(SaveObjectCommand.class, onObject);
    }

    public String toString() {
        ToString str = new ToString(this);
        str.append("complete", complete);
        str.append("commands", commands.size());
        return str.toString();
    }
}
