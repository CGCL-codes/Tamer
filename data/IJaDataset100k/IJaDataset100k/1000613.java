package org.databene.commons;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Provides test settings from a file <code>${user.home}/databene.test.properties</code>.<br/>
 * <br/>
 * Created at 01.10.2009 15:30:51
 * @since 0.5.0
 * @author Volker Bergmann
 */
public class DatabeneTestUtil {

    private static final String DATABENE_TEST_PROPERTIES = "databene.test.properties";

    private static final Logger LOGGER = LoggerFactory.getLogger(DatabeneTestUtil.class);

    private static Map<String, String> properties;

    static {
        init();
    }

    private static void init() {
        File file = new File(SystemInfo.getUserHome(), DATABENE_TEST_PROPERTIES);
        if (file.exists()) {
            try {
                properties = IOUtil.readProperties(file.getAbsolutePath());
            } catch (IOException e) {
                LOGGER.error("Error reading " + file.getAbsolutePath(), e);
                createDefaultProperties();
            }
        } else {
            createDefaultProperties();
            try {
                IOUtil.writeProperties(properties, file.getAbsolutePath());
            } catch (Exception e) {
                LOGGER.error("Error writing " + file.getAbsolutePath(), e);
            }
        }
    }

    private static void createDefaultProperties() {
        properties = new HashMap<String, String>();
        properties.put("online", "false");
    }

    public static boolean isOnline() {
        String setting = properties.get("online");
        if (StringUtil.isEmpty(setting)) return false; else return setting.toLowerCase().equals("true");
    }

    public static String ftpDownloadUrl() {
        return properties.get("ftp.download.url");
    }

    public static String ftpUploadUrl() {
        return properties.get("ftp.upload.url");
    }

    public static Map<String, String> getProperties() {
        return properties;
    }
}
