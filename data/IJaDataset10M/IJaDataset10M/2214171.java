package org.infoeng.ofbiz.ltans.util;

import org.bouncycastle.asn1.*;
import org.bouncycastle.asn1.cms.*;
import org.bouncycastle.asn1.cms.ContentInfo;
import org.bouncycastle.asn1.cms.IssuerAndSerialNumber;
import org.bouncycastle.asn1.cms.SignerInfo;
import org.bouncycastle.asn1.cms.SignedData;
import org.bouncycastle.asn1.nist.*;
import org.bouncycastle.asn1.pkcs.*;
import org.bouncycastle.asn1.x509.*;
import org.bouncycastle.asn1.x509.Time;
import org.bouncycastle.asn1.x509.X509Extension;
import org.bouncycastle.asn1.x509.X509Extensions;
import org.bouncycastle.jce.provider.*;
import org.bouncycastle.util.encoders.*;
import org.bouncycastle.openssl.*;
import java.math.BigInteger;
import java.io.*;
import java.security.*;
import java.util.*;
import org.infoeng.ofbiz.ltans.afm.*;
import org.infoeng.ofbiz.ltans.afm.req.*;
import org.infoeng.ofbiz.ltans.ers.*;
import org.infoeng.ofbiz.ltans.ltap.*;
import org.infoeng.ofbiz.ltans.scep.*;
import org.infoeng.ofbiz.ltans.LTANSObject;
import javax.crypto.*;
import java.security.*;
import java.security.cert.*;
import java.security.spec.*;
import java.security.interfaces.*;

public class LtansUtils implements PasswordFinder {

    private static final int classVersion = 0;

    public static final DERObjectIdentifier defaultSignatureOID = PKCSObjectIdentifiers.sha256WithRSAEncryption;

    public static final DERObjectIdentifier defaultDigestOID = NISTObjectIdentifiers.id_sha256;

    public static final String SIGNATURE_ALGORITHM = "SHA256withRSA";

    public static final String DIGEST_ALGORITHM = "SHA-256";

    public static final String SYMMETRIC_ALGORITHM = "AES";

    public static final String PUBLIC_KEY_ALGORITHM = "RSA";

    public static final String LTANS_AFM_FILE = "ltans-afm.properties";

    private static HashMap algMap;

    private static HashMap javaAlgMap;

    private static HashMap digestAlgMap;

    private static HashMap digestJavaAlgMap;

    static {
        algMap = new HashMap();
        javaAlgMap = new HashMap();
        digestAlgMap = new HashMap();
        digestJavaAlgMap = new HashMap();
        digestAlgMap.put("default", NISTObjectIdentifiers.id_sha256);
        digestAlgMap.put("MD5", PKCSObjectIdentifiers.md5);
        digestAlgMap.put("SHA-1", X509ObjectIdentifiers.id_SHA1);
        digestAlgMap.put("SHA-256", NISTObjectIdentifiers.id_sha256);
        digestAlgMap.put("SHA-384", NISTObjectIdentifiers.id_sha384);
        digestAlgMap.put("SHA-512", NISTObjectIdentifiers.id_sha512);
        digestJavaAlgMap.put("default", "SHA-256");
        digestJavaAlgMap.put(PKCSObjectIdentifiers.md5, "MD5");
        digestJavaAlgMap.put(X509ObjectIdentifiers.id_SHA1, "SHA-1");
        digestJavaAlgMap.put(NISTObjectIdentifiers.id_sha256, "SHA-256");
        digestJavaAlgMap.put(NISTObjectIdentifiers.id_sha384, "SHA-384");
        digestJavaAlgMap.put(NISTObjectIdentifiers.id_sha512, "SHA-512");
        algMap.put("default", PKCSObjectIdentifiers.sha256WithRSAEncryption);
        algMap.put("AES", NISTObjectIdentifiers.aes);
        algMap.put("RSA", PKCSObjectIdentifiers.sha256WithRSAEncryption);
        algMap.put("MD5withRSA", PKCSObjectIdentifiers.md5WithRSAEncryption);
        algMap.put("SHA1withRSA", PKCSObjectIdentifiers.sha1WithRSAEncryption);
        algMap.put("SHA256withRSA", PKCSObjectIdentifiers.sha256WithRSAEncryption);
        algMap.put("SHA384withRSA", PKCSObjectIdentifiers.sha384WithRSAEncryption);
        algMap.put("SHA512withRSA", PKCSObjectIdentifiers.sha512WithRSAEncryption);
        javaAlgMap.put("default", "SHA256withRSA");
        javaAlgMap.put("RSA", "SHA256withRSA");
        javaAlgMap.put(NISTObjectIdentifiers.aes, "AES");
        javaAlgMap.put(PKCSObjectIdentifiers.md5WithRSAEncryption, "MD5withRSA");
        javaAlgMap.put(PKCSObjectIdentifiers.sha1WithRSAEncryption, "SHA1withRSA");
        javaAlgMap.put(PKCSObjectIdentifiers.sha256WithRSAEncryption, "SHA256withRSA");
        javaAlgMap.put(PKCSObjectIdentifiers.sha384WithRSAEncryption, "SHA384withRSA");
        javaAlgMap.put(PKCSObjectIdentifiers.sha512WithRSAEncryption, "SHA512withRSA");
    }

