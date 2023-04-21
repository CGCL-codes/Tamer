package com.ericdaugherty.mail.server.crypto;

import java.io.IOException;
import javax.crypto.spec.SecretKeySpec;
import junit.framework.*;
import com.ericdaugherty.mail.server.crypto.mac.HMACParameterSpec;
import com.ericdaugherty.mail.server.crypto.mac.JESMac;
import com.ericdaugherty.mail.server.utils.ByteUtils;

/**
 *
 * @author Andreas Kyrmegalos
 */
public class HMACTest extends TestCase {

    /**
    * The primary class responsible for executing the available tests
    *
    * @param testName
    * @throws java.io.IOException
    */
    public HMACTest(String testName) throws IOException {
        super(testName);
    }

    public static Test suite() {
        return new TestSuite(HMACTest.class);
    }

    private final char[] hexDigits = "0123456789abcdef".toCharArray();

    public void testConvertors() {
        char[] hex = new char[2];
        byte[] hexByte;
        for (int i = 0x0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                hex[0] = hexDigits[i];
                hex[1] = hexDigits[j];
                hexByte = ByteUtils.toByteArray(hex);
                new String(ByteUtils.toHex(hexByte)).equals(String.copyValueOf(hex));
            }
        }
    }

    private boolean execute(byte[] key, byte[] data, String digest, String algorithm, HMACParameterSpec spec) throws Exception {
        byte[] digestBytes;
        JESMac mac = JESMac.getInstance(algorithm);
        mac.init(new SecretKeySpec(key, algorithm), spec);
        mac.update(data, 0, data.length);
        digestBytes = mac.doFinal();
        return new String(ByteUtils.toHex(digestBytes)).equals(digest);
    }

    public void test1HMACMD5() throws Exception {
        String key = "0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b";
        String data = "Hi There";
        String digest = "9294727a3638bb1c13f48ef8158bfc9d";
        assertTrue(execute(ByteUtils.toByteArray(key.toCharArray()), data.getBytes("US-ASCII"), digest, "HmacMD5", new HMACParameterSpec()));
    }

    public void test2HMACMD5() throws Exception {
        String key = "Jefe";
        String data = "what do ya want for nothing?";
        String digest = "750c783e6ab0b503eaa86e310a5db738";
        assertTrue(execute(key.getBytes("US-ASCII"), data.getBytes("US-ASCII"), digest, "HmacMD5", new HMACParameterSpec()));
    }

    public void test3HMACMD5() throws Exception {
        String key = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
        String data = "dddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd";
        String digest = "56be34521d144c88dbb8c733f0e8b3f6";
        assertTrue(execute(ByteUtils.toByteArray(key.toCharArray()), ByteUtils.toByteArray(data.toCharArray()), digest, "HmacMD5", new HMACParameterSpec()));
    }

    public void test4HMACMD5() throws Exception {
        String key = "0102030405060708090a0b0c0d0e0f10111213141516171819";
        String data = "cdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcd";
        String digest = "697eaf0aca3a3aea3a75164746ffaa79";
        assertTrue(execute(ByteUtils.toByteArray(key.toCharArray()), ByteUtils.toByteArray(data.toCharArray()), digest, "HmacMD5", new HMACParameterSpec()));
    }

    public void test5HMACMD5() throws Exception {
        String key = "0c0c0c0c0c0c0c0c0c0c0c0c0c0c0c0c";
        String data = "Test With Truncation";
        String digest = "56461ef2342edc00f9bab995690efd4c";
        String digest96 = "56461ef2342edc00f9bab995";
        assertTrue(execute(ByteUtils.toByteArray(key.toCharArray()), data.getBytes("US-ASCII"), digest, "HmacMD5", new HMACParameterSpec()));
        assertTrue(execute(ByteUtils.toByteArray(key.toCharArray()), data.getBytes("US-ASCII"), digest96, "HmacMD5", new HMACParameterSpec(true)));
    }

    public void test6HMACMD5() throws Exception {
        String key = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
        String data = "Test Using Larger Than Block-Size Key - Hash Key First";
        String digest = "6b1ab7fe4bd7bf8f0b62e6ce61b9d0cd";
        assertTrue(execute(ByteUtils.toByteArray(key.toCharArray()), data.getBytes("US-ASCII"), digest, "HmacMD5", new HMACParameterSpec()));
    }

    public void test7HMACMD5() throws Exception {
        String key = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
        String data = "Test Using Larger Than Block-Size Key and Larger Than One Block-Size Data";
        String digest = "6f630fad67cda0ee1fb1f562db3aa53e";
        assertTrue(execute(ByteUtils.toByteArray(key.toCharArray()), data.getBytes("US-ASCII"), digest, "HmacMD5", new HMACParameterSpec()));
    }

    public void test1HMACSHA1() throws Exception {
        String key = "0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b";
        String data = "Hi There";
        String digest = "b617318655057264e28bc0b6fb378c8ef146be00";
        assertTrue(execute(ByteUtils.toByteArray(key.toCharArray()), data.getBytes("US-ASCII"), digest, "HmacSHA1", new HMACParameterSpec()));
    }

    public void test2HMACSHA1() throws Exception {
        String key = "Jefe";
        String data = "what do ya want for nothing?";
        String digest = "effcdf6ae5eb2fa2d27416d5f184df9c259a7c79";
        assertTrue(execute(key.getBytes("US-ASCII"), data.getBytes("US-ASCII"), digest, "HmacSHA1", new HMACParameterSpec()));
    }

    public void test3HMACSHA1() throws Exception {
        String key = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
        String data = "dddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd";
        String digest = "125d7342b9ac11cd91a39af48aa17b4f63f175d3";
        assertTrue(execute(ByteUtils.toByteArray(key.toCharArray()), ByteUtils.toByteArray(data.toCharArray()), digest, "HmacSHA1", new HMACParameterSpec()));
    }

    public void test4HMACSHA1() throws Exception {
        String key = "0102030405060708090a0b0c0d0e0f10111213141516171819";
        String data = "cdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcd";
        String digest = "4c9007f4026250c6bc8414f9bf50c86c2d7235da";
        assertTrue(execute(ByteUtils.toByteArray(key.toCharArray()), ByteUtils.toByteArray(data.toCharArray()), digest, "HmacSHA1", new HMACParameterSpec()));
    }

    public void test5HMACSHA1() throws Exception {
        String key = "0c0c0c0c0c0c0c0c0c0c0c0c0c0c0c0c0c0c0c0c";
        String data = "Test With Truncation";
        String digest = "4c1a03424b55e07fe7f27be1d58bb9324a9a5a04";
        String digest96 = "4c1a03424b55e07fe7f27be1";
        assertTrue(execute(ByteUtils.toByteArray(key.toCharArray()), data.getBytes("US-ASCII"), digest, "HmacSHA1", new HMACParameterSpec()));
        assertTrue(execute(ByteUtils.toByteArray(key.toCharArray()), data.getBytes("US-ASCII"), digest96, "HmacSHA1", new HMACParameterSpec(true)));
    }

    public void test6HMACSHA1() throws Exception {
        String key = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
        String data = "Test Using Larger Than Block-Size Key - Hash Key First";
        String digest = "aa4ae5e15272d00e95705637ce8a3b55ed402112";
        assertTrue(execute(ByteUtils.toByteArray(key.toCharArray()), data.getBytes("US-ASCII"), digest, "HmacSHA1", new HMACParameterSpec()));
    }

    public void test7HMACSHA1() throws Exception {
        String key = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
        String data = "Test Using Larger Than Block-Size Key and Larger Than One Block-Size Data";
        String digest = "e8e99d0f45237d786d6bbaa7965c7808bbff1a91";
        assertTrue(execute(ByteUtils.toByteArray(key.toCharArray()), data.getBytes("US-ASCII"), digest, "HmacSHA1", new HMACParameterSpec()));
    }

    public void test1HMACSHA2() throws Exception {
        String key = "0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b";
        String data = "Hi There";
        String digest256 = "b0344c61d8db38535ca8afceaf0bf12b" + "881dc200c9833da726e9376c2e32cff7";
        String digest384 = "afd03944d84895626b0825f4ab46907f" + "15f9dadbe4101ec682aa034c7cebc59c" + "faea9ea9076ede7f4af152e8b2fa9cb6";
        String digest512 = "87aa7cdea5ef619d4ff0b4241a1d6cb0" + "2379f4e2ce4ec2787ad0b30545e17cde" + "daa833b7d6b8a702038b274eaea3f4e4" + "be9d914eeb61f1702e696c203a126854";
        assertTrue(execute(ByteUtils.toByteArray(key.toCharArray()), data.getBytes("US-ASCII"), digest256, "HmacSHA256", new HMACParameterSpec()));
        assertTrue(execute(ByteUtils.toByteArray(key.toCharArray()), data.getBytes("US-ASCII"), digest384, "HmacSHA384", new HMACParameterSpec()));
        assertTrue(execute(ByteUtils.toByteArray(key.toCharArray()), data.getBytes("US-ASCII"), digest512, "HmacSHA512", new HMACParameterSpec()));
    }

    public void test2HMACSHA2() throws Exception {
        String key = "Jefe";
        String data = "what do ya want for nothing?";
        String digest256 = "5bdcc146bf60754e6a042426089575c7" + "5a003f089d2739839dec58b964ec3843";
        String digest384 = "af45d2e376484031617f78d2b58a6b1b" + "9c7ef464f5a01b47e42ec3736322445e" + "8e2240ca5e69e2c78b3239ecfab21649";
        String digest512 = "164b7a7bfcf819e2e395fbe73b56e0a3" + "87bd64222e831fd610270cd7ea250554" + "9758bf75c05a994a6d034f65f8f0e6fd" + "caeab1a34d4a6b4b636e070a38bce737";
        assertTrue(execute(key.getBytes("US-ASCII"), data.getBytes("US-ASCII"), digest256, "HmacSHA256", new HMACParameterSpec()));
        assertTrue(execute(key.getBytes("US-ASCII"), data.getBytes("US-ASCII"), digest384, "HmacSHA384", new HMACParameterSpec()));
        assertTrue(execute(key.getBytes("US-ASCII"), data.getBytes("US-ASCII"), digest512, "HmacSHA512", new HMACParameterSpec()));
    }

    public void test3HMACSHA2() throws Exception {
        String key = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
        String data = "dddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd";
        String digest256 = "773ea91e36800e46854db8ebd09181a7" + "2959098b3ef8c122d9635514ced565fe";
        String digest384 = "88062608d3e6ad8a0aa2ace014c8a86f" + "0aa635d947ac9febe83ef4e55966144b" + "2a5ab39dc13814b94e3ab6e101a34f27";
        String digest512 = "fa73b0089d56a284efb0f0756c890be9" + "b1b5dbdd8ee81a3655f83e33b2279d39" + "bf3e848279a722c806b485a47e67c807" + "b946a337bee8942674278859e13292fb";
        assertTrue(execute(ByteUtils.toByteArray(key.toCharArray()), ByteUtils.toByteArray(data.toCharArray()), digest256, "HmacSHA256", new HMACParameterSpec()));
        assertTrue(execute(ByteUtils.toByteArray(key.toCharArray()), ByteUtils.toByteArray(data.toCharArray()), digest384, "HmacSHA384", new HMACParameterSpec()));
        assertTrue(execute(ByteUtils.toByteArray(key.toCharArray()), ByteUtils.toByteArray(data.toCharArray()), digest512, "HmacSHA512", new HMACParameterSpec()));
    }

    public void test4HMACSHA2() throws Exception {
        String key = "0102030405060708090a0b0c0d0e0f10111213141516171819";
        String data = "cdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcdcd";
        String digest256 = "82558a389a443c0ea4cc819899f2083a" + "85f0faa3e578f8077a2e3ff46729665b";
        String digest384 = "3e8a69b7783c25851933ab6290af6ca7" + "7a9981480850009cc5577c6e1f573b4e" + "6801dd23c4a7d679ccf8a386c674cffb";
        String digest512 = "b0ba465637458c6990e5a8c5f61d4af7" + "e576d97ff94b872de76f8050361ee3db" + "a91ca5c11aa25eb4d679275cc5788063" + "a5f19741120c4f2de2adebeb10a298dd";
        assertTrue(execute(ByteUtils.toByteArray(key.toCharArray()), ByteUtils.toByteArray(data.toCharArray()), digest256, "HmacSHA256", new HMACParameterSpec()));
        assertTrue(execute(ByteUtils.toByteArray(key.toCharArray()), ByteUtils.toByteArray(data.toCharArray()), digest384, "HmacSHA384", new HMACParameterSpec()));
        assertTrue(execute(ByteUtils.toByteArray(key.toCharArray()), ByteUtils.toByteArray(data.toCharArray()), digest512, "HmacSHA512", new HMACParameterSpec()));
    }

    public void test6HMACSHA2() throws Exception {
        String key = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
        String data = "Test Using Larger Than Block-Size Key - Hash Key First";
        String digest256 = "60e431591ee0b67f0d8a26aacbf5b77f" + "8e0bc6213728c5140546040f0ee37f54";
        String digest384 = "4ece084485813e9088d2c63a041bc5b4" + "4f9ef1012a2b588f3cd11f05033ac4c6" + "0c2ef6ab4030fe8296248df163f44952";
        String digest512 = "80b24263c7c1a3ebb71493c1dd7be8b4" + "9b46d1f41b4aeec1121b013783f8f352" + "6b56d037e05f2598bd0fd2215d6a1e52" + "95e64f73f63f0aec8b915a985d786598";
        assertTrue(execute(ByteUtils.toByteArray(key.toCharArray()), data.getBytes("US-ASCII"), digest256, "HmacSHA256", new HMACParameterSpec()));
        assertTrue(execute(ByteUtils.toByteArray(key.toCharArray()), data.getBytes("US-ASCII"), digest384, "HmacSHA384", new HMACParameterSpec()));
        assertTrue(execute(ByteUtils.toByteArray(key.toCharArray()), data.getBytes("US-ASCII"), digest512, "HmacSHA512", new HMACParameterSpec()));
    }

    public void test7HMACSHA2() throws Exception {
        String key = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
        String data = "This is a test using a larger than block-size key and a larger than block-size data. The key needs to be hashed before being used by the HMAC algorithm.";
        String digest256 = "9b09ffa71b942fcb27635fbcd5b0e944" + "bfdc63644f0713938a7f51535c3a35e2";
        String digest384 = "6617178e941f020d351e2f254e8fd32c" + "602420feb0b8fb9adccebb82461e99c5" + "a678cc31e799176d3860e6110c46523e";
        String digest512 = "e37b6a775dc87dbaa4dfa9f96e5e3ffd" + "debd71f8867289865df5a32d20cdc944" + "b6022cac3c4982b10d5eeb55c3e4de15" + "134676fb6de0446065c97440fa8c6a58";
        assertTrue(execute(ByteUtils.toByteArray(key.toCharArray()), data.getBytes("US-ASCII"), digest256, "HmacSHA256", new HMACParameterSpec()));
        assertTrue(execute(ByteUtils.toByteArray(key.toCharArray()), data.getBytes("US-ASCII"), digest384, "HmacSHA384", new HMACParameterSpec()));
        assertTrue(execute(ByteUtils.toByteArray(key.toCharArray()), data.getBytes("US-ASCII"), digest512, "HmacSHA512", new HMACParameterSpec()));
    }
}
