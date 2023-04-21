package org.pojosoft.lms.user;

import org.pojosoft.core.persistence.PersistenceException;
import org.pojosoft.lms.user.model.User;

/**
 * The User Service.
 * @author POJO Software
 * @version 1.0
 * @since 1.0
 */
public interface UserService {

    /**
   * Get the user entity.
   *
   * @param userId The user id.
   * @return User
   * @throws PersistenceException Thrown on Persistence errors.
   * @throws UserServiceException Thrown on service errors.
   */
    User getUser(String userId) throws PersistenceException, UserServiceException;

    /**
   * Add the user entity.
   *
   * @param user the user entity.
   * @return User
   * @throws PersistenceException Thrown on Persistence errors.
   * @throws UserServiceException Thrown on service errors.
   */
    User addUser(User user) throws PersistenceException, UserServiceException;

    /**
   * Update the user.
   *
   * @param user The user to be updated.
   * @return User
   * @throws PersistenceException Thrown on Persistence errors.
   * @throws UserServiceException Thrown on service errors.
   */
    User updateUser(User user) throws PersistenceException, UserServiceException;

    /**
   * Remove the user entity.
   *
   * @param id the user id.
   * @throws PersistenceException Thrown on Persistence errors.
   * @throws UserServiceException Thrown on service errors.
   */
    void removeUser(String id) throws PersistenceException, UserServiceException;
}
