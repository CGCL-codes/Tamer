package br.net.woodstock.rockframework.security.test;

import java.security.SecureRandom;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import junit.framework.TestCase;
import br.net.woodstock.rockframework.security.crypt.KeyType;
import br.net.woodstock.rockframework.security.crypt.impl.SyncCrypter;
import br.net.woodstock.rockframework.utils.Base64Utils;

public class KeyTest extends TestCase {

    public void test1() throws Exception {
        KeyGenerator generator = KeyGenerator.getInstance(KeyType.DESEDE.getAlgorithm());
        SecureRandom random = new SecureRandom("pvspusugwwfkchfuwzntxtcwdoxkpsfl".getBytes());
        generator.init(168, random);
        SecretKey secretKey = generator.generateKey();
        byte[] bytes = Base64Utils.fromBase64("7ToDUys/Lo1T6DwxVrGS1Q==").getBytes();
        SyncCrypter crypter = new SyncCrypter(secretKey);
        System.out.println(crypter.decrypt(bytes));
    }
}
