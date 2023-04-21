package com.cyberoblivion.util;

import org.apache.log4j.Logger;

/**
 * Describe class BytesToBits here.  This is a utility class which has
 * many helpful function when dealing with bits and byte in java
 *
 * Created: Mon Feb  6 23:25:33 2006
 *
 * @author <a href="mailto:bene@velvet.cyberoblivion.com">Ben Erridge</a>
 * @version 1.0
 */
public class BitByteUtils {

    /**
    *  variable <code>logger</code> .
    *  the logger to display faults, errore, etc
    */
    public static Logger logger = Logger.getLogger(BitByteUtils.class);

    /**
    *  constant <code>BITS_IN_BYTES</code> .  how many bits are in a
    *  byte on this system. can probably get this from a system
    *  variabl but for now this will do
    */
    public static final int BITS_IN_BYTES = 8;

    /**
    * Describe constant <code>LEAST_SIG_MASK</code> here.  mask to get
    * the least significat byte out of a 16bit variable
    */
    public static final int LEAST_SIG_MASK = 255;

    /**
    * Describe constant <code>MOST_SIG_MASK</code> here.  mask to get
    * the most significant byte out of a 16bit variable
    */
    public static final int MOST_SIG_MASK = 65280;

    /**
    *  constant <code>BIG_ENDIAN</code> .  this is used for byte and
    * bit orders for some functions.
    */
    public static final int BIG_ENDIAN = 1;

    /**
    *  constant <code>LITTLE_ENDIAN</code> .  this is used for byte
    * and bit orders for some functions.
    */
    public static final int LITTLE_ENDIAN = 0;

    /**
    * Creates a new <code>BitByteUtils</code> instance.
    *
    */
    public BitByteUtils() {
    }

    /**
    *  <code>getIntFromBytes</code> method . There is probably a
    *  better wasy to convert these 2 bytes into 1 int but I can't
    *  think of a way without the dumb signing of java getting in the
    *  way
    *
    * @param highByte a <code>byte</code> value
    * @param lowByte a <code>byte</code> value
    * @return an <code>int</code> value the bytes combined into an int
    * ignoring any signing
    */
    public static int getIntFromBytes(byte highByte, byte lowByte) {
        int intValue = 0;
        int i;
        boolean[] bHighByte = getBits(highByte, BIG_ENDIAN);
        boolean[] bLowByte = getBits(lowByte, BIG_ENDIAN);
        for (i = 0; i < (BITS_IN_BYTES); i++) {
            intValue += (bHighByte[i] ? 1 : 0) << (15 - i);
        }
        for (i = 0; i < (BITS_IN_BYTES); i++) {
            intValue += (bLowByte[i] ? 1 : 0) << (7 - i);
        }
        return intValue;
    }

    /**
    * <code>getBits</code> method . This will give you back a boolean
    *array from an array of bytes TODO make endian sensitive
    *
    * @param bytes a <code>byte[]</code> value
    * @return a <code>boolean[]</code> value an array of booleans from
    * the supplied byte array
    */
    public static boolean[] getBits(byte[] bytes) {
        boolean bits[] = new boolean[bytes.length * BITS_IN_BYTES];
        byte tmp = 0;
        int i, j, n = 0;
        for (i = 0; i < bytes.length; i++) {
            for (j = BITS_IN_BYTES - 1; j >= 0; j--) {
                tmp = 0;
                tmp = (byte) ((byte) bytes[i] & (byte) Math.pow(2, j));
                if (tmp == 0) {
                    bits[n] = false;
                } else {
                    bits[n] = true;
                }
                n++;
            }
        }
        return bits;
    }

