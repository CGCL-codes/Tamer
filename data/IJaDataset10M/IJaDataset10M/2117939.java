package org.opencms.security;

import org.opencms.file.CmsGroup;
import org.opencms.file.CmsObject;
import org.opencms.file.CmsUser;
import org.opencms.i18n.CmsMessages;
import org.opencms.main.OpenCms;
import org.opencms.test.OpenCmsTestCase;
import org.opencms.test.OpenCmsTestProperties;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Tests the OpenCms system roles.<p>
 */
public class TestRoles extends OpenCmsTestCase {

    /**
     * Default JUnit constructor.<p>
     * 
     * @param arg0 JUnit parameters
     */
    public TestRoles(String arg0) {
        super(arg0);
    }

    /**
     * Test suite for this test class.<p>
     * Setup is done without importing vfs data.
     * 
     * @return the test suite
     */
    public static Test suite() {
        OpenCmsTestProperties.initialize(org.opencms.test.AllTests.TEST_PROPERTIES_PATH);
        TestSuite suite = new TestSuite();
        suite.setName(TestRoles.class.getName());
        suite.addTest(new TestRoles("testRoleExceptionMessages"));
        suite.addTest(new TestRoles("testRoleAssignments"));
        suite.addTest(new TestRoles("testSubRoles"));
        suite.addTest(new TestRoles("testVirtualRoleGroups"));
        suite.addTest(new TestRoles("testRoleDelegating"));
        TestSetup wrapper = new TestSetup(suite) {

            protected void setUp() {
                setupOpenCms("simpletest", "/sites/default/");
            }

            protected void tearDown() {
                removeOpenCms();
            }
        };
        return wrapper;
    }

    /**
     * Check the given message.<p>
     * 
     * @param message the message to check
     */
    private static void checkMessage(String message) {
        System.out.println(message);
        assertFalse(message.indexOf(CmsMessages.UNKNOWN_KEY_EXTENSION) >= 0);
        assertFalse(message.indexOf('{') >= 0);
    }

    /**
     * Tests role delegating.<p>
     * 
     * @throws Exception if the test fails
     */
    public void testRoleDelegating() throws Exception {
        echo("Testing role delegating");
        CmsObject cms = getCmsObject();
        CmsRoleManager roleMan = OpenCms.getRoleManager();
        CmsUser user = cms.createUser("testUser", "testUser", "testUser", null);
        roleMan.addUserToRole(cms, CmsRole.ACCOUNT_MANAGER.forOrgUnit(""), user.getName());
        cms.loginUser(user.getName(), "testUser");
        CmsUser u2 = cms.createUser("testUser2", "testUser2", "testUser2", null);
        try {
            roleMan.addUserToRole(cms, CmsRole.DEVELOPER.forOrgUnit(""), u2.getName());
            fail("it should not be possible to delegate a role you do not have");
        } catch (CmsRoleViolationException e) {
        }
        roleMan.addUserToRole(cms, CmsRole.ACCOUNT_MANAGER.forOrgUnit(""), u2.getName());
    }

