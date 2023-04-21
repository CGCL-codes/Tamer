package uk.co.fortunecookie.timesheet.data.daos;

import java.util.List;
import uk.co.patrickvankann.commons.data.paging.Page;
import uk.co.fortunecookie.timesheet.data.entities.Client;

public interface ClientDao {

    /**
	 * Get the Client with the specified identity
	 * Note: returns null if no matching identity found
	 * @param clientId the value of the identity
	 */
    public abstract Client getClient(java.lang.Integer clientId);

    /**
	 * Load the Client with the specified identity
	 * Note: throws unchecked DaoException 
	 * if entity with matching identity not found
	 * @param clientId the value of the identity
     * @throws DaoException if entity 
     * with matching identity not found
	 */
    public abstract Client loadClient(java.lang.Integer clientId);

    /**
	 * Retrieve all Client objects
	 * 
	 * @return List<Client>
	 * @return all Client objects in the database. 
	 */
    public abstract List<Client> findAllClients();

    /**
	 * Retrieve all Client objects matching the parameterless query
	 * @param query the name of the query to execute
	 * @return List<Client>
	 * @return all Client objects in the database matching the query 
	 */
    public abstract List<Client> findClientsByQuery(String query);

    /**
	 * Retrieve all Client objects matching the query with parameters specified in the value bean
	 * @param query the name of the query to execute
	 * @param valueBean a JavaBean containing the values of the parameters in the query
	 * @return List<Client>
	 * @return all Client objects in the database matching the parameters specified in the query 
	 */
    public abstract List<Client> findClientsByQuery(String query, Object valueBean);

    /**
	 * Retrieve all Client objects matching the query with parameters specified in the value bean
	 * @param query the name of the query to execute
	 * @param parameters an object array containing the values of the parameters in the query
	 * @return List<Client>
	 * @return all Client objects in the database matching the parameters specified in the query 
	 */
    public abstract List<Client> findClientsByQuery(String query, Object[] parameters);

    /**
     * Retrieve all Client objects matching the supplied example Client entity
     * @param example the example Client entity
     * @return all Client objects in the database matching the example 
     */
    public abstract List<Client> findClientsByExample(Client example);

    /**
	 * Retrieve all Client objects in pages
	 * @param page the page number of the data to request
	 * @param pageSize the size of the data page to return
	 * @return Page<Client>
	 * @return a page of Client objects
	 */
    public abstract Page<Client> findAllClients(Integer page, Integer pageSize);

    /**
	 * Retrieve all Client objects matching the query (no parameters)
	 * @param query the data query to execute
	 * @param countQuery the count query to execute
	 * @param page the page number of the data to request
	 * @param pageSize the size of the data page to return
	 * @return Page<Client>
	 * @return a page of Client objects in the database matching the query 
	 */
    public abstract Page<Client> findClientsByQuery(String query, String countQuery, Integer page, Integer pageSize);

    /**
	 * Retrieve all Client objects matching the query with parameters specified in the object array
	 * @param query the data query to execute
	 * @param countQuery the count query to execute
	 * @param parameters an object array containing the values of the parameters in the query
	 * @param page the page number of the data to request
	 * @param pageSize the size of the data page to return
	 * @return Page<Client>
	 * @return a page of Client objects in the database matching the parameters specified in the query 
	 */
    public abstract Page<Client> findClientsByQuery(String query, String countQuery, Object[] parameters, Integer page, Integer pageSize);

    /**
	 * Retrieve all Client objects matching the query with parameters specified in the value bean
	 * @param queryName the name of the query to execute
	 * @param countQueryName the name of the query to execute to return the count
	 * @param valueBean a JavaBean containing the values of the parameters in the query
	 * @param page the page number of the data to request
	 * @param pageSize the size of the data page to return
	 * @return Page<Client>
	 * @return a page of Client objects in the database matching the parameters specified in the query 
	 */
    public abstract Page<Client> findClientsByNamedQuery(String queryName, String countQueryName, Object valueBean, Integer page, Integer pageSize);

    /**
	 * Update a supplied Client loaded in an existing transaction
	 * @param client the Client to update
	 */
    public abstract void saveClient(Client client);

    /**
	 * Update a supplied Client loaded in a separate transaction
	 * @param client the Client to update
	 */
    public abstract void updateClient(Client client);

    /**
	 * Create a supplied Client object
	 * @param client the Client to create
	 */
    public abstract void createClient(Client client);

    /**
	 * Remove the Client with the specified identity
	 * @param clientId the value of the identity
	 */
    public abstract void removeClient(java.lang.Integer clientId);

    /**
	 * Remove all Clients from the database
	 */
    public abstract void removeAllClients();
}
