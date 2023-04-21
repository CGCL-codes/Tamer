package org.bims.bimswebaccess;

import org.bims.bimscore.BIMSCoreFacade;
import org.bims.bimscore.BIMSCoreFacadeImplementation;
import org.bims.bimscore.model.User;
import org.springframework.security.context.SecurityContextHolder;

public class BIMSWebAccessUserManager {

    private BIMSCoreFacade _bimsCore = BIMSCoreFacadeImplementation.getInstance();

    private User userLogged = null;

    public BIMSWebAccessUserManager() {
        this.searchWorkingUserByUsername();
        this.updateDateOfWorkingUser();
    }

    protected void searchWorkingUserByUsername() {
        this.setUserLogged(this.getBimsCore().searchAUserByUsernameWithOrganization(SecurityContextHolder.getContext().getAuthentication().getName()));
    }

    protected void updateDateOfWorkingUser() {
        this.getUserLogged().setLastAccess(new java.util.Date());
        this.getBimsCore().updateAUser(this.getUserLogged());
    }

    /**
     * @return the userLogged
     */
    public User getUserLogged() {
        return userLogged;
    }

    /**
     * @param userLogged the userLogged to set
     */
    public void setUserLogged(User userLogged) {
        this.userLogged = userLogged;
    }

    /**
     * @return the _bimsCore
     */
    public BIMSCoreFacade getBimsCore() {
        return _bimsCore;
    }

    /**
     * @param bimsCore the _bimsCore to set
     */
    public void setBimsCore(BIMSCoreFacade bimsCore) {
        this._bimsCore = bimsCore;
    }
}
