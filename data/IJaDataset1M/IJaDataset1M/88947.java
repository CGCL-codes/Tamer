package tgdh.crypto;

import java.io.*;
import java.math.BigInteger;
import java.security.interfaces.DSAParams;

public class TgdhKeySpec implements DSAParams, Serializable {

    public TgdhKeySpec() {
    }

    public TgdhKeySpec(BigInteger p, BigInteger q, BigInteger g) {
        this.p = p;
        this.q = q;
        this.g = g;
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
            bout.write(getAlgorithm().getBytes());
            bout.write(p.toByteArray());
            bout.write(q.toByteArray());
            bout.write(g.toByteArray());
        } catch (IOException e) {
            return null;
        }
        return bout.toByteArray();
    }

    public DSAParams getParams() {
        return new TgdhKeySpec(p, q, g);
    }

    public String toString() {
        StringBuffer s = new StringBuffer();
        s.append("p:\n");
        s.append(p.toString());
        s.append("\n");
        s.append("q:\n");
        s.append(q.toString());
        s.append("\n");
        s.append("g:\n");
        s.append(g.toString());
        s.append("\n");
        return s.toString();
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

    private BigInteger p;

    private BigInteger q;

    private BigInteger g;
}
