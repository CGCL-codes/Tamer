package be.jabapage.snooker.service.administration.impl;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;
import org.apache.commons.lang.Validate;
import be.jabapage.snooker.exception.EntityNotFoundException;
import be.jabapage.snooker.exception.MultipleInstanceFoundException;
import be.jabapage.snooker.jdo.administration.Player;
import be.jabapage.snooker.service.administration.api.IPlayerService;

public class AppEnginePlayerService implements IPlayerService, Serializable {

    private static final long serialVersionUID = 276996568509289866L;

    private final PersistenceManagerFactory pmf;

    public AppEnginePlayerService(final PersistenceManagerFactory pmf) {
        Validate.notNull(pmf, "The persistence Manager factory cannot be null");
        this.pmf = pmf;
    }

    /**
	 * {@inheritDoc}
	 */
    public void store(final Player player) {
        Validate.notNull(player);
        PersistenceManager pm = pmf.getPersistenceManager();
        try {
            pm.makePersistent(player);
        } finally {
            pm.close();
        }
    }

    /**
	 * {@inheritDoc}
	 */
    public Player retrieve(final Long key) throws EntityNotFoundException {
        Validate.notNull(key, "A key should be provided.");
        PersistenceManager pm = pmf.getPersistenceManager();
        try {
            Player objectById = pm.getObjectById(Player.class, key);
            if (objectById == null) {
                throw new EntityNotFoundException();
            }
            return objectById;
        } finally {
            pm.close();
        }
    }

    /**
	 * {@inheritDoc}
	 */
    public Player retrieve(final String name) throws MultipleInstanceFoundException, EntityNotFoundException {
        Validate.notNull(name, "We cannot search any object without the name.");
        Set<Player> resultSet = search(name);
        if (resultSet.size() == 0) {
            throw new EntityNotFoundException();
        }
        if (resultSet.size() > 1) {
            throw new MultipleInstanceFoundException(String.format("search criteria : (name = %s)", name));
        }
        Iterator<Player> iterator = resultSet.iterator();
        return iterator.next();
    }

    /**
	 * {@inheritDoc}
	 */
    public void delete(final Long key) throws EntityNotFoundException {
        Validate.notNull(key, "We cannot delete an instance withour a key.");
        PersistenceManager pm = pmf.getPersistenceManager();
        try {
            Player dataInstance = pm.getObjectById(Player.class, key);
            if (dataInstance == null) {
                throw new EntityNotFoundException();
            }
            pm.deletePersistent(dataInstance);
        } finally {
            pm.close();
        }
    }

    /**
	 * {@inheritDoc}
	 */
    public Set<Player> search(final String name) {
        Validate.notNull(name, "We need to have a name.  If you want to retrieve the entire list, use the wildcard for that.");
        PersistenceManager pm = pmf.getPersistenceManager();
        Query query = pm.newQuery(Player.class);
        query.setFilter("name == nameParam");
        query.declareParameters("String nameParam");
        Set<Player> resultSet = new HashSet<Player>();
        try {
            @SuppressWarnings("unchecked") List<Player> results = (List<Player>) query.execute(name);
            if (results != null) {
                resultSet.addAll(results);
            }
        } finally {
            query.closeAll();
            pm.close();
        }
        return resultSet;
    }

    public Set<Player> retrieveAllForClub(final Long clubId) {
        Validate.notNull(clubId, "We need to have regionId.");
        PersistenceManager pm = pmf.getPersistenceManager();
        Query query = pm.newQuery(Player.class);
        query.setFilter("clubId == clubParam");
        query.declareParameters("Long clubParam");
        Set<Player> resultSet = new HashSet<Player>();
        try {
            @SuppressWarnings("unchecked") List<Player> results = (List<Player>) query.execute(clubId);
            if (results != null) {
                resultSet.addAll(results);
            }
        } finally {
            query.closeAll();
            pm.close();
        }
        return resultSet;
    }

    public Set<Player> retrieveAllForTeam(final Long teamId) {
        PersistenceManager pm = pmf.getPersistenceManager();
        Query query = pm.newQuery(Player.class);
        query.setFilter("teamId == teamParam");
        query.declareParameters("Long teamParam");
        Set<Player> resultSet = new HashSet<Player>();
        try {
            @SuppressWarnings("unchecked") List<Player> results = (List<Player>) query.execute(teamId);
            if (results != null) {
                resultSet.addAll(results);
            }
        } finally {
            query.closeAll();
            pm.close();
        }
        return resultSet;
    }
}
