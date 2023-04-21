package pl.edu.pjwstk.p2pp.util;

/**
 * Utility class for various operations on bytes.
 * 
 * @author Maciej Skorupka s3874@pjwstk.edu.pl
 * 
 */
public final class ByteUtils {

    private static final String INDEX_OUT_OF_BOUNDS_MESSAGE = "You're trying " + "to write some bits before or after the array.";

    /**
	 * This method adds byte to an array of bytes at bit index given as a second argument. For example, we have an array
	 * of bytes as follows:<br>
	 * &nbsp 00000000 00000000<br>
	 * and this method is invoked with 11110101 b argument and bitIndex is 4. After that method array will have this
	 * value:<br>
	 * &nbsp 00001111 01010000<br>
	 * If you'll try to add a byte after or before an array, ArrayIndexOutOfBoundsException is thrown. If array is null,
	 * NullPointerException is thrown.
	 * 
	 * TODO TEST
	 * 
	 * @param b
	 *            Byte to be added to an array.
	 * @param array
	 *            Array of bytes which will contain b after this method.
	 * @param bitIndex
	 *            Index of bit at which the the first bit of byte will be added. Bits in byte are indexed from 0 to 7
	 *            starting at most significant.
	 * @throws ArrayIndexOutOfBoundsException
	 * 
	 */
    public static void addByteToArrayAtBitIndex(byte b, byte[] array, int bitIndex) {
        if (array == null) {
            throw new NullPointerException("Byte array is null.");
        }
        if (bitIndex + 8 > (array.length * 8) || bitIndex < 0) {
            throw new ArrayIndexOutOfBoundsException(INDEX_OUT_OF_BOUNDS_MESSAGE);
        }
        int byteIndex = (int) (bitIndex / 8);
        byte indexOfBitInFirstByte = (byte) (bitIndex % 8);
        byte firstByte = array[byteIndex];
        byte newByte = copyBitsToByte(b, firstByte, 0, indexOfBitInFirstByte);
        array[byteIndex] = newByte;
        if (indexOfBitInFirstByte > 0) {
            array[byteIndex + 1] = copyBitsToByte(b, array[byteIndex + 1], 8 - indexOfBitInFirstByte, 0);
        }
    }

    /**
	 * Copies bits of first byte (starting at firstIndex) in second byte (starting at secondIndex). Indexing of bits is
	 * from 0 to 7. 0 is most significant bit and 7 is the least one. <br>
	 * Easiest way for explaining this method is by example. For first byte=11110101, second=00000000, firstIndex=2 and
	 * secondIndex=4 this method will return byte of value=00001101. In second byte bits after the fourth one were
	 * changed to those from first byte after second index.<br>
	 * If firstIndex or secondIndex are below 0 or above 7 this method will return the second byte. TODO TEST
	 * 
	 * @param first
	 *            Byte whose bits are to be copied.
	 * @param second
	 *            Byte whose bits are to be changed.
	 * @param firstIndex
	 *            First byte's index at which the copying will start.
	 * @param secondIndex
	 *            Second byte's index at which the copying will start.
	 */
    public static byte copyBitsToByte(byte first, byte second, int firstIndex, int secondIndex) {
        if (firstIndex < 0 || firstIndex > 7 || secondIndex < 0 || secondIndex > 7) {
            return second;
        }
        int secondChanged = 0;
        int firstAsInt = first & 0xFF;
        if (secondIndex > 0) {
            for (int i = 0; i < secondIndex; i++) {
                secondChanged += (int) Math.pow(2, 8 - i - 1) * ((second >>> 8 - i - 1) & 0x01);
            }
        }
        while (firstIndex < 8 && secondIndex < 8) {
            int firstShift = 8 - firstIndex - 1;
            int secondShift = 8 - secondIndex - 1;
            int currentBitInFirst = (firstAsInt >>> firstShift) & 0x01;
            int pow = (int) Math.pow(2, secondShift);
            if (currentBitInFirst == 1) {
                secondChanged += pow;
            }
            firstIndex++;
            secondIndex++;
        }
        if (firstIndex > secondIndex) {
            secondChanged = copyBitsToByte(second, (byte) secondChanged, secondIndex, secondIndex);
        }
        return (byte) secondChanged;
    }

