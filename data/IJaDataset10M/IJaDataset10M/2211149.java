package com.eaio.plateau.examples.filters;

import java.math.BigInteger;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.security.*;
import java.security.spec.InvalidParameterSpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.Cipher;
import javax.crypto.KeyAgreement;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.DHParameterSpec;
import com.eaio.plateau.filter.PacketFilter;
import com.eaio.util.io.BufferOutputStream;

/**
 * This class implements transparent encryption and decryption using a user-
 * definable algorithm with Diffie-Hellman key exchange.
 * 
 * @author <a href="mailto:jb@eaio.com">Johann Burkard</a>
 * @version $Id: CipherPacketFilterImpl.java,v 1.1 2005/11/20 20:15:02 grnull Exp $
 */
public final class CipherPacketFilterImpl implements PacketFilter {

    /**
  * The algorithm to use.
  */
    private String algorithm;

    /**
  * The provider (a String or a Provider instance).
  */
    private Object provider;

    /**
  * The key pair.
  */
    private KeyPair keys;

    /**
  * The encrypted form of the public key.
  */
    private byte[] encryptedPublicKey;

    /**
  * The mapping of host to session key.
  */
    private Map hostKeys = new HashMap();

    /**
  * Constructor for CipherPacketFilterImpl. Uses the Triple-DES algorithm.
  * 
  * @throws NoSuchAlgorithmException
  * @throws NoSuchProviderException
  * @throws InvalidParameterSpecException
  * @throws InvalidAlgorithmParameterException
  * @throws InvalidKeyException
  */
    public CipherPacketFilterImpl() throws NoSuchAlgorithmException, NoSuchProviderException, InvalidParameterSpecException, InvalidAlgorithmParameterException, InvalidKeyException {
        this("DESede", (Object) null);
    }

    /**
  * Constructor for CipherPacketFilterImpl.
  * 
  * @param algorithm the algorithm to use, may not be <code>null</code>
  * @param provider the Provider, may be <code>null</code>
  * @throws NoSuchAlgorithmException
  * @throws NoSuchProviderException
  * @throws InvalidParameterSpecException
  * @throws InvalidAlgorithmParameterException
  * @throws InvalidKeyException
  */
    public CipherPacketFilterImpl(String algorithm, Provider provider) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidParameterSpecException, InvalidAlgorithmParameterException, InvalidKeyException {
        this(algorithm, (Object) provider);
    }

    /**
  * Constructor for CipherPacketFilterImpl.
  * 
  * @param algorithm the algorithm to use, may not be <code>null</code>
  * @param provider the Provider, may be <code>null</code>
  * @throws NoSuchAlgorithmException
  * @throws NoSuchProviderException
  * @throws InvalidParameterSpecException
  * @throws InvalidAlgorithmParameterException
  * @throws InvalidKeyException
  */
    public CipherPacketFilterImpl(String algorithm, String provider) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidParameterSpecException, InvalidAlgorithmParameterException, InvalidKeyException {
        this(algorithm, (Object) provider);
    }

    /**
  * Constructor for CipherPacketFilterImpl.
  * 
  * @param algorithm the algorithm to use, may not be <code>null</code>
  * @param provider the Provider, may be <code>null</code>
  * @throws NoSuchAlgorithmException
  * @throws NoSuchProviderException
  * @throws InvalidParameterSpecException
  * @throws InvalidAlgorithmParameterException
  * @throws InvalidKeyException
  */
    private CipherPacketFilterImpl(String algorithm, Object provider) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidParameterSpecException, InvalidAlgorithmParameterException, InvalidKeyException {
        if (algorithm == null) {
            throw new NullPointerException();
        }
        this.algorithm = algorithm;
        this.provider = provider;
        initKeys();
    }

    private void initKeys() throws NoSuchAlgorithmException, NoSuchProviderException, InvalidParameterSpecException, InvalidAlgorithmParameterException, InvalidKeyException {
        KeyPairGenerator generator = createKeyPairGenerator();
        generator.initialize(SPEC);
        keys = generator.generateKeyPair();
        encryptedPublicKey = keys.getPublic().getEncoded();
    }

