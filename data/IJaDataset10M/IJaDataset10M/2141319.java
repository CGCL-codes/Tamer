package test.base.user.JCard;

import java.io.FileInputStream;
import junit.framework.TestCase;
import base.user.jcard.Birthdate;
import base.user.jcard.ElectronicMail;
import base.user.jcard.JCard;
import base.user.jcard.Name;

public class JCardTest extends TestCase {

    public static void main(String[] args) {
        junit.textui.TestRunner.run(JCardTest.class);
    }

    public void testEnd2End() {
        try {
            JCard jcard = new JCard();
            Name name = (Name) jcard.addProperty("NAME");
            name.setGivenName("Lokesh");
            System.out.println(name.toString());
            Birthdate birthday = (Birthdate) jcard.addProperty("BIRTHDATE");
            birthday.setBirthdate(1980, 12, 11);
            ElectronicMail email = (ElectronicMail) jcard.addProperty("EMAIL");
            email.setEmailAddress("lokeshrj@users.sourceforge.net");
            jcard.write("testcard.vcf");
            FileInputStream fis = new FileInputStream("testcard.vcf");
            byte[] b = new byte[fis.available()];
            fis.read(b);
            String result = new String(b);
            String expected = new String("BEGIN:VCARD\r\nVERSION:2.1\r\nN:;Lokesh;;;\r\nBDAY:1980-12-11\r\nEMAIL;INTERNET:lokeshrj@users.sourceforge.net\r\nEND:VCARD");
            assertEquals(expected, result);
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    public void testAddProperty() {
    }

    public void testToString() {
    }
}
