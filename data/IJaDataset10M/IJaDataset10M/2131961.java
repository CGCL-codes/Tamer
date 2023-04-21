package com.coyou.spider.config;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.Properties;
import org.apache.log4j.Logger;

public class Config {

    public static int THREAD_NUM = 0;

    public static int PUT_TASK_SPACE_TIME = 0;

    public static String REPORT_USER_URL = "";

    public static boolean IS_MULTI_THREAD = true;

    public static String COMMAND = "";

    public static Properties p = new Properties();

    public static String filepath = "";

    private static final Logger l = Logger.getLogger(Config.class);

    public static void loadP(String configname) {
        URL url = ClassLoader.getSystemResource("");
        filepath = url.getPath();
        if (filepath.contains("bin")) {
            filepath = filepath.replace("bin", "config") + "config.properties";
        } else {
            filepath = filepath + "config/config.properties";
        }
        try {
            p.load(new FileInputStream(filepath));
            l.info("配置就需!");
        } catch (IOException ioe) {
            l.error(ioe.getMessage(), ioe);
            ioe.printStackTrace();
        }
        init();
    }

    public static void init() {
        THREAD_NUM = Integer.parseInt(p.getProperty("THREAD_NUM"));
        REPORT_USER_URL = p.getProperty("REPORT_USER_URL");
        IS_MULTI_THREAD = "true".equalsIgnoreCase(p.getProperty("IS_MULTI_THREAD")) ? true : false;
        PUT_TASK_SPACE_TIME = Integer.parseInt(p.getProperty("PUT_TASK_SPACE_TIME"));
        COMMAND = p.getProperty("COMMAND");
    }

    public static void saveFile() throws IOException {
        OutputStream out = new FileOutputStream(filepath);
        p.store(out, "config");
        init();
        out.flush();
        out.close();
    }
}
