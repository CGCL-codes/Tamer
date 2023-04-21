package cdc.standard.rsa;

import codec.*;
import codec.asn1.*;
import codec.pkcs1.*;
import codec.pkcs8.*;
import codec.x509.*;
import java.io.*;
import java.math.BigInteger;
import java.security.*;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
* This class implements the RSA signature algorithm as defined in
* <a href=http://>SSL</a>. Note that this is a special version of
* a RSA signature that does not use a PKCS#1-compliant DigestInfo
* structure!
* <p>
* @version 0.1
*/
public class SSLSignature extends SignatureSpi {

    /**
    * The source of randomness.
    */
    private SecureRandom secureRandom_;

    /**
    * The fist message digest: MD5.
    */
    private MessageDigest mdMD5_;

    /**
    * The second message digest: SHA1.
    */
    private MessageDigest mdSHA1_;

    /**
    * The cipher algorithm - here RSA.
    */
    private Cipher cipher_;

    /**
    * The standard constructor.
    */
    public SSLSignature() {
    }

    /**
    * This function does nothing.
    * <p>
    * @deprecated Not implemented.
    */
    protected Object engineGetParameter(String parameter) throws InvalidParameterException {
        return null;
    }

    /**
    * Initializes the signature algorithm for signing a message.
    * <p>
    * @param privateKey the private key of the signer.
    * @throws InvalidKeyException if the key is not an instance of RSAPrivKey.
    */
    protected void engineInitSign(PrivateKey privateKey) throws InvalidKeyException {
        secureRandom_ = new SecureRandom();
        try {
            mdSHA1_ = MessageDigest.getInstance("SHA1");
            mdMD5_ = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException nsae) {
            System.out.println("RSASignature: MD Algorithm not found");
            nsae.printStackTrace();
        }
        try {
            cipher_ = Cipher.getInstance("RSA");
        } catch (NoSuchAlgorithmException nsae) {
            System.err.println("RSASignature: RSA Algorithm not found");
            nsae.printStackTrace();
        } catch (NoSuchPaddingException nspe) {
            System.err.println("RSASignature: RSA Algorithm not found");
            nspe.printStackTrace();
        }
        cipher_.init(Cipher.ENCRYPT_MODE, privateKey, secureRandom_);
    }

    /**
    * Initializes the signature algorithm for signing a message.
    * <p>
    * @param privateKey the private key of the signer.
    * @param secureRandom the source of randomness.
    * @throws InvalidKeyException if the key is not an instance of RSAPrivKey.
    */
    protected void engineInitSign(PrivateKey privateKey, SecureRandom secureRandom) throws InvalidKeyException {
        secureRandom_ = secureRandom;
        try {
            mdSHA1_ = MessageDigest.getInstance("SHA1");
            mdMD5_ = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException nsae) {
            System.err.println("RSASignature: MD Algorithm not found");
            nsae.printStackTrace();
        }
        try {
            cipher_ = Cipher.getInstance("RSA");
        } catch (NoSuchAlgorithmException nsae) {
            System.err.println("RSASignature: RSA Algorithm not found");
            nsae.printStackTrace();
        } catch (NoSuchPaddingException nspe) {
            System.err.println("RSASignature: RSA Algorithm not found");
            nspe.printStackTrace();
        }
        cipher_.init(Cipher.ENCRYPT_MODE, privateKey, secureRandom);
    }

    /**
    * Initializes the signature algorithm for verifying a signature.
    * <p>
    * @param publicKey the public key of the signer.
    * @throws InvalidKeyException if the public key is not an instance of RSAPubKey.
    */
    protected void engineInitVerify(PublicKey publicKey) throws InvalidKeyException {
        try {
            mdSHA1_ = MessageDigest.getInstance("SHA1");
            mdMD5_ = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException nsae) {
            System.err.println("RSASignature: MD Algorithm not found");
            nsae.printStackTrace();
        }
        try {
            cipher_ = Cipher.getInstance("RSA");
        } catch (NoSuchAlgorithmException nsae) {
            System.err.println("RSASignature: RSA Algorithm not found");
            nsae.printStackTrace();
        } catch (NoSuchPaddingException nspe) {
            System.err.println("RSASignature: RSA Algorithm not found");
            nspe.printStackTrace();
        }
        cipher_.init(Cipher.DECRYPT_MODE, publicKey);
    }

