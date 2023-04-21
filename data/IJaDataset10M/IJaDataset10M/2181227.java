package net.sf.maple.data.file.testing;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import net.sf.maple.crypt.CryptException;
import net.sf.maple.crypt.PasswordCipher;
import net.sf.maple.data.file.PropFile;
import net.sf.maple.data.file.Saveable;
import junit.framework.TestCase;

public class Test_EncryptedWithinEncrypted extends TestCase {

    private EncryptedWithinEncrypted r1;

    private EncryptedWithinEncrypted r2;

    private File file;

    @Override
    public void setUp() throws IOException {
        file = File.createTempFile("record", ".props");
        file.deleteOnExit();
        r1 = new EncryptedWithinEncrypted();
        r1.setAutoSave(false);
        r1.setFiles(file);
        Saveable.load(r1);
        r2 = new EncryptedWithinEncrypted();
        r2.setAutoSave(false);
        r2.setFiles(file);
    }

    public void test1() {
        assertEquals(3, r1.n);
        assertEquals("user", r1.er.username);
        assertEquals("pass", r1.er.password);
        assertEquals(3, r2.n);
        assertEquals("user", r2.er.username);
        assertEquals("pass", r2.er.password);
    }

    public void test2() throws IOException {
        r1.er.username = "U";
        r1.er.password = "P";
        r1.load();
        assertEquals("U", r1.er.username);
        assertEquals("P", r1.er.password);
        r2.load();
        assertEquals("user", r2.er.username);
        assertEquals("pass", r2.er.password);
    }

    public void test3() throws IOException {
        r1.er.username = "abc";
        r1.er.password = "xyz";
        r1.save();
        r2.load();
        assertEquals("abc", r2.er.username);
        assertEquals("xyz", r2.er.password);
    }

    public void test4() throws IOException {
        r1.er.username = "abc";
        r1.er.password = null;
        r1.save();
        r2.load();
        assertEquals("abc", r2.er.username);
        assertEquals(null, r2.er.password);
    }

    public void test5() throws IOException {
        r1.er.username = "abc";
        r1.er.password = "";
        r1.save();
        r2.load();
        assertEquals("abc", r2.er.username);
        assertEquals("", r2.er.password);
    }

    public void test6() throws IOException, CryptException {
        r1.er.username = "abc";
        r1.er.password = "xyz";
        r1.save();
        r2.load();
        assertEquals("abc", r2.er.username);
        assertEquals("xyz", r2.er.password);
        Map<String, String> map = PropFile.load(new FileInputStream(file));
        assertEquals(r2.er.username, map.get("er.username"));
        String pw = map.get("er.password");
        assertNotNull(pw);
        PasswordCipher cipher = new PasswordCipher("norwegianblue");
        String clearText = cipher.decrypt(pw);
        assertEquals(r2.er.password, clearText);
    }

    public void test7() throws IOException, CryptException {
        String un = "un";
        String pw = "pw";
        Map<String, String> map = new HashMap<String, String>();
        map.put("er.username", un);
        PasswordCipher cipher = new PasswordCipher("norwegianblue");
        map.put("er.password", cipher.encrypt(pw));
        PropFile.save(map, "", new FileOutputStream(file));
        assertFalse(un.equals(r1.er.username));
        assertFalse(pw.equals(r1.er.password));
        r1.load();
        assertEquals(un, r1.er.username);
        assertEquals(pw, r1.er.password);
        assertFalse(un.equals(r2.er.username));
        assertFalse(pw.equals(r2.er.password));
        r2.load();
        assertEquals(un, r2.er.username);
        assertEquals(pw, r2.er.password);
    }

    public static void main(String[] args) throws IOException, CryptException {
        Test_EncryptedWithinEncrypted ts = new Test_EncryptedWithinEncrypted();
        ts.setUp();
        ts.test7();
    }
}
