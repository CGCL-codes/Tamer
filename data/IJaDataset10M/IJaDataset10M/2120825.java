package au.org.tpac.portal.repository;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import au.org.tpac.portal.domain.Dataset;
import au.org.tpac.portal.domain.DlpUser;

/**
 * The Interface DlpUserDao.
 */
public interface DlpUserDao {

    /**
     * List all users
     * @return list of all users
     */
    List<DlpUser> findUsers();

    /**
     * Find datasets that are read-only for a particular user
     * @param user Target user
     * @return read-only datasets for a particular user
     */
    List<Dataset> findReadOnlyDatasets(DlpUser user);

    /**
     * Find datasets that are admin for a particular user
     * @param user Target user
     * @return admin datasets for a particular user
     */
    List<Dataset> findAdminDatasets(DlpUser user);

    /**
     * Find dataset ids that are read-only for a particular user
     * @param userId Target user
     * @return read-only datasets for a particular user
     */
    Set<Integer> findReadOnlyDatasetIds(String userId);

    /**
     * Find, for a particular user, admin access for datasets, returning the ids. 
     * @param userId Target user
     * @return admin datasets for a particular user
     */
    Set<Integer> findAdminDatasetIds(String userId);

    /**
     *  Find, for a particular user, read-only access for category, returning the ids. 
     * @param userId Target user
     * @return admin categories for a particular user
     */
    Set<Integer> findReadOnlyCategoryIds(String userId);

    /**
     * Find, for a particular user, admin access for categories, returning the ids. 
     * @param userId Target user
     * @return admin categories for a particular user
     */
    Set<Integer> findAdminCategoryIds(String userId);

    /**
     * List all datasets
     * @return datasets
     */
    List<Dataset> findDatasets();

    /**
	 * Get the DlpUser for a user id
	 * @param name Username of user to lookup
	 */
    DlpUser getUser(String name);
}
