package jp.co.baka.pachinko.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.Map;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public class RequestUtil {

    public static String getCookeiValue(HttpServletRequest req, String key) {
        String value = null;
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(key)) {
                    try {
                        value = URLDecoder.decode(cookie.getValue(), "UTF-8");
                        break;
                    } catch (UnsupportedEncodingException e) {
                    }
                }
            }
        }
        return value;
    }

    public static String debugRequest(HttpServletRequest req) {
        StringBuffer buf = new StringBuffer();
        buf.append("-----------------------------------");
        buf.append(req.getRequestURI() + "\n");
        buf.append("COOKIE\n");
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                try {
                    buf.append(cookie.getName()).append("=").append(URLDecoder.decode(cookie.getValue(), "UTF-8")).append(" maxAge=").append(cookie.getMaxAge()).append("\n");
                } catch (UnsupportedEncodingException e1) {
                }
            }
        }
        buf.append("request params\n");
        @SuppressWarnings("unchecked") Map<String, Object> reqMap = req.getParameterMap();
        for (Map.Entry<String, Object> e : reqMap.entrySet()) {
            buf.append(e.getKey()).append("=");
            Object value = e.getValue();
            if (value instanceof String) {
                String v = (String) value;
                buf.append(v).append("\n");
            } else if (value instanceof String[]) {
                String[] values = (String[]) value;
                for (String v : values) {
                    buf.append(v).append(",");
                }
                buf.append("\n");
            }
        }
        buf.append("request attributes\n");
        @SuppressWarnings("unchecked") Enumeration<String> reqAttrE = req.getAttributeNames();
        while (reqAttrE.hasMoreElements()) {
            String key = reqAttrE.nextElement();
            buf.append(key).append("=").append(req.getAttribute(key).toString()).append("  (" + req.getAttribute(key).getClass().getCanonicalName() + ")").append("\n");
        }
        return buf.toString();
    }
}
