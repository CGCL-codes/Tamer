package com.ecyrd.jspwiki.auth;

import java.security.Principal;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
import org.apache.commons.lang.ArrayUtils;
import junit.framework.TestCase;
import com.ecyrd.jspwiki.*;
import com.ecyrd.jspwiki.auth.authorize.Group;
import com.ecyrd.jspwiki.auth.authorize.GroupManager;
import com.ecyrd.jspwiki.auth.permissions.PermissionFactory;
import com.ecyrd.jspwiki.auth.user.*;
import com.ecyrd.jspwiki.workflow.*;

/**
 * @author Andrew Jaquith
 */
public class UserManagerTest extends TestCase {

    private TestEngine m_engine;

    private UserManager m_mgr;

    private UserDatabase m_db;

    private String m_groupName;

    /**
   * @see junit.framework.TestCase#setUp()
   */
    protected void setUp() throws Exception {
        super.setUp();
        Properties props = new Properties();
        props.load(TestEngine.findTestProperties());
        props.remove("jspwiki.approver" + UserManager.SAVE_APPROVER);
        props.put(XMLUserDatabase.PROP_USERDATABASE, "tests/etc/userdatabase.xml");
        m_engine = new TestEngine(props);
        m_mgr = m_engine.getUserManager();
        m_db = m_mgr.getUserDatabase();
        m_groupName = "Group" + System.currentTimeMillis();
    }

    protected void tearDown() throws Exception {
        GroupManager groupManager = m_engine.getGroupManager();
        if (groupManager.findRole(m_groupName) != null) {
            groupManager.removeGroup(m_groupName);
        }
    }

    /** Call this setup program to use the save-profile workflow. */
    protected void setUpWithWorkflow() throws Exception {
        Properties props = new Properties();
        props.load(TestEngine.findTestProperties());
        props.put("jspwiki.approver." + UserManager.SAVE_APPROVER, "Admin");
        props.put(XMLUserDatabase.PROP_USERDATABASE, "tests/etc/userdatabase.xml");
        m_engine = new TestEngine(props);
        m_mgr = m_engine.getUserManager();
        m_db = m_mgr.getUserDatabase();
    }

