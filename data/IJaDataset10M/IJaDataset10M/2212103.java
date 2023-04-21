package edu.vt.middleware.crypt.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.X509CRL;
import javax.crypto.SecretKey;
import edu.vt.middleware.crypt.CryptException;
import edu.vt.middleware.crypt.io.PrivateKeyCredentialReader;
import edu.vt.middleware.crypt.io.PublicKeyCredentialReader;
import edu.vt.middleware.crypt.io.SecretKeyCredentialReader;
import edu.vt.middleware.crypt.io.X509CRLCredentialReader;
import edu.vt.middleware.crypt.io.X509CertificateCredentialReader;
import edu.vt.middleware.crypt.io.X509CertificatesCredentialReader;

/**
 * Helper class for performing I/O read operations on cryptographic data.
 *
 * @author  Middleware Services
 * @version  $Revision: 578 $
 */
public class CryptReader {

    /** X.509 certificate type. */
    public static final String DEFAULT_CERTIFICATE_TYPE = "X.509";

    /** Protected constructor of utility class. */
    protected CryptReader() {
    }

    /**
   * Reads the raw bytes of a symmetric encryption key from a file.
   *
   * @param  keyFile  File containing key data.
   * @param  algorithm  Symmetric cipher algorithm for which key is used.
   *
   * @return  Secret key.
   *
   * @throws  CryptException  On cryptography errors such as invalid formats,
   * unsupported ciphers, illegal settings.
   * @throws  IOException  On IO errors.
   */
    public static SecretKey readSecretKey(final File keyFile, final String algorithm) throws CryptException, IOException {
        return new SecretKeyCredentialReader(algorithm).read(keyFile);
    }

    /**
   * Reads the raw bytes of a symmetric encryption key from an input stream.
   *
   * @param  keyStream  Stream containing key data.
   * @param  algorithm  Symmetric cipher algorithm for which key is used.
   *
   * @return  Secret key.
   *
   * @throws  CryptException  On cryptography errors such as invalid formats,
   * unsupported ciphers, illegal settings.
   * @throws  IOException  On IO errors.
   */
    public static SecretKey readSecretKey(final InputStream keyStream, final String algorithm) throws CryptException, IOException {
        return new SecretKeyCredentialReader(algorithm).read(keyStream);
    }

    /**
   * Reads the raw bytes of a symmetric encryption key from a byte array.
   *
   * @param  keyBytes  Byte array containing key data.
   * @param  algorithm  Symmetric cipher algorithm for which key is used.
   *
   * @return  Secret key.
   *
   * @throws  CryptException  On cryptography errors such as invalid formats,
   * unsupported ciphers, illegal settings.
   * @throws  IOException  On IO errors.
   */
    public static SecretKey readSecretKey(final byte[] keyBytes, final String algorithm) throws CryptException, IOException {
        return readSecretKey(new ByteArrayInputStream(keyBytes), algorithm);
    }

    /**
   * Reads a DER-encoded private key in PKCS#8 or OpenSSL "traditional" format
   * from a file into a {@link PrivateKey} object.
   *
   * @param  keyFile  Private key file.
   *
   * @return  Private key containing data read from file.
   *
   * @throws  CryptException  On key format errors.
   * @throws  IOException  On key read errors.
   */
    public static PrivateKey readPrivateKey(final File keyFile) throws CryptException, IOException {
        return new PrivateKeyCredentialReader().read(keyFile);
    }

    /**
   * Reads a DER-encoded private key in PKCS#8 or OpenSSL "traditional" format
   * from an input stream into a {@link PrivateKey} object.
   *
   * @param  keyStream  Input stream containing private key data.
   *
   * @return  Private key containing data read from stream.
   *
   * @throws  CryptException  On key format errors.
   * @throws  IOException  On key read errors.
   */
    public static PrivateKey readPrivateKey(final InputStream keyStream) throws CryptException, IOException {
        return new PrivateKeyCredentialReader().read(keyStream);
    }

    /**
   * Reads a DER-encoded private key in PKCS#8 or OpenSSL "traditional" format
   * from a byte array into a {@link PrivateKey} object.
   *
   * @param  keyBytes  Byte array containing private key data.
   *
   * @return  Private key of type in byte array.
   *
   * @throws  CryptException  On key format errors.
   * @throws  IOException  On key read errors.
   */
    public static PrivateKey readPrivateKey(final byte[] keyBytes) throws CryptException, IOException {
        return readPrivateKey(new ByteArrayInputStream(keyBytes));
    }