    private KeyPairGenerator createKeyPairGenerator() throws NoSuchAlgorithmException, NoSuchProviderException {
        if (provider instanceof Provider) {
            return KeyPairGenerator.getInstance("DH", (Provider) provider);
        } else if (provider instanceof String) {
            return KeyPairGenerator.getInstance("DH", (String) provider);
        } else {
            return KeyPairGenerator.getInstance("DH");
        }
    }

    private Cipher createCipher() throws NoSuchPaddingException, NoSuchAlgorithmException, NoSuchProviderException {
        if (provider instanceof Provider) {
            return Cipher.getInstance(algorithm, (Provider) provider);
        } else if (provider instanceof String) {
            return Cipher.getInstance(algorithm, (String) provider);
        } else {
            return Cipher.getInstance(algorithm);
        }
    }

    private KeyFactory createKeyFactory() throws NoSuchAlgorithmException, NoSuchProviderException {
        if (provider instanceof Provider) {
            return KeyFactory.getInstance("DH", (Provider) provider);
        } else if (provider instanceof String) {
            return KeyFactory.getInstance("DH", (String) provider);
        } else {
            return KeyFactory.getInstance("DH");
        }
    }

    private KeyAgreement createKeyAgreement() throws NoSuchAlgorithmException, NoSuchProviderException {
        if (provider instanceof Provider) {
            return KeyAgreement.getInstance("DH", (Provider) provider);
        } else if (provider instanceof String) {
            return KeyAgreement.getInstance("DH", (String) provider);
        } else {
            return KeyAgreement.getInstance("DH");
        }
    }

    /**
  * @see com.eaio.plateau.filter.PacketFilter#encode(java.net.DatagramPacket)
  */
    public void encode(DatagramPacket packet) throws Exception {
        Host h = new Host(packet.getAddress(), packet.getPort());
        HostKey hostKey = (HostKey) hostKeys.get(h);
        if (hostKey == null) {
            hostKey = new HostKey();
            hostKey.hasOwnPK = true;
            hostKeys.put(h, hostKey);
            byte[] data = packet.getData();
            int o = packet.getOffset();
            int l = packet.getLength();
            int rest = data.length - packet.getOffset() - packet.getLength();
            if (rest >= encryptedPublicKey.length) {
                System.arraycopy(encryptedPublicKey, 0, data, o + l, encryptedPublicKey.length);
                packet.setData(data, o, l + encryptedPublicKey.length);
            } else {
                BufferOutputStream out = new BufferOutputStream(packet.getLength() + encryptedPublicKey.length);
                out.write(data, o, l);
                out.write(encryptedPublicKey);
                packet.setData(out.getBuffer(), 0, out.getPosition());
            }
        } else {
            SecretKey secretKey = hostKey.key;
            Cipher cipher = createCipher();
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encrypted = cipher.doFinal(packet.getData(), packet.getOffset(), packet.getLength());
            synchronized (hostKey) {
                if (hostKey.hasOwnPK) {
                    packet.setData(encrypted);
                } else {
                    byte[] buf = new byte[encrypted.length + encryptedPublicKey.length];
                    System.arraycopy(encrypted, 0, buf, 0, encrypted.length);
                    System.arraycopy(encryptedPublicKey, 0, buf, encrypted.length, encryptedPublicKey.length);
                    packet.setData(buf, 0, buf.length);
                    hostKey.hasOwnPK = true;
                }
            }
        }
    }

