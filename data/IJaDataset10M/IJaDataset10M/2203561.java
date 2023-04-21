package net.sipvip.server.utils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class RedFileForServlet {

    private String filename;

    private String content;

    public RedFileForServlet(String name) throws IOException {
        filename = name;
        readContent();
    }

    public void readContent() throws IOException {
        FileInputStream fis = new FileInputStream(filename);
        BufferedReader fc = new BufferedReader(new InputStreamReader(fis, "UTF8"));
        String line = null;
        List<String> bb = new ArrayList<String>();
        while ((line = fc.readLine()) != null) {
            bb.add(line);
        }
        fc.close();
        content = new String(bb.toString());
    }

    public String getContent() {
        return content;
    }
}