    /**
     * Tests role assignments.<p>
     * 
     * @throws Exception if the test fails
     */
    public void testRoleAssignments() throws Exception {
        echo("Testing role assignments");
        CmsObject cms = getCmsObject();
        CmsRoleManager roleMan = OpenCms.getRoleManager();
        roleMan.checkRoleForResource(cms, CmsRole.ROOT_ADMIN, "/");
        roleMan.checkRole(cms, CmsRole.ROOT_ADMIN);
        roleMan.checkRoleForResource(cms, CmsRole.DEVELOPER, "/");
        roleMan.checkRole(cms, CmsRole.DEVELOPER.forOrgUnit(""));
        roleMan.checkRoleForResource(cms, CmsRole.WORKPLACE_MANAGER, "/");
        roleMan.checkRole(cms, CmsRole.WORKPLACE_MANAGER);
        assertFalse(roleMan.getManageableGroups(cms, "", false).isEmpty());
        assertFalse(roleMan.getManageableUsers(cms, "", false).isEmpty());
        assertFalse(roleMan.getOrgUnitsForRole(cms, CmsRole.ADMINISTRATOR.forOrgUnit(""), false).isEmpty());
        assertFalse(roleMan.getRolesOfUser(cms, cms.getRequestContext().currentUser().getName(), "", true, false, false).isEmpty());
        assertTrue(roleMan.getUsersOfRole(cms, CmsRole.ROOT_ADMIN, true, false).contains(cms.getRequestContext().currentUser()));
        assertTrue(roleMan.getUsersOfRole(cms, CmsRole.ADMINISTRATOR.forOrgUnit(""), true, true).isEmpty());
        assertEquals(1, roleMan.getUsersOfRole(cms, CmsRole.ADMINISTRATOR.forOrgUnit(""), true, false).size());
        CmsUser user = cms.readUser("test1");
        assertFalse(roleMan.hasRoleForResource(cms, user.getName(), CmsRole.ROOT_ADMIN, "/"));
        assertFalse(roleMan.hasRole(cms, user.getName(), CmsRole.ROOT_ADMIN));
        assertFalse(roleMan.hasRoleForResource(cms, user.getName(), CmsRole.DEVELOPER, "/"));
        assertFalse(roleMan.hasRole(cms, user.getName(), CmsRole.DEVELOPER.forOrgUnit("")));
        assertFalse(roleMan.hasRoleForResource(cms, user.getName(), CmsRole.WORKPLACE_MANAGER, "/"));
        assertFalse(roleMan.hasRole(cms, user.getName(), CmsRole.WORKPLACE_MANAGER));
        assertEquals(1, roleMan.getRolesOfUser(cms, user.getName(), "", true, false, false).size());
        assertFalse(roleMan.getUsersOfRole(cms, CmsRole.ROOT_ADMIN, true, false).contains(user));
        assertTrue(roleMan.getUsersOfRole(cms, CmsRole.ROOT_ADMIN, true, false).contains(cms.getRequestContext().currentUser()));
        assertTrue(roleMan.getUsersOfRole(cms, CmsRole.ADMINISTRATOR.forOrgUnit(""), true, false).contains(cms.getRequestContext().currentUser()));
        cms.loginUser(user.getName(), "test1");
        try {
            cms.createUser("mytest", "mytest", "my test", null);
            fail("the user should not have account management permissions");
        } catch (CmsRoleViolationException e) {
        }
        assertTrue(roleMan.getManageableGroups(cms, "", false).isEmpty());
        assertTrue(roleMan.getManageableUsers(cms, "", false).isEmpty());
        assertTrue(roleMan.getOrgUnitsForRole(cms, CmsRole.ADMINISTRATOR.forOrgUnit(""), false).isEmpty());
        cms = getCmsObject();
        roleMan.addUserToRole(cms, CmsRole.ADMINISTRATOR, user.getName());
        cms.loginUser(user.getName(), "test1");
        cms.createUser("mytest", "mytest", "my test", null);
        assertFalse(roleMan.hasRoleForResource(cms, user.getName(), CmsRole.ROOT_ADMIN, "/"));
        assertFalse(roleMan.hasRole(cms, user.getName(), CmsRole.ROOT_ADMIN));
        assertTrue(roleMan.hasRoleForResource(cms, user.getName(), CmsRole.DEVELOPER, "/"));
        assertTrue(roleMan.hasRole(cms, user.getName(), CmsRole.DEVELOPER.forOrgUnit("")));
        assertFalse(roleMan.hasRoleForResource(cms, user.getName(), CmsRole.WORKPLACE_MANAGER, "/"));
        assertFalse(roleMan.hasRole(cms, user.getName(), CmsRole.WORKPLACE_MANAGER));
        assertFalse(roleMan.getManageableGroups(cms, "", false).isEmpty());
        assertFalse(roleMan.getManageableUsers(cms, "", false).isEmpty());
        assertFalse(roleMan.getOrgUnitsForRole(cms, CmsRole.ADMINISTRATOR.forOrgUnit(""), false).isEmpty());
        assertFalse(roleMan.getRolesOfUser(cms, user.getName(), "", true, false, false).isEmpty());
        assertTrue(roleMan.getUsersOfRole(cms, CmsRole.ADMINISTRATOR.forOrgUnit(""), true, false).contains(cms.getRequestContext().currentUser()));
        assertTrue(roleMan.getUsersOfRole(cms, CmsRole.ACCOUNT_MANAGER.forOrgUnit(""), true, true).isEmpty());
        assertTrue(roleMan.getUsersOfRole(cms, CmsRole.ROOT_ADMIN, true, false).contains(cms.readUser("Admin")));
        assertFalse(roleMan.getUsersOfRole(cms, CmsRole.ROOT_ADMIN, true, false).contains(cms.getRequestContext().currentUser()));
    }

