package com.googlecode.batchfb.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Date;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.googlecode.batchfb.BinaryParam;
import com.googlecode.batchfb.Param;

/**
 * Some string handling utilities
 */
public final class StringUtils {

    /**
	 * Masks the useless checked exception from URLEncoder.encode()
	 */
    public static String urlEncode(String string) {
        try {
            return URLEncoder.encode(string, "utf-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
	 * Masks the useless checked exception from URLDecoder.decode()
	 */
    public static String urlDecode(String string) {
        try {
            return URLDecoder.decode(string, "utf-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
	 * Stringify the parameter value in an appropriate way. Note that Facebook fucks up dates by using unix time-since-epoch
	 * some places and ISO-8601 others. However, maybe unix times always work as parameters?
	 */
    public static String stringifyValue(Param param, ObjectMapper mapper) {
        assert !(param instanceof BinaryParam);
        if (param.value instanceof String) return (String) param.value;
        if (param.value instanceof Date) return Long.toString(((Date) param.value).getTime() / 1000); else if (param.value instanceof Number) return param.value.toString(); else return JSONUtils.toJSON(param.value, mapper);
    }

    /**
	 * Reads an input stream into a String, encoding with UTF-8
	 */
    public static String read(InputStream input) {
        try {
            StringBuilder bld = new StringBuilder();
            Reader reader = new InputStreamReader(input, "utf-8");
            int ch;
            while ((ch = reader.read()) >= 0) bld.append((char) ch);
            return bld.toString();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
