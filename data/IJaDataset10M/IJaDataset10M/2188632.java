package by.bsuir.picasso.server.util;

import java.util.ArrayList;
import java.util.List;

public class PolylineDecoder {

    public static List<Vertex> decode(String encoded) {
        List<Vertex> decoded = new ArrayList<Vertex>();
        double lat = 0.0;
        double lng = 0.0;
        for (int index = 0; index < encoded.length(); ) {
            int shift = 0;
            int b;
            int result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result = result | (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = (((result & 1) == 1) ? ~(result >> 1) : (result >> 1));
            lat += dlat;
            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result = result | (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = (((result & 1) == 1) ? ~(result >> 1) : (result >> 1));
            lng += dlng;
            decoded.add(new Vertex(lat * 1e-5, lng * 1e-5));
        }
        return decoded;
    }
}
