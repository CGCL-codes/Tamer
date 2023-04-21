package cz.zuran.blog;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests the OpenSslCrypter with known encrypted strings and their meanings.
 *
 * @author Tomas Langer (tomas.langer@maersk.com)
 */
public class OpenSslCrypterTest {

    @Test
    public void testCrypter() throws Exception {
        testDecrypter("blogpassphrase", "U2FsdGVkX1+9/5jkQcrjZZZHWn/SBThhw9ntqJRRDsE=", "Hello blog!");
        testDecrypter("blogpassphrase", "U2FsdGVkX1/EdBJIwqB11in/KfdC7Cp02AwdMlAWm/Y=", "Hello blog!");
        testDecrypter("nothardcoded", "U2FsdGVkX1/r7SIR5e2ZhfbxmAMXiKBc5/6og0sRKq4=", "Hello blog!");
    }

    @Test
    public void testIsEncrypted() {
        testIsEncrypted("hula hula", false);
        testIsEncrypted("Random string", false);
        testIsEncrypted("U2FsdGVkX18GqS1oFTNzK9MqZAhytAbHXMiDrwFMe2A=", true);
        testIsEncrypted("U2FsdGVkX19nvI3bLwUVxe9eFeI50CPKhQVX7latnJg=", true);
    }

    private void testIsEncrypted(String toTest, boolean isEncrypted) {
        boolean actual = OpenSslCrypter.isEncrypted(toTest);
        assertTrue("String " + toTest + " is actually " + (isEncrypted ? "" : "not ") + "encrypted. Wrong result.", actual == isEncrypted);
    }

    private void testEncrypter(String password, String original) throws Exception {
        String encrypted = OpenSslCrypter.encrypt(original, password);
        String decrypted = OpenSslCrypter.decrypt(encrypted, password);
        assertEquals(original, decrypted);
    }

    private void testDecrypter(String password, String encrypted, String expected) throws Exception {
        String decrypted = OpenSslCrypter.decrypt(encrypted, password);
        assertEquals(expected, decrypted);
    }
}
