package au.gov.naa.digipres.dpr.core.importexport;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.junit.Assert;
import org.junit.Test;
import au.gov.naa.digipres.dpr.core.DPRClient;
import au.gov.naa.digipres.dpr.dao.UserDAO;
import au.gov.naa.digipres.dpr.dao.hibernate.HibernateDataAccessManager;
import au.gov.naa.digipres.dpr.model.permission.Permission;
import au.gov.naa.digipres.dpr.model.user.User;
import au.gov.naa.digipres.dpr.model.user.UserPermission;
import au.gov.naa.digipres.dpr.model.user.User.InvalidPasswordException;
import au.gov.naa.digipres.dpr.task.AdminUserTask;
import au.gov.naa.digipres.dpr.testutils.DPRTestCase;
import au.gov.naa.digipres.dpr.testutils.TestCaseLogOnHandler;
import au.gov.naa.digipres.dpr.testutils.TestDatabaseUtils;

/**
 * <p>Slightly complex test case to verify users are being exported and imported correctly.
 * The test case works by creating a multiple new users on QF, including a 'root' user,
 * with createdBy set to null, exporting the user list,
 * logging off QF, logging onto PF, importing the new user list, then logging off
 * PF, logging back onto QF, modifying the created users, exporting the user list,
 * logging off QF and on to PF, and importing the modified user list.</p>
 * 
 * <p>Assertions are made to ensure users are created propertly, they are exported correctly,
 * and imported correctly.</p>
 * 
 * <p>There will be two seperate user trees, one based on the Flintstones, with the DPR user
 * as the 'root', the other on the Simpsons with a new user - "smithers" as it's root. 
 * Both 'trees' will have to be imported for this test to be successful.</p>
 * 
 * @author andy
 *
 */
public class VerifyMultipleUserImport extends DPRTestCase {

    private static int USER_NAME = 0;

    private static int PASSWORD = 1;

    private static int FIRST_NAME = 2;

    private static int LAST_NAME = 3;

    private static int DATE_CREATED = 4;

    private static int FLINTSTONE_USER_COUNT = 4;

    private static String DPR_USER_NAME = "dpr";

    private static String[] FRED_USER_DATA = { "fred", "ff1", "Fred", "Flintstone", "01/01/2008" };

    private static String[] BARNEY_USER_DATA = { "barn", "br1", "Barney", "Rubble", "01/01/2008" };

    private static String[] WILMA_USER_DATA = { "wilm", "wf1", "Wilma", "Flintstone", "02/01/2008" };

    private static String[][] FLINTSTONE_USER_ARRAY = { FRED_USER_DATA, BARNEY_USER_DATA, WILMA_USER_DATA };

    private static int SIMPSON_USER_COUNT = 4;

    private static String[] SMITHERS_USER_DATA = { "smit", "ws1", "Wheylan", "Smithers", "01/01/2008" };

    private static String[] CARL_USER_DATA = { "carl", "cc1", "Carl", "Carlson", "02/01/2008" };

    private static String[] LENNY_USER_DATA = { "lenn", "ll1", "Lenny", "Leonard", "02/01/2008" };

    private static String[] HOMER_USER_DATA = { "home", "hs1", "Homer", "Simpson", "03/01/2008" };

    private static String[][] SIMPSON_USER_ARRAY = { SMITHERS_USER_DATA, CARL_USER_DATA, LENNY_USER_DATA, HOMER_USER_DATA };

    /**
	 * Log on to QF, create a user, and then serialise it. Verify that the user
	 * has been serialised correctly (permissions, and created by and so on).
	 */
    @Test
    public void testExportUsersFromQF() throws Exception {
        TestDatabaseUtils.setUpQFTables();
        TestDatabaseUtils.setUpPFTables();
        File exportFile = createAndExportUsersFromQF();
        importUserListToPF(exportFile);
        File secondExportFile = modifyAndCreateNewUsersExportFromQF();
        importSecondUserListToPF(secondExportFile);
        File thirdExportFile = modifyAndExportUsersFromQF();
        importThirdUserListToPF(thirdExportFile);
    }

