package eu.more.measurementservice.datareaders;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

/**
 * Data storage class for measured values.
 * <p>
 * This class stores current measurement and translate bytecode sequences into a
 * human readable format.
 * </p>
 * 
 * @author pdaum
 * 
 */
public class SensorRecord {

    private byte gas;

    private byte unit;

    private byte pot;

    private short state;

    private int value;

    /**
   * Creates a new {@link SensorRecord} with given values.
   * 
   * @param byteValue
   */
    public SensorRecord(byte[] byteValue) {
        if (byteValue == null || byteValue.length != 7) throw new IllegalArgumentException("The byte-array must have a length of 7.");
        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(byteValue));
        try {
            gas = dis.readByte();
            unit = dis.readByte();
            pot = dis.readByte();
            state = dis.readShort();
            value = dis.readUnsignedShort();
            dis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
   * Returns current measured gas as a human readable string.
   * 
   * @return current measured gas
   */
    public String getGas() {
        switch(gas) {
            case 59:
                return "CH4";
            case 89:
                return "O2";
            case -6:
                return "Temp1";
            case -5:
                return "Temp2";
            case -4:
                return "Temp3";
            case -8:
                return "Batt";
            case -7:
                return "Pump";
            default:
                return "";
        }
    }

    /**
   * Return the current unit as a human readable string.
   * 
   * @return current Unit
   */
    public String getUnit() {
        boolean b = (unit >= 0 && unit < GassensorConstants.UNITS.length);
        return b ? GassensorConstants.UNITS[unit] : "";
    }

    /**
   * Returns current measured value.
   * 
   * @return current value
   */
    public double getValue() {
        return value * Math.pow(10, pot);
    }

    /**
   * Returns current state of sensor device.
   * 
   * @return current state
   */
    public short getState() {
        return state;
    }
}
