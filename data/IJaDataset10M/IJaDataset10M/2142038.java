package tgdh;

import java.io.NotSerializableException;
import java.io.Serializable;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * The LeafMember class provides the details for the TGDH member nodes
 * 
 * @author Gilbert, Paresh, Sanket
 * 
 */
@SuppressWarnings("serial")
public class LeafMember extends TGDHNode {

    protected transient PrivateKey signKey;

    protected PublicKey verifyKey;

    protected Integer name;

    public LeafMember() {
    }

    public LeafMember(int name) {
        this.name = name;
    }

    public String getName() {
        return name.toString();
    }

    public PublicKey getVerifyKey() {
        return verifyKey;
    }

    public PrivateKey getSignKey() {
        return signKey;
    }

    public void setSignKey(PrivateKey signKey) throws NotSerializableException {
        if (signKey instanceof Serializable) this.signKey = signKey; else throw new NotSerializableException();
    }

    public void setVerifyKey(PublicKey verifyKey) throws NotSerializableException {
        if (verifyKey instanceof Serializable) this.verifyKey = verifyKey; else throw new NotSerializableException();
    }

    public String toString() {
        return (new StringBuilder()).append("<").append(getCoordinate().getLevel()).append(",").append(getCoordinate().getOrdinal()).append(">(").append(getName()).append(")").toString();
    }

    public byte[] toByteArray() {
        byte a[] = super.toByteArray();
        byte b[] = new byte[] { name.byteValue() };
        byte c[] = new byte[a.length + b.length];
        System.arraycopy(a, 0, c, 0, a.length);
        System.arraycopy(b, 0, c, a.length, b.length);
        return c;
    }
}
