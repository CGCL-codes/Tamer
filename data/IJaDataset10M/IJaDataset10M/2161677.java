package org.nightlabs.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * This implementation of {@link ParameterCoder} uses an {@link URLEncoder} and {@link URLDecoder}
 * to encode/decode.
 *
 * @author Marius Heinzmann -- Marius[at]NightLabs[dot]de
 */
public class ParameterCoderURL implements ParameterCoder {

    public String encode(String plain) {
        try {
            return URLEncoder.encode(plain, IOUtil.CHARSET_NAME_UTF_8);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Fucking shit! How the hell can it happen that UTF-8 is not supported?!?!?!!??!???!");
        }
    }

    public String decode(String encoded) {
        try {
            return URLDecoder.decode(encoded, IOUtil.CHARSET_NAME_UTF_8);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Fucking shit! How the hell can it happen that UTF-8 is not supported?!?!?!!??!???!");
        }
    }
}
