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
import be.jabapage.snooker.jdo.administration.Club;
import be.jabapage.snooker.service.administration.api.IClubService;

public class AppEngineClubService implements IClubService, Serializable {

    private static final long serialVersionUID = 4736950176835749886L;

    private final PersistenceManagerFactory pmf;

    public AppEngineClubService(final PersistenceManagerFactory pmf) {
        Validate.notNull(pmf, "The persistence Manager factory cannot be null");
        this.pmf = pmf;
    }

    /**
	 * {@inheritDoc}
	 */
    public void store(final Club club) {
        Validate.notNull(club);
        PersistenceManager pm = pmf.getPersistenceManager();
        try {
            pm.makePersistent(club);
        } finally {
            pm.close();
        }
    }

    /**
	 * {@inheritDoc}
	 */
    public Club retrieve(final Long key) throws EntityNotFoundException {
        Validate.notNull(key, "A key should be provided.");
        PersistenceManager pm = pmf.getPersistenceManager();
        try {
            Club objectById = pm.getObjectById(Club.class, key);
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
    public Club retrieve(final String name) throws MultipleInstanceFoundException, EntityNotFoundException {
        Validate.notNull(name, "We cannot search any object without the name.");
        Set<Club> resultSet = search(name);
        if (resultSet.size() == 0) {
            throw new EntityNotFoundException();
        }
        if (resultSet.size() > 1) {
            throw new MultipleInstanceFoundException(String.format("search criteria : (name = %s)", name));
        }
        Iterator<Club> iterator = resultSet.iterator();
        return iterator.next();
    }

    /**
	 * {@inheritDoc}
	 */
    public void delete(final Long key) throws EntityNotFoundException {
        Validate.notNull(key, "We cannot delete an instance withour a key.");
        PersistenceManager pm = pmf.getPersistenceManager();
        try {
            Club dataInstance = pm.getObjectById(Club.class, key);
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
    public Set<Club> search(final String name) {
        Validate.notNull(name, "We need to have a name.  If you want to retrieve the entire list, use the wildcard for that.");
        PersistenceManager pm = pmf.getPersistenceManager();
        Query query = pm.newQuery(Club.class);
        query.setFilter("name == nameParam");
        query.declareParameters("String nameParam");
        Set<Club> resultSet = new HashSet<Club>();
        try {
            @SuppressWarnings("unchecked") List<Club> results = (List<Club>) query.execute(name);
            if (results != null) {
                resultSet.addAll(results);
            }
        } finally {
            query.closeAll();
            pm.close();
        }
        return resultSet;
    }

    public Set<Club> retrieveAllForRegion(final Long regionId) {
        Validate.notNull(regionId, "We need to have regionId.");
        PersistenceManager pm = pmf.getPersistenceManager();
        Query query = pm.newQuery(Club.class);
        query.setFilter("regionId == regionParam");
        query.declareParameters("Long regionParam");
        Set<Club> resultSet = new HashSet<Club>();
        try {
            @SuppressWarnings("unchecked") List<Club> results = (List<Club>) query.execute(regionId);
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
