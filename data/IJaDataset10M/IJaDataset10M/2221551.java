package com.flagstone.transform;

import java.io.*;

/**
 * FSCoder is a similar to Java stream classes, allowing words and bit fields 
 * to be read and written from an internal array of bytes. FSCoder supports both 
 * little-endian and big-endian byte ordering.
 * 
 * The FSCoder class maintains an internal pointer which points to the next bit 
 * in the internal array where data will be read or written. When calculating an 
 * offset in bytes to jump to simply multiply the offset by 8 for the correct 
 * bit position. The class provides accessor methods, getPointer() and 
 * setPointer() to change the location of the internal pointer.
 * 
 * When writing to an array the size of the array is changed dynamically should 
 * a write operation cause a buffer overflow. For reads if an overflow results 
 * then the bits/bytes that overflowed will be set to zero, rather than throwing 
 * an exception. The eof() method can be used to determine whether the end of 
 * the buffer has been reached.
 */
public class FSCoder {

    /** 
     * Identifies that multibyte words are stored in little-endian format with 
     * the least significant byte in a word stored first.
     */
    public static final int LITTLE_ENDIAN = 0;

    /** 
     * Identifies that multibyte words are stored in big-endian format with the 
     * most significant byte in a word stored first.
     */
    public static final int BIG_ENDIAN = 1;

    /**
     * Calculates the minimum number of bits required to encoded an integer
     * in a bit field.
     * 
     * @param value the value to be encoded.
     * 
     * @param signed where the value will be encoded as a signed or unsigned
     * integer.
     * 
     * @return the number of bits required to encode the value.
     */
    static int size(int value, boolean signed) {
        int size = 0, i = 0;
        int mask = 0x80000000;
        if (signed) {
            value = (value < 0) ? -value : value;
            for (i = 32; (value & mask) == 0 && i > 0; i--) mask >>>= 1;
            size = (i < 32) ? i + 1 : i;
        } else {
            for (i = 32; (value & mask) == 0 && i > 0; i--) mask >>>= 1;
            size = i;
        }
        return size;
    }

    /**
     * Calculates the minimum number of bits required to encoded an array of
     * integer values in a series of bit fields.
     * 
     * @param values the values to be encoded.
     * 
     * @param signed where the values will be encoded as a signed or unsigned
     * integers.
     * 
     * @return the minimum number of bits required to encode all the values.
     */
    static int size(int[] values, boolean signed) {
        int size = 0;
        for (int i = 0; i < values.length; i++) size = Math.max(size, size(values[i], signed));
        return size;
    }

    /**
     * Calculates the minimum number of bits required to encoded a floating
     * point number as a fixed point number in 8.8 format. 
     * 
     * @param value the value to be encoded.
     * 
     * @return the number of bits required to encode the value.
     */
    static int fixedShortSize(float aNumber) {
        float floatValue = aNumber * 256.0f;
        return size((int) floatValue, true);
    }

    /**
     * Calculates the minimum number of bits required to encoded a series of
     * floating point numbers in a fixed point number in 8.8 format. 
     * 
     * @param value the values to be encoded.
     * 
     * @return the minimum number of bits required to encode all the values.
     */
    static int fixedShortSize(float[] values) {
        int size = 0;
        for (int i = 0; i < values.length; i++) size = Math.max(size, fixedShortSize(values[i]));
        return size;
    }

    /**
     * Calculates the minimum number of bits required to encoded a floating
     * point number as a fixed point number in 16.16 format. 
     * 
     * @param value the value to be encoded.
     * 
     * @return the number of bits required to encode the value.
    */
    static int fixedSize(float aNumber) {
        float floatValue = aNumber * 65536.0f;
        return size((int) floatValue, true);
    }

