package org.jinvent.utils;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NmapParser {

    private static Pattern ex = Pattern.compile("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}");

    private static Matcher match;

    public static List<String> ParseString(String nmapText) throws IOException {
        List<String> pingList = new ArrayList<String>();
        InputStream is = new ByteArrayInputStream(nmapText.getBytes("UTF-8"));
        DataInputStream dis = new DataInputStream(is);
        while (dis.available() != 0) {
            String it = dis.readLine();
            match = ex.matcher(it);
            while (match.find()) {
                pingList.add(match.group());
            }
        }
        return pingList;
    }
}
