package com.hifiremote.jp1.translate;

import com.hifiremote.jp1.DeviceParameter;
import com.hifiremote.jp1.Hex;
import com.hifiremote.jp1.Value;

/**
 * The Class Pioneer3DevXlator.
 */
public class Pioneer3DevXlator extends Translator {

    /** The dev index. */
    private static int devIndex = 0;

    /** The obc index. */
    private static int obcIndex = 1;

    /** The obc2 index. */
    private static int obc2Index = 2;

    /**
   * Instantiates a new pioneer3 dev xlator.
   * 
   * @param textParms
   *          the text parms
   */
    public Pioneer3DevXlator(String[] textParms) {
        super(textParms);
    }

    /**
   * Gets the device.
   * 
   * @param hex
   *          the hex
   * @return the device
   */
    private int getDevice(Hex hex) {
        int temp = extract(hex, 8, 3);
        if (temp == 4) {
            return 0;
        }
        if (temp == 2) {
            return 1;
        }
        return 2;
    }

    /**
   * Gets the device.
   * 
   * @param parms
   *          the parms
   * @return the device
   */
    private int getDevice(Value[] parms) {
        if (parms[devIndex] == null || parms[devIndex].getValue() == null) {
            return 0;
        }
        return ((Number) parms[devIndex].getValue()).intValue();
    }

    /**
   * Sets the device.
   * 
   * @param device
   *          the device
   * @param hex
   *          the hex
   */
    private void setDevice(int device, Hex hex) {
        int temp = 1 << 2 - device;
        insert(hex, 8, 3, temp);
    }

    /**
   * Gets the obc.
   * 
   * @param hex
   *          the hex
   * @return the obc
   */
    private int getObc(Hex hex) {
        return reverse(extract(hex, 0, 8));
    }

    /**
   * Gets the obc.
   * 
   * @param parms
   *          the parms
   * @return the obc
   */
    private int getObc(Value[] parms) {
        return ((Number) parms[obcIndex].getValue()).intValue();
    }

    /**
   * Sets the obc.
   * 
   * @param obc
   *          the obc
   * @param hex
   *          the hex
   */
    private void setObc(int obc, Hex hex) {
        insert(hex, 0, 8, reverse(obc));
    }

    /**
   * Adjust.
   * 
   * @param obc
   *          the obc
   * @param obc2
   *          the obc2
   * @return the int
   */
    private int adjust(int obc, int obc2) {
        if ((obc & 0x80) != 0) {
            obc2 += 0x80;
        }
        if ((obc & 0x40) == 0) {
            obc2 += 0x40;
        }
        return obc2;
    }

    /**
   * Gets the obc2.
   * 
   * @param hex
   *          the hex
   * @return the obc2
   */
    private Integer getObc2(Hex hex) {
        int obc2 = reverse(extract(hex, 11, 5), 5);
        if (obc2 == 0) {
            return null;
        } else {
            int obc = getObc(hex);
            return new Integer(adjust(obc, obc2));
        }
    }

    /**
   * Gets the obc2.
   * 
   * @param parms
   *          the parms
   * @return the obc2
   */
    private Number getObc2(Value[] parms) {
        if (parms[obc2Index] == null || parms[obc2Index].getValue() == null) {
            return null;
        }
        return (Number) parms[obc2Index].getValue();
    }

    /**
   * Sets the obc2.
   * 
   * @param obc2
   *          the obc2
   * @param hex
   *          the hex
   */
    private void setObc2(Number obc2, Hex hex) {
        if (obc2 != null) {
            int val = obc2.intValue();
            insert(hex, 11, 5, reverse(val, 5));
            Integer temp = getObc2(hex);
            if (!obc2.equals(temp)) {
                throw new IllegalArgumentException("OBC=" + getObc(hex) + " and OBC2=" + val + " can not be sent using Pioneer 3DEV. Use Pioneer 4DEV instead.");
            }
        } else {
            insert(hex, 11, 5, 0);
        }
    }

    @Override
    public void in(Value[] parms, Hex hex, DeviceParameter[] devParms, int onlyIndex) {
        boolean doAll = onlyIndex < 0;
        if (onlyIndex == devIndex || doAll) {
            setDevice(getDevice(parms), hex);
        }
        if (onlyIndex == obcIndex || doAll) {
            int obc = getObc(parms);
            setObc(obc, hex);
        }
        if (onlyIndex == obc2Index || doAll) {
            setObc2(getObc2(parms), hex);
        }
    }

    @Override
    public void out(Hex hex, Value[] parms, DeviceParameter[] devParms) {
        parms[devIndex] = new Value(new Integer(getDevice(hex)));
        int obc = getObc(hex);
        parms[obcIndex] = new Value(new Integer(obc));
        Integer obc2 = getObc2(hex);
        parms[obc2Index] = new Value(obc2);
    }
}
