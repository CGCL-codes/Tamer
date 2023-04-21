package com.restfb.util;

import static java.net.URLDecoder.decode;
import static java.net.URLEncoder.encode;
import static java.util.Arrays.asList;
import static java.util.logging.Level.WARNING;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.logging.Logger;

/**
 * A collection of string-handling utility methods.
 * 
 * @author <a href="http://restfb.com">Mark Allen</a>
 * @since 1.6
 */
public final class StringUtils {

    /**
   * Default charset to use for encoding/decoding strings.
   */
    public static final String ENCODING_CHARSET = "UTF-8";

    /**
   * Logger.
   */
    private static final Logger logger = Logger.getLogger(StringUtils.class.getName());

    /**
   * Prevents instantiation.
   */
    private StringUtils() {
    }

    /**
   * Is {@code string} blank (null or only whitespace)?
   * 
   * @param string
   *          The string to check.
   * @return {@code true} if {@code string} is blank, {@code false} otherwise.
   */
    public static boolean isBlank(String string) {
        return string == null || "".equals(string.trim());
    }

    /**
   * Returns a trimmed version of {@code string}, or {@code null} if
   * {@code string} is {@code null} or the trimmed version is a blank string.
   * 
   * @param string
   *          The string to trim.
   * @return A trimmed version of {@code string}, or {@code null} if
   *         {@code string} is {@code null} or the trimmed version is a blank
   *         string.
   */
    public static String trimToNull(String string) {
        if (isBlank(string)) return null;
        return string.trim();
    }

    /**
   * Returns a trimmed version of {@code string}, or an empty string if
   * {@code string} is {@code null} or the trimmed version is a blank string.
   * 
   * @param string
   *          The string to trim.
   * @return A trimmed version of {@code string}, or an empty string if
   *         {@code string} is {@code null} or the trimmed version is a blank
   *         string.
   */
    public static String trimToEmpty(String string) {
        if (isBlank(string)) return "";
        return string.trim();
    }

    /**
   * URL-encodes a string.
   * <p>
   * Assumes {@code string} is in {@value #ENCODING_CHARSET} format.
   * 
   * @param string
   *          The string to URL-encode.
   * @return The URL-encoded version of the input string, or {@code null} if
   *         {@code string} is {@code null}.
   * @throws IllegalStateException
   *           If unable to URL-encode because the JVM doesn't support
   *           {@value #ENCODING_CHARSET}.
   */
    public static String urlEncode(String string) {
        if (string == null) return null;
        try {
            return encode(string, ENCODING_CHARSET);
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException("Platform doesn't support " + ENCODING_CHARSET, e);
        }
    }

    /**
   * URL-decodes a string.
   * <p>
   * Assumes {@code string} is in {@value #ENCODING_CHARSET} format.
   * 
   * @param string
   *          The string to URL-decode.
   * @return The URL-decoded version of the input string, or {@code null} if
   *         {@code string} is {@code null}.
   * @throws IllegalStateException
   *           If unable to URL-decode because the JVM doesn't support
   *           {@value #ENCODING_CHARSET}.
   * @since 1.6.5
   */
    public static String urlDecode(String string) {
        if (string == null) return null;
        try {
            return decode(string, ENCODING_CHARSET);
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException("Platform doesn't support " + ENCODING_CHARSET, e);
        }
    }

    /**
   * Converts {@code string} to a byte array.
   * <p>
   * Assumes {@code string} is in {@value #ENCODING_CHARSET} format.
   * 
   * @param string
   *          The string to convert to a byte array.
   * @return A byte array representation of {@code string}.
   * 
   * @throws NullPointerException
   *           If {@code string} is {@code null}.
   * @throws IllegalStateException
   *           If unable to URL-encode because the JVM doesn't support
   *           {@value #ENCODING_CHARSET}.
   */
    public static byte[] toBytes(String string) {
        if (string == null) throw new NullPointerException("Parameter 'string' cannot be null.");
        try {
            return string.getBytes(ENCODING_CHARSET);
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException("Platform doesn't support " + ENCODING_CHARSET, e);
        }
    }

    /**
   * Builds and returns a string representation of the given {@code inputStream}
   * .
   * 
   * @param inputStream
   *          The stream from which a string representation is built.
   * 
   * @return A string representation of the given {@code inputStream}.
   * @throws IOException
   *           If an error occurs while processing the {@code inputStream}.
   */
    public static String fromInputStream(InputStream inputStream) throws IOException {
        if (inputStream == null) return null;
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(inputStream, ENCODING_CHARSET));
            StringBuilder response = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) response.append(line);
            return response.toString();
        } finally {
            if (reader != null) try {
                reader.close();
            } catch (Throwable t) {
                if (logger.isLoggable(WARNING)) logger.warning("Unable to close stream, continuing on: " + t);
            }
        }
    }

    /**
   * Joins the given {@code array} into a comma-separated string.
   * 
   * @param array
   *          The array to join.
   * @return A comma-separated string representation of the given {@code array}.
   */
    public static String join(String[] array) {
        return array == null ? null : join(asList(array));
    }

    /**
   * Joins the given {@code list} into a comma-separated string.
   * 
   * @param list
   *          The list to join.
   * @return A comma-separated string representation of the given {@code list}.
   */
    public static String join(List<String> list) {
        if (list == null) return null;
        StringBuilder joined = new StringBuilder();
        boolean first = true;
        for (String element : list) {
            if (first) first = false; else joined.append(",");
            joined.append(element);
        }
        return joined.toString();
    }
}