    /**
   * Reads an encrypted private key in PKCS#8 or OpenSSL "traditional" format
   * from a file into a {@link PrivateKey} object. Both DER and PEM encoded keys
   * are supported.
   *
   * @param  keyFile  Private key file.
   * @param  password  Password to decrypt private key.
   *
   * @return  Private key containing data read from file.
   *
   * @throws  CryptException  On key format errors.
   * @throws  IOException  On key read errors.
   */
    public static PrivateKey readPrivateKey(final File keyFile, final char[] password) throws CryptException, IOException {
        return new PrivateKeyCredentialReader().read(keyFile, password);
    }

    /**
   * Reads an encrypted private key in PKCS#8 or OpenSSL "traditional" format
   * from an input stream into a {@link PrivateKey} object. Both DER and PEM encoded keys
   * are supported.
   *
   * @param  keyStream  Input stream containing private key data.
   * @param  password  Password to decrypt private key; MUST NOT be null.
   *
   * @return  Private key containing data read from file.
   *
   * @throws  CryptException  On key format errors.
   * @throws  IOException  On key read errors.
   */
    public static PrivateKey readPrivateKey(final InputStream keyStream, final char[] password) throws CryptException, IOException {
        return new PrivateKeyCredentialReader().read(keyStream, password);
    }

    /**
   * Reads an encrypted private key in PKCS#8 or OpenSSL "traditional" format
   * from a byte array into a {@link PrivateKey} object. Both DER and PEM encoded keys
   * are supported.
   *
   * @param  keyBytes  Byte array containing private key data.
   * @param  password  Password to decrypt private key; MUST NOT be null.
   *
   * @return  Private key of type read from byte array.
   *
   * @throws  CryptException  On key format errors.
   * @throws  IOException  On key read errors.
   */
    public static PrivateKey readPrivateKey(final byte[] keyBytes, final char[] password) throws CryptException, IOException {
        return readPrivateKey(new ByteArrayInputStream(keyBytes), password);
    }

    /**
   * Reads a DER-encoded X.509 public key from an input stream into a {@link
   * PublicKey} object.
   *
   * @param  keyFile  File containing DER-encoded X.509 public key.
   *
   * @return  Public key containing data read from file.
   *
   * @throws  CryptException  On key format errors.
   * @throws  IOException  On key read errors.
   */
    public static PublicKey readPublicKey(final File keyFile) throws CryptException, IOException {
        return new PublicKeyCredentialReader().read(keyFile);
    }

    /**
   * Reads a PEM or DER-encoded X.509 public key from an input stream into a {@link
   * PublicKey} object.
   *
   * @param  keyStream  Input stream containing DER-encoded X.509 public key.
   *
   * @return  Public key containing data read from stream.
   *
   * @throws  CryptException  On key format errors.
   * @throws  IOException  On key read errors.
   */
    public static PublicKey readPublicKey(final InputStream keyStream) throws CryptException, IOException {
        return new PublicKeyCredentialReader().read(keyStream);
    }

    /**
   * Reads a PEM or DER-encoded X.509 public key from a byte array into a {@link
   * PublicKey} object.
   *
   * @param  keyBytes  Byte array containing DER-encoded X.509 public key.
   *
   * @return  Public key containing data in byte array.
   *
   * @throws  CryptException  On key format errors.
   * @throws  IOException  On key read errors.
   */
    public static PublicKey readPublicKey(final byte[] keyBytes) throws CryptException, IOException {
        return readPublicKey(new ByteArrayInputStream(keyBytes));
    }

    /**
   * Reads a PEM or DER-encoded certificate of the default type from a file into
   * a {@link Certificate} object.
   *
   * @param  certFile  Path to certificate file.
   *
   * @return  Certificate containing data read from file.
   *
   * @throws  CryptException  On certificate format errors.
   * @throws  IOException  On read errors.
   */
    public static Certificate readCertificate(final File certFile) throws CryptException, IOException {
        return readCertificate(certFile, DEFAULT_CERTIFICATE_TYPE);
    }

