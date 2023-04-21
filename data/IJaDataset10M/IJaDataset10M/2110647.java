package org.metastatic.net.ssh2;

import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.security.SignatureException;
import java.security.interfaces.RSAPublicKey;

/**
 * Concrete implementation of {@link PublicKeyParameters} for the RSA
 * signature algorithm.
 *
 * @version $Revision: 180 $
 */
public class RSAParameters extends PublicKeyParameters {

    public RSAParameters(String username, KeyPair keys, String service) {
        super(username, keys, service);
        alg_name = "ssh-rsa";
    }

    public byte[] makePublicKeyBlob() {
        PacketOutputStream pout = new PacketOutputStream();
        RSAPublicKey pubkey = (RSAPublicKey) keys.getPublic();
        pout.writeASCII(alg_name);
        pout.writeMPint(pubkey.getPublicExponent());
        pout.writeMPint(pubkey.getModulus());
        return pout.getPayload();
    }

    public byte[] makeSignature(byte[] data) throws SSH2Exception {
        try {
            PacketOutputStream pout = new PacketOutputStream(new Configuration());
            Signature s = Signature.getInstance("SHA1withRSA");
            s.initSign(keys.getPrivate());
            s.update(data);
            byte[] sig = s.sign();
            pout.writeASCII(alg_name);
            pout.writeString(sig);
            return pout.getPayload();
        } catch (NoSuchAlgorithmException nsae) {
            throw new SSH2Exception("no implementation of RSA available");
        } catch (InvalidKeyException ike) {
            throw new SSH2Exception("key pair is not a valid RSA key");
        } catch (SignatureException se) {
            throw new SSH2Exception("signing failed: " + se.getMessage());
        }
    }
}
