package org.labrad.grapher.client;

import java.util.ArrayList;
import java.util.List;
import com.google.gwt.http.client.URL;

public class TokenParser {

    public static String directoryLink(List<String> path) {
        return pathToToken(path);
    }

    public static String datasetLink(List<String> path, int num) {
        String numStr = "" + num;
        if (num < 10) numStr = "0000" + num; else if (num < 100) numStr = "000" + num; else if (num < 1000) numStr = "00" + num; else if (num < 10000) numStr = "0" + num;
        return pathToToken(path) + numStr;
    }

    public static int datasetFromToken(String token) {
        if (token.endsWith("/")) {
            return 0;
        } else {
            String[] segments = token.split("/");
            return Integer.parseInt(segments[segments.length - 1]);
        }
    }

    public static String pathToToken(List<String> path) {
        StringBuilder sb = new StringBuilder("/");
        for (String segment : path) {
            sb.append(URL.encodeComponent(segment));
            sb.append("/");
        }
        return sb.toString();
    }

    public static List<String> tokenToPath(String token) {
        String[] segments = token.split("/");
        List<String> path = new ArrayList<String>();
        for (String segment : segments) {
            segment = URL.decodeComponent(segment);
            if (!segment.isEmpty()) {
                path.add(URL.decodeComponent(segment));
            }
        }
        return path;
    }
}