    /**
     * Calculates the minimum number of bits required to encoded a series of
     * floating point numbers in a fixed point number in 16.16 format. 
     * 
     * @param value the values to be encoded.
     * 
     * @return the number of bits required to encode the value.
     */
    static int fixedSize(float[] values) {
        int size = 0;
        for (int i = 0; i < values.length; i++) size = Math.max(size, fixedSize(values[i]));
        return size;
    }

    /**
     * Calculates the length of a string when encoded.
     * 
     * @param string the string to be encoded.
     * 
     * @param encoding the format used to encode the string characters.
     * 
     * @param appendNull whether the string should be terminated with a null
     * byte.
     * 
     * @return the number of bytes required to encode the string.
     */
    static int strlen(String string, String encoding, boolean appendNull) {
        int length = 0;
        if (string != null) {
            try {
                length += string.getBytes(encoding).length;
                length += appendNull ? 1 : 0;
            } catch (UnsupportedEncodingException e) {
            }
        }
        return length;
    }

    /**
     * Calculates the length of a string when encoded.
     * 
     * @param string the string to be encoded.
     * 
     * @param appendNull whether the string should be terminated with a null
     * byte.
     * 
     * @return the number of bytes required to encode the string.
     */
    static int strlen(String string, boolean appendNull) {
        int length = 0;
        if (string != null) {
            try {
                length += string.getBytes("UTF8").length;
                length += appendNull ? 1 : 0;
            } catch (UnsupportedEncodingException e) {
            }
        }
        return length;
    }

    private FSMovieListener listener = null;

    String encoding = "UTF8";

    private int byteOrder = LITTLE_ENDIAN;

    private byte[] data = null;

    private int ptr = 0;

    private int end = 0;

    /**
     * Constructs an FSCoder object containing an array of bytes with the 
     * specified byte ordering.
     * 
     * @param order the byte-order for words, eitherLITTLE_ENDIAN or BIG_ENDIAN.
     * @param size the size of the internal buffer to be created.
     */
    public FSCoder(int order, int size) {
        clearContext();
        byteOrder = order;
        data = new byte[size];
        for (int i = 0; i < size; i++) data[i] = 0;
        ptr = 0;
        end = data.length << 3;
    }

    /**
     * Constructs an FSCoder object containing an array of bytes with the
     * specified byte order.
     * 
     * @param order the byte-order for words, either LITTLE_ENDIAN or BIG_ENDIAN.
     * @param bytes an array of bytes where the data will be read or written.
     */
    public FSCoder(int order, byte[] bytes) {
        clearContext();
        byteOrder = order;
        data = bytes;
        ptr = 0;
        end = data.length << 3;
    }

    public boolean equals(FSCoder coder) {
        boolean result = true;
        result = result && byteOrder == coder.byteOrder;
        result = result && ptr == coder.ptr;
        for (int i = 0; i < data.length; i++) {
            result = result && data[i] == coder.data[i];
        }
        return result;
    }

    /**
     * @deprecated The FSMovieListener interface does not enable recovery from
     * coding errors or corrupt Flash files and therefore will no longer be 
     * used. Instead errors will be reported through exceptions.
     */
    void setListener(FSMovieListener aListener) {
        listener = aListener;
    }

    /**
     * @deprecated The FSMovieListener interface does not enable recovery from
     * coding errors or corrupt Flash files and therefore will no longer be 
     * used. Instead errors will be reported through exceptions.
     */
    FSMovieListener getListener() {
        return listener;
    }

    /**
     * @deprecated
     * 
     * @param name
     */
    void beginObject(String name) {
        if (listener != null) listener.logEvent(new FSMovieEvent(context[FSCoder.Action], FSMovieEvent.Begin, ptr, 0, name));
    }

    /**
     * @deprecated
     * 
     * @param name
     */
    void endObject(String name) {
        if (listener != null) listener.logEvent(new FSMovieEvent(context[FSCoder.Action], FSMovieEvent.End, ptr, 0, name));
    }