    public static PolicyInformation getServicePolicyInformation(X509Certificate serviceCert) throws Exception {
        byte[] tbsCertBytes = serviceCert.getTBSCertificate();
        TBSCertificateStructure tbsCert = new TBSCertificateStructure(ASN1Sequence.getInstance(new ASN1InputStream(tbsCertBytes).readObject()));
        X509Extensions x509Exts = tbsCert.getExtensions();
        X509Extension certPolicyInfosExt = x509Exts.getExtension(X509Extensions.CertificatePolicies);
        if (certPolicyInfosExt == null) return null;
        ASN1Sequence policySetSeq = ASN1Sequence.getInstance(X509Extension.convertValueToObject(certPolicyInfosExt));
        if (policySetSeq != null && policySetSeq.size() == 1) {
            ASN1Sequence policySeq = ASN1Sequence.getInstance(policySetSeq.getObjectAt(0));
            if ((policySeq != null) && ((policySeq.size() == 1) || (policySeq.size() == 2))) return PolicyInformation.getInstance(policySeq);
        }
        return null;
    }

    public static Properties getDefaultProperties() throws Exception {
        Properties keysProps = new Properties();
        InputStream afmIS = Thread.currentThread().getContextClassLoader().getResourceAsStream(LtansUtils.LTANS_AFM_FILE);
        keysProps.load(afmIS);
        return keysProps;
    }

    public static KeyStore getDefaultKeyStore() throws Exception {
        Properties keysProps = getDefaultProperties();
        KeyStore scepKeyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        InputStream scepIS = Thread.currentThread().getContextClassLoader().getResourceAsStream((String) keysProps.getProperty("ltans.keystore.file"));
        scepKeyStore.load(scepIS, ((String) keysProps.getProperty("ltans.keystore.password")).toCharArray());
        return scepKeyStore;
    }

    public static X509Certificate getDefaultCertificate() throws Exception {
        Properties keysProps = getDefaultProperties();
        KeyStore scepKeyStore = getDefaultKeyStore();
        X509Certificate scepCert = (X509Certificate) scepKeyStore.getCertificate(((String) keysProps.getProperty("ltans.keystore.scepKeyAlias")));
        return scepCert;
    }

    public static KeyPair getDefaultKeyPair() throws Exception {
        Properties keysProps = getDefaultProperties();
        KeyStore scepKeyStore = getDefaultKeyStore();
        RSAPrivateCrtKey scepPrivateKey = (RSAPrivateCrtKey) scepKeyStore.getKey(((String) keysProps.getProperty("ltans.keystore.scepKeyAlias")), ((String) keysProps.getProperty("ltans.keystore.scepPassword")).toCharArray());
        RSAPublicKeySpec rsaPubKeySpec = new RSAPublicKeySpec(scepPrivateKey.getModulus(), scepPrivateKey.getPublicExponent());
        KeyFactory rsaPubKeyFactory = KeyFactory.getInstance("RSA");
        RSAPublicKey scepPublicKey = (RSAPublicKey) rsaPubKeyFactory.generatePublic(rsaPubKeySpec);
        return new KeyPair(scepPublicKey, scepPrivateKey);
    }

