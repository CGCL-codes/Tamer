package org.apache.harmony.security.tests.provider.crypto.serialization;

import org.apache.harmony.testframework.serialization.SerializationTest;
import org.apache.harmony.testframework.serialization.SerializationTest.SerializableAssert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import java.security.Security;
import java.security.SecureRandom;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.Serializable;

/**
 * Tests against SecureRandom with SHA1PRNG serialization
 */
public class SHA1PRNG_SecureRandomTest extends TestCase {

    private static final int LIMIT1 = 100;

    private static final int LIMIT2 = 50;

    private static final int CASES = 4;

    static String algorithm = "SHA1PRNG";

    static String provider = "Crypto";

    private int testcase = 0;

    private byte zero[] = new byte[0];

    private int flag;

    private static final int SELF = 0;

    private static final int GOLDEN = 1;

    /**
     * @return array of SecureRandom objects to be deserialized in tests.
     */
    protected Object[] getData() {
        SecureRandom sr;
        Object[] data = new Object[5];
        for (int i = 0; i < data.length; i++) {
            try {
                sr = SecureRandom.getInstance(algorithm, provider);
                switch(i) {
                    case 0:
                        break;
                    case 1:
                        sr.setSeed(zero);
                        break;
                    case 2:
                        sr.setSeed(new byte[] { (byte) 1 });
                        break;
                    case 3:
                        sr.nextBytes(zero);
                        break;
                    case 4:
                        sr.nextBytes(new byte[1]);
                        break;
                }
                data[i] = sr;
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException("ATTENTION: " + e);
            } catch (NoSuchProviderException e) {
                throw new RuntimeException("ATTENTION: " + e);
            }
        }
        return data;
    }

    ;

    /**
     * Comparing sequencies of bytes 
     * returned by "nextBytes(..)" of referenced and tested objects
     */
    private void testingSame(SecureRandom ref, SecureRandom test) {
        byte refBytes[] = null;
        byte testBytes[] = null;
        for (int k = 0; k < LIMIT2; k++) {
            for (int i = 0; i < LIMIT1; i++) {
                refBytes = new byte[i];
                testBytes = new byte[i];
                ref.nextBytes(refBytes);
                test.nextBytes(testBytes);
                for (int j = 0; j < refBytes.length; j++) {
                    assertTrue("NOT same:  testcase =" + testcase + " k=" + k + " i=" + i + " j=" + j + " refBytes[j]=" + refBytes[j] + " testBytes[j]=" + testBytes[j], refBytes[j] == testBytes[j]);
                }
            }
            ref.setSeed(refBytes);
            test.setSeed(testBytes);
        }
    }

    /**
     * Comparing sequencies of bytes 
     * returned by "nextBytes(..)" of referenced and tested objects
     */
    private void testingNotSame(SecureRandom ref, SecureRandom test) {
        byte refTotalBytes[] = new byte[(LIMIT1 * LIMIT1) / 2];
        byte testTotalBytes[] = new byte[(LIMIT1 * LIMIT1) / 2];
        for (int k = 0; k < LIMIT2; k++) {
            byte refBytes[] = null;
            byte testBytes[] = null;
            int n = 0;
            for (int i = 0; i < LIMIT1; i++) {
                refBytes = new byte[i];
                testBytes = new byte[i];
                ref.nextBytes(refBytes);
                test.nextBytes(testBytes);
                System.arraycopy(refBytes, 0, refTotalBytes, n, refBytes.length);
                System.arraycopy(testBytes, 0, testTotalBytes, n, testBytes.length);
                n += i;
            }
            boolean b = true;
            int j = 0;
            for (int n1 = 0; n1 <= n; n1++) {
                b &= refTotalBytes[n1] == testTotalBytes[n1];
                if (j >= 20 || n1 == n) {
                    assertFalse("the same sequencies :: testcase=" + testcase + " k=" + k + "n1 =" + n1, b);
                    b = true;
                    j = 0;
                }
                j++;
            }
            ref.setSeed(refBytes);
            test.setSeed(testBytes);
        }
    }

