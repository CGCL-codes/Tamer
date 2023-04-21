package org.ccnx.ccn.test.io.content;

import static org.junit.Assert.fail;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import org.bouncycastle.util.Arrays;
import org.ccnx.ccn.io.content.ContentNotReadyException;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Test low level (non-CCN) SerializableObject functionality, backing objects to ByteArrayOutputStreams.
 **/
public class SerializableObjectTest {

    static KeyPair kp1 = null;

    static KeyPair kp2 = null;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(512);
        kp1 = kpg.generateKeyPair();
        kp2 = kpg.generateKeyPair();
    }

    @Test
    public void testSave() {
        SerializablePublicKey spk1 = new SerializablePublicKey(kp1.getPublic());
        SerializablePublicKey spk2 = new SerializablePublicKey(kp1.getPublic());
        SerializablePublicKey spk3 = new SerializablePublicKey(kp2.getPublic());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
        ByteArrayOutputStream baos3 = new ByteArrayOutputStream();
        try {
            spk1.save(baos);
            spk2.save(baos2);
            Assert.assertArrayEquals("Serializing two versions of same content should produce same output", baos.toByteArray(), baos2.toByteArray());
            spk3.save(baos3);
            boolean be = Arrays.areEqual(baos.toByteArray(), baos3.toByteArray());
            Assert.assertFalse("Two different objects shouldn't have matching output.", be);
            System.out.println("Saved two public keys, lengths " + baos.toByteArray().length + " and " + baos3.toByteArray().length);
        } catch (IOException e) {
            fail("IOException! " + e.getMessage());
        }
    }

    @Test
    public void testUpdate() {
        boolean caught = false;
        SerializablePublicKey empty = new SerializablePublicKey();
        try {
            empty.publicKey();
        } catch (ContentNotReadyException iox) {
            caught = true;
        } catch (IOException ie) {
            Assert.fail("Unexpectd IOException!");
        }
        Assert.assertTrue("Failed to produce expected exception.", caught);
        SerializablePublicKey spk1 = new SerializablePublicKey(kp1.getPublic());
        SerializablePublicKey spk2 = new SerializablePublicKey();
        SerializablePublicKey spk3 = new SerializablePublicKey(kp2.getPublic());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ByteArrayOutputStream baos3 = new ByteArrayOutputStream();
        try {
            spk1.save(baos);
            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            spk2.update(bais);
            Assert.assertEquals("Writing content out and back in again should give matching object.", spk1, spk2);
            spk3.save(baos3);
            boolean be = Arrays.areEqual(baos.toByteArray(), baos3.toByteArray());
            Assert.assertFalse("Two different objects shouldn't have matching output.", be);
            System.out.println("Saved two public keys, lengths " + baos.toByteArray().length + " and " + baos3.toByteArray().length);
        } catch (IOException e) {
            fail("IOException! " + e.getMessage());
        }
    }
}