    public void testSetRenamedUserProfile() throws Exception {
        int oldUserCount = m_db.getWikiNames().length;
        GroupManager groupManager = m_engine.getGroupManager();
        PageManager pageManager = m_engine.getPageManager();
        AuthorizationManager authManager = m_engine.getAuthorizationManager();
        int oldGroupCount = groupManager.getRoles().length;
        int oldPageCount = pageManager.getTotalPageCount();
        WikiSession session = m_engine.guestSession();
        long now = System.currentTimeMillis();
        String oldLogin = "TestLogin" + now;
        String oldName = "Test User " + now;
        String newLogin = "RenamedLogin" + now;
        String newName = "Renamed User " + now;
        UserProfile profile = m_db.newProfile();
        profile.setEmail("testuser@testville.com");
        profile.setLoginName(oldLogin);
        profile.setFullname(oldName);
        profile.setPassword("password");
        m_mgr.setUserProfile(session, profile);
        profile = m_mgr.getUserProfile(session);
        assertEquals(oldLogin, profile.getLoginName());
        assertEquals(oldName, profile.getFullname());
        assertEquals(oldUserCount + 1, m_db.getWikiNames().length);
        assertTrue(session.isAuthenticated());
        Group group = groupManager.parseGroup(m_groupName, "Alice \n Bob \n Charlie \n " + oldLogin + "\n" + oldName, true);
        groupManager.setGroup(session, group);
        assertEquals(oldGroupCount + 1, groupManager.getRoles().length);
        assertTrue(group.isMember(new WikiPrincipal(oldLogin)));
        assertTrue(group.isMember(new WikiPrincipal(oldName)));
        assertFalse(group.isMember(new WikiPrincipal(newLogin)));
        assertFalse(group.isMember(new WikiPrincipal(newName)));
        assertTrue(groupManager.isUserInRole(session, group.getPrincipal()));
        String pageName = "TestPage" + now;
        m_engine.saveText(pageName, "Test text. [{ALLOW view " + oldName + ", " + oldLogin + ", Alice}] More text.");
        WikiPage p = m_engine.getPage(pageName);
        assertEquals(oldPageCount + 1, pageManager.getTotalPageCount());
        assertNotNull(p.getAcl().getEntry(new WikiPrincipal(oldLogin)));
        assertNotNull(p.getAcl().getEntry(new WikiPrincipal(oldName)));
        assertNull(p.getAcl().getEntry(new WikiPrincipal(newLogin)));
        assertNull(p.getAcl().getEntry(new WikiPrincipal(newName)));
        assertTrue("Test User view page", authManager.checkPermission(session, PermissionFactory.getPagePermission(p, "view")));
        WikiSession bobSession = WikiSessionTest.authenticatedSession(m_engine, Users.BOB, Users.BOB_PASS);
        assertFalse("Bob !view page", authManager.checkPermission(bobSession, PermissionFactory.getPagePermission(p, "view")));
        profile = m_db.newProfile();
        profile.setEmail("testuser@testville.com");
        profile.setLoginName(oldLogin);
        profile.setFullname(newName);
        profile.setPassword("password");
        m_mgr.setUserProfile(session, profile);
        Principal[] principals = session.getPrincipals();
        assertTrue(ArrayUtils.contains(principals, new WikiPrincipal(oldLogin)));
        assertFalse(ArrayUtils.contains(principals, new WikiPrincipal(oldName)));
        assertFalse(ArrayUtils.contains(principals, new WikiPrincipal(newLogin)));
        assertTrue(ArrayUtils.contains(principals, new WikiPrincipal(newName)));
        group = groupManager.getGroup(m_groupName);
        assertFalse(group.isMember(new WikiPrincipal(oldLogin)));
        assertFalse(group.isMember(new WikiPrincipal(oldName)));
        assertFalse(group.isMember(new WikiPrincipal(newLogin)));
        assertTrue(group.isMember(new WikiPrincipal(newName)));
        p = m_engine.getPage(pageName);
        assertNull(p.getAcl().getEntry(new WikiPrincipal(oldLogin)));
        assertNull(p.getAcl().getEntry(new WikiPrincipal(oldName)));
        assertNull(p.getAcl().getEntry(new WikiPrincipal(newLogin)));
        assertNotNull(p.getAcl().getEntry(new WikiPrincipal(newName)));
        assertTrue("Test User view page", authManager.checkPermission(session, PermissionFactory.getPagePermission(p, "view")));
        assertFalse("Bob !view page", authManager.checkPermission(bobSession, PermissionFactory.getPagePermission(p, "view")));
        String expectedText = "[{ALLOW view Alice," + newName + "}]\nTest text.  More text.\r\n";
        String actualText = m_engine.getText(pageName);
        assertEquals(expectedText, actualText);
        m_engine.deletePage(pageName);
        group = groupManager.parseGroup(m_groupName, "Alice \n Bob \n Charlie \n " + oldLogin + "\n" + oldName, true);
        groupManager.setGroup(session, group);
        pageName = "TestPage2" + now;
        m_engine.saveText(pageName, "More test text. [{ALLOW view " + oldName + ", " + oldLogin + ", Alice}] More text.");
        p = m_engine.getPage(pageName);
        assertEquals(oldPageCount + 1, pageManager.getTotalPageCount());
        assertNotNull(p.getAcl().getEntry(new WikiPrincipal(oldLogin)));
        assertNotNull(p.getAcl().getEntry(new WikiPrincipal(oldName)));
        assertNull(p.getAcl().getEntry(new WikiPrincipal(newLogin)));
        assertNull(p.getAcl().getEntry(new WikiPrincipal(newName)));
        assertTrue("Test User view page", authManager.checkPermission(session, PermissionFactory.getPagePermission(p, "view")));
        assertFalse("Bob !view page", authManager.checkPermission(bobSession, PermissionFactory.getPagePermission(p, "view")));
        profile = m_db.newProfile();
        profile.setEmail("testuser@testville.com");
        profile.setLoginName(newLogin);
        profile.setFullname(oldName);
        profile.setPassword("password");
        m_mgr.setUserProfile(session, profile);
        principals = session.getPrincipals();
        assertFalse(ArrayUtils.contains(principals, new WikiPrincipal(oldLogin)));
        assertTrue(ArrayUtils.contains(principals, new WikiPrincipal(oldName)));
        assertTrue(ArrayUtils.contains(principals, new WikiPrincipal(newLogin)));
        assertFalse(ArrayUtils.contains(principals, new WikiPrincipal(newName)));
        group = groupManager.getGroup(m_groupName);
        assertFalse(group.isMember(new WikiPrincipal(oldLogin)));
        assertTrue(group.isMember(new WikiPrincipal(oldName)));
        assertFalse(group.isMember(new WikiPrincipal(newLogin)));
        assertFalse(group.isMember(new WikiPrincipal(newName)));
        p = m_engine.getPage(pageName);
        assertNull(p.getAcl().getEntry(new WikiPrincipal(oldLogin)));
        assertNotNull(p.getAcl().getEntry(new WikiPrincipal(oldName)));
        assertNull(p.getAcl().getEntry(new WikiPrincipal(newLogin)));
        assertNull(p.getAcl().getEntry(new WikiPrincipal(newName)));
        assertTrue("Test User view page", authManager.checkPermission(session, PermissionFactory.getPagePermission(p, "view")));
        assertFalse("Bob !view page", authManager.checkPermission(bobSession, PermissionFactory.getPagePermission(p, "view")));
        expectedText = "[{ALLOW view Alice," + oldName + "}]\nMore test text.  More text.\r\n";
        actualText = m_engine.getText(pageName);
        assertEquals(expectedText, actualText);
        m_db.deleteByLoginName(newLogin);
        assertEquals(oldUserCount, m_db.getWikiNames().length);
        groupManager.removeGroup(group.getName());
        assertEquals(oldGroupCount, groupManager.getRoles().length);
        m_engine.deletePage(pageName);
        assertEquals(oldPageCount, pageManager.getTotalPageCount());
    }

