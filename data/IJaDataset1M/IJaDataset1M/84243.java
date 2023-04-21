package com.sshtools.ext.openssh;

import java.io.IOException;
import java.math.BigInteger;
import java.security.spec.DSAPrivateKeySpec;
import java.security.spec.DSAPublicKeySpec;
import java.security.spec.KeySpec;
import com.sshtools.j2ssh.util.SimpleASNReader;
import com.sshtools.j2ssh.util.SimpleASNWriter;

/**
 *
 *
 * @author $author$
 * @version $Revision: 1.1 $
 */
public class DSAKeyInfo implements KeyInfo {

    private BigInteger p;

    private BigInteger q;

    private BigInteger g;

    private BigInteger x;

    private BigInteger y;

    /**
   * Creates a new DSAKeyInfo object.
   *
   * @param p
   * @param q
   * @param g
   * @param x
   * @param y
   */
    public DSAKeyInfo(BigInteger p, BigInteger q, BigInteger g, BigInteger x, BigInteger y) {
        this.p = p;
        this.q = q;
        this.g = g;
        this.x = x;
        this.y = y;
    }

    /**
   *
   *
   * @return
   */
    public BigInteger getG() {
        return g;
    }

    /**
   *
   *
   * @return
   */
    public BigInteger getP() {
        return p;
    }

    /**
   *
   *
   * @return
   */
    public BigInteger getQ() {
        return q;
    }

    /**
   *
   *
   * @return
   */
    public BigInteger getX() {
        return x;
    }

    /**
   *
   *
   * @return
   */
    public BigInteger getY() {
        return y;
    }

    /**
   *
   *
   * @return
   */
    public KeySpec getPrivateKeySpec() {
        return new DSAPrivateKeySpec(x, p, q, g);
    }

    /**
   *
   *
   * @return
   */
    public KeySpec getPublicKeySpec() {
        return new DSAPublicKeySpec(y, p, q, g);
    }

    /**
   *
   *
   * @param asn
   *
   * @return
   *
   * @throws IOException
   */
    public static DSAKeyInfo getDSAKeyInfo(SimpleASNReader asn) throws IOException {
        asn.assertByte(0x30);
        int length = asn.getLength();
        asn.assertByte(0x02);
        byte[] version = asn.getData();
        asn.assertByte(0x02);
        byte[] paramP = asn.getData();
        asn.assertByte(0x02);
        byte[] paramQ = asn.getData();
        asn.assertByte(0x02);
        byte[] paramG = asn.getData();
        asn.assertByte(0x02);
        byte[] paramY = asn.getData();
        asn.assertByte(0x02);
        byte[] paramX = asn.getData();
        return new DSAKeyInfo(new BigInteger(paramP), new BigInteger(paramQ), new BigInteger(paramG), new BigInteger(paramX), new BigInteger(paramY));
    }

    /**
   *
   *
   * @param asn
   * @param keyInfo
   */
    public static void writeDSAKeyInfo(SimpleASNWriter asn, DSAKeyInfo keyInfo) {
        SimpleASNWriter asn2 = new SimpleASNWriter();
        asn2.writeByte(0x02);
        byte[] version = new byte[1];
        asn2.writeData(version);
        asn2.writeByte(0x02);
        asn2.writeData(keyInfo.getP().toByteArray());
        asn2.writeByte(0x02);
        asn2.writeData(keyInfo.getQ().toByteArray());
        asn2.writeByte(0x02);
        asn2.writeData(keyInfo.getG().toByteArray());
        asn2.writeByte(0x02);
        asn2.writeData(keyInfo.getY().toByteArray());
        asn2.writeByte(0x02);
        asn2.writeData(keyInfo.getX().toByteArray());
        byte[] dsaKeyEncoded = asn2.toByteArray();
        asn.writeByte(0x30);
        asn.writeData(dsaKeyEncoded);
    }
}
