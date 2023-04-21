package OgreIM;

import java.math.BigInteger;
import java.security.SecureRandom;
import OgreIM.Util.OU;

public class OgreNodePacket extends OgrePacket {

    public OgreNodePacket(OgrePacket O) {
    }

    public OgreNodePacket(byte[] msg) {
        super(msg, (byte) 0, (byte) OU.msgTyps.SEL_REQ, 0);
    }

    public OgreNodePacket(byte[] msg, byte t, byte type, int id, boolean data, byte subtype) {
        super(msg, t, type, id, data, subtype);
    }

    public OgreNodePacket(byte[] msg, String ipaddress) {
        super(msg, ipaddress);
    }

    public void msgSetUp(int numHops) {
        SecureRandom randGen = new SecureRandom();
        byte[] nonce = new byte[16];
        randGen.nextBytes(nonce);
        byte[] myHops = new BigInteger(numHops + "").toByteArray();
        int offset = 0;
        if (type() == OU.msgTyps.LINK_DATA) offset += 16;
        System.arraycopy(nonce, 0, data, header_size + offset, nonce.length);
        System.arraycopy(myHops, 0, data, nonce.length + header_size + offset, myHops.length);
        this.updateLen(nonce.length + myHops.length + offset);
        this.updateCheckSum();
    }

    public int getNSel() {
        int offset = 0;
        if (type() == OU.msgTyps.LINK_DATA) offset += 16;
        byte[] nSel = new byte[1];
        System.arraycopy(data, header_size + 16 + offset, nSel, 0, 1);
        return new BigInteger(nSel).intValue();
    }

    public byte[] getNSelByte() {
        byte[] nSel = new byte[1];
        int offset = 0;
        if (type() == OU.msgTyps.LINK_DATA) offset += 16;
        System.arraycopy(data, header_size + 16 + offset, nSel, 0, 1);
        return nSel;
    }

    public byte[] getNonce() {
        byte[] nonce = new byte[16];
        int offset = 0;
        if (type() == OU.msgTyps.LINK_DATA) offset += 16;
        System.arraycopy(data, header_size + offset, nonce, 0, 16);
        return nonce;
    }

    public int getRealSelection() {
        byte[] realSel = new byte[1];
        int offset = 0;
        if (type() == OU.msgTyps.LINK_DATA) offset += 16;
        System.arraycopy(data, header_size + 17 + offset, realSel, 0, 1);
        return new BigInteger(realSel).intValue();
    }
}