    public void testSetUserProfile() throws Exception {
        int oldUserCount = m_db.getWikiNames().length;
        WikiSession session = m_engine.guestSession();
        String loginName = "TestUser" + String.valueOf(System.currentTimeMillis());
        UserProfile profile = m_db.newProfile();
        profile.setEmail("testuser@testville.com");
        profile.setLoginName(loginName);
        profile.setFullname("FullName" + loginName);
        profile.setPassword("password");
        m_mgr.setUserProfile(session, profile);
        profile = m_mgr.getUserProfile(session);
        assertEquals(loginName, profile.getLoginName());
        assertEquals(oldUserCount + 1, m_db.getWikiNames().length);
        m_db.deleteByLoginName(loginName);
        assertEquals(oldUserCount, m_db.getWikiNames().length);
    }

    public void testSetUserProfileWithApproval() throws Exception {
        setUpWithWorkflow();
        int oldUserCount = m_db.getWikiNames().length;
        WikiSession session = m_engine.guestSession();
        String loginName = "TestUser" + String.valueOf(System.currentTimeMillis());
        UserProfile profile = m_db.newProfile();
        profile.setEmail("testuser@testville.com");
        profile.setLoginName(loginName);
        profile.setFullname("FullName" + loginName);
        profile.setPassword("password");
        try {
            m_mgr.setUserProfile(session, profile);
            fail("We should have caught a DecisionRequiredException caused by approval!");
        } catch (DecisionRequiredException e) {
        }
        assertEquals(oldUserCount, m_db.getWikiNames().length);
        DecisionQueue dq = m_engine.getWorkflowManager().getDecisionQueue();
        Collection decisions = dq.getActorDecisions(m_engine.adminSession());
        assertEquals(1, decisions.size());
        Decision d = (Decision) decisions.iterator().next();
        List facts = d.getFacts();
        assertEquals(new Fact(UserManager.PREFS_FULL_NAME, profile.getFullname()), facts.get(0));
        assertEquals(new Fact(UserManager.PREFS_LOGIN_NAME, profile.getLoginName()), facts.get(1));
        assertEquals(new Fact(UserManager.FACT_SUBMITTER, session.getUserPrincipal().getName()), facts.get(2));
        assertEquals(new Fact(UserManager.PREFS_EMAIL, profile.getEmail()), facts.get(3));
        assertEquals(profile, d.getWorkflow().getAttribute(UserManager.SAVED_PROFILE));
        d.decide(Outcome.DECISION_APPROVE);
        assertEquals(oldUserCount + 1, m_db.getWikiNames().length);
        m_db.deleteByLoginName(loginName);
        assertEquals(oldUserCount, m_db.getWikiNames().length);
    }

