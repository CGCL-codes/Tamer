package ch.fork.AdHocRailway.domain.turnouts;

import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import ch.fork.AdHocRailway.domain.HibernatePersistence;
import ch.fork.AdHocRailway.domain.routes.Route;
import ch.fork.AdHocRailway.domain.routes.RouteItem;
import com.jgoodies.binding.list.ArrayListModel;
import de.dermoba.srcp.model.turnouts.SRCPTurnoutTypes;

public class HibernateTurnoutPersistence extends CachingTurnoutPersistence implements TurnoutPersistenceIface {

    static Logger logger = Logger.getLogger(HibernateTurnoutPersistence.class);

    private static TurnoutPersistenceIface instance;

    private HibernateTurnoutPersistence() {
        logger.info("HibernateTurnoutPersistence loaded");
        updateTurnoutTypeCache();
        updateTurnoutCache();
        updateTurnoutGroupCache();
    }

    public static TurnoutPersistenceIface getInstance() {
        if (instance == null) {
            instance = new HibernateTurnoutPersistence();
        }
        return instance;
    }

    private void updateTurnoutTypeCache() {
        for (TurnoutType type : getAllTurnoutTypesDB()) {
            super.addTurnoutType(type);
        }
    }

    private void updateTurnoutCache() {
        SortedSet<Turnout> turnouts = getAllTurnoutsDB();
        for (Turnout t : turnouts) {
            super.addTurnout(t);
        }
    }

    private void updateTurnoutGroupCache() {
        for (TurnoutGroup group : getAllTurnoutGroupsDB()) {
            super.addTurnoutGroup(group);
        }
    }

    public void clear() throws TurnoutPersistenceException {
        logger.debug("clear()");
        EntityManager em = HibernatePersistence.getEntityManager();
        try {
            em.createNativeQuery("TRUNCATE TABLE turnout").executeUpdate();
            em.createNativeQuery("TRUNCATE TABLE turnout_group").executeUpdate();
            super.clear();
            em.getTransaction().commit();
            HibernatePersistence.disconnect();
            HibernatePersistence.connect();
            updateTurnoutTypeCache();
        } catch (HibernateException x) {
            em.close();
            HibernatePersistence.connect();
            throw new TurnoutPersistenceException("Database Error", x);
        } catch (PersistenceException x) {
            em.close();
            HibernatePersistence.connect();
            throw new TurnoutPersistenceException("Database Error", x);
        }
    }

    public ArrayListModel<Turnout> getAllTurnouts() throws TurnoutPersistenceException {
        if (super.getAllTurnouts().size() == 0) {
            updateTurnoutCache();
        }
        return super.getAllTurnouts();
    }

    @SuppressWarnings("unchecked")
    private SortedSet<Turnout> getAllTurnoutsDB() throws TurnoutPersistenceException {
        logger.debug("getAllTurnoutsDB()");
        EntityManager em = HibernatePersistence.getEntityManager();
        try {
            List turnouts = em.createQuery("from Turnout").getResultList();
            SortedSet<Turnout> res = new TreeSet<Turnout>(turnouts);
            return res;
        } catch (HibernateException x) {
            em.close();
            HibernatePersistence.connect();
            throw new TurnoutPersistenceException("Database Error", x);
        } catch (PersistenceException x) {
            em.close();
            HibernatePersistence.connect();
            throw new TurnoutPersistenceException("Database Error", x);
        }
    }

    public Turnout getTurnoutByNumber(int number) {
        logger.debug("getTurnoutByNumber()");
        return super.getTurnoutByNumber(number);
    }

    public Turnout getTurnoutByAddressBus(int bus, int address) {
        logger.debug("getTurnoutByAddressBus()");
        return super.getTurnoutByAddressBus(bus, address);
    }

    public void addTurnout(Turnout turnout) throws TurnoutPersistenceException {
        logger.debug("addTurnout()");
        EntityManager em = HibernatePersistence.getEntityManager();
        if (turnout.getTurnoutGroup() == null) {
            throw new TurnoutPersistenceException("Turnout has no associated Group");
        }
        turnout.getTurnoutGroup().getTurnouts().add(turnout);
        try {
            em.persist(turnout);
            HibernatePersistence.flush();
            super.addTurnout(turnout);
        } catch (HibernateException x) {
            em.close();
            HibernatePersistence.connect();
            throw new TurnoutPersistenceException("Database Error", x);
        } catch (PersistenceException x) {
            em.close();
            HibernatePersistence.connect();
            throw new TurnoutPersistenceException("Database Error", x);
        }
    }

