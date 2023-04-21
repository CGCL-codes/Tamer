package org.isodl.util;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import org.ejbca.cvc.CVCObject;
import org.ejbca.cvc.CVCertificate;
import org.ejbca.cvc.CertificateParser;

/**
 * Some general file utilities for the driving license implementation.
 * 
 * @author Wojciech Mostowski <woj@cs.ru.nl>
 * 
 */
public class Files {

    /**
     * Reads an RSA private key from a PKCS8 encoded file.
     * 
     * @param file
     *            the file with the key
     * @return the RSAPrivateKey object, null if there are problems.
     */
    public static RSAPrivateKey readRSAPrivateKeyFromFile(File file) {
        try {
            byte[] key = loadFile(file);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            PKCS8EncodedKeySpec keysp = new PKCS8EncodedKeySpec(key);
            PrivateKey privK = kf.generatePrivate(keysp);
            return (RSAPrivateKey) privK;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Reads an RSA public key from a X509 encoded file.
     * 
     * @param file
     *            the file with the key
     * @return the RSAPublicKey object, null if there are problems.
     */
    public static RSAPublicKey readRSAPublicKeyFromFile(File file) {
        try {
            byte[] key = loadFile(file);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec keysp = new X509EncodedKeySpec(key);
            PublicKey pubK = kf.generatePublic(keysp);
            return (RSAPublicKey) pubK;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Reads a certificate from a X509 encoded file.
     * 
     * @param file
     *            the file with the certificate
     * @return the X509Certificate object, null if there are problems.
     */
    public static X509Certificate readCertFromFile(File file) {
        try {
            byte[] data = loadFile(file);
            ByteArrayInputStream certstream = new ByteArrayInputStream(data);
            CertificateFactory cf = CertificateFactory.getInstance("X509");
            Certificate c = cf.generateCertificate(certstream);
            return (X509Certificate) c;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Reads a card verifiable certificate from a BERTLV encoded file, see
     * ISO10813-3 for the format.
     * 
     * @param file
     *            the file with the certificate
     * @return the CVCertificate object, null if there are problems.
     */
    public static CVCertificate readCVCertificateFromFile(File f) {
        try {
            byte[] data = loadFile(f);
            CVCObject parsedObject = CertificateParser.parseCertificate(data);
            CVCertificate c = (CVCertificate) parsedObject;
            return c;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Reads the byte data from a file.
     * 
     * @param path
     *            the path to the file
     * @return the raw contents of the file
     * @throws IOException
     *             if there are problems
     */
    public static byte[] loadFile(String path) throws IOException {
        return loadFile(new File(path));
    }

    /**
     * Reads the byte data from a file.
     * 
     * @param file
     *            the file object to read data from
     * @return the raw contents of the file
     * @throws IOException
     *             if there are problems
     */
    public static byte[] loadFile(File file) throws IOException {
        byte[] dataBuffer = null;
        FileInputStream inStream = null;
        try {
            int length = (int) file.length();
            dataBuffer = new byte[length];
            inStream = new FileInputStream(file);
            int offset = 0;
            int readBytes = 0;
            boolean readMore = true;
            while (readMore) {
                readBytes = inStream.read(dataBuffer, offset, length - offset);
                offset += readBytes;
                readMore = readBytes > 0 && offset != length;
            }
        } finally {
            try {
                if (inStream != null) inStream.close();
            } catch (IOException e1) {
                System.out.println("loadFile - error when closing: " + e1);
            }
        }
        return dataBuffer;
    }

    /**
     * Writes raw data to a file. NOTE: overwrites existing files without asking.
     * 
     * @param path
     *            path to the file to be written (no overwrite checks!)
     * @param data
     *            raw data to be written
     * @throws IOException
     *             if something goes wrong
     */
    public static void writeFile(String path, byte[] data) throws IOException {
        writeFile(new File(path), data);
    }

    /**
     * Writes raw data to a file. NOTE: overwrites existing files without asking.
     * 
     * @param file
     *            the file object to be written (no overwrite checks!)
     * @param data
     *            raw data to be written
     * @throws IOException
     *             if something goes wrong
     */
    public static void writeFile(File file, byte[] data) throws IOException {
        FileOutputStream outStream = null;
        BufferedOutputStream bout = null;
        try {
            outStream = new FileOutputStream(file);
            bout = new BufferedOutputStream(outStream, 1000);
            bout.write(data);
        } finally {
            if (bout != null) bout.close();
        }
    }
}
