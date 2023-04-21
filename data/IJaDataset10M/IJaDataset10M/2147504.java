package test.org.p2pvpn.tools;

import java.security.KeyPair;
import java.security.PublicKey;
import org.junit.Before;
import org.junit.Test;
import org.p2pvpn.tools.AdvProperties;
import org.p2pvpn.tools.CryptoUtils;
import static org.junit.Assert.*;

public class TestAdvProperties {

    AdvProperties p;

    @Before
    public void before() {
        p = new AdvProperties();
        p.setProperty("a", "1");
        p.setProperty("b", "2");
        p.setProperty("c", "3");
    }

    @Test
    public void testStore() {
        assertEquals("a=1\nb=2\nc=3\n", p.toString(null, true, false));
    }

    @Test
    public void testSign() {
        KeyPair kp = CryptoUtils.createSignatureKeyPair();
        KeyPair kp2 = CryptoUtils.createSignatureKeyPair();
        p.sign("signature", kp.getPrivate());
        assertTrue(p.verify("signature", kp.getPublic()));
        assertFalse(p.verify("signature", kp2.getPublic()));
    }

    @Test
    public void testDecodeKey() {
        KeyPair kp = CryptoUtils.createSignatureKeyPair();
        p.setPropertyBytes("publicKey", kp.getPublic().getEncoded());
        p.sign("signature", kp.getPrivate());
        PublicKey key = CryptoUtils.decodeRSAPublicKey(p.getPropertyBytes("publicKey", null));
        assertTrue(p.verify("signature", key));
    }
}