    /**
    * This function does nothing.
    * <p>
    *
    * @deprecated Not implemented.
    */
    protected void engineSetParameter(String parameter, Object value) throws InvalidParameterException {
    }

    /**
    * Signs a message.
    * <p>
    * @return the signature.
    * @throws SignatureException if the signature is not initialized properly.
    */
    protected byte[] engineSign() throws SignatureException {
        byte[] out = null;
        byte[] shaMBytes = mdSHA1_.digest();
        byte[] mdMBytes = mdMD5_.digest();
        byte[] plainSig = new byte[16 + 20];
        System.arraycopy(mdMBytes, 0, plainSig, 0, 16);
        System.arraycopy(shaMBytes, 0, plainSig, 16, 20);
        try {
            out = cipher_.doFinal(plainSig);
            return out;
        } catch (IllegalBlockSizeException ibse) {
            System.err.println("RSASignature: cipher.doFinal");
            ibse.printStackTrace();
        } catch (BadPaddingException bpe) {
            System.err.println("RSASignature: cipher.doFinal");
            bpe.printStackTrace();
        }
        return null;
    }

    /**
    * Signs the message and stores the signature in the provided buffer.
    * <p>
    * @param outbuffer buffer for the signature result.
    * @param offset offset into outbuffer where the signature is stored.
    * @param length number of bytes within outbuffer allotted for the signature.
    * @return the number of bytes placed into outbuffer, if length >= signature.length.
    * If length < signature.length then outbuffer is leaved unchanged and 0 is returned.
    * @throws SignatureException if the signature is not initialized properly.
    */
    protected int engineSign(byte[] outbuffer, int offset, int length) throws SignatureException {
        byte[] out = engineSign();
        if (out.length <= length) {
            System.arraycopy(out, 0, outbuffer, offset, out.length);
            return out.length;
        } else {
            return 0;
        }
    }

    /**
    * Passes message bytes to the message digest.
    * <p>
    * @param b The message byte.
    * @param offset The index, where the message bytes starts.
    * @param length The number of message bytes.
    * @throws SignatureException if the signature is not initialized properly.
    */
    protected void engineUpdate(byte[] b, int offset, int length) throws SignatureException {
        mdMD5_.update(b, offset, length);
        mdSHA1_.update(b, offset, length);
    }

    /**
    * Passes a message byte to the message digest.
    * <p>
    * @param b the message byte.
    * @throws SignatureException if the signature is not initialized properly.
    */
    protected void engineUpdate(byte b) throws SignatureException {
        mdMD5_.update(b);
        mdSHA1_.update(b);
    }

    /**
    * Verifies a signature.
    * <p>
    * @param signature the signature to be verified.
    * @return true if the signature is correct - false otherwise.
    */
    protected boolean engineVerify(byte[] signature) {
        byte[] shaMBytes = mdSHA1_.digest();
        byte[] mdMBytes = mdMD5_.digest();
        byte[] plain;
        try {
            plain = cipher_.doFinal(signature);
            for (int i = 0; i < 16; i++) {
                if (plain[i] != mdMBytes[i]) return false;
                if (plain[16 + i] != shaMBytes[i]) return false;
            }
            for (int i = 0; i < 4; i++) {
                if (plain[16 + 16 + i] != shaMBytes[16 + i]) return false;
            }
            return true;
        } catch (IllegalBlockSizeException ibse) {
            System.err.println("RSASignature: cipher.doFinal");
            ibse.printStackTrace();
        } catch (BadPaddingException bpe) {
            System.err.println("RSASignature: cipher.doFinal");
            bpe.printStackTrace();
        }
        return false;
    }
}