    public void testSetUserProfileWithDenial() throws Exception {
        setUpWithWorkflow();
        int oldUserCount = m_db.getWikiNames().length;
        WikiSession session = m_engine.guestSession();
        String loginName = "TestUser" + String.valueOf(System.currentTimeMillis());
        UserProfile profile = m_db.newProfile();
        profile.setEmail("testuser@testville.com");
        profile.setLoginName(loginName);
        profile.setFullname("FullName" + loginName);
        profile.setPassword("password");
        try {
            m_mgr.setUserProfile(session, profile);
            fail("We should have caught a DecisionRequiredException caused by approval!");
        } catch (DecisionRequiredException e) {
        }
        assertEquals(oldUserCount, m_db.getWikiNames().length);
        DecisionQueue dq = m_engine.getWorkflowManager().getDecisionQueue();
        Collection decisions = dq.getActorDecisions(m_engine.adminSession());
        assertEquals(1, decisions.size());
        Decision d = (Decision) decisions.iterator().next();
        List facts = d.getFacts();
        assertEquals(new Fact(UserManager.PREFS_FULL_NAME, profile.getFullname()), facts.get(0));
        assertEquals(new Fact(UserManager.PREFS_LOGIN_NAME, profile.getLoginName()), facts.get(1));
        assertEquals(new Fact(UserManager.FACT_SUBMITTER, session.getUserPrincipal().getName()), facts.get(2));
        assertEquals(new Fact(UserManager.PREFS_EMAIL, profile.getEmail()), facts.get(3));
        assertEquals(profile, d.getWorkflow().getAttribute(UserManager.SAVED_PROFILE));
        d.decide(Outcome.DECISION_DENY);
        assertEquals(oldUserCount, m_db.getWikiNames().length);
    }

    public void testSetCollidingUserProfile() throws Exception {
        int oldUserCount = m_db.getWikiNames().length;
        WikiSession session = m_engine.guestSession();
        String loginName = "TestUser" + String.valueOf(System.currentTimeMillis());
        UserProfile profile = m_db.newProfile();
        profile.setEmail("testuser@testville.com");
        profile.setLoginName(loginName);
        profile.setFullname("FullName" + loginName);
        profile.setPassword("password");
        profile.setLoginName("janne");
        try {
            m_mgr.setUserProfile(session, profile);
            fail("UserManager allowed saving of user with login name 'janne', but it shouldn't have.");
        } catch (DuplicateUserException e) {
            profile.setLoginName(loginName);
        }
        profile.setFullname("Janne Jalkanen");
        try {
            m_mgr.setUserProfile(session, profile);
            fail("UserManager allowed saving of user with login name 'janne', but it shouldn't have.");
        } catch (DuplicateUserException e) {
        }
        assertEquals(oldUserCount, m_db.getWikiNames().length);
    }
}
