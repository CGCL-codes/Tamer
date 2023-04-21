package net.taylor.testing;

import org.jboss.embedded.Bootstrap;
import org.jboss.seam.mock.SeamTest;
import org.testng.annotations.Test;

public class TestNgEmbeddedTest extends SeamTest {

    @Test
    public void testBooted() throws Exception {
        assert Bootstrap.getInstance().isStarted();
        System.out.println("TestNG STARTED...");
    }

    @Test
    public void testLoggedIn() throws Exception {
        new ComponentTest() {

            protected void testComponents() throws Exception {
                assert getValue("#{identity.loggedIn}").equals(false);
            }
        }.run();
    }

    @Test
    public void testInvoke() throws Exception {
        new ComponentTest() {

            protected void testComponents() throws Exception {
                Object echo = invokeMethod("#{sampleService.echo('Hellooo')}");
                System.out.println("TestNG " + echo);
                assert echo.equals("Echoing: Hellooo");
            }
        }.run();
    }
}
