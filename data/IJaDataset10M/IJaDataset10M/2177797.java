package it.unisa.dia.gas.plaf.jpbc.crypto.bls.engines;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.crypto.bls.params.BLSKeyParameters;
import it.unisa.dia.gas.plaf.jpbc.crypto.bls.params.BLSPrivateKeyParameters;
import it.unisa.dia.gas.plaf.jpbc.crypto.bls.params.BLSPublicKeyParameters;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import org.bouncycastle.crypto.*;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class BLSSigner implements Signer {

    private BLSKeyParameters keyParameters;

    private Digest digest;

    private Element h;

    private Pairing pairing;

    public BLSSigner(Digest digest) {
        this.digest = digest;
    }

    public void init(boolean forSigning, CipherParameters param) {
        if (!(param instanceof BLSKeyParameters)) throw new IllegalArgumentException("Invalid parameters. Expected an instance of BLSKeyParameters.");
        keyParameters = (BLSKeyParameters) param;
        if (forSigning && !keyParameters.isPrivate()) throw new IllegalArgumentException("signing requires private key");
        if (!forSigning && keyParameters.isPrivate()) throw new IllegalArgumentException("verification requires public key");
        this.pairing = PairingFactory.getPairing(keyParameters.getParameters().getCurveParams());
    }

    public boolean verifySignature(byte[] signature) {
        if (keyParameters == null) throw new IllegalStateException("BLS engine not initialised");
        BLSPublicKeyParameters publicKey = (BLSPublicKeyParameters) keyParameters;
        Element sig = pairing.getG1().newElement();
        sig.setFromBytes(signature);
        Element temp1 = pairing.pairing(sig, publicKey.getParameters().getG());
        Element temp2 = pairing.pairing(h, publicKey.getPk());
        return temp1.isEqual(temp2);
    }

    public byte[] generateSignature() throws CryptoException, DataLengthException {
        if (keyParameters == null) throw new IllegalStateException("BLS engine not initialised");
        BLSPrivateKeyParameters privateKey = (BLSPrivateKeyParameters) keyParameters;
        int digestSize = digest.getDigestSize();
        byte[] hash = new byte[digestSize];
        digest.doFinal(hash, 0);
        h = pairing.getG1().newElement().setFromHash(hash, 0, hash.length).getImmutable();
        Element sig = h.powZn(privateKey.getSk());
        return sig.toBytes();
    }

    public void reset() {
        digest.reset();
    }

    public void update(byte b) {
        digest.update(b);
    }

    public void update(byte[] in, int off, int len) {
        digest.update(in, off, len);
    }
}