    /**
	 * This method adds integer to an array of bytes at bit index given as a second argument. For example, we have an
	 * array of bytes as follows:<br>
	 * &nbsp 00000000 00000000 00000000 00000000 00000000<br>
	 * and this method is invoked with 11111111 11111111 11111111 11111111 integer argument and bitIndex is 4. After
	 * that method array will have this value:<br>
	 * &nbsp 00001111 11111111 11111111 11111111 11110000<br>
	 * If you'll try to add a byte after or before an array, ArrayIndexOutOfBoundsException is thrown and no changes are
	 * made in given array. If array is null, NullPointerException is thrown.
	 * 
	 * TODO Czy na pewno tak ma dodawać? Jak bajty inta mają być wobec siebie ustawione? TEST
	 * 
	 * @param integer
	 *            Integer to be added to an array.
	 * @param array
	 *            Array of bytes which will contain integer after this method.
	 * @param bitIndex
	 *            Index of bit at which the the first bit of integer will be added. Bits in integer are indexed from 0
	 *            to 31 starting at most significant.
	 * @throws {@link ArrayIndexOutOfBoundsException}
	 * @throws {@link NullPointerException}
	 * 
	 */
    public static void addIntToArrayAtBitIndex(int integer, byte[] array, int bitIndex) {
        if (bitIndex + 32 > array.length * 8) {
            throw new ArrayIndexOutOfBoundsException(INDEX_OUT_OF_BOUNDS_MESSAGE);
        }
        for (int i = 0; i < 4; i++) {
            byte b = (byte) ((integer >>> (4 - i - 1) * 8) & 0xFF);
            addByteToArrayAtBitIndex(b, array, bitIndex + (i * 8));
        }
    }

    /**
	 * This method adds an array of bytes to an array of bytes at bit index given as a second argument. For example, we
	 * have an array of bytes as follows:<br>
	 * &nbsp 00000000 00000000 00000000<br>
	 * and this method is invoked with 11111111 11111111 source argument and bitIndex is 4. After that method array will
	 * have this value:<br>
	 * &nbsp 00001111 11111111 11110000<br>
	 * If you'll try to add bytes after or before an array, ArrayIndexOutOfBoundsException is thrown and given array is
	 * not changed. If array is null, NullPointerException is thrown.
	 * 
	 * TODO TEST
	 * 
	 * @param source
	 *            Array to be added to array being a second argument.
	 * @param array
	 *            Array of bytes which will contain b after this method.
	 * @param bitIndex
	 *            Index of bit at which the the first bit of byte will be added. Bits in byte are indexed from 0 to 7
	 *            starting at most significant.
	 * @throws ArrayIndexOutOfBoundsException
	 * 
	 */
    public static void addByteArrayToArrayAtBitIndex(byte[] source, byte[] array, int bitIndex) {
        if (bitIndex + (source.length * 8) > array.length * 8) {
            throw new ArrayIndexOutOfBoundsException(INDEX_OUT_OF_BOUNDS_MESSAGE);
        }
        for (int i = 0; i < source.length; i++) {
            addByteToArrayAtBitIndex(source[i], array, bitIndex + (i * 8));
        }
    }

    /**
	 * Method that copies whole first array to the second one starting at given byte index of the second one. If
	 * ((byteIndex+toBeCopied.length) > toBeFilled.length) then ArrayIndexOfBoundsException is thrown and array that has
	 * to be filled stays untouched. TODO Test
	 * 
	 * @param toBeCopied
	 * @param toBeFilled
	 * @param byteIndex
	 * @throws ArrayIndexOutOfBoundsException
	 *             Thrown when array that has to be copied won't fit into array.
	 */
    public static void addByteArrayToArrayAtByteIndex(byte[] toBeCopied, byte[] toBeFilled, int byteIndex) {
        int toBeCopiedLength = toBeCopied.length;
        if ((byteIndex + toBeCopiedLength) > toBeFilled.length) {
            throw new ArrayIndexOutOfBoundsException("Array that has to be copied is to long to fit into array that has to be filled. toBeCopied.length=" + toBeCopiedLength + ", toBeFilled.length=" + toBeFilled.length + ", byteIndex=" + byteIndex);
        }
        for (int i = 0; i < toBeCopiedLength; i++) {
            toBeFilled[byteIndex + i] = toBeCopied[i];
        }
    }

