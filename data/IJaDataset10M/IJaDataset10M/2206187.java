package com.vsimtone.pub.utils.encode;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang.StringEscapeUtils;

/**
 * 各种格式的编码加码工具类.
 * 
 * 集成Commons-Codec,Commons-Lang及JDK提供的编解码方法.
 * 
 * @author calvin
 */
public class EncodeUtils {

    private static final String DEFAULT_URL_ENCODING = "UTF-8";

    /**
	 * Hex编码.
	 */
    public static String hexEncode(byte[] input) {
        return Hex.encodeHexString(input);
    }

    /**
	 * Hex解码.
	 */
    public static byte[] hexDecode(String input) {
        try {
            return Hex.decodeHex(input.toCharArray());
        } catch (DecoderException e) {
            throw new IllegalStateException("Hex Decoder exception", e);
        }
    }

    public static String MD5Encode(String password) {
        MessageDigest messageDigest;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(password.getBytes());
            final byte[] digest = messageDigest.digest();
            final StringBuilder buf = new StringBuilder(digest.length * 2);
            final char[] HEX_DIGITS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
            for (int j = 0; j < digest.length; j++) {
                buf.append(HEX_DIGITS[(digest[j] >> 4) & 0x0f]);
                buf.append(HEX_DIGITS[digest[j] & 0x0f]);
            }
            return buf.toString();
        } catch (NoSuchAlgorithmException e) {
            return password;
        }
    }

    /**
	 * Base64编码.
	 */
    public static String base64Encode(byte[] input) {
        return new String(Base64.encodeBase64(input));
    }

    /**
	 * Base64编码, URL安全(将Base64中的URL非法字符如+,/=转为其他字符, 见RFC3548).
	 */
    public static String base64UrlSafeEncode(byte[] input) {
        return Base64.encodeBase64URLSafeString(input);
    }

    /**
	 * Base64解码.
	 */
    public static byte[] base64Decode(String input) {
        return Base64.decodeBase64(input);
    }

    /**
	 * URL 编码, Encode默认为UTF-8.
	 */
    public static String urlEncode(String input) {
        try {
            return URLEncoder.encode(input, DEFAULT_URL_ENCODING);
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException("Unsupported Encoding Exception", e);
        }
    }

    /**
	 * URL 解码, Encode默认为UTF-8.
	 */
    public static String urlDecode(String input) {
        try {
            return URLDecoder.decode(input, DEFAULT_URL_ENCODING);
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException("Unsupported Encoding Exception", e);
        }
    }

    /**
	 * Html 转码.
	 */
    public static String htmlEscape(String html) {
        return StringEscapeUtils.escapeHtml(html);
    }

    /**
	 * Html 解码.
	 */
    public static String htmlUnescape(String htmlEscaped) {
        return StringEscapeUtils.unescapeHtml(htmlEscaped);
    }

    /**
	 * Xml 转码.
	 */
    public static String xmlEscape(String xml) {
        return StringEscapeUtils.escapeXml(xml);
    }

    /**
	 * Xml 解码.
	 */
    public static String xmlUnescape(String xmlEscaped) {
        return StringEscapeUtils.unescapeXml(xmlEscaped);
    }
}