    /**
     * @deprecated
     * 
     */
    void logValue(Object anObject, int location, int numberOfBits) {
        if (listener != null) listener.logEvent(new FSMovieEvent(context[FSCoder.Action], FSMovieEvent.Value, location, numberOfBits, anObject));
    }

    /**
     * @deprecated
     * 
     */
    void logError(String errorKey, int location, int length) {
        if (listener != null) listener.logEvent(new FSMovieEvent(context[FSCoder.Action], FSMovieEvent.Error, location, length, errorKey));
    }

    /**
     * Return the string representation of the character encoding scheme used
     * when encoding or decoding strings as a sequence of bytes.
     * 
     * @return the string the name of the encoding scheme for characters.
     */
    public String getEncoding() {
        return encoding;
    }

    /**
     * Sets the string representation of the character encoding scheme used
     * when encoding or decoding strings as a sequence of bytes.
     * 
     * @param enc the string the name of the encoding scheme for characters.
     */
    public void setEncoding(String enc) {
        encoding = enc;
    }

    /**
     * Returns a copy of the array of bytes.
     * 
     * @return a copy of the internal buffer.
     */
    public byte[] getData() {
        int length = (ptr + 7) >> 3;
        byte[] bytes = new byte[length];
        System.arraycopy(data, 0, bytes, 0, length);
        return bytes;
    }

    /**
     * Sets the array of bytes used to read or write data to.
     * 
     * @param order the byte-order for words, either FSCoder.LITTLE_ENDIAN or 
     * FSCoder.BIG_ENDIAN.
     * 
     * @param bytes a byte array that will be used as the internal buffer.
     */
    public void setData(int order, byte[] bytes) {
        byteOrder = order;
        data = new byte[bytes.length];
        System.arraycopy(bytes, 0, data, 0, bytes.length);
        ptr = 0;
        end = data.length << 3;
    }

    /**
     * Increases the size of the internal buffer. This method is used when 
     * encoding data to automatically adjust the buffer size to avoid overflows.
     *  
     * @param size the number of bytes to add to the buffer.
     */
    public void addCapacity(int size) {
        int length = (end >>> 3) + size;
        byte[] bytes = new byte[length];
        System.arraycopy(data, 0, bytes, 0, data.length);
        data = bytes;
        end = data.length << 3;
    }

    /**
    * Return the size of the internal buffer in bytes.
    * 
    * @return the size of the buffer.
    */
    public int getCapacity() {
        return end >>> 3;
    }

    /**
     * Returns the offset, in bits, from the start of the buffer where the next 
     * value will be read or written.
     * 
     * @return the offset in bits where the next value will be read or written. 
     */
    public int getPointer() {
        return ptr;
    }

    /**
     * Sets the offset, in bits, from the start of the buffer where the next 
     * value will be read or written. If the offset falls outside of the bits 
     * range supported by the buffer then an IllegalArgumentException will
     * be thrown.
     *  
     * @param location the offset in bits from the start of the array of bytes.
     */
    public void setPointer(int location) {
        if (location < 0) {
            location = 0;
        } else if (location > end) {
            location = end;
        }
        ptr = location;
    }

    /**
     * Adds offset, in bits, to the internal pointer to change the location 
     * where the next value will be read or written. If the adjust causes the 
     * point to fall outside the bounds of the internal data then the value 
     * is clamped to either the start of end of the array.
     *  
     * @param offset the offset in bits from the start of the array of bytes.
     */
    public void adjustPointer(int offset) {
        ptr += offset;
        if (ptr < 0) ptr = 0; else if (ptr >= end) ptr = end;
    }

    /**
     * Moves the internal pointer forward so it is aligned on a byte boundary. 
     * All word values read and written to the internal buffer must be 
     * byte-aligned.
     */
    public void alignToByte() {
        ptr = (ptr + 7) & ~7;
    }

    /**
     * Returns true of the internal pointer is at the end of the buffer.
     * 
     * @return true if the pointer is at the end of the buffer, false otherwise.
     */
    public boolean eof() {
        return ptr >= end;
    }

