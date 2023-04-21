package ast.common;

import ast.ASTTestSuite;
import ast.common.data.Role;
import ast.common.data.User;
import ast.common.error.ASTError;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test for {@link ast.common.UserController}.
 *
 * @author viri
 */
public class UserControllerTest {

    /**
     * Instance of {@link ast.common.UserController} for testing.
     */
    private UserController userController;

    /**
     * Empty constructor.
     */
    public UserControllerTest() {
    }

    /**
     * {@inheritDoc}
     *
     * @see junit.framework.TestCase#setUp()
     */
    @Before
    public void setUp() {
        this.userController = new UserController(ASTTestSuite.docController);
    }

    /**
     * {@inheritDoc}
     *
     * @see junit.framework.TestCase#tearDown()
     */
    @After
    public void tearDown() {
        this.userController = null;
    }

    /**
     * Test to add and delete a role.
     *
     * @see ast.common.UserController#addRole(boolean, boolean, boolean, boolean, java.lang.String)
     * @see ast.common.UserController#deleteRole(java.lang.String)
     * @see ast.common.UserController#getRoles()
     */
    @Test
    public void testAddDeleteRole() {
        Assert.assertNotNull("List has to be initialized.", this.userController.getRoles());
        Assert.assertTrue("List has to be empty initially.", this.userController.getRoles().isEmpty());
        this.userController.addRole(true, false, false, true, "Autor");
        Assert.assertFalse("List has not to be empty now.", this.userController.getRoles().isEmpty());
        Assert.assertEquals(true, this.userController.getRoles().get(0).isReadComments());
        Assert.assertEquals(false, this.userController.getRoles().get(0).isReadDocument());
        Assert.assertEquals(false, this.userController.getRoles().get(0).isWriteComments());
        Assert.assertEquals(true, this.userController.getRoles().get(0).isWriteDocument());
        Assert.assertEquals("Autor", this.userController.getRoles().get(0).getName());
        this.userController.addRole(false, true, true, false, "Lektor");
        this.userController.deleteRole("Autor");
        Assert.assertFalse("List has not to be empty now.", this.userController.getRoles().isEmpty());
        Assert.assertEquals(false, this.userController.getRoles().get(0).isReadComments());
        Assert.assertEquals(true, this.userController.getRoles().get(0).isReadDocument());
        Assert.assertEquals(true, this.userController.getRoles().get(0).isWriteComments());
        Assert.assertEquals(false, this.userController.getRoles().get(0).isWriteDocument());
        Assert.assertEquals("Lektor", this.userController.getRoles().get(0).getName());
        this.userController.deleteRole("Lektor");
        Assert.assertTrue(this.userController.getRoles().isEmpty());
    }

    /**
     * Test to add and delete a user.
     *
     * @see ast.common.UserController#addUser(java.lang.String, java.lang.String, java.lang.String, java.lang.String, ast.common.data.Role)
     * @see ast.common.UserController#deleteUser(int)
     * @see ast.common.UserController#getUser()
     */
    @Test
    public void testAddDeleteUser() {
        Assert.assertNotNull("List has to be initialized.", this.userController.getUser());
        Assert.assertTrue("List has to be empty initially.", this.userController.getUser().isEmpty());
        this.userController.addUser("Wurst", "Hans", "BOB", "pipapo", new Role(false, true, true, false, "Lektor"));
        Assert.assertFalse("List has not to be empty now.", this.userController.getUser().isEmpty());
        Assert.assertTrue(this.userController.getUser().get(0).getID() == 0);
        Assert.assertEquals("Wurst", this.userController.getUser().get(0).getName());
        Assert.assertEquals("Hans", this.userController.getUser().get(0).getSurname());
        Assert.assertEquals("BOB", this.userController.getUser().get(0).getNick());
        Assert.assertEquals("pipapo", this.userController.getUser().get(0).getPass());
        Assert.assertEquals("Lektor", this.userController.getUser().get(0).getRole().getName());
        Assert.assertEquals(false, this.userController.getUser().get(0).getRole().isReadComments());
        Assert.assertEquals(true, this.userController.getUser().get(0).getRole().isReadDocument());
        Assert.assertEquals(true, this.userController.getUser().get(0).getRole().isWriteComments());
        Assert.assertEquals(false, this.userController.getUser().get(0).getRole().isWriteDocument());
        this.userController.addUser("Lustig", "Peter", "LISA", "puff", new Role(false, true, true, false, "Lektor"));
        this.userController.deleteUser(0);
        Assert.assertFalse("List has not to be empty now.", this.userController.getUser().isEmpty());
        Assert.assertTrue(this.userController.getUser().get(0).getID() == 1);
        Assert.assertEquals("Lustig", this.userController.getUser().get(0).getName());
        Assert.assertEquals("Peter", this.userController.getUser().get(0).getSurname());
        Assert.assertEquals("LISA", this.userController.getUser().get(0).getNick());
        Assert.assertEquals("puff", this.userController.getUser().get(0).getPass());
        Assert.assertEquals("Lektor", this.userController.getUser().get(0).getRole().getName());
        Assert.assertEquals(false, this.userController.getUser().get(0).getRole().isReadComments());
        Assert.assertEquals(true, this.userController.getUser().get(0).getRole().isReadDocument());
        Assert.assertEquals(true, this.userController.getUser().get(0).getRole().isWriteComments());
        Assert.assertEquals(false, this.userController.getUser().get(0).getRole().isWriteDocument());
        this.userController.deleteUser(1);
        Assert.assertTrue("List has to be empty now.", this.userController.getUser().isEmpty());
    }

