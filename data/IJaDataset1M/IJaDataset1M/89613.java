package com.kyte.api.util;

import com.twmacinta.util.MD5;

/**
 *
 * Created: elliott, Mar 12, 2008
 */
public class ApiUtil {

    public static String generatePasswordHash(String password, String apiSecret) {
        String input = password + apiSecret;
        String hash = new MD5(input).asHex();
        return hash;
    }
}
