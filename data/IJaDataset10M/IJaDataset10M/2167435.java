package org.bouncycastle.jce.provider;

import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Hashtable;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.generators.ECKeyPairGenerator;
import org.bouncycastle.crypto.params.*;
import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.spec.ECParameterSpec;

public abstract class JDKKeyPairGenerator extends KeyPairGenerator {

    public JDKKeyPairGenerator(String algorithmName) {
        super(algorithmName);
    }

    public abstract void initialize(int strength, SecureRandom random);

    public abstract KeyPair generateKeyPair();

    public static class EC extends JDKKeyPairGenerator {

        ECKeyGenerationParameters param;

        ECKeyPairGenerator engine = new ECKeyPairGenerator();

        ECParameterSpec ecParams = null;

        int strength = 239;

        int certainty = 50;

        SecureRandom random = new SecureRandom();

        boolean initialised = false;

        String algorithm;

        private static Hashtable ecParameters;

        static {
            ecParameters = new Hashtable();
            ecParameters.put(new Integer(192), ECNamedCurveTable.getParameterSpec("prime192v1"));
            ecParameters.put(new Integer(239), ECNamedCurveTable.getParameterSpec("prime239v1"));
            ecParameters.put(new Integer(256), ECNamedCurveTable.getParameterSpec("prime256v1"));
        }

        public EC(String algorithm) {
            super(algorithm);
            this.algorithm = algorithm;
        }

        public void initialize(int strength, SecureRandom random) {
            this.strength = strength;
            this.random = random;
            this.ecParams = (ECParameterSpec) ecParameters.get(new Integer(strength));
            if (ecParams != null) {
                param = new ECKeyGenerationParameters(new ECDomainParameters(ecParams.getCurve(), ecParams.getG(), ecParams.getN()), random);
                engine.init(param);
                initialised = true;
            }
        }

        public void initialize(AlgorithmParameterSpec params, SecureRandom random) throws InvalidAlgorithmParameterException {
            if (!(params instanceof ECParameterSpec)) {
                throw new InvalidAlgorithmParameterException("parameter object not a ECParameterSpec");
            }
            this.ecParams = (ECParameterSpec) params;
            param = new ECKeyGenerationParameters(new ECDomainParameters(ecParams.getCurve(), ecParams.getG(), ecParams.getN()), random);
            engine.init(param);
            initialised = true;
        }

        public KeyPair generateKeyPair() {
            if (!initialised) {
                throw new IllegalStateException("EC Key Pair Generator not initialised");
            }
            AsymmetricCipherKeyPair pair = engine.generateKeyPair();
            ECPublicKeyParameters pub = (ECPublicKeyParameters) pair.getPublic();
            ECPrivateKeyParameters priv = (ECPrivateKeyParameters) pair.getPrivate();
            return new KeyPair(new JCEECPublicKey(algorithm, pub, ecParams), new JCEECPrivateKey(algorithm, priv, ecParams));
        }
    }

    public static class ECDSA extends EC {

        public ECDSA() {
            super("ECDSA");
        }
    }

    public static class ECDH extends EC {

        public ECDH() {
            super("ECDH");
        }
    }

    public static class ECDHC extends EC {

        public ECDHC() {
            super("ECDHC");
        }
    }
}
