package org.nfcsigning.algorithm;

import java.io.IOException;
import org.bouncycastle.asn1.x509.RSAPublicKeyStructure;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.crypto.AsymmetricBlockCipher;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.engines.RSAEngine;
import org.bouncycastle.crypto.params.RSAKeyParameters;
import org.bouncycastle.crypto.signers.PSSSigner;

/**
 *
 * @author Markus Kilås
 */
public class RSASSAPSSVerifier implements SignatureVerifier {

    private static RSASSAPSSVerifier instance;

    public static RSASSAPSSVerifier getInstance() {
        if (instance == null) {
            instance = new RSASSAPSSVerifier();
        }
        return instance;
    }

    public boolean verifySignature(byte[] signature, byte[] coveredBytes, PublicKey key) {
        try {
            SubjectPublicKeyInfo pkInfo = key.getSubjectPublicKeyInfo();
            RSAPublicKeyStructure pubKey = RSAPublicKeyStructure.getInstance(pkInfo.getPublicKey());
            RSAKeyParameters pubParameters = new RSAKeyParameters(true, pubKey.getModulus(), pubKey.getPublicExponent());
            AsymmetricBlockCipher rsaEngine = new RSAEngine();
            rsaEngine.init(false, pubParameters);
            Digest digest = DigestFactory.getDigest("SHA-1");
            PSSSigner signer = new PSSSigner(rsaEngine, digest, digest.getDigestSize());
            signer.init(true, pubParameters);
            signer.update(coveredBytes, 0, coveredBytes.length);
            return signer.verifySignature(signature);
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new RuntimeException("IOException: " + ex.getMessage());
        }
    }
}
