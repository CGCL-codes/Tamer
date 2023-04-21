package es.devel.opentrats.dao;

import es.devel.opentrats.dao.exception.UserDaoException;
import es.devel.opentrats.model.User;

/**
 *
 * @author Fran Serrano
 */
public interface IUserDao {

    User load(String userNameArg) throws UserDaoException;

    void save(User userArg) throws UserDaoException;

    void delete(User userArg) throws UserDaoException;
}
