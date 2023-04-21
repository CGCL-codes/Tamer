package com.bm.datagen.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Random;

/**
 * This class creates pseudo random data for java primitives Date, String, etc.
 * 
 * @author Daniel Wiese
 * @since 07.10.2005
 */
public final class BaseRandomDataGenerator {

    private static final Random random = new Random(System.currentTimeMillis());

    /** first 255 unicude letters* */
    private static final char[] chars = { 'q', 'w', 'e', 'r', 't', 'z', 'u', 'i', 'o', 'p', 'a', 's', 'd', 'f', 'g', 'h', 'j', 'k', 'l', 'y', 'x', 'c', 'v', 'b', 'n', 'm', '�', '�', '�', '�', 'Q', 'W', 'E', 'R', 'T', 'Z', 'U', 'I', 'O', 'P', 'A', 'S', 'D', 'F', 'G', 'H', 'J', 'K', 'L', 'Y', 'X', 'C', 'V', 'B', 'N', 'M', '�', '�', '�', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '!', '"', '�', '$', '%', '&', '/', '(', ')', '=', '?', '>', '#', '+', '*', ',', ';', ':', '.', '-', '_', '`', '�', '{', '}' };

    private static final char[] simpleChars = { 'q', 'w', 'e', 'r', 't', 'z', 'u', 'i', 'o', 'p', 'a', 's', 'd', 'f', 'g', 'h', 'j', 'k', 'l', 'y', 'x', 'c', 'v', 'b', 'n', 'm', '�', '�', '�', '�' };

    private BaseRandomDataGenerator() {
    }

    /**
	 * Random int value.
	 * 
	 * @return random int value
	 */
    public static int getValueInt() {
        return random.nextInt();
    }

    /**
	 * Random double value.
	 * 
	 * @return random double value
	 */
    public static double getValueDouble() {
        return random.nextDouble();
    }

    /**
	 * Random float value.
	 * 
	 * @return random float value
	 */
    public static float getValueFloat() {
        return random.nextFloat();
    }

    /**
	 * Random boolean value.
	 * 
	 * @return random boolean value
	 */
    public static boolean getValueBoolean() {
        return random.nextBoolean();
    }

    /**
	 * Generates a random byte array.
	 * 
	 * @param length -
	 *            the length of the byte array
	 * @return random boolean value
	 */
    public static byte[] getValueByte(int length) {
        final byte[] back = new byte[length];
        random.nextBytes(back);
        return back;
    }

    /**
	 * Random char value.
	 * 
	 * @return random char value
	 */
    public static char getValueChar() {
        return chars[random.nextInt(chars.length)];
    }

    /**
	 * Generates a random string.
	 * 
	 * @param size -
	 *            the size of the random string
	 * @param isSimple -
	 *            if true only letters a-z and umlauts are generated
	 * @return random string value value
	 */
    public static String getValueString(int size, boolean isSimple) {
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; i++) {
            if (isSimple) {
                sb.append(simpleChars[random.nextInt(simpleChars.length)]);
            } else {
                sb.append(chars[random.nextInt(chars.length)]);
            }
        }
        return sb.toString();
    }

    /**
	 * Generatetes a random Date.
	 * 
	 * @return - a random date
	 */
    public static Date getValueDate() {
        final Calendar toGenerate = new GregorianCalendar();
        toGenerate.setTime(new Date(System.currentTimeMillis()));
        if (random.nextBoolean()) {
            toGenerate.add(Calendar.MONTH, -Math.abs(random.nextInt(240)));
        } else {
            toGenerate.add(Calendar.MONTH, Math.abs(random.nextInt(240)));
        }
        if (random.nextBoolean()) {
            toGenerate.add(Calendar.HOUR, -Math.abs(random.nextInt(24)));
        } else {
            toGenerate.add(Calendar.HOUR, Math.abs(random.nextInt(24)));
        }
        if (random.nextBoolean()) {
            toGenerate.add(Calendar.MINUTE, -Math.abs(random.nextInt(60)));
        } else {
            toGenerate.add(Calendar.MINUTE, Math.abs(random.nextInt(60)));
        }
        if (random.nextBoolean()) {
            toGenerate.add(Calendar.SECOND, -Math.abs(random.nextInt(60)));
        } else {
            toGenerate.add(Calendar.SECOND, Math.abs(random.nextInt(60)));
        }
        return toGenerate.getTime();
    }

    /**
	 * Random long value.
	 * 
	 * @return random long value
	 */
    public static long getValueLong() {
        return random.nextLong();
    }

    /**
	 * Random short value.
	 * 
	 * @return random short value
	 */
    public static short getValueShort() {
        return (short) random.nextInt(255);
    }
}
