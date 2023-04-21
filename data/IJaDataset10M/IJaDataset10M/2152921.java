package cz.zcu.kiv.jet.jpa.service;

import java.util.ArrayList;
import java.util.List;
import org.jfree.util.Log;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import cz.zcu.kiv.jet.jpa.dao.AddressDao;
import cz.zcu.kiv.jet.jpa.dao.BlipDao;
import cz.zcu.kiv.jet.jpa.dao.GroupDao;
import cz.zcu.kiv.jet.jpa.dao.MembershipDao;
import cz.zcu.kiv.jet.jpa.dao.MessageDao;
import cz.zcu.kiv.jet.jpa.dao.UserDao;
import cz.zcu.kiv.jet.jpa.data.generator.TextGenerator;
import cz.zcu.kiv.jet.jpa.data.generator.impl.TextGeneratorImpl;
import cz.zcu.kiv.jet.jpa.domain.Group;
import cz.zcu.kiv.jet.jpa.domain.Membership;
import cz.zcu.kiv.jet.jpa.domain.User;
import cz.zcu.kiv.jet.jpa.exception.GroupValidationException;
import cz.zcu.kiv.jet.jpa.exception.ObjectNotPersistException;
import cz.zcu.kiv.jet.jpa.exception.UserAlreadyExistsException;
import cz.zcu.kiv.jet.jpa.exception.UserValidationException;
import cz.zcu.kiv.jet.jpa.service.UserService;

/**
 * Simple unit test with Spring context which tests the methods in class
 * UserService.
 */
@ContextConfiguration({ "/appctx-main.xml", "/appctx-orm.xml", "/appctx-generator.xml" })
public class UserServiceTest extends AbstractJUnit4SpringContextTests {

    /**
	 * User service instance.
	 */
    @Autowired
    UserService userService;

    /**
	 * Message service instance.
	 */
    @Autowired
    MessageService messageService;

    /**
	 * BlipDao instance.
	 */
    @Autowired
    BlipDao blipDao;

    /**
	 * MassageDao instance.
	 */
    @Autowired
    MessageDao messageDao;

    /**
	 * UserDao instance.
	 */
    @Autowired
    UserDao userDao;

    /**
	 * GroupDao instance.
	 */
    @Autowired
    GroupDao groupDao;

    /**
	 * MembershipDao instance.
	 */
    @Autowired
    MembershipDao membershipDao;

    /**
	 * AddressDao instance.
	 */
    @Autowired
    AddressDao addressDao;

    @Autowired
    TextGenerator textGenerator;

    /**
	 * Tests if userService.storeUser() with null argument throws
	 * IllegalArgumentException.
	 */
    @Test(expected = IllegalArgumentException.class)
    public void storeUserIllegalArgument() {
        userService.storeUser(null);
    }

    /**
	 * Tests if userService.StoreUser() with empty User argument throws
	 * UserValidationException.
	 */
    @Test(expected = UserValidationException.class)
    public void storeEmptyUser() {
        userService.storeUser(new User());
    }

    @Test(expected = UserValidationException.class)
    public void storeUncompleteUserNoName() {
        User usr = new User();
        usr.setSurname("Prijimeni");
        usr.setUsername("Username");
        usr.setPassword("heslo");
        usr.setEmail("address@email.com");
        userService.storeUser(usr);
    }

    @Test(expected = UserValidationException.class)
    public void storeUncompleteUserNoSurname() {
        User usr = new User();
        usr.setName("Jmeno");
        usr.setUsername("Username");
        usr.setPassword("heslo");
        usr.setEmail("address@email.com");
        userService.storeUser(usr);
    }

    @Test(expected = UserValidationException.class)
    public void storeUncompleteUserNoUsername() {
        User usr = new User();
        usr.setName("Jmeno");
        usr.setSurname("Prijimeni");
        usr.setPassword("heslo");
        usr.setEmail("address@email.com");
        userService.storeUser(usr);
    }

    @Test(expected = UserValidationException.class)
    public void storeUncompleteUserNoPassword() {
        User usr = new User();
        usr.setName("Jmeno");
        usr.setSurname("Prijimeni");
        usr.setUsername("Username");
        usr.setEmail("address@email.com");
        userService.storeUser(usr);
    }

    @Test(expected = UserValidationException.class)
    public void storeUncompleteUserNoEmail() {
        User usr = new User();
        usr.setName("Jmeno");
        usr.setSurname("Prijimeni");
        usr.setUsername("Username");
        usr.setPassword("heslo");
        userService.storeUser(usr);
    }