    public static ArchiveFiduciaryMediaSet[] newAFMSet(ArchiveFiduciaryMediaRequest afmReq, ArchiveFiduciaryMediaIssuancePolicy issuancePolicy, GeneralNames afmSeriesId, DERInteger serialNumber, X509CertificateObject caCert, KeyPair caKeyPair, KeyPair seriesKP) throws Exception {
        MessageDigest sha256 = MessageDigest.getInstance(LtansUtils.DIGEST_ALGORITHM);
        Random rnd = new Random();
        ArrayList digestList = new ArrayList<byte[]>();
        ArrayList afmuList = new ArrayList<ArchiveFiduciaryMediaUnit>();
        long currenttime = System.currentTimeMillis();
        BigInteger duration = issuancePolicy.getDuration();
        Time sTime = new Time(new Date(currenttime));
        Time eTime = new Time(new Date(currenttime + (duration.intValue() * 1000)));
        int certNum = issuancePolicy.getCertificateNumber().intValue();
        ArchiveFiduciaryMediaUnitTemplate[] afmUnitTemplateArray = new ArchiveFiduciaryMediaUnitTemplate[certNum];
        for (int x = 0; x < certNum; x++) {
            BigInteger issuerBigInt = new BigInteger(64, rnd);
            BigInteger subjectBigInt = new BigInteger(64, rnd);
            ArchiveFiduciaryMediaUnitTemplate afmUnitTemplate = new ArchiveFiduciaryMediaUnitTemplate(new DERInteger(x + 1), new X509Name(caCert.getIssuerDN().toString()), afmSeriesId, sTime, eTime, new DERBitString(issuerBigInt.toByteArray()), new DERBitString(subjectBigInt.toByteArray()), null, seriesKP.getPublic());
            afmUnitTemplateArray[x] = afmUnitTemplate;
            byte[] encAfmIssuerBytes = afmUnitTemplate.getIssuerDataBytes();
            byte[] digestBytes = sha256.digest(encAfmIssuerBytes);
            digestList.add(digestBytes);
        }
        Signature treeSig = Signature.getInstance(LtansUtils.SIGNATURE_ALGORITHM);
        treeSig.initSign(seriesKP.getPrivate());
        MerkleTree mt = MerkleTree.buildTree(digestList);
        int treeHeight = mt.getTreeHeight();
        byte[] rootDigest = mt.getRootDigest();
        treeSig.update(rootDigest);
        byte[] digestSig = treeSig.sign();
        int afmuNum = afmuList.size();
        DERBitString issuerTreeSigValue = new DERBitString(digestSig);
        int q = 0;
        while (Math.pow(2, q) < digestList.size()) {
            q++;
        }
        AlgorithmIdentifier issuerTreeSigAlg = new AlgorithmIdentifier(new DERObjectIdentifier(AFMObjectIdentifiers.sha256_rsa_tree_signature_oid + "." + q + ""));
        ArchiveFiduciaryMediaUnit[] distSet = new ArchiveFiduciaryMediaUnit[certNum];
        for (int x = 0; x < certNum; x++) {
            distSet[x] = new ArchiveFiduciaryMediaUnit(afmUnitTemplateArray[x], issuerTreeSigAlg, issuerTreeSigValue, seriesKP);
        }
        ASN1EncodableVector vect = new ASN1EncodableVector();
        vect.add(new DERInteger(0));
        vect.add(serialNumber);
        vect.add(new X509Name(caCert.getIssuerDN().toString()));
        ASN1EncodableVector tmpSeq = new ASN1EncodableVector();
        tmpSeq.add(sTime);
        tmpSeq.add(eTime);
        vect.add(new DERSequence(tmpSeq));
        vect.add(afmSeriesId);
        vect.add(new SubjectPublicKeyInfo(new AlgorithmIdentifier(PKCSObjectIdentifiers.rsaEncryption), caKeyPair.getPublic().getEncoded()));
        ASN1EncodableVector treeSigSeqV = new ASN1EncodableVector();
        ASN1EncodableVector digestListV = new ASN1EncodableVector();
        for (int w = 0; w < digestList.size(); w++) {
            digestListV.add(new DERBitString((byte[]) digestList.get(w)));
        }
        treeSigSeqV.add(new DERInteger(0));
        treeSigSeqV.add(new DERSequence(digestListV));
        treeSigSeqV.add(issuerTreeSigAlg);
        treeSigSeqV.add(issuerTreeSigValue);
        ASN1EncodableVector vOne = new ASN1EncodableVector();
        vOne.add(new DERObjectIdentifier(AFMObjectIdentifiers.id_afm_series_descriptor));
        vOne.add(new DERBoolean(true));
        DEROctetString treeSigStr = new DEROctetString(LtansUtils.getDERObjectBytes(new DERSequence(treeSigSeqV)));
        vOne.add(treeSigStr);
        ASN1EncodableVector vtwo = new ASN1EncodableVector();
        vtwo.add(new DERSequence(vOne));
        X509Extensions exts = X509Extensions.getInstance(new DERSequence(vtwo));
        vect.add(exts);
        TBSCertificateStructure tbsCert = new TBSCertificateStructure(new DERSequence(vect));
        byte[] tbsCertBytes = LtansUtils.getDERObjectBytes(tbsCert);
        Signature certSig = Signature.getInstance(LtansUtils.SIGNATURE_ALGORITHM);
        certSig.initSign(seriesKP.getPrivate());
        certSig.update(tbsCertBytes);
        byte[] tbsSigBytes = certSig.sign();
        DERBitString derBitStr = new DERBitString(tbsSigBytes);
        ASN1EncodableVector vTBSVect = new ASN1EncodableVector();
        vTBSVect.add(tbsCert);
        vTBSVect.add(new AlgorithmIdentifier(LtansUtils.defaultSignatureOID));
        vTBSVect.add(derBitStr);
        X509CertificateStructure x509CertStr = new X509CertificateStructure(new DERSequence(vTBSVect));
        ArchiveFiduciaryMediaSet releasedAfmSet = new ArchiveFiduciaryMediaSet(distSet, new DERSet(x509CertStr), seriesKP);
        ArchiveFiduciaryMediaSet[] afmSet = new ArchiveFiduciaryMediaSet[1];
        afmSet[0] = releasedAfmSet;
        return afmSet;
    }