    /**
    *  <code>getBits</code> method . This will give you a boolean
    *  array back from a byte, you can specify the bit endianness
    *
    * @param theByte a <code>byte</code> value the byte to parse into
    * a boolean array
    * @param bitOrder an <code>int</code> value. whether the most
    * significant bit is first in the boolean array or last
    * @return a <code>boolean[]</code> value
    */
    public static boolean[] getBits(byte theByte, int bitOrder) {
        boolean bits[] = new boolean[BITS_IN_BYTES];
        byte tmp = 0;
        int j, i;
        if (bitOrder == BIG_ENDIAN) {
            for (j = 0, i = (BITS_IN_BYTES - 1); j < BITS_IN_BYTES; j++, i--) {
                tmp = 0;
                tmp = (byte) ((byte) theByte & (byte) Math.pow(2, j));
                if (tmp == 0) {
                    bits[i] = false;
                } else {
                    bits[i] = true;
                }
            }
        } else if (bitOrder == LITTLE_ENDIAN) {
            for (j = BITS_IN_BYTES - 1; j >= 0; j--) {
                tmp = 0;
                tmp = (byte) ((byte) theByte & (byte) Math.pow(2, j));
                if (tmp == 0) {
                    bits[j] = false;
                } else {
                    bits[j] = true;
                }
            }
        }
        return bits;
    }

    /**
    *  <code>printBits</code> method .
    *
    * @param bits 
    */
    public static void printBits(boolean bits[]) {
        int i = 0;
        logger.info("Bits Are");
        String debugInfo;
        for (i = 0; i < bits.length; i++) {
            logger.info("Bit " + i + " = " + bits[i] + "");
        }
    }

    /**
    *  <code>getHighLowBytes</code> method .  turns an int into a byte
    *  array 2 elements deep
    * @param a an <code>int</code> value
    * @return a <code>byte[]</code> value the high and low bytes of
    * the int. The high byte is at location 0 in the array
    */
    public static byte[] getHighLowBytes(int a) {
        byte bytes[] = new byte[2];
        bytes[0] = (byte) ((a & MOST_SIG_MASK) >> BITS_IN_BYTES);
        bytes[1] = (byte) (((a) & LEAST_SIG_MASK));
        return bytes;
    }

    /**
    *  <code>getHighByte</code> method .
    * gets the high byte of an int
    * @param a an <code>int</code> value
    * @return a <code>byte</code> value returns the high byte of an
    * int as a byte
    */
    public static byte getHighByte(int a) {
        return (byte) ((a & MOST_SIG_MASK) >> BITS_IN_BYTES);
    }

    /**
    *  <code>getLowByte</code> method .  gets the low byte of an int
    * and returns it as a byte
    * @param a an <code>int</code> value
    * @return a <code>byte</code> value the low byt of the supplied
    * int as a byte
    */
    public static byte getLowByte(int a) {
        return (byte) (((a) & LEAST_SIG_MASK));
    }

    /**
    *  <code>setBit</code> method .  this function will set a
    * particular bit in an array of bytes. this is 0 based
    * @param bytes 
    * @param bitOffset an <code>int</code> value the bit to set to the
    * value
    * @param value a <code>boolean</code> value
    */
    public static void setBit(byte bytes[], int bitOffset, boolean value) {
        int byteOffset = 0;
        if (bitOffset > BITS_IN_BYTES) {
            byteOffset = (int) (bitOffset / BITS_IN_BYTES);
        }
        bitOffset = (BITS_IN_BYTES - 1) - (bitOffset % BITS_IN_BYTES);
        bytes[byteOffset] = getByteToValue(bytes[byteOffset], bitOffset, value);
    }

    /**
    *  <code>getByteToValue</code> method .  This function sets a
    * particular bit in a byte and returns the resulting byte
    * @param theByte a <code>byte</code> value the byte to modify
    * @param bitOffset an <code>int</code> value the bit to set this
    * is zero based
    * @param value a <code>boolean</code> value to set the bit to
    * @return a <code>byte</code> value the resulting byte after the
    * bit has been modified
    */
    public static byte getByteToValue(byte theByte, int bitOffset, boolean value) {
        if (bitOffset <= BITS_IN_BYTES) {
            if (value) {
                theByte = (byte) ((int) theByte | (int) (Math.pow(2, bitOffset)));
            } else {
                theByte = (byte) ((int) theByte & (int) (255 - (Math.pow(2, bitOffset))));
            }
        }
        return theByte;
    }
}
