package edu.vt.middleware.crypt.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import org.bouncycastle.openssl.PEMReader;
import org.bouncycastle.openssl.PEMWriter;
import org.bouncycastle.openssl.PasswordFinder;

/**
 * Helper class provides operations for encoding/decoding cryptographic keys and
 * certificates to PEM format.
 *
 * @author  Middleware Services
 * @version  $Revision: 3 $
 */
public class PemHelper {

    /** Encryption algorithm used for password-protected private keys. */
    public static final String KEY_ENCRYPTION_ALGORITHM = "AES-256-CBC";

    /** PEM encoding header start string. */
    public static final String HEADER_BEGIN = "-----BEGIN";

    /** PEM encoding footer start string. */
    public static final String FOOTER_END = "-----END";

    /** Procedure type tag for PEM-encoded private key in OpenSSL format. */
    public static final String PROC_TYPE = "Proc-Type:";

    /** Decryption infor tag for PEM-encoded private key in OpenSSL format. */
    public static final String DEK_INFO = "DEK-Info:";

    /** Hidden constructor of utility class. */
    protected PemHelper() {
    }

    /**
   * Decodes a PEM-encoded cryptographic object into the raw bytes of its ASN.1
   * encoding. Header/footer data and metadata info, e.g. Proc-Type, are
   * ignored.
   *
   * @param  pem  Bytes of PEM-encoded data to decode.
   *
   * @return  ASN.1 encoded bytes.
   *
   * @throws  IOException  On decoding error.
   */
    public static byte[] decode(final byte[] pem) throws IOException {
        return decode(new String(pem, "ASCII"));
    }