    @Test(expected = UserValidationException.class)
    public void storeTooLongUserName() {
        User usr = new User();
        usr.setName(textGenerator.getText(255));
        usr.setSurname("Prijimeni");
        usr.setUsername("Username");
        usr.setPassword("heslo");
        usr.setEmail("adress@email.com");
        userService.storeUser(usr);
    }

    @Test(expected = UserValidationException.class)
    public void storeTooLongUserSurname() {
        User usr = new User();
        usr.setName("Jmeno");
        usr.setSurname(textGenerator.getText(255));
        usr.setUsername("Username");
        usr.setPassword("heslo");
        usr.setEmail("adress@email.com");
        userService.storeUser(usr);
    }

    @Test(expected = UserValidationException.class)
    public void storeTooLongUserUsername() {
        User usr = new User();
        usr.setName("Jmeno");
        usr.setSurname("Prijimeni");
        usr.setUsername(textGenerator.getText(255));
        usr.setPassword("heslo");
        usr.setEmail("adress@email.com");
        userService.storeUser(usr);
    }

    @Test(expected = UserValidationException.class)
    public void storeTooLongUserPassword() {
        User usr = new User();
        usr.setName("Jmeno");
        usr.setSurname("Prijimeni");
        usr.setUsername("Username");
        usr.setPassword(textGenerator.getText(255));
        usr.setEmail("adress@email.com");
        userService.storeUser(usr);
    }

    @Test(expected = UserValidationException.class)
    public void storeTooLongUserEmail() {
        User usr = new User();
        usr.setName("Jmeno");
        usr.setSurname("Prijimeni");
        usr.setUsername("Username");
        usr.setPassword("heslo");
        usr.setEmail(textGenerator.getText(255).replaceAll(" ", "") + "@email.com");
        userService.storeUser(usr);
    }

    /**
	 * Tests if userService.storeUser() stores a legal User.
	 * 
	 * Creates a legal instance of User and stores it into database. This method
	 * should return not null instance. So we assert a returning instance to not
	 * to be null.
	 */
    @Test
    public void storeUser() {
        User usr = new User();
        usr.setName("Jmeno");
        usr.setSurname("Prijimeni");
        usr.setUsername("Username");
        usr.setPassword("heslo");
        usr.setEmail("address@email.com");
        Assert.assertNotNull(userService.storeUser(usr));
    }

    /**
	 * Tests if it is impossible to store the user with the same username.
	 * 
	 * Creates a legal instance of User. Than it creates another one instance
	 * with the same username and tries to store it as well.
	 */
    @Test(expected = UserAlreadyExistsException.class)
    public void storeExistingUser() {
        User usr = new User();
        usr.setName("Jmeno");
        usr.setSurname("Prijimeni");
        usr.setUsername("Username");
        usr.setPassword("heslo");
        usr.setEmail("address@email.com");
        userService.storeUser(usr);
        User usr1 = new User();
        usr1.setName("Jmeno");
        usr1.setSurname("Prijimeni");
        usr1.setUsername("Username");
        usr1.setPassword("heslo");
        usr1.setEmail("address@email.com");
        userService.storeUser(usr1);
    }

    /**
	 * Tries to create and store a user with the impossible email address and
	 * tests the exceptions the method storeUser() throws. It has a list of
	 * impossible email address to test with.
	 */
    @Test
    public void userBadEmailAddress() {
        String[] badEmails = { "addressEmail.com", "addresEmailcom", "address@emailcom", null };
        for (String string : badEmails) {
            Assert.assertFalse(emailTest(string));
        }
    }

    @Test
    public void getAllUsersNoUsers() {
        Assert.assertEquals(0, userService.getAllUsers().size());
    }

