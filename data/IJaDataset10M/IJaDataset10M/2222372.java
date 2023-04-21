package com.hanhuy.scurp.server;

import java.io.ByteArrayInputStream;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.PublicKey;
import java.security.Security;
import java.security.SignatureException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Date;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.security.auth.x500.X500Principal;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.KeyUsage;
import org.bouncycastle.asn1.x509.X509Extensions;
import org.bouncycastle.x509.X509V3CertificateGenerator;
import org.bouncycastle.x509.extension.AuthorityKeyIdentifierStructure;
import org.bouncycastle.x509.extension.SubjectKeyIdentifierStructure;

public class CertificateGenerator {

    public static final long CA_VALIDITY = 10L * 365 * 24 * 60 * 60 * 1000;

    public static final long CERT_VALIDITY = CA_VALIDITY;

    public static final String CERT_SIGNATURE_ALGORITHM = "SHA1WithRSAEncryption";

    private static final boolean hasBCP;

    static {
        boolean found = false;
        try {
            Class<?> c = Class.forName("org.bouncycastle.jce.provider.BouncyCastleProvider");
            Security.addProvider((Provider) c.newInstance());
            found = true;
        } catch (ClassNotFoundException e) {
        } catch (InstantiationException e) {
        } catch (IllegalAccessException e) {
        }
        hasBCP = found;
    }

    public static boolean isEnabled() {
        return hasBCP;
    }

    /**
     * Check if the JCE Unlimited Strength Jurisdiction policy is installed.
     * Perform this check by trying to initialize a 256bit AES cipher, strong
     * policy only allows a maximum of 128bit.
     */
    public static boolean isUnlimitedStrength() {
        try {
            KeyGenerator kg = KeyGenerator.getInstance("AES");
            kg.init(256);
            SecretKey k = kg.generateKey();
            Cipher c = Cipher.getInstance("AES");
            c.init(Cipher.ENCRYPT_MODE, k);
            return true;
        } catch (NoSuchAlgorithmException e) {
        } catch (InvalidKeyException e) {
        } catch (NoSuchPaddingException e) {
        }
        return false;
    }

    public static KeyPair generateKeyPair() {
        KeyPairGenerator kpg;
        try {
            kpg = KeyPairGenerator.getInstance("RSA", "BC");
            kpg.initialize(1024);
            return kpg.generateKeyPair();
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("Unable to initialize keypair generator", e);
        }
    }

    public static X509Certificate generateCACertificate(KeyPair kp, String name) throws CertificateParsingException, CertificateEncodingException, SignatureException, InvalidKeyException {
        X509V3CertificateGenerator cg = new X509V3CertificateGenerator();
        String dn = "CN=SCURP Certificate Authority: " + name + "," + "OU=Certificate Management," + "O=SCURP";
        cg.setSerialNumber(BigInteger.valueOf(1));
        cg.setIssuerDN(new X500Principal(dn));
        cg.setNotBefore(new Date());
        cg.setNotAfter(new Date(System.currentTimeMillis() + CA_VALIDITY));
        cg.setSubjectDN(new X500Principal(dn));
        cg.setPublicKey(kp.getPublic());
        cg.setSignatureAlgorithm(CERT_SIGNATURE_ALGORITHM);
        cg.addExtension(X509Extensions.SubjectKeyIdentifier, false, new SubjectKeyIdentifierStructure(kp.getPublic()));
        cg.addExtension(X509Extensions.BasicConstraints, true, new BasicConstraints(true));
        cg.addExtension(X509Extensions.KeyUsage, true, new KeyUsage(KeyUsage.digitalSignature | KeyUsage.keyCertSign));
        try {
            return cg.generate(kp.getPrivate());
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("RSA is not supported?!", e);
        }
    }

    public static X509Certificate generateCertificate(String cn, PublicKey entityKey, PrivateKey caKey, X509Certificate caCert) throws CertificateParsingException, CertificateEncodingException, InvalidKeyException, SignatureException {
        String dn = "CN=" + cn + "," + "OU=SCURP SSL Certificates," + "O=SCURP";
        X509V3CertificateGenerator cg = new X509V3CertificateGenerator();
        cg.setSerialNumber(BigInteger.valueOf(System.currentTimeMillis()));
        cg.setIssuerDN(caCert.getSubjectX500Principal());
        cg.setNotBefore(new Date());
        cg.setNotAfter(new Date(System.currentTimeMillis() + CERT_VALIDITY));
        cg.setSubjectDN(new X500Principal(dn));
        cg.setPublicKey(entityKey);
        cg.setSignatureAlgorithm(CERT_SIGNATURE_ALGORITHM);
        cg.addExtension(X509Extensions.AuthorityKeyIdentifier, false, new AuthorityKeyIdentifierStructure(caCert));
        cg.addExtension(X509Extensions.SubjectKeyIdentifier, false, new SubjectKeyIdentifierStructure(entityKey));
        cg.addExtension(X509Extensions.BasicConstraints, true, new BasicConstraints(false));
        cg.addExtension(X509Extensions.KeyUsage, true, new KeyUsage(KeyUsage.digitalSignature | KeyUsage.keyEncipherment));
        try {
            return cg.generate(caKey);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("RSA is not supported?!", e);
        }
    }

    public static X509Certificate loadCertificate(byte[] certificateBuf) throws CertificateException {
        try {
            CertificateFactory cf = CertificateFactory.getInstance("X.509", "BC");
            return (X509Certificate) cf.generateCertificate(new ByteArrayInputStream(certificateBuf));
        } catch (NoSuchProviderException e) {
            throw new IllegalStateException("BC provider not found", e);
        }
    }

    public static PrivateKey loadKey(byte[] keyBuf) throws InvalidKeySpecException {
        try {
            KeyFactory kf = KeyFactory.getInstance("RSA", "BC");
            PKCS8EncodedKeySpec privSpec = new PKCS8EncodedKeySpec(keyBuf);
            return kf.generatePrivate(privSpec);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("RSA not supported?!", e);
        } catch (NoSuchProviderException e) {
            throw new IllegalStateException("BC provider not found", e);
        }
    }
}
