package org.toolme.security;

import jmunit.framework.cldc10.AssertionFailedException;
import jmunit.framework.cldc10.TestCase;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.java.security.SecureRandom;
import org.toolme.testUtils.TestUtils;

/**
 * Various test cases for the BlowfishCrypto class.
 */
public class BlowfishCryptoTest extends TestCase {

    /**
	 * Number of tests.
	 */
    private static final int TESTCOUNT = 1;

    /**
	 * Key length.
	 */
    private static final int KEY_LENGTH = 32;

    /**
	 * Initialise.
	 */
    public BlowfishCryptoTest() {
        super(TESTCOUNT, "BlowfishCryptoTest");
    }

    /**
	 * @param testNumber
	 *            current test number.
	 * @throws Throwable
	 *             test exception.
	 */
    public final void test(final int testNumber) throws Throwable {
        switch(testNumber) {
            case 0:
                testPerformDecrypt();
                break;
            default:
                break;
        }
    }

    /**
	 * Test of performDecrypt and performEncrypt methods, of the class
	 * BlowfishCrypto.
	 * 
	 * @throws AssertionFailedException
	 *             test exception.
	 */
    public final void testPerformDecrypt() throws AssertionFailedException {
        byte[] keyBytes = new byte[KEY_LENGTH];
        SecureRandom ran = new SecureRandom();
        ran.nextBytes(keyBytes);
        BlowfishCrypto crypto = new BlowfishCrypto(keyBytes);
        String plainText = TestUtils.generatePlainText();
        byte[] plainBytes = plainText.getBytes();
        byte[] cipherBytes;
        try {
            cipherBytes = crypto.performEncrypt(plainBytes);
            assertNotEquals(plainBytes.length, cipherBytes.length);
            String expectedResult = plainText;
            String actualResult = new String(crypto.performDecrypt(cipherBytes));
            assertEquals("Unencryption plain text should match original plain text.", expectedResult, actualResult);
        } catch (DataLengthException e) {
            fail("testPerformDecrypt(); " + e.getMessage());
        } catch (InvalidCipherTextException e) {
            fail("testPerformDecrypt(); " + e.getMessage());
        }
    }
}
