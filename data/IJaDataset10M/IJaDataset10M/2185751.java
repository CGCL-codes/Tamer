package com.quikj.application.utilities.postinstall;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * 
 * @author amit
 */
public class BackupFiles {

    /** Creates a new instance of BackupFiles */
    public BackupFiles() {
    }

    public String backup(String base, ScreenPrinterInterface out) {
        try {
            File top = new File(base);
            if (top.isDirectory() == false) {
                return "The top-level folder " + base + " is not a folder";
            }
            String[] suffixes = { ".htm", ".html", ".xml" };
            String[] excludes = { "data/global/www/aceapp/data", "data/global/www/aceapp/doc", "data/global/www/aceapp/license", "src", "sh" };
            ArrayList file_list = new ArrayList();
            FileUtils.list(top, top, suffixes, excludes, file_list);
            Iterator iter = file_list.iterator();
            while (iter.hasNext() == true) {
                String path = (String) iter.next();
                out.println("Renaming file " + path + " to " + path + ".bak");
                FileUtils.copy(path, path + ".bak");
            }
            return null;
        } catch (Exception ex) {
            return "IO error occured while backing up system files";
        }
    }
}
