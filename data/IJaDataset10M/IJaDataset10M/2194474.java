package net.sf.gridarta.utils;

import java.util.Arrays;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Test;

/**
 * Regression tests for {@link Xtea}.
 * @author Andreas Kirschbaum
 */
public class XteaTest {

    /**
     * The key for the test.
     */
    @NotNull
    private static final byte[] key = { (byte) 0x12, (byte) 0x34, (byte) 0x56, (byte) 0x78, (byte) 0x9a, (byte) 0xbc, (byte) 0xde, (byte) 0xf0, (byte) 0x01, (byte) 0x23, (byte) 0x45, (byte) 0x67, (byte) 0x89, (byte) 0xab, (byte) 0xcd, (byte) 0xef };

    /**
     * The plaintext for the test.
     */
    @NotNull
    private static final byte[] plaintext = { (byte) 0x01, (byte) 0x02, (byte) 0x03, (byte) 0x04, (byte) 0x05, (byte) 0x06, (byte) 0x07, (byte) 0x08 };

    /**
     * The expected ciphertext corresponding to {@link #key} and {@link
     * #plaintext}.
     */
    @NotNull
    private static final byte[] ciphertext = { (byte) 0x06, (byte) 0x6a, (byte) 0xb0, (byte) 0x51, (byte) 0xf8, (byte) 0xa7, (byte) 0xa3, (byte) 0xc3 };

    /**
     * Checks that basic encryption and decryption works.
     */
    @Test
    public void test() {
        final Xtea xtea = new Xtea(key);
        final byte[][] tmp = new byte[2][8];
        xtea.encrypt(plaintext, tmp[0]);
        xtea.decrypt(tmp[0], tmp[1]);
        if (!Arrays.equals(tmp[0], ciphertext)) {
            Assert.fail("encrypt failure");
        }
        if (!Arrays.equals(tmp[1], plaintext)) {
            Assert.fail("decrypt failure");
        }
    }
}
