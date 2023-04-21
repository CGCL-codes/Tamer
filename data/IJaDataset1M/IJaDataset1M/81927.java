package edu.whitman.halfway.bcomponent;

import org.apache.log4j.*;
import java.io.File;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;

public class IOByteUtil {

    private static Logger log = Logger.getLogger(IOByteUtil.class.getName());

    public static boolean readBoolean(InputStream in) {
        return (readInt(in, 1) == 1);
    }

    public static int readInt(InputStream in, int numBytes) {
        try {
            byte[] byteArray = new byte[numBytes];
            in.read(byteArray, 0, numBytes);
            return byteToInt(byteArray);
        } catch (IOException e) {
            log.error(e);
            return -1;
        }
    }

    public static String readString(InputStream in, int numHeaderBytes) {
        try {
            byte[] byteArray = new byte[numHeaderBytes];
            in.read(byteArray, 0, numHeaderBytes);
            return byteToString(byteArray);
        } catch (IOException e) {
            log.error(e);
            return "";
        }
    }

    public static void writeBoolean(OutputStream out, boolean value) {
        int val = (value) ? 1 : 0;
        writeInt(out, val, 1);
    }

    public static void writeInt(OutputStream out, int i, int numBytes) {
        try {
            out.write(intToNumByte(i, numBytes));
        } catch (IOException e) {
            log.error(e);
        }
    }

    public static void writeString(OutputStream out, String s, int numHeaderBytes) {
        try {
            byte[] outArray = new byte[numHeaderBytes];
            byte[] byteArray = s.getBytes();
            int minVal = Math.min(outArray.length, byteArray.length);
            for (int i = 0; i < minVal; i++) {
                outArray[i] = byteArray[i];
            }
            out.write(outArray);
        } catch (IOException e) {
            log.error(e);
        }
    }

    public static byte[] intToNumByte(int val, int numBytes) {
        byte[] byteArray = new byte[numBytes];
        log.debug("Value: " + val + "   Num Bytes: " + numBytes);
        for (int i = (numBytes - 1); i >= 0; i--) {
            int modRemain = (int) (val % Byte.MAX_VALUE);
            byteArray[i] = (byte) modRemain;
            System.out.print(" [" + modRemain + "]");
            val = (int) ((1.0 * val - modRemain) / Byte.MAX_VALUE);
        }
        System.out.println();
        return byteArray;
    }

    public static byte[] integerToByte(Integer value) {
        double val = Math.abs(1.0 * value.intValue());
        if (val == 0) return intToNumByte(0, 1);
        return intToNumByte(value.intValue(), (int) Math.ceil(Math.log(val) / Math.log(1.0 + Byte.MAX_VALUE)));
    }

    public static byte[] stringToByte(String val) {
        return val.getBytes();
    }

    public static byte[] booleanToByte(Boolean b) {
        byte[] a = new byte[1];
        a[0] = (byte) (b.booleanValue() ? 1 : 0);
        return a;
    }

    public static int byteToInt(byte[] val) {
        int retVal = 0;
        for (int i = 0; i < val.length; i++) {
            retVal = Byte.MAX_VALUE * retVal + val[i];
            if (val[i] < 0) retVal += Byte.MAX_VALUE;
        }
        return retVal;
    }

    public static Integer byteToInteger(byte[] b) {
        int val = byteToInt(b);
        return new Integer(val);
    }

    public static String byteToString(byte[] val) {
        String result = new String(val);
        int nullIndex = result.indexOf('\0');
        if (nullIndex >= 0) {
            return result.substring(0, nullIndex);
        } else {
            return result;
        }
    }

    public static File byteToFile(byte[] val) {
        return new File(byteToString(val));
    }

    public static Boolean byteToBoolean(byte[] b) {
        int value = byteToInt(b);
        if (value == 1) return new Boolean(true);
        return new Boolean(false);
    }
}
