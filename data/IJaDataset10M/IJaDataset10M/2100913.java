package org.ancora.MicroblazeInterpreter.Commons;

import org.ancora.jCommons.Console;
import org.ancora.jCommons.DefaultConsole;

/**
 * Contains static methods used in MicroBlaze instructions.
 *
 * @author Joao Bispo
 */
public class BitOperations {

    /**
     * Calculates the carryOut of the sum of rA with rB and carry.
     * Operation is rA + rB + carry.
     *
     * @param rA
     * @param rB
     * @param carry the carry from the previous operation. Should be 0 or 1.
     * @return 1 if there is carry out, or 0 if not.
     */
    public static int getCarryOutAdd(int rA, int rB, int carry) {
        if (carry != 0 && carry != 1) {
            console.warn("getCarryOut: Carry is different than 0 or 1 (" + carry + ")");
        }
        long lRa = rA & MASK_32_BITS;
        long lRb = rB & MASK_32_BITS;
        long lCarry = carry;
        long result = lRa + lRb + lCarry;
        int carryOut = (int) ((result & MASK_BIT_33) >>> 32);
        return carryOut;
    }

    /**
     * Calculates the carryOut of the reverse subtraction of rA with rB and 
     * carry. Operation is rB + ~rA + carry.
     *
     * @param rA
     * @param rB
     * @param carry the carry from the previous operation. Should be 0 or 1.
     * @return 1 if there is carry out, or 0 if not.
     */
    public static int getCarryOutRsub(int rA, int rB, int carry) {
        if (carry != 0 && carry != 1) {
            console.warn("getCarryOut: Carry is different than 0 or 1 (" + carry + ")");
        }
        long lRa = rA & MASK_32_BITS;
        long lRb = rB & MASK_32_BITS;
        long lCarry = carry;
        long result = lRb + ~lRa + lCarry;
        int carryOut = (int) ((result & MASK_BIT_33) >>> 32);
        return carryOut;
    }

    /**
     * Performs a 32-bit unsigned division.
     * 
     * @param a
     * @param b
     * @return
     */
    public static int unsignedDiv(int a, int b) {
        final long la = a & MASK_32_BITS;
        final long lb = b & MASK_32_BITS;
        return (int) (la / lb);
    }

    /**
     * Returns true if a is greater than b.
     * 
     * @param a
     * @param b
     * @return
     */
    public static boolean unsignedComp(int a, int b) {
        long longA = a & MASK_32_BITS;
        long longB = b & MASK_32_BITS;
        return longA > longB;
    }

    /**
    * Sets a specific bit of an int.
    *
    * @param bit the bit to set. The least significant bit is bit 0
    * @param target the integer where the bit will be set
    * @return the updated value of the target
    */
    public static int setBit(int bit, int target) {
        ;
        int mask = 1 << bit;
        return target | mask;
    }

    /**
    * Clears a specific bit of an int.
    *
    * @param bit the bit to clear. The least significant bit is bit 0
    * @param target the integer where the bit will be cleared
    * @return the updated value of the target
    */
    public static int clearBit(int bit, int target) {
        int mask = 1 << bit;
        return target & ~mask;
    }

    /**
    * Writes a value to a specific bit of an int.
    *
    * @param bit the bit to write
    * @param value 0 for clearing the bit, 1 for setting the bit
    * @param target the integer where the bit will be written
    * @return the updated value of the integer
    */
    public static int writeBit(int bit, int value, int target) {
        if (value == 0) {
            return clearBit(bit, target);
        } else if (value == 1) {
            return setBit(bit, target);
        } else {
            console.warn("writeBit: Value is not 1 or 0 (" + value + ")");
            return target;
        }
    }

    /**
    * Gets the a single bit of the target.
    * 
    * @param position
    * @param target
    * @return
    */
    public static int getBit(int position, int target) {
        return (target >>> position) & MASK_BIT_1;
    }

    /**
    * Writes a set of bits to an intenger. 
    * Only works for sizes between 1 and 31.
    *
    * @param position the least significat bit that will be written
    * @param size how many bits of the value, from the least significant bit,
    * will be used to write
    * @param value the bits to write
    * @param target the integer where the bits will be written
    * @return the updated value of the integer
    */
    public static int writeBits(int position, int size, int value, int target) {
        int clearMask;
        clearMask = 1 << size;
        clearMask -= 1;
        clearMask = clearMask << position;
        clearMask = ~clearMask;
        target = target & clearMask;
        value = value << position;
        value = value & ~clearMask;
        target = target | value;
        return target;
    }

    /**
    * Pads the string with zeros on the left until it has the requested size.
    *
    * @param binaryNumber
    * @param size
    * @return
    */
    public static String padBinaryString(String binaryNumber, int size) {
        int stringSize = binaryNumber.length();
        if (stringSize >= size) {
            return binaryNumber;
        }
        int numZeros = size - stringSize;
        StringBuilder builder = new StringBuilder(numZeros);
        for (int i = 0; i < numZeros; i++) {
            builder.append(ZERO);
        }
        return builder.toString() + binaryNumber;
    }

    /**
    * Pads the string with 0x and zeros on the left until it has the
    * requested size.
    *
    * @param hexNumber
    * @param size
    * @return
    */
    public static String padHexString(String hexNumber, int size) {
        int stringSize = hexNumber.length();
        if (stringSize >= size) {
            return hexNumber;
        }
        int numZeros = size - stringSize;
        StringBuilder builder = new StringBuilder(numZeros + HEX_PREFIX.length());
        builder.append(HEX_PREFIX);
        for (int i = 0; i < numZeros; i++) {
            builder.append(ZERO);
        }
        return builder.toString() + hexNumber;
    }

    private static final long MASK_32_BITS = 0xFFFFFFFFL;

    private static final long MASK_BIT_33 = 0x100000000L;

    private static final int MASK_BIT_1 = 0x1;

    private static final String ZERO = "0";

    private static final String HEX_PREFIX = "0x";

    private static final Console console = DefaultConsole.getConsole();
}
