package org.appfuse.tutorial.webapp.pages;

import org.junit.Test;
import org.subethamail.wiser.Wiser;
import static org.junit.Assert.assertTrue;

public class PasswordHintTest extends BasePageTestCase {

    @Test
    public void testActivate() throws Exception {
        Wiser wiser = new Wiser();
        wiser.setPort(getSmtpPort());
        wiser.start();
        doc = tester.renderPage("passwordHint/admin");
        wiser.stop();
        assertTrue(wiser.getMessages().size() == 1);
        assertTrue(doc.getElementById("successMessages").toString().contains("The password hint for admin has been sent to"));
    }
}
