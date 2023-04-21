package fr.umlv.jee.hibou.bean;

import java.util.Date;
import fr.umlv.jee.hibou.bdd.table.User;
import junit.framework.TestCase;

/**
 * JUnit Test Class for {@link fr.umlv.jee.hibou.bean.UserBean}.
 * @author micka, alex, nak, matt
 */
public class UserBeanTest extends TestCase {

    private UserBean ub;

    private Date date;

    protected void setUp() throws Exception {
        super.setUp();
        date = new Date();
        User user = new User("test@test.com", "test", "test", "M", "France", false, false, date);
        ub = new UserBean(user);
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
	 * Test method for {@link fr.umlv.jee.hibou.bean.UserBean#getCountry()}.
	 */
    public void testGetCountry() {
        assertEquals(ub.getCountry(), "France");
    }

    /**
	 * Test method for {@link fr.umlv.jee.hibou.bean.UserBean#setCountry(java.lang.String)}.
	 */
    public void testSetCountry() {
        ub.setCountry("usa");
        assertEquals(ub.getCountry(), "usa");
    }

    /**
	 * Test method for {@link fr.umlv.jee.hibou.bean.UserBean#getEmail()}.
	 */
    public void testGetEmail() {
        assertEquals(ub.getEmail(), "test@test.com");
    }

    /**
	 * Test method for {@link fr.umlv.jee.hibou.bean.UserBean#setEmail(java.lang.String)}.
	 */
    public void testSetEmail() {
        ub.setEmail("test2@test.com");
        assertEquals(ub.getEmail(), "test2@test.com");
    }

    /**
	 * Test method for {@link fr.umlv.jee.hibou.bean.UserBean#getNickname()}.
	 */
    public void testGetNickname() {
        assertEquals(ub.getNickname(), "test");
    }

    /**
	 * Test method for {@link fr.umlv.jee.hibou.bean.UserBean#setNickname(java.lang.String)}.
	 */
    public void testSetNickname() {
        ub.setNickname("test2");
        assertEquals(ub.getNickname(), "test2");
    }

    /**
	 * Test method for {@link fr.umlv.jee.hibou.bean.UserBean#getSex()}.
	 */
    public void testGetSex() {
        assertEquals(ub.getSex(), "M");
    }

    /**
	 * Test method for {@link fr.umlv.jee.hibou.bean.UserBean#setSex(java.lang.String)}.
	 */
    public void testSetSex() {
        ub.setSex("F");
        assertEquals(ub.getSex(), "F");
    }
}
