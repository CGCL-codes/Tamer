package matheus.jparallelport.gui;

public class Bits {

    public boolean[] data = new boolean[8];

    public Bits() {
    }

    public Bits(byte xorMask, byte andMask, int shiftMask) {
        this.xorMask = xorMask;
        this.andMask = andMask;
        this.shiftMask = shiftMask;
    }

    public Bits(byte xorMask, byte andMask) {
        this.xorMask = xorMask;
        this.andMask = andMask;
    }

    public Bits(byte xorMask) {
        this.xorMask = xorMask;
    }

    /**
	 * 1st applied mask
	 */
    public byte xorMask = 0;

    /**
	 * 2nd applied mask
	 */
    public byte andMask = (byte) 0xff;

    /**
	 * 3rd applied mask - Shift right when reading from port and left to write
	 * it
	 */
    public int shiftMask = 0;

    public byte toByte() {
        byte b = 0;
        for (int i = 0; i < 8; i++) {
            if (data[i]) {
                b |= 1 << i;
            }
        }
        return b;
    }

    public byte toByteMascarade() {
        return (byte) (((toByte() ^ xorMask) & andMask) >> shiftMask);
    }

    public String toHexString() {
        return String.format("%X", toByte());
    }

    public String toHexStringMacarade() {
        return String.format("%X", toByteMascarade());
    }

    public String toDecString() {
        return String.format("%d", toByte());
    }

    public String toDecStringMascarade() {
        return String.format("%d", toByteMascarade());
    }

    public void setBits(byte b) {
        for (int i = 0; i < 8; i++) {
            data[i] = ((b >> i) & 0x01) == 0x01;
        }
    }

    public void setBitsMacarade(byte b) {
        setBits((byte) (((b ^ xorMask) & andMask) << shiftMask));
    }

    public void fromDecString(String decstr) {
        if (decstr.length() == 0) setBits((byte) 0); else {
            try {
                int i = Integer.parseInt(decstr);
                System.out.println(i);
                setBits((byte) i);
            } catch (Exception e) {
            }
        }
    }

    public void fromDecStringMascarade(String decstr) {
        if (decstr.length() == 0) setBitsMacarade((byte) 0); else {
            try {
                int i = Integer.parseInt(decstr);
                System.out.println(i);
                setBitsMacarade((byte) i);
            } catch (Exception e) {
            }
        }
    }

    public void fromHexString(String hexstr) {
        if (hexstr.length() == 0) setBits((byte) 0); else {
            try {
                int i = Integer.parseInt(hexstr, 16);
                System.out.println(i);
                setBits((byte) i);
            } catch (Exception e) {
            }
        }
    }

    public void fromHexStringMascarade(String hexstr) {
        if (hexstr.length() == 0) setBitsMacarade((byte) 0); else {
            try {
                int i = Integer.parseInt(hexstr, 16);
                System.out.println(i);
                setBitsMacarade((byte) i);
            } catch (Exception e) {
            }
        }
    }
}
