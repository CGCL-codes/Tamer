package net.sourceforge.myjorganizer.jpa.entities;

import javax.persistence.EntityManager;

/**
 * <p>
 * BasicDataLoader class.
 * </p>
 * 
 * @author Davide Bellettini <dbellettini@users.sourceforge.net>
 * @version $Id: BasicDataLoader.java 149 2010-11-09 09:08:41Z dbellettini $
 */
public class BasicDataLoader {

    /**
     * <p>
     * ensurePriorities
     * </p>
     * 
     * @param em
     *            a {@link javax.persistence.EntityManager} object.
     */
    public static void ensurePriorities(EntityManager em) {
        for (TaskPriority p : TaskPriority.getAll()) {
            if (null == em.find(TaskPriority.class, p)) {
                em.persist(p);
            }
        }
    }

    /**
     * <p>
     * ensureStatuses
     * </p>
     * 
     * @param em
     *            a {@link javax.persistence.EntityManager} object.
     */
    public static void ensureStatuses(EntityManager em) {
        TaskStatus[] statuses = { new TaskStatus("open"), new TaskStatus("closed"), new TaskStatus("postponed"), new TaskStatus("canceled") };
        for (TaskStatus status : statuses) {
            if (null == em.find(TaskStatus.class, status.getId())) {
                em.persist(status);
            } else {
                em.merge(status);
            }
        }
    }

    /**
     * <p>
     * ensureBasicData
     * </p>
     * 
     * @param em
     *            a {@link javax.persistence.EntityManager} object.
     */
    public static void ensureBasicData(EntityManager em) {
        ensurePriorities(em);
        ensureStatuses(em);
    }
}