    public void deleteTurnout(Turnout turnout) throws TurnoutPersistenceException {
        logger.debug("deleteTurnout()");
        EntityManager em = HibernatePersistence.getEntityManager();
        TurnoutGroup group = turnout.getTurnoutGroup();
        group.getTurnouts().remove(turnout);
        TurnoutType type = turnout.getTurnoutType();
        type.getTurnouts().remove(turnout);
        Set<RouteItem> routeItems = turnout.getRouteItems();
        for (RouteItem ri : routeItems) {
            Route route = ri.getRoute();
            route.getRouteItems().remove(ri);
            em.remove(ri);
        }
        try {
            em.remove(turnout);
            HibernatePersistence.flush();
            super.deleteTurnout(turnout);
        } catch (HibernateException x) {
            em.close();
            HibernatePersistence.connect();
            throw new TurnoutPersistenceException("Database Error", x);
        } catch (PersistenceException x) {
            em.close();
            HibernatePersistence.connect();
            throw new TurnoutPersistenceException("Database Error", x);
        }
    }

    public void updateTurnout(Turnout turnout) throws TurnoutPersistenceException {
        logger.debug("updateTurnout()");
        EntityManager em = HibernatePersistence.getEntityManager();
        try {
            HibernatePersistence.flush();
            super.updateTurnout(turnout);
        } catch (HibernateException x) {
            em.close();
            HibernatePersistence.connect();
            throw new TurnoutPersistenceException("Database Error", x);
        } catch (PersistenceException x) {
            em.close();
            HibernatePersistence.connect();
            throw new TurnoutPersistenceException("Database Error", x);
        }
    }

    public ArrayListModel<TurnoutGroup> getAllTurnoutGroups() {
        if (super.getAllTurnoutGroups().isEmpty()) {
            updateTurnoutGroupCache();
        }
        return super.getAllTurnoutGroups();
    }

    @SuppressWarnings("unchecked")
    public SortedSet<TurnoutGroup> getAllTurnoutGroupsDB() throws TurnoutPersistenceException {
        logger.debug("getAllTurnoutGroups()");
        EntityManager em = HibernatePersistence.getEntityManager();
        try {
            List<TurnoutGroup> groups = em.createQuery("from TurnoutGroup").getResultList();
            em.getTransaction().commit();
            em.getTransaction().begin();
            return new TreeSet<TurnoutGroup>(groups);
        } catch (HibernateException x) {
            em.close();
            HibernatePersistence.connect();
            throw new TurnoutPersistenceException("Database Error", x);
        } catch (PersistenceException x) {
            em.close();
            HibernatePersistence.connect();
            throw new TurnoutPersistenceException("Database Error", x);
        }
    }

    public TurnoutGroup getTurnoutGroupByName(String name) {
        logger.debug("getTurnoutGroupByName()");
        return super.getTurnoutGroupByName(name);
    }

    public void addTurnoutGroup(TurnoutGroup group) throws TurnoutPersistenceException {
        logger.debug("addTurnoutGroup()");
        EntityManager em = HibernatePersistence.getEntityManager();
        try {
            em.persist(group);
            HibernatePersistence.flush();
            super.addTurnoutGroup(group);
        } catch (HibernateException x) {
            em.close();
            HibernatePersistence.connect();
            throw new TurnoutPersistenceException("Database Error", x);
        } catch (PersistenceException x) {
            em.close();
            HibernatePersistence.connect();
            throw new TurnoutPersistenceException("Database Error", x);
        }
    }

