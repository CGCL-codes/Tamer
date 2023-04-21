package es.us.isw2.persistance;

import static org.junit.Assert.assertEquals;
import java.util.HashSet;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import es.us.isw2.server.domain.AlumnDAO;
import es.us.isw2.server.domain.WorkDAO;
import es.us.isw2.server.persistence.dao.impl.AlumnDAOJpaController;
import es.us.isw2.shared.exceptions.IllegalOrphanException;
import es.us.isw2.shared.exceptions.NonexistentEntityException;
import es.us.isw2.shared.exceptions.PreexistingEntityException;

/**
 * @author Miguel These tests must be launched with the hibernate.hbm2ddl.auto
 *         param = create
 */
public class UserTest {

    private AlumnDAO user;

    private AlumnDAOJpaController aController;

    @Before
    public void setUp() {
        aController = new AlumnDAOJpaController();
        user = new AlumnDAO("test@us.es", "TestUser1", "superPassWord", "TestSurname", "Spain", true, new HashSet<WorkDAO>());
    }

    @Test
    public void testInsertUser() throws Exception {
        aController.create(user);
    }

    @Test
    public void getUserById() throws Exception {
        AlumnDAO a = aController.findAlumnDAO(user.getEmail());
        assertEquals(a.getEmail(), "test@us.es");
    }

    @Test
    public void updateUser() throws NonexistentEntityException, Exception {
        user.setName("ModifiedName");
        aController.edit(user);
        AlumnDAO modifiedUser = aController.findAlumnDAO(user.getEmail());
        assertEquals("ModifiedName", modifiedUser.getName());
    }

    @Test
    public void testDeleteUser() throws IllegalOrphanException, PreexistingEntityException, Exception {
        aController.destroy("myguelruiz@gmail.com");
        aController.destroy(user.getEmail());
    }

    @Test
    public void countAllAlumns() throws IllegalOrphanException, PreexistingEntityException, Exception {
        List<AlumnDAO> allAlumns = aController.findAlumnDAOEntities();
        int totalAlumns = aController.getAlumnDAOCount();
        assertEquals(totalAlumns, allAlumns.size());
    }

    @Test(expected = NonexistentEntityException.class)
    public void testNegativeDeleteUser() throws IllegalOrphanException, PreexistingEntityException, Exception {
        aController.destroy("NonExistingEmail@false.com");
    }

    @Test(expected = PreexistingEntityException.class)
    public void insertDuplicatedUser() throws PreexistingEntityException, Exception {
        aController.create(user);
        aController.create(user);
        aController.destroy(user.getEmail());
    }
}