    /**
     * Test to edit a role.
     *
     * @see ast.common.UserController#addRole(boolean, boolean, boolean, boolean, java.lang.String)
     * @see ast.common.UserController#editRole(java.lang.String, boolean, boolean, boolean, boolean, java.lang.String)
     * @see ast.common.UserController#deleteRole(java.lang.String)
     * @see ast.common.UserController#getRoles()
     */
    @Test
    public void testEditRole() {
        this.userController.addRole(true, false, false, true, "Autor");
        this.userController.editRole("Autor", false, true, true, false, "Lektor");
        Assert.assertEquals(false, this.userController.getRoles().get(0).isReadComments());
        Assert.assertEquals(true, this.userController.getRoles().get(0).isReadDocument());
        Assert.assertEquals(true, this.userController.getRoles().get(0).isWriteComments());
        Assert.assertEquals(false, this.userController.getRoles().get(0).isWriteDocument());
        Assert.assertEquals("Lektor", this.userController.getRoles().get(0).getName());
        this.userController.deleteRole("Lektor");
    }

    /**
     * Test to edit a user.
     *
     * @see ast.common.UserController#addUser(java.lang.String, java.lang.String, java.lang.String, java.lang.String, ast.common.data.Role)
     * @see ast.common.UserController#editUser(int, java.lang.String, java.lang.String, java.lang.String, java.lang.String, ast.common.data.Role)
     * @see ast.common.UserController#deleteUser(int)
     * @see ast.common.UserController#getUser()
     */
    @Test
    public void testEditUser() {
        this.userController.addUser("Wurst", "Hans", "BOB", "pipapo", new Role(false, true, true, false, "Lektor"));
        this.userController.editUser(0, "Lustig", "Peter", "LISA", "puff", new Role(true, false, false, true, "Autor"));
        Assert.assertTrue(this.userController.getUser().get(0).getID() == 0);
        Assert.assertEquals("Lustig", this.userController.getUser().get(0).getName());
        Assert.assertEquals("Peter", this.userController.getUser().get(0).getSurname());
        Assert.assertEquals("LISA", this.userController.getUser().get(0).getNick());
        Assert.assertEquals("puff", this.userController.getUser().get(0).getPass());
        Assert.assertEquals("Autor", this.userController.getUser().get(0).getRole().getName());
        Assert.assertEquals(true, this.userController.getUser().get(0).getRole().isReadComments());
        Assert.assertEquals(false, this.userController.getUser().get(0).getRole().isReadDocument());
        Assert.assertEquals(false, this.userController.getUser().get(0).getRole().isWriteComments());
        Assert.assertEquals(true, this.userController.getUser().get(0).getRole().isWriteDocument());
        this.userController.deleteUser(0);
    }

    /**
     * Test to get a role.
     *
     * @see ast.common.UserController#addRole(boolean, boolean, boolean, boolean, java.lang.String)
     * @see ast.common.UserController#getRole(java.lang.String)
     * @see ast.common.UserController#deleteRole(java.lang.String)
     */
    @Test
    public void testGetRole() {
        this.userController.addRole(true, false, false, true, "Autor");
        Role role = this.userController.getRole("Autor");
        Assert.assertEquals(role.getName(), "Autor");
        Assert.assertEquals(role.isReadComments(), true);
        Assert.assertEquals(role.isReadDocument(), false);
        Assert.assertEquals(role.isWriteComments(), false);
        Assert.assertEquals(role.isWriteDocument(), true);
        this.userController.deleteRole("Autor");
    }

