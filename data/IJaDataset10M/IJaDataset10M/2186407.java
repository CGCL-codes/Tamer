package org.jbelt.desk.session.test;

import org.testng.annotations.Test;
import org.jboss.seam.mock.SeamTest;

public class SkinTest extends SeamTest {

    @Test
    public void test_skin() throws Exception {
        new FacesRequest() {

            @Override
            protected void invokeApplication() {
                invokeMethod("#{Skin.skin}");
            }
        }.run();
    }
}
