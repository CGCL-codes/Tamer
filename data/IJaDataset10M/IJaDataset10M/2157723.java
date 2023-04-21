package com.lonelytaste.narafms.core.Utils;

import java.io.File;
import java.io.IOException;
import com.lonelytaste.narafms.core.FmsFileID;

/**
 * <p> Title: [名称]</p>
 * <p> Description: [描述]</p>
 * <p> Created on May 10, 2009</p>
 * <p> Copyright: Copyright (c) 2009</p>
 * <p> Company: </p>
 * @author 苏红胜 - mrsuhongsheng@gmail.com
 * @version 1.0
 */
public class FileManager {

    public static boolean createDir(File file) {
        return file.mkdirs();
    }

    public static File createFile(String fmsRoot, String path) throws IOException {
        System.out.println(fmsRoot + path);
        File file = new File(fmsRoot + path);
        if (!file.exists()) {
            ConfigureManager.createDirInitConfigure(file.getPath());
            file.createNewFile();
        }
        return file;
    }

    public static boolean deleteFile(final FmsFileID fmsfileid) {
        String filename = fmsfileid.getFMSRoot() + FilePathAndIDGenerater.getDIRString(fmsfileid.getId());
        File file = new File(filename);
        if (file.exists()) {
            return file.delete();
        }
        return true;
    }
}
