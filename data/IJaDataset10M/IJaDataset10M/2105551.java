package com.wgo.precise.server.wiring;

import java.util.Set;
import org.apache.log4j.Logger;
import org.hibernate.exception.SQLGrammarException;
import remato.domain.common.RequirementSystem;
import remato.domain.common.Status;
import remato.domain.server.ejb.StatusEjb;
import com.wgo.bpot.common.transport.exception.db.DatabaseException;
import com.wgo.bpot.domain.common.User;
import com.wgo.bpot.domain.server.ejb.UserEjb;
import com.wgo.bpot.server.configuration.UsersFileServices;
import com.wgo.bpot.server.persist.PersistService;
import com.wgo.bpot.server.persist.Persistent;
import com.wgo.bpot.server.persist.hibernate.HibernatePersistService;
import com.wgo.bpot.wiring.InitializationService;
import com.wgo.precise.privilege.PreciseUserRole;

/**
 * @version $Id: PreciseInitializationServiceImpl.java 540 2008-01-09 19:42:31Z petter.eide $
 */
public class PreciseInitializationServiceImpl implements InitializationService {

    protected static final String PRECISE_USER_FILE = "/precise-users.xml";

    protected static final String PRECISE_DEFAULT_USER_FILE = "/default/precise-users.xml";

    private HibernatePersistService<Persistent> persistService;

    private static final Logger log = Logger.getLogger(PreciseInitializationServiceImpl.class);

    public PreciseInitializationServiceImpl(HibernatePersistService<Persistent> persistService) {
        this.persistService = persistService;
    }

    public PersistService<Persistent> getPersistService() {
        return persistService;
    }

    public boolean isInitializationNeeded() {
        try {
            return null == persistService.findDbRootSystem(RequirementSystem.class);
        } catch (DatabaseException e) {
            return true;
        } catch (SQLGrammarException e) {
            return true;
        }
    }

    public void initialize() {
        log.info("Attempting to initialize Precise!");
        try {
            persistService.createSchema();
            persistService.commitTransaction();
            log.info("Database schemas created successfully!");
            persistService.startTransaction();
            for (Class<? extends Persistent> rootConceptType : persistService.getDomainModelConvention().getRootConceptClasses()) {
                Class<? extends Persistent> ejbType = persistService.getDomainModelConvention().apiToEjb(rootConceptType);
                Persistent rootConcept = null;
                try {
                    rootConcept = ejbType.newInstance();
                } catch (Exception e) {
                    throw new RuntimeException("Unable to instantiate root concept." + e);
                }
                persistService.saveOrUpdate(rootConcept);
            }
            persistService.commitTransaction();
            log.info("Requirement system created successfully!");
            persistService.startTransaction();
            RequirementSystem reqSys = persistService.findDbRootSystem(RequirementSystem.class);
            createInitialRequirementStatuses(reqSys);
            persistService.saveOrUpdate(reqSys);
            persistService.commitTransaction();
            log.info("Requirement statuses created successfully!");
            persistService.startTransaction();
            reqSys = persistService.findDbRootSystem(RequirementSystem.class);
            createInitialUsers(persistService);
            persistService.saveOrUpdate(reqSys);
            persistService.commitTransaction();
            persistService.startTransaction();
        } finally {
        }
    }

    protected static void createInitialUsers(HibernatePersistService<Persistent> persistService) {
        UsersFileServices usersFileServices = new PreciseUsersFileServices(persistService);
        Set<User> users = null;
        try {
            users = usersFileServices.getUsersFromFileQuiet(PRECISE_USER_FILE);
        } catch (Throwable t) {
            log.warn("Reading users from file " + PRECISE_USER_FILE + " failed.");
        }
        if (null == users || 0 == users.size()) {
            try {
                users = usersFileServices.getUsersFromFileQuiet(PRECISE_DEFAULT_USER_FILE);
            } catch (Throwable t) {
                log.warn("Reading users from file failed.", t);
            }
        }
        if (null == users || 0 == users.size()) {
            persistService.findDbRootSystem(RequirementSystem.class).addUser(createUser("admin", "admin", PreciseUserRole.ADMIN));
            log.info("Default admin user created successfully!");
        }
    }

    public boolean updateUsersFromFile() {
        return new PreciseUsersFileServices(persistService).updatePersistedUsersFromFile(PRECISE_USER_FILE);
    }

    protected static User createUser(String userName, String password, PreciseUserRole role) {
        UserEjb user = new UserEjb();
        user.setUserName(userName);
        user.setPassword(password);
        user.setUserRole(role);
        return user;
    }

    protected static void createInitialRequirementStatuses(RequirementSystem reqSys) {
        Status opened = new StatusEjb("Opened");
        Status closed = new StatusEjb("Closed");
        Status assigned = new StatusEjb("Assigned");
        Status reopened = new StatusEjb("Reopened");
        Status rejected = new StatusEjb("Rejected");
        opened.addNextStatus(assigned, rejected, closed);
        assigned.addNextStatus(closed, rejected);
        closed.addNextStatus(reopened);
        reqSys.addRequirementStatus(opened);
        reqSys.addRequirementStatus(closed);
        reqSys.addRequirementStatus(assigned);
        reqSys.addRequirementStatus(reopened);
        reqSys.addRequirementStatus(rejected);
    }
}
