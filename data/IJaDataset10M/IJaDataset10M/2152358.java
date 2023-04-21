package se.slackers.locality.dao;

import java.util.List;
import se.slackers.locality.model.MetaTag;

public interface MetaTagDao {

    /**
	 * Removes the metatag from the database and deletes all references to it.
	 * @param tag
	 */
    public void delete(MetaTag tag);

    /**
	 * Find a metatag by the metatag id. If no metatag was found a DataRetrievalFailureException is thrown.
	 * 
	 * @param id
	 * @return
	 */
    public MetaTag get(Long id);

    /**
	 * Find a metatag by the name of the metatag. If no metatag was found a DataRetrievalFailureException is thrown.
	 * 
	 * @param name
	 * @return
	 */
    public MetaTag get(String name);

    /**
	 * Searches for metatags that have similar names to the given string. Before the search the name is converted to
	 * lower case.
	 * 
	 * @param name
	 * @return
	 */
    public List<MetaTag> getLike(String name);

    /**
	 * Saves or updates the metatag.
	 * 
	 * @param metatag
	 */
    public void save(MetaTag metatag);
}
