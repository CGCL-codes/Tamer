package edu.psu.citeseerx.myciteseer.web.utils;

import java.io.*;
import java.util.*;

/**
 * Determines if a string is within the defined set of foul words.
 * @author Isaac Councill
 * @version $Rev: 845 $ $Date: 2008-12-22 12:29:13 -0500 (Mon, 22 Dec 2008) $
 */
public class FoulWordFilter {

    public void setFoulWordList(String foulWordFile) throws IOException {
        readList(foulWordFile);
    }

    private HashSet<String> foulWords = new HashSet<String>();

    protected void readList(String foulWordFile) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(foulWordFile)));
        String line = "";
        while ((line = reader.readLine()) != null) {
            if (line.matches("^\\s*$") || line.charAt(0) == '#') {
                continue;
            }
            line = line.toLowerCase();
            foulWords.add(line);
        }
    }

    public String findFoulWord(String str) {
        if (str == null) return null;
        StringTokenizer st = new StringTokenizer(str);
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            if (foulWords.contains(token.toLowerCase())) {
                return token;
            }
        }
        return null;
    }
}
