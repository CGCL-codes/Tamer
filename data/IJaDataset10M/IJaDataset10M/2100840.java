package org.exos.crypto.dsa;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import org.bouncycastle.asn1.x509.DSAParameter;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.params.DSAKeyParameters;
import org.bouncycastle.crypto.params.DSAParameters;
import org.bouncycastle.crypto.params.RSAKeyParameters;
import org.exos.crypto.CryptoFactory;
import org.exos.crypto.PublicKey;

public class BCDSAPublicKey extends PublicKey {

    private DSAKeyParameters dsaPublicKey;

    private boolean isInitialized = false;

    public BCDSAPublicKey() {
    }

    public BCDSAPublicKey(DSAKeyParameters p) {
        if (!p.isPrivate()) {
            dsaPublicKey = p;
        } else {
            throw new RuntimeException("Wrong key parameter");
        }
        isInitialized = true;
    }

    public boolean isInitialized() {
        return isInitialized;
    }

    public void read(InputStream in) throws IOException {
        DataInputStream din = new DataInputStream(in);
        int l = din.readInt();
        byte[] b = new byte[l];
        din.readFully(b, 0, l);
        BigInteger p = new BigInteger(b);
        l = din.readInt();
        b = new byte[l];
        din.readFully(b, 0, l);
        BigInteger q = new BigInteger(b);
        l = din.readInt();
        b = new byte[l];
        din.readFully(b, 0, l);
        BigInteger g = new BigInteger(b);
        DSAParameters params = new DSAParameters(p, q, g);
        dsaPublicKey = new DSAKeyParameters(false, params);
        isInitialized = true;
    }

    public void write(OutputStream out) throws IOException {
        DataOutputStream dos = new DataOutputStream(out);
        byte[] b = dsaPublicKey.getParameters().getP().toByteArray();
        dos.writeInt(b.length);
        dos.write(b, 0, b.length);
        b = dsaPublicKey.getParameters().getQ().toByteArray();
        dos.writeInt(b.length);
        dos.write(b, 0, b.length);
        b = dsaPublicKey.getParameters().getG().toByteArray();
        dos.writeInt(b.length);
        dos.write(b, 0, b.length);
    }

    public AsymmetricKeyParameter getBCVersion() {
        return dsaPublicKey;
    }

    public byte[] getASN1DEREncoded() {
        if (!isInitialized) return null;
        DSAParameter dsa = new DSAParameter(dsaPublicKey.getParameters().getP(), dsaPublicKey.getParameters().getQ(), dsaPublicKey.getParameters().getG());
        return dsa.getDEREncoded();
    }

    public String getAlgorithm() {
        return CryptoFactory.ALGORITHM_DSA;
    }
}
