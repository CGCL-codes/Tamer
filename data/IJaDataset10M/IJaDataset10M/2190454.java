package uk.co.fortunecookie.timesheet.data.daos;

import java.util.List;
import uk.co.patrickvankann.commons.data.paging.Page;
import uk.co.fortunecookie.timesheet.data.entities.Project;

public interface ProjectDao {

    /**
	 * Get the Project with the specified identity
	 * Note: returns null if no matching identity found
	 * @param projectId the value of the identity
	 */
    public abstract Project getProject(java.lang.Integer projectId);

    /**
	 * Load the Project with the specified identity
	 * Note: throws unchecked DaoException 
	 * if entity with matching identity not found
	 * @param projectId the value of the identity
     * @throws DaoException if entity 
     * with matching identity not found
	 */
    public abstract Project loadProject(java.lang.Integer projectId);

    /**
	 * Retrieve all Project objects
	 * 
	 * @return List<Project>
	 * @return all Project objects in the database. 
	 */
    public abstract List<Project> findAllProjects();

    /**
	 * Retrieve all Project objects matching the parameterless query
	 * @param query the name of the query to execute
	 * @return List<Project>
	 * @return all Project objects in the database matching the query 
	 */
    public abstract List<Project> findProjectsByQuery(String query);

    /**
	 * Retrieve all Project objects matching the query with parameters specified in the value bean
	 * @param query the name of the query to execute
	 * @param valueBean a JavaBean containing the values of the parameters in the query
	 * @return List<Project>
	 * @return all Project objects in the database matching the parameters specified in the query 
	 */
    public abstract List<Project> findProjectsByQuery(String query, Object valueBean);

    /**
	 * Retrieve all Project objects matching the query with parameters specified in the value bean
	 * @param query the name of the query to execute
	 * @param parameters an object array containing the values of the parameters in the query
	 * @return List<Project>
	 * @return all Project objects in the database matching the parameters specified in the query 
	 */
    public abstract List<Project> findProjectsByQuery(String query, Object[] parameters);

    /**
     * Retrieve all Project objects matching the supplied example Project entity
     * @param example the example Project entity
     * @return all Project objects in the database matching the example 
     */
    public abstract List<Project> findProjectsByExample(Project example);

    /**
	 * Retrieve all Project objects in pages
	 * @param page the page number of the data to request
	 * @param pageSize the size of the data page to return
	 * @return Page<Project>
	 * @return a page of Project objects
	 */
    public abstract Page<Project> findAllProjects(Integer page, Integer pageSize);

    /**
	 * Retrieve all Project objects matching the query (no parameters)
	 * @param query the data query to execute
	 * @param countQuery the count query to execute
	 * @param page the page number of the data to request
	 * @param pageSize the size of the data page to return
	 * @return Page<Project>
	 * @return a page of Project objects in the database matching the query 
	 */
    public abstract Page<Project> findProjectsByQuery(String query, String countQuery, Integer page, Integer pageSize);

    /**
	 * Retrieve all Project objects matching the query with parameters specified in the object array
	 * @param query the data query to execute
	 * @param countQuery the count query to execute
	 * @param parameters an object array containing the values of the parameters in the query
	 * @param page the page number of the data to request
	 * @param pageSize the size of the data page to return
	 * @return Page<Project>
	 * @return a page of Project objects in the database matching the parameters specified in the query 
	 */
    public abstract Page<Project> findProjectsByQuery(String query, String countQuery, Object[] parameters, Integer page, Integer pageSize);

    /**
	 * Retrieve all Project objects matching the query with parameters specified in the value bean
	 * @param queryName the name of the query to execute
	 * @param countQueryName the name of the query to execute to return the count
	 * @param valueBean a JavaBean containing the values of the parameters in the query
	 * @param page the page number of the data to request
	 * @param pageSize the size of the data page to return
	 * @return Page<Project>
	 * @return a page of Project objects in the database matching the parameters specified in the query 
	 */
    public abstract Page<Project> findProjectsByNamedQuery(String queryName, String countQueryName, Object valueBean, Integer page, Integer pageSize);

    /**
	 * Update a supplied Project loaded in an existing transaction
	 * @param project the Project to update
	 */
    public abstract void saveProject(Project project);

    /**
	 * Update a supplied Project loaded in a separate transaction
	 * @param project the Project to update
	 */
    public abstract void updateProject(Project project);

    /**
	 * Create a supplied Project object
	 * @param project the Project to create
	 */
    public abstract void createProject(Project project);

    /**
	 * Remove the Project with the specified identity
	 * @param projectId the value of the identity
	 */
    public abstract void removeProject(java.lang.Integer projectId);

    /**
	 * Remove all Projects from the database
	 */
    public abstract void removeAllProjects();
}
