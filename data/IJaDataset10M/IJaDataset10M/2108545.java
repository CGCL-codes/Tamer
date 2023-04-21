package org.maestroframework.utils;

import java.io.IOException;
import java.io.Writer;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {

    private static final int BASELENGTH = 128;

    private static final int LOOKUPLENGTH = 16;

    private static final byte[] hexNumberTable = new byte[BASELENGTH];

    private static final char[] lookUpHexAlphabet = new char[LOOKUPLENGTH];

    static {
        for (int i = 0; i < BASELENGTH; i++) {
            hexNumberTable[i] = -1;
        }
        for (int i = '9'; i >= '0'; i--) {
            hexNumberTable[i] = (byte) (i - '0');
        }
        for (int i = 'F'; i >= 'A'; i--) {
            hexNumberTable[i] = (byte) (i - 'A' + 10);
        }
        for (int i = 'f'; i >= 'a'; i--) {
            hexNumberTable[i] = (byte) (i - 'a' + 10);
        }
        for (int i = 0; i < 10; i++) {
            lookUpHexAlphabet[i] = (char) ('0' + i);
        }
        for (int i = 10; i <= 15; i++) {
            lookUpHexAlphabet[i] = (char) ('A' + i - 10);
        }
    }

    /**
	 * Repeat a string a number of times and return the result back to the sender
	 * @param times
	 * @param string
	 * @return
	 */
    public static String repeat(int times, String string) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < times; i++) {
            builder.append(string);
        }
        return builder.toString();
    }

    public static void repeat(Writer writer, int times, String string) throws IOException {
        for (int i = 0; i < times; i++) {
            writer.write(string);
        }
    }

    public static String join(String joinBy, Iterable values) {
        StringBuilder builder = new StringBuilder();
        Iterator it = values.iterator();
        while (it.hasNext()) {
            builder.append(it.next());
            if (it.hasNext()) builder.append(joinBy);
        }
        return builder.toString();
    }

    public static String max(String string, int maxLength, String suffix) {
        StringBuilder builder = new StringBuilder(string);
        if (builder.length() > maxLength) {
            builder.setLength(maxLength);
            if (suffix != null) builder.append(suffix);
        }
        return builder.toString();
    }

    public static String maxToWord(String string, int maxLength, String suffix) {
        StringBuilder builder = new StringBuilder(string);
        if (builder.length() > maxLength) {
            int nextWord = builder.indexOf(" ", maxLength);
            builder.setLength(nextWord);
            if (suffix != null) builder.append(suffix);
        }
        return builder.toString();
    }

    public static String maxToSentence(String string, int maxLength, String suffix) {
        StringBuilder builder = new StringBuilder(string);
        if (builder.length() > maxLength) {
            int nextWord = builder.indexOf(".", maxLength);
            builder.setLength(nextWord);
            if (suffix != null) builder.append(suffix);
        }
        return builder.toString() + ".";
    }

    private static final Pattern NONLATIN = Pattern.compile("[^\\w-]");

    private static final Pattern WHITESPACE = Pattern.compile("[\\s]");

    /**
	 * Turn an input string into a slug for pretty URLs. 
	 * Taken from a stack overflow article: http://stackoverflow.com/questions/1657193/java-code-library-for-generating-slugs-for-use-in-pretty-urls
	 * @param input
	 * @return
	 */
    public static String toSlug(String input) {
        String nowhitespace = WHITESPACE.matcher(input).replaceAll("-");
        String normalized = Normalizer.normalize(nowhitespace, Form.NFD);
        String slug = NONLATIN.matcher(normalized).replaceAll("");
        return slug.toLowerCase(Locale.ENGLISH);
    }

    /**
	 * Silly, dumb method that tries to remove all HTML tags from a string.
	 * @param content
	 * @return
	 */
    public static String stripHTMLTags(String content) {
        return content.replaceAll("\\<.*?>", "");
    }

    public static String formatHTTPDate(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz");
        return format.format(date);
    }

    public static String encodeXMLEntites(String text) {
        text = text.replaceAll("&", "&amp;");
        text = text.replaceAll("\"", "&quot;");
        text = text.replaceAll("'", "&apos;");
        text = text.replaceAll("<", "&lt;");
        text = text.replaceAll(">", "&gt;");
        return text;
    }

    /**
	 * Turn a convert weird characters in a charsequence into HTML hex entities. Taken
	 * from http://stackoverflow.com/questions/1273986/converting-utf-8-to-iso-8859-1-in-java
	 * @param sequence
	 * @param writer
	 * @throws IOException
	 */
    public static void encodeHTMLChars(CharSequence sequence, Writer writer) throws IOException {
        for (int i = 0; i < sequence.length(); i++) {
            char ch = sequence.charAt(i);
            if (Character.UnicodeBlock.of(ch) == Character.UnicodeBlock.BASIC_LATIN) {
                writer.write(ch);
            } else {
                int codepoint = Character.codePointAt(sequence, i);
                i += Character.charCount(codepoint) - 1;
                writer.write("&#x");
                writer.write(Integer.toHexString(codepoint));
                writer.write(";");
            }
        }
    }

    public static String encodeHTMLChars(String string) {
        StringBuffer encoded = new StringBuffer();
        for (int i = 0; i < string.length(); i++) {
            char ch = string.charAt(i);
            if (Character.UnicodeBlock.of(ch) == Character.UnicodeBlock.BASIC_LATIN) {
                encoded.append(ch);
            } else {
                int codepoint = Character.codePointAt(string, i);
                i += Character.charCount(codepoint) - 1;
                encoded.append("&#x");
                encoded.append(Integer.toHexString(codepoint));
                encoded.append(";");
            }
        }
        return encoded.toString();
    }

    public static String encryptMd5(String plaintext) {
        String hashtext = "";
        try {
            MessageDigest m;
            m = MessageDigest.getInstance("MD5");
            m.reset();
            m.update(plaintext.getBytes());
            byte[] digest = m.digest();
            BigInteger bigInt = new BigInteger(1, digest);
            hashtext = bigInt.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return hashtext;
    }

    public static String encodeBase64(String plainString) {
        return Base64.encodeBytes(plainString.getBytes());
    }

    public static String decodeBase64(String encodedString) throws Exception {
        return new String(Base64.decode(encodedString), "UTF-8");
    }

    public static String encodeHex(byte[] binaryData) {
        if (binaryData == null) return null;
        int lengthData = binaryData.length;
        int lengthEncode = lengthData * 2;
        char[] encodedData = new char[lengthEncode];
        int temp;
        for (int i = 0; i < lengthData; i++) {
            temp = binaryData[i];
            if (temp < 0) temp += 256;
            encodedData[i * 2] = lookUpHexAlphabet[temp >> 4];
            encodedData[i * 2 + 1] = lookUpHexAlphabet[temp & 0xf];
        }
        return new String(encodedData);
    }

    public static byte[] decodeHex(String encoded) {
        if (encoded == null) return null;
        int lengthData = encoded.length();
        if (lengthData % 2 != 0) return null;
        char[] binaryData = encoded.toCharArray();
        int lengthDecode = lengthData / 2;
        byte[] decodedData = new byte[lengthDecode];
        byte temp1, temp2;
        char tempChar;
        for (int i = 0; i < lengthDecode; i++) {
            tempChar = binaryData[i * 2];
            temp1 = (tempChar < BASELENGTH) ? hexNumberTable[tempChar] : -1;
            if (temp1 == -1) return null;
            tempChar = binaryData[i * 2 + 1];
            temp2 = (tempChar < BASELENGTH) ? hexNumberTable[tempChar] : -1;
            if (temp2 == -1) return null;
            decodedData[i] = (byte) ((temp1 << 4) | temp2);
        }
        return decodedData;
    }

    public static String replaceTokens(String text, Map<String, String> values) {
        Pattern pattern = Pattern.compile("\\$\\{(.+?)\\}");
        Matcher matcher = pattern.matcher(text);
        StringBuilder builder = new StringBuilder();
        int i = 0;
        while (matcher.find()) {
            String replacement = values.get(matcher.group(1));
            builder.append(text.substring(i, matcher.start()));
            if (replacement == null) builder.append(matcher.group(0)); else builder.append(replacement);
            i = matcher.end();
        }
        builder.append(text.substring(i, text.length()));
        return builder.toString();
    }

    public static String replaceTokens(String text, Object bean) {
        Pattern pattern = Pattern.compile("\\$\\{(.+?)\\}");
        Matcher matcher = pattern.matcher(text);
        StringBuilder builder = new StringBuilder();
        int i = 0;
        while (matcher.find()) {
            Object replacement = "";
            try {
                replacement = BeanUtils.getBeanProperty(bean, matcher.group(1));
            } catch (Exception e) {
            }
            builder.append(text.substring(i, matcher.start()));
            if (replacement == null) builder.append(matcher.group(0)); else builder.append(replacement);
            i = matcher.end();
        }
        builder.append(text.substring(i, text.length()));
        return builder.toString();
    }
}
