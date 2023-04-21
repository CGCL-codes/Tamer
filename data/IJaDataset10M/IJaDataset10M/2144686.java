package org.isurf.spmiddleware.dao;

import org.isurf.spmiddleware.SPClientProfile;

/**
 * DAO for CRUD of {@link SPClientProfile}s.
 */
public interface SPClientProfileDAO {

    /**
	 * Finds the {@link SPClientProfile}.
	 *
	 * @return The SPClientProfile.
	 */
    SPClientProfile find();

    /**
	 * Saves or updates the {@link SPClientProfile}.
	 *
	 * @param spClientProfile The profile to be updated.
	 */
    void saveOrUpdate(SPClientProfile spClientProfile);
}
