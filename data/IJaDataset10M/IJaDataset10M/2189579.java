package org.photovault.replication;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import org.photovault.persistence.DAOFactory;

/**
 ChangeFactory provides a link between the change replication logic and local 
 persistence management layer. It can be used to create local persistent 
 instances from a serialized change.
 */
public class ChangeFactory<T> {

    /**
     DAO used for accessing local persistent data.
     */
    ChangeDAO<T> dao;

    /**
     Construct a new ChangeFactory
     @param dao The DAO that is used to access local persistent data
     */
    public ChangeFactory(ChangeDAO<T> dao) {
        this.dao = dao;
    }

    /**
     Reads a serialized {@link ChangeDTO} from input stream and persists the 
     {@link Change} described by it in the context associated with this factory 
     if the change is not yet known in this context.
     @param is The stream from which the change is read
     @return The read change
     @throws IOException if reading the change fails
     @throws ClassNotFoundException if the change subclass or some of its fields
     are not known.
     @throws IllegalStateException if the serialized change is somehow corrupted
     (i.e. its content does not match its UUID)
     */
    public Change<T> readChange(ObjectInputStream is) throws IOException, ClassNotFoundException {
        ChangeDTO<T> data = (ChangeDTO<T>) is.readObject();
        return createChange(data);
    }

    public void addObjectHistory(ObjectHistoryDTO<T> h) throws ClassNotFoundException, IOException {
        for (ChangeDTO<T> ch : h.getChanges()) {
            createChange(ch);
        }
    }

    /**
     Creates a local persistent change based on a DTO, if the change is not yet 
     known. The change is also added to the correct history object.
     @param data The change DTO
     @return Local persisted change object based on the DTO. If the change was
     already known, the old change is returned. Otherwise, a new Change is 
     created.
     @throws java.lang.ClassNotFoundException
     @throws java.io.IOException
     */
    private Change<T> createChange(ChangeDTO<T> data) throws ClassNotFoundException, IOException {
        UUID changeUuid = data.changeUuid;
        Change<T> existingChange = dao.findChange(changeUuid);
        if (existingChange != null) {
            return existingChange;
        }
        ObjectHistory<T> targetHistory = dao.findObjectHistory(data.targetUuid);
        Change<T> change = null;
        if (targetHistory == null) {
            try {
                Class targetClass = Class.forName(data.targetClassName);
                DAOFactory df;
                VersionedObjectEditor<T> e = new VersionedObjectEditor<T>(targetClass, data.targetUuid, null);
                T target = e.getTarget();
                dao.makePersistent(target);
                dao.flush();
                targetHistory = e.history;
                change = targetHistory.getVersion();
            } catch (InstantiationException ex) {
                throw new IOException("Cannot instantiate history of class " + data.historyClass + " for object " + data.targetUuid, ex);
            } catch (IllegalAccessException ex) {
                throw new IOException("Cannot instantiate history of class " + data.historyClass + " for object " + data.targetUuid, ex);
            }
        } else {
            change = new Change<T>();
            change.setTargetHistory(targetHistory);
            change.setUuid(data.changeUuid);
            Set<Change<T>> parents = new HashSet<Change<T>>();
            for (UUID parentId : data.parentIds) {
                Change<T> parent = dao.findById(parentId, false);
                parents.add(parent);
                parent.addChildChange(change);
            }
            change.setParentChanges(parents);
            change.setChangedFields(data.changedFields);
            dao.makePersistent(change);
            dao.flush();
            targetHistory.addChange(change);
        }
        return change;
    }
}
