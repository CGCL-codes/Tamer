package org.s3b.search.index;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

/**
 * 
 * @author Sebastian Ryszard Kruk, Sławek Grzonkowski,
 */
public class HtmlToText {

    private static HtmlToText htt = new HtmlToText();

    private HtmlToText() {
    }

    public static HtmlToText getInstance() {
        return htt;
    }

    public String convert(String resourceURI) {
        String _resourceURI = resourceURI;
        if (_resourceURI.startsWith(Statics.FILE_1)) _resourceURI = _resourceURI.substring(6);
        StringBuilder out = new StringBuilder();
        try {
            File f = new File(_resourceURI);
            BufferedReader br = new BufferedReader(new FileReader(f));
            char[] buf = new char[10000];
            boolean skip = false;
            int m, n;
            while (((m = br.read(buf, 0, 10000)) > 0)) {
                for (n = 0; n < m; n++) {
                    if (buf[n] == '<') skip = true; else if (buf[n] == '>') skip = false;
                    if (skip == false && buf[n] != '>') out.append(buf[n]);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return out.toString();
    }
}
