package wilos.business.services.gen;

import java.util.List;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import wilos.model.misc.wilosuser.WilosUser;
import wilos.hibernate.gen.WilosUserDao;

/**
 * Service object for domain model class WilosUser.
 * 
 * @see wilos.model.misc.wilosuser.WilosUser
 * <p/>Remark: This class is auto-generated.
 */
@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
public class WilosUserService {

    /**
     * This field allows you to have access to the WilosUserDao.
     */
    private WilosUserDao WilosUserDao;

    /**
     * Allows to save the WilosUser _elt.
     * 
     * @param _elt 
     *            The WilosUser to save.
     */
    public void saveWilosUser(WilosUser _elt) {
        this.WilosUserDao.saveOrUpdateWilosUser(_elt);
    }

    /**
	 * Save WilosUser with encryption.
	 * 
	 * @param _elt
	 */
    public void saveWilosUserWithEncryption(WilosUser _elt) {
        _elt.setPassword(wilos.utils.Security.encode(_elt.getPassword()));
        this.saveWilosUser(_elt);
    }

    /**
     * Allows to get the WilosUser having as id _id.
     * 
     * @param _id 
     *            The id of the WilosUser to get.
     * @return The corresponding WilosUser.
     */
    public WilosUser getWilosUser(String _id) {
        return this.WilosUserDao.getWilosUser(_id);
    }

    /**
     * Return the WilosUser which have the login _login.
     * 
     * @param _login 
     *            The id of the WilosUser.
     * @return The corresponding WilosUser.
     */
    public WilosUser getWilosUserByLogin(String _login) {
        return this.WilosUserDao.getWilosUserByLogin(_login);
    }

    /**
     * Allows to remove the WilosUser .
     * 
     * @param _elt 
     *            The WilosUser to remove.
     */
    public void removeWilosUser(WilosUser _elt) {
        this.WilosUserDao.deleteWilosUser(_elt);
    }

    /**
     * Return the list of all the WilosUsers.
     * 
     * @return The list of all the WilosUsers.
     */
    public List<WilosUser> getAllWilosUsers() {
        return this.WilosUserDao.getAllWilosUsers();
    }

    /**
     * Get the WilosUser Dao.
     * 
     * @return the WilosUserDao
     */
    public WilosUserDao getWilosUserDao() {
        return this.WilosUserDao;
    }

    /**
     * Set the WilosUser Dao with _dao.
     * 
     * @param _dao 
     *            The WilosUserDao to set.
     */
    public void setWilosUserDao(WilosUserDao _dao) {
        this.WilosUserDao = _dao;
    }
}
