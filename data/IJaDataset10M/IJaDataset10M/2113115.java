package org.openuss.security.acl;

import org.acegisecurity.AcegiSecurityException;
import org.acegisecurity.acl.basic.SimpleAclEntry;
import org.acegisecurity.context.SecurityContextHolder;
import org.acegisecurity.providers.UsernamePasswordAuthenticationToken;
import org.acegisecurity.providers.dao.cache.EhCacheBasedUserCache;
import org.acegisecurity.providers.encoding.Md5PasswordEncoder;
import org.apache.log4j.Logger;
import org.openuss.TestUtility;
import org.openuss.lecture.Institute;
import org.openuss.lecture.InstituteImpl;
import org.openuss.lecture.InstituteService;
import org.openuss.lecture.InstituteServiceException;
import org.openuss.security.Group;
import org.openuss.security.GroupDao;
import org.openuss.security.Roles;
import org.openuss.security.User;
import org.openuss.security.UserDao;
import org.springframework.test.AbstractTransactionalDataSourceSpringContextTests;

/**
 * 
 * @author Ingo Dueppe
 */
public class AclPermissionIntegrationTest extends AbstractTransactionalDataSourceSpringContextTests {

    private static final String TEST_ROLE_ADMIN = "ROLE_ADMIN";

    private static final String TEST_ADMIN = "test_admin";

    private static final Logger logger = Logger.getLogger(AclPermissionIntegrationTest.class);

    private InstituteService instituteService;

    private UserDao userDao;

    private GroupDao groupDao;

    private PermissionDao permissionDao;

    private ObjectIdentityDao objectIdentityDao;

    private Institute institute;

    private Group roleUser;

    private Group groupInstitute;

    private EhCacheBasedUserCache cache;

    private TestUtility testUtility;

    private long instituteTestID;

    @Override
    protected void onSetUpInTransaction() throws Exception {
        instituteTestID = TestUtility.unique();
        cache.getCache().flush();
        createSecureContext(TEST_ADMIN, TEST_ROLE_ADMIN);
        institute = new InstituteImpl();
        institute.setId(instituteTestID);
    }

    public void testAclAccessDeniedMethodInvocation() {
        try {
            instituteService.findInstitute(institute.getId());
            fail();
        } catch (AcegiSecurityException ase) {
            logger.error(ase);
        } catch (InstituteServiceException ise) {
            fail(ise.getMessage());
            ise.printStackTrace();
        }
    }

    public void testAclAccessGrantedToUserCheckedMethodInvocation() {
        ObjectIdentity oi = ObjectIdentity.Factory.newInstance();
        oi.setId(instituteTestID);
        Permission permission = Permission.Factory.newInstance();
        permission.setPermissionPk(new PermissionPK());
        permission.getPermissionPk().setAclObjectIdentity(oi);
        permission.setMask(SimpleAclEntry.READ | SimpleAclEntry.ADMINISTRATION);
        permission.getPermissionPk().setRecipient(roleUser);
        objectIdentityDao.create(oi);
        try {
            instituteService.findInstitute(institute.getId());
            fail();
        } catch (AcegiSecurityException ase) {
            logger.info("access denied");
        } catch (InstituteServiceException ise) {
            fail(ise.getMessage());
        }
        oi.addPermission(permission);
        permissionDao.create(permission);
        try {
            instituteService.findInstitute(institute.getId());
            logger.info("access granted");
        } catch (AcegiSecurityException ase) {
            logger.error(ase);
            fail(ase.getMessage());
        } catch (InstituteServiceException ise) {
            logger.info("access granted");
        }
    }

    public void testAclHirarchyAccessGrantedToUserCheckedMethodInvocation() {
        ObjectIdentity oi = ObjectIdentity.Factory.newInstance();
        oi.setId(instituteTestID);
        Permission permission = Permission.Factory.newInstance();
        permission.setPermissionPk(new PermissionPK());
        permission.getPermissionPk().setAclObjectIdentity(oi);
        permission.setMask(SimpleAclEntry.READ | SimpleAclEntry.ADMINISTRATION);
        permission.getPermissionPk().setRecipient(roleUser);
        objectIdentityDao.create(oi);
        oi.addPermission(permission);
        long oid = TestUtility.unique();
        ObjectIdentity oi2 = ObjectIdentity.Factory.newInstance();
        oi2.setId(oid);
        oi2.setParent(oi);
        objectIdentityDao.create(oi2);
        permissionDao.create(permission);
        Institute institute2 = new InstituteImpl();
        institute2.setId(oid);
        try {
            instituteService.findInstitute(institute.getId());
            logger.info("access granted");
        } catch (AcegiSecurityException ase) {
            logger.error(ase);
            fail(ase.getMessage());
        } catch (InstituteServiceException ise) {
            logger.info("access granted");
        }
    }

