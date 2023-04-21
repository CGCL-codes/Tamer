package org.extwind.osgi.repository;

/**
 * @author donf.yang
 * 
 */
public interface RepositoryFactory {

    public static final String CONFIGNAME = "repository.xml";

    public String[] getRepositoryLocations();

    public Repository[] getRepositories();

    /**
	 * @param location
	 * @return null if no repository match the given location
	 */
    public Repository getRepository(String location);

    public Repository getFrameworkRepository();

    /**
	 * 
	 * @param location
	 * @return
	 * @throws Exception
	 *             if the given location is already exists
	 */
    public Repository addRepository(String location) throws Exception;

    public void removeRepository(String location) throws Exception;
}
