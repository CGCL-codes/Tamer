package tudu.domain.dao.hibernate3;

import java.util.HashSet;
import org.springframework.test.AbstractTransactionalSpringContextTests;
import tudu.domain.RolesEnum;
import tudu.domain.dao.UserDAO;
import tudu.domain.model.Role;
import tudu.domain.model.User;

public class UserDAOHibernateTest extends AbstractTransactionalSpringContextTests {

    static {
        HsqldbLauncher.launch();
    }

    private User newUser = null;

    private UserDAO dao = null;

    public void setDao(UserDAO dao) {
        this.dao = dao;
    }

    protected String[] getConfigLocations() {
        String webappDir = System.getProperty("TUDU_WEBAPP_DIR");
        if (webappDir == null) {
            webappDir = "src/webapp";
        }
        return new String[] { "file:" + webappDir + "/WEB-INF/applicationContext.xml", "file:" + webappDir + "/WEB-INF/applicationContext-hibernate.xml" };
    }

    protected void onSetUpBeforeTransaction() throws Exception {
        super.onSetUpBeforeTransaction();
        newUser = new User();
        newUser.setEmail("email@test.com");
        newUser.setEnabled(true);
        newUser.setLastName("lastName");
        newUser.setLogin("testUser");
        newUser.setPassword("password");
        HashSet<Role> roles = new HashSet<Role>();
        Role role = new Role();
        role.setRole(RolesEnum.ROLE_USER.toString());
        roles.add(role);
        newUser.setRoles(roles);
    }

    protected void onSetUpInTransaction() throws Exception {
        super.onSetUpInTransaction();
    }

    public void testSaveUser() {
        HsqldbLauncher.populateDatabase();
        dao.saveUser(newUser);
        User user = dao.getUser("testUser");
        assertEquals(newUser.getLastName(), user.getLastName());
    }

    public void testUpdateUser() {
        HsqldbLauncher.populateDatabase();
        dao.saveUser(newUser);
        newUser.setLastName("newLastName");
        dao.updateUser(newUser);
        User updatedUser = dao.getUser("testUser");
        assertEquals(newUser.getLastName(), updatedUser.getLastName());
    }
}