    private SerializableAssert comparator = new SerializableAssert() {

        /**
         * Tests that data objects can be serialized and deserialized without exceptions 
         * and the deserialization produces object of the same class.
         */
        public void assertDeserialized(Serializable reference, Serializable test) {
            SecureRandom ref = (SecureRandom) reference;
            SecureRandom tst = (SecureRandom) test;
            boolean b;
            byte seed[] = new byte[] { 0 };
            switch(testcase) {
                case 0:
                    ref.setSeed(zero);
                    tst.setSeed(zero);
                    testingSame(ref, tst);
                    break;
                case 5:
                    ref.setSeed(seed);
                    tst.setSeed(seed);
                    testingSame(ref, tst);
                    break;
                case 10:
                    ref.nextBytes(zero);
                    tst.nextBytes(zero);
                    testingNotSame(ref, tst);
                    break;
                case 15:
                    ref.nextBytes(seed);
                    tst.nextBytes(seed);
                    testingNotSame(ref, tst);
                    break;
                case 1:
                    ref.setSeed(zero);
                    tst.setSeed(zero);
                    testingSame(ref, tst);
                    break;
                case 6:
                    ref.setSeed(seed);
                    tst.setSeed(seed);
                    testingSame(ref, tst);
                    break;
                case 11:
                    ref.nextBytes(zero);
                    tst.nextBytes(zero);
                    testingSame(ref, tst);
                    break;
                case 16:
                    ref.nextBytes(seed);
                    tst.nextBytes(seed);
                    testingSame(ref, tst);
                    break;
                case 2:
                    ref.setSeed(zero);
                    tst.setSeed(zero);
                    testingSame(ref, tst);
                    break;
                case 7:
                    ref.setSeed(seed);
                    tst.setSeed(seed);
                    testingSame(ref, tst);
                    break;
                case 12:
                    ref.nextBytes(zero);
                    tst.nextBytes(zero);
                    testingSame(ref, tst);
                    break;
                case 17:
                    ref.nextBytes(seed);
                    tst.nextBytes(seed);
                    testingSame(ref, tst);
                    break;
                case 3:
                    ref.setSeed(zero);
                    tst.setSeed(zero);
                    if (flag == SELF) {
                        testingSame(ref, tst);
                    } else {
                        testingNotSame(ref, tst);
                    }
                    break;
                case 8:
                    ref.setSeed(seed);
                    tst.setSeed(seed);
                    if (flag == SELF) {
                        testingSame(ref, tst);
                    } else {
                        testingNotSame(ref, tst);
                    }
                    break;
                case 13:
                    ref.nextBytes(zero);
                    tst.nextBytes(zero);
                    if (flag == SELF) {
                        testingSame(ref, tst);
                    } else {
                        testingNotSame(ref, tst);
                    }
                    break;
                case 18:
                    ref.nextBytes(seed);
                    tst.nextBytes(seed);
                    if (flag == SELF) {
                        testingSame(ref, tst);
                    } else {
                        testingNotSame(ref, tst);
                    }
                    break;
                case 4:
                    ref.setSeed(zero);
                    tst.setSeed(zero);
                    if (flag == SELF) {
                        testingSame(ref, tst);
                    } else {
                        testingNotSame(ref, tst);
                    }
                    break;
                case 9:
                    ref.setSeed(seed);
                    tst.setSeed(seed);
                    if (flag == SELF) {
                        testingSame(ref, tst);
                    } else {
                        testingNotSame(ref, tst);
                    }
                    break;
                case 14:
                    ref.nextBytes(zero);
                    tst.nextBytes(zero);
                    if (flag == SELF) {
                        testingSame(ref, tst);
                    } else {
                        testingNotSame(ref, tst);
                    }
                    break;
                case 19:
                    ref.nextBytes(seed);
                    tst.nextBytes(seed);
                    if (flag == SELF) {
                        testingSame(ref, tst);
                    } else {
                        testingNotSame(ref, tst);
                    }
                    break;
                default:
                    fail("ATTENTION: default case is not expected to happen");
            }
            testcase++;
        }
    };

    /**
     * Testing deserialized object.
     */
    public void testSerializationSelf() throws Exception {
        Object[] data;
        flag = SELF;
        for (int i = 0; i < CASES; i++) {
            data = getData();
            SerializationTest.verifySelf(data, comparator);
        }
    }

    /**
     * Testing that SecureRandom with SHA1PRNG objects can be deserialized from golden files.
     */
    public void testSerializationCompartibility() throws Exception {
        Object[] data;
        flag = GOLDEN;
        for (int i = 0; i < CASES; i++) {
            data = getData();
            SerializationTest.verifyGolden(this, data, comparator);
        }
    }

    public static Test suite() {
        return new TestSuite(SHA1PRNG_SecureRandomTest.class);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }
}
