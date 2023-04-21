package com.zildo;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import zildo.fwk.file.EasyBuffering;
import zildo.fwk.file.FileUtil;

/**
 * @author Tchegito
 * 
 */
public class AndroidFileUtil implements FileUtil {

    @Override
    public EasyBuffering openFile(String path) {
        return new AndroidReadingFile(path);
    }

    @Override
    public File[] listFiles(String path, FilenameFilter filter) {
        List<File> files = new ArrayList<File>();
        try {
            String[] strFiles = AndroidReadingFile.assetManager.list("resources" + File.separator + "saves");
            for (String s : strFiles) {
                System.out.println(s);
                files.add(new File(s));
            }
        } catch (IOException e) {
            System.out.println("Error reading folder " + path);
        }
        return files.toArray(new File[] {});
    }
}
