package org.progeeks.mapview.gmaps;

import java.io.*;
import java.net.*;
import java.util.*;
import org.progeeks.json.*;
import org.progeeks.util.*;
import org.progeeks.util.log.*;
import org.progeeks.mapview.GeoPoint;

/**
 *  Uses google's map service to return geocoding information
 *  for an address.
 *
 *  TODO: Standardize a geocoding interface for this class to
 *  implement.
 *
 *  @version   $Revision: 4203 $
 *  @author    Paul Speed
 */
public class GeoLookup {

    static Log log = Log.getLog();

    public static final String GMAPS_URL = "http://maps.google.com/maps/geo";

    private static JsonParser parser = new JsonParser();

    private String url;

    public GeoLookup() {
        this.url = GMAPS_URL + "?output=json";
    }

    protected Object lookupAddress(String address) throws IOException {
        String encoded = URLEncoder.encode(address, "UTF-8");
        URL fullUrl = new URL(url + "&q=" + encoded);
        URLConnection conn = fullUrl.openConnection();
        InputStream in = conn.getInputStream();
        try {
            Map<String, List<String>> header = conn.getHeaderFields();
            if (log.isTraceEnabled()) log.trace("Header:" + header);
            String contentType = conn.getContentType();
            if (log.isTraceEnabled()) log.trace("Content type:" + contentType);
            String result = StringUtils.readString(new InputStreamReader(in, "UTF-8"));
            if (log.isTraceEnabled()) log.trace("raw result:\n" + result);
            Object o = parser.parse(result);
            if (log.isTraceEnabled()) log.trace("parsed:" + o);
            return o;
        } finally {
            in.close();
        }
    }

    public Map<String, Object> lookupAddressObject(String address) throws IOException {
        Object o = lookupAddress(address);
        if (!(o instanceof Map)) return null;
        return (Map<String, Object>) o;
    }

    public GeoPoint addressObjectToGeoPoint(Map<String, Object> address) {
        Object val = TemplateExpressionProcessor.evaluateTemplateExpression(address, "${Placemark[0].Point.coordinates}");
        if (log.isTraceEnabled()) log.trace("Placemark[0].Point.coordinates = " + val);
        if (val == null || !(val instanceof List)) return null;
        List l = (List) val;
        double d1 = Double.parseDouble(String.valueOf(l.get(0)));
        double d2 = Double.parseDouble(String.valueOf(l.get(1)));
        return new GeoPoint(d1, d2);
    }

    public GeoPoint lookupAddressLocation(String address) throws IOException {
        Object o = lookupAddress(address);
        if (!(o instanceof Map)) return null;
        Object val = TemplateExpressionProcessor.evaluateTemplateExpression(o, "${Placemark[0].Point.coordinates}");
        if (log.isTraceEnabled()) log.trace("Placemark[0].Point.coordinates = " + val);
        if (val == null || !(val instanceof List)) return null;
        List l = (List) val;
        double d1 = Double.parseDouble(String.valueOf(l.get(0)));
        double d2 = Double.parseDouble(String.valueOf(l.get(1)));
        return new GeoPoint(d1, d2);
    }

    public static void main(String[] args) throws Exception {
        Log.initialize();
        GeoLookup gl = new GeoLookup();
        for (String s : args) {
            Object o = gl.lookupAddressLocation(s);
            System.out.println("o:" + o);
        }
    }
}