    public void testAclHirarchyAccessGrantedToRoleCheckedMethodInvocation() {
        ObjectIdentity oi = ObjectIdentity.Factory.newInstance();
        oi.setId(instituteTestID);
        Permission permission = Permission.Factory.newInstance();
        permission.setPermissionPk(new PermissionPK());
        permission.getPermissionPk().setAclObjectIdentity(oi);
        permission.setMask(SimpleAclEntry.READ | SimpleAclEntry.ADMINISTRATION);
        permission.getPermissionPk().setRecipient(groupInstitute);
        objectIdentityDao.create(oi);
        oi.addPermission(permission);
        long oid = TestUtility.unique();
        ObjectIdentity oi2 = ObjectIdentity.Factory.newInstance();
        oi2.setId(oid);
        oi2.setParent(oi);
        objectIdentityDao.create(oi2);
        permissionDao.create(permission);
        Institute institute2 = new InstituteImpl();
        institute2.setId(oid);
        try {
            instituteService.findInstitute(institute.getId());
            logger.info("access granted");
        } catch (Exception exception) {
            logger.error("Exception occure during findInstitute", exception);
            fail(exception.getMessage());
        }
    }

    private void createSecureContext(String username, String rolename) {
        Md5PasswordEncoder encoder = new Md5PasswordEncoder();
        String password = encoder.encodePassword("password", null);
        User user = User.Factory.newInstance();
        user.setUsername(username);
        user.setPassword(password);
        user.setFirstName("firstName");
        user.setLastName("lastName");
        user.setEmail(testUtility.unique("email"));
        user.setEnabled(true);
        roleUser = groupDao.load(Roles.USER_ID);
        user.addGroup(roleUser);
        roleUser.addMember(user);
        userDao.create(user);
        groupDao.create(roleUser);
        assertNotNull(user.getId());
        assertNotNull(roleUser.getId());
        groupInstitute = Group.Factory.newInstance();
        groupInstitute.setName("GROUP_INSTITUTE");
        groupInstitute.setLabel("Group label of the Authory");
        groupInstitute.addMember(user);
        user.addGroup(groupInstitute);
        groupDao.create(groupInstitute);
        userDao.update(user);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username, "password");
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Override
    protected String[] getConfigLocations() {
        return new String[] { "classpath*:applicationContext.xml", "classpath*:applicationContext-beans.xml", "classpath*:applicationContext-lucene.xml", "classpath*:applicationContext-cache.xml", "classpath*:applicationContext-messaging.xml", "classpath*:applicationContext-resources.xml", "classpath*:testContext.xml", "classpath*:testAclSecurity.xml", "classpath*:testDataSource.xml" };
    }

    public InstituteService getInstituteService() {
        return instituteService;
    }

    public void setInstituteService(InstituteService instituteService) {
        this.instituteService = instituteService;
    }

    public GroupDao getGroupDao() {
        return groupDao;
    }

    public void setGroupDao(GroupDao groupDao) {
        this.groupDao = groupDao;
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public ObjectIdentityDao getObjectIdentityDao() {
        return objectIdentityDao;
    }

    public void setObjectIdentityDao(ObjectIdentityDao objectIdentityDao) {
        this.objectIdentityDao = objectIdentityDao;
    }

    public PermissionDao getPermissionDao() {
        return permissionDao;
    }

    public void setPermissionDao(PermissionDao permissionDao) {
        this.permissionDao = permissionDao;
    }

    public EhCacheBasedUserCache getCache() {
        return cache;
    }

    public void setCache(EhCacheBasedUserCache cache) {
        this.cache = cache;
    }

    public TestUtility getTestUtility() {
        return testUtility;
    }

    public void setTestUtility(TestUtility testUtility) {
        this.testUtility = testUtility;
    }
}
