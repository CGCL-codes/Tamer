package net.sf.esims.dao;

import java.util.List;
import net.sf.esims.model.entity.User;
import net.sf.esims.dao.EsimsBaseDataAccessObject;

/**
 * @author juby.victor@gmail.com
 *
 */
public interface UserDAOIfc extends EsimsBaseDataAccessObject<User, Long> {

    /**
	 * Returns list of users whose userNames
	 * match by a xxxx% based condition.
	 * @param firstName
	 * @param LastName
	 * @return
	 */
    public List<User> getByUserName(String userName);

    public List<User> getByName(String firstName, String lastName);

    public List<User> getUserByFirstName(String firstName);
}
