package de.fau.cs.dosis.util.email;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

public class EmailHandlerSmtpTest {

    EmailHandlerSmtp emailManager;

    @Before
    public void setUp() {
        emailManager = new EmailHandlerSmtp("", "", "", "", "testEmail/", "true");
    }

    @Test
    public void testGenerateEmailNotExistingTemplate() {
        try {
            emailManager.generateEmail("notexist", null);
            Assert.fail("not existing email template, should throw exception");
        } catch (IOException e) {
        }
    }

    @Test
    public void testGenerateEmailFriendly() throws IOException {
        Map<String, String> values = new HashMap<String, String>();
        values.put("userName", "name");
        values.put("activationLink", "link");
        values.put("activationToken", "token");
        String email = emailManager.generateEmail("test", values);
        Assert.assertEquals("namelinktoken\n", email);
    }
}
