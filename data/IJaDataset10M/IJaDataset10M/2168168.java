package com.qspin.qtaste.ui.tools;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author vdubois
 */
public class FileSearch {

    private ArrayList<String> searchPaths;

    public FileSearch() {
        searchPaths = new ArrayList<String>();
    }

    public void addSearchPath(String path) {
        searchPaths.add(path);
    }

    public String getFirstFileFound(String fileName) {
        Iterator<String> it = searchPaths.iterator();
        while (it.hasNext()) {
            String path = it.next();
            File file = new File(path + File.separator + fileName);
            if (file.exists()) return path + File.separator + fileName;
        }
        return null;
    }
}
