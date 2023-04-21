package org.nakedobjects.runtime.objectstore.inmemory.internal.commands;

import org.apache.log4j.Logger;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.runtime.objectstore.inmemory.internal.ObjectStorePersistedObjects;
import org.nakedobjects.runtime.persistence.objectstore.transaction.CreateObjectCommand;
import org.nakedobjects.runtime.persistence.objectstore.transaction.PersistenceCommandContext;
import org.nakedobjects.runtime.transaction.ObjectPersistenceException;

public final class InMemoryCreateObjectCommand extends AbstractInMemoryPersistenceCommand implements CreateObjectCommand {

    private static final Logger LOG = Logger.getLogger(InMemoryCreateObjectCommand.class);

    public InMemoryCreateObjectCommand(NakedObject object, final ObjectStorePersistedObjects persistedObjects) {
        super(object, persistedObjects);
    }

    public void execute(final PersistenceCommandContext context) throws ObjectPersistenceException {
        if (LOG.isDebugEnabled()) {
            LOG.debug("  create object " + onObject());
        }
        save(onObject());
    }

    @Override
    public String toString() {
        return "CreateObjectCommand [object=" + onObject() + "]";
    }
}
