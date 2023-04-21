package com.inet.qlcbcc.repository;

import java.util.List;
import org.webos.core.repository.RepositoryException;
import org.webos.repository.hibernate.HibernateModifiableRepository;
import com.inet.qlcbcc.domain.User;

/**
 * A modifiable user repository interface.
 *
 * @author Dzung Nguyen
 * @version $Id: UserModifiableRepository.java 2011-05-01 2:29:13z nguyen_dv $
 *
 * @since 1.0
 */
public interface UserModifiableRepository extends HibernateModifiableRepository<User, String> {

    /**
   * Locked user from the given list of user names.
   *
   * @param ids the given list of users to lock/unlock.
   * @param locked the given user locked mode.
   * @return the number of effected rows.
   * @throws RepositoryException if an error occurs during locking users.
   */
    int lock(List<String> ids, boolean locked) throws RepositoryException;

    /**
   * Delete role, account relationship from the given user identifier.
   *
   * @param id the given user identifier.
   * @throws RepositoryException if an error occurs during deleting the role, account relation ship.
   */
    void deleteRelationship(String id) throws RepositoryException;
}
