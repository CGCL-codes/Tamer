package com.luzan.common.nfs.s3;

import org.apache.commons.codec.binary.Base64;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class Utils {

    static final String METADATA_PREFIX = "x-amz-meta-";

    static final String AMAZON_HEADER_PREFIX = "x-amz-";

    static final String ALTERNATIVE_DATE_HEADER = "x-amz-date";

    static final String DEFAULT_HOST = "s3.amazonaws.com";

    static final int SECURE_PORT = 443;

    static final int INSECURE_PORT = 80;

    /**
     * HMAC/SHA1 Algorithm per RFC 2104.
     */
    private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";

    static String makeCanonicalString(String method, String resource, Map headers) {
        return makeCanonicalString(method, resource, headers, null);
    }

    /**
     * Calculate the canonical string.  When expires is non-null, it will be
     * used instead of the Date header.
     */
    static String makeCanonicalString(String method, String resource, Map headers, String expires) {
        StringBuffer buf = new StringBuffer();
        buf.append(method + "\n");
        SortedMap interestingHeaders = new TreeMap();
        if (headers != null) {
            for (Iterator i = headers.keySet().iterator(); i.hasNext(); ) {
                String key = (String) i.next();
                if (key == null) continue;
                String lk = key.toLowerCase();
                if (lk.equals("content-type") || lk.equals("content-md5") || lk.equals("date") || lk.startsWith(AMAZON_HEADER_PREFIX)) {
                    List s = (List) headers.get(key);
                    interestingHeaders.put(lk, concatenateList(s));
                }
            }
        }
        if (interestingHeaders.containsKey(ALTERNATIVE_DATE_HEADER)) {
            interestingHeaders.put("date", "");
        }
        if (expires != null) {
            interestingHeaders.put("date", expires);
        }
        if (!interestingHeaders.containsKey("content-type")) {
            interestingHeaders.put("content-type", "");
        }
        if (!interestingHeaders.containsKey("content-md5")) {
            interestingHeaders.put("content-md5", "");
        }
        for (Iterator i = interestingHeaders.keySet().iterator(); i.hasNext(); ) {
            String key = (String) i.next();
            if (key.startsWith(AMAZON_HEADER_PREFIX)) {
                buf.append(key).append(':').append(interestingHeaders.get(key));
            } else {
                buf.append(interestingHeaders.get(key));
            }
            buf.append("\n");
        }
        int queryIndex = resource.indexOf('?');
        if (queryIndex == -1) {
            buf.append("/" + resource);
        } else {
            buf.append("/" + resource.substring(0, queryIndex));
        }
        if (resource.matches(".*[&?]acl($|=|&).*")) {
            buf.append("?acl");
        } else if (resource.matches(".*[&?]torrent($|=|&).*")) {
            buf.append("?torrent");
        }
        return buf.toString();
    }

    /**
     * Calculate the HMAC/SHA1 on a string.
     *
     * @return Signature
     * @throws NoSuchAlgorithmException If the algorithm does not exist.  Unlikely
     * @throws InvalidKeyException      If the key is invalid.
     */
    static String encode(String awsSecretAccessKey, String canonicalString, boolean urlencode) {
        SecretKeySpec signingKey = new SecretKeySpec(awsSecretAccessKey.getBytes(), HMAC_SHA1_ALGORITHM);
        Mac mac = null;
        try {
            mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Could not find sha1 algorithm", e);
        }
        try {
            mac.init(signingKey);
        } catch (InvalidKeyException e) {
            throw new RuntimeException("Could not initialize the MAC algorithm", e);
        }
        String b64 = new String(Base64.encodeBase64(mac.doFinal(canonicalString.getBytes())));
        if (urlencode) {
            return urlencode(b64);
        } else {
            return b64;
        }
    }

    static String pathForListOptions(String bucket, String prefix, String marker, Integer maxKeys) {
        return pathForListOptions(bucket, prefix, marker, maxKeys, null);
    }

    static String pathForListOptions(String bucket, String prefix, String marker, Integer maxKeys, String delimiter) {
        StringBuffer path = new StringBuffer(bucket);
        path.append("?");
        if (prefix != null) path.append("prefix=" + urlencode(prefix) + "&");
        if (marker != null) path.append("marker=" + urlencode(marker) + "&");
        if (delimiter != null) path.append("delimiter=" + urlencode(delimiter) + "&");
        if (maxKeys != null) path.append("max-keys=" + maxKeys + "&");
        path.deleteCharAt(path.length() - 1);
        return path.toString();
    }

    static String urlencode(String unencoded) {
        try {
            return URLEncoder.encode(unencoded, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Could not url encode to UTF-8", e);
        }
    }

    static XMLReader createXMLReader() {
        try {
            return XMLReaderFactory.createXMLReader();
        } catch (SAXException e) {
            System.setProperty("org.xml.sax.driver", "org.apache.crimson.parser.XMLReaderImpl");
        }
        try {
            return XMLReaderFactory.createXMLReader();
        } catch (SAXException e) {
            throw new RuntimeException("Couldn't initialize a sax driver for the XMLReader");
        }
    }

    /**
     * Concatenates a bunch of header values, seperating them with a comma.
     *
     * @param values List of header values.
     * @return String of all headers, with commas.
     */
    private static String concatenateList(List values) {
        StringBuffer buf = new StringBuffer();
        for (int i = 0, size = values.size(); i < size; ++i) {
            buf.append(((String) values.get(i)).replaceAll("\n", "").trim());
            if (i != (size - 1)) {
                buf.append(",");
            }
        }
        return buf.toString();
    }
}