    /**
     * Tests if all keys in the system roles exception messages can be resolved.<p>
     * 
     * @throws Exception if the test fails
     */
    public void testRoleExceptionMessages() throws Exception {
        echo("Testing role exception messages");
        Iterator i = CmsRole.getSystemRoles().iterator();
        while (i.hasNext()) {
            CmsRole role = (CmsRole) i.next();
            checkMessage(role.getName(Locale.ENGLISH));
            checkMessage(role.getDescription(Locale.ENGLISH));
        }
        String roleName = "MY_VERY_SPECIAL_ROLE";
        CmsRole myRole = new CmsRole(roleName, null, OpenCms.getDefaultUsers().getGroupAdministrators(), true);
        checkMessage(myRole.getName(Locale.ENGLISH));
        checkMessage(myRole.getDescription(Locale.ENGLISH));
    }

    /**
     * Tests subroles operations.<p>
     * 
     * @throws Exception if the test fails
     */
    public void testSubRoles() throws Exception {
        echo("Testing subroles operations");
        CmsObject cms = getCmsObject();
        CmsRoleManager roleMan = OpenCms.getRoleManager();
        List adminRoles = roleMan.getRolesOfUser(cms, cms.getRequestContext().currentUser().getName(), "", false, true, false);
        assertEquals(1, adminRoles.size());
        assertTrue(adminRoles.contains(CmsRole.ROOT_ADMIN));
        roleMan.addUserToRole(cms, CmsRole.DEVELOPER.forOrgUnit(""), cms.getRequestContext().currentUser().getName());
        adminRoles = roleMan.getRolesOfUser(cms, cms.getRequestContext().currentUser().getName(), "", false, true, false);
        assertEquals(1, adminRoles.size());
        assertTrue(adminRoles.contains(CmsRole.ROOT_ADMIN));
        CmsUser user = cms.readUser("test2");
        List roles = roleMan.getRolesOfUser(cms, user.getName(), "", true, true, false);
        assertEquals(1, roles.size());
        assertTrue(roles.contains(CmsRole.WORKPLACE_USER.forOrgUnit(user.getOuFqn())));
        roleMan.addUserToRole(cms, CmsRole.VFS_MANAGER.forOrgUnit(user.getOuFqn()), user.getName());
        roles = roleMan.getRolesOfUser(cms, user.getName(), "", true, true, false);
        assertEquals(2, roles.size());
        assertTrue(roles.contains(CmsRole.VFS_MANAGER.forOrgUnit(user.getOuFqn())));
        assertTrue(roles.contains(CmsRole.WORKPLACE_USER.forOrgUnit(user.getOuFqn())));
        roles = roleMan.getRolesOfUser(cms, user.getName(), "", true, false, false);
        List children = CmsRole.VFS_MANAGER.forOrgUnit("").getChildren(true);
        children.add(CmsRole.VFS_MANAGER.forOrgUnit(""));
        children.addAll(CmsRole.WORKPLACE_USER.forOrgUnit("").getChildren(true));
        children.add(CmsRole.WORKPLACE_USER.forOrgUnit(""));
        assertEquals(children.size(), roles.size());
        Iterator it = roles.iterator();
        while (it.hasNext()) {
            CmsRole role = (CmsRole) it.next();
            assertTrue(children.contains(role));
        }
        roleMan.addUserToRole(cms, CmsRole.ADMINISTRATOR.forOrgUnit(user.getOuFqn()), user.getName());
        roles = roleMan.getRolesOfUser(cms, user.getName(), "", true, true, false);
        assertEquals(1, roles.size());
        assertTrue(roles.contains(CmsRole.ADMINISTRATOR.forOrgUnit(user.getOuFqn())));
        roles = roleMan.getRolesOfUser(cms, user.getName(), "", true, false, false);
        children = CmsRole.ADMINISTRATOR.forOrgUnit("").getChildren(true);
        children.add(CmsRole.ADMINISTRATOR.forOrgUnit(""));
        assertEquals(children.size(), roles.size());
        it = roles.iterator();
        while (it.hasNext()) {
            CmsRole role = (CmsRole) it.next();
            assertTrue(children.contains(role));
        }
    }

