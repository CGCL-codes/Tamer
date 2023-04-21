package com.oki.sample.b2bua.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import javax.servlet.sip.SipServletMessage;

public class SipHeaderUtil {

    private static List<String> systems = new ArrayList<String>(11);

    static {
        systems.add("Call-ID".toLowerCase());
        systems.add("From".toLowerCase());
        systems.add("To".toLowerCase());
        systems.add("CSeq".toLowerCase());
        systems.add("Via".toLowerCase());
        systems.add("Record-Route".toLowerCase());
        systems.add("Route".toLowerCase());
        systems.add("Path".toLowerCase());
        systems.add("Contact".toLowerCase());
        systems.add("RSeq".toLowerCase());
        systems.add("RAck".toLowerCase());
    }

    public static Map<String, List<String>> getHeaderMap(SipServletMessage msg) {
        Map<String, List<String>> headerMap = new HashMap<String, List<String>>(10);
        Iterator<String> headers = msg.getHeaderNames();
        while (headers.hasNext()) {
            String header = headers.next();
            ListIterator<String> values = msg.getHeaders(header);
            List<String> value = new ArrayList<String>();
            while (values.hasNext()) {
                value.add(values.next());
            }
            headerMap.put(header, value);
        }
        return headerMap;
    }

    public static Map<String, List<String>> getHeaderMapIgnoreSystem(SipServletMessage msg) {
        Map<String, List<String>> headerMap = new HashMap<String, List<String>>(10);
        Iterator<String> headers = msg.getHeaderNames();
        while (headers.hasNext()) {
            String header = headers.next();
            if (!isSystemHeader(header)) {
                ListIterator<String> values = msg.getHeaders(header);
                List<String> value = new ArrayList<String>();
                while (values.hasNext()) {
                    value.add(values.next());
                }
                headerMap.put(header, value);
            }
        }
        return headerMap;
    }

    public static void copy(SipServletMessage from, SipServletMessage to) {
        for (Iterator<String> ite = from.getHeaderNames(); ite.hasNext(); ) {
            String name = ite.next();
            if (!isSystemHeader(name.toLowerCase())) {
                to.removeHeader(name);
                for (ListIterator<String> ite2 = from.getHeaders(name); ite2.hasNext(); ) {
                    String value = ite2.next();
                    to.addHeader(name, value);
                }
            }
        }
    }

    public static boolean isSystemHeader(String name) {
        return systems.contains(name);
    }

    public static boolean isHeaderValueExist(SipServletMessage msg, String header, String value) {
        ListIterator<String> it = msg.getHeaders(header);
        if (it != null) {
            while (it.hasNext()) {
                if (it.next().equals(value)) {
                    return true;
                }
            }
        }
        return false;
    }
}
