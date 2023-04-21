package org.pojosoft.lms.resource;

import org.pojosoft.core.persistence.PersistenceException;
import org.pojosoft.lms.resource.model.Location;

/**
 * The resource service.
 */
public interface ResourceService {

    /**
   * Get Location.
   *
   * @param id
   * @return
   * @throws PersistenceException
   * @throws ResourceServiceException
   */
    Location getLocation(String id) throws PersistenceException, ResourceServiceException;

    /**
   * Add Location.
   *
   * @param location
   * @return
   * @throws PersistenceException
   * @throws ResourceServiceException
   */
    Location addLocation(Location location) throws PersistenceException, ResourceServiceException;

    /**
   * Update Location.
   *
   * @param location
   * @return
   * @throws PersistenceException
   * @throws ResourceServiceException
   */
    Location updateLocation(Location location) throws PersistenceException, ResourceServiceException;

    /**
   * Remove removeLocation.
   *
   * @param id
   * @throws PersistenceException
   * @throws ResourceServiceException
   */
    void removeLocation(String id) throws PersistenceException, ResourceServiceException;
}
