package com.pn.runnable;

import com.pn.services.LoginServices;
import com.pn.test.BaseTest;

public class TestUserManagement extends BaseTest {

    private static String LoginServices = "com/pn/services/LoginServices";

    public TestUserManagement() {
        super();
    }

    public void testAddUser() {
        LoginServices loginServices = (LoginServices) context.getBean(LoginServices);
        try {
            loginServices.addUser("user1", "user1", true);
        } catch (Exception e) {
            assertFalse(true);
        }
        assertTrue(true);
    }

    public void testAddRole() {
        LoginServices loginServices = (LoginServices) context.getBean(LoginServices);
        try {
            loginServices.addRole("role1", "role1", "role1");
        } catch (Exception e) {
            assertFalse(true);
        }
        assertTrue(true);
    }

    public void testAddUserByRole() {
        LoginServices loginServices = (LoginServices) context.getBean(LoginServices);
        try {
            loginServices.addUserByRole("user1", "role1", "user1_role1");
        } catch (Exception e) {
            assertFalse(true);
        }
        assertTrue(true);
    }

    public void testDeleteUser() {
        LoginServices loginServices = (LoginServices) context.getBean(LoginServices);
        try {
            loginServices.deleteUser("user1");
        } catch (Exception e) {
            assertFalse(true);
        }
        assertTrue(true);
    }

    public void testDeleteRole() {
        LoginServices loginServices = (LoginServices) context.getBean(LoginServices);
        try {
            loginServices.deleteRole("role1");
        } catch (Exception e) {
            assertFalse(true);
        }
        assertTrue(true);
    }
}