    /**
     * Read a bit field from the internal buffer.
     * 
     * If a buffer overflow would occur then the bits which would cause the 
     * error when read will be set to zero.
     * 
     * @param numberOfBits the number of bits to read.
     * 
     * @param signed a boolean flag indicating whether the value read should 
     * be sign extended.
     * 
     * @return the value read.
     */
    public int readBits(int numberOfBits, boolean signed) {
        int value = 0;
        if (numberOfBits < 0 || numberOfBits > 32) throw new IllegalArgumentException("Number of bits must be in the range 1..32.");
        if (numberOfBits == 0) return 0;
        int index = ptr >> 3;
        int base = (data.length - index > 4) ? 0 : (4 - (data.length - index)) * 8;
        for (int i = 32; i > base; i -= 8, index++) value |= (data[index] & 0x000000FF) << (i - 8);
        value <<= ptr % 8;
        if (signed) value >>= 32 - numberOfBits; else value >>>= 32 - numberOfBits;
        ptr += numberOfBits;
        if (ptr > (data.length << 3)) ptr = data.length << 3;
        return value;
    }

    /**
     * Write a bit value to the internal buffer. The buffer will resize 
     * automatically if required.
     * 
     * @param value an integer containing the value to be written.
     * @param numberOfBits the least significant n bits from the value that 
     * will be written to the buffer.
     */
    public void writeBits(int value, int numberOfBits) {
        if (numberOfBits < 0 || numberOfBits > 32) throw new IllegalArgumentException("Number of bits must be in the range 1..32.");
        if (ptr + 32 > end) addCapacity(data.length / 2 + 4);
        int index = ptr >> 3;
        value <<= (32 - numberOfBits);
        value = value >>> (ptr % 8);
        value = value | (data[index] << 24);
        for (int i = 24; i >= 0; i -= 8, index++) data[index] = (byte) (value >>> i);
        ptr += numberOfBits;
        if (ptr > (data.length << 3)) ptr = data.length << 3;
    }

    /**
     * Read a word from the internal buffer.
     * 
     * If a buffer overflow would occur then the bytes which would cause the 
     * error when read will be set to zero.
     * 
     * @param numberOfBytes the number of bytes read in the range 1..4.
     * 
     * @param signed a boolean flag indicating whether the value read should be 
     * sign extended.
     * 
     * @return the value read.
     */
    public int readWord(int numberOfBytes, boolean signed) {
        int value = 0;
        if (numberOfBytes < 0 || numberOfBytes > 4) throw new IllegalArgumentException("Number of bytes must be in the range 1..4.");
        int index = ptr >> 3;
        if (index + numberOfBytes > data.length) numberOfBytes = data.length - index;
        int numberOfBits = numberOfBytes * 8;
        if (byteOrder == LITTLE_ENDIAN) {
            for (int i = 0; i < numberOfBits; i += 8, ptr += 8, index++) value += (data[index] & 0x000000FF) << i;
        } else {
            for (int i = 0; i < numberOfBits; i += 8, ptr += 8, index++) {
                value = value << 8;
                value += data[index] & 0x000000FF;
            }
        }
        if (signed) {
            value <<= 32 - numberOfBits;
            value >>= 32 - numberOfBits;
        }
        return value;
    }

    /**
     * Write a word to the internal buffer. The buffer will resize automatically
     * if required.
     * 
     * @param value an integer containing the value to be written.
     * @param numberOfBytes the least significant n bytes from the value that 
     * will be written to the buffer.
     */
    public void writeWord(int value, int numberOfBytes) {
        if (numberOfBytes < 0 || numberOfBytes > 4) throw new IllegalArgumentException("Number of bytes must be in the range 1..4.");
        int numberOfBits = numberOfBytes * 8;
        if (ptr + numberOfBits > end) addCapacity(data.length / 2 + numberOfBytes);
        if (byteOrder == LITTLE_ENDIAN) {
            int index = ptr >>> 3;
            for (int i = 0; i < numberOfBits; i += 8, ptr += 8, value >>>= 8, index++) data[index] = (byte) value;
        } else {
            int index = (ptr + numberOfBits - 8) >>> 3;
            for (int i = 0; i < numberOfBits; i += 8, ptr += 8, value >>>= 8, index--) data[index] = (byte) value;
        }
    }

