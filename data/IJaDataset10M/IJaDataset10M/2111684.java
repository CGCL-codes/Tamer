package edu.xjtu.se.hcy.encrypt;

import java.math.BigInteger;
import java.util.Map;

/**
 * 
 * @author 梁栋
 * @version 1.0
 * @since 1.0
 */
public class ECCCoderTest {

    public void test() throws Exception {
        String inputStr = "黄丛宇06161032";
        byte[] data = inputStr.getBytes();
        Map<String, Object> keyMap = ECCCoder.initKey();
        byte[] publicKey = ECCCoder.getPublicKey(keyMap);
        byte[] privateKey = ECCCoder.getPrivateKey(keyMap);
        System.out.println("公钥: \n" + publicKey);
        System.out.println("私钥： \n" + privateKey);
        byte[] encodedData = ECCCoder.encrypt(data, publicKey);
        BigInteger endate = new BigInteger(encodedData);
        System.out.println("加密后的数据:" + endate.toString(16));
        byte[] decodedData = ECCCoder.decrypt(encodedData, privateKey);
        String outputStr = new String(decodedData);
        System.out.println("加密前: " + inputStr + "\n\r" + "解密后: " + outputStr);
    }
}
