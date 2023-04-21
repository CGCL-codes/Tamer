package org.isurf.spmiddleware.security;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Unit tests for {@link EncrypterImpl}.
 */
public class EncrypterTest {

    private static Encrypter encrypter;

    /**
	 * Initialises the test.
	 */
    @BeforeClass
    public static void setUpBeforeClass() {
        String[] contexts = { "classpath:org/isurf/spmiddleware/security/SecurityApplicationContext.xml", "classpath:org/isurf/spmiddleware/dao/DAOApplicationContext.xml" };
        ApplicationContext context = new ClassPathXmlApplicationContext(contexts);
        encrypter = (Encrypter) context.getBean("encrypter");
    }

    /**
	 * Cleans up the test.
	 */
    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        encrypter = null;
    }

    /**
	 * Test method for {@link org.isurf.spmiddleware.security.EncrypterImpl#digest(java.lang.String)}.
	 */
    @Test
    public void testDigestUsingNull() {
        String encryptedValue = encrypter.digest(null);
        assertNull("encryptedValue aws not null", encryptedValue);
    }

    /**
	 * Test method for {@link org.isurf.spmiddleware.security.EncrypterImpl#encrypt(java.lang.String)}.
	 */
    @Test
    public void testEncryptUsingNull() {
        String encryptedValue = encrypter.encrypt(null);
        assertNull("encryptedValue aws not null", encryptedValue);
    }

    /**
	 * Test method for {@link org.isurf.spmiddleware.security.EncrypterImpl#encrypt(java.lang.String)}.
	 */
    @Test
    public void testDigest() {
        String digestValue = encrypter.digest("value to encrypt");
        assertEquals("ͷ����F�I�6�����", digestValue);
    }

    /**
	 * Test method for {@link org.isurf.spmiddleware.security.EncrypterImpl#encrypt(java.lang.String)}.
	 */
    @Test
    public void testEncrypt() {
        String encryptedValue = encrypter.encrypt("value to encrypt");
        assertEquals("126-7-117-141-17-49-174-44-30-105-245-78-182-121-196-158-115-216-27-31-236-173-165-226", encryptedValue);
    }

    /**
	 * Test method for {@link org.isurf.spmiddleware.security.EncrypterImpl#encrypt(java.lang.String)}.
	 */
    @Test
    public void testDecrypt() {
        String decryptedValue = encrypter.decrypt("126-7-117-141-17-49-174-44-30-105-245-78-182-121-196-158-115-216-27-31-236-173-165-226");
        assertEquals("value to encrypt", decryptedValue);
    }

    /**
	 * Test method for {@link org.isurf.spmiddleware.security.EncrypterImpl#encrypt(java.lang.String)}.
	 */
    @Test
    public void testEncryptionIsSymmetric() {
        String clearText = "testEncryptionIsSymmetric";
        String encryptedValue = encrypter.encrypt(clearText);
        assertEquals("Encryption was not symmetric", clearText, encrypter.decrypt(encryptedValue));
    }
}