    /**
     * Reads an array of bytes from the internal buffer. If a read overflow 
     * would occur while reading the internal buffer then the remaining bytes 
     * in the array will not be filled. The method returns the number of bytes 
     * read.
     * 
     * @param bytes the array that will contain the bytes read.
     * @return the number of bytes read from the buffer.
     */
    public int readBytes(byte[] bytes) {
        int bytesRead = 0;
        if (bytes == null || bytes.length == 0) return bytesRead;
        int index = ptr >>> 3;
        int numberOfBytes = bytes.length;
        if (index + numberOfBytes > data.length) numberOfBytes = data.length - index;
        for (int i = 0; i < numberOfBytes; i++, ptr += 8, index++, bytesRead++) bytes[i] = data[index];
        return bytesRead;
    }

    /**
     * Writes an array of bytes from the internal buffer. The internal buffer 
     * will be resized automatically if required.
     * 
     * @param bytes the array containing the data to be written.
     * @return the number of bytes written to the buffer.
     */
    public int writeBytes(byte[] bytes) {
        int bytesWritten = 0;
        if (ptr + (bytes.length << 3) > end) addCapacity(data.length / 2 + bytes.length);
        if (bytes == null || bytes.length == 0) return bytesWritten;
        int index = ptr >>> 3;
        int numberOfBytes = bytes.length;
        for (int i = 0; i < numberOfBytes; i++, ptr += 8, index++, bytesWritten++) data[index] = bytes[i];
        return bytesWritten;
    }

    /**
     * Read a bit field without adjusting the internal pointer.
     * 
     * @param numberOfBits the number of bits to read.
     * 
     * @param signed a boolean flag indicating whether the value read should 
     * be sign extended.
     * 
     * @return the value read.
     */
    public int scanBits(int numberOfBits, boolean signed) {
        int start = ptr;
        int value = readBits(numberOfBits, signed);
        ptr = start;
        return value;
    }

    /**
     * Read a word without adjusting the internal pointer.
     * 
     * @param numberOfBytes the number of bytes to read.
     * 
     * @param signed a boolean flag indicating whether the value read should 
     * be sign extended.
     * 
     * @return the value read.
     */
    public int scanWord(int numberOfBytes, boolean signed) {
        int start = ptr;
        int value = readWord(numberOfBytes, signed);
        ptr = start;
        return value;
    }

    /**
     * Read a fixed point number, in either (8.8) or (16.16) format from a bit 
     * field.
     * 
     * @param numberOfBits the number of bits the number is encoded in.
     * @param fractionSize the number of bits occupied by the fractional
     * part of the number. The integer part will be signed extended.
     * 
     * @return the value read as a floating-point number.
     */
    public float readFixedBits(int numberOfBits, int fractionSize) {
        float divisor = (float) (1 << fractionSize);
        float value = ((float) readBits(numberOfBits, true)) / divisor;
        return value;
    }

    /**
     * Write a fixed point number, in either (8.8) or (16.16) format to a bit 
     * field.
     * 
     * @param value the value to be ecoded.
     * @param numberOfBits the number of bits the number is encoded in.
     * @param fractionSize the number of bits occupied by the fractional
     * part of the number. The integer part will be signed extended.
     */
    public void writeFixedBits(float value, int numberOfBits, int fractionSize) {
        float multiplier = (float) (1 << fractionSize);
        writeBits((int) (value * multiplier), numberOfBits);
    }

