package com.spr.bo;

import java.util.List;
import com.spr.dao.base.factory.DaoRegistry;
import com.spr.dao.entity.SPRUserDao;
import com.spr.to.SprUser;

public class LoginBO {

    private SPRUserDao userDao = DaoRegistry.getSprUserDaoImpl();

    public LoginBO() {
    }

    /****
	 * 
	 * @param sprUser
	 * @return
	 * @throws Exception
	 */
    public SprUser validateUser(SprUser sprUser) throws Exception {
        SPRUserDao dao = DaoRegistry.getSprUserDaoImpl();
        return dao.validateUser(sprUser);
    }

    /****
	 * 
	 * @param sprUser
	 * @throws Exception
	 */
    public void updateLoggedOurUser(SprUser sprUser) throws Exception {
        userDao.updateLastLoggedDate(sprUser);
    }

    /****
	 * 
	 * @return
	 */
    public List<SprUser> getAllManagedUsers() {
        return userDao.getAllManagedUsers();
    }
}
