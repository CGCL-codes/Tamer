package edu.nus.iss.ejava.team4.ejb.module;

import java.util.List;
import java.util.Map;
import javax.ejb.Remote;
import edu.nus.iss.ejava.team4.ejb.exception.DAOException;
import edu.nus.iss.ejava.team4.ejb.exception.DataNotFoundException;
import edu.nus.iss.ejava.team4.ejb.exception.UserNotFoundException;
import edu.nus.iss.ejava.team4.model.User;
import edu.nus.iss.ejava.team4.model.UserSecurity;

@Remote
public interface UserBeanRemote {

    public User retreiveUserById(String id) throws UserNotFoundException, DAOException;

    public List<User> retrieveByCriteria(Map<String, Object> criteria) throws DAOException, DataNotFoundException;

    public void resetPassword(UserSecurity user) throws DAOException;

    public UserSecurity getUserPasswordReset(String userid) throws DAOException;
}