    @Test
    public void getAllUsers() {
        createUser("Petr", "Novak", "peta", "heslo", "asdf@asdf.com");
        Assert.assertEquals(1, userService.getAllUsers().size());
        createUser("Petr", "Novak", "peta1", "heslo", "asdf@asdf.com");
        Assert.assertEquals(2, userService.getAllUsers().size());
        createUser("Petr", "Novak", "peta2", "heslo", "asdf@asdf.com");
        Assert.assertEquals(3, userService.getAllUsers().size());
        User u = createUser("Petr", "Novak", "peta3", "heslo", "asdf@asdf.com");
        Assert.assertEquals(4, userService.getAllUsers().size());
        ArrayList<User> users = new ArrayList<User>();
        users.add(u);
        userService.deleteUsers(users);
        Assert.assertEquals(3, userService.getAllUsers().size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void findUsersByNameIllegalArgument() {
        userService.findUsersByName(null);
    }

    @Test
    public void findUsersByNameNotExistingUser() {
        userService.findUsersByName("Karel");
    }

    @Test
    public void findUsersByNameExistingUser() {
        createUser("Karel", "Novak", "kaja", "pass", "asdf@asdf.asd");
        Assert.assertEquals(1, userService.findUsersByName("Karel").size());
        createUser("Karel", "Novotny", "karlos", "password", "asdssf@asdf.asd");
        Assert.assertEquals(2, userService.findUsersByName("Karel").size());
        createUser("Karel", "Novotny", "karlos3", "password", "asdssf@asdf.asd");
        Assert.assertEquals(3, userService.findUsersByName("Karel").size());
        createUser("Karel", "Novotny", "karlos1", "password", "asdssf@asdf.asd");
        Assert.assertEquals(4, userService.findUsersByName("Karel").size());
        createUser("Karel", "Novotny", "karlos2", "password", "asdssf@asdf.asd");
        Assert.assertEquals(5, userService.findUsersByName("Karel").size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void deleteUsersIllegalArgument() {
        userService.deleteUsers(null);
    }

    @Test
    public void deleteUsersEmptyList() {
        createUser("Karel", "Novak", "kaja", "pass", "asdf@asdf.asd");
        createUser("Karel", "Novotny", "karlos", "password", "asdssf@asdf.asd");
        createUser("Karel", "Novotny", "karlos3", "password", "asdssf@asdf.asd");
        createUser("Karel", "Novotny", "karlos1", "password", "asdssf@asdf.asd");
        List<User> users = new ArrayList<User>();
        userService.deleteUsers(users);
        Assert.assertEquals(4, userService.getAllUsers().size());
    }

    @Test
    public void deleteUsersNotInDatabase() {
        List<User> users = new ArrayList<User>();
        users.add(createUser("Karel", "Novak", "kaja", "pass", "asdf@asdf.asd"));
        users.add(createUser("Karel", "Novotny", "karlos", "password", "asdssf@asdf.asd"));
        users.add(createUser("Karel", "Novotny", "karlos3", "password", "asdssf@asdf.asd"));
        users.add(createUser("Karel", "Novotny", "karlos1", "password", "asdssf@asdf.asd"));
        users.add(new User());
        userService.deleteUsers(users);
        Assert.assertEquals(0, userService.getAllUsers().size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void getUserProfileIllegalArgument() {
        userService.getUserProfile(null);
    }

    @Test(expected = ObjectNotPersistException.class)
    public void getUserProfileNotPersistArgument() {
        userService.getUserProfile(new User());
    }

    @Test(expected = IllegalArgumentException.class)
    public void storeGroupIllegalArgument() {
        userService.storeGroup(null);
    }

    @Test(expected = GroupValidationException.class)
    public void storeGroupEmptyGroup() {
        userService.storeGroup(new Group());
    }

    @Test
    public void storeGroup() {
        Group g = new Group();
        g.setName("jednicka");
        Assert.assertNotNull(userService.storeGroup(g));
    }

    @Test(expected = GroupValidationException.class)
    public void storeGroupUncompleteGroupNoName() {
        Group g = new Group();
        userService.storeGroup(g);
    }

    @Test(expected = IllegalArgumentException.class)
    public void deleteGroupsIllegalArgument() {
        userService.deleteGroups(null);
    }

    @Test
    public void deleteGroupsEmptyList() {
        createGroup("jednicka");
        createGroup("dvojka");
        createGroup("trojka");
        createGroup("ctyrka");
        createGroup("petka");
        createGroup("sestka");
        List<Group> groups = new ArrayList<Group>();
        userService.deleteGroups(groups);
        Assert.assertEquals(6, userService.getAllGroups().size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void addUserToGroupIllegalArgument1() {
        User u = createUser("Karel", "pepa", "pepca", "heslo", "asdf@asdf.com");
        userService.addUserToGroup(u, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addUserToGroupIllegalArgument2() {
        Group g = createGroup("Ahoj");
        userService.addUserToGroup(null, g);
    }

    @Test
    public void deleteGroupsNotInDatabase() {
        List<Group> groups = new ArrayList<Group>();
        groups.add(createGroup("jednicka"));
        groups.add(createGroup("dvojka"));
        groups.add(createGroup("trojka"));
        groups.add(createGroup("ctyrka"));
        groups.add(createGroup("petka"));
        groups.add(createGroup("sestka"));
        groups.add(new Group());
        userService.deleteGroups(groups);
        Assert.assertEquals(0, userService.getAllGroups().size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void deleteMembershipsIllegalArgument() {
        userService.deleteMemberships(null);
    }

    @Test
    public void deleteMembershipsEmptyList() {
        List<Membership> memberships = new ArrayList<Membership>();
        List<User> users = new ArrayList<User>();
        List<Group> groups = new ArrayList<Group>();
        users.add(createUser("Karel", "Novak", "kaja", "heslo", "asdf@asdf.asd"));
        users.add(createUser("Karel", "Novak", "kaja2", "heslo", "asdf@asdf.asd"));
        users.add(createUser("Karel", "Novak", "kaja3", "heslo", "asdf@asdf.asd"));
        groups.add(createGroup("ahoj"));
        groups.add(createGroup("nazdar"));
        groups.add(createGroup("cau"));
        userService.addUserToGroup(users.get(0), groups.get(0));
        userService.addUserToGroup(users.get(0), groups.get(1));
        userService.addUserToGroup(users.get(1), groups.get(0));
        userService.addUserToGroup(users.get(1), groups.get(1));
        userService.deleteMemberships(memberships);
        Assert.assertEquals(4, userService.getAllMemberships().size());
    }

    @Test
    public void deleteMemberships() {
        membershipDao.deleteAll(membershipDao.getAll());
        List<Membership> memberships = new ArrayList<Membership>();
        List<User> users = new ArrayList<User>();
        List<Group> groups = new ArrayList<Group>();
        users.add(createUser("Karel", "Novak", "kaja", "heslo", "asdf@asdf.asd"));
        users.add(createUser("Karel", "Novak", "kaja2", "heslo", "asdf@asdf.asd"));
        users.add(createUser("Karel", "Novak", "kaja3", "heslo", "asdf@asdf.asd"));
        groups.add(createGroup("ahoj"));
        groups.add(createGroup("nazdar"));
        groups.add(createGroup("cau"));
        Assert.assertEquals(0, userService.getAllMemberships().size());
        memberships.add(userService.addUserToGroup(users.get(0), groups.get(0)));
        Assert.assertEquals(1, userService.getAllMemberships().size());
        userService.addUserToGroup(users.get(0), groups.get(1));
        Assert.assertEquals(2, userService.getAllMemberships().size());
        memberships.add(userService.addUserToGroup(users.get(1), groups.get(0)));
        Assert.assertEquals(3, userService.getAllMemberships().size());
        userService.addUserToGroup(users.get(1), groups.get(1));
        Assert.assertEquals(4, userService.getAllMemberships().size());
        userService.deleteMemberships(memberships);
        Assert.assertEquals(2, userService.getAllMemberships().size());
    }

    private Group createGroup(String name) {
        Group g = new Group();
        g.setName(name);
        return userService.storeGroup(g);
    }

    private User createUser(String name, String surname, String uname, String pass, String emailAddr) {
        User u = new User();
        u.setName(name);
        u.setSurname(surname);
        u.setUsername(uname);
        u.setPassword(pass);
        u.setEmail(emailAddr);
        return userService.storeUser(u);
    }

    /**
	 * Tests to create and store new user with given email address.
	 * 
	 * @return True in case of success, false otherwise.
	 */
    private boolean emailTest(String email) {
        try {
            User usr = new User();
            usr.setName("Jmeno");
            usr.setSurname("Prijimeni");
            usr.setUsername("Username");
            usr.setPassword("heslo");
            usr.setEmail(email);
            userService.storeUser(usr);
        } catch (UserValidationException exc) {
            Log.error("Email bad");
            return false;
        }
        return true;
    }

    /**
	 * Cleans the database.
	 */
    @After
    public void after() {
        blipDao.deleteAll(blipDao.getAll());
        messageDao.deleteAll(messageDao.getAll());
        groupDao.deleteAll(groupDao.getAll());
        addressDao.deleteAll(addressDao.getAll());
        userDao.deleteAll(userDao.getAll());
        membershipDao.deleteAll(membershipDao.getAll());
    }
}
