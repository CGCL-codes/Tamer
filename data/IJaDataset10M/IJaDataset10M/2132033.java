package com.controltier.ctl.utils;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Enumeration;
import java.util.Vector;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

/**
 * JARVerifier verifies signed JAR files given a list of trusted CA certificates. See <a
 * href="http://java.sun.com/products/jce/doc/guide/HowToImplAProvider.html#MutualAuth">http://java.sun.com/products/jce/doc/guide/HowToImplAProvider.html#MutualAuth</a>
 *
 * @author Greg Schueler <a href="mailto:greg@controltier.com">greg@controltier.com</a>
 * @version $Revision: 1079 $
 */
public final class JARVerifier {

    private X509Certificate[] trustedCaCerts;

    /**
     * Create a JAR verifier with an array of trusted certificate authority certificates.
     *
     * @param trustedCaCerts
     */
    public JARVerifier(X509Certificate[] trustedCaCerts) {
        this.trustedCaCerts = trustedCaCerts;
    }

    /**
     * Construct a JARVerifier with a keystore and alias and password.
     *
     * @param keystore filepath to the keystore
     * @param alias    alias name of the cert chain to verify with
     * @param passwd   password to use to verify the keystore, or null
     * @return
     * @throws IOException
     * @throws KeyStoreException
     * @throws NoSuchAlgorithmException
     * @throws CertificateException
     */
    public static JARVerifier create(String keystore, String alias, char[] passwd) throws IOException, KeyStoreException, NoSuchAlgorithmException, CertificateException {
        FileInputStream fileIn = new FileInputStream(keystore);
        KeyStore keyStore = KeyStore.getInstance("JKS");
        keyStore.load(fileIn, passwd);
        Certificate[] chain = keyStore.getCertificateChain(alias);
        if (chain == null) {
            Certificate cert = keyStore.getCertificate(alias);
            if (cert == null) {
                throw new IllegalArgumentException("No trusted certificate or chain found for alias: " + alias);
            }
            chain = new Certificate[] { cert };
        }
        X509Certificate certChain[] = new X509Certificate[chain.length];
        fileIn.close();
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        for (int count = 0; count < chain.length; count++) {
            ByteArrayInputStream certIn = new ByteArrayInputStream(chain[count].getEncoded());
            X509Certificate cert = (X509Certificate) cf.generateCertificate(certIn);
            certChain[count] = cert;
        }
        JARVerifier jarVerifier = new JARVerifier(certChain);
        return jarVerifier;
    }

    /**
     * An Exception thrown during verification.
     */
    public final class VerifierException extends Exception {

        public VerifierException(Throwable throwable) {
            super(throwable);
        }

        public VerifierException(String s, Throwable throwable) {
            super(s, throwable);
        }

        public VerifierException(String s) {
            super(s);
        }

        public VerifierException() {
            super();
        }
    }

    /**
     * Verify the JAR file signatures with the trusted CA certificates.
     *
     * @param jf
     * @throws IOException
     * @throws CertificateException
     * @throws VerifierException    If the jar file cannot be verified.
     */
    public final void verifySingleJarFile(JarFile jf) throws IOException, CertificateException, VerifierException {
        Vector entriesVec = new Vector();
        Manifest man = jf.getManifest();
        if (man == null) {
            throw new VerifierException("The JAR is not signed");
        }
        byte[] buffer = new byte[8192];
        Enumeration entries = jf.entries();
        while (entries.hasMoreElements()) {
            JarEntry je = (JarEntry) entries.nextElement();
            entriesVec.addElement(je);
            InputStream is = jf.getInputStream(je);
            int n;
            while ((n = is.read(buffer, 0, buffer.length)) != -1) {
            }
            is.close();
        }
        jf.close();
        Enumeration e = entriesVec.elements();
        while (e.hasMoreElements()) {
            JarEntry je = (JarEntry) e.nextElement();
            if (je.isDirectory()) {
                continue;
            }
            Certificate[] certs = je.getCertificates();
            if ((certs == null) || (certs.length == 0)) {
                if (!je.getName().startsWith("META-INF")) {
                    throw new VerifierException("The JAR file has unsigned files.");
                }
            } else {
                Certificate[] chainRoots = getChainRoots(certs);
                boolean signedAsExpected = false;
                for (int i = 0; i < chainRoots.length; i++) {
                    if (isTrusted((X509Certificate) chainRoots[i], trustedCaCerts)) {
                        signedAsExpected = true;
                        break;
                    }
                }
                if (!signedAsExpected) {
                    throw new VerifierException("The JAR file is not signed by a trusted signer");
                }
            }
        }
    }

    private static boolean isTrusted(X509Certificate cert, X509Certificate[] trustedCaCerts) {
        for (int i = 0; i < trustedCaCerts.length; i++) {
            if (cert.getSubjectDN().equals(trustedCaCerts[i].getSubjectDN())) {
                if (cert.equals(trustedCaCerts[i])) {
                    return true;
                }
            }
        }
        for (int i = 0; i < trustedCaCerts.length; i++) {
            if (cert.getIssuerDN().equals(trustedCaCerts[i].getSubjectDN())) {
                try {
                    cert.verify(trustedCaCerts[i].getPublicKey());
                    return true;
                } catch (Exception e) {
                }
            }
        }
        return false;
    }

    private static Certificate[] getChainRoots(Certificate[] certs) {
        Vector result = new Vector(3);
        for (int i = 0; i < certs.length - 1; i++) {
            if (!((X509Certificate) certs[i + 1]).getSubjectDN().equals(((X509Certificate) certs[i]).getIssuerDN())) {
                result.addElement(certs[i]);
            }
        }
        result.addElement(certs[certs.length - 1]);
        Certificate[] ret = new Certificate[result.size()];
        result.copyInto(ret);
        return ret;
    }
}