    /**
   * Decodes a PEM-encoded cryptographic object into the raw bytes of its ASN.1
   * encoding. Header/footer data and metadata info, e.g. Proc-Type, are
   * ignored.
   *
   * @param  pem  PEM-encoded data to decode.
   *
   * @return  ASN.1 encoded bytes.
   *
   * @throws  IOException  On decoding error.
   */
    public static byte[] decode(final String pem) throws IOException {
        final BufferedReader reader = new BufferedReader(new StringReader(pem));
        final ByteBuffer buffer = ByteBuffer.allocateDirect(pem.length() * 3 / 4);
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.startsWith(HEADER_BEGIN) || line.startsWith(FOOTER_END) || line.startsWith(PROC_TYPE) || line.startsWith(DEK_INFO) || line.trim().length() == 0) {
                continue;
            }
            buffer.put(Convert.fromBase64(line));
        }
        buffer.flip();
        final byte[] result = new byte[buffer.limit()];
        buffer.get(result);
        return result;
    }

    /**
   * Encodes the given public key to PEM format.
   *
   * @param  key  Public key to encode.
   *
   * @return  Key as PEM-encoded text.
   *
   * @throws  IOException  On encoding error.
   */
    public static String encodeKey(final PublicKey key) throws IOException {
        return encodeObject(key);
    }

    /**
   * Decodes the given public key from PEM format.
   *
   * @param  pemKey  PEM-encoded public key text to decode.
   *
   * @return  Public key.
   *
   * @throws  IOException  On decoding error.
   */
    public static PublicKey decodeKey(final String pemKey) throws IOException {
        final PEMReader reader = new PEMReader(new StringReader(pemKey));
        final PublicKey key = (PublicKey) reader.readObject();
        if (key != null) {
            return key;
        } else {
            throw new IOException("Error decoding public key.");
        }
    }

    /**
   * Encodes the given private key to PEM format.
   *
   * @param  key  Private key to encode.
   * @param  password  Password used to encrypt private key using 256-bit AES
   * encryption; may be null to indicate no encryption.
   * @param  random  Secure random provider used for encrypting private key.
   *
   * @return  Key as PEM-encoded text.
   *
   * @throws  IOException  On encoding error.
   */
    public static String encodeKey(final PrivateKey key, final char[] password, final SecureRandom random) throws IOException {
        if (password == null || password.length == 0) {
            return encodeObject(key);
        } else {
            final StringWriter sw = new StringWriter();
            PEMWriter writer = null;
            try {
                writer = new PEMWriter(sw);
                writer.writeObject(key, KEY_ENCRYPTION_ALGORITHM, password, random);
            } finally {
                if (writer != null) {
                    writer.close();
                }
            }
            return sw.toString();
        }
    }

    /**
   * Decodes the given private key from PEM format.
   *
   * @param  pemKey  PEM-encoded private key text to decode.
   * @param  password  Optional password that is used to decrypt private key
   * using DESEDE algorithm when specified.
   *
   * @return  Private key.
   *
   * @throws  IOException  On decoding error.
   */
    public static PrivateKey decodeKey(final String pemKey, final char[] password) throws IOException {
        PEMReader reader = null;
        if (password == null || password.length == 0) {
            reader = new PEMReader(new StringReader(pemKey));
        } else {
            reader = new PEMReader(new StringReader(pemKey), new PasswordFinder() {

                public char[] getPassword() {
                    return password;
                }
            });
        }
        final KeyPair keyPair = (KeyPair) reader.readObject();
        if (keyPair != null) {
            return keyPair.getPrivate();
        } else {
            throw new IOException("Error decoding private key.");
        }
    }

    /**
   * Encodes the given certificate to PEM format.
   *
   * @param  key  Certificate to encode.
   *
   * @return  Certificate as PEM-encoded text.
   *
   * @throws  IOException  On encoding error.
   */
    public static String encodeCert(final Certificate key) throws IOException {
        return encodeObject(key);
    }

    /**
   * Decodes the given certificate from PEM format.
   *
   * @param  pemCert  PEM-encoded certificate text to decode.
   *
   * @return  Certificate.
   *
   * @throws  IOException  On decoding error.
   */
    public static Certificate decodeCert(final String pemCert) throws IOException {
        final PEMReader reader = new PEMReader(new StringReader(pemCert));
        final Certificate cert = (Certificate) reader.readObject();
        if (cert != null) {
            return cert;
        } else {
            throw new IOException("Error decoding certificate.");
        }
    }

    /**
   * Determines whether the data in the given byte array is base64-encoded data
   * of PEM encoding. The determination is made using as little data from the
   * given array as necessary to make a reasonable determination about encoding.
   *
   * @param  data  Data to test for PEM encoding
   *
   * @return  True if data appears to be PEM encoded, false otherwise.
   */
    public static boolean isPem(final byte[] data) {
        boolean result = true;
        try {
            final String start = new String(data, 0, 10, "ASCII");
            if (start.startsWith(HEADER_BEGIN) || start.startsWith(PROC_TYPE)) {
                return true;
            } else {
                final int lineLength = 64;
                for (int i = 0; i < lineLength && result; i++) {
                    result = isBase64Char(data[i]);
                    if (i > lineLength - 3) {
                        result |= data[i] == 61;
                    }
                }
            }
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException("YIKES! ASCII encoding not supported.");
        }
        return result;
    }

    /**
   * Determines whether the given byte represents an ASCII character in the
   * character set for base64 encoding.
   *
   * @param  b  Byte to test.
   *
   * @return  True if the byte represents an ASCII character in the set of valid
   * characters for base64 encoding, false otherwise. The padding character '='
   * is not considered valid since it may only appear at the end of a base64
   * encoded value.
   */
    public static boolean isBase64Char(final byte b) {
        return !(b < 47 || b > 122 || b > 57 && b < 65 || b > 90 && b < 97) || b == 43;
    }

    /**
   * Encodes the given object to PEM format if possible.
   *
   * @param  o  Object to encode.
   *
   * @return  Object as PEM-encoded text.
   *
   * @throws  IOException  On encoding error.
   */
    private static String encodeObject(final Object o) throws IOException {
        final StringWriter sw = new StringWriter();
        PEMWriter writer = null;
        try {
            writer = new PEMWriter(sw);
            writer.writeObject(o);
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
        return sw.toString();
    }
}