    /**
   * Reads a PEM or DER-encoded certificate of the given type from a file into a
   * {@link Certificate} object.
   *
   * @param  certFile  Path to certificate file.
   * @param  type  Type of certificate to read, e.g. X.509.
   *
   * @return  Certificate containing data read from file.
   *
   * @throws  CryptException  On certificate format errors.
   * @throws  IOException  On read errors.
   */
    public static Certificate readCertificate(final File certFile, final String type) throws CryptException, IOException {
        if (!DEFAULT_CERTIFICATE_TYPE.equals(type)) {
            throw new UnsupportedOperationException(type + " not supported.");
        }
        return new X509CertificateCredentialReader().read(certFile);
    }

    /**
   * Reads a PEM or DER-encoded certificate of the default type from an input
   * stream into a {@link Certificate} object.
   *
   * @param  certStream  Input stream with certificate data.
   *
   * @return  Certificate created from data read from stream.
   *
   * @throws  CryptException  On certificate read or format errors.
   * @throws  IOException  On read errors.
   */
    public static Certificate readCertificate(final InputStream certStream) throws CryptException, IOException {
        return readCertificate(certStream, DEFAULT_CERTIFICATE_TYPE);
    }

    /**
   * Reads a PEM or DER-encoded certificate of the default type from an input
   * stream into a {@link Certificate} object.
   *
   * @param  certStream  Input stream with certificate data.
   * @param  type  Type of certificate to read, e.g. X.509.
   *
   * @return  Certificate created from data read from stream.
   *
   * @throws  IOException  On read errors.
   * @throws  CryptException  On certificate read or format errors.
   */
    public static Certificate readCertificate(final InputStream certStream, final String type) throws CryptException, IOException {
        if (!DEFAULT_CERTIFICATE_TYPE.equals(type)) {
            throw new UnsupportedOperationException(type + " not supported.");
        }
        return new X509CertificateCredentialReader().read(certStream);
    }

    /**
   * Reads a PEM or DER-encoded certificate of the default type from a byte array
   * into a {@link Certificate} object.
   *
   * @param  certBytes  Byte array containing certificate data.
   *
   * @return  Certificate created from data in byte array.
   *
   * @throws  CryptException  On certificate read or format errors.
   * @throws  IOException  On read errors.
   */
    public static Certificate readCertificate(final byte[] certBytes) throws CryptException, IOException {
        return readCertificate(certBytes, DEFAULT_CERTIFICATE_TYPE);
    }

    /**
   * Reads a PEM or DER-encoded certificate of the default type from a byte array
   * into a {@link Certificate} object.
   *
   * @param  certBytes  Byte array containing certificate data.
   * @param  type  Type of certificate to read, e.g. X.509.
   *
   * @return  Certificate created from data in byte array.
   *
   * @throws  IOException  On read errors.
   * @throws  CryptException  On certificate read or format errors.
   */
    public static Certificate readCertificate(final byte[] certBytes, final String type) throws CryptException, IOException {
        return readCertificate(new ByteArrayInputStream(certBytes), type);
    }

    /**
   * Reads a certificate chain of the default certificate type from a file
   * containing data in any of the formats supported by {@link #readCertificateChain(InputStream, String)}.
   *
   * @param  chainFile  Path to certificate chain file.
   *
   * @return  Array of certificates in the order in which they appear in the given file.
   *
   * @throws  CryptException  On certificate format errors.
   * @throws  IOException  On read errors.
   */
    public static Certificate[] readCertificateChain(final File chainFile) throws CryptException, IOException {
        return readCertificateChain(chainFile, DEFAULT_CERTIFICATE_TYPE);
    }

    /**
   * Reads a certificate chain of the given type from a file containing data in
   * any of the formats supported by {@link #readCertificateChain(InputStream, String)}.
   *
   * @param  chainFile  Path to certificate chain file.
   * @param  type  Type of certificate to read, e.g. X.509.
   *
   * @return  Array of certificates in the order in which they appear in the given file.
   *
   * @throws  CryptException  On certificate format errors.
   * @throws  IOException  On read errors.
   */
    public static Certificate[] readCertificateChain(final File chainFile, final String type) throws CryptException, IOException {
        return readCertificateChain(toInputStream(chainFile), type);
    }

    /**
   * Reads a certificate chain of the default certificate type from an input
   * stream containing data in any of the formats supported by {@link #readCertificateChain(InputStream, String)}.
   *
   * @param  chainStream  Stream containing certificate chain data.
   *
   * @return  Array of certificates in the order in which they appear in the stream.
   *
   * @throws  CryptException  On certificate read or format errors.
   * @throws  IOException  On read errors.
   */
    public static Certificate[] readCertificateChain(final InputStream chainStream) throws CryptException, IOException {
        return readCertificateChain(chainStream, DEFAULT_CERTIFICATE_TYPE);
    }

