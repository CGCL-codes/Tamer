package csiebug.test.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Resource;
import javax.sql.DataSource;
import static org.junit.Assert.*;
import org.easymock.EasyMock;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import csiebug.dao.DAOException;
import csiebug.dao.UserDAO;
import csiebug.domain.Cookie;
import csiebug.domain.Dashboard;
import csiebug.domain.DashboardPortlet;
import csiebug.domain.DomainObjectFactory;
import csiebug.domain.User;
import csiebug.domain.UserEmail;
import csiebug.service.UserService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:springTest.xml" })
public class UserServiceTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    private UserService userService;

    @Autowired
    private DomainObjectFactory domainObjectFactory;

    @Autowired
    private UserDAO userDAO;

    private UserDAO mockUserDAO;

    @Resource(name = "dataSource")
    public void setDataSource(DataSource dataSource) {
        super.setDataSource(dataSource);
    }

    private void setUp() {
        mockUserDAO = EasyMock.createStrictMock(UserDAO.class);
        userService.setUserDAO(mockUserDAO);
    }

    private void tearDown() {
        EasyMock.verify(mockUserDAO);
        userService.setUserDAO(userDAO);
    }

    private User prepareUserData(boolean insertFlag) throws Exception {
        User user = domainObjectFactory.createUser();
        user.setId("test");
        user.setPassword("testPassword");
        user.setEnabled(true);
        user.setCreateUserId("admin");
        user.setCreateDate(Calendar.getInstance());
        if (insertFlag) {
            userDAO.insert(user);
        }
        return user;
    }

    private void setUpForSearchUsers(User voObj, User user) throws DAOException {
        setUp();
        List<User> expectList = new ArrayList<User>();
        expectList.add(user);
        EasyMock.expect(mockUserDAO.search(voObj)).andReturn(expectList);
        EasyMock.replay(mockUserDAO);
    }

    @Test
    public void testSearchUsers() throws Exception {
        User user = prepareUserData(false);
        User voObj = domainObjectFactory.createUser();
        voObj.setId("test");
        setUpForSearchUsers(voObj, user);
        List<User> list = userService.searchUsers(voObj);
        assertEquals(1, list.size());
        assertEquals(user, list.get(0));
        tearDown();
    }

    private List<User> prepareUserPaginationData(boolean insertFlag) throws Exception {
        List<User> list = new ArrayList<User>();
        for (int i = 0; i < 10; i++) {
            User user = domainObjectFactory.createUser();
            user.setId("test_" + i);
            user.setPassword("testPassword_" + i);
            user.setEnabled(true);
            user.setCreateUserId("admin");
            user.setCreateDate(Calendar.getInstance());
            if (insertFlag) {
                userDAO.insert(user);
            }
            list.add(user);
        }
        return list;
    }

    private List<User> prepareUserPaginationData(int firstResult, int maxResults) throws Exception {
        List<User> list = new ArrayList<User>();
        for (int i = firstResult; i < (firstResult + maxResults); i++) {
            User user = domainObjectFactory.createUser();
            user.setId("test_" + i);
            user.setPassword("testPassword_" + i);
            user.setEnabled(true);
            user.setCreateUserId("admin");
            user.setCreateDate(Calendar.getInstance());
            list.add(user);
        }
        return list;
    }

    private void setUpForSearchUsersPagination(User voObj, List<User> list) throws Exception {
        setUp();
        EasyMock.expect(mockUserDAO.searchRowCount(voObj)).andReturn(list.size());
        for (int i = 0; i < 10; i = i + 2) {
            EasyMock.expect(mockUserDAO.searchPagination(voObj, i, 2)).andReturn(prepareUserPaginationData(i, 2));
        }
        EasyMock.replay(mockUserDAO);
    }

    @Test
    public void testSearchUsersPagination() throws Exception {
        List<User> list = prepareUserPaginationData(false);
        User voObj = domainObjectFactory.createUser();
        setUpForSearchUsersPagination(voObj, list);
        List<User> list2 = userService.searchUsersPagination(voObj, 2);
        assertEquals(list.size(), list2.size());
        for (int i = 0; i < list.size(); i++) {
            assertEquals(list.get(i), list2.get(i));
        }
        tearDown();
    }

    private void setUpForSaveUser(User user) throws DAOException {
        setUp();
        mockUserDAO.insertOrUpdate(user);
        List<User> expectList = new ArrayList<User>();
        expectList.add(user);
        EasyMock.expect(mockUserDAO.search(user)).andReturn(expectList);
        EasyMock.replay(mockUserDAO);
    }

    @Test
    public void testSaveUser() throws Exception {
        User user = domainObjectFactory.createUser();
        user.setId("test");
        user.setPassword("testPassword");
        user.setEnabled(true);
        user.setCreateUserId("admin");
        user.setCreateDate(Calendar.getInstance());
        setUpForSaveUser(user);
        userService.saveUser(user);
        List<User> list = userService.searchUsers(user);
        assertEquals(1, list.size());
        assertEquals(user, list.get(0));
        tearDown();
    }

    private void setUpForDeleteUser(User user) throws DAOException {
        setUp();
        List<User> expectList = new ArrayList<User>();
        expectList.add(user);
        EasyMock.expect(mockUserDAO.search(user)).andReturn(expectList);
        List<DashboardPortlet> portlets = new ArrayList<DashboardPortlet>();
        Iterator<Dashboard> iterator = user.getDashboards().iterator();
        while (iterator.hasNext()) {
            Dashboard dashboard = iterator.next();
            portlets.addAll(dashboard.getPortlets());
        }
        mockUserDAO.deleteDashboardPortlet(portlets);
        mockUserDAO.deleteDashboard(user.getDashboards());
        mockUserDAO.deleteUserEmail(user.getUserEmails());
        mockUserDAO.delete(user);
        expectList = new ArrayList<User>();
        EasyMock.expect(mockUserDAO.search(user)).andReturn(expectList);
        EasyMock.replay(mockUserDAO);
    }

    @Test
    public void testDeleteUser() throws Exception {
        User user = prepareUserData(false);
        setUpForDeleteUser(user);
        List<User> list = userService.searchUsers(user);
        assertEquals(1, list.size());
        assertEquals(user, list.get(0));
        userService.deleteUser(user);
        list = userService.searchUsers(user);
        assertEquals(0, list.size());
        tearDown();
    }

    private Cookie prepareCookieData(User user, boolean insertFlag) throws Exception {
        Cookie cookie = domainObjectFactory.createCookie();
        cookie.setUserId(user.getId());
        cookie.setToken("123456789012345678901234567890123456789012345678");
        cookie.setSeries("testSerial");
        cookie.setLastUsed(Calendar.getInstance());
        user.addCookie(cookie);
        if (insertFlag) {
            userDAO.insert(cookie);
        }
        return cookie;
    }

    private void setUpForDeleteCookie(User user, Cookie cookie) throws DAOException {
        setUp();
        List<User> expectList = new ArrayList<User>();
        expectList.add(user);
        EasyMock.expect(mockUserDAO.search(user)).andReturn(expectList);
        mockUserDAO.delete(cookie);
        expectList = new ArrayList<User>();
        expectList.add(user);
        EasyMock.expect(mockUserDAO.search(user)).andReturn(expectList);
        EasyMock.replay(mockUserDAO);
    }

    @Test
    public void testDeleteCookie() throws Exception {
        User user = prepareUserData(false);
        Cookie cookie = prepareCookieData(user, false);
        setUpForDeleteCookie(user, cookie);
        List<User> list = userService.searchUsers(user);
        assertEquals(1, list.size());
        assertEquals(user, list.get(0));
        assertEquals(1, list.get(0).getCookies().size());
        assertEquals(cookie, list.get(0).getCookie("testSerial"));
        user.removeCookie(cookie);
        userService.deleteCookie(cookie);
        list = userService.searchUsers(user);
        assertEquals(1, list.size());
        assertEquals(user, list.get(0));
        assertEquals(0, list.get(0).getCookies().size());
        tearDown();
    }

    private void setUpForAddCookie(User user, Cookie cookie) throws DAOException {
        setUp();
        mockUserDAO.insert(cookie);
        List<User> expectList = new ArrayList<User>();
        expectList.add(user);
        EasyMock.expect(mockUserDAO.search(user)).andReturn(expectList);
        EasyMock.replay(mockUserDAO);
    }

    @Test
    public void testAddCookie() throws Exception {
        User user = prepareUserData(false);
        Cookie cookie = domainObjectFactory.createCookie();
        cookie.setUserId(user.getId());
        cookie.setToken("123456789012345678901234567890123456789012345678");
        cookie.setSeries("testSerial");
        cookie.setLastUsed(Calendar.getInstance());
        setUpForAddCookie(user, cookie);
        user.addCookie(cookie);
        userService.addCookie(cookie);
        List<User> list = userService.searchUsers(user);
        assertEquals(1, list.size());
        assertEquals(user, list.get(0));
        assertEquals(1, list.get(0).getCookies().size());
        assertEquals(cookie, list.get(0).getCookie("testSerial"));
        tearDown();
    }

    private void setUpForUpdateCookie(User user, Cookie cookie) throws DAOException {
        setUp();
        mockUserDAO.update(cookie);
        List<User> expectList = new ArrayList<User>();
        expectList.add(user);
        EasyMock.expect(mockUserDAO.search(user)).andReturn(expectList);
        EasyMock.replay(mockUserDAO);
    }

    @Test
    public void testUpdateCookie() throws Exception {
        User user = prepareUserData(false);
        Cookie cookie = prepareCookieData(user, false);
        String token = "098765432109876543210987654321098765432109876543";
        cookie.setToken(token);
        setUpForUpdateCookie(user, cookie);
        userService.updateCookie(cookie);
        List<User> list = userService.searchUsers(user);
        assertEquals(1, list.size());
        assertEquals(user, list.get(0));
        assertEquals(1, list.get(0).getCookies().size());
        assertEquals(cookie, list.get(0).getCookie("testSerial"));
        assertEquals(token, list.get(0).getCookie("testSerial").getToken());
        tearDown();
    }

    private Dashboard prepareDashboardData(User user, boolean insertFlag) throws Exception {
        Dashboard dashboard = domainObjectFactory.createDashboard();
        dashboard.setUserId(user.getId());
        dashboard.setDashboardId("testDashboardId");
        dashboard.setCreateUserId(user.getId());
        dashboard.setCreateDate(Calendar.getInstance());
        user.addDashboard(dashboard);
        if (insertFlag) {
            userDAO.insert(dashboard);
        }
        return dashboard;
    }

    private void setUpDeleteDashboard(User user, Dashboard dashboard) throws DAOException {
        setUp();
        List<User> expectList = new ArrayList<User>();
        expectList.add(user);
        EasyMock.expect(mockUserDAO.search(user)).andReturn(expectList);
        mockUserDAO.deleteDashboardPortlet(dashboard.getPortlets());
        mockUserDAO.delete(dashboard);
        expectList = new ArrayList<User>();
        expectList.add(user);
        EasyMock.expect(mockUserDAO.search(user)).andReturn(expectList);
        EasyMock.replay(mockUserDAO);
    }

    @Test
    public void testDeleteDashboard() throws Exception {
        User user = prepareUserData(false);
        Dashboard dashboard = prepareDashboardData(user, false);
        setUpDeleteDashboard(user, dashboard);
        List<User> list = userService.searchUsers(user);
        assertEquals(1, list.size());
        assertEquals(user, list.get(0));
        assertEquals(1, list.get(0).getDashboards().size());
        assertEquals(dashboard, list.get(0).getDashboard("testDashboardId"));
        user.removeDashboard(dashboard);
        userService.deleteDashboard(dashboard);
        list = userService.searchUsers(user);
        assertEquals(1, list.size());
        assertEquals(user, list.get(0));
        assertEquals(0, list.get(0).getDashboards().size());
        tearDown();
    }

    private void setUpForAddDashboard(User user, Dashboard dashboard) throws DAOException {
        setUp();
        mockUserDAO.insert(dashboard);
        List<User> expectList = new ArrayList<User>();
        expectList.add(user);
        EasyMock.expect(mockUserDAO.search(user)).andReturn(expectList);
        EasyMock.replay(mockUserDAO);
    }

    @Test
    public void testAddDashboard() throws Exception {
        User user = prepareUserData(false);
        Dashboard dashboard = domainObjectFactory.createDashboard();
        dashboard.setUserId(user.getId());
        dashboard.setDashboardId("testDashboardId");
        dashboard.setCreateUserId(user.getId());
        dashboard.setCreateDate(Calendar.getInstance());
        setUpForAddDashboard(user, dashboard);
        user.addDashboard(dashboard);
        userService.addDashboard(dashboard);
        List<User> list = userService.searchUsers(user);
        assertEquals(1, list.size());
        assertEquals(user, list.get(0));
        assertEquals(1, list.get(0).getDashboards().size());
        assertEquals(dashboard, list.get(0).getDashboard("testDashboardId"));
        tearDown();
    }

    private DashboardPortlet prepareDashboardPortletData(User user, boolean insertFlag) throws Exception {
        DashboardPortlet portlet = domainObjectFactory.createDashboardPortlet();
        portlet.setUserId(user.getId());
        portlet.setDashboardId("testDashboardId");
        portlet.setPortletId("testPortletId");
        portlet.setVisible(true);
        portlet.setCreateUserId(user.getId());
        portlet.setCreateDate(Calendar.getInstance());
        portlet.setDashboard(user.getDashboard("testDashboardId"));
        user.getDashboard("testDashboardId").addPortlet(portlet);
        if (insertFlag) {
            userDAO.insert(portlet);
        }
        return portlet;
    }

    private void setUpForDeleteDashboardPortlet(User user, DashboardPortlet portlet) throws DAOException {
        setUp();
        List<User> expectList = new ArrayList<User>();
        expectList.add(user);
        EasyMock.expect(mockUserDAO.search(user)).andReturn(expectList);
        mockUserDAO.delete(portlet);
        EasyMock.replay(mockUserDAO);
    }

    @Test
    public void testDeleteDashboardPortlet() throws Exception {
        User user = prepareUserData(false);
        Dashboard dashboard = prepareDashboardData(user, false);
        DashboardPortlet portlet = prepareDashboardPortletData(user, false);
        setUpForDeleteDashboardPortlet(user, portlet);
        List<User> list = userService.searchUsers(user);
        assertEquals(1, list.size());
        assertEquals(user, list.get(0));
        assertEquals(1, list.get(0).getDashboards().size());
        assertEquals(1, list.get(0).getDashboard("testDashboardId").getPortlets().size());
        assertEquals(portlet, list.get(0).getDashboard("testDashboardId").getPortlets().iterator().next());
        assertEquals(portlet, list.get(0).getDashboardPortlet("testPortletId"));
        dashboard.removePortlet(portlet);
        userService.deleteDashboardPortlet(portlet);
        assertEquals(1, list.size());
        assertEquals(user, list.get(0));
        assertEquals(0, list.get(0).getDashboard("testDashboardId").getPortlets().size());
        tearDown();
    }

    private void setUpForAddDashboardPortlet(User user, DashboardPortlet portlet) throws DAOException {
        setUp();
        mockUserDAO.insert(portlet);
        List<User> expectList = new ArrayList<User>();
        expectList.add(user);
        EasyMock.expect(mockUserDAO.search(user)).andReturn(expectList);
        EasyMock.replay(mockUserDAO);
    }

    @Test
    public void testAddDashboardPortlet() throws Exception {
        User user = prepareUserData(false);
        Dashboard dashboard = prepareDashboardData(user, false);
        DashboardPortlet portlet = domainObjectFactory.createDashboardPortlet();
        portlet.setUserId(dashboard.getUserId());
        portlet.setDashboardId(dashboard.getDashboardId());
        portlet.setPortletId("testPortletId");
        portlet.setVisible(true);
        portlet.setCreateUserId(dashboard.getUserId());
        portlet.setCreateDate(Calendar.getInstance());
        portlet.setDashboard(dashboard);
        dashboard.addPortlet(portlet);
        setUpForAddDashboardPortlet(user, portlet);
        userService.addDashboardPortlet(portlet);
        List<User> list = userService.searchUsers(user);
        assertEquals(1, list.size());
        assertEquals(user, list.get(0));
        assertEquals(1, list.get(0).getDashboards().size());
        assertEquals(1, list.get(0).getDashboard("testDashboardId").getPortlets().size());
        assertEquals(portlet, list.get(0).getDashboard("testDashboardId").getPortlets().iterator().next());
        assertEquals(portlet, list.get(0).getDashboardPortlet("testPortletId"));
        tearDown();
    }

    private void setUpForUpdateDashboardPortlet(User user, DashboardPortlet portlet) throws DAOException {
        setUp();
        mockUserDAO.update(portlet);
        List<User> expectList = new ArrayList<User>();
        expectList.add(user);
        EasyMock.expect(mockUserDAO.search(user)).andReturn(expectList);
        EasyMock.replay(mockUserDAO);
    }

    @Test
    public void testUpdateDashboardPortlet() throws Exception {
        User user = prepareUserData(false);
        prepareDashboardData(user, false);
        DashboardPortlet portlet = prepareDashboardPortletData(user, false);
        portlet.setPortletTitle("testTitle");
        portlet.setColumnName("thick");
        portlet.setVisible(false);
        setUpForUpdateDashboardPortlet(user, portlet);
        userService.updateDashboardPortlet(portlet);
        List<User> list = userService.searchUsers(user);
        assertEquals(1, list.size());
        assertEquals(user, list.get(0));
        assertEquals(portlet, list.get(0).getDashboardPortlet("testPortletId"));
        assertEquals("testTitle", list.get(0).getDashboardPortlet("testPortletId").getPortletTitle());
        assertEquals("thick", list.get(0).getDashboardPortlet("testPortletId").getColumnName());
        assertEquals(false, list.get(0).getDashboardPortlet("testPortletId").getVisible());
        tearDown();
    }

    private UserEmail prepareUserEmailData(User user, boolean insertFlag) throws Exception {
        UserEmail email = domainObjectFactory.createUserEmail();
        email.setUserId(user.getId());
        email.setEmailAccount(user.getId());
        email.setEmailDomain("test.com");
        email.setMajorFlag(false);
        email.setCreateUserId(user.getId());
        email.setCreateDate(Calendar.getInstance());
        user.addUserEmail(email);
        if (insertFlag) {
            userDAO.insert(email);
        }
        return email;
    }

    private void setUpForDeleteUserEmail(User user, UserEmail email) throws DAOException {
        setUp();
        List<User> expectList = new ArrayList<User>();
        expectList.add(user);
        EasyMock.expect(mockUserDAO.search(user)).andReturn(expectList);
        mockUserDAO.delete(email);
        EasyMock.replay(mockUserDAO);
    }

    @Test
    public void testDeleteUserEmail() throws Exception {
        User user = prepareUserData(false);
        UserEmail email = prepareUserEmailData(user, false);
        setUpForDeleteUserEmail(user, email);
        List<User> list = userService.searchUsers(user);
        assertEquals(1, list.size());
        assertEquals(user, list.get(0));
        assertEquals(1, list.get(0).getUserEmails().size());
        assertEquals(email, list.get(0).getUserEmails().iterator().next());
        assertEquals("test@test.com", list.get(0).getMajorEmail());
        user.removeUserEmail(email);
        userService.deleteUserEmail(email);
        assertEquals(1, list.size());
        assertEquals(user, list.get(0));
        assertEquals(0, list.get(0).getUserEmails().size());
        tearDown();
    }

    private void setUpForAddUserEmail(User user, UserEmail email) throws DAOException {
        setUp();
        mockUserDAO.insert(email);
        List<User> expectList = new ArrayList<User>();
        expectList.add(user);
        EasyMock.expect(mockUserDAO.search(user)).andReturn(expectList);
        EasyMock.replay(mockUserDAO);
    }

    @Test
    public void testAddUserEmail() throws Exception {
        User user = prepareUserData(false);
        UserEmail email = domainObjectFactory.createUserEmail();
        email.setUserId(user.getId());
        email.setEmailAccount(user.getId());
        email.setEmailDomain("test.com");
        email.setMajorFlag(false);
        email.setCreateUserId(user.getId());
        email.setCreateDate(Calendar.getInstance());
        setUpForAddUserEmail(user, email);
        user.addUserEmail(email);
        userService.addUserEmail(email);
        List<User> list = userService.searchUsers(user);
        assertEquals(1, list.size());
        assertEquals(user, list.get(0));
        assertEquals(1, list.get(0).getUserEmails().size());
        assertEquals(email, list.get(0).getUserEmails().iterator().next());
        assertEquals("test@test.com", list.get(0).getMajorEmail());
        tearDown();
    }

    private void setUpForUpdateUserEmail(User user, UserEmail email) throws DAOException {
        setUp();
        mockUserDAO.update(email);
        List<User> expectList = new ArrayList<User>();
        expectList.add(user);
        EasyMock.expect(mockUserDAO.search(user)).andReturn(expectList);
        EasyMock.replay(mockUserDAO);
    }

    @Test
    public void testUpdateUserEmail() throws Exception {
        User user = prepareUserData(false);
        UserEmail email = prepareUserEmailData(user, false);
        email.setMajorFlag(true);
        setUpForUpdateUserEmail(user, email);
        userService.updateUserEmail(email);
        List<User> list = userService.searchUsers(user);
        assertEquals(1, list.size());
        assertEquals(user, list.get(0));
        assertEquals(email, list.get(0).getUserEmails().iterator().next());
        assertEquals(true, list.get(0).getUserEmails().iterator().next().getMajorFlag());
        assertEquals("test@test.com", list.get(0).getMajorEmail());
        tearDown();
    }
}