    public static X509Extensions getX509Extensions(DERObjectIdentifier extnID, DERBoolean critical, DEROctetString extnValue) {
        ASN1EncodableVector v = new ASN1EncodableVector();
        v.add(extnID);
        v.add(critical);
        v.add(extnValue);
        ASN1EncodableVector vTwo = new ASN1EncodableVector();
        vTwo.add(new DERSequence(v));
        return X509Extensions.getInstance(new DERSequence(vTwo));
    }

    public static AlgorithmIdentifier getSignatureAlgorithmIdentifier(String inAlgId) throws Exception {
        Object algObj = algMap.get(inAlgId);
        if (algObj instanceof DERObjectIdentifier) {
            return new AlgorithmIdentifier((DERObjectIdentifier) algObj);
        } else return null;
    }

    public static AlgorithmIdentifier getAlgorithmIdentifier(String inAlgId) throws Exception {
        if (inAlgId == null) throw new IllegalArgumentException();
        Object algObj = digestAlgMap.get(inAlgId);
        if (algObj != null) {
            return new AlgorithmIdentifier((DERObjectIdentifier) algObj);
        } else {
            algObj = algMap.get(inAlgId);
            if (algObj != null) {
                return new AlgorithmIdentifier((DERObjectIdentifier) algObj);
            }
        }
        return null;
    }

    public static String getJavaAlgorithmIdentifier(AlgorithmIdentifier algIdIn) {
        if (algIdIn == null) throw new IllegalArgumentException();
        String algObj = (String) digestJavaAlgMap.get(algIdIn);
        if (algObj != null) {
            return algObj;
        } else {
            algObj = (String) javaAlgMap.get(algIdIn);
            if (algObj != null) {
                return algObj;
            }
        }
        return null;
    }

