package com.google.code.javascribd.connection;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.UndeclaredThrowableException;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Logger;

final class ScribdParameterUtil {

    private static final Logger LOG = Logger.getLogger(ScribdParameterUtil.class.getName());

    private ScribdParameterUtil() {
    }

    public static String getEncodedParametersForURL(Map<String, String> parameters) {
        StringBuffer sb = new StringBuffer();
        for (String key : parameters.keySet()) {
            sb.append("&");
            sb.append(key);
            sb.append("=");
            String value = parameters.get(key);
            String encodedValue;
            try {
                encodedValue = URLEncoder.encode(value, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                throw new IllegalStateException("UTF-8 is a unknown encoding.", e);
            }
            sb.append(encodedValue);
        }
        return sb.toString();
    }

    public static String calculateApiSig(String secretKey, Map<String, String> parameters) {
        Map<String, String> sortedParameters = new TreeMap<String, String>(parameters);
        String coupledParameters = secretKey;
        coupledParameters += coupleParameters(sortedParameters);
        String md5 = calculateMD5(coupledParameters);
        LOG.finer("md5:" + md5 + "coupledParameters:" + coupledParameters);
        return md5;
    }

    private static String coupleParameters(Map<String, String> parameters) {
        String coupledParameters = "";
        Set<String> keys = parameters.keySet();
        for (String key : keys) {
            String value = parameters.get(key);
            coupledParameters += key + value;
        }
        return coupledParameters;
    }

    private static String calculateMD5(String s) {
        try {
            MessageDigest m = MessageDigest.getInstance("MD5");
            m.reset();
            m.update(s.getBytes());
            return new BigInteger(1, m.digest()).toString(16);
        } catch (NoSuchAlgorithmException e) {
            throw new UndeclaredThrowableException(e);
        }
    }
}