    /**
     * Read a fixed point number, in either (8.8) or (16.16) format from a 
     * word field, accounting for the byte-ordering used.
     * 
     * @param mantissaSize the number of bits occupied by the integer
     * part of the number. This will be signed extended.
     * @param fractionSize the number of bits occupied by the fractional
     * part of the number.
     * 
     * @return the value read as a floating-point number.
     */
    public float readFixedWord(int mantissaSize, int fractionSize) {
        float divisor = (float) (1 << (fractionSize * 8));
        int fraction = readWord(fractionSize, false);
        int mantissa = readWord(mantissaSize, true) << (fractionSize * 8);
        return (mantissa + fraction) / divisor;
    }

    /**
     * Write a fixed point number, in either (8.8) or (16.16) format to a 
     * word field, accounting for the byte-ordering used.
     * 
     * @param value the value to be written.
     * @param mantissaSize the number of bits occupied by the integer
     * part of the number.
     * @param fractionSize the number of bits occupied by the fractional
     * part of the number.
     */
    public void writeFixedWord(float value, int mantissaSize, int fractionSize) {
        float multiplier = (float) (1 << (fractionSize * 8));
        int fraction = (int) (value * multiplier);
        int mantissa = (int) value;
        writeWord(fraction, fractionSize);
        writeWord(mantissa, mantissaSize);
    }

    /**
     * Read a double-precision floating point number from a sequence of bytes
     * using the byte-ordering of the buffer.
     * 
     * @return the value.
     */
    public double readDouble() {
        int upperInt = readWord(4, false);
        int lowerInt = readWord(4, false);
        long longValue = (long) upperInt << 32;
        longValue |= (long) lowerInt & 0x00000000FFFFFFFFL;
        return Double.longBitsToDouble(longValue);
    }

    /**
     * Write a double-precision floating point number as a sequence of bytes
     * using the byte-ordering of the buffer.
     * 
     * @param value the value to be written.
     */
    public void writeDouble(double value) {
        long longValue = Double.doubleToLongBits(value);
        int lowerInt = (int) longValue;
        int upperInt = (int) (longValue >>> 32);
        writeWord(upperInt, 4);
        writeWord(lowerInt, 4);
    }

    /**
     * Read a string containing the specified number of characters using the 
     * default character encoding scheme.
     * 
     * @param length the number of characters to read.
     * 
     * @return the string containing the specified number of characters.
     */
    public String readString(int length) {
        return readString(length, encoding);
    }

    /**
     * Read a string containing the specified number of characters with the  
     * given character encoding scheme.
     * 
     * @param length the number of characters to read.
     * @return enc, the string the name of the encoding schemd for characters.
     * 
     * @return the string containing the specified number of characters.
     */
    public String readString(int length, String enc) {
        if (length == 0) return "";
        String value = null;
        byte[] str = new byte[length];
        int len = readBytes(str);
        try {
            value = new String(str, 0, len, enc);
        } catch (java.io.UnsupportedEncodingException e) {
            value = "";
        }
        return value;
    }

    /**
     * Read a null-terminated string using the default character encoding scheme.
     * 
     * @return the string read from the internal buffer.
     */
    public String readString() {
        return readString(encoding);
    }

    /**
     * Read a null-terminated string using the specified character encoding scheme.
     * 
     * @return the string read from the internal buffer.
     */
    public String readString(String enc) {
        String value = null;
        int start = ptr >> 3;
        int length = 0;
        while (start < data.length && data[start++] != 0) length++;
        byte[] str = new byte[length];
        int len = readBytes(str);
        try {
            value = new String(str, 0, len, enc);
        } catch (java.io.UnsupportedEncodingException e) {
            value = "";
        }
        readWord(1, false);
        len++;
        return value;
    }

    /**
     * Write a string to the internal buffer using the default character 
     * encoding scheme.
     * 
     * @param str the string.
     * 
     * @return the number of bytes written.
     */
    public int writeString(String str) {
        return writeString(str, encoding);
    }