    /**
	 * This method adds short to an array of bytes at bit index given as a second argument. For example, we have an
	 * array of bytes as follows:<br>
	 * &nbsp 00000000 00000000 00000000<br>
	 * and this method is invoked with 11111111 11111111 short argument and bitIndex is 4. After that method array will
	 * have this value:<br>
	 * &nbsp 00001111 11111111 11110000<br>
	 * If you'll try to add bytes after or before an array, ArrayIndexOutOfBoundsException is thrown and given array is
	 * not changed. If array is null, NullPointerException is thrown.
	 * 
	 * TODO Not tested.
	 * 
	 * @param s
	 *            short to be added to an array.
	 * @param array
	 *            Array of bytes which will contain b after this method.
	 * @param bitIndex
	 *            Index of bit at which the the first bit of byte will be added. Bits in byte are indexed from 0 to 7
	 *            starting at most significant.
	 * @throws ArrayIndexOutOfBoundsException
	 * 
	 */
    public static void addShortToArrayAtBitIndex(short s, byte[] array, int bitIndex) {
        if (bitIndex + 16 > array.length * 8) {
            throw new ArrayIndexOutOfBoundsException(INDEX_OUT_OF_BOUNDS_MESSAGE);
        }
        for (int i = 0; i < 2; i++) {
            byte b = (byte) ((s >>> (2 - i - 1) * 8) & 0xFF);
            addByteToArrayAtBitIndex(b, array, bitIndex + (i * 8));
        }
    }

    /**
	 * Method that adds array of booleans to byte array starting at startIndex in byte array. If there are more bits
	 * than byte array can handle (for example byte array length is 1 and we're trying to write 9 bits starting at index
	 * 0), this method throws ArrayIndexOutOfBoundsException. Byte array stays untouched after that exception.
	 * 
	 * TODO TEST
	 * 
	 * @see ArrayIndexOutOfBoundsException
	 * 
	 * @param source
	 *            Array that will be copied to byte array.
	 * @param array
	 *            Array that will contain bits of source array.
	 * @param startIndex
	 *            Start index in byte array.
	 */
    public static void addBooleanArrayToArrayAtIndex(boolean[] source, byte[] array, int startIndex) {
        if (source.length + startIndex > array.length * 8) {
            throw new ArrayIndexOutOfBoundsException(INDEX_OUT_OF_BOUNDS_MESSAGE);
        }
        for (int i = 0; i < source.length; i++) {
            int arrayIndex = (startIndex + i) / 8;
            byte currentByte = array[arrayIndex];
            boolean currentBool = source[i];
            int bitInByteIndex = (startIndex + i) % 8;
            int currentBitInArray = (currentByte >> 8 - bitInByteIndex - 1) & 0x01;
            if (currentBitInArray == 1 && !currentBool) {
                currentByte -= Math.pow(2, 8 - bitInByteIndex - 1);
            } else if (currentBitInArray == 0 && currentBool) {
                currentByte += Math.pow(2, 8 - bitInByteIndex - 1);
            }
            array[arrayIndex] = currentByte;
        }
    }

    /**
	 * Interprets given bytes as parts of long. Long number looks like this:<br>
	 * first second third fourth.<br>
	 * TODO TEST
	 */
    public static long bytesToLong(byte first, byte second, byte third, byte fourth) {
        long result = 0;
        for (int i = 0; i < 4; i++) {
            byte current = 0;
            switch(i) {
                case 0:
                    current = first;
                    break;
                case 1:
                    current = second;
                    break;
                case 2:
                    current = third;
                    break;
                case 3:
                    current = fourth;
                    break;
            }
            for (int j = 0; j < 8; j++) {
                int currentShift = 8 - j - 1;
                int currentBit = (current >> currentShift) & 0x01;
                if (currentBit == 1) {
                    int currentPower = 32 - (i * 8) - j - 1;
                    result += Math.pow(2, currentPower);
                }
            }
        }
        return result;
    }

    /**
	 * Gets an array of bits from byte array. If (startIndex + length is bigger then number of bits in byte array) or
	 * (length < 1) or (startIndex < 0) , then ArrayIndexOutOfBoundsException is thrown. For instance, when you give
	 * arguments: {00110011}, 2, 4, then this method will return an array as follows: {1100}. Another example is
	 * {01010101, 11110000}, 7, 9 that returns {111110000}.
	 * 
	 * @return Subarray of bits.
	 */
    public static boolean[] getBitsFromByteArray(byte[] array, int startIndex, int length) {
        if (startIndex + length > array.length * 8 || length < 1 || startIndex < 0) {
            throw new ArrayIndexOutOfBoundsException("You can't read from after the array. ");
        }
        boolean[] result = new boolean[length];
        for (int i = 0; i < length; i++) {
            int byteIndex = ((startIndex + i) / 8);
            int bitInByteIndex = (startIndex + i) % 8;
            int currentByte = array[byteIndex];
            int currentBitValue = (currentByte >> 8 - bitInByteIndex - 1) & 0x01;
            if (currentBitValue == 1) {
                result[i] = true;
            }
        }
        return result;
    }