    /**
     * Test to get a user.
     *
     * @see ast.common.UserController#addUser(java.lang.String, java.lang.String, java.lang.String, java.lang.String, ast.common.data.Role)
     * @see ast.common.UserController#getUser(int)
     * @see ast.common.UserController#deleteUser(int)
     * @see ast.common.UserController#getUser()
     */
    @Test
    public void testGetUser() {
        this.userController.addUser("Wurst", "Hans", "BOB", "pipapo", new Role(false, true, true, false, "Lektor"));
        User user = this.userController.getUser(0);
        Assert.assertTrue(this.userController.getUser().get(0).getID() == 0);
        Assert.assertEquals("Wurst", user.getName());
        Assert.assertEquals("Hans", user.getSurname());
        Assert.assertEquals("BOB", user.getNick());
        Assert.assertEquals("pipapo", user.getPass());
        Assert.assertEquals("Lektor", user.getRole().getName());
        Assert.assertEquals(false, user.getRole().isReadComments());
        Assert.assertEquals(true, user.getRole().isReadDocument());
        Assert.assertEquals(true, user.getRole().isWriteComments());
        Assert.assertEquals(false, user.getRole().isWriteDocument());
        this.userController.deleteUser(0);
    }

    /**
     * Test for saving and loading data.
     *
     * @see ast.common.UserController#addRole(boolean, boolean, boolean, boolean, java.lang.String)
     * @see ast.common.UserController#addUser(java.lang.String, java.lang.String, java.lang.String, java.lang.String, ast.common.data.Role)
     * @see ast.common.UserController#saveData()
     * @see ast.common.UserController#resetData()
     * @see ast.common.UserController#loadData()
     * @see ast.common.UserController#deleteUser(int)
     * @see ast.common.UserController#deleteRole(java.lang.String)
     * @throws ASTError
     */
    @Test
    public void testSaveLoadData() throws ASTError {
        System.out.println(this.userController.getRoles().isEmpty());
        Role role = new Role(false, true, true, false, "Lektor");
        this.userController.addRole(role.isReadComments(), role.isReadDocument(), role.isWriteComments(), role.isWriteDocument(), role.getName());
        System.out.println("1." + this.userController.getRole("Lektor").getName());
        this.userController.addUser("Wurst", "Hans", "BOB", "pipapo", role);
        System.out.println("2." + this.userController.getUser(0).getRole().getName());
        this.userController.saveData();
        this.userController.resetData();
        this.userController.loadData();
        System.out.println("3." + this.userController.getRole("Lektor").getName());
        System.out.println("4." + this.userController.getUser(0).getRole().getName());
        Assert.assertTrue(this.userController.getUser().get(0).getID() == 0);
        Assert.assertEquals("Wurst", this.userController.getUser(0).getName());
        Assert.assertEquals("Hans", this.userController.getUser(0).getSurname());
        Assert.assertEquals("BOB", this.userController.getUser(0).getNick());
        Assert.assertEquals("pipapo", this.userController.getUser(0).getPass());
        Assert.assertEquals("Lektor", this.userController.getUser(0).getRole().getName());
        Assert.assertEquals(false, this.userController.getUser(0).getRole().isReadComments());
        Assert.assertEquals(true, this.userController.getUser(0).getRole().isReadDocument());
        Assert.assertEquals(true, this.userController.getUser(0).getRole().isWriteComments());
        Assert.assertEquals(false, this.userController.getUser(0).getRole().isWriteDocument());
        this.userController.deleteUser(0);
        this.userController.deleteRole("Lektor");
    }

    /**
     * Test for resetting data.
     *
     * @see ast.common.UserController#addRole(boolean, boolean, boolean, boolean, java.lang.String)
     * @see ast.common.UserController#addUser(java.lang.String, java.lang.String, java.lang.String, java.lang.String, ast.common.data.Role)
     * @see ast.common.UserController#resetData()
     */
    @Test
    public void testResetData() {
        Role role = new Role(false, true, true, false, "Lektor");
        this.userController.addRole(role.isReadComments(), role.isReadDocument(), role.isWriteComments(), role.isWriteDocument(), role.getName());
        this.userController.addUser("Wurst", "Hans", "BOB", "pipapo", role);
        Assert.assertFalse(this.userController.getRoles().isEmpty());
        Assert.assertFalse(this.userController.getUser().isEmpty());
        this.userController.resetData();
        Assert.assertTrue(this.userController.getRoles().isEmpty());
        Assert.assertTrue(this.userController.getUser().isEmpty());
    }
}