    public void deleteTurnoutGroup(TurnoutGroup group) throws TurnoutPersistenceException {
        logger.debug("deleteTurnoutGroup()");
        EntityManager em = HibernatePersistence.getEntityManager();
        if (!group.getTurnouts().isEmpty()) {
            SortedSet<Turnout> turnouts = new TreeSet<Turnout>(group.getTurnouts());
            for (Turnout turnout : turnouts) {
                deleteTurnout(turnout);
            }
        }
        try {
            em.remove(group);
            HibernatePersistence.flush();
            super.deleteTurnoutGroup(group);
        } catch (HibernateException x) {
            em.close();
            HibernatePersistence.connect();
            throw new TurnoutPersistenceException("Database Error", x);
        } catch (PersistenceException x) {
            em.close();
            HibernatePersistence.connect();
            throw new TurnoutPersistenceException("Database Error", x);
        }
    }

    public void updateTurnoutGroup(TurnoutGroup group) throws TurnoutPersistenceException {
        logger.debug("updateTurnoutGroup()");
        EntityManager em = HibernatePersistence.getEntityManager();
        try {
            em.merge(group);
            HibernatePersistence.flush();
            super.updateTurnoutGroup(group);
        } catch (HibernateException x) {
            em.close();
            HibernatePersistence.connect();
            throw new TurnoutPersistenceException("Database Error", x);
        } catch (PersistenceException x) {
            em.close();
            HibernatePersistence.connect();
            throw new TurnoutPersistenceException("Database Error", x);
        }
    }

    public SortedSet<TurnoutType> getAllTurnoutTypes() throws TurnoutPersistenceException {
        logger.debug("getAllTurnoutTypes()");
        return super.getAllTurnoutTypes();
    }

    @SuppressWarnings("unchecked")
    private SortedSet<TurnoutType> getAllTurnoutTypesDB() {
        logger.debug("getAllTurnoutTypessDB()");
        EntityManager em = HibernatePersistence.getEntityManager();
        try {
            List turnoutTypes = em.createQuery("from TurnoutType").getResultList();
            SortedSet<TurnoutType> res = new TreeSet<TurnoutType>(turnoutTypes);
            return res;
        } catch (HibernateException x) {
            em.close();
            HibernatePersistence.connect();
            throw new TurnoutPersistenceException("Database Error", x);
        } catch (PersistenceException x) {
            em.close();
            HibernatePersistence.connect();
            throw new TurnoutPersistenceException("Database Error", x);
        }
    }

    public TurnoutType getTurnoutType(SRCPTurnoutTypes typeName) throws TurnoutPersistenceException {
        logger.debug("getTurnoutType()");
        return super.getTurnoutType(typeName);
    }

    public void addTurnoutType(TurnoutType type) throws TurnoutPersistenceException {
        logger.debug("addTurnoutType()");
        EntityManager em = HibernatePersistence.getEntityManager();
        try {
            if (getTurnoutType(type.getTurnoutTypeEnum()) == null) {
                em.persist(type);
                HibernatePersistence.flush();
                super.addTurnoutType(type);
            }
        } catch (HibernateException x) {
            em.close();
            HibernatePersistence.connect();
            throw new TurnoutPersistenceException("Database Error", x);
        } catch (PersistenceException x) {
            em.close();
            HibernatePersistence.connect();
            throw new TurnoutPersistenceException("Database Error", x);
        }
    }

    public void deleteTurnoutType(TurnoutType type) throws TurnoutPersistenceException {
        logger.debug("deleteTurnoutType()");
        EntityManager em = HibernatePersistence.getEntityManager();
        if (!type.getTurnouts().isEmpty()) {
            throw new TurnoutPersistenceException("Cannot delete turnout type with associated turnouts");
        }
        try {
            em.remove(type);
            HibernatePersistence.flush();
            super.deleteTurnoutType(type);
        } catch (HibernateException x) {
            em.close();
            HibernatePersistence.connect();
            throw new TurnoutPersistenceException("Database Error", x);
        } catch (PersistenceException x) {
            em.close();
            HibernatePersistence.connect();
            throw new TurnoutPersistenceException("Database Error", x);
        }
    }

    public void enlargeTurnoutGroups() {
        logger.debug("enlargeTurnoutGroups()");
        super.enlargeTurnoutGroups();
        HibernatePersistence.flush();
    }

    public void flush() {
        HibernatePersistence.flush();
    }

    public void reload() {
        super.clear();
        updateTurnoutTypeCache();
        logger.info("HibernateTurnoutPersistence reloaded");
    }
}
