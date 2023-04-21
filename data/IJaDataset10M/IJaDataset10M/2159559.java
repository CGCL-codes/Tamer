package com.eric.formatter.view.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.apache.commons.io.FileUtils;

public class FormatterConfiguration {

    public static final String CONFIG_FILE = "config.properties";

    public static final String UI_NLS = "nls";

    public static final String OUPUT_FILE = "output.file";

    public static final String DEFAULT_NLS = "en_US";

    public static final String DEFAULT_OUTPUT_FILENAME = "Formatted_PhoneNumber.txt";

    public static File basePath;

    public static String nls;

    public static String output;

    public static void init() {
        basePath = FileUtils.toFile(Thread.currentThread().getContextClassLoader().getResource(""));
        String realConfig = basePath.getAbsolutePath() + File.separator + "config" + File.separator + CONFIG_FILE;
        InputStream is = null;
        Properties prop = null;
        try {
            is = new FileInputStream(realConfig);
            prop = new Properties();
            prop.load(is);
            nls = prop.getProperty(UI_NLS);
            output = prop.getProperty(OUPUT_FILE);
        } catch (Exception e) {
        } finally {
            verify();
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
            }
        }
    }

    private static void verify() {
        if (nls == null || "".equals(nls) || (!nls.equals("en_US") && !nls.equals("ch_CN"))) {
            nls = DEFAULT_NLS;
        }
        if (output == null || "".equals(output)) {
            String usrDir = System.getProperty("user.dir");
            output = usrDir + File.separator + "output" + File.separator + DEFAULT_OUTPUT_FILENAME;
        }
    }
}
