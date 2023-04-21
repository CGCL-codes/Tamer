package org.jazzteam;

import static org.junit.Assert.*;
import java.io.IOException;
import org.jazzteam.exteptionmodel.LoginAndPassword;
import org.junit.Test;

public class LoginAndPasswordTest {

    @Test
    public void test() throws IOException {
        LoginAndPassword object = new LoginAndPassword("3456h#@");
        assertEquals(object.password(), "�������� ������");
    }

    @Test
    public void test2() throws IOException {
        LoginAndPassword object = new LoginAndPassword("3456");
        assertEquals(object.password(), "������ ������");
    }

    @Test
    public void test3() throws IOException {
        LoginAndPassword object = new LoginAndPassword("3456");
        assertTrue(object.password() == "������ ������");
    }

    @Test
    public void test4() throws IOException {
        LoginAndPassword object = new LoginAndPassword("3456@;�");
        assertFalse(object.password() == "������ ������");
    }

    @Test
    public void test5() throws IOException {
        LoginAndPassword object = new LoginAndPassword("3456@;�");
        assertTrue(object.password() == "�������� ������");
    }

    @Test
    public void test6() throws IOException {
        LoginAndPassword object = new LoginAndPassword("3456");
        assertFalse(object.password() == "�������� ������");
    }

    @Test
    public void test7() throws IOException {
        LoginAndPassword object = new LoginAndPassword("asd&?");
        assertTrue(object.login() == "�������� �����");
    }
}
