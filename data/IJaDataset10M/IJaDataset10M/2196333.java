package jp.co.msr.osaka.umgr.service.impl;

import java.sql.Connection;
import jp.co.msr.osaka.umgr.entity.BloodType;
import jp.co.msr.osaka.umgr.entity.User;
import jp.co.msr.osaka.umgr.exception.UserRegistrationException;
import junit.framework.TestCase;

public class UserServiceImplTest extends TestCase {

    protected void setUp() throws Exception {
        super.setUp();
    }

    /**
	 * コネクションを取得する
	 * 
	 * @return
	 */
    private Connection getConnection() {
        return null;
    }

    public void testCreateUser正常に登録できるケース() {
        MockUserDao dao = new MockUserDao();
        Connection con = null;
        try {
            User user = new User();
            user.setBlood(BloodType.A);
            UserServiceImpl impl = new UserServiceImpl();
            impl.setUserDao(dao);
            impl.createUser(con, user);
        } catch (UserRegistrationException e) {
            fail();
        }
    }

    public void testCreateUserO型の人は登録できないケース() {
        MockUserDao dao = new MockUserDao();
        Connection con = null;
        try {
            User user = new User();
            user.setBlood(BloodType.O);
            UserServiceImpl impl = new UserServiceImpl();
            impl.setUserDao(dao);
            impl.createUser(con, user);
            fail();
        } catch (UserRegistrationException e) {
            assertEquals(e.getMessage(), "O型の人は登録できません");
        }
    }

    public void testDeleteUser() {
        fail("まだ実装されていません");
    }

    public void testFindAllUser() {
        fail("まだ実装されていません");
    }

    public void testFindAllUserWithDeleted() {
        fail("まだ実装されていません");
    }

    public void testFindUserById() {
        fail("まだ実装されていません");
    }

    public void testUpdateUser() {
        fail("まだ実装されていません");
    }

    public void testAddRole() {
    }
}