    private File createAndExportUsersFromQF() throws Exception {
        DPRClient dprClient = new DPRClient();
        dprClient.setDataAccessManager(new HibernateDataAccessManager(dprClient));
        Map<String, String> connectionProperties = dprClient.getConnectionProperties();
        setPropertiesForQF(connectionProperties);
        dprClient.connectToDataStore(connectionProperties);
        User dprUser = dprClient.logOn("dpr", "dpr", new TestCaseLogOnHandler());
        AdminUserTask userTask = (AdminUserTask) dprClient.getTaskByName(dprUser, AdminUserTask.TASK_NAME);
        UserDAO userDAO = dprClient.getDataAccessManager().getUserDAO(userTask);
        List<Permission> permissions = new ArrayList<Permission>();
        createAndPersistUser(userDAO, FRED_USER_DATA, dprUser, permissions);
        createAndPersistUser(userDAO, BARNEY_USER_DATA, dprUser, permissions);
        createAndPersistUser(userDAO, WILMA_USER_DATA, dprUser, permissions);
        File exportFile = File.createTempFile("user", ".db4o");
        logger.fine("First export file name: " + exportFile.getAbsolutePath());
        exportFile.deleteOnExit();
        UserImportExportWrapper userImportExportWrapper = new UserImportExportWrapper();
        userImportExportWrapper.serialiseToFile(exportFile, userDAO);
        try {
            UserImportExportWrapper fromFileUserWrapper = UserImportExportWrapper.deserialiseFromFile(exportFile);
            Assert.assertNotNull(fromFileUserWrapper);
            List<User> readUserList = fromFileUserWrapper.getUserList();
            Assert.assertNotNull(readUserList);
            Assert.assertTrue(readUserList.size() == FLINTSTONE_USER_COUNT);
            User retrievedDprUser = null;
            for (User user : readUserList) {
                if (user.getUserName().equals(DPR_USER_NAME)) {
                    retrievedDprUser = user;
                }
            }
            Assert.assertNotNull(retrievedDprUser);
            for (User user : readUserList) {
                for (String[] element : FLINTSTONE_USER_ARRAY) {
                    if (user.getUserName().equals(element[USER_NAME])) {
                        validateUser(user, element, retrievedDprUser, permissions, false);
                        break;
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            Assert.fail();
        }
        dprClient.logOff(dprUser);
        dprClient.disconnectFromDataStore();
        return exportFile;
    }

    private void importUserListToPF(File exportFile) throws Exception {
        DPRClient dprClient = new DPRClient();
        dprClient.setDataAccessManager(new HibernateDataAccessManager(dprClient));
        Map<String, String> connectionProperties;
        connectionProperties = dprClient.getConnectionProperties();
        setPropertiesForPF(connectionProperties);
        dprClient.connectToDataStore(connectionProperties);
        User dprUser = dprClient.logOn("dpr", "dpr", new TestCaseLogOnHandler());
        AdminUserTask newUserTask = (AdminUserTask) dprClient.getTaskByName(dprUser, AdminUserTask.TASK_NAME);
        UserImportExportWrapper userWrapper = UserImportExportWrapper.deserialiseFromFile(exportFile);
        UserDAO pfUserDAO = dprClient.getDataAccessManager().getUserDAO(newUserTask);
        pfUserDAO.synchroniseAndMergeUsers(userWrapper.getUserList());
        List<User> pfUserList = pfUserDAO.getUserList();
        Assert.assertNotNull(pfUserList);
        User retrievedDPRUser = null;
        for (User user : pfUserList) {
            if (user.getUserName().equals(DPR_USER_NAME)) {
                retrievedDPRUser = user;
            }
        }
        Assert.assertNotNull(retrievedDPRUser);
        List<Permission> permissions = new ArrayList<Permission>();
        for (User user : pfUserList) {
            for (String[] element : FLINTSTONE_USER_ARRAY) {
                if (user.getUserName().equals(element[USER_NAME])) {
                    validateUser(user, element, retrievedDPRUser, permissions, false);
                }
            }
        }
        dprClient.logOff(dprUser);
        dprClient.disconnectFromDataStore();
    }

    /**
	 * Back to QF to modify the existing users and add some new ones.
	 * @return
	 * @throws Exception
	 */
    private File modifyAndCreateNewUsersExportFromQF() throws Exception {
        DPRClient dprClient = new DPRClient();
        dprClient.setDataAccessManager(new HibernateDataAccessManager(dprClient));
        Map<String, String> connectionProperties = dprClient.getConnectionProperties();
        setPropertiesForQF(connectionProperties);
        dprClient.connectToDataStore(connectionProperties);
        User dprUser = dprClient.logOn("dpr", "dpr", new TestCaseLogOnHandler());
        AdminUserTask userTask = (AdminUserTask) dprClient.getTaskByName(dprUser, AdminUserTask.TASK_NAME);
        UserDAO userDAO = dprClient.getDataAccessManager().getUserDAO(userTask);
        List<Permission> adminPermissions = new ArrayList<Permission>();
        adminPermissions.add(Permission.ADMIN_PERMISSION);
        List<Permission> userPermissions = new ArrayList<Permission>();
        User smithersUser = createAndPersistUser(userDAO, SMITHERS_USER_DATA, null, adminPermissions);
        createAndPersistUser(userDAO, CARL_USER_DATA, smithersUser, userPermissions);
        User lennyUser = createAndPersistUser(userDAO, LENNY_USER_DATA, smithersUser, adminPermissions);
        createAndPersistUser(userDAO, HOMER_USER_DATA, lennyUser, userPermissions);
        User fredUser = userDAO.getUser(FRED_USER_DATA[USER_NAME]);
        Assert.assertNotNull(fredUser);
        fredUser.setDisabled(true);
        userDAO.saveUser(fredUser);
        File exportFile = File.createTempFile("user", ".db4o");
        exportFile.deleteOnExit();
        logger.fine("Second export file name: " + exportFile.getAbsolutePath());
        UserImportExportWrapper userImportExportWrapper = new UserImportExportWrapper();
        userImportExportWrapper.serialiseToFile(exportFile, userDAO);
        try {
            UserImportExportWrapper fromFileUserWrapper = UserImportExportWrapper.deserialiseFromFile(exportFile);
            Assert.assertNotNull(fromFileUserWrapper);
            List<User> readUserList = fromFileUserWrapper.getUserList();
            Assert.assertNotNull(readUserList);
            Assert.assertTrue(readUserList.size() == FLINTSTONE_USER_COUNT + SIMPSON_USER_COUNT);
        } catch (Exception ex) {
            ex.printStackTrace();
            Assert.fail();
        }
        dprClient.logOff(dprUser);
        dprClient.disconnectFromDataStore();
        return exportFile;
    }

    private void importSecondUserListToPF(File exportFile) throws Exception {
        DPRClient dprClient = new DPRClient();
        dprClient.setDataAccessManager(new HibernateDataAccessManager(dprClient));
        Map<String, String> connectionProperties;
        connectionProperties = dprClient.getConnectionProperties();
        setPropertiesForPF(connectionProperties);
        dprClient.connectToDataStore(connectionProperties);
        User dprUser = dprClient.logOn("dpr", "dpr", new TestCaseLogOnHandler());
        AdminUserTask newUserTask = (AdminUserTask) dprClient.getTaskByName(dprUser, AdminUserTask.TASK_NAME);
        UserImportExportWrapper userWrapper = UserImportExportWrapper.deserialiseFromFile(exportFile);
        UserDAO pfUserDAO = dprClient.getDataAccessManager().getUserDAO(newUserTask);
        List<User> wrapperUserList = userWrapper.getUserList();
        pfUserDAO.synchroniseAndMergeUsers(userWrapper.getUserList());
        List<User> pfUserList = pfUserDAO.getUserList();
        Assert.assertNotNull(wrapperUserList);
        User retrievedDPRUser = null;
        User retrievedSmithersUser = null;
        User retrievedLennyUser = null;
        for (User user : pfUserList) {
            if (user.getUserName().equals(DPR_USER_NAME)) {
                retrievedDPRUser = user;
            } else if (user.getUserName().equals(SMITHERS_USER_DATA[USER_NAME])) {
                retrievedSmithersUser = user;
            } else if (user.getUserName().equals(LENNY_USER_DATA[USER_NAME])) {
                retrievedLennyUser = user;
            }
        }
        Assert.assertNotNull(retrievedSmithersUser);
        Assert.assertNotNull(retrievedDPRUser);
        List<Permission> userPermissions = new ArrayList<Permission>();
        List<Permission> adminPermissions = new ArrayList<Permission>();
        adminPermissions.add(Permission.ADMIN_PERMISSION);
        for (User user : pfUserList) {
            for (String[] element : FLINTSTONE_USER_ARRAY) {
                if (user.getUserName().equals(element[USER_NAME])) {
                    boolean isDisabled = user.getUserName().equals(FRED_USER_DATA[USER_NAME]);
                    validateUser(user, element, retrievedDPRUser, userPermissions, isDisabled);
                    break;
                }
            }
            for (String[] element : SIMPSON_USER_ARRAY) {
                if (user.getUserName().equals(element[USER_NAME])) {
                    List<Permission> currentUserPermissions = userPermissions;
                    if (user.getUserName().equals(SMITHERS_USER_DATA[USER_NAME]) || user.getUserName().equals(LENNY_USER_DATA[USER_NAME])) {
                        currentUserPermissions = adminPermissions;
                    }
                    User creatingUser = retrievedSmithersUser;
                    if (user.getUserName().equals(SMITHERS_USER_DATA[USER_NAME])) {
                        creatingUser = null;
                    } else if (user.getUserName().equals(HOMER_USER_DATA[USER_NAME])) {
                        creatingUser = retrievedLennyUser;
                    }
                    validateUser(user, element, creatingUser, currentUserPermissions, false);
                    break;
                }
            }
        }
        dprClient.logOff(dprUser);
        dprClient.disconnectFromDataStore();
    }

    private File modifyAndExportUsersFromQF() throws Exception {
        DPRClient dprClient = new DPRClient();
        dprClient.setDataAccessManager(new HibernateDataAccessManager(dprClient));
        Map<String, String> connectionProperties = dprClient.getConnectionProperties();
        setPropertiesForQF(connectionProperties);
        dprClient.connectToDataStore(connectionProperties);
        User dprUser = dprClient.logOn("dpr", "dpr", new TestCaseLogOnHandler());
        AdminUserTask userTask = (AdminUserTask) dprClient.getTaskByName(dprUser, AdminUserTask.TASK_NAME);
        UserDAO userDAO = dprClient.getDataAccessManager().getUserDAO(userTask);
        for (User user : userDAO.getUserList()) {
            for (String[] element : FLINTSTONE_USER_ARRAY) {
                if (user.getUserName().equals(element[USER_NAME])) {
                    user.addPermission(Permission.ADMIN_PERMISSION, dprUser);
                    user.setDisabled(false);
                    userDAO.saveUser(user);
                }
            }
        }
        File exportFile = File.createTempFile("user", ".db4o");
        logger.fine("First export file name: " + exportFile.getAbsolutePath());
        exportFile.deleteOnExit();
        UserImportExportWrapper userImportExportWrapper = new UserImportExportWrapper();
        userImportExportWrapper.serialiseToFile(exportFile, userDAO);
        try {
            UserImportExportWrapper fromFileUserWrapper = UserImportExportWrapper.deserialiseFromFile(exportFile);
            Assert.assertNotNull(fromFileUserWrapper);
            List<User> readUserList = fromFileUserWrapper.getUserList();
            Assert.assertNotNull(readUserList);
            Assert.assertTrue(readUserList.size() == FLINTSTONE_USER_COUNT + SIMPSON_USER_COUNT);
            User retrievedDprUser = null;
            for (User user : readUserList) {
                if (user.getUserName().equals(DPR_USER_NAME)) {
                    retrievedDprUser = user;
                }
            }
            List<Permission> permissions = new ArrayList<Permission>();
            permissions.add(Permission.ADMIN_PERMISSION);
            Assert.assertNotNull(retrievedDprUser);
            for (User user : readUserList) {
                for (String[] element : FLINTSTONE_USER_ARRAY) {
                    if (user.getUserName().equals(element[USER_NAME])) {
                        validateUser(user, element, retrievedDprUser, permissions, false);
                        break;
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            Assert.fail();
        }
        dprClient.logOff(dprUser);
        dprClient.disconnectFromDataStore();
        return exportFile;
    }

    private void importThirdUserListToPF(File exportFile) throws Exception {
        DPRClient dprClient = new DPRClient();
        dprClient.setDataAccessManager(new HibernateDataAccessManager(dprClient));
        Map<String, String> connectionProperties;
        connectionProperties = dprClient.getConnectionProperties();
        setPropertiesForPF(connectionProperties);
        dprClient.connectToDataStore(connectionProperties);
        User dprUser = dprClient.logOn("dpr", "dpr", new TestCaseLogOnHandler());
        AdminUserTask newUserTask = (AdminUserTask) dprClient.getTaskByName(dprUser, AdminUserTask.TASK_NAME);
        UserImportExportWrapper userWrapper = UserImportExportWrapper.deserialiseFromFile(exportFile);
        UserDAO pfUserDAO = dprClient.getDataAccessManager().getUserDAO(newUserTask);
        List<User> wrapperUserList = userWrapper.getUserList();
        pfUserDAO.synchroniseAndMergeUsers(userWrapper.getUserList());
        List<User> pfUserList = pfUserDAO.getUserList();
        Assert.assertNotNull(wrapperUserList);
        User retrievedDPRUser = null;
        User retrievedSmithersUser = null;
        User retrievedLennyUser = null;
        for (User user : pfUserList) {
            if (user.getUserName().equals(DPR_USER_NAME)) {
                retrievedDPRUser = user;
            } else if (user.getUserName().equals(SMITHERS_USER_DATA[USER_NAME])) {
                retrievedSmithersUser = user;
            } else if (user.getUserName().equals(LENNY_USER_DATA[USER_NAME])) {
                retrievedLennyUser = user;
            }
        }
        Assert.assertNotNull(retrievedSmithersUser);
        Assert.assertNotNull(retrievedDPRUser);
        List<Permission> userPermissions = new ArrayList<Permission>();
        List<Permission> adminPermissions = new ArrayList<Permission>();
        adminPermissions.add(Permission.ADMIN_PERMISSION);
        for (User user : pfUserList) {
            for (String[] element : FLINTSTONE_USER_ARRAY) {
                if (user.getUserName().equals(element[USER_NAME])) {
                    validateUser(user, element, retrievedDPRUser, adminPermissions, false);
                    break;
                }
            }
            for (String[] element : SIMPSON_USER_ARRAY) {
                if (user.getUserName().equals(element[USER_NAME])) {
                    List<Permission> currentUserPermissions = userPermissions;
                    if (user.getUserName().equals(SMITHERS_USER_DATA[USER_NAME]) || user.getUserName().equals(LENNY_USER_DATA[USER_NAME])) {
                        currentUserPermissions = adminPermissions;
                    }
                    User creatingUser = retrievedSmithersUser;
                    if (user.getUserName().equals(SMITHERS_USER_DATA[USER_NAME])) {
                        creatingUser = null;
                    } else if (user.getUserName().equals(HOMER_USER_DATA[USER_NAME])) {
                        creatingUser = retrievedLennyUser;
                    }
                    validateUser(user, element, creatingUser, currentUserPermissions, false);
                    break;
                }
            }
        }
        dprClient.logOff(dprUser);
        dprClient.disconnectFromDataStore();
    }

    private User createAndPersistUser(UserDAO userDAO, String[] userData, User creatingUser, List<Permission> permissions) throws Exception {
        User newUser = new User();
        newUser.setUserName(userData[USER_NAME]);
        try {
            newUser.setNewPassword(userData[PASSWORD]);
        } catch (InvalidPasswordException e) {
            e.printStackTrace();
            Assert.fail();
        }
        newUser.setFirstName(userData[FIRST_NAME]);
        newUser.setLastName(userData[LAST_NAME]);
        newUser.setCreatedBy(creatingUser);
        Date dateCreated = new SimpleDateFormat("dd/MM/yyyy").parse(userData[DATE_CREATED]);
        newUser.setDateCreated(dateCreated);
        newUser.setDisabled(false);
        for (Permission permission : permissions) {
            newUser.addPermission(permission, creatingUser);
        }
        userDAO.saveUser(newUser);
        return newUser;
    }

    private void validateUser(User user, String[] userData, User creatingUser, List<Permission> permissions, boolean isDisabled) throws Exception {
        Assert.assertEquals(userData[USER_NAME], user.getUserName());
        Assert.assertEquals(userData[FIRST_NAME], user.getFirstName());
        Assert.assertEquals(userData[LAST_NAME], user.getLastName());
        Date dateUserCreated = new SimpleDateFormat("dd/MM/yyyy").parse(userData[DATE_CREATED]);
        Assert.assertEquals(dateUserCreated, user.getDateCreated());
        Assert.assertEquals(creatingUser, user.getCreatedBy());
        Assert.assertTrue(permissions.size() == user.getPermissions().size());
        for (Permission permission : permissions) {
            Assert.assertTrue(user.hasPermission(permission));
        }
        Collection<UserPermission> currentUserPermissions = user.getPermissions();
        for (UserPermission userPermission : currentUserPermissions) {
            Assert.assertNotNull(userPermission.getDateGranted());
            Assert.assertNotNull(userPermission.getPermission());
            if (creatingUser != null) {
                Assert.assertNotNull(userPermission.getGrantedBy());
                Assert.assertTrue(userPermission.getGrantedBy().equals(creatingUser));
            }
        }
        Assert.assertTrue(isDisabled == user.isDisabled());
    }
}
