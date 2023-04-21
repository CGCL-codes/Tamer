package org.itracker.persistence.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.itracker.AbstractDependencyInjectionTest;
import org.itracker.model.PermissionType;
import org.itracker.model.User;
import org.itracker.services.util.UserUtilities;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.dao.DataIntegrityViolationException;

public class UserDAOImplTest extends AbstractDependencyInjectionTest {

    private UserDAO userDAO;

    @Test
    public void testCreateUser() {
        User user = new User("admin_test2", "admin_test2", "admin firstname2", "admin lastname2", "", true);
        user.setCreateDate(new Date());
        user.setLastModifiedDate(new Date());
        userDAO.saveOrUpdate(user);
        User foundUser = userDAO.findByLogin("admin_test2");
        assertNotNull(foundUser);
        assertEquals("admin_test2", foundUser.getPassword());
        assertEquals("admin firstname2", foundUser.getFirstName());
        assertEquals("admin lastname2", foundUser.getLastName());
        assertTrue(foundUser.isSuperUser());
        userDAO.delete(user);
    }

    @Test
    public void testCreateUserWithNotNullPK() {
        try {
            User user = new User("admin_test3", "admin_test3", "admin firstname3", "admin lastname3", "", true);
            user.setId(-1);
            user.setCreateDate(new Date());
            userDAO.saveOrUpdate(user);
        } catch (Exception e) {
            assertTrue(e instanceof DataIntegrityViolationException);
        }
    }

    @Test
    public void testFindUsersForProjectByAllPermissionTypeList() {
        Integer[] permissionTypes = new Integer[] { UserUtilities.PERMISSION_PRODUCT_ADMIN, UserUtilities.PERMISSION_CREATE, UserUtilities.PERMISSION_EDIT };
        List<User> users = userDAO.findUsersForProjectByAllPermissionTypeList(2, permissionTypes);
        assertEquals(2, users.size());
        assertContainsUser(userDAO.findByPrimaryKey(2), users);
        assertContainsUser(userDAO.findByPrimaryKey(3), users);
    }

    @Test
    @Ignore
    public void testFindAll() {
        List<User> users = userDAO.findAll();
        assertNotNull(users);
        assertEquals(6, users.size());
    }

    @Test
    public void testFindActive() {
        List<User> users = userDAO.findActive();
        assertNotNull(users);
        assertEquals(4, users.size());
    }

    @Test
    public void testFindByStatus() {
        List<User> users = userDAO.findByStatus(1);
        assertNotNull(users);
        assertEquals(4, users.size());
    }

    @Test
    public void testFindSuperUsers() {
        List<User> users = userDAO.findSuperUsers();
        assertNotNull(users);
        assertEquals(1, users.size());
    }

    @Test
    public void testFindByRegistrationType() {
        List<User> users = userDAO.findByRegistrationType(1);
        assertNotNull(users);
        assertEquals(5, users.size());
    }

    @Test
    public void testGetUsersMapOfProjectsAndPermissionTypes() {
        Map<Integer, Set<PermissionType>> map = userDAO.getUsersMapOfProjectsAndPermissionTypes(userDAO.findByPrimaryKey(2));
        assertNotNull(map);
        assertEquals("projects", 1, map.size());
    }

    private void assertContainsUser(User user, List<User> users) {
        if (!users.contains(user)) {
            fail("User not found in the list.");
        }
    }

    @Override
    public void onSetUp() throws Exception {
        super.onSetUp();
        userDAO = (UserDAO) applicationContext.getBean("userDAO");
    }

    protected String[] getDataSetFiles() {
        return new String[] { "dataset/userpreferencesbean_dataset.xml", "dataset/userbean_dataset.xml", "dataset/projectbean_dataset.xml", "dataset/permissionbean_dataset.xml" };
    }

    protected String[] getConfigLocations() {
        return new String[] { "application-context.xml" };
    }
}
