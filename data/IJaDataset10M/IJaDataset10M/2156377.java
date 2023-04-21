package cat.jm.languages.dao.impl;

import cat.jm.languages.model.Future;
import cat.jm.languages.dao.FutureDao;
import cat.jm.languages.dao.generic.GenericDao;
import org.springframework.stereotype.Repository;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An class that provides a data management to the Future table.
 */
@Repository(value = "futureDao")
public class FutureDaoImpl extends GenericDao implements FutureDao {

    private Class PERSISTENCE_CLASS = Future.class;

    private static final Logger logger = LoggerFactory.getLogger(FutureDaoImpl.class);

    /**
	 * The default constructor class
	 */
    public FutureDaoImpl() {
        setClazz(PERSISTENCE_CLASS);
    }

    /**
	 * Get a Future Object resource by id.
	 *
	 * @param id The data object to create.
     * @return Future Object instance
	 */
    public Future find(Integer id) {
        if (id == null) throw new IllegalArgumentException("Id can not be null");
        return (Future) super.find(id);
    }

    /**
	 * Create a Future resource.
	 *
	 * @param future The data object.
	 */
    public void create(Future future) {
        if (future == null) throw new IllegalArgumentException("Future can not be null");
        super.create(future);
    }

    /**
	 * Delete a Future resource.
	 *
	 * @param future The data object.
	 */
    public void remove(Future future) {
        if (future == null) throw new IllegalArgumentException("Future can not be null");
        super.remove(future);
    }

    /**
	 * Delete a Future resource by Id.
	 *
	 * @param id The id data object.
	 */
    public void remove(Integer id) {
        if (id == null) throw new IllegalArgumentException("Future Id can not be null");
        Future future = find(id);
        if (future != null) super.remove(future); else throw new IllegalArgumentException("Future Id not exists");
    }

    /**
	 * Update a Future resource.
	 *
	 * @param future The data object.
	 */
    public void update(Future future) {
        if (future == null) throw new IllegalArgumentException("Future can not be null");
        if (find(future.getId()) == null) throw new IllegalArgumentException("Future doesn't exists.");
        super.update(future);
    }

    /**
	 * Search a Future list resource.
     * @return A Future list resource
	 */
    public List<Future> get() {
        return this.findByNamedQuery("Future.getAll");
    }

    /**
	 * Search a Future list resource.
	 *
	 * @param index The page number.
     * @param maxResults The max results for page.
     * @return A Future list resource
	 */
    public List<Future> get(Integer index, Integer maxResults) {
        return this.findByNamedQueryPaginated("Future.getAll", index, maxResults);
    }
}
