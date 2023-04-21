package net.paoding.rose.testcases;

import java.io.IOException;
import javax.servlet.ServletException;

public class OncePerRequestTest extends AbstractControllerTest {

    public void test1() throws ServletException, IOException {
        assertEquals("ok", invoke("/oncePerRequest"));
    }

    public void test2() throws ServletException, IOException {
        assertEquals("ok", invoke("/oncePerRequest2"));
    }
}
