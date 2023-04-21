package org.kenict.repository.manager;

import java.util.List;
import org.kenict.repository.Item;
import org.kenict.repository.RepositoryException;

/**
 * The item manager allows access to the virtual repository of the user 
 *  
 * @author lennaert
 */
public interface ItemManager {

    /**
	 * stores an item in the repository
	 * 
	 * Storing an item always creates a new version. An existing version cannot be modified. 
	 * Set the objectId to null to store a new object.
	 * 
	 * The object will be modified
	 * 
	 * @param item
	 */
    void store(Item item) throws RepositoryException;

    /**
	 * loads the most recent item by object id from the repository
	 * 
	 * @param objectId
	 * @return item
	 * @throws ResourceManagerException
	 */
    Item load(String objectId) throws RepositoryException;

    /**
	 * loads a specific item by object id ad version id from the repository
	 * 
	 * @param objectId
	 * @return item
	 * @throws ResourceManagerException
	 */
    Item load(String objectId, String versionId) throws RepositoryException;

    /**
	 * deletes an item from the repository
	 * 
	 * @param objectId
	 * @throws ResourceManagerException
	 */
    void delete(String objectId) throws RepositoryException;

    /**
	 * gets the url to download the object data
	 * 
	 * the location either returns the object data or a redirect to the resource
	 * @param item
	 * @return url
	 */
    String getBrowserLocation(Item item) throws RepositoryException;

    /**
	 * executes a query on the repository
	 * @param query
	 * @return list of objectIds of objects that match the query. May be empty.
	 * @throws ResourceManagerException
	 */
    List<Item> search(String query) throws RepositoryException;

    /**
	 * returns a list of object versions of an object
	 * 
	 * the list is sorted by date. the most recent version is first in the list, the oldest version last.
	 * @param objectId
	 * @return list of versions
	 * @throws ResourceManagerException
	 */
    List<String> getVersions(String objectId) throws RepositoryException;
}
