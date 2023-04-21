package org.nfcsigning.bc.crypto.signers;

import org.bouncycastle.crypto.AsymmetricBlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.CryptoException;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.Signer;
import org.bouncycastle.crypto.params.RSABlindingParameters;
import org.bouncycastle.crypto.params.RSAKeyParameters;

/**
 *
 * @author Markus Kilås
 */
public class PKCS1Signer implements Signer {

    private Digest digest;

    private AsymmetricBlockCipher cipher;

    private int hLen;

    private int kLen;

    public PKCS1Signer(AsymmetricBlockCipher cipher, Digest digest) {
        this.cipher = cipher;
        this.digest = digest;
        this.hLen = digest.getDigestSize();
    }

    public void init(boolean forSigning, CipherParameters params) {
        cipher.init(forSigning, params);
        RSAKeyParameters kParam;
        if (params instanceof RSABlindingParameters) {
            kParam = ((RSABlindingParameters) params).getPublicKey();
        } else {
            kParam = (RSAKeyParameters) params;
        }
        kLen = (kParam.getModulus().bitLength() + 7) / 8;
        reset();
    }

    public void update(byte b) {
        digest.update(b);
    }

    public void update(byte[] in, int off, int len) {
        digest.update(in, off, len);
    }

    public void reset() {
        digest.reset();
    }

    public byte[] generateSignature() throws CryptoException, DataLengthException {
        byte[] em = emsaPkcs1v15Encode(kLen);
        byte[] s = cipher.processBlock(em, 0, em.length);
        return s;
    }

    public boolean verifySignature(byte[] signature) {
        if (signature.length != kLen) {
            return false;
        }
        byte[] em;
        try {
            em = cipher.processBlock(signature, 0, signature.length);
            em[0] = (byte) 0x00;
            em[1] = (byte) 0x01;
        } catch (Exception ex) {
            return false;
        }
        byte[] emPrime = emsaPkcs1v15Encode(em.length);
        return PKCS1Signer.equals(em, emPrime);
    }

    private static final byte[] SHA1_WITH_RSA_ENCRYPTION = { (byte) 0x30, (byte) 0x21, (byte) 0x30, (byte) 0x09, (byte) 0x06, (byte) 0x05, (byte) 0x2b, (byte) 0x0e, (byte) 0x03, (byte) 0x02, (byte) 0x1a, (byte) 0x05, (byte) 0x00, (byte) 0x04, (byte) 0x14 };

    byte[] emsaPkcs1v15Encode(int emLength) {
        byte[] h = new byte[hLen];
        digest.doFinal(h, 0);
        byte[] digestInfo = SHA1_WITH_RSA_ENCRYPTION;
        byte[] t = new byte[digestInfo.length + h.length];
        System.arraycopy(digestInfo, 0, t, 0, digestInfo.length);
        System.arraycopy(h, 0, t, digestInfo.length, h.length);
        if (emLength < t.length + 11) {
            throw new IllegalArgumentException("intended encoded message length too short");
        }
        int psLen = (emLength - t.length - 3);
        byte[] emPrime = new byte[2 + psLen + 1 + t.length];
        for (int i = 2; i < psLen + 2; i++) {
            emPrime[i] = (byte) 0xff;
        }
        emPrime[0] = (byte) 0x00;
        emPrime[1] = (byte) 0x01;
        emPrime[2 + psLen] = (byte) 0x00;
        System.arraycopy(t, 0, emPrime, 2 + psLen + 1, t.length);
        return emPrime;
    }

    private static boolean equals(byte[] bytes1, byte[] bytes2) {
        if (bytes1 == null && bytes2 == null) {
            return true;
        } else if (bytes1 == null || bytes2 == null) {
            return false;
        }
        if (bytes1.length != bytes2.length) {
            return false;
        }
        for (int i = 0; i < bytes1.length; i++) {
            if (bytes1[i] != bytes2[i]) {
                return false;
            }
        }
        return true;
    }
}
