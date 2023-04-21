package addressbook.servlet.model;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.interfaces.DSAParams;
import java.security.interfaces.DSAPrivateKey;
import java.util.Properties;
import addressbook.servlet.AddressBookProcessor;

public class CertificateOperations extends AbstractOperations {

    public static final String DSA = "DSA";

    public static final String DES = "DES";

    @Override
    public void init(AddressBookProcessor abp) {
    }

    public KeyPair createCertificate(String algorithm) throws NoSuchAlgorithmException {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance(algorithm);
        SecureRandom sr = new SecureRandom();
        kpg.initialize(512, new SecureRandom(sr.generateSeed(8)));
        return kpg.genKeyPair();
    }

    void storeKey(File _file, boolean _private) {
        try {
            Properties key = new Properties();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    class A implements DSAPrivateKey {

        byte[] k;

        DSAParams param;

        BigInteger x;

        public A(File _key) {
            try {
                Properties kp = new Properties();
                x = new BigInteger(kp.getProperty("X"), 16);
                param = new B(new BigInteger(kp.getProperty("P"), 16), new BigInteger(kp.getProperty("Q"), 16), new BigInteger(kp.getProperty("G"), 16));
            } catch (Exception e) {
                System.err.println("problem reading a key." + e);
            }
        }

        public String getAlgorithm() {
            return DSA;
        }

        public String getFormat() {
            return "X.509";
        }

        public byte[] getEncoded() {
            return k;
        }

        public DSAParams getParams() {
            return param;
        }

        public BigInteger getX() {
            return x;
        }
    }

    class B implements DSAParams {

        BigInteger p, q, g;

        B(BigInteger _p, BigInteger _q, BigInteger _g) {
            p = _p;
            q = _q;
            g = _g;
        }

        public BigInteger getP() {
            return p;
        }

        public BigInteger getQ() {
            return q;
        }

        public BigInteger getG() {
            return g;
        }
    }
}