    public static String getJavaAlgorithmIdentifier(String algIdIn) {
        if (algIdIn == null) throw new IllegalArgumentException();
        String algObj = (String) digestJavaAlgMap.get(algIdIn);
        if (algObj != null) {
            return algObj;
        } else {
            algObj = (String) javaAlgMap.get(algIdIn);
            if (algObj != null) {
                return algObj;
            }
        }
        return null;
    }

    public static byte[] getDERObjectBytes(DEREncodable derObj) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ASN1OutputStream asnOut = new ASN1OutputStream(baos);
            asnOut.writeObject(derObj);
            byte[] derObjBytes = baos.toByteArray();
            return derObjBytes;
        } catch (Exception e) {
            return null;
        }
    }

    public char[] getPassword(String promptString) {
        try {
            char[] passwordChars = PasswordField.getPassword(System.in, promptString);
            return passwordChars;
        } catch (Exception e) {
            return null;
        }
    }

    public char[] getPassword() {
        try {
            String promptString = "Please enter the password:  ";
            char[] passwordChars = PasswordField.getPassword(System.in, promptString);
            return passwordChars;
        } catch (Exception e) {
            return null;
        }
    }

    public static char[] staticGetPassword() throws Exception {
        return staticGetPassword("Please enter the password:");
    }

    public static char[] staticGetPassword(String promptString) throws Exception {
        char[] passwordChars = PasswordField.getPassword(System.in, promptString);
        return passwordChars;
    }

    public static byte[] duplicateByteArray(byte[] byteArray) {
        if (byteArray == null) return null;
        byte[] retBytes = new byte[byteArray.length];
        for (int x = 0; x < byteArray.length; x++) {
            retBytes[x] = byteArray[x];
        }
        return retBytes;
    }
}

class MaskingThread extends Thread {

    private volatile boolean stop;

    private char echochar = '*';

    /**
     * @param prompt The prompt displayed to the user
     */
    public MaskingThread(String prompt) {
        System.out.print(prompt);
    }

    /**
     * Begin masking until asked to stop.
     */
    public void run() {
        Thread currentT = Thread.currentThread();
        int priority = currentT.getPriority();
        currentT.setPriority(Thread.MAX_PRIORITY);
        try {
            stop = true;
            while (stop) {
                System.out.print("\010" + echochar);
                try {
                    Thread.sleep(1);
                } catch (InterruptedException iex) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        } finally {
            Thread.currentThread().setPriority(priority);
        }
    }

    /**
     * Instruct the thread to stop masking.
     */
    public void stopMasking() {
        this.stop = false;
    }
}

class PasswordField {

    /**
     *@param input stream to be used (e.g. System.in)
     *@param prompt The prompt to display to the user.
     *@return The password as entered by the user.
     */
    public static final char[] getPassword(InputStream in, String prompt) throws IOException {
        MaskingThread maskingthread = new MaskingThread(prompt);
        Thread thread = new Thread(maskingthread);
        thread.start();
        char[] lineBuffer;
        char[] buf;
        int i;
        buf = lineBuffer = new char[128];
        int room = buf.length;
        int offset = 0;
        int c;
        loop: while (true) {
            switch(c = in.read()) {
                case -1:
                case '\n':
                    break loop;
                case '\r':
                    int c2 = in.read();
                    if ((c2 != '\n') && (c2 != -1)) {
                        if (!(in instanceof PushbackInputStream)) {
                            in = new PushbackInputStream(in);
                        }
                        ((PushbackInputStream) in).unread(c2);
                    } else {
                        break loop;
                    }
                default:
                    if (--room < 0) {
                        buf = new char[offset + 128];
                        room = buf.length - offset - 1;
                        System.arraycopy(lineBuffer, 0, buf, 0, offset);
                        Arrays.fill(lineBuffer, ' ');
                        lineBuffer = buf;
                    }
                    buf[offset++] = (char) c;
                    break;
            }
        }
        maskingthread.stopMasking();
        if (offset == 0) {
            return null;
        }
        char[] ret = new char[offset];
        System.arraycopy(buf, 0, ret, 0, offset);
        Arrays.fill(buf, ' ');
        return ret;
    }
}
