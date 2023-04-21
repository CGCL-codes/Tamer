package com.sourcetohtml;

import java.io.File;

/**
 * Generates breadcrumb with links to path of the file
 * @author jcelak
 */
public class BreadcrumGenerator {

    public String generateThumbnail(File root, File aFile) {
        File file = aFile;
        String path = "";
        String result = "";
        if (!file.equals(root)) {
            result = " / " + file.getName();
            if (file.isDirectory()) path = "../";
            file = file.getParentFile();
            while (!file.equals(root)) {
                result = "/ <a href=\"" + path + "index.html\">" + file.getName() + "</a>" + result;
                file = file.getParentFile();
                path += "../";
            }
        }
        return "<div id=\"thumbnail\"><a href=\"" + path + "index.html\">Home</a> " + result + "</div>" + ((aFile.isDirectory()) ? "" : "<a id=\"download\" href=\"" + aFile.getName() + "\">Download original file</a>");
    }
}
