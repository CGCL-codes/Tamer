package gnujatella.gnutella;

import java.util.Random;
import gnujatella.utils.HexDec;

public class GUID {

    public static final int DATA_LENGTH = 16;

    public static Random sRandom = new Random();

    private byte[] mBytes;

    private Integer mHashCode;

    public GUID() {
        mBytes = new byte[DATA_LENGTH];
        sRandom.nextBytes(mBytes);
        computeHashCode();
    }

    public GUID(byte[] guidBytes) {
        mBytes = new byte[DATA_LENGTH];
        System.arraycopy(guidBytes, 0, mBytes, 0, DATA_LENGTH);
        computeHashCode();
    }

    public void setGuid(byte[] guidBytes) {
        System.arraycopy(guidBytes, 0, mBytes, 0, DATA_LENGTH);
        computeHashCode();
    }

    public byte[] getGuid() {
        return mBytes;
    }

    public Integer getHashCode() {
        return mHashCode;
    }

    public boolean equals(GUID b) {
        for (int i = 0; i < DATA_LENGTH; i++) {
            if (mBytes[i] != b.mBytes[i]) return false;
        }
        return true;
    }

    private void computeHashCode() {
        int hashedValue;
        int value;
        int v1, v2, v3, v4;
        v1 = (((int) mBytes[0]) < 0 ? ((int) mBytes[0]) + 256 : ((int) mBytes[0]));
        v2 = (((int) mBytes[1]) < 0 ? ((int) mBytes[1]) + 256 : ((int) mBytes[1]));
        v3 = (((int) mBytes[2]) < 0 ? ((int) mBytes[2]) + 256 : ((int) mBytes[2]));
        v4 = (((int) mBytes[3]) < 0 ? ((int) mBytes[3]) + 256 : ((int) mBytes[3]));
        hashedValue = (v1 << 24) | (v2 << 16) | (v3 << 8) | (v4);
        for (int i = 4; i < DATA_LENGTH; i += 4) {
            v1 = (((int) mBytes[i + 0]) < 0 ? ((int) mBytes[i + 0]) + 256 : ((int) mBytes[i + 0]));
            v2 = (((int) mBytes[i + 1]) < 0 ? ((int) mBytes[i + 1]) + 256 : ((int) mBytes[i + 1]));
            v3 = (((int) mBytes[i + 2]) < 0 ? ((int) mBytes[i + 2]) + 256 : ((int) mBytes[i + 2]));
            v4 = (((int) mBytes[i + 3]) < 0 ? ((int) mBytes[i + 3]) + 256 : ((int) mBytes[i + 3]));
            value = (v1 << 24) | (v2 << 16) | (v3 << 8) | (v4);
            hashedValue ^= value;
        }
        mHashCode = new Integer(hashedValue);
    }

    public int getSize() {
        return DATA_LENGTH;
    }

    public void copy(byte[] guidBytes) {
        System.arraycopy(guidBytes, 0, mBytes, 0, DATA_LENGTH);
        computeHashCode();
    }

    public int serialize(byte[] outbuf, int offset) throws Exception {
        System.arraycopy(mBytes, 0, outbuf, offset, DATA_LENGTH);
        return offset + DATA_LENGTH;
    }

    public int deserialize(byte[] inbuf, int offset) {
        System.arraycopy(inbuf, offset, mBytes, 0, DATA_LENGTH);
        computeHashCode();
        return offset + DATA_LENGTH;
    }

    public String toString() {
        return generateString();
    }

    private String generateString() {
        String guidStr = "";
        guidStr += HexDec.convertBytesToHexString(mBytes, 0, 4) + "-";
        guidStr += HexDec.convertBytesToHexString(mBytes, 4, 4) + "-";
        guidStr += HexDec.convertBytesToHexString(mBytes, 8, 4) + "-";
        guidStr += HexDec.convertBytesToHexString(mBytes, 12, 4);
        return guidStr;
    }

    public String toHexString() {
        return HexDec.convertBytesToHexString(mBytes);
    }

    public void fromHexString(String hexValue) throws Exception {
        mBytes = HexDec.convertHexStringToBytes(hexValue);
        computeHashCode();
    }

    public int hashCode() {
        return mHashCode.intValue();
    }

    public boolean equals(Object o) {
        if (o instanceof GUID) if (this.hashCode() == o.hashCode()) return true;
        return false;
    }
}
