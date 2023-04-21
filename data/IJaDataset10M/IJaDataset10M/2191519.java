package it.unisa.dia.gas.crypto.jpbc.rfid.utma.strong.generators;

import it.unisa.dia.gas.crypto.jpbc.rfid.utma.strong.params.*;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.CurveParams;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.AsymmetricCipherKeyPairGenerator;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.KeyGenerationParameters;
import org.bouncycastle.crypto.params.ElGamalParameters;
import org.bouncycastle.crypto.params.ElGamalPrivateKeyParameters;
import org.bouncycastle.crypto.params.ElGamalPublicKeyParameters;
import java.io.*;
import java.math.BigInteger;
import static it.unisa.dia.gas.crypto.jpbc.utils.IOUtils.*;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class UTMAStrongKeyPairGenerator implements AsymmetricCipherKeyPairGenerator {

    private UTMAStrongKeyGenerationParameters param;

    private Pairing pairing;

    public void init(KeyGenerationParameters param) {
        this.param = (UTMAStrongKeyGenerationParameters) param;
        pairing = PairingFactory.getPairing(this.param.getParameters().getPublicParameters().getCurveParams());
    }

    public AsymmetricCipherKeyPair generateKeyPair() {
        UTMAStrongParameters parameters = param.getParameters();
        UTMAStrongPublicParameters publicParameters = parameters.getPublicParameters();
        UTMAStrongMasterSecretKeyParameters mskParameters = parameters.getMasterSecretKeyParameters();
        Element r = pairing.getZr().newElement().setToRandom();
        Element pk = publicParameters.getG1().powZn(r).mul(publicParameters.getG0());
        Element D0 = publicParameters.getG().powZn(r.duplicate().mul(mskParameters.getT1()).mul(mskParameters.getT2()).mul(mskParameters.getT3()));
        Element D1 = publicParameters.getG().powZn(mskParameters.getOmega().duplicate().mul(mskParameters.getT1()).mul(mskParameters.getT3()).negate()).mul(pk.duplicate().powZn(r.duplicate().mul(mskParameters.getT1()).mul(mskParameters.getT3()).negate()));
        Element D2 = publicParameters.getG().powZn(mskParameters.getOmega().duplicate().mul(mskParameters.getT1()).mul(mskParameters.getT2()).negate()).mul(pk.duplicate().powZn(r.duplicate().mul(mskParameters.getT1()).mul(mskParameters.getT2()).negate()));
        Element D3 = publicParameters.getG().powZn(mskParameters.getOmega().duplicate().mul(mskParameters.getT2()).mul(mskParameters.getT3()).negate()).mul(pk.duplicate().powZn(r.duplicate().mul(mskParameters.getT2()).mul(mskParameters.getT3()).negate()));
        return new AsymmetricCipherKeyPair(new UTMAStrongPublicKeyParameters(publicParameters, pk.getImmutable()), new UTMAStrongPrivateKeyParameters(publicParameters, D0.getImmutable(), D1.getImmutable(), D2.getImmutable(), D3.getImmutable(), parameters.getRPublicParameters().getRPrivateKey()));
    }

    public void store(OutputStream outputStream, AsymmetricCipherKeyPair keyPair) throws IOException {
        storePublicKey(outputStream, keyPair.getPublic());
        storePrivateKey(outputStream, keyPair.getPrivate());
    }

    public void storePublicKey(OutputStream outputStream, CipherParameters publicCipherParameters) throws IOException {
        DataOutputStream out = new DataOutputStream(outputStream);
        UTMAStrongPublicKeyParameters publicKeyParameters = (UTMAStrongPublicKeyParameters) publicCipherParameters;
        UTMAStrongPublicParameters publicParameters = publicKeyParameters.getParameters();
        writeByteArray(out, toBytes(publicParameters.getCurveParams()));
        writeElement(out, publicParameters.getG());
        writeElement(out, publicParameters.getG0());
        writeElement(out, publicParameters.getG1());
        writeElement(out, publicParameters.getT1());
        writeElement(out, publicParameters.getT2());
        writeElement(out, publicParameters.getT3());
        writeElement(out, publicParameters.getOmega());
        storeRPublicKey(out, publicParameters.getRPublicKey());
        writeElement(out, publicKeyParameters.getPk());
    }

    public void storePrivateKey(OutputStream outputStream, CipherParameters privateCipherParameters) throws IOException {
        DataOutputStream out = new DataOutputStream(outputStream);
        UTMAStrongPrivateKeyParameters privateKeyParameters = (UTMAStrongPrivateKeyParameters) privateCipherParameters;
        UTMAStrongPublicParameters publicParameters = privateKeyParameters.getParameters();
        writeByteArray(out, toBytes(publicParameters.getCurveParams()));
        writeElement(out, publicParameters.getG());
        writeElement(out, publicParameters.getG0());
        writeElement(out, publicParameters.getG1());
        writeElement(out, publicParameters.getT1());
        writeElement(out, publicParameters.getT2());
        writeElement(out, publicParameters.getT3());
        writeElement(out, publicParameters.getOmega());
        storeRPublicKey(out, publicParameters.getRPublicKey());
        storeRPrivateKey(out, privateKeyParameters.getRPrivateKey());
        writeElement(out, privateKeyParameters.getD0());
        writeElement(out, privateKeyParameters.getD1());
        writeElement(out, privateKeyParameters.getD2());
        writeElement(out, privateKeyParameters.getD3());
    }

    public AsymmetricCipherKeyPair load(InputStream inputStream) throws IOException {
        return new AsymmetricCipherKeyPair(loadPublicKey(inputStream), loadPrivateKey(inputStream));
    }

    public CipherParameters loadPublicKey(InputStream inputStream) throws IOException {
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
        Element pk = pairing.getG1().newElement();
        pk.setFromBytes(readByteArray(in));
        return new UTMAStrongPublicKeyParameters(new UTMAStrongPublicParameters(curveParams, g.getImmutable(), g0.getImmutable(), g1.getImmutable(), Omega.getImmutable(), T1.getImmutable(), T2.getImmutable(), T3.getImmutable(), rPublicKey), pk.getImmutable());
    }

    public CipherParameters loadPrivateKey(InputStream inputStream) throws IOException {
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
        Element D0 = pairing.getG1().newElement();
        D0.setFromBytes(readByteArray(in));
        Element D1 = pairing.getG1().newElement();
        D1.setFromBytes(readByteArray(in));
        Element D2 = pairing.getG1().newElement();
        D2.setFromBytes(readByteArray(in));
        Element D3 = pairing.getG1().newElement();
        D3.setFromBytes(readByteArray(in));
        return new UTMAStrongPrivateKeyParameters(new UTMAStrongPublicParameters(curveParams, g.getImmutable(), g0.getImmutable(), g1.getImmutable(), Omega.getImmutable(), T1.getImmutable(), T2.getImmutable(), T3.getImmutable(), rPublicKey), D0.getImmutable(), D1.getImmutable(), D2.getImmutable(), D3.getImmutable(), rPrivateKey);
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
