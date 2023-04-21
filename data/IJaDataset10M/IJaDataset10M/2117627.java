package tgdh;

import java.io.*;
import java.math.BigInteger;
import java.security.interfaces.DSAParams;
import java.security.interfaces.DSAPublicKey;

/**
 * TGDHPublicKey class for the TGDH public key
 * 
 * @author Gilbert, Paresh, Sanket
 * 
 */
@SuppressWarnings("serial")
public class TGDHPublicKey implements DSAPublicKey {

    private DSAParams params;

    private BigInteger y;

    public TGDHPublicKey() {
    }

    public TGDHPublicKey(DSAParams params, BigInteger y) {
        if (!(params instanceof Serializable)) {
            throw new IllegalArgumentException((new StringBuilder()).append(params.getClass().getName()).append(" isn't serializable").toString());
        } else {
            this.params = params;
            this.y = y;
            return;
        }
    }

    public BigInteger getY() {
        return y;
    }

    public void setParams(DSAParams params) {
        this.params = params;
    }

    public void setY(BigInteger y) {
        if (y.compareTo(getParams().getP()) >= 0) {
            throw new IllegalArgumentException("y greater than p");
        } else {
            this.y = y;
            return;
        }
    }

    public String getAlgorithm() {
        return "DSA";
    }

    public String getFormat() {
        return null;
    }

    public byte[] getEncoded() {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        try {
            bout.write(params.getP().toByteArray());
            bout.write(params.getQ().toByteArray());
            bout.write(params.getG().toByteArray());
            bout.write(y.toByteArray());
        } catch (IOException e) {
            return null;
        }
        return bout.toByteArray();
    }

    public String toString() {
        StringBuffer s = new StringBuffer();
        s.append("p: \n");
        s.append(params.getP().toString(16));
        s.append("\n");
        s.append("q: \n");
        s.append(params.getQ().toString(16));
        s.append("\n");
        s.append("g: \n");
        s.append(params.getG().toString(16));
        s.append("\n");
        s.append("y: \n");
        s.append(y.toString(16));
        s.append("\n");
        return s.toString();
    }

    public DSAParams getParams() {
        return params;
    }

    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof TGDHPublicKey)) {
            return false;
        } else {
            TGDHPublicKey key = (TGDHPublicKey) o;
            DSAParams tmp = key.getParams();
            return y.equals(key.y) && params.getP().equals(tmp.getP()) && params.getQ().equals(tmp.getQ()) && params.getG().equals(tmp.getG());
        }
    }
}