    /**
  * @see com.eaio.plateau.filter.PacketFilter#decode(java.net.DatagramPacket)
  */
    public boolean decode(DatagramPacket packet) {
        Host h = new Host(packet.getAddress(), packet.getPort());
        HostKey hostKey = (HostKey) hostKeys.get(h);
        try {
            if (hostKey == null) {
                hostKey = new HostKey();
                hostKey.hasOwnPK = false;
                hostKeys.put(h, hostKey);
                byte[] data = packet.getData();
                int o = packet.getOffset();
                int l = packet.getLength();
                int start = o + l - encryptedPublicKey.length;
                byte[] other = new byte[encryptedPublicKey.length];
                System.arraycopy(data, start, other, 0, encryptedPublicKey.length);
                X509EncodedKeySpec spec = new X509EncodedKeySpec(other);
                KeyFactory factory = createKeyFactory();
                PublicKey otherKey = factory.generatePublic(spec);
                KeyAgreement agreement = createKeyAgreement();
                agreement.init(keys.getPrivate());
                agreement.doPhase(otherKey, true);
                hostKey.key = agreement.generateSecret(algorithm);
                packet.setData(packet.getData(), packet.getOffset(), packet.getData().length - packet.getOffset() - encryptedPublicKey.length);
            } else {
                if (hostKey.key == null) {
                    byte[] data = packet.getData();
                    int o = packet.getOffset();
                    int l = packet.getLength();
                    int start = o + l - encryptedPublicKey.length;
                    byte[] other = new byte[encryptedPublicKey.length];
                    System.arraycopy(data, start, other, 0, encryptedPublicKey.length);
                    X509EncodedKeySpec spec = new X509EncodedKeySpec(other);
                    KeyFactory factory = createKeyFactory();
                    PublicKey otherKey = factory.generatePublic(spec);
                    KeyAgreement agreement = createKeyAgreement();
                    agreement.init(keys.getPrivate());
                    agreement.doPhase(otherKey, true);
                    hostKey.key = agreement.generateSecret(algorithm);
                }
                Cipher cipher = createCipher();
                cipher.init(Cipher.DECRYPT_MODE, hostKey.key);
                byte[] decrypted = cipher.update(packet.getData(), packet.getOffset(), packet.getLength());
                packet.setData(decrypted);
            }
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    /**
  * Encapsulates host information.
  */
    private final class Host {

        private InetAddress address;

        private int port;

        private Host(InetAddress address, int port) {
            this.address = address;
            this.port = port;
        }

        /**
   * @see java.lang.Object#equals(Object)
   */
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (!(obj instanceof Host)) return false;
            Host p = (Host) obj;
            return address.equals(p.address) && port == p.port;
        }

        /**
   * @see java.lang.Object#hashCode()
   */
        public int hashCode() {
            return address.hashCode() ^ port;
        }
    }

    /**
  * Encapsulates host keys.
  */
    private final class HostKey {

        private SecretKey key;

        private boolean hasOwnPK = false;
    }

    private static final DHParameterSpec SPEC = new DHParameterSpec(new BigInteger(new byte[] { (byte) 0x00, (byte) 0xbc, (byte) 0x85, (byte) 0xb4, (byte) 0x18, (byte) 0xf5, (byte) 0x34, (byte) 0xee, (byte) 0xfe, (byte) 0x8b, (byte) 0x77, (byte) 0x87, (byte) 0xcb, (byte) 0x47, (byte) 0xbd, (byte) 0x5a, (byte) 0xc7, (byte) 0x57, (byte) 0xf2, (byte) 0x66, (byte) 0x2a, (byte) 0x6c, (byte) 0x3b, (byte) 0x42, (byte) 0x4b, (byte) 0xa7, (byte) 0x61, (byte) 0xad, (byte) 0x53, (byte) 0x95, (byte) 0x74, (byte) 0xc9, (byte) 0x1f, (byte) 0x88, (byte) 0x36, (byte) 0xbd, (byte) 0xea, (byte) 0x9c, (byte) 0x92, (byte) 0xe5, (byte) 0xf0, (byte) 0xc5, (byte) 0xec, (byte) 0x12, (byte) 0x7a, (byte) 0xa7, (byte) 0xfb, (byte) 0xed, (byte) 0x9b, (byte) 0xbc, (byte) 0x52, (byte) 0x88, (byte) 0xd5, (byte) 0x66, (byte) 0xe4, (byte) 0x88, (byte) 0x06, (byte) 0xc0, (byte) 0xbd, (byte) 0x38, (byte) 0xf7, (byte) 0xd1, (byte) 0x58, (byte) 0xcd, (byte) 0xca, (byte) 0x7d, (byte) 0x3a, (byte) 0x6a, (byte) 0x6c, (byte) 0xd1, (byte) 0xc7, (byte) 0xbb, (byte) 0x6d, (byte) 0xaa, (byte) 0x7f, (byte) 0x99, (byte) 0x5c, (byte) 0x52, (byte) 0x89, (byte) 0x54, (byte) 0x8c, (byte) 0x2d, (byte) 0x9a, (byte) 0x11, (byte) 0xb1, (byte) 0x6e, (byte) 0x69, (byte) 0x10, (byte) 0x5b, (byte) 0x31, (byte) 0x68, (byte) 0x3a, (byte) 0x81, (byte) 0x5b, (byte) 0xfb, (byte) 0x3f, (byte) 0xdd, (byte) 0xc3, (byte) 0x10, (byte) 0xa9, (byte) 0x0c, (byte) 0x1a, (byte) 0x74, (byte) 0xa5, (byte) 0xb6, (byte) 0x5d, (byte) 0x9d, (byte) 0xe6, (byte) 0xe6, (byte) 0xdf, (byte) 0x61, (byte) 0xe5, (byte) 0x89, (byte) 0x3b, (byte) 0x7a, (byte) 0x38, (byte) 0x29, (byte) 0xe8, (byte) 0x9c, (byte) 0x5f, (byte) 0xd2, (byte) 0x0b, (byte) 0x86, (byte) 0xc9, (byte) 0x0c, (byte) 0x1a, (byte) 0x09, (byte) 0xac, (byte) 0x5d }), new BigInteger(new byte[] { (byte) 0x15, (byte) 0x2c, (byte) 0x78, (byte) 0x2e, (byte) 0x93, (byte) 0xd9, (byte) 0xd5, (byte) 0x78, (byte) 0x44, (byte) 0x44, (byte) 0x76, (byte) 0x79, (byte) 0x06, (byte) 0x7f, (byte) 0x33, (byte) 0xaa, (byte) 0xe3, (byte) 0x1e, (byte) 0xf0, (byte) 0x1f, (byte) 0xe1, (byte) 0xd5, (byte) 0x07, (byte) 0x39, (byte) 0x12, (byte) 0xe0, (byte) 0x1c, (byte) 0x53, (byte) 0x62, (byte) 0x8c, (byte) 0x63, (byte) 0x3f, (byte) 0x8c, (byte) 0x51, (byte) 0x96, (byte) 0xa0, (byte) 0xbd, (byte) 0x44, (byte) 0xd3, (byte) 0xd2, (byte) 0x5d, (byte) 0x0d, (byte) 0xe5, (byte) 0x33, (byte) 0xe1, (byte) 0x6b, (byte) 0x8f, (byte) 0xf0, (byte) 0x10, (byte) 0xeb, (byte) 0xb3, (byte) 0xfa, (byte) 0xb2, (byte) 0xd1, (byte) 0x6b, (byte) 0x00, (byte) 0x01, (byte) 0xf9, (byte) 0xdd, (byte) 0x17, (byte) 0xcc, (byte) 0xd1, (byte) 0x4f, (byte) 0x9d, (byte) 0xd2, (byte) 0xd1, (byte) 0x19, (byte) 0xbb, (byte) 0x20, (byte) 0x25, (byte) 0xa2, (byte) 0xd9, (byte) 0x28, (byte) 0x65, (byte) 0xfc, (byte) 0x73, (byte) 0x91, (byte) 0x60, (byte) 0xf8, (byte) 0xa8, (byte) 0x88, (byte) 0x6d, (byte) 0x4a, (byte) 0x79, (byte) 0x4c, (byte) 0xa8, (byte) 0xe0, (byte) 0xe8, (byte) 0x01, (byte) 0xad, (byte) 0x7b, (byte) 0xa0, (byte) 0xdb, (byte) 0x3c, (byte) 0x52, (byte) 0x9d, (byte) 0x28, (byte) 0x88, (byte) 0xb9, (byte) 0x19, (byte) 0x72, (byte) 0x2d, (byte) 0x13, (byte) 0x87, (byte) 0xb3, (byte) 0xb2, (byte) 0xf4, (byte) 0x2c, (byte) 0x6c, (byte) 0xa9, (byte) 0xde, (byte) 0xa3, (byte) 0x2a, (byte) 0xc2, (byte) 0x82, (byte) 0xe1, (byte) 0xa1, (byte) 0x76, (byte) 0xbb, (byte) 0xfe, (byte) 0x5a, (byte) 0x08, (byte) 0x11, (byte) 0x1f, (byte) 0x01, (byte) 0xec, (byte) 0x01, (byte) 0x3a }));
}
