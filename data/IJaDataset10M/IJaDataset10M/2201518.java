package csimage.demo.openhouse;

import java.io.*;
import java.util.*;

public class Keyboard {

    private static boolean printErrors = true;

    private static int errorCount = 0;

    public static int getErrorCount() {
        return errorCount;
    }

    public static void resetErrorCount(int count) {
        errorCount = 0;
    }

    public static boolean getPrintErrors() {
        return printErrors;
    }

    public static void setPrintErrors(boolean flag) {
        printErrors = flag;
    }

    private static void error(String str) {
        errorCount++;
        if (printErrors) System.out.println(str);
    }

    private static String current_token = null;

    private static StringTokenizer reader;

    private static BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

    private static String getNextToken() {
        return getNextToken(true);
    }

    private static String getNextToken(boolean skip) {
        String token;
        if (current_token == null) token = getNextInputToken(skip); else {
            token = current_token;
            current_token = null;
        }
        return token;
    }

    private static String getNextInputToken(boolean skip) {
        final String delimiters = " \t\n\r\f";
        String token = null;
        try {
            if (reader == null) reader = new StringTokenizer(in.readLine(), delimiters, true);
            while (token == null || ((delimiters.indexOf(token) >= 0) && skip)) {
                while (!reader.hasMoreTokens()) reader = new StringTokenizer(in.readLine(), delimiters, true);
                token = reader.nextToken();
            }
        } catch (Exception exception) {
            token = null;
        }
        return token;
    }

    public static boolean endOfLine() {
        return !reader.hasMoreTokens();
    }

    public static String readString() {
        String str;
        try {
            str = getNextToken(false);
            while (!endOfLine()) {
                str = str + getNextToken(false);
            }
        } catch (Exception exception) {
            error("Error reading String data, null value returned.");
            str = null;
        }
        return str;
    }

    public static String readWord() {
        String token;
        try {
            token = getNextToken();
        } catch (Exception exception) {
            error("Error reading String data, null value returned.");
            token = null;
        }
        return token;
    }

    public static boolean readBoolean() {
        String token = getNextToken();
        boolean bool;
        try {
            if (token.toLowerCase().equals("true")) bool = true; else if (token.toLowerCase().equals("false")) bool = false; else {
                error("Error reading boolean data, false value returned.");
                bool = false;
            }
        } catch (Exception exception) {
            error("Error reading boolean data, false value returned.");
            bool = false;
        }
        return bool;
    }

    public static char readChar() {
        String token = getNextToken(false);
        char value;
        try {
            if (token.length() > 1) {
                current_token = token.substring(1, token.length());
            } else current_token = null;
            value = token.charAt(0);
        } catch (Exception exception) {
            error("Error reading char data, MIN_VALUE value returned.");
            value = Character.MIN_VALUE;
        }
        return value;
    }

    public static int readInt() {
        String token = getNextToken();
        int value;
        try {
            value = Integer.parseInt(token);
        } catch (Exception exception) {
            error("Error reading int data, MIN_VALUE value returned.");
            value = Integer.MIN_VALUE;
        }
        return value;
    }

    public static long readLong() {
        String token = getNextToken();
        long value;
        try {
            value = Long.parseLong(token);
        } catch (Exception exception) {
            error("Error reading long data, MIN_VALUE value returned.");
            value = Long.MIN_VALUE;
        }
        return value;
    }

    public static float readFloat() {
        String token = getNextToken();
        float value;
        try {
            value = (new Float(token)).floatValue();
        } catch (Exception exception) {
            error("Error reading float data, NaN value returned.");
            value = Float.NaN;
        }
        return value;
    }

    public static double readDouble() {
        String token = getNextToken();
        double value;
        try {
            value = (new Double(token)).doubleValue();
        } catch (Exception exception) {
            error("Error reading double data, NaN value returned.");
            value = Double.NaN;
        }
        return value;
    }
}
