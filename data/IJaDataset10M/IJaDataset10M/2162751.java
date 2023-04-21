package no.ugland.utransprod.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import no.ugland.utransprod.dao.ApplicationUserDAO;
import no.ugland.utransprod.model.ApplicationUser;
import no.ugland.utransprod.model.ProductAreaGroup;
import no.ugland.utransprod.service.ApplicationUserManager;
import no.ugland.utransprod.service.JobFunctionManager;

/**
 * Implementasjon av manager for brukere.
 * 
 * @author atle.brekka
 */
public class ApplicationUserManagerImpl extends ManagerImpl<ApplicationUser> implements ApplicationUserManager {

    private JobFunctionManager jobFunctionManager;

    /**
	 * @param aJobFunctionManager
	 */
    public final void setJobFunctionManager(final JobFunctionManager aJobFunctionManager) {
        jobFunctionManager = aJobFunctionManager;
    }

    /**
	 * @see no.ugland.utransprod.service.ApplicationUserManager#login(java.lang.String,
	 *      java.lang.String)
	 */
    public final ApplicationUser login(final String userName, final String password) {
        List<ApplicationUser> users = ((ApplicationUserDAO) dao).findByUserNameAndPassword(userName, password);
        if (users != null && users.size() == 1) {
            return users.get(0);
        }
        return null;
    }

    /**
	 * @see no.ugland.utransprod.service.ApplicationUserManager#findAll()
	 */
    public final List<ApplicationUser> findAll() {
        return dao.getObjects("userName");
    }

    /**
	 * @see no.ugland.utransprod.service.ApplicationUserManager#findAllNotGroup()
	 */
    public final List<ApplicationUser> findAllNotGroup() {
        return ((ApplicationUserDAO) dao).findAllNotGroup();
    }

    /**
	 * @param object
	 * @return brukere
	 * @see no.ugland.utransprod.service.OverviewManager#findByObject(java.lang.Object)
	 */
    public final List<ApplicationUser> findByObject(final ApplicationUser object) {
        return dao.findByExampleLike(object);
    }

    /**
	 * @param object
	 * @see no.ugland.utransprod.service.OverviewManager#refreshObject(java.lang.Object)
	 */
    public final void refreshObject(final ApplicationUser object) {
        ((ApplicationUserDAO) dao).refreshObject(object);
    }

    /**
	 * @param object
	 * @see no.ugland.utransprod.service.OverviewManager#removeObject(java.lang.Object)
	 */
    public final void removeObject(final ApplicationUser object) {
        dao.removeObject(object.getUserId());
    }

    /**
	 * @param object
	 * @see no.ugland.utransprod.service.OverviewManager#saveObject(java.lang.Object)
	 */
    public final void saveObject(final ApplicationUser object) {
        dao.saveObject(object);
    }

    /**
	 * @see no.ugland.utransprod.service.ApplicationUserManager#findAllPackers()
	 */
    public final List<String> findAllPackers(ProductAreaGroup productAreaGroup) {
        return ((ApplicationUserDAO) dao).findAllPackers(productAreaGroup);
    }

    /**
	 * @see no.ugland.utransprod.service.ApplicationUserManager#
	 *      isUserFunctionManager(no.ugland.utransprod.model.ApplicationUser)
	 */
    public final Boolean isUserFunctionManager(final ApplicationUser user) {
        return jobFunctionManager.isFunctionManager(user);
    }

    /**
	 * @see no.ugland.utransprod.service.ApplicationUserManager#findAllNamesNotGroup()
	 */
    public final List<String> findAllNamesNotGroup() {
        List<ApplicationUser> users = findAllNotGroup();
        List<String> userNames = new ArrayList<String>();
        if (users != null) {
            for (ApplicationUser user : users) {
                userNames.add(user.getFullName());
            }
        }
        return userNames;
    }

    /**
	 * @see no.ugland.utransprod.service.ApplicationUserManager#
	 *      saveApplicationUser(no.ugland.utransprod.model.ApplicationUser)
	 */
    public final void saveApplicationUser(final ApplicationUser user) {
        dao.saveObject(user);
    }

    @Override
    protected Serializable getObjectId(ApplicationUser object) {
        return object.getUserId();
    }
}
