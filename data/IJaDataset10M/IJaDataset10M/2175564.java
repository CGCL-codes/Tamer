package br.ufmg.lcc.pcollecta.ws.certs;

import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.SignatureException;
import java.security.cert.X509Certificate;
import java.util.Date;
import org.bouncycastle.asn1.ASN1Set;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.PolicyInformation;
import org.bouncycastle.asn1.x509.X509Extensions;
import org.bouncycastle.asn1.x509.X509Name;
import org.bouncycastle.jce.PKCS10CertificationRequest;
import org.bouncycastle.x509.X509V3CertificateGenerator;
import br.ufmg.lcc.arangi.commons.StringHelper;
import com.mindprod.base64.Base64;

public class CertHandler {

    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {
    }

    /**
     * Gera um certificado auto assinado e key privada
     * @return Posi��o 0: certificado, Posi��o 1: key privada
     * @throws Exception
     */
    public static Object[] generateGenericCertificateSelfSigned(String dnName, int validity) throws Exception {
        Provider myProvider = new org.bouncycastle.jce.provider.BouncyCastleProvider();
        Security.addProvider(myProvider);
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        SecureRandom rand = new SecureRandom();
        kpg.initialize(1024, rand);
        KeyPair keyPair = kpg.generateKeyPair();
        PrivateKey privateKey = keyPair.getPrivate();
        PublicKey publicKey = keyPair.getPublic();
        return generateCertificate(dnName, dnName, validity, privateKey, publicKey);
    }

    /**
     * Gera certificado auto-assinado
     * @param dn Distinguished Name do certificado a ser assinado, no formato C=BR, xxx, CN=common name
     * @param validity Validade, em dias
     * @param policyId Pode ser null
     * @param privKey Chave privada associada com o certificado
     * @param pubKey Chave p�blica 
     * @param isCA
     * @param caDn
     * @param caPrivateKey
     * @param acPubKey
     * @return
     * @throws NoSuchAlgorithmException
     * @throws SignatureException
     * @throws InvalidKeyException
     */
    private static Object[] genCert(String dn, long validity, String policyId, PrivateKey privKey, PublicKey pubKey, boolean isCA, String caDn, PrivateKey caPrivateKey, PublicKey acPubKey) throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        String sigAlg = "SHA1WithRSA";
        Date firstDate = new Date();
        firstDate.setTime(firstDate.getTime() - (10 * 60 * 1000));
        Date lastDate = new Date();
        lastDate.setTime(lastDate.getTime() + (validity * (24 * 60 * 60 * 1000)));
        X509V3CertificateGenerator certgen = new X509V3CertificateGenerator();
        byte[] serno = new byte[8];
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        random.setSeed((new Date().getTime()));
        random.nextBytes(serno);
        certgen.setSerialNumber((new java.math.BigInteger(serno)).abs());
        certgen.setNotBefore(firstDate);
        certgen.setNotAfter(lastDate);
        certgen.setSignatureAlgorithm(sigAlg);
        X509Name x509NameSubject = new X509Name(dn);
        X509Name x509NameIssuer = new X509Name(caDn);
        certgen.setSubjectDN(x509NameSubject);
        certgen.setIssuerDN(x509NameIssuer);
        certgen.setPublicKey(pubKey);
        BasicConstraints bc = new BasicConstraints(isCA);
        certgen.addExtension(X509Extensions.BasicConstraints.getId(), true, bc);
        if (policyId != null) {
            PolicyInformation pi = new PolicyInformation(new DERObjectIdentifier(policyId));
            DERSequence seq = new DERSequence(pi);
            certgen.addExtension(X509Extensions.CertificatePolicies.getId(), false, seq);
        }
        X509Certificate selfcert = certgen.generateX509Certificate(caPrivateKey);
        return new Object[] { selfcert, privKey };
    }

    /**
     * @param dnName
     * @param issuerName
     * @param validity
     * @param caPrivKey
     * @param caPubKey
     * @return
     * @throws Exception
     */
    private static Object[] generateCertificate(String dnName, String issuerName, int validity, PrivateKey caPrivKey, PublicKey caPubKey) throws Exception {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        SecureRandom rand = new SecureRandom();
        kpg.initialize(1024, rand);
        KeyPair keyPair = kpg.generateKeyPair();
        PrivateKey privateKey = keyPair.getPrivate();
        PublicKey publicKey = keyPair.getPublic();
        Object[] o = null;
        try {
            o = genCert(dnName, validity, null, privateKey, publicKey, false, issuerName, caPrivKey, caPubKey);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (SignatureException e) {
            e.printStackTrace();
        }
        return o;
    }

    public static String generateCSR(KeyStore keyStore, String alias, char[] password) throws Exception {
        X509Certificate cert = null;
        try {
            cert = (X509Certificate) keyStore.getCertificate(alias);
        } catch (KeyStoreException e) {
        }
        PrivateKey key = null;
        try {
            key = (PrivateKey) keyStore.getKey(alias, password);
        } catch (Exception e) {
        }
        String csr = null;
        try {
            csr = generateCSR(cert, key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return csr;
    }

    private static String generateCSR(X509Certificate cert, PrivateKey signingKey) throws Exception {
        String sigalg = cert.getSigAlgName();
        X509Name subject = new X509Name(cert.getSubjectDN().toString());
        PublicKey publicKey = cert.getPublicKey();
        ASN1Set attributes = null;
        PKCS10CertificationRequest csr = new PKCS10CertificationRequest(sigalg, subject, publicKey, attributes, signingKey);
        if (!csr.verify()) {
            throw new KeyStoreException("CSR verification failed");
        }
        Base64 b64 = new Base64();
        String strCsr = b64.encode(csr.getEncoded());
        final String BEGIN_CERT_REQ = "-----BEGIN CERTIFICATE REQUEST-----";
        final String END_CERT_REQ = "-----END CERTIFICATE REQUEST-----";
        StringBuffer sbuf = new StringBuffer(BEGIN_CERT_REQ).append('\n');
        sbuf.append(strCsr).append(StringHelper.BREAK_LINE);
        sbuf.append(END_CERT_REQ);
        return sbuf.toString();
    }
}