    /**
	 * Method for getting a subarray of the given array. If startIndex is below 0 or startIndex + length > array.length,
	 * then ArrayIndexOutOfBoundsException is thrown. TODO Test
	 * 
	 * @param array
	 * @param startIndex
	 *            Index of first byte to be copied.
	 * @param length
	 * @return
	 */
    public static byte[] subarray(byte[] array, int startIndex, int length) {
        if (startIndex < 0 || startIndex + length > array.length) {
            throw new ArrayIndexOutOfBoundsException("You're trying to reach " + "an index that is out of an array.");
        }
        byte[] subarray = new byte[length];
        for (int i = 0; i < length; i++) {
            subarray[i] = array[startIndex + i];
        }
        return subarray;
    }

    /**
	 * Creates byte representation of given int. If, for instance, that int was 1, then an array will be [0, 0, 0, 1].
	 * 
	 * @param source
	 *            Integer to be changed to byte array.
	 * @return Byte array representing given int.
	 */
    public static byte[] intToByteArray(int source) {
        byte[] array = new byte[4];
        array[0] = (byte) ((source >> 24) & 0xFF);
        array[1] = (byte) ((source >> 16) & 0xFF);
        array[2] = (byte) ((source >> 8) & 0xFF);
        array[3] = (byte) (source & 0xFF);
        return array;
    }

    /**
	 * Method that converts IP address in text form (IPv4 or IPv6) to array of bytes. TODO now only IPv4 is implemented.
	 * Null is returned if given address wasn't in IPv4 or IPv6 form.
	 * 
	 * @param address
	 * @return
	 */
    public static byte[] stringIPAddressToBytes(String address) {
        byte[] addressBytes = null;
        int dotIndex = address.indexOf('.');
        try {
            if (dotIndex > 0) {
                addressBytes = new byte[4];
                int previousIndex = -1;
                for (int i = 0; i < 4; i++) {
                    String ipPart = address.substring(previousIndex + 1, dotIndex);
                    int ipPartInt = Integer.parseInt(ipPart);
                    addressBytes[i] = (byte) ipPartInt;
                    previousIndex = dotIndex;
                    dotIndex = address.indexOf('.', dotIndex + 1);
                    if (dotIndex == -1) dotIndex = address.length();
                }
            }
        } catch (Exception e) {
            return null;
        }
        return addressBytes;
    }

    /**
	 * Converts given array to hex String representation. Returns null if given array is null.
	 * 
	 * @param newPeerID
	 * @return
	 */
    public static String byteArrayToHexString(byte[] array) {
        if (array == null) {
            return null;
        } else {
            StringBuilder builder;
            String currentByteString = "";
            String zero = "0";
            try {
                builder = new StringBuilder();
                for (int i = 0; i < array.length; i++) {
                    currentByteString = Integer.toHexString(array[i] & 0xFF);
                    if (currentByteString.length() == 1) {
                        builder.append(zero).append(currentByteString);
                    } else {
                        builder.append(currentByteString);
                    }
                    currentByteString = null;
                    currentByteString = "";
                }
                return builder.toString();
            } finally {
                builder = null;
                zero = null;
                currentByteString = null;
            }
        }
    }

    /**
	 * Returns boolean array converted to int. This method sums powers of 2 until end of given array, so if given array
	 * is too long, returned int may be bad (for example array of length=100 returns a number that will be to big to fit
	 * into int). Throws NullPointerException if array is null.
	 * 
	 * @param array
	 *            Array to be converted.
	 * @return
	 */
    public static int booleanArrayToInt(boolean[] array) {
        int result = 0;
        for (int i = array.length - 1, power = 1; i >= 0; i--, power *= 2) {
            if (array[i]) {
                result += power;
            }
        }
        return result;
    }

    /**
	 * Returns String being a text representation of given boolean array. For instance if array is "true,false", string
	 * will look like this "10". If array is null, null is returned.
	 * 
	 * @param array
	 * @return
	 */
    public static String booleanArrayToBinaryString(boolean[] array) {
        if (array == null) {
            return null;
        }
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < array.length; i++) {
            if (array[i]) {
                builder.append("1");
            } else {
                builder.append("0");
            }
        }
        return builder.toString();
    }

    /**
	 * Method converting given array of bytes to IPv4 or IPv6 representation. Given array must be 4 (for IPv4) or 16
	 * bytes long (for IPv6). If array doesn't meet those conditions, null is returned. TODO currently only IPv4 is
	 * ready
	 * 
	 * @param byteArray
	 * @return
	 */
    public static String bytesToStringIP(byte[] byteArray) {
        int length = byteArray.length;
        if (length != 4 && length != 16) {
            return null;
        }
        return (byteArray[0] & 0xFF) + "." + (byteArray[1] & 0xFF) + "." + (byteArray[2] & 0xFF) + "." + (byteArray[3] & 0xFF);
    }
}
