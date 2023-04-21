package com.inet.qlcbcc.repository;

import java.util.List;
import org.webos.core.option.Option;
import org.webos.core.repository.RepositoryException;
import org.webos.repository.hibernate.HibernateReadableRepository;
import com.inet.qlcbcc.domain.Account;
import com.inet.qlcbcc.domain.User;

/**
 * A readable user interface.
 *
 * @author Dzung Nguyen
 * @version $Id: UserReadableRepository.java 2011-05-01 2:12:37z nguyen_dv $
 *
 * @since 1.0
 */
public interface UserReadableRepository extends HibernateReadableRepository<User, String> {

    /**
   * Finds the user from the given user name.
   *
   * @param username the given user name.
   * @return the {@link User} information.
   * @throws RepositoryException if an error occurs during finding the user name.
   */
    Option<User> findByUsername(String username) throws RepositoryException;

    /**
   * Finds user from the given user code.
   *
   * @param code the given user code.
   * @return the user information or {@code Option.None} if not found.
   * @throws RepositoryException if an error occurs during finding the user from user code.
   */
    Option<User> findByCode(String code) throws RepositoryException;

    /**
   * Checks the username existing in this system.
   *
   * @param username the given username to check.
   * @return {@code true} if the username existing, {@code false} otherwise.
   * @throws RepositoryException if an error occurs during checking. 
   */
    boolean exists(String username) throws RepositoryException;

    /**
   * Finds the list of accounts associate to user who has the given username.
   *
   * @param username the given username.
   * @return the list of accounts associate to user.
   * @throws RepositoryException if an errors occurs during finding the list of accounts.
   */
    List<Account> findAccountBy(String username) throws RepositoryException;

    /**
   * Find user by given user's name and function type
   * 
   * @param username the given username
   * @param functionType the type of function
   * @return the list of function
   * @throws RepositoryException if an errors occurs during finding functions
   */
    Option<User> findByUnameAndFunctionType(String username, String functionType) throws RepositoryException;

    /**
   * Returns the user revision.
   *
   * @param username the given username.
   * @return the username revision.
   * @throws RepositoryException if an error occurs during getting revision.
   */
    int getRevision(String username) throws RepositoryException;
}
