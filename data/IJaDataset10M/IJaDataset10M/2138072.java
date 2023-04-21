package gov.sns.tools.apputils;

import java.io.*;
import java.util.regex.*;
import java.net.*;

public class AbsolutePathFinder {

    private String xalName = null;

    private String xalPath = null;

    public AbsolutePathFinder() {
        this("xaldev");
    }

    public AbsolutePathFinder(String xalName) {
        this.xalName = xalName;
        Pattern p = Pattern.compile(System.getProperty("path.separator"));
        String[] path_arr = p.split(System.getProperty("java.class.path"));
        for (int i = 0; i < path_arr.length; i++) {
            if (Pattern.matches(".*" + xalName + ".*", path_arr[i])) {
                p = Pattern.compile(".*" + xalName + "(?:.)");
                Matcher m = p.matcher(path_arr[i]);
                if (m.find()) {
                    xalPath = m.group();
                    break;
                }
            }
        }
    }

    public String getAbsolutePath(String relPathIn) {
        String relPath = fileNameConverter(relPathIn);
        if (xalPath != null) {
            File flIn = new File(xalPath + relPath);
            if (flIn.exists()) {
                return flIn.getAbsolutePath();
            }
        }
        return null;
    }

    public URL getURL(String relPathIn) {
        String relPath = fileNameConverter(relPathIn);
        if (xalPath != null) {
            File flIn = new File(xalPath + relPath);
            if (flIn.exists()) {
                try {
                    return flIn.toURL();
                } catch (MalformedURLException e) {
                }
            }
        }
        return null;
    }

    public String getXALPath() {
        return xalPath;
    }

    private String fileNameConverter(String relPath) {
        Pattern p = Pattern.compile("/");
        String[] parts = p.split(relPath);
        String res = "";
        for (int i = 0; i < parts.length; i++) {
            res = res + System.getProperty("file.separator") + parts[i];
        }
        return res;
    }
}
