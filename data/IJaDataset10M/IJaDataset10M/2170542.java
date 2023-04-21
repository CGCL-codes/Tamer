package org.imogene.common.dao;

import java.util.List;
import org.imogene.common.data.SynchronizableUser;

/**
 * Interface that describes a User manager 
 * that enables to manage user access
 * @author MEDES-IMPS
 */
public interface UserDao extends EntityDao {

    public List<SynchronizableUser> getUserFromLogin(String login);
}
