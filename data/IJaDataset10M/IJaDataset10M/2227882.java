package com.ihc.app.service.impl;

import org.springframework.security.providers.dao.DaoAuthenticationProvider;
import org.springframework.security.providers.dao.SaltSource;
import org.springframework.security.providers.encoding.PasswordEncoder;
import org.springframework.security.userdetails.UsernameNotFoundException;
import com.ihc.app.dao.*;
import com.ihc.app.service.WellnessProfileExistsException;
import com.ihc.app.service.WellnessManager;
import com.ihc.app.model.*;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.dao.DataIntegrityViolationException;
import javax.jws.WebService;
import javax.persistence.EntityExistsException;
import java.util.List;

/**
 * Implementation of UserManager interface.
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
@WebService(serviceName = "WellnessService", endpointInterface = "com.ihc.app.service.WellnessService")
public class WellnessManagerImpl extends UniversalManagerImpl implements WellnessManager {

    MainDao mainDao;

    PwpDao pwpDao;

    PwpSupplementalDao pwpSupplementalDao;

    PwpComputedDao pwpComputedDao;

    SharedDao sharedDao;

    /**
     * Set the Dao for communication with the data layer.
     * @param Dao that communicates with the database
     */
    @Required
    public void setMainDao(MainDao mainDao) {
        this.mainDao = mainDao;
    }

    @Required
    public void setPwpDao(PwpDao pwpDao) {
        this.pwpDao = pwpDao;
    }

    @Required
    public void setPwpSupplementalDao(PwpSupplementalDao pwpSupplementalDao) {
        this.pwpSupplementalDao = pwpSupplementalDao;
    }

    @Required
    public void setPwpComputedDao(PwpComputedDao pwpComputedDao) {
        this.pwpComputedDao = pwpComputedDao;
    }

    @Required
    public void setSharedDao(SharedDao sharedDao) {
        this.sharedDao = sharedDao;
    }

    /**
     * {@inheritDoc}
     */
    public void saveMain(Main main) throws WellnessProfileExistsException {
        mainDao.save(main);
    }

    public void savePwp(Pwp pwp) throws WellnessProfileExistsException {
        pwpDao.save(pwp);
    }

    public void savePwpSupplemental(PwpSupplemental pwpSup) throws WellnessProfileExistsException {
        pwpSupplementalDao.save(pwpSup);
    }

    public void savePwpComputed(PwpComputed pwpComp) throws WellnessProfileExistsException {
        pwpComputedDao.save(pwpComp);
    }

    public void saveShared(Shared shared) throws WellnessProfileExistsException {
        sharedDao.save(shared);
    }
}
