package org.streams.agent.file.actions.impl.db;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import org.streams.agent.file.FileTrackingStatus.STATUS;
import org.streams.agent.file.actions.FileLogActionEvent;
import org.streams.agent.file.actions.FileLogManagerMemory;
import org.streams.agent.file.actions.LastModificationTimeComparator;

/**
 * 
 * Implements the DB storage logic for the FileLogManagerMemory.
 * 
 */
public class DBFileLogManagerMemory implements FileLogManagerMemory {

    EntityManagerFactory entityManagerFactory;

    public DBFileLogManagerMemory() {
    }

    public DBFileLogManagerMemory(EntityManagerFactory entityManagerFactory) {
        super();
        this.entityManagerFactory = entityManagerFactory;
    }

    public EntityManagerFactory getEntityManagerFactory() {
        return entityManagerFactory;
    }

    public void setEntityManagerFactory(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    /**
	 * Persists and assigns a database id the to event
	 */
    @Override
    public FileLogActionEvent registerEvent(FileLogActionEvent event) {
        FileLogActionEventEntity entity = FileLogActionEventEntity.createEntity(event);
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(entity);
            event.setId(entity.getId());
        } finally {
            entityManager.getTransaction().commit();
            entityManager.close();
        }
        return event;
    }

    @Override
    public void removeEvent(Long eventId) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            try {
                FileLogActionEventEntity entity = entityManager.find(FileLogActionEventEntity.class, eventId);
                if (entity != null) {
                    entityManager.remove(entity);
                }
            } catch (NoResultException noResultExcp) {
            }
        } finally {
            entityManager.getTransaction().commit();
            entityManager.close();
        }
    }

    /**
	 * fileLogActionEventEntity.byPath
	 */
    @SuppressWarnings("unchecked")
    @Override
    public SortedSet<FileLogActionEvent> listEventsForFile(String fileName) {
        SortedSet<FileLogActionEvent> statusColl = new TreeSet<FileLogActionEvent>(LastModificationTimeComparator.INSTANCE);
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            List<FileLogActionEventEntity> statusEntityList = entityManager.createNamedQuery("fileLogActionEventEntity.byPath").setParameter("path", fileName).getResultList();
            if (!(statusEntityList == null || statusEntityList.size() < 1)) {
                for (FileLogActionEventEntity entity : statusEntityList) {
                    statusColl.add(entity.createEventObject());
                }
            }
        } finally {
            entityManager.getTransaction().commit();
            entityManager.close();
        }
        return statusColl;
    }

    @Override
    public Collection<FileLogActionEvent> listEvents() {
        return listEvents(null);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Collection<FileLogActionEvent> listEvents(STATUS status) {
        Collection<FileLogActionEvent> statusColl = null;
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            List<FileLogActionEventEntity> statusEntityList = null;
            if (status == null) {
                statusEntityList = entityManager.createNamedQuery("fileLogActionEventEntity.list").getResultList();
            } else {
                statusEntityList = entityManager.createNamedQuery("fileLogActionEventEntity.byStatus").setParameter("status", status.toString().toUpperCase()).getResultList();
            }
            if (statusEntityList == null || statusEntityList.size() < 1) {
                statusColl = new ArrayList<FileLogActionEvent>();
            } else {
                int len = statusEntityList.size();
                statusColl = new ArrayList<FileLogActionEvent>(len);
                for (FileLogActionEventEntity entity : statusEntityList) {
                    statusColl.add(entity.createEventObject());
                }
            }
        } finally {
            try {
                entityManager.getTransaction().commit();
                entityManager.close();
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
        return statusColl;
    }

    /**
	 * 
	 */
    @SuppressWarnings("unchecked")
    @Override
    public Collection<FileLogActionEvent> listEventsWithDelay(int threshold) {
        SortedSet<FileLogActionEvent> statusColl = new TreeSet<FileLogActionEvent>(LastModificationTimeComparator.INSTANCE);
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            List<FileLogActionEventEntity> statusEntityList = entityManager.createNamedQuery("fileLogActionEventEntity.byDelay").setParameter("delay", threshold).getResultList();
            if (!(statusEntityList == null || statusEntityList.size() < 1)) {
                for (FileLogActionEventEntity entity : statusEntityList) {
                    statusColl.add(entity.createEventObject());
                }
            }
        } finally {
            entityManager.getTransaction().commit();
            entityManager.close();
        }
        return statusColl;
    }

    @Override
    public void close() {
    }

    /**
	 * fileLogActionEventEntity.listExpired
	 */
    @SuppressWarnings("unchecked")
    @Override
    public Collection<FileLogActionEvent> listExpiredEvents(int threshold) {
        Collection<FileLogActionEvent> statusColl = null;
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            long currentTime = System.currentTimeMillis();
            List<FileLogActionEventEntity> statusEntityList = entityManager.createNamedQuery("fileLogActionEventEntity.listExpired").setParameter("delay", threshold).setParameter("currentTime", currentTime).getResultList();
            if (!(statusEntityList == null || statusEntityList.size() < 1)) {
                statusColl = new ArrayList<FileLogActionEvent>(statusEntityList.size());
                for (FileLogActionEventEntity entity : statusEntityList) {
                    statusColl.add(entity.createEventObject());
                }
            } else {
                statusColl = new ArrayList<FileLogActionEvent>();
            }
        } finally {
            entityManager.getTransaction().commit();
            entityManager.close();
        }
        return statusColl;
    }
}