    /**
   * Reads a certificate chain of the default certificate type from an input
   * stream containing data in any of the following formats:
   *
   * <ul>
   *   <li>Sequence of DER-encoded certificates</li>
   *   <li>Concatenation of PEM-encoded certificates</li>
   *   <li>PKCS#7 certificate chain</li>
   * </ul>
   *
   * @param  chainStream  Stream containing certificate chain data.
   * @param  type  Type of certificate to read, e.g. X.509.
   *
   * @return  Array of certificates in the order in which they appear in the stream.
   *
   * @throws  CryptException  On certificate read or format errors.
   * @throws  IOException  On read errors.
   */
    public static Certificate[] readCertificateChain(final InputStream chainStream, final String type) throws CryptException, IOException {
        if (!DEFAULT_CERTIFICATE_TYPE.equals(type)) {
            throw new UnsupportedOperationException(type + " not supported.");
        }
        return new X509CertificatesCredentialReader().read(chainStream);
    }

    /**
   * Reads a certificate chain of the default certificate type from a byte array
   * containing data in any of the formats supported by {@link #readCertificateChain(InputStream, String)}.
   *
   * @param  chainBytes  Byte array containing certificate chain data.
   *
   * @return  Array of certificates in the order in which they appear in the given byte array.
   *
   * @throws  CryptException  On certificate format errors.
   * @throws  IOException  On read errors.
   */
    public static Certificate[] readCertificateChain(final byte[] chainBytes) throws CryptException, IOException {
        return readCertificateChain(chainBytes, DEFAULT_CERTIFICATE_TYPE);
    }

    /**
   * Reads a certificate chain of the given type from a byte array containing data in
   * any of the formats supported by {@link #readCertificateChain(InputStream, String)}.
   *
   * @param  chainBytes  Byte array containing certificate chain data.
   * @param  type  Type of certificate to read, e.g. X.509.
   *
   * @return  Array of certificates in the order in which they appear in the given file.
   *
   * @throws  CryptException  On certificate format errors.
   * @throws  IOException  On read errors.
   */
    public static Certificate[] readCertificateChain(final byte[] chainBytes, final String type) throws CryptException, IOException {
        return readCertificateChain(new ByteArrayInputStream(chainBytes), type);
    }

    /**
   * Reads a PEM or DER-encoded X.509 CRL from an input stream into an {@link X509CRL} object.
   *
   * @param  crlStream  Input stream with CRL data.
   *
   * @return  CRL created from data read from stream.
   *
   * @throws  IOException  On read errors.
   * @throws  CryptException  On CRL read or format errors.
   */
    public static X509CRL readCRL(final InputStream crlStream) throws CryptException, IOException {
        return new X509CRLCredentialReader().read(crlStream);
    }

    /**
   * Reads a PEM or DER-encoded X.509 CRL from a file into an {@link X509CRL} object.
   *
   * @param  crlFile  Path to CRL file.
   *
   * @return  CRL created from data read from file.
   *
   * @throws  IOException  On read errors.
   * @throws  CryptException  On CRL read or format errors.
   */
    public static X509CRL readCRL(final File crlFile) throws CryptException, IOException {
        return new X509CRLCredentialReader().read(toInputStream(crlFile));
    }

    /**
   * Reads a PEM or DER-encoded X.509 CRL from a byte array into an {@link X509CRL} object.
   *
   * @param  crlBytes  Byte array containing CRL data.
   *
   * @return  CRL created from data read from byte array.
   *
   * @throws  IOException  On read errors.
   * @throws  CryptException  On CRL read or format errors.
   */
    public static X509CRL readCRL(final byte[] crlBytes) throws CryptException, IOException {
        return new X509CRLCredentialReader().read(new ByteArrayInputStream(crlBytes));
    }

    /**
   * Wraps an input stream around the given file.
   *
   * @param  file  File from which to obtain input stream.
   *
   * @return  Input stream around given file.
   *
   * @throws  IOException  On IO errors.
   */
    private static InputStream toInputStream(final File file) throws IOException {
        return new BufferedInputStream(new FileInputStream(file));
    }
}
