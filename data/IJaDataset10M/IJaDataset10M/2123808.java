package org.directdemocracyportal.democracy.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import java.util.Date;
import org.directdemocracyportal.democracy.model.application.User;
import org.directdemocracyportal.democracy.test.unit.AbstractUnitTest;
import org.junit.Test;

/**
 * The Class UserTest.
 */
public class UserTest extends AbstractUnitTest {

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        Date createdDate = new Date();
        User user = new User(createdDate, "Username", "Password", null, null);
        assertEquals("Username", user.getUsername());
        assertEquals("Password", user.getPassword());
        assertEquals(createdDate, user.getCreatedDate());
    }

    /**
     * Test set password.
     */
    @Test
    public void testSetPassword() {
        User user = new User();
        user.setPassword("Password");
        assertEquals("Password", user.getPassword());
    }

    /**
     * Test set username.
     */
    @Test
    public void testSetUsername() {
        User user = new User();
        user.setUsername("Username");
        assertEquals("Username", user.getUsername());
    }

    /**
     * Test set created date.
     */
    @Test
    public void testSetCreatedDate() {
        User user = new User();
        Date createdDate = new Date();
        user.setCreatedDate(createdDate);
        assertEquals(createdDate, user.getCreatedDate());
    }

    /**
     * Test equals and hash code.
     */
    @Test
    public void testEqualsAndHashCode() {
        Date createdDate = new Date();
        User userOne = new User(createdDate, "Username", "Password", null, null);
        userOne.setId(1L);
        User userTwo = new User(createdDate, "Username", "Password", null, null);
        userTwo.setId(1L);
        assertEquals(userOne, userTwo);
        assertEquals(userOne.hashCode(), userTwo.hashCode());
    }

    /**
     * Test not equals and hash code.
     */
    @Test
    public void testNotEqualsAndHashCode() {
        Date createdDate = new Date();
        User userOne = new User(createdDate, "Username", "Password", null, null);
        userOne.setId(1L);
        User userTwo = new User(createdDate, "Username", "Password", null, null);
        userTwo.setId(2L);
        assertFalse(userOne.equals(userTwo));
        assertFalse(userOne.hashCode() == userTwo.hashCode());
    }
}