    /**
     * Tests virtual role groups.<p>
     * 
     * @throws Exception if the test fails
     */
    public void testVirtualRoleGroups() throws Exception {
        echo("Testing virtual role groups");
        CmsObject cms = getCmsObject();
        CmsGroup group = cms.createGroup("mytest", "vfs managers", CmsRole.VFS_MANAGER.getVirtualGroupFlags(), null);
        List roleUsers = OpenCms.getRoleManager().getUsersOfRole(cms, CmsRole.VFS_MANAGER.forOrgUnit(""), true, false);
        List groupUsers = cms.getUsersOfGroup(group.getName());
        assertEquals(new HashSet(roleUsers), new HashSet(groupUsers));
        OpenCms.getRoleManager().addUserToRole(cms, CmsRole.DEVELOPER.forOrgUnit(""), "Guest");
        assertEquals(new HashSet(roleUsers), new HashSet(cms.getUsersOfGroup(group.getName())));
        OpenCms.getRoleManager().addUserToRole(cms, CmsRole.ADMINISTRATOR.forOrgUnit(""), "Guest");
        assertEquals(groupUsers.size() + 1, cms.getUsersOfGroup(group.getName()).size());
        assertTrue(cms.getUsersOfGroup(group.getName()).contains(cms.readUser("Guest")));
        OpenCms.getRoleManager().removeUserFromRole(cms, CmsRole.ADMINISTRATOR.forOrgUnit(""), "Guest");
        groupUsers = cms.getUsersOfGroup(group.getName());
        assertEquals(new HashSet(roleUsers), new HashSet(groupUsers));
        cms.deleteGroup(group.getName());
        assertFalse(OpenCms.getOrgUnitManager().getGroups(cms, "", true).contains(group));
        assertEquals(Collections.singletonList(CmsRole.WORKPLACE_USER.forOrgUnit("")), OpenCms.getRoleManager().getRolesOfUser(cms, "Guest", "", true, true, true));
        OpenCms.getRoleManager().removeUserFromRole(cms, CmsRole.WORKPLACE_USER.forOrgUnit(""), "Guest");
        assertTrue(OpenCms.getRoleManager().getRolesOfUser(cms, "Guest", "", true, true, true).isEmpty());
        group = cms.createGroup("mytest", "vfs managers", CmsRole.VFS_MANAGER.getVirtualGroupFlags(), null);
        assertEquals(1, cms.getGroupsOfUser("Guest", false).size());
        assertTrue(OpenCms.getRoleManager().getRolesOfUser(cms, "Guest", "", true, true, true).isEmpty());
        cms.addUserToGroup("Guest", group.getName());
        assertEquals(3, cms.getGroupsOfUser("Guest", false).size());
        assertEquals(2, OpenCms.getRoleManager().getRolesOfUser(cms, "Guest", "", true, true, true).size());
        cms.removeUserFromGroup("Guest", group.getName());
        assertEquals(2, cms.getGroupsOfUser("Guest", false).size());
        assertEquals(1, OpenCms.getRoleManager().getRolesOfUser(cms, "Guest", "", true, true, true).size());
        assertEquals(1, OpenCms.getRoleManager().getRolesOfUser(cms, "Guest", "", true, true, true).size());
        assertTrue(OpenCms.getRoleManager().getRolesOfUser(cms, "Guest", "", true, true, true).contains(CmsRole.WORKPLACE_USER.forOrgUnit("")));
        OpenCms.getRoleManager().removeUserFromRole(cms, CmsRole.WORKPLACE_USER.forOrgUnit(""), "Guest");
        assertEquals(1, cms.getGroupsOfUser("Guest", false).size());
        assertTrue(OpenCms.getRoleManager().getRolesOfUser(cms, "Guest", "", true, true, true).isEmpty());
    }
}
