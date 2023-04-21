package org.sevaapp.security.dao;

import java.util.Collection;
import org.sevaapp.dao.DaoException;
import org.sevaapp.security.domain.Authority;

/**
 * @author Srini
 *
 */
public interface AuthorityDao {

    Authority load(String id) throws DaoException;

    Collection<Authority> findAll() throws DaoException;

    void add(Authority domainObject) throws DaoException;

    void update(Authority domainObject) throws DaoException;

    void delete(String id) throws DaoException;
}
