package org.jpasample.dao;

import java.util.List;
import org.jpasample.model.Release;

public interface ReleaseDAO {

    /**
		 * Create a new Release
		 * @param newRelease
		 */
    public Release save(Release newRelease);

    /**
		 * Delete an Release
		 * @param release
		 */
    public void delete(Release release);

    /**
		 * Update Release Info
		 * @param release
		 */
    public Release update(Release release);

    /**
		 * Search all releases
		 * @return List of releases
		 */
    public List<Release> findAll();

    /**
		 * Find by Name
		 * @param name
		 * @return
		 */
    public List<Release> findByName(String name);

    /**
		 * Find sueprvisor by id
		 * @param id
		 * @return Release
		 */
    public Release findById(long id);
}
