package ru.caffeineim.protocols.icq;

import ru.caffeineim.protocols.icq.exceptions.RawDataBadForcedLenghtException;

/**
 * <p>Created by
 *   @author Fabrice Michellonet
 */
public class RawData extends DataField {

    /** Byte length representation  */
    public static final int BYTE_LENGHT = 1;

    /** Word length representation  */
    public static final int WORD_LENGHT = 2;

    /** Double Word length representation  */
    public static final int DWORD_LENGHT = 4;

    private int value;

    private int forcedLenght = 0;

    private String stringValue = null;

    private boolean reversed = false;

    /**
	 * Construct a RawData containing a numeric value, but affect that value into the specified
	 * type of variable [byte, Word, DWord].
	 *
	 * @param value The RawData's numeric value.
	 * @param forcedLenght The type of variable.
	 */
    public RawData(int value, int forcedLenght) {
        this.value = value;
        this.forcedLenght = forcedLenght;
        long maxValue = 0;
        for (int i = 0; i < forcedLenght; i++) {
            maxValue = ((maxValue << 8) | 0xFF);
        }
        if (value <= maxValue) insert(value, forcedLenght); else {
            throw new RawDataBadForcedLenghtException(value + " cannot fit in " + forcedLenght + " Byte(s).");
        }
    }

    /**
	 * Construct a RawData containing a numeric value.
	 *
	 * @param value The RawData's numeric value.
	 */
    public RawData(int value) {
        this.value = value;
        long maxValue = 0xFF;
        int i = 1;
        while (maxValue < value) {
            maxValue = ((maxValue << 8) | 0xFF);
            i++;
        }
        this.insert(value, i);
    }

    /**
	 * Construct a RawData containing a String.
	 *
	 * @param string The RawData's String value.
	 */
    public RawData(String string) {
        stringValue = string;
        insertString(string);
    }

    public RawData(byte[] array) {
        this.byteArray = array;
        this.value = getByte(array);
        this.stringValue = new String(byteArray);
    }

    public RawData(byte[] value, int start, int len) {
        byte[] tmp = new byte[len];
        System.arraycopy(value, start, tmp, 0, len);
        byteArray = tmp;
        this.value = getByte(tmp);
        this.stringValue = new String(tmp);
    }

    private static int getByte(byte[] array) {
        int result = 0;
        for (int i = 0; i < array.length; i++) {
            long shift = ((array.length - (i + 1)) * 8);
            long mask = 0xFF << shift;
            result |= (array[i] << shift) & mask;
        }
        return result;
    }

    private void insert(int value, int len) {
        byteArray = new byte[len];
        for (int i = 0; i < len; i++) {
            byteArray[i] = (byte) ((value >> (((len - 1) - i) * 8)) & 0xFF);
        }
    }

    private void insertString(String string) {
        byteArray = new byte[string.length()];
        for (int i = 0; i < string.length(); i++) {
            byteArray[i] = (byte) string.charAt(i);
        }
    }

    public void invertIndianness() {
        byte[] tmp = new byte[byteArray.length];
        for (int i = 0; i < byteArray.length; i++) {
            tmp[i] = byteArray[byteArray.length - 1 - i];
        }
        this.byteArray = tmp;
        reversed = true;
    }

    /**
	 *
	 * @return The arithmetic value of the RawData.
	 */
    public int getValue() {
        if (reversed) {
            value = 0;
            for (int i = 0; i < byteArray.length; i++) {
                value |= (byteArray[i] << (byteArray.length - 1 - i) * 8) & 0xFFL << (byteArray.length - 1 - i) * 8;
            }
            reversed = false;
        }
        return value;
    }

    /**
	 *
	 * @return The data's length corresponding value;
	 */
    public int getForcedLenght() {
        return forcedLenght;
    }

    /**
	 *
	 * @return The RawData's String value.
	 */
    public String getStringValue() {
        if ((stringValue == null) || (reversed == true)) {
            stringValue = new String(byteArray);
        }
        reversed = false;
        return stringValue;
    }

    /**
	 * 
	 * @return The integer RawData as String value
	 */
    public String toStringValue() {
        return String.valueOf(getValue());
    }

    /**
	 *
	 * @return The RawData's byte array representation.
	 */
    public byte[] getByteArray() {
        return byteArray;
    }
}