    /**
     * Write a string to the internal buffer using the specified character 
     * encoding scheme.
     * 
     * @param str the string.
     * 
     * @return the number of bytes written.
     */
    public int writeString(String str, String enc) {
        int bytesWritten = 0;
        try {
            bytesWritten = writeBytes(str.getBytes(enc));
        } catch (java.io.UnsupportedEncodingException e) {
        }
        return bytesWritten;
    }

    /**
     * Searches the internal buffer for a bit pattern and advances the pointer 
     * to the start of the bit field, returning true to signal a successful 
     * search. If the bit pattern cannot be found then the method returns false 
     * and the position of the internal pointer is not changed.
     * 
     * The step, in bits, added to the pointer can be specified, allowing the
     * number of bits being searched to be independent of the location in the 
     * internal buffer. This is useful for example when searching for a bit 
     * field that begins on a byte or word boundary.
     *  
     * @param value an integer containing the bit patter to search for.
     * @param numberOfBits least significant n bits in the value to search for.
     * @param step the increment in bits to add to the internal pointer as the 
     * buffer is searched.
     * 
     * @return true if the pattern was found, false otherwise.
     */
    public boolean findBits(int value, int numberOfBits, int step) {
        boolean found = false;
        int start = ptr;
        for (; ptr < end; ptr += step) {
            if (scanBits(numberOfBits, false) == value) {
                found = true;
                break;
            }
        }
        if (found == false) ptr = start;
        return found;
    }

    /**
     * Searches the internal buffer for a word and advances the pointer to the 
     * location where the word was found, returning true to signal a successful 
     * search. The search will begin on the next byte boundary. If word cannot 
     * be found then the method returns false and the position of the internal 
     * pointer is not changed.
     * 
     * Specifying the number of bytes in the search value allows word of either 
     * 8, 16, 24 or 32 bits to be searched for. Searches for words are performed 
     * faster than using the findBits() method.
     * 
     * @param value an integer containing the word to search for.
     * 
     * @param numberOfBytes least significant n bytes in the value to search 
     * for.
     * 
     * @param step the increment in bits to add to the internal pointer as the 
     * buffer is searched.
     * 
     * @return true if the pattern was found, false otherwise.
     */
    public boolean findWord(int value, int numberOfBytes, int step) {
        boolean found = false;
        for (; ptr < end; ptr += step) {
            if (scanWord(numberOfBytes, false) == value) {
                found = true;
                break;
            }
        }
        return found;
    }

    /**
     * TransparentColors is used to pass information to FSCOlor objects when
     * they are being encoded or decoded so that the alpha channel will be 
     * included.
     */
    public static final int TransparentColors = 0;

    static final int Action = 1;

    /**
     * Version is used to pass the current version of Flash that an object is 
     * being encoded or decoded for.
     */
    public static final int Version = 2;

    static final int Type = 3;

    static final int Empty = 4;

    static final int Identifier = 5;

    static final int NumberOfFillBits = 6;

    static final int NumberOfLineBits = 7;

    static final int NumberOfAdvanceBits = 8;

    static final int NumberOfGlyphBits = 9;

    static final int NumberOfShapeBits = 10;

    static final int ArrayCountExtended = 11;

    static final int WideCodes = 12;

    static final int Delta = 13;

    static final int CodingError = 14;

    static final int TypeInError = 15;

    static final int StartOfError = 16;

    static final int ExpectedLength = 17;

    static final int DecodeActions = 18;

    static final int DecodeShapes = 19;

    static final int DecodeGlyphs = 20;

    int[] context = new int[21];

    private void clearContext() {
        for (int i = 0; i < context.length; i++) context[i] = 0;
    }

    public int getContext(int key) {
        return context[key];
    }

    public void setContext(int key, int value) {
        context[key] = value;
    }
}
