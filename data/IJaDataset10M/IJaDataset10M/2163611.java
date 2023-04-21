package uk.co.altv.simpledb.coder;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.logging.Logger;

/**
 * @author niki
 * 
 */
public class SimpleURLCoder implements ICoder {

    private static final Logger log = Logger.getLogger(SimpleURLCoder.class.getName());

    public boolean canDecode(Object source) {
        if (source instanceof String) {
            return true;
        }
        return false;
    }

    public boolean canEncode(Object source) {
        if (source instanceof String) {
            return true;
        }
        return false;
    }

    public Object decode(Object source) {
        if (((String) source).equals("")) {
            return "";
        }
        try {
            return URLDecoder.decode(((String) source).replace("~", "%7E").replace("%2A", "*").replace("%20", "+"), "utf-8");
        } catch (UnsupportedEncodingException e) {
            log.warning("URLDecoder says can't do utf-8 when trying to decode value :" + (String) source);
            throw new RuntimeException(e);
        }
    }

    public Object encode(Object source) {
        if (((String) source).equals("")) {
            return "";
        }
        try {
            return URLEncoder.encode((String) source, "utf-8").replace("+", "%20").replace("*", "%2A").replace("%7E", "~");
        } catch (UnsupportedEncodingException e) {
            log.warning("URLEncoder says can't do utf-8 when trying to encode value :" + (String) source);
            throw new RuntimeException(e);
        }
    }
}
