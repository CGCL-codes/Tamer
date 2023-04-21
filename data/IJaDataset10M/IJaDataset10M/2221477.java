package wilos.business.services.gen;

import java.util.List;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import wilos.model.misc.wilosuser.Administrator;
import wilos.hibernate.gen.AdministratorDao;

/**
 * Service object for domain model class Administrator.
 * 
 * @see wilos.model.misc.wilosuser.Administrator
 * <p/>Remark: This class is auto-generated.
 */
@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
public class AdministratorService {

    /**
     * This field allows you to have access to the AdministratorDao.
     */
    private AdministratorDao AdministratorDao;

    /**
     * Allows to save the Administrator _elt.
     * 
     * @param _elt 
     *            The Administrator to save.
     */
    public void saveAdministrator(Administrator _elt) {
        this.AdministratorDao.saveOrUpdateAdministrator(_elt);
    }

    /**
	 * Save Administrator with encryption.
	 * 
	 * @param _elt
	 */
    public void saveAdministratorWithEncryption(Administrator _elt) {
        _elt.setPassword(wilos.utils.Security.encode(_elt.getPassword()));
        this.saveAdministrator(_elt);
    }

    /**
     * Allows to get the Administrator having as id _id.
     * 
     * @param _id 
     *            The id of the Administrator to get.
     * @return The corresponding Administrator.
     */
    public Administrator getAdministrator(String _id) {
        return this.AdministratorDao.getAdministrator(_id);
    }

    /**
     * Return the Administrator which have the login _login.
     * 
     * @param _login 
     *            The id of the Administrator.
     * @return The corresponding Administrator.
     */
    public Administrator getAdministratorByLogin(String _login) {
        return this.AdministratorDao.getAdministratorByLogin(_login);
    }

    /**
     * Allows to remove the Administrator .
     * 
     * @param _elt 
     *            The Administrator to remove.
     */
    public void removeAdministrator(Administrator _elt) {
        this.AdministratorDao.deleteAdministrator(_elt);
    }

    /**
     * Return the list of all the Administrators.
     * 
     * @return The list of all the Administrators.
     */
    public List<Administrator> getAllAdministrators() {
        return this.AdministratorDao.getAllAdministrators();
    }

    /**
     * Get the Administrator Dao.
     * 
     * @return the AdministratorDao
     */
    public AdministratorDao getAdministratorDao() {
        return this.AdministratorDao;
    }

    /**
     * Set the Administrator Dao with _dao.
     * 
     * @param _dao 
     *            The AdministratorDao to set.
     */
    public void setAdministratorDao(AdministratorDao _dao) {
        this.AdministratorDao = _dao;
    }
}
