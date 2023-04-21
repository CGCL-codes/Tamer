package org.pittjug.email.automated.test;

import java.util.Properties;
import java.io.IOException;
import java.util.Hashtable;
import org.pittjug.email.automated.AutomatedEmail;
import javax.mail.MessagingException;
import junit.framework.TestSuite;
import junit.framework.Test;
import junit.framework.TestCase;

/**
 * <p>Title: </p> <p>Description: </p> <p>Copyright: Copyright (c) 2002</p> <p>Company: </p>
 * @author Carl Trusiak
 * @author Barbara McMillin
 * @version 1.0
 */
public class TestNewMemberAutomatedEmail extends TestCase {

    private static Properties props;

    public TestNewMemberAutomatedEmail(String s) {
        super(s);
    }

    public void setUp() {
        if (props == null) {
            props = new Properties();
            ClassLoader cl = this.getClass().getClassLoader();
            try {
                props.load(cl.getResourceAsStream("pittjug-TestEmail.properties"));
                AutomatedEmail.init(props);
            } catch (IOException ioe) {
            }
        }
    }

    public void testSend() {
        try {
            assertNotNull("Properties not properly loaded", props);
            Hashtable ht = new Hashtable();
            ht.put("site.name", "Pittjug ListServe Site");
            ht.put("member.name", "Joe Smith");
            ht.put("member.login", "jsmith");
            ht.put("member.password", "a34bc");
            ht.put("site.login.url", "some.url");
            AutomatedEmail email = AutomatedEmail.getInstance("NewMember");
            email.setMessage(ht);
            ht = new Hashtable();
            ht.put("site.name", "Pittjug ListServe Site");
            email.setSubject(ht);
            email.send((String) props.get("sendto.address"), (String) props.get("from.address"));
        } catch (Exception e) {
            e.printStackTrace();
            fail("Send failed");
        }
    }

    public static Test suite() {
        TestSuite suite = new TestSuite();
        suite.addTest(new TestNewMemberAutomatedEmail("testSend"));
        return suite;
    }

    public static void main(String args[]) {
        junit.textui.TestRunner.run(suite());
    }
}
