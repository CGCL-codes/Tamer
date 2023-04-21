package org.openjf.usergroup;

import java.sql.Timestamp;
import java.util.List;
import org.openjf.exception.*;
import org.openjf.persistence.Persistence;
import org.openjf.test.util.*;
import org.openjf.util.TestUtil;
import junit.framework.TestCase;
import org.hibernate.HibernateException;

public class UserTest extends TestCase {

    public UserTest() {
    }

    protected void setUp() throws Exception {
        super.setUp();
        TestData.clearAllData();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Testa os metodos insert e update, que devem lancar excecao (caso
     * contrario o teste falha).
     *
     * @param userId User
     * @param nick String
     * @throws HibernateException
     * @throws BoardException
     */
    private void assertExceptionInsertUpdate(int userId, String globalId, String nick, String email, Class expectedExceptionClass) throws HibernateException, BoardException {
        Persistence.openSessionAndTransaction();
        try {
            UserControl.updateUser(userId, globalId, nick, email);
            fail();
        } catch (Exception ex) {
            TestUtil.assertExceptionClass(expectedExceptionClass, ex);
        }
        Persistence.commitSessionAndTransaction();
        Persistence.openSessionAndTransaction();
        try {
            UserControl.insertUser(globalId, nick, email);
            fail();
        } catch (Exception ex) {
            TestUtil.assertExceptionClass(expectedExceptionClass, ex);
        }
        Persistence.commitSessionAndTransaction();
    }

    /**
     * Testa diversos metodos de UserControl
     *
     * @throws HibernateException
     * @throws BoardException
     */
    public void testInsertUpdateDelete() throws HibernateException, BoardException {
        User user;
        User array[];
        Persistence.openSessionAndTransaction();
        User userAdmin = null;
        User userAnonymous = UserControl.findUserAnonymous();
        Persistence.commitSessionAndTransaction();
        Persistence.openSessionAndTransaction();
        List users = UserControl.findAllUsersSortedById();
        array = (User[]) users.toArray(new User[users.size()]);
        ArrayTestUtil.assertArrayEquailsRecursive(new User[] { userAdmin, userAnonymous }, array);
        Persistence.commitSessionAndTransaction();
        Persistence.openSessionAndTransaction();
        User user1 = UserControl.insertUser("12233", "    N   AA  \t", "ss");
        user1 = UserControl.findUser(user1.getId());
        assertEquals("N AA", user1.getNick());
        Persistence.commitSessionAndTransaction();
        Persistence.openSessionAndTransaction();
        User user2 = UserControl.insertUser("545454", "N2", "ss");
        Persistence.commitSessionAndTransaction();
        Persistence.openSessionAndTransaction();
        User user3 = UserControl.insertUser("2123232", "N3", "ss");
        Persistence.commitSessionAndTransaction();
        assertExceptionInsertUpdate(user3.getId(), "875667", "N9", null, BoardValidationException.class);
        assertExceptionInsertUpdate(user3.getId(), "32323232", "N9", "", BoardValidationException.class);
        assertExceptionInsertUpdate(user3.getId(), "875667", "ss", null, BoardValidationException.class);
        assertExceptionInsertUpdate(user3.getId(), "32323232", "", "ss", BoardValidationException.class);
        assertExceptionInsertUpdate(user3.getId(), "1232433", " N   AA", "ss", BoardValidationException.class);
        int user3Id = user3.getId();
        Persistence.openSessionAndTransaction();
        UserControl.deleteUser(user3Id);
        Persistence.commitSessionAndTransaction();
        Persistence.openSessionAndTransaction();
        try {
            UserControl.deleteUser(user3Id);
            fail();
        } catch (Exception ex) {
            TestUtil.assertExceptionClass(BoardObjectNotFoundException.class, ex);
        }
        Persistence.commitSessionAndTransaction();
        Persistence.openSessionAndTransaction();
        Timestamp ttt = new Timestamp(123000000L);
        UserControl.updateUserViewAllPostsTime(user1.getId(), ttt);
        Persistence.commitSessionAndTransaction();
        user1.setViewAllPostsTime(ttt);
        user = UserControl.findUser(user1.getId());
        assertEquals(user, user1);
        Persistence.openSessionAndTransaction();
        users = UserControl.findAllUsersSortedById();
        ArrayTestUtil.assertArrayEquailsRecursive(new Object[] { userAdmin, userAnonymous, user1, user2 }, users.toArray());
        Persistence.commitSessionAndTransaction();
        Persistence.openSessionAndTransaction();
        assertEquals("", UserControl.getUserEncryptedPassword(user1.getId()));
        assertTrue(UserControl.verifyUserPassword(user1.getId(), ""));
        Persistence.commitSessionAndTransaction();
        Persistence.openSessionAndTransaction();
        UserControl.updateUserPassword(user1.getId(), "teste");
        assertFalse(UserControl.verifyUserPassword(user1.getId(), ""));
        assertFalse(UserControl.verifyUserPassword(user1.getId(), "test"));
        assertTrue(UserControl.verifyUserPassword(user1.getId(), "teste"));
        Persistence.commitSessionAndTransaction();
        Persistence.openSessionAndTransaction();
        UserControl.updateUserPassword(user1.getId(), "");
        assertFalse(UserControl.verifyUserPassword(user1.getId(), "teste"));
        assertFalse(UserControl.verifyUserPassword(user1.getId(), "test"));
        assertTrue(UserControl.verifyUserPassword(user1.getId(), ""));
        Persistence.commitSessionAndTransaction();
    }
}
