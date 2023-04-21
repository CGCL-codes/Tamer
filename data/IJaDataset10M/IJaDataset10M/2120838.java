package cat.jm.languages.dao.impl;

import cat.jm.languages.model.Past;
import cat.jm.languages.dao.PastDao;
import cat.jm.languages.dao.generic.GenericDao;
import org.springframework.stereotype.Repository;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An class that provides a data management to the Past table.
 */
@Repository(value = "pastDao")
public class PastDaoImpl extends GenericDao implements PastDao {

    private Class PERSISTENCE_CLASS = Past.class;

    private static final Logger logger = LoggerFactory.getLogger(PastDaoImpl.class);

    /**
	 * The default constructor class
	 */
    public PastDaoImpl() {
        setClazz(PERSISTENCE_CLASS);
    }

    /**
	 * Get a Past Object resource by id.
	 *
	 * @param id The data object to create.
     * @return Past Object instance
	 */
    public Past find(Integer id) {
        if (id == null) throw new IllegalArgumentException("Id can not be null");
        return (Past) super.find(id);
    }

    /**
	 * Create a Past resource.
	 *
	 * @param past The data object.
	 */
    public void create(Past past) {
        if (past == null) throw new IllegalArgumentException("Past can not be null");
        super.create(past);
    }

    /**
	 * Delete a Past resource.
	 *
	 * @param past The data object.
	 */
    public void remove(Past past) {
        if (past == null) throw new IllegalArgumentException("Past can not be null");
        super.remove(past);
    }

    /**
	 * Delete a Past resource by Id.
	 *
	 * @param id The id data object.
	 */
    public void remove(Integer id) {
        if (id == null) throw new IllegalArgumentException("Past Id can not be null");
        Past past = find(id);
        if (past != null) super.remove(past); else throw new IllegalArgumentException("Past Id not exists");
    }

    /**
	 * Update a Past resource.
	 *
	 * @param past The data object.
	 */
    public void update(Past past) {
        if (past == null) throw new IllegalArgumentException("Past can not be null");
        if (find(past.getId()) == null) throw new IllegalArgumentException("Past doesn't exists.");
        super.update(past);
    }

    /**
	 * Search a Past list resource.
     * @return A Past list resource
	 */
    public List<Past> get() {
        return this.findByNamedQuery("Past.getAll");
    }

    /**
	 * Search a Past list resource.
	 *
	 * @param index The page number.
     * @param maxResults The max results for page.
     * @return A Past list resource
	 */
    public List<Past> get(Integer index, Integer maxResults) {
        return this.findByNamedQueryPaginated("Past.getAll", index, maxResults);
    }
}
