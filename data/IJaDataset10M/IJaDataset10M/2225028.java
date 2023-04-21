package org.aplikator.server.rpc.handler;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.aplikator.client.data.ContainerNode;
import org.aplikator.client.data.Operation;
import org.aplikator.client.data.PrimaryKey;
import org.aplikator.client.data.Record;
import org.aplikator.client.data.RecordContainer;
import org.aplikator.client.descriptor.ViewDTO;
import org.aplikator.client.rpc.impl.ProcessRecords;
import org.aplikator.client.rpc.impl.ProcessRecordsResponse;
import org.aplikator.server.Context;
import org.aplikator.server.DescriptorRegistry;
import org.aplikator.server.descriptor.Entity;
import org.aplikator.server.descriptor.View;
import org.aplikator.server.persistence.Persister;
import org.aplikator.server.persistence.PersisterFactory;
import org.aplikator.server.persistence.PersisterTriggers;
import org.aplikator.server.persistence.Transaction;
import org.aplikator.server.rpc.CommandHandler;

public class ProcessRecordsHandler implements CommandHandler<ProcessRecords, ProcessRecordsResponse> {

    private static final Logger LOG = Logger.getLogger(ProcessRecordsHandler.class.getName());

    private Persister persister = PersisterFactory.getPersister();

    public ProcessRecordsHandler() {
    }

    public ProcessRecordsResponse execute(ProcessRecords command, Context ctx) {
        RecordContainer container = command.getRecordContainer();
        Transaction tx = null;
        try {
            tx = persister.beginTransaction();
            for (ContainerNode node : container.getRecords()) {
                PersisterTriggers trigger = null;
                ViewDTO viewDTO = node.getView();
                if (viewDTO != null) {
                    View view = (View) DescriptorRegistry.get().getDescriptionItem(viewDTO.getId());
                    trigger = view.getPersisterTriggers(ctx);
                } else {
                    if (node.getOriginal() != null) {
                        Entity entity = (Entity) DescriptorRegistry.get().getDescriptionItem(node.getOriginal().getPrimaryKey().getEntityId());
                        trigger = entity.getPersisterTriggers(ctx);
                    } else if (node.getEdited() != null) {
                        Entity entity = (Entity) DescriptorRegistry.get().getDescriptionItem(node.getEdited().getPrimaryKey().getEntityId());
                        trigger = entity.getPersisterTriggers(ctx);
                    }
                }
                switch(node.getOperation()) {
                    case CREATE:
                        if (trigger != null) {
                            trigger.beforeCreate(node.getEdited(), ctx);
                        }
                        node.setEdited(persister.updateRecord(node.getEdited().getPrimaryKey().getEntityId(), node.getEdited(), tx, ctx));
                        if (trigger != null) {
                            trigger.afterCreate(node.getEdited(), ctx);
                        }
                        break;
                    case UPDATE:
                        if (trigger != null) {
                            trigger.beforeUpdate(node.getEdited(), ctx);
                        }
                        persister.updateRecord(node.getEdited().getPrimaryKey().getEntityId(), node.getEdited(), tx, ctx);
                        if (trigger != null) {
                            trigger.afterUpdate(node.getEdited(), ctx);
                        }
                        break;
                    case DELETE:
                        if (trigger != null) {
                            trigger.beforeDelete(node.getOriginal(), ctx);
                        }
                        persister.deleteRecord(node.getOriginal().getPrimaryKey().getEntityId(), node.getOriginal().getPrimaryKey().getId(), tx, ctx);
                        if (trigger != null) {
                            trigger.afterDelete(node.getOriginal(), ctx);
                        }
                        break;
                }
            }
            persister.commitTransaction(tx);
        } catch (Throwable th) {
            LOG.log(Level.SEVERE, "Error in processing records:", th);
            throw new RuntimeException("Error in processing records:", th);
        } finally {
            persister.close(tx);
        }
        for (ContainerNode cn : container.getRecords()) {
            if (cn.getOperation().equals(Operation.CREATE) || cn.getOperation().equals(Operation.UPDATE)) {
                PrimaryKey pk = cn.getEdited().getPrimaryKey();
                ViewDTO v = cn.getView();
                if (v != null) {
                    Record updated = persister.getRecord(v, pk, ctx);
                    if (cn.getOperation().equals(Operation.CREATE)) {
                        updated.getPrimaryKey().setTempId(pk.getTempId());
                    }
                    cn.setOriginal(updated);
                }
            }
        }
        return new ProcessRecordsResponse(container);
    }
}
