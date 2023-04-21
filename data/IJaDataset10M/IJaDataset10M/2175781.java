package tw.edu.shu.im.iccio;

import tw.edu.shu.im.iccio.datatype.*;

/**
 * ICCUtils class contains some useful helper methods, mostly static methods.
 * 
 * @author Ted Wen
 * @version 0.1
 * @update 2006-10-29
 * 
 * It will include some static utility methods to help simplifying common routines.
 */
public class ICCUtils {

    /**
	 * Make a byte array from a Streamable object and copy it into the larger byte array.
	 * The byte array needs to be allocated with enough size to hold the streamed bytes, otherwise
	 * an exception is raised.
	 *
	 * @param bar - the byte array in which the Streamable object will be inserted.
	 * @param offset - from which position to insert the object
	 * @param src - the Streamable instance
	 */
    public static void appendByteArray(byte[] bar, int offset, Streamable src) throws ICCProfileException {
        if (bar == null || src == null) throw new ICCProfileException("ICCUtils.appendByteArray():null argument", ICCProfileException.IllegalArgumentException);
        byte[] ba = src.toByteArray();
        if (bar.length < offset + ba.length) throw new ICCProfileException("ICCUtils.appendByteArray():byte array not big enough", ICCProfileException.WrongSizeException);
        System.arraycopy(ba, 0, bar, offset, ba.length);
    }

    /**
	 * Expand array of unsigned integer to add an increment empty elements.
	 * The data objects contained in the original array are copied to the new one.
	 * The result array is the original array plus increment null placeholders.
	 *
	 * @param array - the array to expand
	 * @param increment - the number of elements to add
	 */
    public static UInt8Number[] expand(UInt8Number[] array, int increment) {
        UInt8Number[] newArray = new UInt8Number[array.length + increment];
        System.arraycopy(array, 0, newArray, 0, array.length);
        return newArray;
    }

    public static UInt16Number[] expand(UInt16Number[] array, int increment) {
        UInt16Number[] newArray = new UInt16Number[array.length + increment];
        System.arraycopy(array, 0, newArray, 0, array.length);
        return newArray;
    }

    public static UInt32Number[] expand(UInt32Number[] array, int increment) {
        UInt32Number[] newArray = new UInt32Number[array.length + increment];
        System.arraycopy(array, 0, newArray, 0, array.length);
        return newArray;
    }

    /**
	 * Expand array of UInt64Number to add an increment empty elements.
	 * The data objects contained in the original array are copied to the new one.
	 * The result array is the original array plus increment null placeholders.
	 * 
	 * @param array - the array to expand
	 * @param increment - the number of elements to add
	 */
    public static UInt64Number[] expand(UInt64Number[] array, int increment) {
        UInt64Number[] newArray = new UInt64Number[array.length + increment];
        System.arraycopy(array, 0, newArray, 0, array.length);
        return newArray;
    }

    /**
	 * Convert a string into byte array and encode as UTF-16BE (big-endian) for ICC.
	 * The string to convert must be created with proper encoding such as "Big5".
	 * @param text - native encoded string
	 */
    public static byte[] enUnicode(String text) throws ICCProfileException {
        try {
            return text.getBytes("UTF-16BE");
        } catch (java.io.UnsupportedEncodingException e) {
            throw new ICCProfileException("deUnicode: " + e.getMessage(), ICCProfileException.UnsupportedEncodingException);
        }
    }

    /**
	 * Convert a byte array of UTF-16BE encoding into a string of a given encoding.
	 * For example: deUnicode(data, "Big5");
	 * @param data - byte array of UTF-16BE encoding
	 */
    public static String deUnicode(byte[] data) throws ICCProfileException {
        try {
            return new String(data, "UTF-16BE");
        } catch (java.io.UnsupportedEncodingException e) {
            throw new ICCProfileException("deUnicode: " + e.getMessage(), ICCProfileException.UnsupportedEncodingException);
        }
    }
}
