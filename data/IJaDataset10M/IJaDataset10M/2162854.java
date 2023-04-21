package com.lonelytaste.narafms.core.Utils;

import java.io.File;

/**
 * <p> Title: [对于配置信息进行初始化,创建目录结构和功能文件夹等]</p>
 * <p> Description: [描述]</p>
 * <p> Created on May 6, 2009</p>
 * <p> Copyright: Copyright (c) 2009</p>
 * <p> Company: </p>
 * @author 苏红胜 - mrsuhongsheng@gmail.com
 * @version 1.0
 */
public class ConfigureManager {

    public static void createDirInitConfigure(String pathName) {
        File file = new File(pathName);
        if (!file.isDirectory()) file = file.getParentFile();
        FileManager.createDir(file);
    }

    public static void createNebulaFMSInitConfigure() {
    }
}
