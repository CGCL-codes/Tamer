package it.unisa.dia.gas.plaf.jpbc.crypto.rfid.utma.strong.generators;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.crypto.rfid.utma.strong.params.UTMAStrongMasterSecretKeyParameters;
import it.unisa.dia.gas.plaf.jpbc.crypto.rfid.utma.strong.params.UTMAStrongParameters;
import it.unisa.dia.gas.plaf.jpbc.crypto.rfid.utma.strong.params.UTMAStrongPublicParameters;
import it.unisa.dia.gas.plaf.jpbc.crypto.rfid.utma.strong.params.UTMAStrongRPublicParameters;
import static it.unisa.dia.gas.plaf.jpbc.crypto.utils.IOUtils.*;
import it.unisa.dia.gas.plaf.jpbc.pairing.CurveParams;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.AsymmetricCipherKeyPairGenerator;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.KeyGenerationParameters;
import org.bouncycastle.crypto.generators.ElGamalKeyPairGenerator;
import org.bouncycastle.crypto.params.ElGamalKeyGenerationParameters;
import org.bouncycastle.crypto.params.ElGamalParameters;
import org.bouncycastle.crypto.params.ElGamalPrivateKeyParameters;
import org.bouncycastle.crypto.params.ElGamalPublicKeyParameters;
import java.io.*;
import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class UTMAStrongParametersGenerator {

    private CurveParams curveParams;

    private AsymmetricCipherKeyPairGenerator rKeyPairGenerator;

    private Pairing pairing;

    public UTMAStrongParametersGenerator(AsymmetricCipherKeyPairGenerator rKeyPairGenerator) {
        this.rKeyPairGenerator = rKeyPairGenerator;
    }

    public UTMAStrongParametersGenerator() {
        this(new ElGamalKeyPairGenerator());
    }

    public void init(CurveParams curveParams, KeyGenerationParameters keyGenerationParameters) {
        this.curveParams = curveParams;
        this.pairing = PairingFactory.getPairing(curveParams);
        rKeyPairGenerator.init(keyGenerationParameters);
    }

    public void init(CurveParams curveParams, ElGamalParameters elGamalParameters) {
        this.curveParams = curveParams;
        this.pairing = PairingFactory.getPairing(curveParams);
        rKeyPairGenerator.init(new ElGamalKeyGenerationParameters(new SecureRandom(), elGamalParameters));
    }

    public UTMAStrongParameters generateParameters() {
        Element g = pairing.getG1().newRandomElement();
        Element g0 = pairing.getG1().newRandomElement();
        Element g1 = pairing.getG1().newRandomElement();
        Element t1 = pairing.getZr().newRandomElement();
        Element t2 = pairing.getZr().newRandomElement();
        Element t3 = pairing.getZr().newRandomElement();
        Element omega = pairing.getZr().newRandomElement();
        Element T1 = g.duplicate().powZn(t1);
        Element T2 = g.duplicate().powZn(t2);
        Element T3 = g.duplicate().powZn(t3);
        Element Omega = pairing.pairing(g, g).powZn(omega.duplicate().mul(t1).mul(t2).mul(t3));
        AsymmetricCipherKeyPair rKeyPair = rKeyPairGenerator.generateKeyPair();
        UTMAStrongPublicParameters utmaPublicParameters = new UTMAStrongPublicParameters(curveParams, g.getImmutable(), g0.getImmutable(), g1.getImmutable(), Omega.getImmutable(), T1.getImmutable(), T2.getImmutable(), T3.getImmutable(), rKeyPair.getPublic());
        return new UTMAStrongParameters(utmaPublicParameters, new UTMAStrongRPublicParameters(rKeyPair.getPrivate()), new UTMAStrongMasterSecretKeyParameters(utmaPublicParameters, t1.getImmutable(), t2.getImmutable(), t3.getImmutable(), omega.getImmutable()));
    }

    public void store(OutputStream outputStream, UTMAStrongParameters parameters) throws IOException {
        DataOutputStream out = new DataOutputStream(outputStream);
        UTMAStrongPublicParameters publicParameters = parameters.getPublicParameters();
        writeByteArray(out, toBytes(publicParameters.getCurveParams()));
        writeElement(out, publicParameters.getG());
        writeElement(out, publicParameters.getG0());
        writeElement(out, publicParameters.getG1());
        writeElement(out, publicParameters.getT1());
        writeElement(out, publicParameters.getT2());
        writeElement(out, publicParameters.getT3());
        writeElement(out, publicParameters.getOmega());
        storeRPublicKey(out, publicParameters.getRPublicKey());
        UTMAStrongRPublicParameters rPublicParameters = parameters.getRPublicParameters();
        storeRPrivateKey(out, rPublicParameters.getRPrivateKey());
        UTMAStrongMasterSecretKeyParameters masterSecretKeyParameters = parameters.getMasterSecretKeyParameters();
        writeElement(out, masterSecretKeyParameters.getT1());
        writeElement(out, masterSecretKeyParameters.getT2());
        writeElement(out, masterSecretKeyParameters.getT3());
        writeElement(out, masterSecretKeyParameters.getOmega());
    }

    public UTMAStrongParameters load(InputStream inputStream) throws IOException {
        DataInputStream in = new DataInputStream(inputStream);
        CurveParams curveParams = fromBytes(CurveParams.class, readByteArray(in));
        this.pairing = PairingFactory.getPairing(curveParams);
        Element g = pairing.getG1().newElement();
        g.setFromBytes(readByteArray(in));
        Element g0 = pairing.getG1().newElement();
        g0.setFromBytes(readByteArray(in));
        Element g1 = pairing.getG1().newElement();
        g1.setFromBytes(readByteArray(in));
        Element T1 = pairing.getG1().newElement();
        T1.setFromBytes(readByteArray(in));
        Element T2 = pairing.getG1().newElement();
        T2.setFromBytes(readByteArray(in));
        Element T3 = pairing.getG1().newElement();
        T3.setFromBytes(readByteArray(in));
        Element Omega = pairing.getGT().newElement();
        Omega.setFromBytes(readByteArray(in));
        CipherParameters rPublicKey = loadRPublicKey(in);
        CipherParameters rPrivateKey = loadRPrivateKey(in);
        Element t1 = pairing.getZr().newElement();
        t1.setFromBytes(readByteArray(in));
        Element t2 = pairing.getZr().newElement();
        t2.setFromBytes(readByteArray(in));
        Element t3 = pairing.getZr().newElement();
        t3.setFromBytes(readByteArray(in));
        Element omega = pairing.getZr().newElement();
        omega.setFromBytes(readByteArray(in));
        UTMAStrongPublicParameters utmaPublicParameters = new UTMAStrongPublicParameters(curveParams, g.getImmutable(), g0.getImmutable(), g1.getImmutable(), Omega.getImmutable(), T1.getImmutable(), T2.getImmutable(), T3.getImmutable(), rPublicKey);
        return new UTMAStrongParameters(utmaPublicParameters, new UTMAStrongRPublicParameters(rPrivateKey), new UTMAStrongMasterSecretKeyParameters(utmaPublicParameters, t1.getImmutable(), t2.getImmutable(), t3.getImmutable(), omega.getImmutable()));
    }

    protected void storeRPublicKey(DataOutputStream out, CipherParameters cipherParameters) throws IOException {
        if (cipherParameters instanceof ElGamalPublicKeyParameters) {
            ElGamalPublicKeyParameters publicKeyParameters = (ElGamalPublicKeyParameters) cipherParameters;
            writeBigInteger(out, publicKeyParameters.getParameters().getG());
            writeBigInteger(out, publicKeyParameters.getParameters().getP());
            out.writeInt(publicKeyParameters.getParameters().getL());
            writeBigInteger(out, publicKeyParameters.getY());
        } else throw new IllegalArgumentException("The cipherParamenters is not an instance of ElGamalPublicKeyParameters");
    }

    protected CipherParameters loadRPublicKey(DataInputStream in) throws IOException {
        BigInteger g = readBigInteger(in);
        BigInteger p = readBigInteger(in);
        int l = in.readInt();
        BigInteger y = readBigInteger(in);
        return new ElGamalPublicKeyParameters(y, new ElGamalParameters(p, g, l));
    }

    protected void storeRPrivateKey(DataOutputStream out, CipherParameters cipherParameters) throws IOException {
        if (cipherParameters instanceof ElGamalPrivateKeyParameters) {
            ElGamalPrivateKeyParameters privateKeyParameters = (ElGamalPrivateKeyParameters) cipherParameters;
            writeBigInteger(out, privateKeyParameters.getParameters().getG());
            writeBigInteger(out, privateKeyParameters.getParameters().getP());
            out.writeInt(privateKeyParameters.getParameters().getL());
            writeBigInteger(out, privateKeyParameters.getX());
        } else throw new IllegalArgumentException("The cipherParamenters is not an instance of ElGamalPrivateKeyParameters");
    }

    protected CipherParameters loadRPrivateKey(DataInputStream in) throws IOException {
        BigInteger g = readBigInteger(in);
        BigInteger p = readBigInteger(in);
        int l = in.readInt();
        BigInteger x = readBigInteger(in);
        return new ElGamalPrivateKeyParameters(x, new ElGamalParameters(p, g, l));
    }
}
