package com.hifiremote.jp1;

public class LutronTranslator extends Translate {

    public LutronTranslator(String[] textParms) {
        super(textParms);
        deviceOrCommand = Integer.parseInt(textParms[0]);
    }

    public void in(Value[] parms, Hex hexData, DeviceParameter[] devParms, int onlyIndex) {
        if (deviceOrCommand == DEVICE) {
            int device = ((Integer) parms[0].getValue()).intValue();
            int temp = (device & 0xE0) >> 5;
            insert(hexData, 8, 4, encode[temp]);
            temp = (device & 0x1C) >> 2;
            insert(hexData, 12, 4, encode[temp]);
        } else {
            int device = ((Integer) devParms[0].getValueOrDefault()).intValue();
            if ((parms[1] != null) && (parms[1].getValue() != null)) {
                device &= 0xFC;
                device |= ((Integer) parms[1].getValue()).intValue();
            }
            int temp = device & 3;
            temp <<= 1;
            int obc = ((Integer) parms[0].getValue()).intValue();
            temp |= (obc & 0x80) >> 7;
            insert(hexData, 0, 4, encode[temp]);
            temp = (obc & 0x70) >> 4;
            insert(hexData, 4, 4, encode[temp]);
            temp = (obc & 0x0E) >> 1;
            insert(hexData, 8, 4, encode[temp]);
            temp = device ^ obc;
            int checksum = 0;
            checksum ^= (temp & 0x03);
            temp >>= 2;
            checksum ^= (temp & 0x03);
            temp >>= 2;
            checksum ^= (temp & 0x03);
            temp >>= 2;
            checksum ^= (temp & 0x03);
            temp = ((obc & 0x01) << 2);
            temp |= checksum;
            insert(hexData, 12, 4, encode[temp]);
        }
    }

    public void out(Hex hexData, Value[] parms, DeviceParameter[] devParms) {
        if (deviceOrCommand == DEVICE) {
            int temp = decode(extract(hexData, 8, 4));
            int device = temp << 5;
            temp = decode(extract(hexData, 12, 3));
            device |= temp << 2;
            parms[0] = new Value(new Integer(device));
        } else {
            int temp = decode(extract(hexData, 0, 4));
            int obc = (temp & 1) << 7;
            int device = temp >> 1;
            temp = decode(extract(hexData, 4, 4));
            obc |= temp << 4;
            temp = decode(extract(hexData, 8, 4));
            obc |= temp << 1;
            temp = decode(extract(hexData, 12, 4));
            obc |= temp >> 2;
            parms[0] = new Value(new Integer(obc));
            parms[1] = new Value(new Integer(device));
        }
    }

    private int decode(int val) {
        for (int i = 0; i < encode.length; i++) {
            if (encode[i] == val) return i;
        }
        System.err.println("LutronTranslator.decode( " + val + " ) failed!");
        return -1;
    }

    private static int[] encode = { 1, 2, 7, 4, 13, 14, 11, 8 };

    private int deviceOrCommand = 0;

    private static final int DEVICE = 0;

    private static final int COMMAND = 1;
}
