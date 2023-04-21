package com.dyuproject.protostuff.parser;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

/**
 * Provide ascii text parsing and formatting support for proto2 instances.
 * The implementation largely follows google/protobuf/text_format.cc.
 *
 * @author wenboz@google.com Wenbo Zhu
 * @author kenton@google.com Kenton Varda
 * @author David Yu
 */
public final class TextFormat {

    private TextFormat() {
    }

    static final Charset UTF8 = Charset.forName("UTF-8"), ISO_8859_1 = Charset.forName("ISO-8859-1");

    /**
   * Escapes bytes in the format used in protocol buffer text format, which
   * is the same as the format used for C string literals.  All bytes
   * that are not printable 7-bit ASCII characters are escaped, as well as
   * backslash, single-quote, and double-quote characters.  Characters for
   * which no defined short-hand escape sequence is defined will be escaped
   * using 3-digit octal sequences.
   */
    static StringBuilder escapeBytes(ByteBuffer input) {
        int length = input.limit();
        final StringBuilder builder = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            final byte b = input.get(i);
            switch(b) {
                case 0x07:
                    builder.append("\\007");
                    break;
                case '\b':
                    builder.append("\\b");
                    break;
                case '\f':
                    builder.append("\\f");
                    break;
                case '\n':
                    builder.append("\\n");
                    break;
                case '\r':
                    builder.append("\\r");
                    break;
                case '\t':
                    builder.append("\\t");
                    break;
                case 0x0b:
                    builder.append("\\013");
                    break;
                case '\\':
                    builder.append("\\\\");
                    break;
                case '\'':
                    builder.append("\\\'");
                    break;
                case '"':
                    builder.append("\\\"");
                    break;
                default:
                    if (b >= 0x20) {
                        builder.append((char) b);
                    } else {
                        builder.append('\\');
                        builder.append((char) ('0' + ((b >>> 6) & 3)));
                        builder.append((char) ('0' + ((b >>> 3) & 7)));
                        builder.append((char) ('0' + (b & 7)));
                    }
                    break;
            }
        }
        return builder;
    }

    /**
   * Un-escape a byte sequence as escaped using
   * {@link #escapeBytes(ByteString)}.  Two-digit hex escapes (starting with
   * "\x") are also recognized.
   */
    static ByteBuffer unescapeBytes(final CharSequence input) {
        int pos = 0, len = input.length();
        final byte[] result = new byte[len];
        ByteBuffer buffer = ByteBuffer.wrap(result);
        for (int i = 0; i < len; i++) {
            char c = input.charAt(i);
            if (c == '\\') {
                if (i + 1 < len) {
                    ++i;
                    c = input.charAt(i);
                    if (isOctal(c)) {
                        int code = digitValue(c);
                        if (i + 1 < len && isOctal(input.charAt(i + 1))) {
                            ++i;
                            code = code * 8 + digitValue(input.charAt(i));
                        }
                        if (i + 1 < len && isOctal(input.charAt(i + 1))) {
                            ++i;
                            code = code * 8 + digitValue(input.charAt(i));
                        }
                        result[pos++] = (byte) code;
                    } else {
                        switch(c) {
                            case 'a':
                                result[pos++] = 0x07;
                                break;
                            case 'b':
                                result[pos++] = '\b';
                                break;
                            case 'f':
                                result[pos++] = '\f';
                                break;
                            case 'n':
                                result[pos++] = '\n';
                                break;
                            case 'r':
                                result[pos++] = '\r';
                                break;
                            case 't':
                                result[pos++] = '\t';
                                break;
                            case 'v':
                                result[pos++] = 0x0b;
                                break;
                            case '\\':
                                result[pos++] = '\\';
                                break;
                            case '\'':
                                result[pos++] = '\'';
                                break;
                            case '"':
                                result[pos++] = '\"';
                                break;
                            case 'x':
                                int code = 0;
                                if (i + 1 < len && isHex(input.charAt(i + 1))) {
                                    ++i;
                                    code = digitValue(input.charAt(i));
                                } else {
                                    throw new InvalidEscapeSequenceException("Invalid escape sequence: '\\x' with no digits");
                                }
                                if (i + 1 < len && isHex(input.charAt(i + 1))) {
                                    ++i;
                                    code = code * 16 + digitValue(input.charAt(i));
                                }
                                result[pos++] = (byte) code;
                                break;
                            default:
                                throw new InvalidEscapeSequenceException("Invalid escape sequence: '\\" + c + '\'');
                        }
                    }
                } else {
                    throw new InvalidEscapeSequenceException("Invalid escape sequence: '\\' at end of string.");
                }
            } else {
                result[pos++] = (byte) c;
            }
        }
        buffer.limit(pos);
        return buffer;
    }

    /**
   * Thrown by {@link TextFormat#unescapeBytes} and
   * {@link TextFormat#unescapeText} when an invalid escape sequence is seen.
   */
    static class InvalidEscapeSequenceException extends RuntimeException {

        private static final long serialVersionUID = -8164033650142593305L;

        InvalidEscapeSequenceException(final String description) {
            super(description);
        }
    }

    /**
   * Like {@link #escapeBytes(ByteString)}, but escapes a text string.
   * Non-ASCII characters are first encoded as UTF-8, then each byte is escaped
   * individually as a 3-digit octal escape.  Yes, it's weird.
   */
    static String escapeText(final String input) {
        return escapeBytes(ByteBuffer.wrap(input.getBytes(ISO_8859_1))).toString();
    }

    /**
   * Un-escape a text string as escaped using {@link #escapeText(String)}.
   * Two-digit hex escapes (starting with "\x") are also recognized.
   */
    static String unescapeText(String input) {
        ByteBuffer buffer = unescapeBytes(input);
        return new String(buffer.array(), buffer.position(), buffer.limit(), ISO_8859_1);
    }

    /** Is this an octal digit? */
    private static boolean isOctal(final char c) {
        return '0' <= c && c <= '7';
    }

    /** Is this a hex digit? */
    private static boolean isHex(final char c) {
        return ('0' <= c && c <= '9') || ('a' <= c && c <= 'f') || ('A' <= c && c <= 'F');
    }

    /**
   * Interpret a character as a digit (in any base up to 36) and return the
   * numeric value.  This is like {@code Character.digit()} but we don't accept
   * non-ASCII digits.
   */
    private static int digitValue(final char c) {
        if ('0' <= c && c <= '9') {
            return c - '0';
        } else if ('a' <= c && c <= 'z') {
            return c - 'a' + 10;
        } else {
            return c - 'A' + 10;
        }
    }

    /**
   * Parse a 32-bit signed integer from the text.  Unlike the Java standard
   * {@code Integer.parseInt()}, this function recognizes the prefixes "0x"
   * and "0" to signify hexidecimal and octal numbers, respectively.
   */
    static int parseInt32(final String text) throws NumberFormatException {
        return (int) parseInteger(text, true, false);
    }

    /**
   * Parse a 32-bit unsigned integer from the text.  Unlike the Java standard
   * {@code Integer.parseInt()}, this function recognizes the prefixes "0x"
   * and "0" to signify hexidecimal and octal numbers, respectively.  The
   * result is coerced to a (signed) {@code int} when returned since Java has
   * no unsigned integer type.
   */
    static int parseUInt32(final String text) throws NumberFormatException {
        return (int) parseInteger(text, false, false);
    }

    /**
   * Parse a 64-bit signed integer from the text.  Unlike the Java standard
   * {@code Integer.parseInt()}, this function recognizes the prefixes "0x"
   * and "0" to signify hexidecimal and octal numbers, respectively.
   */
    static long parseInt64(final String text) throws NumberFormatException {
        return parseInteger(text, true, true);
    }

    /**
   * Parse a 64-bit unsigned integer from the text.  Unlike the Java standard
   * {@code Integer.parseInt()}, this function recognizes the prefixes "0x"
   * and "0" to signify hexidecimal and octal numbers, respectively.  The
   * result is coerced to a (signed) {@code long} when returned since Java has
   * no unsigned long type.
   */
    static long parseUInt64(final String text) throws NumberFormatException {
        return parseInteger(text, false, true);
    }

    private static long parseInteger(final String text, final boolean isSigned, final boolean isLong) throws NumberFormatException {
        int pos = 0;
        boolean negative = false;
        if (text.startsWith("-", pos)) {
            if (!isSigned) {
                throw new NumberFormatException("Number must be positive: " + text);
            }
            ++pos;
            negative = true;
        }
        int radix = 10;
        if (text.startsWith("0x", pos)) {
            pos += 2;
            radix = 16;
        } else if (text.startsWith("0", pos)) {
            radix = 8;
        }
        final String numberText = text.substring(pos);
        long result = 0;
        if (numberText.length() < 16) {
            result = Long.parseLong(numberText, radix);
            if (negative) {
                result = -result;
            }
            if (!isLong) {
                if (isSigned) {
                    if (result > Integer.MAX_VALUE || result < Integer.MIN_VALUE) {
                        throw new NumberFormatException("Number out of range for 32-bit signed integer: " + text);
                    }
                } else {
                    if (result >= (1L << 32) || result < 0) {
                        throw new NumberFormatException("Number out of range for 32-bit unsigned integer: " + text);
                    }
                }
            }
        } else {
            BigInteger bigValue = new BigInteger(numberText, radix);
            if (negative) {
                bigValue = bigValue.negate();
            }
            if (!isLong) {
                if (isSigned) {
                    if (bigValue.bitLength() > 31) {
                        throw new NumberFormatException("Number out of range for 32-bit signed integer: " + text);
                    }
                } else {
                    if (bigValue.bitLength() > 32) {
                        throw new NumberFormatException("Number out of range for 32-bit unsigned integer: " + text);
                    }
                }
            } else {
                if (isSigned) {
                    if (bigValue.bitLength() > 63) {
                        throw new NumberFormatException("Number out of range for 64-bit signed integer: " + text);
                    }
                } else {
                    if (bigValue.bitLength() > 64) {
                        throw new NumberFormatException("Number out of range for 64-bit unsigned integer: " + text);
                    }
                }
            }
            result = bigValue.longValue();
        }
        return result;
    }
}
