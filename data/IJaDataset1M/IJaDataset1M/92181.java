package org.personalsmartspace.spm.policy.impl.negotiation;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;
import org.personalsmartspace.spm.negotiation.api.platform.IAgreement;

/**
 * @author Elizabeth
 *
 */
public class AgreementFinaliser {

    private KeyPair keypair;

    public AgreementFinaliser() {
    }

    public byte[] signAgreement(IAgreement agreement) {
        this.generateKeys();
        if (keypair == null) {
            System.out.println("Unable to generate Keys for signing the Agreement object");
            return null;
        }
        try {
            Signature dsa = Signature.getInstance("SHA1withDSA", "SUN");
            dsa.initSign(this.keypair.getPrivate());
            byte[] byteArray = this.getBytes(agreement);
            dsa.update(byteArray);
            return dsa.sign();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SignatureException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }
        return null;
    }

    private byte[] getBytes(Object obj) throws java.io.IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(obj);
        oos.flush();
        oos.close();
        bos.close();
        byte[] data = bos.toByteArray();
        return data;
    }

    private void generateKeys() {
        try {
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("DSA");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
            kpg.initialize(1024, random);
            this.keypair = kpg.generateKeyPair();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}
