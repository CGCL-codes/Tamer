package uk.co.fortunecookie.timesheet.data.daos;

import java.util.List;
import uk.co.patrickvankann.commons.data.paging.Page;
import uk.co.fortunecookie.timesheet.data.entities.EmployeeType;

public interface EmployeeTypeDao {

    /**
	 * Get the EmployeeType with the specified identity
	 * Note: returns null if no matching identity found
	 * @param employeeTypeId the value of the identity
	 */
    public abstract EmployeeType getEmployeeType(java.lang.Integer employeeTypeId);

    /**
	 * Load the EmployeeType with the specified identity
	 * Note: throws unchecked DaoException 
	 * if entity with matching identity not found
	 * @param employeeTypeId the value of the identity
     * @throws DaoException if entity 
     * with matching identity not found
	 */
    public abstract EmployeeType loadEmployeeType(java.lang.Integer employeeTypeId);

    /**
	 * Retrieve all EmployeeType objects
	 * 
	 * @return List<EmployeeType>
	 * @return all EmployeeType objects in the database. 
	 */
    public abstract List<EmployeeType> findAllEmployeeTypes();

    /**
	 * Retrieve all EmployeeType objects matching the parameterless query
	 * @param query the name of the query to execute
	 * @return List<EmployeeType>
	 * @return all EmployeeType objects in the database matching the query 
	 */
    public abstract List<EmployeeType> findEmployeeTypesByQuery(String query);

    /**
	 * Retrieve all EmployeeType objects matching the query with parameters specified in the value bean
	 * @param query the name of the query to execute
	 * @param valueBean a JavaBean containing the values of the parameters in the query
	 * @return List<EmployeeType>
	 * @return all EmployeeType objects in the database matching the parameters specified in the query 
	 */
    public abstract List<EmployeeType> findEmployeeTypesByQuery(String query, Object valueBean);

    /**
	 * Retrieve all EmployeeType objects matching the query with parameters specified in the value bean
	 * @param query the name of the query to execute
	 * @param parameters an object array containing the values of the parameters in the query
	 * @return List<EmployeeType>
	 * @return all EmployeeType objects in the database matching the parameters specified in the query 
	 */
    public abstract List<EmployeeType> findEmployeeTypesByQuery(String query, Object[] parameters);

    /**
     * Retrieve all EmployeeType objects matching the supplied example EmployeeType entity
     * @param example the example EmployeeType entity
     * @return all EmployeeType objects in the database matching the example 
     */
    public abstract List<EmployeeType> findEmployeeTypesByExample(EmployeeType example);

    /**
	 * Retrieve all EmployeeType objects in pages
	 * @param page the page number of the data to request
	 * @param pageSize the size of the data page to return
	 * @return Page<EmployeeType>
	 * @return a page of EmployeeType objects
	 */
    public abstract Page<EmployeeType> findAllEmployeeTypes(Integer page, Integer pageSize);

    /**
	 * Retrieve all EmployeeType objects matching the query (no parameters)
	 * @param query the data query to execute
	 * @param countQuery the count query to execute
	 * @param page the page number of the data to request
	 * @param pageSize the size of the data page to return
	 * @return Page<EmployeeType>
	 * @return a page of EmployeeType objects in the database matching the query 
	 */
    public abstract Page<EmployeeType> findEmployeeTypesByQuery(String query, String countQuery, Integer page, Integer pageSize);

    /**
	 * Retrieve all EmployeeType objects matching the query with parameters specified in the object array
	 * @param query the data query to execute
	 * @param countQuery the count query to execute
	 * @param parameters an object array containing the values of the parameters in the query
	 * @param page the page number of the data to request
	 * @param pageSize the size of the data page to return
	 * @return Page<EmployeeType>
	 * @return a page of EmployeeType objects in the database matching the parameters specified in the query 
	 */
    public abstract Page<EmployeeType> findEmployeeTypesByQuery(String query, String countQuery, Object[] parameters, Integer page, Integer pageSize);

    /**
	 * Retrieve all EmployeeType objects matching the query with parameters specified in the value bean
	 * @param queryName the name of the query to execute
	 * @param countQueryName the name of the query to execute to return the count
	 * @param valueBean a JavaBean containing the values of the parameters in the query
	 * @param page the page number of the data to request
	 * @param pageSize the size of the data page to return
	 * @return Page<EmployeeType>
	 * @return a page of EmployeeType objects in the database matching the parameters specified in the query 
	 */
    public abstract Page<EmployeeType> findEmployeeTypesByNamedQuery(String queryName, String countQueryName, Object valueBean, Integer page, Integer pageSize);

    /**
	 * Update a supplied EmployeeType loaded in an existing transaction
	 * @param employeeType the EmployeeType to update
	 */
    public abstract void saveEmployeeType(EmployeeType employeeType);

    /**
	 * Update a supplied EmployeeType loaded in a separate transaction
	 * @param employeeType the EmployeeType to update
	 */
    public abstract void updateEmployeeType(EmployeeType employeeType);

    /**
	 * Create a supplied EmployeeType object
	 * @param employeeType the EmployeeType to create
	 */
    public abstract void createEmployeeType(EmployeeType employeeType);

    /**
	 * Remove the EmployeeType with the specified identity
	 * @param employeeTypeId the value of the identity
	 */
    public abstract void removeEmployeeType(java.lang.Integer employeeTypeId);

    /**
	 * Remove all EmployeeTypes from the database
	 */
    public abstract void removeAllEmployeeTypes();
}
