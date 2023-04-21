package ca.uwaterloo.crysp.otr.crypt.jca;

import java.math.BigInteger;

/**
 * Abstract DH key using the JCA provider. The g, l and p parameters as well as the 
 * actual key values are represented using BigInteger objects.
 * 
 * @author Andrew Chung (kachung@uwaterloo.ca)
 */
public abstract class JCADHKey extends ca.uwaterloo.crysp.otr.crypt.DHKey {

    private BigInteger g;

    private BigInteger p;

    public JCADHKey(BigInteger g, BigInteger p) {
        this.g = g;
        this.p = p;
    }

    public byte[] getG() {
        return JCAMPI.toBytes(g);
    }

    public byte[] getP() {
        return JCAMPI.toBytes(p);
    }
}
