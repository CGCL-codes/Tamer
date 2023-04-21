package org.mylabnote.dao;

import java.util.List;
import javax.annotation.Resource;
import javax.ejb.*;
import javax.persistence.*;
import org.mylabnote.exception.LabNoteException;
import org.mylabnote.model.Entry;
import org.mylabnote.model.User;

@Remote
@Stateless
public class LabNoteModelImpl implements LabNoteModel {

    @PersistenceContext
    private EntityManager em;

    @Resource
    private SessionContext context;

    public LabNoteModelImpl() {
    }

    public void addEntry(Entry entry) throws LabNoteException {
        try {
            em.persist(entry);
        } catch (EntityExistsException exe) {
            throw new LabNoteException("Entry: " + entry.getId() + " already exists.");
        }
    }

    public void deleteEntry(Entry entry) throws LabNoteException {
        Entry entryToRemove = em.find(Entry.class, entry.getId());
        if (null == entryToRemove) {
            throw new LabNoteException("Entry: " + entry.getId() + " not found.");
        } else {
            em.remove(entryToRemove);
        }
    }

    public void updateEntry(Entry entry) throws LabNoteException {
        Entry entryToMerge = em.find(Entry.class, entry.getId());
        if (null == entryToMerge) {
            throw new LabNoteException("Entry: " + entry.getId() + " not found.");
        } else {
            try {
                em.merge(entryToMerge);
            } catch (OptimisticLockException ole) {
                throw new LabNoteException("Entry: " + entry.getId() + " has been modified since retrieval.");
            }
        }
    }

    public Entry getEntry(String id) throws LabNoteException {
        Entry entry = em.find(Entry.class, id);
        if (null != entry) {
            return entry;
        } else {
            throw new LabNoteException("Entry:" + id + " not found.");
        }
    }

    public Entry[] getAllEntries() throws LabNoteException {
        Query query = em.createNativeQuery("SELECT * FROM ENTRIES", Entry.class);
        List resultList = query.getResultList();
        return (Entry[]) resultList.toArray(new Entry[0]);
    }

    public void addUser(User user) throws LabNoteException {
        try {
            em.persist(user);
        } catch (EntityExistsException exe) {
            throw new LabNoteException("User: " + user.getId() + " already exists.");
        }
    }

    public void deleteUser(User user) throws LabNoteException {
        User userToRemove = em.find(User.class, user.getId());
        if (null == userToRemove) {
            throw new LabNoteException("User: " + user.getId() + " not found.");
        } else {
            em.remove(userToRemove);
        }
    }

    public void updateUser(User user) throws LabNoteException {
        User userToMerge = em.find(User.class, user.getId());
        if (null == userToMerge) {
            throw new LabNoteException("User: " + user.getId() + " not found.");
        } else {
            try {
                em.merge(user);
            } catch (OptimisticLockException ole) {
                throw new LabNoteException("User: " + user.getId() + " has been modified since retrieval.");
            }
        }
    }

    public User getUser(String id) throws LabNoteException {
        User user = em.find(User.class, id);
        if (null == user) {
            throw new LabNoteException("User:" + id + " not found.");
        } else {
            return user;
        }
    }

    public User[] getAllUsers() throws LabNoteException {
        Query query = em.createNativeQuery("SELECT * FROM USERS", User.class);
        List resultList = query.getResultList();
        return (User[]) resultList.toArray(new User[0]);
    }
}
